package com.github.dsipaint.AMGN.entities.plugins.intrinsic.controlEnableDisable;

import java.util.function.Consumer;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.CommandEvent;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

public class ReloadCommand implements Consumer<CommandEvent>
{	
	public void accept(CommandEvent e)
	{
		//check if plugin is enabled with that name
		if(e.getArgs().length == 1)
			return;
		
		//iterate through plugins
		for(Plugin plugin : AMGN.plugin_listeners.keySet())
		{
			if(plugin.getName().equalsIgnoreCase(e.getArgs()[1])) //find correct plugin
			{
				GuildNetwork.disablePlugin(plugin);
				GuildNetwork.enablePlugin(plugin);
				
				e.getTextChannel().sendMessage("Plugin was successfully reloaded.").queue();
				GuildNetwork.sendToModlogs(e.getGuild().getIdLong(), "Plugin " + plugin.getName() + " "
						+ plugin.getVersion() + " reloaded by " + e.getSender().getUser().getName());
				return;
			}
		}
		
		e.getTextChannel().sendMessage("No plugin found with this name.").queue();
		return;
	}
}
