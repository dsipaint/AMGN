package com.github.dsipaint.AMGN.entities.plugins.intrinsic.metadata;

import javax.management.relation.Role;

import com.github.dsipaint.AMGN.entities.Guild;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.io.IOHandler;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class MetaUpdateListener extends ListenerAdapter
{
	//allows the changing of prefix, modlogs and modrole
	//if no guild metadata is found, this command also creates a guild object with default values
	//before using the command, and then writes this back to guilds.json
	//NOTE: defaulting guilds may break system for now- concurrency issue, looping through list and adding new guild to list
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		
		//^updatemetainfo
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + "updatemetainfo"))
		{
			if(args.length <= 2)
				return;
			
			//allows injection otherwise (?)
			if(args[2].contains("\""))
				return;
			
			long guild_id = e.getGuild().getIdLong();
			
			//^updatemetainfo prefix {new prefix}
			if(args[1].equalsIgnoreCase("prefix"))
			{
				Guild guild = GuildNetwork.guild_data.getOrDefault(guild_id, new Guild(guild_id));
				guild.setPrefix(args[2]);
				GuildNetwork.guild_data.put(e.getGuild().getIdLong(), guild); //*overwrites* if data was already there, or *sets* if data was not
				IOHandler.writeNetworkData(GuildNetwork.guild_data, GuildNetwork.operators, GuildNetwork.NETWORKINFO_PATH); //write this to guilds.json
				
				e.getChannel().sendMessage("New prefix for this guild was set to " + args[2]).queue();
				GuildNetwork.sendToModlogs(guild_id, "New prefix for this guild was set to " + args[2]
						+ " by " + e.getAuthor().getAsTag());
				return;
			}
			
			//^updatemetainfo modlogs {modlogs id}
			if(args[1].equalsIgnoreCase("modlogs"))
			{
				//if valid id format
				if(args[2].matches("\\d{18}"))
				{
					for(TextChannel tc : e.getGuild().getTextChannels())
					{
						//and channel does indeed exist
						if(tc.getId().equals(args[2]))
						{
							Guild guild = GuildNetwork.guild_data.getOrDefault(guild_id, new Guild(guild_id));
							guild.setModlogs(Long.parseLong(args[2]));
							GuildNetwork.guild_data.put(e.getGuild().getIdLong(), guild); //*overwrites* if data was already there, or *sets* if data was not
							IOHandler.writeNetworkData(GuildNetwork.guild_data, GuildNetwork.operators, GuildNetwork.NETWORKINFO_PATH); //write this to guilds.json
							
							e.getChannel().sendMessage("Modlogs channel for server updated to "+ tc.getAsMention()).queue();
							GuildNetwork.sendToModlogs(guild_id, "Modlogs channel for server updated to "+ tc.getAsMention()
									+ " by " + e.getAuthor().getAsTag());
							return;
						}
					}
				}
				
				return;
			}
			
			//^updatemetainfo modrole {modrole id}
			if(args[1].equalsIgnoreCase("modrole"))
			{
				//if valid id format
				if(args[2].matches("\\d{18}"))
				{
					for(Role r : e.getGuild().getRoles())
					{
						//if this role is indeed in this server
						if(r.getId().equals(args[2]))
						{
							Guild guild = GuildNetwork.guild_data.getOrDefault(guild_id, new Guild(guild_id));
							guild.setModrole(Long.parseLong(args[2]));
							GuildNetwork.guild_data.put(e.getGuild().getIdLong(), guild); //*overwrites* if data was already there, or *sets* if data was not
							IOHandler.writeNetworkData(GuildNetwork.guild_data, GuildNetwork.operators, GuildNetwork.NETWORKINFO_PATH); //write this to guilds.json							
							e.getChannel().sendMessage("Modrole updated to " + r.getName()).queue();
							GuildNetwork.sendToModlogs(guild_id, "Modrole updated to " + r.getName()
									+ " by " + e.getAuthor().getAsTag());
							return;
						}
					}
				}
				
				return;
			}
		}
	}
}
