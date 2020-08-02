package entities.plugins.intrinsic.closenetwork;

import entities.listeners.Command;
import entities.plugins.Plugin;
import main.GuildNetwork;
import main.Main;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class CloseListener extends Command
{
	public CloseListener(Plugin plugin)
	{
		super(plugin, "closenetwork");
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		
		//^closenetwork
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + this.getLabel()))
		{
			//no permission handling for now
			
			//disable every active plugin
			Main.plugin_listeners.forEach((plugin, listenerlist) ->
			{
				plugin.onDisable(); //don't use GuildNetwork.disablePlugin due to concurrency issues accessing this hashmap/arraylists in the hashmap
			});
			
			//run disable methods and then leave the unregistering of plugins/listeners to the jda shutdown
			//and the garbage-collecter
			
			e.getChannel().sendMessage("Disabled all plugins. Shutting down...").queue();
			e.getJDA().shutdown();
			System.exit(0); //close program
		}
	}
}
