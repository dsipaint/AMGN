package main;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.security.auth.login.LoginException;

import org.json.simple.DeserializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import entities.Listener;
import entities.Plugin;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main
{
	public static JDA jda;
	public static Logger logger = LoggerFactory.getLogger("AMGN"); //logger
	
	//by definition, also acts as a list of all ENABLED plugins as well as a list of their listeners
	public static HashMap<Plugin, ArrayList<Listener>> plugin_listeners = new HashMap<Plugin, ArrayList<Listener>>();
	
	public static void main(String[] args)
	{
		//SETUP
		logger.info("Commencing setup...");
		logger.info("Initialising bot account...");
		try
		{
			jda = new JDABuilder(AccountType.BOT).setToken("NjQyODM5OTg5NjQxNjc0NzUy.XccxZA.Ab2rqIsBK-JhsPN6xY00_sQ0hWY").build();
		}
		catch (LoginException e1)
		{
			e1.printStackTrace();
		}
		
		try
		{
			jda.awaitReady();
		}
		catch (InterruptedException e1)
		{
			e1.printStackTrace();
		}
		
		try
		{
			logger.info("Reading guild data...");
			GuildNetwork.guild_data = Data.readGuildData("guilds.json"); //read guild data from guilds.json
		}
		catch (IOException | DeserializationException e)
		{
			e.printStackTrace();
		}
		
		logger.info("Enabling plugins...");
		
		//a list of only the jars found directly in the plugins directory
		File[] plugins_directory = new File(GuildNetwork.PLUGIN_PATH).listFiles((path) ->
		{
			if(path.getName().endsWith(".jar"))
				return true;
				
			return false;
		});
		
		//loop through these jars
		for(File file : plugins_directory)
		{
			//look for the actual plugin class here (if it exists) and then assign it to a plugin object in this program
			//enable these classes/plugins with GuildNetwork.enablePlugin
		}
		
		logger.info("Adding prebuilt commands..."); //prebuilt commands/plugins of the library go here
		
		logger.info("Finished setup.");
		jda.shutdown(); //DEBUG
		//END SETUP
	}
}
