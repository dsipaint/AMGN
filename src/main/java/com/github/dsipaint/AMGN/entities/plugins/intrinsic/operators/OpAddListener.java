package com.github.dsipaint.AMGN.entities.plugins.intrinsic.operators;

import java.io.IOException;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.io.IOHandler;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class OpAddListener extends ListenerAdapter
{	
	public void onMessageReceived(MessageReceivedEvent e)
	{
		if(!e.isFromGuild())
			return;
			
		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + "setoperator") && GuildNetwork.isOperator(e.getAuthor()))
		{
			//valid user id and is a real user/role
			if(args[1].matches(GuildNetwork.ID_REGEX) && (e.getGuild().getMemberById(args[1]) != null || e.getGuild().getRoleById(args[1]) != null))
			{
				//if user or role is not already opped
				if(!GuildNetwork.operators.contains(Long.parseLong(args[1])))
				{
					GuildNetwork.operators.add(Long.parseLong(args[1]));
					try
					{
						IOHandler.writeNetworkData(GuildNetwork.guild_data, GuildNetwork.operators, GuildNetwork.NETWORKINFO_PATH);
					}
					catch(IOException e1)
					{
						e1.printStackTrace();
					}
					GuildNetwork.sendToModlogs(e.getGuild().getIdLong(), e.getAuthor().getAsTag() + " added "
							+ (e.getGuild().getMemberById(args[1]) != null ? e.getGuild().getMemberById(args[1]).getUser().getAsTag() : (e.getGuild().getRoleById(args[1]).getName() + "role")) 
							+ " as an operator.");
					
					return;
				}
				else
					e.getChannel().sendMessage("User or role is already an operator!").queue();
			}
			else
				e.getChannel().sendMessage("Invalid id").queue();
			
			return;
		}
	}
}
