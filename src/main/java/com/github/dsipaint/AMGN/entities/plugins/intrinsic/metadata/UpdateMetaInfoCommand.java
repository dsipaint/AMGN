package com.github.dsipaint.AMGN.entities.plugins.intrinsic.metadata;

import java.io.IOException;
import java.util.function.Consumer;

import com.github.dsipaint.AMGN.entities.Guild;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.CommandEvent;
import com.github.dsipaint.AMGN.io.IOHandler;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public final class UpdateMetaInfoCommand implements Consumer<CommandEvent>
{
	//allows the changing of prefix, modlogs and modrole
	//if no guild metadata is found, this command also creates a guild object with default values
	//before using the command, and then writes this back to guilds.json
	//NOTE: defaulting guilds may break system for now- concurrency issue, looping through list and adding new guild to list
	public void accept(CommandEvent e)
	{
		if(e.getArgs().length <= 2)
			return;
		
		//allows injection otherwise (?)
		if(e.getArgs()[2].contains("\""))
			return;
		
		long guild_id = e.getGuild().getIdLong();
		
		//^updatemetainfo prefix {new prefix}
		if(e.getArgs()[1].equalsIgnoreCase("prefix"))
		{
			Guild guild = GuildNetwork.guild_data.getOrDefault(guild_id, new Guild(guild_id));
			guild.setPrefix(e.getArgs()[2]);
			GuildNetwork.guild_data.put(e.getGuild().getIdLong(), guild); //*overwrites* if data was already there, or *sets* if data was not

			try
			{
				IOHandler.writeNetworkData(GuildNetwork.guild_data, GuildNetwork.NETWORKINFO_PATH); //write this to network.yml
			}
			catch(IOException e1)
			{
				e1.printStackTrace();
			}
			
			e.getTextChannel().sendMessage("New prefix for this guild was set to " + e.getArgs()[2]).queue();
			GuildNetwork.sendToModlogs(guild_id, "New prefix for this guild was set to " + e.getArgs()[2]
					+ " by " + e.getSender().getUser().getName());
			return;
		}
		
		//^updatemetainfo modlogs {modlogs id}
		if(e.getArgs()[1].equalsIgnoreCase("modlogs"))
		{
			//if valid id format
			if(e.getArgs()[2].matches(GuildNetwork.ID_REGEX))
			{
				for(TextChannel tc : e.getGuild().getTextChannels())
				{
					//and channel does indeed exist
					if(tc.getId().equals(e.getArgs()[2]))
					{
						Guild guild = GuildNetwork.guild_data.getOrDefault(guild_id, new Guild(guild_id));
						guild.setModlogs(Long.parseLong(e.getArgs()[2]));
						GuildNetwork.guild_data.put(e.getGuild().getIdLong(), guild); //*overwrites* if data was already there, or *sets* if data was not
						try
						{
							IOHandler.writeNetworkData(GuildNetwork.guild_data, GuildNetwork.NETWORKINFO_PATH); //write this to network.yml
						}
						catch(IOException e1)
						{
							e1.printStackTrace();
						}
						
						e.getTextChannel().sendMessage("Modlogs channel for server updated to "+ tc.getAsMention()).queue();
						GuildNetwork.sendToModlogs(guild_id, "Modlogs channel for server updated to "+ tc.getAsMention()
								+ " by " + e.getSender().getUser().getName());
						return;
					}
				}
			}
			
			return;
		}

		//^updatemetainfo acceptcol {hex code}
		if(e.getArgs()[1].equalsIgnoreCase("acceptcol"))
		{
			if(e.getArgs()[2].matches("#([a-fA-F0-9]){6}"))
			{
				int newcol = Integer.parseInt(e.getArgs()[2].replace("#", ""), 16);
				Guild guild = GuildNetwork.guild_data.getOrDefault(guild_id, new Guild(guild_id));
				guild.setAccept_col(newcol);
				GuildNetwork.guild_data.put(e.getGuild().getIdLong(), guild); //*overwrites* if data was already there, or *sets* if data was not
				try
				{
					IOHandler.writeNetworkData(GuildNetwork.guild_data, GuildNetwork.NETWORKINFO_PATH); //write this to network.yml
				}
				catch(IOException e1)
				{
					e1.printStackTrace();
				}
				e.getTextChannel().sendMessage("Accept colour updated to " + e.getArgs()[2]).queue();
				GuildNetwork.sendToModlogs(guild_id, "Accept colour updated to " + e.getArgs()[2]
						+ " by " + e.getSender().getUser().getName());
				return;
			}
		}

		//^updatemetainfo declinecol {hex code}
		if(e.getArgs()[1].equalsIgnoreCase("declinecol"))
		{
			if(e.getArgs()[2].matches("#([a-fA-F0-9]){6}"))
			{
				int newcol = Integer.parseInt(e.getArgs()[2].replace("#", ""), 16);
				Guild guild = GuildNetwork.guild_data.getOrDefault(guild_id, new Guild(guild_id));
				guild.setDecline_col(newcol);
				GuildNetwork.guild_data.put(e.getGuild().getIdLong(), guild); //*overwrites* if data was already there, or *sets* if data was not
				try
				{
					IOHandler.writeNetworkData(GuildNetwork.guild_data, GuildNetwork.NETWORKINFO_PATH); //write this to network.yml
				}
				catch(IOException e1)
				{
					e1.printStackTrace();
				}
				e.getTextChannel().sendMessage("Decline colour updated to " + e.getArgs()[2]).queue();
				GuildNetwork.sendToModlogs(guild_id, "Decline colour updated to " + e.getArgs()[2]
						+ " by " + e.getSender().getUser().getName());
				return;
			}
		}

		//^updatemetainfo uniquecol {hex code}
		if(e.getArgs()[1].equalsIgnoreCase("uniquecol"))
		{
			if(e.getArgs()[2].matches("#([a-fA-F0-9]){6}"))
			{
				int newcol = Integer.parseInt(e.getArgs()[2].replace("#", ""), 16);
				Guild guild = GuildNetwork.guild_data.getOrDefault(guild_id, new Guild(guild_id));
				guild.setUnique_col(newcol);
				GuildNetwork.guild_data.put(e.getGuild().getIdLong(), guild); //*overwrites* if data was already there, or *sets* if data was not
				try
				{
					IOHandler.writeNetworkData(GuildNetwork.guild_data, GuildNetwork.NETWORKINFO_PATH); //write this to network.yml
				}
				catch(IOException e1)
				{
					e1.printStackTrace();
				}
				e.getTextChannel().sendMessage("Unique colour updated to " + e.getArgs()[2]).queue();
				GuildNetwork.sendToModlogs(guild_id, "Unique colour updated to " + e.getArgs()[2]
						+ " by " + e.getSender().getUser().getName());
				return;
			}
		}
	}
}
