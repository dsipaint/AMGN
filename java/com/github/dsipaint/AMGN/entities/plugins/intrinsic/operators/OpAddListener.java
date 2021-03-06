package com.github.dsipaint.AMGN.entities.plugins.intrinsic.operators;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.io.IOHandler;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class OpAddListener extends ListenerAdapter
{	
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + "setoperator") && GuildNetwork.operators.contains(e.getMember().getIdLong()))
		{
			//valid user id and is a real user
			if(args[1].matches("\\d{18}") && e.getGuild().getMemberById(args[1]) != null)
			{
				//if user is not already opped
				if(!GuildNetwork.operators.contains(Long.parseLong(args[1])))
				{
					GuildNetwork.operators.add(Long.parseLong(args[1]));
					IOHandler.writeNetworkData(GuildNetwork.guild_data, GuildNetwork.operators, GuildNetwork.NETWORKINFO_PATH);
					GuildNetwork.sendToModlogs(e.getGuild().getIdLong(), e.getAuthor().getAsTag() + " added "
							+ e.getGuild().getMemberById(args[1]).getUser().getAsTag() + " as an operator.");
					
					return;
				}
				else
					e.getChannel().sendMessage("User is already an operator!").queue();
			}
			else
				e.getChannel().sendMessage("Invalid user id").queue();
			
			return;
		}
	}
}
