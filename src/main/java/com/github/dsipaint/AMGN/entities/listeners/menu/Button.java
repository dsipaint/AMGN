package com.github.dsipaint.AMGN.entities.listeners.menu;

import java.util.function.Consumer;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Button extends ListenerAdapter
{
    private Emoji emoji;
    private Message message;
    private Consumer<MenuButtonClickEvent> press, unpress;
    public Plugin plugin;

    public Button(Plugin plugin, Emoji emoji, Consumer<MenuButtonClickEvent> press, Consumer<MenuButtonClickEvent> unpress)
    {
        this.plugin = plugin;
        this.emoji = emoji;
        this.press = press;
        this.unpress = unpress;
    }

    public void attachToMessage(Message message)
    {
        if(emoji.isCustom())
            message.addReaction(emoji.getName() + ":" + emoji.getId()).queue();
        else
            message.addReaction(emoji.getName()).queue();

        this.message = message;

        GuildNetwork.registerListener(this, plugin); //register these listeners to the plugin they came from
    }

    public void onMessageReactionAdd(MessageReactionAddEvent e)
    {
        if(e.getUser().equals(e.getJDA().getSelfUser()))
            return;
        

        if(e.getReactionEmote().isEmote()
            && e.getReactionEmote().getId().equals(this.emoji.getId())
            && e.getMessageId().equals(this.message.getId()))
            press.accept(new MenuButtonClickEvent(e.getMember(), message, this, true));
        else if(e.getReactionEmote().getName().equals(this.emoji.getName())
            && e.getMessageId().equals(this.message.getId()))
            press.accept(new MenuButtonClickEvent(e.getMember(), message, this, true));
    }

    public void onMessageReactionRemove(MessageReactionRemoveEvent e)
    {
        if(e.getUser().equals(e.getJDA().getSelfUser()))
            return;
            
        if(e.getReactionEmote().isEmote()
            && e.getReactionEmote().getId().equals(this.emoji.getId())
            && e.getMessageId().equals(this.message.getId()))
            unpress.accept(new MenuButtonClickEvent(e.getMember(), message, this, false));
        else if(e.getReactionEmote().getName().equals(this.emoji.getName())
            && e.getMessageId().equals(this.message.getId()))
            unpress.accept(new MenuButtonClickEvent(e.getMember(), message, this, false));
    }

    public class MenuButtonClickEvent
    {
        private Member member;
        private Message message;
        private Button button;
        private boolean isPress;

        public MenuButtonClickEvent(Member member, Message message, Button button, boolean isPress)
        {
            this.member = member;
            this.message = message;
            this.button = button;
            this.isPress = isPress;
        }

        public Member getMember()
        {
            return this.member;
        }

        public Message getMessage()
        {
            return this.message;
        }

        public Button getButton()
        {
            return this.button;
        }

        //if the button is pressed, this returns TRUE
        //if the button is unpressed (i.e. emote removed), this returns FALSE
        public boolean isPress()
        {
            return this.isPress;
        }
}
}
