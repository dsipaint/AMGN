package com.github.dsipaint.AMGN;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;

import com.github.dsipaint.AMGN.entities.Guild;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.CommandEvent;
import com.github.dsipaint.AMGN.entities.listeners.IListener;
import com.github.dsipaint.AMGN.entities.listeners.managed.Command;
import com.github.dsipaint.AMGN.entities.listeners.managed.DefaultCommand;
import com.github.dsipaint.AMGN.entities.listeners.managed.ListenerProxy;
import com.github.dsipaint.AMGN.entities.listeners.managed.menu.Menu;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.consistency.MenuDeleteListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.consistency.ModlogsListener;
import com.github.dsipaint.AMGN.io.IOHandler;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

@SpringBootApplication
@SuppressWarnings("unchecked")
public class AMGN
{
	public static JDA bot;
	public static Logger logger = LogManager.getLogger("AMGN");

	public static ArrayList<Menu> menucache = new ArrayList<Menu>();
	
	//by definition, also acts as a list of all ENABLED plugins as well as a list of their listeners
	public static ConcurrentHashMap<Plugin, CopyOnWriteArrayList<IListener>> plugin_listeners;
	public static void main(String[] args)
	{
		//SETUP
		logger.info("Commencing setup...");
		String token = "";
		String membercachepolicy = null;

		List<String> cacheflags;
		List<CacheFlag> parsecacheflags = new ArrayList<CacheFlag>();

		IOHandler.dumperopts = new DumperOptions();
		IOHandler.dumperopts.setIndent(2);
		IOHandler.dumperopts.setDefaultFlowStyle(FlowStyle.BLOCK);

		try
		{
			//by default we use ./web/ for web assets
			if(IOHandler.copyFileToExternalPath("networkdefault.yml", GuildNetwork.NETWORKINFO_PATH))
			{
				logger.warn("network.yml did not exist- made a copy. Please edit this file and restart AMGN to run properly");
				System.exit(0);
			}

			if(IOHandler.copyFileToExternalPath("whitelistdefault.yml", GuildNetwork.WHITELIST_PATH))
				logger.warn("whitelist.yml did not exist- made a copy.");

			if(IOHandler.copyFileToExternalPath("permissionsdefault.yml", GuildNetwork.PERMISSIONS_PATH))
				logger.warn("permissions.yml did not exist- made a copy.");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		MemberCachePolicy parsecachepolicy = null;

		try
		{
			logger.info("Reading token from network settings...");
			token = (String) IOHandler.readYamlData(GuildNetwork.NETWORKINFO_PATH, "token");//read token from network.yml
			if(token == null)
				token = System.getenv("AMGN_TOKEN");

			logger.info("Reading caching settings from network settings...");

			membercachepolicy = (String) IOHandler.readYamlData(GuildNetwork.NETWORKINFO_PATH, "membercachepolicy");
			if(membercachepolicy == null)
				membercachepolicy = GuildNetwork.DEFAULT_MEMBERCACHEPOLICY;


			parsecachepolicy = getMemberCachePolicy(membercachepolicy);
			//parse in "cache_limit" if exists
			Integer cache_limit = (Integer) IOHandler.readYamlData(GuildNetwork.NETWORKINFO_PATH, "cache_limit");
			if(cache_limit != null) //if it does exist, update the cahce policy
				parsecachepolicy = parsecachepolicy.or(MemberCachePolicy.lru(cache_limit));


			cacheflags = (List<String>) IOHandler.readYamlData(GuildNetwork.NETWORKINFO_PATH, "cacheflags");
			if(cacheflags == null)
				cacheflags = GuildNetwork.DEFAULT_CACHEFLAGS;
			for(String flag : cacheflags)
				parsecacheflags.add(CacheFlag.valueOf(flag.toUpperCase()));
		}
		catch (IOException e)
		{
			logger.error("Couldn't read network settings");
			e.printStackTrace();
			System.exit(1); //exit if not able to read network settings
		}
		
		logger.info("Initialising bot account...");
		try
		{
			bot = JDABuilder.createDefault(token)
					.enableIntents(EnumSet.allOf(GatewayIntent.class)) //for now we just ask for all gateway intents- this could be a bad idea?
					.setMemberCachePolicy(parsecachepolicy) //parse in the membercachepolicy from network.yml
					.enableCache(parsecacheflags) //parse in the cacheflags
					.build();
		}
		catch(InvalidTokenException | IllegalArgumentException e1)
		{
			logger.error("Invalid token, shutting down network...");
			e1.printStackTrace();
			if(bot != null)
				bot.shutdownNow();
			
			System.exit(1); //exit program if invalid token
		}
		
		try
		{
			bot.awaitReady();
		}
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}

		try
		{
			logger.info("Reading webpanel settings from network settings- clientid and redirect uri");
			GuildNetwork.clientid = (String) IOHandler.readYamlData(GuildNetwork.NETWORKINFO_PATH, "clientid");
			if(GuildNetwork.clientid == null)
				GuildNetwork.clientid = System.getenv("AMGN_CLIENTID");

			GuildNetwork.clientsecret = (String) IOHandler.readYamlData(GuildNetwork.NETWORKINFO_PATH, "clientsecret");
			if(GuildNetwork.clientsecret == null)
				GuildNetwork.clientsecret = System.getenv("AMGN_CLIENTSECRET");

			GuildNetwork.redirecturi = (String) IOHandler.readYamlData(GuildNetwork.NETWORKINFO_PATH, "redirecturi");
			if(GuildNetwork.redirecturi == null)
				GuildNetwork.redirecturi = System.getenv("AMGN_REDIRECT");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		try
		{
			logger.info("Reading guild data from network settings...");
			GuildNetwork.guild_data = IOHandler.readGuildData(); //read guild data from network.yml
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		logger.info("Adding default guild settings for missing network.yml guilds...");
		bot.getGuilds().forEach(guild ->
		{
			try
			{
				//add default guild data
				if(!GuildNetwork.guild_data.containsKey(guild.getIdLong()))
				{
					logger.warn("Guild " + guild + " has no settings in network.json");
					GuildNetwork.guild_data.put(guild.getIdLong(), new Guild(guild.getIdLong()));
					IOHandler.writeNetworkData(GuildNetwork.guild_data, GuildNetwork.NETWORKINFO_PATH);
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			
		});
		
		logger.info("Implementing intrinsic features..."); //prebuilt commands/features of the library go here
		
		//consistency plugin
		bot.addEventListener(new ModlogsListener());
		bot.addEventListener(new MenuDeleteListener());

		//generic listening for commands/listeners
		bot.addEventListener(new ListenerProxy());
		
		
		logger.info("initialising listener cache...");
		plugin_listeners = new ConcurrentHashMap<Plugin, CopyOnWriteArrayList<IListener>>();

		logger.info("applying whitelist...");
		try
		{
			GuildNetwork.whitelist = (HashMap<String, List<Long>>) IOHandler.readYamlData(GuildNetwork.WHITELIST_PATH, "whitelist");
			GuildNetwork.blacklist = (HashMap<String, List<Long>>) IOHandler.readYamlData(GuildNetwork.WHITELIST_PATH, "blacklist");
			if(GuildNetwork.whitelist == null)
				GuildNetwork.whitelist = new HashMap<String, List<Long>>();
			if(GuildNetwork.blacklist == null)
				GuildNetwork.blacklist = new HashMap<String, List<Long>>();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		try
		{
			logger.info("Finding plugins...");
		
			File plugins = new File(GuildNetwork.PLUGIN_PATH);
			plugins.mkdir(); //folder for plugins
			
			LinkedList<Plugin> loadorder = new LinkedList<Plugin>();
			ArrayList<Plugin> foundplugins = new ArrayList<Plugin>();
			if(!plugins.createNewFile()) //create the folder if one doesn't exist- if it did, do this:
			{
				//a list of only the jars found directly in the plugins directory
				File[] plugins_directory = plugins.listFiles(path -> {return path.getName().endsWith(".jar");});
				
				//loop through these jars
				for(File file : plugins_directory)
				{
					try
					{
						//look for the actual plugin class here (if it exists)
						Plugin p = IOHandler.getPluginObjectFromJar(file);
						
						//plugins must have a name and name must have no whitespace
						if(p.getName() == null || p.getName().matches(".*\\s.*"))
						{
							logger.info("Plugin " + file.getPath() + " has invalid name, skipping...");
							continue;
						}

						foundplugins.add(p);
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			else
				logger.info("No plugins folder found. Created a plugins folder.");

			if(foundplugins.isEmpty())
				logger.info("AMGN has been started with no plugins.");
			else
			{
				logger.info("Applying plugin load order...");
				//check network.yml for a load order
				//this should be a list of strings in the order we want to load
				ArrayList<String> parseloadorder = (ArrayList<String>) IOHandler.readYamlData(GuildNetwork.NETWORKINFO_PATH, "load_order");
				LinkedList<String> userloadorder;
				if(parseloadorder != null)
					userloadorder = new LinkedList<String>(parseloadorder);
				else
					userloadorder = new LinkedList<String>();
				
				// if(userloadorder == null)
				// 	userloadorder = new LinkedList<String>(); //default to no order if value is missing
				
				//iterate through user's requested loading order
				for(String pluginname : userloadorder)
				{
					//if we have a plugin from the order that we found,
					for(int i = 0; i < foundplugins.size(); i++)
					{
						if(foundplugins.get(i).getName().equalsIgnoreCase(pluginname))
						{
							//remove that plugin from our list of plugins
							//and add that plugin to the load order
							loadorder.add(foundplugins.remove(i));
							break;
						}
					}
				}

				//then just add whatever is left, onto the load order, we don't care what order that bit is in
				loadorder.addAll(foundplugins);

				//finally, enable all those plugins
				logger.info("Enabling plugins...");
				for(Plugin p : loadorder)
				{
					logger.info("Enabling " + p.getName() + " " + p.getVersion());
					//enable these classes/plugins with GuildNetwork.enablePlugin
					try
					{
						GuildNetwork.enablePlugin(p);
					}
					catch(Exception e)
					{
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						e.printStackTrace(pw);

						logger.error("Error occurred enabling plugin " + p.getName() + " " + p.getVersion() + ":\n"
							+ sw.toString());
						continue;
					}
				}
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		logger.info("Setting up webpanel...");

		try
		{
			File webdir = new File(GuildNetwork.WEB_PATH);
			if(!webdir.exists())
				webdir.mkdir();

			//extract any missing web assets to external location
			File jarfile = new File(new AMGN().getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
			JarFile jarinstance = new JarFile(jarfile);
			Enumeration<JarEntry> entries = jarinstance.entries();
			while(entries.hasMoreElements())
			{
				JarEntry entry = entries.nextElement();
				//extract internal resources held in web/ directory
				if(entry.getName().startsWith("web/") && !entry.getName().equals("web/"))
				{
					//copy these if they are missing, to ./web/
					String plainfilename = entry.getName().replace("web/", "");
					if(IOHandler.copyFileToExternalPath(entry.getName(), GuildNetwork.WEB_PATH + "/" + plainfilename))
						logger.info(plainfilename + " did not exist- copied to " + GuildNetwork.WEB_PATH);
				}
			}

			jarinstance.close();
		}
		catch(IOException | URISyntaxException e)
		{
			e.printStackTrace();
		}

		Boolean use_web = true;
		try
		{
			use_web = (Boolean) IOHandler.readYamlData(GuildNetwork.NETWORKINFO_PATH, "use_webpanel");
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}

		if(use_web == null || use_web)
			SpringApplication.run(AMGN.class, args);
		
		//log in modlogs
		GuildNetwork.guild_data.keySet().forEach(guild_id -> {
			GuildNetwork.sendToModlogs(guild_id, "Network started");
		});

		logger.info("Finished setup.");
		//END SETUP
	}

	public static final void runCommand(String cmdtxt, Member member, TextChannel tc)
	{
		AMGN.logger.info("Running command \"" + cmdtxt + "\"" + " as member " + member.toString()
			+ (tc != null ? " in channel " + tc.toString() : ""));

		String[] args = cmdtxt.split(" ");

		boolean[] cmd_found = {false}; //written like this for pass-by-reference to use in the lambda below
		
		//check and run default commands
		for(DefaultCommand cmd : DefaultCommand.values())
		{
			if(args[0].equalsIgnoreCase(cmd.getLabel()))
			{
				cmd_found[0] = true;
				if(cmd.hasPermission(member))
					cmd.getCommandAction().accept(new CommandEvent(cmdtxt, member, tc, null));
				else
					AMGN.logger.warn("Member " + member.toString()
						+ " does not have permission to run command \"" + cmdtxt + "\"");
			}
		}

		AMGN.plugin_listeners.forEach((plugin, listeners) ->
		{
			if(tc != null && !ListenerProxy.applyWhitelistBlacklistRules(plugin.getName(), tc.getGuild()))
			{
				AMGN.logger.info("Network whitelist/blacklist rules do not allow the command \"" + cmdtxt + "\""
					+ " to be run in the guild " + tc.getGuild().toString());
				return;
			}

			for(IListener listener : listeners)
			{
				if(listener instanceof Command)
				{
					Command cmd = ((Command) listener);
					if(args[0].equalsIgnoreCase(cmd.getLabel()))
					{
						cmd_found[0] = true;
						if(cmd.hasPermission(member))
							cmd.onCommand(new CommandEvent(cmdtxt, member, tc, null));
						else
							AMGN.logger.warn("Member " + member.toString()
								+ " does not have permission to run command \"" + cmdtxt + "\"");
					}
				}
			}
		});

		if(!cmd_found[0])
			AMGN.logger.warn("Command " + args[0] + " was not found, so the command \""
				+ cmdtxt + "\" was not executed");
	}

	public static final MemberCachePolicy getMemberCachePolicy(String policy)
	{
		switch(policy.toLowerCase())
		{
			case "all":
				return MemberCachePolicy.ALL;

			case "booster":
				return MemberCachePolicy.BOOSTER;

			case "default":
				return MemberCachePolicy.DEFAULT;

			case "none":
				return MemberCachePolicy.NONE;

			case "online":
				return MemberCachePolicy.ONLINE;

			case "owner":
				return MemberCachePolicy.OWNER;

			case "pending":
				return MemberCachePolicy.PENDING;

			case "voice":
				return MemberCachePolicy.VOICE;

			default:
				AMGN.logger.error("Caching policy could not be correctly parsed- defaulting to DEFAULT.");
				return MemberCachePolicy.DEFAULT;
		}
	}

	public static String getWorkingDirectory()
	{
		try
		{
			return new File(GuildNetwork.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
		}
		catch(URISyntaxException e)
		{
			e.printStackTrace();
		}

		return ".";
	}
}
