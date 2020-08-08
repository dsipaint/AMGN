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
import com.github.dsipaint.AMGN.io.IOHandler;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Main
{
	public static JDA jda;
	static Logger logger = LoggerFactory.getLogger("AMGN"); //logger
	
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
			System.exit(0); //exit program if invalid token
		}
		
		try
		{
			jda.awaitReady();
		}
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
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
					Plugin p = IOHandler.getPluginObjectFromPath(file.getPath());
					
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
		
		
		logger.info("Implementing intrinsic plugins..."); //prebuilt commands/plugins of the library go here

		//help plugin
		Plugin helpinstance = IOHandler.getPluginObjectFromPath("com.github.dsipaint.AMGN.entities.plugins.intrinsic.help.Help");
		GuildNetwork.enablePlugin(helpinstance);
		
		//enable/disable plugin
		Plugin enabledisableinstance = IOHandler.getPluginObjectFromPath("com.github.dsipaint.AMGN.entities.plugins.intrinsic.controlEnableDisable.ControlEnableDisable");
		GuildNetwork.enablePlugin(enabledisableinstance);
		
		//closenetwork plugin
		Plugin closenetworkinstance = IOHandler.getPluginObjectFromPath("com.github.dsipaint.AMGN.entities.plugins.intrinsic.closenetwork.CloseNetwork");
		GuildNetwork.enablePlugin(closenetworkinstance);
		
		//metadata plugin
		Plugin metadatainstance = IOHandler.getPluginObjectFromPath("com.github.dsipaint.AMGN.entities.plugins.intrinsic.metadata.Metadata");
		GuildNetwork.enablePlugin(metadatainstance);
		
		//isrunning plugin
		Plugin isrunninginstance = IOHandler.getPluginObjectFromPath("com.github.dsipaint.AMGN.entities.plugins.intrinsic.running.Running");
		GuildNetwork.enablePlugin(isrunninginstance);
		
		//consistency plugin
		Plugin consistencyinstance = IOHandler.getPluginObjectFromPath("com.github.dsipaint.AMGN.entities.plugins.intrinsic.consistency.Consistency");
		GuildNetwork.enablePlugin(consistencyinstance);
		
		//operators plugin
		Plugin operatorsplugin = IOHandler.getPluginObjectFromPath("com.github.dsipaint.AMGN.entities.plugins.intrinsic.operators.Operators");
		GuildNetwork.enablePlugin(operatorsplugin);
		
		logger.info("Finished setup.");
		//END SETUP
	}
}
