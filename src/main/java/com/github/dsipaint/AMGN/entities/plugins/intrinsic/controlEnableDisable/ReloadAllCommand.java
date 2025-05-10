package com.github.dsipaint.AMGN.entities.plugins.intrinsic.controlEnableDisable;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.function.Consumer;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.CommandEvent;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

public class ReloadAllCommand implements Consumer<CommandEvent>
{
    public void accept(CommandEvent e)
    {
        ArrayList<Plugin> broken_plugins = new ArrayList<Plugin>();

        //iterate through enabled plugins
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //disable and enable each one
            try
            {
                GuildNetwork.disablePlugin(plugin);
                GuildNetwork.enablePlugin(plugin);
            }
            catch(Exception ex)
            {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                ex.printStackTrace(pw);

                AMGN.logger.error("Error occurred reloading plugin " + plugin.getName() + " " + plugin.getVersion() + ":\n"
                    + sw.toString());

                broken_plugins.add(plugin);
            }
        });
        
        if(broken_plugins.isEmpty())
            e.getTextChannel().sendMessage("All plugins were successfully reloaded.").queue();
        else
        {
            StringBuilder brokenreport = new StringBuilder("All plugins reloaded- the following plugins ran into problems when reloading:\n");
            broken_plugins.forEach(plugin ->
            {
                brokenreport.append(plugin.getName() + " " + plugin.getVersion() + ", ");
            });
            brokenreport.setLength(brokenreport.length() - 2);

            e.getTextChannel().sendMessage(brokenreport.toString()).queue();
        }

        GuildNetwork.sendToModlogs(e.getGuild().getIdLong(), "All plugins reloaded by " + e.getSender().getUser().getName());
        return;
    }
}
