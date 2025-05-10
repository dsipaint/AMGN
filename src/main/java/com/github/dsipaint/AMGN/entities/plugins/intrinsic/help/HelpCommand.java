package com.github.dsipaint.AMGN.entities.plugins.intrinsic.help;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.CommandEvent;
import com.github.dsipaint.AMGN.entities.listeners.managed.Command;
import com.github.dsipaint.AMGN.entities.listeners.managed.DefaultCommand;
import com.github.dsipaint.AMGN.entities.listeners.managed.ListenerProxy;
import com.github.dsipaint.AMGN.entities.listeners.managed.menu.ScrollMenuBuilder;
import com.github.dsipaint.AMGN.entities.listeners.managed.menu.MenuBuilder.InvalidMenuException;

import net.dv8tion.jda.api.EmbedBuilder;

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
				if(ListenerProxy.applyWhitelistBlacklistRules(plugin.getName(), e.getGuild()))
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
			// for(MessageEmbed embed : returnFormattedEmbedList(commandlist.toString(), e.getGuild().getIdLong()))
			// 	e.getTextChannel().sendMessageEmbeds(embed).queue();

			EmbedBuilder eb = new EmbedBuilder()
				.setTitle("Commands:")
				.setColor(GuildNetwork.guild_data.get(e.getGuild().getIdLong()).getAccept_col());
			
			try
			{
				ScrollMenuBuilder scrollbuilder = new ScrollMenuBuilder(null, commandlist.toString(), "\n", eb, e.getTextChannel());
				scrollbuilder.getBuilder().setTimeoutSoftDestroy(5, TimeUnit.MINUTES); //soft-destroy after 5 minutes
				scrollbuilder.alphabetiseDescription();
				scrollbuilder.build();
			}
			catch(InvalidMenuException e1)
			{
				e1.printStackTrace();
			}
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
						.setDescription("**Usage: **" + command.getUsage(e.getGuild()) + "\n**Description: **" + command.getDesc());
					
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
				
				EmbedBuilder eb = new EmbedBuilder()
					.setTitle("Commands:")
					.setColor(GuildNetwork.guild_data.get(e.getGuild().getIdLong()).getAccept_col());

				try
				{
					ScrollMenuBuilder scrollbuilder = new ScrollMenuBuilder(null, plugin_commands.toString(), "\n", eb, e.getTextChannel());
					scrollbuilder.getBuilder().setTimeoutSoftDestroy(5, TimeUnit.MINUTES); //soft-destroy after 5 minutes
					scrollbuilder.alphabetiseDescription();
					scrollbuilder.build(); 
				}
				catch(InvalidMenuException e1)
				{
					e1.printStackTrace();
				}
				
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
						.setDescription("**Usage: **" + ((Command) listener).getUsage(e.getGuild()) + "\n**Description: **" + ((Command) listener).getDesc());
					
					eb = eb.addField("Author:", plugin.getAuthor(), true);
					eb = eb.addField("Plugin:", plugin.getName(), true);
					
					e.getTextChannel().sendMessageEmbeds(eb.build()).queue();
					return;
				}
			});
		});
	}
}
