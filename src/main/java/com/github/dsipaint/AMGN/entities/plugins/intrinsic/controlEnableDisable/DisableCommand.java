package com.github.dsipaint.AMGN.entities.plugins.intrinsic.controlEnableDisable;

import java.util.function.Consumer;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.CommandEvent;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

public final class DisableCommand implements Consumer<CommandEvent>
{
	
	public void accept(CommandEvent e)
	{
		if(e.getArgs().length == 1)
			return;
		
		
		//^disable (plugin name}
		
		Plugin correct_plugin = null;
		//iterate through plugins
		for(Plugin plugin : AMGN.plugin_listeners.keySet())
		{
			if(plugin.getName().equalsIgnoreCase(e.getArgs()[1])) //find correct plugin
			{
				correct_plugin = plugin;
				GuildNetwork.disablePlugin(correct_plugin);
				break;
			}
		}
		
		if(correct_plugin != null) //remove the plugin if we found the one to be removed
		{
			AMGN.plugin_listeners.remove(correct_plugin);
			e.getTextChannel().sendMessage("Plugin was successfully disabled.").queue();
			GuildNetwork.sendToModlogs(e.getGuild().getIdLong(), "Plugin " + correct_plugin.getName() + " "
					+ correct_plugin.getVersion() + " disabled by " + e.getSender().getUser().getName());
		}
		else
			e.getTextChannel().sendMessage("No plugin found with this name.").queue();
	}
}
