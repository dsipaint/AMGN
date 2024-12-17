package com.github.dsipaint.AMGN.entities.plugins.intrinsic.controlEnableDisable;

import java.util.function.Consumer;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.CommandEvent;

public class ReloadAllCommand implements Consumer<CommandEvent>
{
    public void accept(CommandEvent e)
    {
        //iterate through enabled plugins
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //disable and enable each
            plugin.onDisable();
            AMGN.plugin_listeners.get(plugin).forEach(AMGN.bot::removeEventListener); //remove listeners
            plugin.onEnable();
        });
        
        e.getTextChannel().sendMessage("All plugins were successfully reloaded.").queue();
        GuildNetwork.sendToModlogs(e.getGuild().getIdLong(), "All plugins reloaded by " + e.getSender().getUser().getName());
        return;
    }
}
