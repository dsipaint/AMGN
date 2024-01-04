package com.github.dsipaint.AMGN.entities.plugins.intrinsic.controlEnableDisable;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.CommandEvent;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.io.IOHandler;

public final class EnableCommand implements Consumer<CommandEvent>
{
	public void accept(CommandEvent e)
	{
		if(e.getArgs().length == 1)
			return;
		
		//^enable {plugin name}
		//check if there is a valid plugin in the filesystem with this name
		if(IOHandler.pluginExists(e.getArgs()[1]))
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
				if(plugin != null && plugin.getName().equalsIgnoreCase(e.getArgs()[1]))
				{						
					if(!IOHandler.isEnabled(plugin)) //check that it isn't already enabled
					{
						GuildNetwork.enablePlugin(plugin); //wrong reference, fix possibly
						e.getTextChannel().sendMessage("Plugin was successfully enabled.").queue();
						GuildNetwork.sendToModlogs(e.getGuild().getIdLong(), "Plugin " + plugin.getName() + " "
								+ plugin.getVersion() + " enabled by " + e.getSender().getUser().getName());
						return;
					}
				}
			}
		}
		else
			e.getTextChannel().sendMessage("No plugin found with this name.").queue();
	}
}
