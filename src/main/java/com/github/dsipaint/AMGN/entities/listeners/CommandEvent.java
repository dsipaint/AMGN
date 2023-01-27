package com.github.dsipaint.AMGN.entities.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandEvent extends MessageReceivedEvent
{
    private final String[] args = this.getMessage().getContentRaw().split(" ");

    public CommandEvent(MessageReceivedEvent e)
    {
        super(e.getJDA(), e.getResponseNumber(), e.getMessage());
    }

    public final String[] getArgs()
    {
        return this.args;
    }
}
