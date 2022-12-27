package com.github.dsipaint.AMGN.entities.plugins.intrinsic.controlEnableDisable;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.Command;
import com.github.dsipaint.AMGN.entities.listeners.DefaultCommand;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReloadListener extends ListenerAdapter
{
	//for ^reload and ^reloadall
	
	public void onMessageReceived(MessageReceivedEvent e)
	{
		String[] args = e.getMessage().getContentRaw().split(" ");

		if(!e.isFromGuild())
			return;

		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + DefaultCommand.RELOAD.getLabel())
				&& Command.hasPermission(e.getMember(), DefaultCommand.RELOAD.getGuildPermission()))
		{
			//check if plugin is enabled with that name
			if(args.length == 1)
				return;
			
			//iterate through plugins
			for(Plugin plugin : AMGN.plugin_listeners.keySet())
			{
				if(plugin.getName().equalsIgnoreCase(args[1])) //find correct plugin
				{
					plugin.onDisable(); //disable plugin
					AMGN.plugin_listeners.get(plugin).forEach(AMGN.bot::removeEventListener); //remove listeners
					AMGN.menucache.forEach(menu ->
					{
						if(menu.getPlugin().equals(plugin))
							menu.softDestroy();
					});
					AMGN.menucache.removeIf(menu -> {return menu.getPlugin().equals(plugin);});//remove menus
					plugin.onEnable(); //re-enable plugin
					
					e.getChannel().sendMessage("Plugin was successfully reloaded.").queue();
					GuildNetwork.sendToModlogs(e.getGuild().getIdLong(), "Plugin " + plugin.getName() + " "
							+ plugin.getVersion() + " reloaded by " + e.getAuthor().getAsTag());
					return;
				}
			}
			
			e.getChannel().sendMessage("No plugin found with this name.").queue();
			return;
		}
		
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + DefaultCommand.RELOADALL.getLabel())
				&& Command.hasPermission(e.getMember(), DefaultCommand.RELOADALL.getGuildPermission()))
		{
			//iterate through enabled plugins
			AMGN.plugin_listeners.forEach((plugin, listeners) ->
			{
				//disable and enable each
				plugin.onDisable();
				AMGN.plugin_listeners.get(plugin).forEach(AMGN.bot::removeEventListener); //remove listeners
				plugin.onEnable();
			});
			
			e.getChannel().sendMessage("All plugins were successfully reloaded.").queue();
			GuildNetwork.sendToModlogs(e.getGuild().getIdLong(), "All plugins reloaded by " + e.getAuthor().getAsTag());
			return;
		}
	}
}
