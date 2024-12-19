package com.github.dsipaint.AMGN.entities.plugins.intrinsic.metadata;

import java.util.Collections;
import java.util.function.Consumer;

import com.github.dsipaint.AMGN.entities.Guild;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.CommandEvent;

public final class ViewMetaInfoCommand implements Consumer<CommandEvent>
{
	@SuppressWarnings("unchecked")
	public void accept(CommandEvent e)
	{
		long id = e.getTextChannel().getGuild().getIdLong();
		e.getTextChannel().sendMessageEmbeds(GuildNetwork.guild_data.getOrDefault(id, new Guild(id))
			.asEmbed())
			.setAllowedMentions(Collections.EMPTY_SET).queue();
			
			return;
	}
}
