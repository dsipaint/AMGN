package com.github.dsipaint.AMGN.entities.plugins.intrinsic.consistency;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.menu.Button;
import com.github.dsipaint.AMGN.entities.listeners.menu.Menu;

import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MenuDeleteListener extends ListenerAdapter
{
    //if a message is deleted, we need to check if it's one with button listeners attached
    public void onMessageDelete(MessageDeleteEvent e)
    {
        for(Menu menu : AMGN.menucache)
        {
            if(e.getMessageId().equals(menu.getMessage().getId()))
            {
                for(Button b : menu.getButtons())
                    GuildNetwork.unregisterListener(b, menu.getPlugin());
                return;
            }
        }
    }    
}
