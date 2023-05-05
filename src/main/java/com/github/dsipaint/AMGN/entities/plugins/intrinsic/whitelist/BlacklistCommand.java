package com.github.dsipaint.AMGN.entities.plugins.intrinsic.whitelist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.DefaultCommand;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.io.IOHandler;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BlacklistCommand extends ListenerAdapter
{
    public void onMessageReceived(MessageReceivedEvent e)
	{
		if(!e.isFromGuild())
			return;
			
		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");

		if(!DefaultCommand.BLACKLIST.hasPermission(e.getMember()))
			return;
		
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + DefaultCommand.BLACKLIST.getLabel()))
		{
			if(args.length < 3)
				return;

			if(args[1].equalsIgnoreCase("add"))
			{
				for(Plugin plugin : AMGN.plugin_listeners.keySet())
				{
					if(plugin.getName().equalsIgnoreCase(args[2]))
					{
						List<Long> blacklistforplugin = GuildNetwork.blacklist.getOrDefault(plugin.getName(), new ArrayList<Long>());
						GuildNetwork.blacklist.put(plugin.getName(), blacklistforplugin);
						if(!blacklistforplugin.contains(e.getGuild().getIdLong()))
						{
							blacklistforplugin.add(e.getGuild().getIdLong());
							try
							{
								IOHandler.writeWhitelistBlacklist();
							}
							catch(IOException e1)
							{
								e1.printStackTrace();
							}
						}

						e.getChannel().sendMessage("Added " + e.getGuild().getName() + " to this plugin's blacklist").queue();
						return;
					}
				}

				e.getChannel().sendMessage("No plugin found with this name").queue();
				return;				
			}

			if(args[1].equalsIgnoreCase("remove"))
			{
				for(Plugin plugin : AMGN.plugin_listeners.keySet())
				{
					if(plugin.getName().equalsIgnoreCase(args[2]))
					{
						List<Long> blacklistforplugin = GuildNetwork.blacklist.getOrDefault(plugin.getName(), new ArrayList<Long>());
						if(blacklistforplugin.contains(e.getGuild().getIdLong()))
						{
							blacklistforplugin.remove(e.getGuild().getIdLong());
							GuildNetwork.blacklist.put(plugin.getName(), blacklistforplugin);
							try
							{
								IOHandler.writeWhitelistBlacklist();
							}
							catch(IOException e1)
							{
								e1.printStackTrace();
							}
						}

						e.getChannel().sendMessage("Removed " + e.getGuild().getName() + " from this plugin's blacklist").queue();
						return;
					}
				}

				e.getChannel().sendMessage("No plugin found with this name").queue();
				return;		
			}
        }
    }
}
