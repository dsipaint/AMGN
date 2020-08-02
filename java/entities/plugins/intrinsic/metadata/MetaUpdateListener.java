package entities.plugins.intrinsic.metadata;

import entities.listeners.Command;
import entities.plugins.Plugin;
import main.GuildNetwork;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class MetaUpdateListener extends Command
{
	public MetaUpdateListener(Plugin plugin)
	{
		super(plugin, "updatemetainfo");
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
