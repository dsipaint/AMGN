package com.github.dsipaint.AMGN.entities.plugins.intrinsic.running;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.DefaultCommand;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class RunningAllListener extends ListenerAdapter
{
    public void onMessageReceived(MessageReceivedEvent e)
	{
		if(!e.isFromGuild())
			return;

		if(!DefaultCommand.RUNNINGALL.hasPermission(e.getMember()))
			return;

		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + DefaultCommand.RUNNINGALL.getLabel()))
		{
			EmbedBuilder eb = new EmbedBuilder()
					.setTitle("Active plugins in network: ")
					.setColor(GuildNetwork.guild_data.get(e.getGuild().getIdLong()).getAccept_col());
			
			AMGN.plugin_listeners.forEach((plugin, listeners) ->
			{
				appendEmbed(eb, plugin.getName() + " " + plugin.getVersion() + " (written by " + plugin.getAuthor() + ")\n");
			});
			
			e.getChannel().sendMessageEmbeds(eb.build()).queue();
        }
    }

	private void appendEmbed(EmbedBuilder eb, String msg)
	{
		eb = eb.appendDescription(msg);
	}
}
