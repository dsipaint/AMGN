package com.github.dsipaint.AMGN.entities.plugins;

import java.io.InputStreamReader;
import java.util.Map;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.io.Config;

import org.yaml.snakeyaml.Yaml;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public abstract class Plugin
{
	static final String RESOURCE_PATH = "/plugin.yml"; //absolute path inside jar
	private String config_path, //relative path for config files
		name,
		version,
		author,
		url,
		imageurl,
		description;

	private Config config; //config object for the plugin
	
	public abstract void onEnable();
	
	public abstract void onDisable();
	
	public Plugin()
	{
		Map<String, Object> metadata = new Yaml().load(new InputStreamReader(getClass().getResourceAsStream(RESOURCE_PATH)));
		name = (String) metadata.get("name");
		version = (String) metadata.get("version");
		author = (String) metadata.get("author");
		url = (String) metadata.get("url");
		imageurl = (String) metadata.get("image");
		description = (String) metadata.get("description");
		
		config_path = "./plugins/" + getName().toLowerCase();
		config = new Config(this);
	}
	
	/** 
	 * @return String name of the plugin
	 */
	public final String getName()
	{
		return this.name;
	}
	
	
	/** 
	 * @return String listed version of the plugin
	 */
	public final String getVersion()
	{
		return this.version;
	}
	
	
	/** 
	 * @return String listed author of the plugin
	 */
	public final String getAuthor()
	{
		return this.author;
	}
	
	
	/** 
	 * @return String listed url for the plugin
	 */
	public final String getUrl()
	{
		return this.url;
	}
	
	
	/** 
	 * @return String listed image url for the plugin
	 */
	public final String getImageUrl()
	{
		return this.imageurl;
	}
	
	
	/** 
	 * @return String description of the plugin
	 */
	public final String getDescription()
	{
		return this.description;
	}

	/** 
	 * @return Config config object for the plugin
	 */
	public final Config getConfig()
	{
		return this.config;
	}

	/** 
	 * @return String config path of the plugin i.e. ./plugins/name/
	 */
	public final String getConfigPath()
	{
		return this.config_path;
	}
	
	/** 
	 * @return MessageEmbed embed displaying all info of the plugin
	 */
	public MessageEmbed getDisplayEmbed()
	{
		return new EmbedBuilder()
				.setTitle(this.getName() + " " + this.getVersion())
				.setAuthor("Author: " + this.getAuthor(), this.getUrl(), null)
				.setImage(this.getImageUrl())
				.setColor(GuildNetwork.GREEN_EMBED_COLOUR)
				.setDescription(this.getDescription())
				.build();
	}
}
