package com.github.dsipaint.AMGN.entities.plugins.intrinsic.metadata;

import com.github.dsipaint.AMGN.entities.Guild;
import com.github.dsipaint.AMGN.entities.GuildNetwork;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class MetaViewListener extends ListenerAdapter
{
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		long id = e.getGuild().getIdLong();
		
		//^viewmetainfo
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(id) + "viewmetainfo"))
		{
			e.getChannel().sendMessage(GuildNetwork.guild_data.getOrDefault(id, new Guild(id))
					.asEmbed()).queue();
			
			return;
		}
	}
}
