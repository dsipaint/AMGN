package entities.plugins.intrinsic.running;

import entities.listeners.Command;
import entities.plugins.Plugin;
import main.GuildNetwork;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class RunningListener extends Command
{
	public RunningListener(Plugin plugin)
	{
		super(plugin, "showplugins");
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + this.getLabel()))
		{
			
		}
	}
}
