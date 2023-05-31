package com.github.dsipaint.AMGN.entities.plugins.intrinsic.metadata;

import java.util.Collections;

import com.github.dsipaint.AMGN.entities.Guild;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.DefaultCommand;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class MetaViewListener extends ListenerAdapter
{
	@SuppressWarnings("unchecked")
	public void onMessageReceived(MessageReceivedEvent e)
	{
		if(!e.isFromGuild())
			return;
			
		if(!DefaultCommand.METAVIEW.hasPermission(e.getMember()))
			return;

		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		long id = e.getGuild().getIdLong();
		//^viewmetainfo
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(id) + DefaultCommand.METAVIEW.getLabel()))
		{
			e.getChannel().sendMessageEmbeds(GuildNetwork.guild_data.getOrDefault(id, new Guild(id))
					.asEmbed())
					.setAllowedMentions(Collections.EMPTY_SET).queue();
			
			return;
		}
	}
}
