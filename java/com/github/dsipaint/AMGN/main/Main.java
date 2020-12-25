package com.github.dsipaint.AMGN.main;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.security.auth.login.LoginException;

import org.json.simple.DeserializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.closenetwork.CloseListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.consistency.ModlogsListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.consistency.ModroleListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.controlEnableDisable.DisableListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.controlEnableDisable.EnableListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.help.HelpListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.metadata.MetaUpdateListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.metadata.MetaViewListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.operators.OpAddListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.operators.OpRemoveListener;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.running.RunningListener;
import com.github.dsipaint.AMGN.io.IOHandler;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Main
{
	public static JDA jda;
	static Logger logger = LoggerFactory.getLogger("AMGN"); //logger
	
	//TODO: add console command support
	
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
			e.printStackTrace();
		}
		
		logger.info("Initialising bot account...");
		try
		{
			jda = new JDABuilder(AccountType.BOT).setToken(token).build();
		}
		catch (LoginException e1)
		{
			e1.printStackTrace();
			if(jda != null)
				jda.shutdownNow();
			
			System.exit(0); //exit program if invalid token
		}
		
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
				File[] plugins_directory = plugins.listFiles((path) ->
				{
					if(path.getName().endsWith(".jar"))
						return true;
						
					return false;
				});
				
				//loop through these jars
				for(File file : plugins_directory)
				{
					//look for the actual plugin class here (if it exists)
					Plugin p = IOHandler.getPluginObjectFromJar(file);
					
					//plugins must have a name
					if(p.getName() == null)
					{
						logger.info("Plugin " + file.getPath() + " has no name, skipping...");
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
		
		
		logger.info("Implementing intrinsic features..."); //prebuilt commands/features of the library go here

		//help feature
		jda.addEventListener(new HelpListener());
		
		//enable/disable plugin
		jda.addEventListener(new EnableListener());
		jda.addEventListener(new DisableListener());
		
		//closenetwork plugin
		jda.addEventListener(new CloseListener());
		
		//metadata plugin
		jda.addEventListener(new MetaViewListener());
		jda.addEventListener(new MetaUpdateListener());
		
		//isrunning plugin
		jda.addEventListener(new RunningListener());
		
		//consistency plugin
		jda.addEventListener(new ModlogsListener());
		jda.addEventListener(new ModroleListener());
		
		//operators plugin
		jda.addEventListener(new OpAddListener());
		jda.addEventListener(new OpRemoveListener());
		
		logger.info("Finished setup.");
		//END SETUP
	}
}
