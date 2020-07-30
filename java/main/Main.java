package main;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.security.auth.login.LoginException;

import org.json.simple.DeserializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import entities.plugins.Plugin;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Main
{
	public static JDA jda;
	public static Logger logger = LoggerFactory.getLogger("AMGN"); //logger
	
	//by definition, also acts as a list of all ENABLED plugins as well as a list of their listeners
	public static HashMap<Plugin, ArrayList<ListenerAdapter>> plugin_listeners = new HashMap<Plugin, ArrayList<ListenerAdapter>>();
	
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
			GuildNetwork.guild_data = IOHandler.readGuildData("guilds.json"); //read guild data from guilds.json
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
			//look for the actual plugin class here (if it exists)
			Plugin p = IOHandler.getPluginObjectFromPath(file.getPath());
			
			//enable these classes/plugins with GuildNetwork.enablePlugin
			if(GuildNetwork.enablePlugin(p))
				logger.info("Enabled " + p.getName() + " " + p.getVersion());
		}
		
		logger.info("Implementing intrinsic plugins..."); //prebuilt commands/plugins of the library go here
		
		logger.info("Finished setup.");
		jda.shutdown(); //DEBUG
		//END SETUP
	}
}
