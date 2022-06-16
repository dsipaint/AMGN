package com.github.dsipaint.AMGN.entities.listeners.menu;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.main.AMGN;

import net.dv8tion.jda.api.entities.Message;

public class Menu
{
    private Plugin plugin;
    private Message message;
    private Button[] buttons;

    public Menu(Plugin plugin, Message message, Button... buttons)
    {
        this.plugin = plugin;
        this.message = message;
        this.buttons = buttons;
        for(Button button : buttons)
            button.attachToMessage(message);
        AMGN.menucache.add(this); //add to menu cache for consistency plugin
    }

    public Message getMessage()
    {
        return this.message;
    }

    public Button[] getButtons()
    {
        return this.buttons.clone();
    }

    public Plugin getPlugin()
    {
        return this.plugin;
    }

    //will destroy the menu, deleting the message and removing the button listeners
    public void destroy()
    {
        this.message.delete().queue();
        //remove listeners
        for(Button button : buttons)
            GuildNetwork.unregisterListener(button, this.plugin);
    }

    //takes off the buttons without deleting the message
    public void softDestroy()
    {
        for(Button button : buttons)
            GuildNetwork.unregisterListener(button, this.plugin);
    }
}
