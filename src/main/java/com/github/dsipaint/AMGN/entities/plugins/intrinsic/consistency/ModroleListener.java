package com.github.dsipaint.AMGN.entities.plugins.intrinsic.consistency;

import java.io.IOException;

import com.github.dsipaint.AMGN.entities.Guild;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.io.IOHandler;

import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class ModroleListener extends ListenerAdapter
{
	public void onRoleDelete(RoleDeleteEvent e)
	{
		long id = e.getGuild().getIdLong();
		
		Guild g = GuildNetwork.guild_data.getOrDefault(id, new Guild(id));
		
		if(e.getRole().getIdLong() == id)
		{
			g.setModrole(Guild.DEFAULT_ID); //set to default id if modrole was deleted
			GuildNetwork.guild_data.put(id, g); //update storage to reflect this
			try
			{
				IOHandler.writeNetworkData(GuildNetwork.guild_data, GuildNetwork.operators, GuildNetwork.NETWORKINFO_PATH);
			}
			catch(IOException e1)
			{
				e1.printStackTrace();
			}
			
			GuildNetwork.sendToModlogs(id, "This guild's modrole " + e.getRole().getName() + " was deleted.");
		}
	}
}
