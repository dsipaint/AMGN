package com.github.dsipaint.AMGN.entities.plugins.intrinsic.operators;

import java.io.IOException;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.io.IOHandler;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class OpRemoveListener extends ListenerAdapter
{
	
	public void onMessageReceived(MessageReceivedEvent e)
	{
		if(!e.isFromGuild())
			return;
			
		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + "removeoperator") && GuildNetwork.operators.contains(e.getMember().getIdLong()))
		{
			//valid user id and is a real user
			if(args[1].matches("\\d{18}") && e.getGuild().getMemberById(args[1]) != null)
			{
				//if user is not already opped
				if(GuildNetwork.operators.contains(Long.parseLong(args[1])))
				{
					GuildNetwork.operators.remove(Long.parseLong(args[1]));
					try
					{
						IOHandler.writeNetworkData(GuildNetwork.guild_data, GuildNetwork.operators, GuildNetwork.NETWORKINFO_PATH);
					}
					catch(IOException e1)
					{
						e1.printStackTrace();
					}
					GuildNetwork.sendToModlogs(e.getGuild().getIdLong(), e.getAuthor().getAsTag() + " removed "
							+ e.getGuild().getMemberById(args[1]).getUser().getAsTag() + " as an operator.");
					
					return;
				}
				else
					e.getChannel().sendMessage("User is already not an operator!").queue();
			}
			else
				e.getChannel().sendMessage("Invalid user id").queue();
			
			return;
		}
	}
}
