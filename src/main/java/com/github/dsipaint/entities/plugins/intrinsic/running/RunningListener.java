package com.github.dsipaint.AMGN.entities.plugins.intrinsic.running;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.Command;
import com.github.dsipaint.AMGN.entities.listeners.DefaultCommand;
import com.github.dsipaint.AMGN.main.AMGN;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class RunningListener extends ListenerAdapter
{	
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + DefaultCommand.RUNNING.getLabel())
				&& Command.hasPermission(e.getMember(), DefaultCommand.RUNNING.getGuildPermission()))
		{
			EmbedBuilder eb = new EmbedBuilder()
					.setTitle("Active plugins: ")
					.setColor(GuildNetwork.GREEN_EMBED_COLOUR);
			
			AMGN.plugin_listeners.forEach((plugin, listeners) ->
			{
				appendEmbed(eb, plugin.getName() + " " + plugin.getVersion() + " (written by " + plugin.getAuthor() + ")\n");
			});
			
			e.getChannel().sendMessage(eb.build()).queue();
		}
	}
	
	private void appendEmbed(EmbedBuilder eb, String msg)
	{
		eb = eb.appendDescription(msg);
	}
}
