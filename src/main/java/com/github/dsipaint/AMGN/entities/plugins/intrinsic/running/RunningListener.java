package com.github.dsipaint.AMGN.entities.plugins.intrinsic.running;

import java.util.ArrayList;
import java.util.List;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.Command;
import com.github.dsipaint.AMGN.entities.listeners.DefaultCommand;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class RunningListener extends ListenerAdapter
{
	public void onMessageReceived(MessageReceivedEvent e)
	{
		if(!e.isFromGuild())
			return;
			
		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + DefaultCommand.RUNNING.getLabel())
				&& Command.hasPermission(e.getMember(), DefaultCommand.RUNNING.getGuildPermission()))
		{
			EmbedBuilder eb = new EmbedBuilder()
					.setTitle("Active plugins in " + e.getGuild().getName() + ": ")
					.setColor(GuildNetwork.guild_data.get(e.getGuild().getIdLong()).getAccept_col());
			
			List<Plugin> plugins_for_guild = new ArrayList<Plugin>();
			AMGN.plugin_listeners.forEach((plugin, listeners) ->
			{
				if(GuildNetwork.whitelist.get(plugin.getName()).contains(e.getGuild().getIdLong()))
					plugins_for_guild.add(plugin);
				if(GuildNetwork.blacklist.get(plugin.getName()).contains(e.getGuild().getIdLong()))
					plugins_for_guild.remove(plugin);
			});

			plugins_for_guild.forEach(plugin ->
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
