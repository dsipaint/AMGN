package entities.listeners;

import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.DeserializationException;
import org.json.simple.JsonArray;
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
			JsonObject plugin_metadata = (JsonObject) Jsoner.deserialize(
					new InputStreamReader(getClass().getResourceAsStream("plugin.json")));
			
			JsonArray command_metadata = (JsonArray) plugin_metadata.get("commands");
			
			command_metadata.forEach(command ->
			{
				JsonObject command_obj = (JsonObject) command;
				
				//if this is the desired command within the metadata
				if(command_obj.containsValue(label))
				{
					this.label = label; //take the command data we need
					this.usageinfo = command_obj.getString("usageinfo");
				}
			});
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
