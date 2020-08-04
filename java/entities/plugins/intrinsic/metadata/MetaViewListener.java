package entities.plugins.intrinsic.metadata;

import entities.Guild;
import entities.listeners.Command;
import entities.plugins.Plugin;
import main.GuildNetwork;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class MetaViewListener extends Command
{
	public MetaViewListener(Plugin plugin)
	{
		super(plugin, "viewmetainfo");
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		long id = e.getGuild().getIdLong();
		
		//^viewmetainfo
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(id) + this.getLabel()))
		{
			e.getChannel().sendMessage(GuildNetwork.guild_data.getOrDefault(id, new Guild(id))
					.asEmbed()).queue();
			
			return;
		}
	}
}
