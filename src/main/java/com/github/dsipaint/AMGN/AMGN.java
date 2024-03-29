package com.github.dsipaint.AMGN;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;

import com.github.dsipaint.AMGN.entities.Guild;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.Command;
import com.github.dsipaint.AMGN.entities.listeners.CommandEvent;
import com.github.dsipaint.AMGN.entities.listeners.Listener;
import com.github.dsipaint.AMGN.entities.listeners.ListenerWrapper;
import com.github.dsipaint.AMGN.entities.listeners.menu.Menu;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.closenetwork.CloseListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.consistency.MenuDeleteListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.consistency.ModlogsListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.controlEnableDisable.DisableListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.controlEnableDisable.EnableListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.controlEnableDisable.ReloadListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.help.HelpListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.metadata.MetaUpdateListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.metadata.MetaViewListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.permissions.GroupsListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.permissions.ListPermissionsListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.permissions.PermissionAddRemoveListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.running.RunningAllListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.running.RunningListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.whitelist.BlacklistCommand;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.whitelist.WhitelistCommand;
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
	public static Logger logger = LoggerFactory.getLogger("AMGN"); //logger

	public static ArrayList<Menu> menucache = new ArrayList<Menu>();
	
	//by definition, also acts as a list of all ENABLED plugins as well as a list of their listeners
	public static HashMap<Plugin, ArrayList<Listener>> plugin_listeners;
	public static void main(String[] args)
	{
		//SETUP
		logger.info("Commencing setup...");
		String token = "";

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
			{
				logger.warn("whitelist.yml did not exist- made a copy. Please edit this file and restart AMGN to run properly");
				System.exit(0);
			}

			if(IOHandler.copyFileToExternalPath("permissionsdefault.yml", GuildNetwork.PERMISSIONS_PATH))
			{
				logger.warn("permissions.yml did not exist- made a copy. Please edit this file and restart AMGN to run properly");
				System.exit(0);
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		try
		{
			logger.info("Reading token from network settings...");
			token = (String) IOHandler.readYamlData(GuildNetwork.NETWORKINFO_PATH, "token");//read token from network.yml
			if(token == null)
				token = System.getenv("AMGN_TOKEN");
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
					.enableIntents(EnumSet.allOf(GatewayIntent.class))
					.setMemberCachePolicy(MemberCachePolicy.ALL)
					.enableCache(CacheFlag.ONLINE_STATUS,
						CacheFlag.ACTIVITY) //honestly I just need these here for one plugin rn
					.build(); //TODO: change to a plugin-specific gateway intent system
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
			logger.info("Reading operators and guild data from network settings...");
			GuildNetwork.guild_data = IOHandler.readGuildData(); //read guild data from network.yml
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		logger.info("Loading guild members, adding default guild settings for missing network.yml guilds...");
		bot.getGuilds().forEach(guild ->
		{
			guild.loadMembers();//good idea???
			//add default guild data NOTE: this won't save this data to network.yml- this must be done manually if you want actual values
			if(!GuildNetwork.guild_data.containsKey(guild.getIdLong()))
			{
				logger.warn("Guild " + guild + " has no settings in network.json- using default values-this won't save this data to network.json- this must be done manually if you want actual values");
				GuildNetwork.guild_data.put(guild.getIdLong(), new Guild(guild.getIdLong()));
			}
			
		});
		
		logger.info("Implementing intrinsic features..."); //prebuilt commands/features of the library go here

		//help feature
		bot.addEventListener(new HelpListener());
		
		//enable/disable plugin
		bot.addEventListener(new EnableListener());
		bot.addEventListener(new DisableListener());
		bot.addEventListener(new ReloadListener());
		
		//closenetwork plugin
		bot.addEventListener(new CloseListener());
		
		//metadata plugin
		bot.addEventListener(new MetaViewListener());
		bot.addEventListener(new MetaUpdateListener());
		
		//isrunning plugin
		bot.addEventListener(new RunningListener());
		bot.addEventListener(new RunningAllListener());
		
		//consistency plugin
		bot.addEventListener(new ModlogsListener());
		bot.addEventListener(new MenuDeleteListener());

		//whitelist plugin
		bot.addEventListener(new BlacklistCommand());
		bot.addEventListener(new WhitelistCommand());

		//permissions plugin
		bot.addEventListener(new PermissionAddRemoveListener());
		bot.addEventListener(new ListPermissionsListener());
		bot.addEventListener(new GroupsListener());

		bot.addEventListener(new ListenerWrapper());
		
		
		logger.info("initialising listener cache...");
		plugin_listeners = new HashMap<Plugin, ArrayList<Listener>>();

		logger.info("applying whitelist...");
		try
		{
			GuildNetwork.whitelist = (HashMap<String, List<Long>>) IOHandler.readYamlData("whitelist.yml", "whitelist");
			GuildNetwork.blacklist = (HashMap<String, List<Long>>) IOHandler.readYamlData("whitelist.yml", "blacklist");
			if(GuildNetwork.whitelist == null)
				GuildNetwork.whitelist = new HashMap<String, List<Long>>();
			if(GuildNetwork.blacklist == null)
				GuildNetwork.blacklist = new HashMap<String, List<Long>>();
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		logger.info("Enabling plugins...");
		
		File plugins = new File(GuildNetwork.PLUGIN_PATH);
		plugins.mkdir(); //folder for plugins
		
		try
		{
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
						
						//enable these classes/plugins with GuildNetwork.enablePlugin
						if(GuildNetwork.enablePlugin(p))
							logger.info("Enabled " + p.getName() + " " + p.getVersion());
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
			else
				logger.info("No plugins folder found. Created a plugins folder.");
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
		AMGN.plugin_listeners.values().forEach(listeners ->
		{
			listeners.forEach(listener ->
			{
				if(listener instanceof Command)
				{
					Command cmd = ((Command) listener);
					String[] args = cmdtxt.split(" ");
					if(args[0].equalsIgnoreCase(cmd.getLabel())
						&& cmd.hasPermission(member))
						cmd.onCommand(new CommandEvent(cmdtxt, member, tc, null));
				}
			});
		});	
	}
}
