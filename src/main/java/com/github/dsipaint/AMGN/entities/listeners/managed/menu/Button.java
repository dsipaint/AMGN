package com.github.dsipaint.AMGN.entities.listeners.managed.menu;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.function.Consumer;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.Listener;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

public class Button extends Listener
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
        message.addReaction(emoji).queue();

        this.message = message;

        if(this.plugin != null)
            GuildNetwork.registerListener(this, plugin); //register these listeners to the plugin they came from
        else
            AMGN.bot.addEventListener(this); //null plugins allow the intrinsic plugins to make use of menus
    }

    public void onMessageReactionAdd(MessageReactionAddEvent e)
    {
        if(e.getJDA().getSelfUser().equals(e.getUser()))
            return;
        
        if(e.getReaction().getEmoji().getName().equals(this.emoji.getName())
            && e.getMessageId().equals(this.message.getId()))
        {
            try
            {
                press.accept(new MenuButtonClickEvent(e.retrieveMember().complete(), message, this, true));
            }
            catch(Exception ex)
            {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);

                AMGN.logger.error("Error encountered on menu button press\n"
                    + "Plugin: " + plugin.getName() + " " + plugin.getVersion() + "\n"
                    + "Message: " + e.getMessageId() + " - " + e.getJumpUrl() + "\n"
                    + "Channel: " + e.getChannel() + "\n"
                    + "Guild: " + e.getGuild() + "\n"
                    + "User: " + e.getUser() + "\n"
                    + "Emoji: " + e.getEmoji() + "\n"
                    + "Stacktrace: " + sw.toString()
                    + "\n");
            }
        }
    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent e)
    {
        if(e.getJDA().getSelfUser().equals(e.getUser()))
            return;
        
        if(e.getReaction().getEmoji().getName().equals(this.emoji.getName())
            && e.getMessageId().equals(this.message.getId()))
        {
            try
            {
                unpress.accept(new MenuButtonClickEvent(e.retrieveMember().complete(), message, this, false));
            }
            catch(Exception ex)
            {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);

                AMGN.logger.error("Error encountered on menu button unpress\n"
                    + "Plugin: " + plugin.getName() + " " + plugin.getVersion() + "\n"
                    + "Message: " + e.getMessageId() + " - " + e.getJumpUrl() + "\n"
                    + "Channel: " + e.getChannel() + "\n"
                    + "Guild: " + e.getGuild() + "\n"
                    + "User: " + e.getUser() + "\n"
                    + "Emoji: " + e.getEmoji() + "\n"
                    + "Stacktrace: " + sw.toString()
                    + "\n");
            }
        }
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
