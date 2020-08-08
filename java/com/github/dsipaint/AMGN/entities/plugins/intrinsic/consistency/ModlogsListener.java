package com.github.dsipaint.AMGN.entities.plugins.intrinsic.consistency;

import com.github.dsipaint.AMGN.entities.Guild;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.io.IOHandler;

import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class ModlogsListener extends ListenerAdapter
{
	public void onTextChannelDelete(TextChannelDeleteEvent e)
	{
		long id = e.getChannel().getIdLong();
		
		Guild g = GuildNetwork.guild_data.getOrDefault(id, new Guild(id));
		
		if(id == g.getModlogs())
		{
			g.setModlogs(Guild.DEFAULT_ID);
			GuildNetwork.guild_data.put(id, g);
			IOHandler.writeNetworkData(GuildNetwork.guild_data, GuildNetwork.operators, GuildNetwork.NETWORKINFO_PATH);
			//no point sending to modlogs, as we know the modlogs channel was just deleted
		}
		
	}
}
