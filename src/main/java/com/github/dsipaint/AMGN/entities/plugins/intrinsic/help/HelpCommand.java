package com.github.dsipaint.AMGN.entities.plugins.intrinsic.help;

import java.util.function.Consumer;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.Command;
import com.github.dsipaint.AMGN.entities.listeners.CommandEvent;
import com.github.dsipaint.AMGN.entities.listeners.DefaultCommand;
import com.github.dsipaint.AMGN.entities.listeners.ListenerWrapper;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public final class HelpCommand implements Consumer<CommandEvent>
{
	public void accept(CommandEvent e)
	{
		String prefix = GuildNetwork.getPrefix(e.getGuild().getIdLong());
		
		//^help
		if(e.getArgs().length == 1)
		{				
			StringBuilder commandlist = new StringBuilder();
			
			//go through inbuilt commands
			for(DefaultCommand command : DefaultCommand.values())
			{
				//only show commands a user has permission to see
				if(command.hasPermission(e.getSender()))
					commandlist.append("**" + prefix + command.getLabel() + ":** " + command.getDesc() + "\n");
			}

			AMGN.plugin_listeners.forEach((plugin, list) ->
			{
				if(ListenerWrapper.pluginShouldRun(plugin.getName(), e.getGuild()))
				{
					list.forEach(listener ->
					{
						//if this is a command and the user has permission for this command
						if(listener instanceof Command && ((Command) listener).hasPermission(e.getSender()))
							commandlist.append("**" + prefix + ((Command) listener).getLabel() + ":** " + ((Command) listener).getDesc() + "\n");
					});
				}
			});
			
			//produce the formatted list of embeds and display them
			for(MessageEmbed embed : returnFormattedEmbedList(commandlist.toString(), e.getGuild().getIdLong()))
				e.getTextChannel().sendMessageEmbeds(embed).queue();
			
			return;
		}
		
		//^help {command/plugin}
		//search for command with the name of args[1], and return its help syntax
		
		//loop through all intrinsic commands
		for(DefaultCommand command : DefaultCommand.values())
		{
			if(command.getLabel().equalsIgnoreCase(e.getArgs()[1]) && command.hasPermission(e.getSender()))
			{
				EmbedBuilder eb = new EmbedBuilder()
						.setTitle(prefix + command.getLabel())
						.setColor(GuildNetwork.guild_data.get(e.getGuild().getIdLong()).getAccept_col())
						.setDescription("**Usage: **" + command.getUsage() + "\n**Description: **" + command.getDesc());
					
					eb = eb.addField("Author:", "al~", true);
					eb = eb.addField("Plugin:", "AMGN-inbuilt", true);
					
					e.getTextChannel().sendMessageEmbeds(eb.build()).queue();
			}
				
		}
		
		//loop through all plugins and listeners
		AMGN.plugin_listeners.forEach((plugin, listeners) ->
		{
			//display all plugins with commands with the specified name
			if(plugin.getName().equalsIgnoreCase(e.getArgs()[1]))
			{
				e.getTextChannel().sendMessageEmbeds(plugin.getDisplayEmbed()).queue();
				
				//list all commands from this plugin
				StringBuilder plugin_commands = new StringBuilder();
				listeners.forEach(listener ->
				{
					if(listener instanceof Command && ((Command) listener).hasPermission(e.getSender()))
						plugin_commands.append("**" + prefix + ((Command) listener).getLabel() + ": ** " + ((Command) listener).getDesc() + "\n");
				});
				
				
				for(MessageEmbed embed : returnFormattedEmbedList(plugin_commands.toString(), e.getGuild().getIdLong()))
					e.getTextChannel().sendMessageEmbeds(embed).queue();
				
				return;
			}
			
			//check commands for names if plugin was not found with the name
			listeners.forEach(listener ->
			{
				//check registered commands, find one with the same name
				if(listener instanceof Command && ((Command) listener).getLabel().equalsIgnoreCase(e.getArgs()[1])
						&& ((Command) listener).hasPermission(e.getSender()))
				{
					EmbedBuilder eb = new EmbedBuilder()
						.setTitle(prefix + ((Command) listener).getLabel())
						.setColor(GuildNetwork.guild_data.get(e.getGuild().getIdLong()).getAccept_col())
						.setDescription("**Usage: **" + ((Command) listener).getUsage() + "\n**Description: **" + ((Command) listener).getDesc());
					
					eb = eb.addField("Author:", plugin.getAuthor(), true);
					eb = eb.addField("Plugin:", plugin.getName(), true);
					
					e.getTextChannel().sendMessageEmbeds(eb.build()).queue();
					return;
				}
			});
		});
	}

	private MessageEmbed[] returnFormattedEmbedList(String desc, long guildid)
	{
		int embednum = (desc.length()/MessageEmbed.TEXT_MAX_LENGTH) + 1; //how many embeds are needed, simple maths to find out
		MessageEmbed[] embedlist = new MessageEmbed[embednum]; //2+2 = 4 -1 = 3
		
		String[] lines = desc.split("\n"); //split the description into individual commands
		int currentline = 0; //to remember which line we've written up to
		
		for(int i = 0; i < embedlist.length; i++)
		{
			EmbedBuilder eb = new EmbedBuilder()
					.setTitle("Commands:")
					.setColor(GuildNetwork.guild_data.get(guildid).getAccept_col());
			
			//find out how many lines can fit into the embed and add them
			int chartotal = 0;
			for(int j = currentline; j < lines.length; j++)
			{
				currentline++; //if this updates the original value of j then I'm in trouble
				
				if(chartotal + lines[j].length() < MessageEmbed.TEXT_MAX_LENGTH)
				{
					chartotal += lines[j].length();
					eb = eb.appendDescription(lines[j] + "\n"); //if we can add the next line safely, do so, if not, break
				}
				else
					break;
			}
			
			embedlist[i] = eb.build();
		}
		
		return embedlist;
	}
}
