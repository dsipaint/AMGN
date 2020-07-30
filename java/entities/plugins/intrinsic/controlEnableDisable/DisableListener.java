package entities.plugins.intrinsic.controlEnableDisable;

import java.io.File;

import entities.listeners.Command;
import entities.plugins.Plugin;
import main.GuildNetwork;
import main.IOHandler;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class DisableListener extends Command
{
	public DisableListener(Plugin main)
	{
		super(main, "disable");
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		
		//^disable
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + this.getLabel()))
		{
			if(args.length == 1)
				return;
			
			//^disable (plugin name}
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
				
				//loop through all plugins to find the right one
				for(File file : plugins_directory)
				{
					Plugin plugin = IOHandler.getPluginObjectFromPath(file.getPath());
					
					//if there is, check that it is already enabled
					if(IOHandler.isEnabled(plugin))
					{
						if(args[1].equals(plugin.getName())) //disable it if names match
						{
							GuildNetwork.disablePlugin(plugin); //wrong reference, fix possibly
							e.getChannel().sendMessage("Plugin was successfully disabled.").queue();
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
