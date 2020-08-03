package entities.plugins;

import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.DeserializationException;
import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

import main.GuildNetwork;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public abstract class Plugin
{	
	public abstract void onEnable();
	
	public abstract void onDisable();
	
	
	//NOTE: it is possible to tidy these methods up a bit and make them more efficient
	public String getName()
	{
		try
		{
			JsonObject metadata = (JsonObject) Jsoner.deserialize(new InputStreamReader(getClass().getResourceAsStream("plugin.json")));
			return metadata.getString("name");
		}
		catch (DeserializationException | IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getVersion()
	{
		try
		{
			JsonObject metadata = (JsonObject) Jsoner.deserialize(new InputStreamReader(getClass().getResourceAsStream("plugin.json")));
			return metadata.getString("version");
		}
		catch (DeserializationException | IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getAuthor()
	{
		try
		{
			JsonObject metadata = (JsonObject) Jsoner.deserialize(new InputStreamReader(getClass().getResourceAsStream("plugin.json")));
			return metadata.getString("author");
		}
		catch (DeserializationException | IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getUrl()
	{
		try
		{
			JsonObject metadata = (JsonObject) Jsoner.deserialize(new InputStreamReader(getClass().getResourceAsStream("plugin.json")));
			return metadata.getString("url");
		}
		catch (DeserializationException | IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getImageUrl()
	{
		try
		{
			JsonObject metadata = (JsonObject) Jsoner.deserialize(new InputStreamReader(getClass().getResourceAsStream("plugin.json")));
			return metadata.getString("image");
		}
		catch (DeserializationException | IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getDescription()
	{
		try
		{
			JsonObject metadata = (JsonObject) Jsoner.deserialize(new InputStreamReader(getClass().getResourceAsStream("plugin.json")));
			return metadata.getString("description");
		}
		catch (DeserializationException | IOException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public final MessageEmbed getDisplayEmbed()
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
