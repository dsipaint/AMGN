package com.github.dsipaint.AMGN.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

import org.yaml.snakeyaml.Yaml;

public class Config
{
    /*
        This object controls all plugin operations for a plugin.
        It is designed to make recalling config data more easily, setting and getting config data.
    */
    private Plugin plugin;
    private Yaml yaml = new Yaml();

    public Config(Plugin plugin)
    {
        this.plugin = plugin;
    }

    //TODO: add method: public boolean exists(String filename)
    //TODO: add setting methods for setting config data

    //can generate files that are stored in the jar- will most commonly be used for template config.ymls
    public final void generateResource(String filename)
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
					if(currententry.getName().equals("plugin.yml"))
                    {
						URL url = new URL("jar:file:" + jar.getAbsolutePath() + "!/plugin.yml");
						JarURLConnection jarcon = (JarURLConnection) url.openConnection();
                        InputStream newfilein = jarcon.getInputStream();
						Map<String, Object> pluginyml = new Yaml().load(newfilein);
						//check that the name of the plugin is our name
						if(((String) pluginyml.get("name")).equalsIgnoreCase(filename))
                        {
                            //if so, copy the file to ./plugins/name/filename
                            File newfileout = new File(plugin.getConfigPath() + "/" + filename);
                            Files.copy(newfilein, newfileout.toPath(), StandardCopyOption.REPLACE_EXISTING);
                            newfilein.close();
                            return;
                        }

                        newfilein.close();
                    }
				}
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
    }

    //parse maps of yaml files given the filename
    public final Map<String, Object> getConfig(String filename) throws FileNotFoundException
    {
        return yaml.load(new FileReader(new File(plugin.getConfigPath() + "/" + filename)));
    }

    //get a specific value from a yaml file
    public final Object getValueFromConfig(String filename, String key) throws FileNotFoundException
    {
        Map<String, Object> config = getConfig(filename);
        return getValueFromMap(config, key);
    }

    //recursively get a value set inside a Map, checking if it is contained in any maps within this map
    @SuppressWarnings("unchecked")
    private final Object getValueFromMap(Map<String, Object> map, String key)
    {
        Object value = map.get("key");
        //if we couldn't find the value, check inside nested objects if possible
        if(value == null)
        {
            Object nestedvalue = null;
            for(Object obj : map.values())
            {
                if(obj instanceof Map)
                    nestedvalue = getValueFromMap((Map<String, Object>) obj, key);
            }

            return nestedvalue;
        }
        else
            return value;
    }
}