package com.github.dsipaint.AMGN.entities.plugins.intrinsic.consistency;

import com.github.dsipaint.AMGN.entities.Guild;
import com.github.dsipaint.AMGN.entities.listeners.Listener;
import com.github.dsipaint.AMGN.io.IOHandler;
import com.github.dsipaint.AMGN.main.GuildNetwork;

import net.dv8tion.jda.api.events.role.RoleDeleteEvent;

public class ModroleListener extends Listener
{
	public void onRoleDelete(RoleDeleteEvent e)
	{
		long id = e.getGuild().getIdLong();
		
		Guild g = GuildNetwork.guild_data.getOrDefault(id, new Guild(id));
		
		if(e.getRole().getIdLong() == id)
		{
			g.setModrole(Guild.DEFAULT_ID); //set to default id if modrole was deleted
			GuildNetwork.guild_data.put(id, g); //update storage to reflect this
			IOHandler.writeNetworkData(GuildNetwork.guild_data, GuildNetwork.operators, GuildNetwork.NETWORKINFO_PATH);
			
			GuildNetwork.sendToModlogs(id, "This guild's modrole " + e.getRole().getName() + " was deleted.");
		}
	}
}
