package com.github.dsipaint.AMGN.main;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

import javax.security.auth.login.LoginException;

import org.json.simple.DeserializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dsipaint.AMGN.entities.Guild;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.closenetwork.CloseListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.consistency.ModlogsListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.consistency.ModroleListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.controlEnableDisable.DisableListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.controlEnableDisable.EnableListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.controlEnableDisable.ReloadListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.help.HelpListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.metadata.MetaUpdateListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.metadata.MetaViewListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.operators.OpAddListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.operators.OpRemoveListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.running.RunningListener;
import com.github.dsipaint.AMGN.io.IOHandler;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class AMGN
{
	public static JDA bot;
	public static Logger logger = LoggerFactory.getLogger("AMGN"); //logger
	
	//TODO: add config system
	
	//by definition, also acts as a list of all ENABLED plugins as well as a list of their listeners
	public static HashMap<Plugin, ArrayList<ListenerAdapter>> plugin_listeners;
	
	public static void main(String[] args)
	{
		//SETUP
		logger.info("Commencing setup...");
		String token = "";
		try
		{
			logger.info("Reading network settings...");
			GuildNetwork.guild_data = IOHandler.readGuildData(GuildNetwork.NETWORKINFO_PATH); //read guild data from network.json
			GuildNetwork.operators = IOHandler.readOperators(GuildNetwork.NETWORKINFO_PATH); //read operators from network.json
			token = IOHandler.readToken(GuildNetwork.NETWORKINFO_PATH); //read token from network.json
		}
		catch (IOException | DeserializationException e)
		{
			logger.error("Couldn't read network settings");
			e.printStackTrace();
		}
		
		logger.info("Initialising bot account...");
		try
		{
			bot = JDABuilder.createDefault(token)
					.enableIntents(EnumSet.allOf(GatewayIntent.class))
					.setMemberCachePolicy(MemberCachePolicy.ALL)
					.build(); //TODO: change to a plugin-specific gateway intent system
		}
		catch (LoginException e1)
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
		
		logger.info("Loading guild members, adding default guild settings for missing network.json guilds...");
		bot.getGuilds().forEach(guild ->
		{
			guild.loadMembers();//good idea???
			//add default guild data NOTE: this won't save this data to network.json- this must be done manually if you want actual values
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
		
		//consistency plugin
		bot.addEventListener(new ModlogsListener());
		bot.addEventListener(new ModroleListener());
		
		//operators plugin
		bot.addEventListener(new OpAddListener());
		bot.addEventListener(new OpRemoveListener());
		
		
		logger.info("initialising listener cache...");
		plugin_listeners = new HashMap<Plugin, ArrayList<ListenerAdapter>>();
		
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
			}
			else
				logger.info("No plugins folder found. Created a plugins folder.");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		logger.info("Finished setup.");
		//END SETUP
	}
}
