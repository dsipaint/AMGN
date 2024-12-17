package com.github.dsipaint.AMGN.entities.plugins.intrinsic.consistency;

import java.io.IOException;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.Guild;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.io.IOHandler;

import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildJoinSettings extends ListenerAdapter
{
    public void onGuildJoin(GuildJoinEvent e)
    {
        try
        {
            //add default guild data
            if(!GuildNetwork.guild_data.containsKey(e.getGuild().getIdLong()))
            {
                AMGN.logger.warn("Guild " + e.getGuild() + " has no settings in network.json");
                GuildNetwork.guild_data.put(e.getGuild().getIdLong(), new Guild(e.getGuild().getIdLong()));
                IOHandler.writeNetworkData(GuildNetwork.guild_data, GuildNetwork.NETWORKINFO_PATH);
            }
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
