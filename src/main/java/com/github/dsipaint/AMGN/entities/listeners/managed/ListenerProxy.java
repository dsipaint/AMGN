package com.github.dsipaint.AMGN.entities.listeners.managed;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.CommandEvent;
import com.github.dsipaint.AMGN.entities.listeners.Listener;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.UpdateEvent;
import net.dv8tion.jda.api.events.channel.GenericChannelEvent;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ListenerProxy extends ListenerAdapter
{
    //TODO singleton class?

    //this class provides a wrapper where we can apply blacklist/whitelisting rules
    //before we choose to pass the event to a plugin's listener (if the guild has whitelisted this plugin/listener)
    
    public final void onGenericEvent(GenericEvent e)
    {
        //if this event happened in a guild, subject it to
        //allow-listing rules
        if(findEventGuild(e) != null)
        {
            //parse messagereceivedevents that happen in a guild separately as it may be a command
            if(e instanceof MessageReceivedEvent)
                handleMessageAsCommand((MessageReceivedEvent) e);

            AMGN.plugin_listeners.forEach((plugin, listeners) ->
            {
                //if rules allow the plugin to be used, then run the plugin
                if(applyWhitelistBlacklistRules(plugin.getName(), findEventGuild(e)))
                {
                    listeners.forEach(listener ->
                    {
                        if(listener instanceof Listener)
                            executeListener((Listener) listener, e);
                    });
                }
            });
        }
        else //if it's not a guild event, we can just pass the event straight on with no allow-listing
        {
            AMGN.plugin_listeners.values().forEach(pluginlisteners ->
            {
                pluginlisteners.forEach(pluginlistener ->
                {
                    if(pluginlistener instanceof Listener)
                        executeListener((Listener) pluginlistener, e);
                });
            });
        }   
    }

    private final static void handleMessageAsCommand(MessageReceivedEvent e)
    {
        String[] args = e.getMessage().getContentRaw().split(" ");

        //check default commands to be run
        for(DefaultCommand cmd : DefaultCommand.values())
        {
            if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + cmd.getLabel()))
            {
                AMGN.logger.info("Member " + e.getMember().toString() + " is running command \""
                    + e.getMessage().getContentRaw().substring(1) + "\" in channel " + e.getChannel().toString());

                if(cmd.hasPermission(e.getMember()))
                    cmd.getCommandAction().accept(new CommandEvent(e.getMessage().getContentRaw(), e.getMember(), (TextChannel) e.getChannel(), e.getMessage()));
                else
                    AMGN.logger.info("Member " + e.getMember().toString() + " does not have the permission to run command \"" + e.getMessage().getContentRaw().substring(1) + "\"");
            }
        }


        //iterate through all plugins
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            boolean pluginshouldrun = applyWhitelistBlacklistRules(plugin.getName(), e.getGuild());

            //go through all listeners
            listeners.forEach(listener ->
            {
                //also run as a command if it's a command to be run
                if(listener instanceof Command)
                {
                    Command cmd = ((Command) listener);
                    if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + cmd.getLabel()))
                    {
                        AMGN.logger.info("Member " + e.getMember().toString() + " is running command \""
                            + e.getMessage().getContentRaw().substring(1) + "\" in channel " + e.getChannel().toString());

                        //if whitelisting/blacklisting doesn't allow, we want to let the user know and do nothing more
                        if(!pluginshouldrun)
                        {
                            AMGN.logger.info("Network whitelist/blacklist rules do not allow the command \"" + e.getMessage().getContentRaw().substring(1) + "\""
					            + " to be run in the guild " + e.getGuild().toString());
                            return;
                        }

                        if(cmd.hasPermission(e.getMember())) //check if the user has permission to run the command
                        {
                            try
                            {
                                cmd.onCommand(new CommandEvent(e.getMessage().getContentRaw(), e.getMember(), (TextChannel) e.getChannel(), e.getMessage()));
                            }
                            catch(Exception ex)
                            {
                                StringWriter sw = new StringWriter();
                                PrintWriter pw = new PrintWriter(sw);
                                ex.printStackTrace(pw);
        
                                AMGN.logger.error("Error occurred with user-run command\n"
                                    + "User: " + e.getAuthor() + "\n"
                                    + "Channel: " + e.getChannel() + "\n"
                                    + "Guild: " + e.getGuild() + "\n"
                                    + "Command: " + e.getMessage().getContentRaw() + "\n"
                                    + "Stacktrace: \n" + sw.toString()
                                    + "\n");

                                e.getChannel().sendMessage("Error encountered when running this command.").queue();
                            }
                        }
                        else
                            AMGN.logger.info("Member " + e.getMember().toString() + " does not have the permission to run command \"" + e.getMessage().getContentRaw().substring(1) + "\"");
                    }
                }
            });
        });
    }

    private final static Guild findEventGuild(GenericEvent e)
    {
        if(e instanceof GenericGuildEvent)
            return ((GenericGuildEvent) e).getGuild();

        if(e instanceof GenericChannelEvent && ((GenericChannelEvent) e).isFromGuild())
            return ((GenericChannelEvent) e).getGuild();

        if(e instanceof GenericMessageEvent && ((GenericMessageEvent) e).isFromGuild())
            return ((GenericMessageEvent) e).getGuild();

        return null;
    }

    private final static void executeListener(Listener listener, GenericEvent event)
    {
        listener.onGenericEvent(event);
        if(event instanceof UpdateEvent)
            listener.onGenericUpdate((UpdateEvent<?, ?>) event);

        //quickest, cheapest way to check which methods of a listener will accept the event
        for(Method method : listener.getClass().getMethods())
        {
            if(method.getName().equals("on" + event.getClass().getSimpleName().substring(0, event.getClass().getSimpleName().length() - "Event".length()))
                && method.getParameterTypes()[0].equals(event.getClass()))
            {
                try
                {
                    method.invoke(listener, event);
                }
                catch(InvocationTargetException | IllegalAccessException e)
                {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);


                    AMGN.logger.error("Error occurred when calling " + method.getName() + " in class " + listener.getClass().getName() + "\n"
                        + "Event: " + event.toString() + "\n"
                        + "Stacktrace: \n"
                        + sw.toString()
                        + "\n");
                }
            }
        }
    }

    //accepts a guild and returns a list of all plugins that make it through the whitelist/blacklsit rules
    //to run in that guild
    public static final List<Plugin> getRunningPlugins(Guild g)
    {
        ArrayList<Plugin> returnplugins = new ArrayList<Plugin>();

        AMGN.plugin_listeners.keySet().forEach(plugin ->
        {
            if(applyWhitelistBlacklistRules(plugin.getName(), g))
                returnplugins.add(plugin);
        });

        return returnplugins;
    }

    //look at a plugin and guild and apply whitelisting/blacklisting rules to determine if it should run or not
    //this is the actual whitelisting/blacklisting logic
    public static final boolean applyWhitelistBlacklistRules(String plugin, Guild g)
    {
        if(!GuildNetwork.blacklist.isEmpty() && GuildNetwork.whitelist.isEmpty()) //if there is a blacklist but no whitelist
            return !GuildNetwork.blacklist.getOrDefault(plugin, new ArrayList<Long>()).contains(g.getIdLong()); //if it is blacklisted, then we should return false otherwise true
        else if(!GuildNetwork.whitelist.isEmpty()) //if there is a whitelist
        {
            //if it is whitelisted, then we should return true
            boolean shouldrun = GuildNetwork.whitelist.getOrDefault(plugin, new ArrayList<Long>()).contains(g.getIdLong());

            //UNLESS it is also blacklisted, as the order we apply in is whitelist -> blacklist
            if(!GuildNetwork.blacklist.isEmpty() && GuildNetwork.blacklist.getOrDefault(plugin, new ArrayList<Long>()).contains(g.getIdLong()))
                shouldrun = !GuildNetwork.blacklist.getOrDefault(plugin, new ArrayList<Long>()).contains(g.getIdLong());

            return shouldrun;
        }
        else //if there is no blacklist or whitelist
            return true; //run everything
    }
}
