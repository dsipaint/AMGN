package com.github.dsipaint.AMGN.entities.plugins.intrinsic.consistency;

import java.io.IOException;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.io.IOHandler;

import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class OperatorListener extends ListenerAdapter
{
    public void onRoleDelete(RoleDeleteEvent e)
	{
        long roleid = e.getRole().getIdLong();
        if(GuildNetwork.operators.contains(roleid))
        {
            GuildNetwork.operators.remove(roleid);
            try
            {
                IOHandler.writeNetworkData(GuildNetwork.guild_data, GuildNetwork.operators, GuildNetwork.NETWORKINFO_PATH);
            }
            catch(IOException e1)
            {
                e1.printStackTrace();
            }
        }
    }
}
