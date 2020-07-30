package entities.listeners;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.DeserializationException;
import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

import entities.plugins.Plugin;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public abstract class Command extends ListenerAdapter
{
	private String label, usageinfo; 
	
	public Command(Plugin plugin, String label)
	{
		try
		{
			JsonObject plugin_metadata = (JsonObject) Jsoner.deserialize(new FileReader(new File("./plugin.json")));
			JsonObject command_metadata = (JsonObject) plugin_metadata.get("commands");
			
			this.label = label;
			this.usageinfo = command_metadata.getString("usageinfo");
		}
		catch (DeserializationException | IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public abstract void onGuildMessageReceived(GuildMessageReceivedEvent e); //must inherit and implement this method for it to be a command

	public String getLabel()
	{
		return label;
	}

	public String getUsageinfo()
	{
		return usageinfo;
	}
}
