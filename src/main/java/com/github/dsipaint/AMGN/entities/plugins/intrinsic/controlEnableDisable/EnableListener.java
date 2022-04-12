package com.github.dsipaint.AMGN.entities.plugins.intrinsic.controlEnableDisable;

import java.io.File;
import java.io.IOException;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.Command;
import com.github.dsipaint.AMGN.entities.listeners.DefaultCommand;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.io.IOHandler;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class EnableListener extends ListenerAdapter
{
	public void onMessageReceived(MessageReceivedEvent e)
	{
		String[] args = e.getMessage().getContentRaw().split(" ");
		
		//^enable
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + DefaultCommand.ENABLE.getLabel())
				&& Command.hasPermission(e.getMember(), DefaultCommand.ENABLE.getGuildPermission()))
		{
			if(args.length == 1)
				return;
			
			//^enable {plugin name}
			//check if there is a valid plugin in the filesystem with this name
			if(IOHandler.pluginExists(args[1]))
			{
				//find the plugin jar
				
				//a list of only the jars found directly in the plugins directory
				File[] plugins_directory = new File(GuildNetwork.PLUGIN_PATH).listFiles(path -> {return path.getName().endsWith(".jar");});
				
				//(loop through all plugins to find the right one)
				for(File file : plugins_directory)
				{
					Plugin plugin = null;
					try
					{
						plugin = IOHandler.getPluginObjectFromJar(file);
					}
					catch (IOException e1)
					{
						e1.printStackTrace();
					}
					
					//enable it if the name matches the command argument
					if(plugin.getName().equalsIgnoreCase(args[1]))
					{						
						if(!IOHandler.isEnabled(plugin)) //check that it isn't already enabled
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
