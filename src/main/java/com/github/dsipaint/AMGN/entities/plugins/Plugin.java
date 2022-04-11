package com.github.dsipaint.AMGN.entities.plugins;

import java.io.InputStreamReader;
import java.util.Map;

import com.github.dsipaint.AMGN.entities.GuildNetwork;

import org.yaml.snakeyaml.Yaml;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public abstract class Plugin
{
	static final String RESOURCE_PATH = "/plugin.yml"; //absolute path inside jar
	
	public abstract void onEnable();
	
	public abstract void onDisable();
	
	
	
	/** 
	 * @return String name of the plugin
	 */
	//NOTE: it is possible to tidy these methods up a bit and make them more efficient
	public final String getName()
	{
		Map<String, Object> metadata = new Yaml().load(new InputStreamReader(getClass().getResourceAsStream(RESOURCE_PATH)));
		return (String) metadata.get("name");
	}
	
	
	/** 
	 * @return String listed version of the plugin
	 */
	public final String getVersion()
	{
		Map<String, Object> metadata = new Yaml().load(new InputStreamReader(getClass().getResourceAsStream(RESOURCE_PATH)));
		return (String) metadata.get("version");
	}
	
	
	/** 
	 * @return String listed author of the plugin
	 */
	public final String getAuthor()
	{
		Map<String, Object> metadata = new Yaml().load(new InputStreamReader(getClass().getResourceAsStream(RESOURCE_PATH)));
		return (String) metadata.get("author");
	}
	
	
	/** 
	 * @return String listed url for the plugin
	 */
	public final String getUrl()
	{
		Map<String, Object> metadata = new Yaml().load(new InputStreamReader(getClass().getResourceAsStream(RESOURCE_PATH)));
		return (String) metadata.get("url");
	}
	
	
	/** 
	 * @return String listed image url for the plugin
	 */
	public final String getImageUrl()
	{
		Map<String, Object> metadata = new Yaml().load(new InputStreamReader(getClass().getResourceAsStream(RESOURCE_PATH)));
		return (String) metadata.get("image");
	}
	
	
	/** 
	 * @return String description of the plugin
	 */
	public final String getDescription()
	{
		Map<String, Object> metadata = new Yaml().load(new InputStreamReader(getClass().getResourceAsStream(RESOURCE_PATH)));
		return (String) metadata.get("description");
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
