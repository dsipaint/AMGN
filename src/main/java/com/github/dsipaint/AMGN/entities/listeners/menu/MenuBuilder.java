package com.github.dsipaint.AMGN.entities.listeners.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.menu.Button.MenuButtonClickEvent;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;

public class MenuBuilder
{
    private Plugin plugin;
    private Message message;
    private MessageEmbed embed;
    private String messagetext;
    private TextChannel channel;
    private List<Button> buttons;
    
    public MenuBuilder(Plugin plugin, Message message)
    {
        this.plugin = plugin;
        this.message = message;

        buttons = new ArrayList<Button>();
    }

    public MenuBuilder(Plugin plugin, TextChannel channel, MessageEmbed embed)
    {
        this.plugin = plugin;
        this.channel = channel;
        this.embed = embed;
        buttons = new ArrayList<Button>();
    }

    public MenuBuilder(Plugin plugin, TextChannel channel, String messagetext)
    {
        this.plugin = plugin;
        this.channel = channel;
        this.messagetext = messagetext;
        buttons = new ArrayList<Button>();
    }

    //self-referencing to allow for chaining
    public MenuBuilder setMessage(Message message)
    {
        this.message.editMessage(MessageEditBuilder.fromMessage(message).build());
        return this;
    }

    public MenuBuilder setMessage(String messagetext)
    {
        this.messagetext = messagetext;
        return this;
    }

    public MenuBuilder setMessage(MessageEmbed embed)
    {
        this.embed = embed;
        return this;
    }

    public MenuBuilder addButton(Emoji emoji, Consumer<MenuButtonClickEvent> press, Consumer<MenuButtonClickEvent> unpress)
    {
        buttons.add(new Button(this.plugin, emoji, press, unpress));
        return this;
    }

    public MenuBuilder removeButton(Button button)
    {
        GuildNetwork.unregisterListener(button, plugin);
        buttons.remove(button);
        return this;
    }

    public Menu build() throws InvalidMenuException
    {
        if(buttons.size() == 0)
            throw new InvalidMenuException("Menus must have at least 1 button associated with them.");

        //this is a hack because apparently I can't just cast buttons.toArray() to Button[]
        Button[] buttonsarr = new Button[buttons.size()];
        for(int i = 0; i < buttonsarr.length; i++)
            buttonsarr[i] = buttons.get(i);

        if(this.message != null)
            return new Menu(this.plugin, this.message, buttonsarr);
        else if(this.embed != null && this.channel != null)
        {
            this.message = this.channel.sendMessageEmbeds(embed).complete();
            return new Menu(this.plugin, this.message, buttonsarr);
        }
        else if(this.messagetext != null && this.channel != null)
        {
            this.message = this.channel.sendMessage(messagetext).complete();
            return new Menu(this.plugin, this.message, buttonsarr);
        }
        else
            throw new InvalidMenuException("Must have a non-null message and a non-null channel for the menu.");
    }

    public class InvalidMenuException extends Exception
    {
        public InvalidMenuException(String message)
        {
            super(message);
        }
    }
}
