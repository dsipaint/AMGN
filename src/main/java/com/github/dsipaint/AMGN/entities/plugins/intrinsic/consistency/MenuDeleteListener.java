package com.github.dsipaint.AMGN.entities.plugins.intrinsic.consistency;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.managed.menu.Button;
import com.github.dsipaint.AMGN.entities.listeners.managed.menu.Menu;

import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MenuDeleteListener extends ListenerAdapter
{
    //if a message is deleted, we need to check if it's one with button listeners attached
    public void onMessageDelete(MessageDeleteEvent e)
    {
        Menu foundmenu = null;
        for(Menu menu : AMGN.menucache)
        {
            //if we find the menu that was deleted
            if(e.getMessageId().equals(menu.getMessage().getId()))
            {
                //unregister listeners
                for(Button b : menu.getButtons())
                    GuildNetwork.unregisterListener(b, menu.getPlugin());

                //we can stop looking now
                foundmenu = menu;
                break;   
            }
        }

        if(foundmenu != null)
            AMGN.menucache.remove(foundmenu); //remove the menu we found
    }    
}
