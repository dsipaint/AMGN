package com.github.dsipaint.AMGN.entities.plugins.intrinsic.consistency;

import java.io.IOException;

import com.github.dsipaint.AMGN.entities.Guild;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.io.IOHandler;

import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class ModlogsListener extends ListenerAdapter
{
	public void onChannelDelete(ChannelDeleteEvent e)
	{
		long id = e.getChannel().getIdLong();
		
		Guild g = GuildNetwork.guild_data.getOrDefault(id, new Guild(id));
		
		if(id == g.getModlogs())
		{
			g.setModlogs(Guild.DEFAULT_ID);
			GuildNetwork.guild_data.put(id, g);
			try
			{
				IOHandler.writeNetworkData(GuildNetwork.guild_data, GuildNetwork.NETWORKINFO_PATH);
			}
			catch(IOException e1)
			{
				e1.printStackTrace();
			}
			
			//no point sending to modlogs, as we know the modlogs channel was just deleted
		}
		
	}
}
