package com.github.dsipaint.AMGN.entities.plugins.intrinsic.whitelist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.CommandEvent;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.io.IOHandler;

public class BlacklistCommand implements Consumer<CommandEvent>
{
    public void accept(CommandEvent e)
	{
		if(e.getArgs().length < 3)
			return;

		if(e.getArgs()[1].equalsIgnoreCase("add"))
		{
			for(Plugin plugin : AMGN.plugin_listeners.keySet())
			{
				if(plugin.getName().equalsIgnoreCase(e.getArgs()[2]))
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

					e.getTextChannel().sendMessage("Added " + e.getGuild().getName() + " to this plugin's blacklist").queue();
					return;
				}
			}

			e.getTextChannel().sendMessage("No plugin found with this name").queue();
			return;				
		}

		if(e.getArgs()[1].equalsIgnoreCase("remove"))
		{
			for(Plugin plugin : AMGN.plugin_listeners.keySet())
			{
				if(plugin.getName().equalsIgnoreCase(e.getArgs()[2]))
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

					e.getTextChannel().sendMessage("Removed " + e.getGuild().getName() + " from this plugin's blacklist").queue();
					return;
				}
			}

			e.getTextChannel().sendMessage("No plugin found with this name").queue();
			return;		
		}
    }
}
