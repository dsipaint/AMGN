package io;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.DeserializationException;
import org.json.simple.JsonArray;
import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

import entities.Guild;
import entities.plugins.Plugin;
import main.GuildNetwork;
import main.Main;

public class IOHandler
{
	
	//reads data in from guilds.json, using default values if not found (see default values in GuildNetwork.java
	public static HashMap<Long, Guild> readGuildData(String path) throws FileNotFoundException, IOException, DeserializationException
	{
		JsonArray guilds_in = (JsonArray) Jsoner.deserialize(new FileReader(new File(path)));
		HashMap<Long, Guild> guilds_out = new HashMap<Long, Guild>();
		
		guilds_in.forEach(obj ->
		{
			JsonObject guild = (JsonObject) obj;
			
			/*objects parsed from the jsonarray that do not exist have a default value of null. As for primitive values, 
			 * the toString method of the wrapper class is called, this causes a nullpointerexception if searching for
			 * a primitive value which does not exist e.g. guild.getLong("modrole"), if no "modrole" has been specified
			 * in that part of the json file. Therefore we must use methods such as getLongOrDefault for these cases,
			 * to account for the fact that the values may not be there:
			 */
			Guild g = new Guild(
					guild.getLongOrDefault("guild_id", Guild.DEFAULT_ID),
					guild.getLongOrDefault("modlogs", Guild.DEFAULT_ID),
					guild.getLongOrDefault("modrole", Guild.DEFAULT_ID),
					guild.getStringOrDefault("prefix", Guild.DEFAULT_PREFIX)
					);
			
			guilds_out.put(guild.getLong("guild_id"), g);
		});
		
		return guilds_out;
	}
	
	/*
	 * writes data to a file from a valid hashmap (i.e. GuildNetwork.guild_data)
	 * 
	 * This method no longer depends on Json.simple as it was acting weird-
	 * Json.simple may not also pretty-print, and this allows me to do that at
	 * least, even if it might not be super efficient
	 */
	public static void writeGuildData(Map<Long, Guild> guilds, String path)
	{
		try
		{
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(path))));
			Object[] guild_arr = guilds.values().toArray();
			
			pw.println("[");
			
			//we do last entry manually so we don't add the comma
			for(int i = 0; i < guild_arr.length - 1; i++)
				pw.println(((Guild) guild_arr[i]).asJson("\t") + ",");
			
			pw.println(((Guild) guild_arr[guild_arr.length - 1]).asJson("\t")); //(no comma, as this is the last in the list)
			
			pw.println("]");
			pw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	//returns true if a valid plugin jar has this plugin name
	public static boolean pluginExists(String name)
	{
		//a list of only the jars found directly in the plugins directory
		File[] plugins_directory = new File(GuildNetwork.PLUGIN_PATH).listFiles((path) ->
		{
			if(path.getName().endsWith(".jar"))
				return true;
				
			return false;
		});
		
		for(File jar : plugins_directory)
		{
			File f = new File(jar.getPath() + "plugin.json"); //the right path??
			if(f.exists())
				return true;
		}
		
		return false;
	}
	
	//if a plugin with the given path is ENABLED currently, return true
	public static boolean isEnabled(Plugin plugin)
	{
		//list of active plugins
		if(Main.plugin_listeners.containsKey(plugin))
			return true;
		
		return false;
	}
	
	//return a Plugin representation of a valid path
	public static Plugin getPluginObjectFromPath(String path)
	{
		try
		{
			return (Plugin) Class.forName(path).newInstance();
		}
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}
