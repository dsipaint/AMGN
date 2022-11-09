package com.github.dsipaint.AMGN.io;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.yaml.snakeyaml.Yaml;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.Guild;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

public class IOHandler
{
	
	
	/** 
	 * @param path a path to the network.yml file
	 * @return HashMap<Long, Guild> a map of guild ids with a Guild object containing their network data
	 * @throws FileNotFoundException if network.yml is missing
	 */
	//reads data in from network.yml, using default values if not found (see default values in GuildNetwork.java
	@SuppressWarnings("unchecked")
	public static final HashMap<Long, Guild> readGuildData(String path) throws FileNotFoundException
	{
		ArrayList<Object> guilds_data = (ArrayList<Object>) readYamlData(GuildNetwork.NETWORKINFO_PATH, "guild_data");
		HashMap<Long, Guild> guilds_out = new HashMap<Long, Guild>();

		guilds_data.forEach(obj ->
		{
			Map<String, Object> guild = (Map<String, Object>) obj;

			/*
				if the id number is too big to be an int, it will be parsed as a long
			  	otherwise, it will be parsed as an int
			  	all IDs are guaranteed to be longs, so parse them as longs- test data crashes code because test IDs are ints and not longs
			  	in practice, all IDs should be longs and this crash should not occur- if you are getting a crash related to
			  	converting ints to longs, look at this code
			*/
			
			Guild parsed_guild_obj = new Guild(
				(long) (guild.getOrDefault("guild_id", Guild.DEFAULT_ID)),
				(long) guild.getOrDefault("modlogs", Guild.DEFAULT_ID),
				(long) guild.getOrDefault("modrole", Guild.DEFAULT_ID),
				(String) guild.getOrDefault("prefix", Guild.DEFAULT_PREFIX)
				);
			guilds_out.put(parsed_guild_obj.getGuild_id(), parsed_guild_obj);

			if(parsed_guild_obj.getGuild_id() == Guild.DEFAULT_ID)
				AMGN.logger.warn("There is a missing ID for this guild- in order to fully integrate AMGN with this guild, please specify its id.");
			if(parsed_guild_obj.getModlogs() == Guild.DEFAULT_ID)
			{
				AMGN.logger.warn("Modlogs ID is missing for " 
					+ (parsed_guild_obj.getGuild_id() == Guild.DEFAULT_ID ? "this guild" : AMGN.bot.getGuildById(parsed_guild_obj.getGuild_id()))
					+ ". In order to use modlogs features for this guild with AMGN, please set a value for the modlogs with the updatemetainfo"
					+ " command or by editing " + GuildNetwork.NETWORKINFO_PATH + ".");
			}
			if(parsed_guild_obj.getModrole() == Guild.DEFAULT_ID)
			{
				AMGN.logger.warn("Modrole ID is missing for " 
					+ (parsed_guild_obj.getGuild_id() == Guild.DEFAULT_ID ? "this guild" : AMGN.bot.getGuildById(parsed_guild_obj.getGuild_id()))
					+ ". In order to use modrole features for this guild with AMGN, please set a value for the modrole with the updatemetainfo"
					+ " command or by editing " + GuildNetwork.NETWORKINFO_PATH + ".");
			}
		});
		
		return guilds_out;
	}
	
	
	/** 
	 * @param path
	 * @return String
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static final Object readYamlData(String path, String value) throws FileNotFoundException
	{
		return ((HashMap<String, Object>) new Yaml().load(new FileReader(new File(path)))).get(value);
	}
	
	
	/** 
	 * @param guilds
	 * @param operators
	 * @param path
	 */
	//writes data to a file from a valid hashmap (i.e. GuildNetwork.guild_data)
	public static final void writeNetworkData(Map<Long, Guild> guilds, List<Long> operators, String path) throws IOException
	{
		Yaml yaml_out = new Yaml();
		Map<String, Object> parse_objects = new HashMap<String, Object>();

		//parse token first
		parse_objects.put("token", AMGN.bot.getToken().replace("Bot ", ""));
		//then parse operators
		parse_objects.put("operators", operators);
		//then parse guild data
		List<Map<String, Object>> parse_guilds = new ArrayList<Map<String, Object>>();
		guilds.values().forEach(guild ->
		{
			//we now don't write default ids out
			//missing values are parsed in as default values
			//and any values that aren't longs are simply
			//treated as invalid, even if they are the default id

			Map<String, Object> guild_list_obj = new HashMap<String, Object>();
			if(guild.getGuild_id() != Guild.DEFAULT_ID)
				guild_list_obj.put("guild_id", guild.getGuild_id());
			if(guild.getModlogs() != Guild.DEFAULT_ID)
				guild_list_obj.put("modlogs", guild.getModlogs());
			if(guild.getModrole() != Guild.DEFAULT_ID)
				guild_list_obj.put("modrole", guild.getModrole());

			guild_list_obj.put("prefix", guild.getPrefix());

			parse_guilds.add(guild_list_obj);
		});
		parse_objects.put("guild_data", parse_guilds);
		//TODO: use tidier yaml dump settings (see customcommands)
		yaml_out.dump(parse_objects, new FileWriter(new File(path)));
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
					//if a valid plugin i.e. has a plugin.yml
					if(currententry.getName().equals("plugin.yml"))
					{
						URL url = new URL("jar:file:" + jar.getAbsolutePath() + "!/plugin.yml");
						JarURLConnection jarcon = (JarURLConnection) url.openConnection();
						Map<String, Object> pluginyml = new Yaml().load(jarcon.getInputStream());
						//check that the name of the plugin is our name
						if(((String) pluginyml.get("name")).equalsIgnoreCase(name))
                            return true; //if so, the plugin does exist
					}
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

	//copy a file from inside the jar, to a path outside the jar
	//returns false if file already exists and was not copied
	//returns true if file was copied
	public static final boolean copyFileToExternalPath(String internalpath, String externalpath) throws IOException
	{
		ClassLoader cl = new IOHandler().getClass().getClassLoader();
		InputStream inputstream = cl.getResourceAsStream(internalpath);

		File copyFile = new File(externalpath);

		try
		{
			Files.copy(inputstream, copyFile.toPath());
		}
		catch(FileAlreadyExistsException e)
		{
			return false;
		}

		return true;
	}
}
