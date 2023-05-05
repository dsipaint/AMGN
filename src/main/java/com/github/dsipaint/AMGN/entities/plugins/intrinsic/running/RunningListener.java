package com.github.dsipaint.AMGN.entities.plugins.intrinsic.running;

import java.util.HashMap;
import java.util.List;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.DefaultCommand;
import com.github.dsipaint.AMGN.entities.listeners.ListenerWrapper;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class RunningListener extends ListenerAdapter
{
	public void onMessageReceived(MessageReceivedEvent e)
	{
		if(!e.isFromGuild())
			return;
			
		if(!DefaultCommand.RUNNING.hasPermission(e.getMember()))
			return;

		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + DefaultCommand.RUNNING.getLabel()))
		{
			EmbedBuilder eb = new EmbedBuilder()
					.setTitle("Active plugins in " + e.getGuild().getName() + ": ")
					.setColor(GuildNetwork.guild_data.get(e.getGuild().getIdLong()).getAccept_col());

			HashMap<String, List<Long>> plugins_for_guild = ListenerWrapper.applyWhitelistBlacklist(e.getGuild());

			plugins_for_guild.keySet().forEach(name ->
			{
				AMGN.plugin_listeners.keySet().forEach(plugin ->
				{
					if(name.equals(plugin.getName()))
						appendEmbed(eb, plugin.getName() + " " + plugin.getVersion() + " (written by " + plugin.getAuthor() + ")\n");
				});
			});
			
			e.getChannel().sendMessageEmbeds(eb.build()).queue();
		}
	}

	private void appendEmbed(EmbedBuilder eb, String msg)
	{
		eb = eb.appendDescription(msg);
	}
}
