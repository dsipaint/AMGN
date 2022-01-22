package com.github.dsipaint.AMGN.io;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.github.dsipaint.AMGN.entities.Guild;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.main.AMGN;

import org.json.simple.DeserializationException;
import org.json.simple.JsonArray;
import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

public class IOHandler
{
	
	
	/** 
	 * @param path a path to the network.json file
	 * @return HashMap<Long, Guild> a map of guild ids with a Guild object containing their network data
	 * @throws FileNotFoundException if network.json is missing
	 * @throws IOException
	 * @throws DeserializationException if network.json is poorly-written
	 */
	//reads data in from network.json, using default values if not found (see default values in GuildNetwork.java
	public static final HashMap<Long, Guild> readGuildData(String path) throws FileNotFoundException, IOException, DeserializationException
	{
		JsonArray json_in = (JsonArray) ((JsonObject) Jsoner.deserialize(new FileReader(new File(path)))).get("guild_data");
		HashMap<Long, Guild> guilds_out = new HashMap<Long, Guild>();
		
		json_in.forEach(obj ->
		{
			JsonObject guild = (JsonObject) obj;
			
			/*objects parsed from the jsonarray that do not exist have a default value of null. As for primitive values, 
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
	
	
	/** 
	 * @param path
	 * @return List<Long>
	 * @throws FileNotFoundException
	 * @throws DeserializationException
	 * @throws IOException
	 */
	public static final List<Long> readOperators(String path) throws FileNotFoundException, DeserializationException, IOException
	{
		JsonArray ops_in = (JsonArray) ((JsonObject) Jsoner.deserialize(new FileReader(new File(path)))).get("operators");
		List<Long> ops_out = new ArrayList<Long>();
		ops_in.forEach(object ->
		{
			ops_out.add(Long.parseLong(object.toString()));
		});
		
		return ops_out;
	}
	
	
	/** 
	 * @param path
	 * @return String
	 * @throws FileNotFoundException
	 * @throws DeserializationException
	 * @throws IOException
	 */
	public static final String readToken(String path) throws FileNotFoundException, DeserializationException, IOException
	{
		return ((JsonObject) Jsoner.deserialize(new FileReader(new File(path)))).getString("token");
	}
	
	
	/** 
	 * @param guilds
	 * @param operators
	 * @param path
	 */
	/*
	 * writes data to a file from a valid hashmap (i.e. GuildNetwork.guild_data)
	 * 
	 * This method no longer depends on Json.simple as it was acting weird-
	 * Json.simple may not also pretty-print, and this allows me to do that at
	 * least, even if it might not be super efficient
	 */
	public static final void writeNetworkData(Map<Long, Guild> guilds, List<Long> operators, String path)
	{
		try
		{
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(path))));
			Object[] guild_arr = guilds.values().toArray();
			
			pw.println("{");
			
			pw.println("\t\"token\": " + AMGN.bot.getToken() + ",");
			
			pw.print("\t\"operators\": [");
			for(int i = 0; i < operators.size() - 1; i++)
				pw.print(operators.get(i) + ", ");
			
			pw.print(operators.get(operators.size() - 1) + "],\n");
			
			
			pw.println("\t\"guild_data\": [");
			
			//we do last entry manually so we don't add the comma
			for(int i = 0; i < guild_arr.length - 1; i++)
				pw.println(((Guild) guild_arr[i]).asJson("\t\t") + ",");
			
			pw.println(((Guild) guild_arr[guild_arr.length - 1]).asJson("\t\t")); //(no comma, as this is the last in the list)
			
			pw.println("\t]\n");
			pw.println("}");
			
			
			pw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	/** 
	 * @param name
	 * @return boolean
	 */
	//returns true if a valid plugin jar has this plugin name (if the jar physically exists, rather than if it is loaded in)
	public static final boolean pluginExists(String name)
	{
		//a list of only the jars found directly in the plugins directory
		File[] plugins_directory = new File(GuildNetwork.PLUGIN_PATH).listFiles((path) -> {return path.getName().endsWith(".jar");});
		
		for(File jar : plugins_directory)
		{
			try
			{
				JarFile potential_plugin_jar = new JarFile(jar.getPath());
				Enumeration<JarEntry> jar_entries = potential_plugin_jar.entries();
				while(jar_entries.hasMoreElements())
				{
					JarEntry currententry = jar_entries.nextElement();
					if(currententry.getName().equals("plugin.json"))
						return true;
				}
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
		
		return false;
	}
	
	
	/** 
	 * @param plugin
	 * @return boolean
	 */
	//if a plugin with the given path is ENABLED currently, return true
	public static final boolean isEnabled(Plugin plugin)
	{
		//list of active plugins
		if(AMGN.plugin_listeners.containsKey(plugin))
			return true;
		
		return false;
	}
	
	
	/** 
	 * @param path
	 * @return Plugin
	 */
	//return a Plugin representation of a valid path (i.e. an internal path such as com.github.dsipaint.io.IOHandler)
	public static final Plugin getPluginObjectFromPath(String path)
	{
		try
		{
			return (Plugin) Class.forName(path).getDeclaredConstructor().newInstance();
		}
		catch (InstantiationException | IllegalAccessException | ClassNotFoundException |
				IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/** 
	 * @param file
	 * @return Plugin
	 * @throws IOException
	 */
	//quick hack/fix to get actual plugin instance from jar
	public static final Plugin getPluginObjectFromJar(File file) throws IOException
	{
		JarFile jar = new JarFile(file.getPath());
		Enumeration<JarEntry> entries = jar.entries();
		
		URL[] url = {new URL("jar:file:" + file.getAbsolutePath() + "!/")};
		URLClassLoader loader = URLClassLoader.newInstance(url);
		
		while(entries.hasMoreElements())
		{
			JarEntry currententry = entries.nextElement();
			
			if(currententry.isDirectory() || !currententry.getName().endsWith(".class"))
				continue;
			
			String classname = currententry.getName().replace(".class", "")
					.replace("/", ".");
			
			try
			{
				//if the class is an instance of Plugin
				if(Plugin.class.isAssignableFrom(loader.loadClass(classname)))
				{
					jar.close();
					return (Plugin) loader.loadClass(classname).getDeclaredConstructor().newInstance();
				}
			}
			catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
					IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
			{
				e.printStackTrace();
			}
		}
		
		jar.close();
		return null;
	}
}
