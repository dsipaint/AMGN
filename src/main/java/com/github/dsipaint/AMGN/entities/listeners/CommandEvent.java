package com.github.dsipaint.AMGN.entities.listeners;


import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class CommandEvent
{
    private final String[] args;
    private Member sender;
    private TextChannel tc;
    private Message msg;

    public CommandEvent(String cmd, Member sender, TextChannel tc, Message msg)
    {
        this.args = cmd.split(" ");
        this.sender = sender;
        this.tc = tc;
        this.msg = msg;
    }

    public final String[] getArgs()
    {
        return this.args;
    }

    public final Member getSender()
    {
        return this.sender;
    }

    public final TextChannel getTextChannel()
    {
        return this.tc;
    }

    public final Message getMessage()
    {
        return this.msg;
    }

    public final boolean isProgrammedCommand()
    {
        return this.tc == null || this.msg == null;
    }

    public final Guild getGuild()
    {
        if(this.tc != null)
            return this.tc.getGuild();
        if(this.msg != null)
            return this.msg.getGuild();
        if(this.sender != null)
            return this.sender.getGuild();
        
        return null;
    }
}
