package com.github.dsipaint.AMGN.entities.plugins.intrinsic.controlEnableDisable;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.Command;
import com.github.dsipaint.AMGN.entities.listeners.DefaultCommand;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.main.AMGN;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class DisableListener extends ListenerAdapter
{
	
	public void onMessageReceived(MessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		
		//^disable
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + DefaultCommand.DISABLE.getLabel())
				&& Command.hasPermission(e.getMember(), DefaultCommand.DISABLE.getGuildPermission()))
		{
			if(args.length == 1)
				return;
			
			
			//^disable (plugin name}
			
			Plugin correct_plugin = null;
			//iterate through plugins
			for(Plugin plugin : AMGN.plugin_listeners.keySet())
			{
				if(plugin.getName().equalsIgnoreCase(args[1])) //find correct plugin
				{
					correct_plugin = plugin;
					plugin.onDisable(); //disable plugin
					AMGN.plugin_listeners.get(plugin).forEach(AMGN.bot::removeEventListener); //remove listeners
					
					break;
				}
			}
			
			if(correct_plugin != null) //remove the plugin if we found the one to be removed
			{
				AMGN.plugin_listeners.remove(correct_plugin);
				e.getChannel().sendMessage("Plugin was successfully disabled.").queue();
				GuildNetwork.sendToModlogs(e.getGuild().getIdLong(), "Plugin " + correct_plugin.getName() + " "
						+ correct_plugin.getVersion() + " disabled by " + e.getAuthor().getAsTag());
			}
			else
				e.getChannel().sendMessage("No plugin found with this name.").queue();
			
		}
	}
}
