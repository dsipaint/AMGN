package com.github.dsipaint.AMGN.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.yaml.snakeyaml.Yaml;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

import net.dv8tion.jda.api.entities.Guild;

public class Config
{
    /*
        This object controls all plugin operations for a plugin.
        It is designed to make recalling config data more easily, setting and getting config data.
    */
    private Plugin plugin;
    private Yaml yaml = new Yaml(IOHandler.dumperopts);

    public Config(Plugin plugin)
    {
        this.plugin = plugin;
    }

    //can generate files that are stored in the jar- will most commonly be used for template config.ymls
    public final void generateGlobalResource(String filename)
    {
        AMGN.logger.info("Generating resource " + filename + " for plugin " + plugin.getName() + "...");
        //a list of only the jars found directly in the plugins directory
		File[] plugins_directory = new File(GuildNetwork.PLUGIN_PATH).listFiles((path) -> {return path.getName().endsWith(".jar");});
		AMGN.logger.info("Locating plugin...");
		for(File jar : plugins_directory)
		{
			try
			{
				JarFile potential_plugin_jar = new JarFile(jar.getPath());
				Enumeration<JarEntry> jar_entries = potential_plugin_jar.entries();
				while(jar_entries.hasMoreElements())
				{
					JarEntry currententry = jar_entries.nextElement();
                    //if this is a "valid" plugin
					if(currententry.getName().equals("plugin.yml"))
                    {
                        //check that this is our plugin
						URL url = new URL("jar:file:" + jar.getAbsolutePath() + "!/plugin.yml");
						JarURLConnection jarcon = (JarURLConnection) url.openConnection();
                        InputStream pluginyamlin = jarcon.getInputStream();
						Map<String, Object> pluginyml = new Yaml().load(pluginyamlin);
						if(((String) pluginyml.get("name")).equalsIgnoreCase(this.plugin.getName()))
                        {
                            AMGN.logger.info("Located plugin.");
                            File config_dir = new File(plugin.getGlobalConfigPath());
                            
                            if(!config_dir.exists() || !config_dir.isDirectory())
                            {
                                AMGN.logger.info("plugin config directory does not exist- creating...");
                                Files.createDirectories(config_dir.toPath());
                            }

                            AMGN.logger.info("copying resource " + filename + " to " + plugin.getGlobalConfigPath() + "/" + filename);
                            //if so, copy the specified file to the config folder
                            url = new URL("jar:file:" + jar.getAbsolutePath() + "!/" + filename);
                            jarcon = (JarURLConnection) url.openConnection();
                            InputStream newfilein = jarcon.getInputStream();

                            File newfileout = new File(plugin.getGlobalConfigPath() + "/" + filename);
                            Files.copy(newfilein, newfileout.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            newfilein.close();
                            pluginyamlin.close();
                            
                            AMGN.logger.info("Resource " + plugin.getGlobalConfigPath() + "/" + filename + " generated.");
                            return;
                        }

                        pluginyamlin.close();
                    }
				}

                potential_plugin_jar.close();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
    }

    public final void generateLocalResource(String filename, Guild g)
    {
        AMGN.logger.info("Generating resource " + filename + " for plugin " + plugin.getName() + "...");
        //a list of only the jars found directly in the plugins directory
		File[] plugins_directory = new File(GuildNetwork.PLUGIN_PATH).listFiles((path) -> {return path.getName().endsWith(".jar");});
		AMGN.logger.info("Locating plugin...");
		for(File jar : plugins_directory)
		{
			try
			{
				JarFile potential_plugin_jar = new JarFile(jar.getPath());
				Enumeration<JarEntry> jar_entries = potential_plugin_jar.entries();
				while(jar_entries.hasMoreElements())
				{
					JarEntry currententry = jar_entries.nextElement();
                    //if this is a "valid" plugin
					if(currententry.getName().equals("plugin.yml"))
                    {
                        //check that this is our plugin
						URL url = new URL("jar:file:" + jar.getAbsolutePath() + "!/plugin.yml");
						JarURLConnection jarcon = (JarURLConnection) url.openConnection();
                        InputStream pluginyamlin = jarcon.getInputStream();
						Map<String, Object> pluginyml = new Yaml().load(pluginyamlin);
						if(((String) pluginyml.get("name")).equalsIgnoreCase(this.plugin.getName()))
                        {
                            AMGN.logger.info("Located plugin.");
                            File config_dir = new File(plugin.getGuildConfigPath(g));
                            
                            if(!config_dir.exists() || !config_dir.isDirectory())
                            {
                                AMGN.logger.info("plugin config directory does not exist- creating...");
                                Files.createDirectories(config_dir.toPath());
                            }

                            AMGN.logger.info("copying resource " + filename + " to " + plugin.getGuildConfigPath(g) + "/" + filename);
                            //if so, copy the specified file to the config folder
                            url = new URL("jar:file:" + jar.getAbsolutePath() + "!/" + filename);
                            jarcon = (JarURLConnection) url.openConnection();
                            InputStream newfilein = jarcon.getInputStream();

                            File newfileout = new File(plugin.getGuildConfigPath(g) + "/" + filename);
                            Files.copy(newfilein, newfileout.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            newfilein.close();
                            pluginyamlin.close();
                            
                            AMGN.logger.info("Resource " + plugin.getGuildConfigPath(g) + "/" + filename + " generated.");
                            return;
                        }

                        pluginyamlin.close();
                    }
				}

                potential_plugin_jar.close();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
    }

    //parse maps of yaml files given the filename
    public final Map<String, Object> getGlobalConfig(String filename)
    {
        try
        {
            return IOHandler.readAllYamlData(plugin.getGlobalConfigPath() + "/" + filename);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public final Map<String, Object> getGuildConfig(String filename, Guild g)
    {
        try
        {
            return IOHandler.readAllYamlData(plugin.getGuildConfigPath(g) + "/" + filename);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    //will retrieve a config according to the priority:
    //local config -> global config -> default config -> null
    public final Map<String, Object> getConfig(String filename, Guild g)
    {
        if(g != null && new File(plugin.getGuildConfigPath(g) + "/" + filename).exists())
            return getGuildConfig(filename, g);
        if(new File(plugin.getGlobalConfigPath() + "/" + filename).exists())
            return getGlobalConfig(filename);

        return getDefaultConfig(filename);
    }

    public final Map<String, Object> getConfig(String filename)
    {
        if(new File(plugin.getGlobalConfigPath() + "/" + filename).exists())
            return getGlobalConfig(filename);

        return getDefaultConfig(filename);
    }

    //get a specific value from a yaml file
    public final Object getValueFromGlobalConfig(String filename, String key)
    {
        Map<String, Object> config = getGlobalConfig(filename);
        return config.get(key);
    }

    //get a specific value from a yaml file
    public final Object getValueFromGuildConfig(String filename, String key, Guild g)
    {
        Map<String, Object> config = getGuildConfig(filename, g);
        return config.get(key);
    }

    //will retrieve a config value according to the priority:
    //local config -> global config -> default config -> null
    public final Object getValue(String filename, String key, Guild g)
    {
        if(new File(plugin.getGuildConfigPath(g) + "/" + filename).exists()
            && getValueFromGuildConfig(filename, key, g) != null)
            return getValueFromGuildConfig(filename, key, g);
        if(new File(plugin.getGlobalConfigPath() + "/" + filename).exists()
            && getValueFromGlobalConfig(filename, key) != null)
            return getValueFromGlobalConfig(filename, key);

        return getDefaultValue(filename, key);
    }

    //will retrieve a config value according to the priority:
    //global config -> default config -> null
    public final Object getValue(String filename, String key)
    {
        if(new File(plugin.getGlobalConfigPath() + "/" + filename).exists()
            && getValueFromGlobalConfig(filename, key) != null)
            return getValueFromGlobalConfig(filename, key);

        return getDefaultValue(filename, key);
    }

    public final Map<String, Object> getDefaultConfig(String filename)
    {
        return yaml.load(this.plugin.getClass().getResourceAsStream(filename));
    }

    @SuppressWarnings("unchecked")
    public final Object getDefaultValue(String path, String key)
    {
        return ((Map<String, Object>) yaml.load(this.plugin.getClass().getResourceAsStream("/" + path))).get(key);
    }

    public final void setGlobalConfig(String filename, Map<String, Object> config)
    {
        try
        {
            IOHandler.writeYamlData(config, plugin.getGlobalConfigPath() + "/" + filename);   
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public final void setGuildConfig(String filename, Map<String, Object> config, Guild g)
    {
        try
        {
            IOHandler.writeYamlData(config, plugin.getGuildConfigPath(g) + "/" + filename);   
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public final void setConfig(String filename, Map<String, Object> config, Guild g)
    {
        File localconfig = new File(plugin.getGuildConfigPath(g));
        if(localconfig.exists())
            setGuildConfig(filename, config, g);
        else
            setGlobalConfig(filename, config);
    }

    public final void setGlobalValue(String filename, String key, Object value)
    {
        try
        {
            Map<String, Object> config = IOHandler.readAllYamlData(plugin.getGlobalConfigPath() + "/" + filename);
            config.put(key, value);
            IOHandler.writeYamlData(config, plugin.getGlobalConfigPath() + "/" + filename);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public final void setGuildValue(String filename, String key, Object value, Guild g)
    {
        try
        {
            Map<String, Object> config = IOHandler.readAllYamlData(plugin.getGuildConfigPath(g) + "/" + filename);
            config.put(key, value);
            IOHandler.writeYamlData(config, plugin.getGuildConfigPath(g) + "/" + filename);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public final void setValue(String filename, String key, Object value, Guild g)
    {
        File localconfig = new File(plugin.getGuildConfigPath(g));
        if(localconfig.exists())
            setGuildValue(filename, key, value, g);
        else
            setGlobalValue(filename, key, value);
    }
}
