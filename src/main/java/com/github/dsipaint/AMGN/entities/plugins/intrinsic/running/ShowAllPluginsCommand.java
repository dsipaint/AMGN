package com.github.dsipaint.AMGN.entities.plugins.intrinsic.running;

import java.util.function.Consumer;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.CommandEvent;

import net.dv8tion.jda.api.EmbedBuilder;

public class ShowAllPluginsCommand implements Consumer<CommandEvent>
{
    public void accept(CommandEvent e)
	{
		EmbedBuilder eb = new EmbedBuilder()
				.setTitle("Active plugins in network: ")
				.setColor(GuildNetwork.guild_data.get(e.getGuild().getIdLong()).getAccept_col());
		
		AMGN.plugin_listeners.forEach((plugin, listeners) ->
		{
			appendEmbed(eb, plugin.getName() + " " + plugin.getVersion() + " (written by " + plugin.getAuthor() + ")\n");
		});
		
		e.getTextChannel().sendMessageEmbeds(eb.build()).queue();
    }

	private void appendEmbed(EmbedBuilder eb, String msg)
	{
		eb = eb.appendDescription(msg);
	}
}
