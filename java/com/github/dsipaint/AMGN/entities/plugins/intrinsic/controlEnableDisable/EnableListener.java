package com.github.dsipaint.AMGN.entities.plugins.intrinsic.controlEnableDisable;

import java.io.File;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.Command;
import com.github.dsipaint.AMGN.entities.listeners.DefaultCommand;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.io.IOHandler;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class EnableListener extends ListenerAdapter
{
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		
		//^enable
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + DefaultCommand.ENABLE.getLabel())
				&& Command.hasPermission(e.getMember(), DefaultCommand.ENABLE.getGuildPermission()))
		{
			if(args.length == 1)
				return;
			
			//^enable {plugin name}
			//check if there is a plugin in the filesystem with this name
			if(IOHandler.pluginExists(args[1]))
			{
				//a list of only the jars found directly in the plugins directory
				File[] plugins_directory = new File(GuildNetwork.PLUGIN_PATH).listFiles((path) ->
				{
					if(path.getName().endsWith(".jar"))
						return true;
						
					return false;
				});
				
				//(loop through all plugins to find the right one)
				for(File file : plugins_directory)
				{
					Plugin plugin = IOHandler.getPluginObjectFromPath(file.getPath());
					
					//check that it isn't already enabled
					if(!IOHandler.isEnabled(plugin))
					{						
						if(plugin.getName().equals(args[1])) //enable it if the name matches the command argument
						{
							GuildNetwork.enablePlugin(plugin); //wrong reference, fix possibly
							e.getChannel().sendMessage("Plugin was successfully enabled.").queue();
							GuildNetwork.sendToModlogs(e.getGuild().getIdLong(), "Plugin " + plugin.getName() + " "
									+ plugin.getVersion() + " enabled by " + e.getAuthor().getAsTag());
							return;
						}
					}
				}
			}
			else
				e.getChannel().sendMessage("No plugin found with this name.").queue();
		}
	}
}
