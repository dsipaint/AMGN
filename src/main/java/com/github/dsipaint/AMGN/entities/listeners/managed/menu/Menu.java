package com.github.dsipaint.AMGN.entities.listeners.managed.menu;

import java.time.Instant;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

import net.dv8tion.jda.api.entities.Message;

public class Menu
{
    private static ScheduledThreadPoolExecutor destroyer = new ScheduledThreadPoolExecutor(3);

    private Plugin plugin;
    private Message message;
    private Button[] buttons;

    private Instant destroytime, softdestroytime;

    private ScheduledFuture<?> destroytask, softdestroytask;
    private Consumer<Menu> aftersoftdestroy, afterdestroy;

    public Menu(Plugin plugin, Message message, long timeoutsoftdestroy, long timeoutdestroy, TimeUnit unittimeoutsoftdestroy,
        TimeUnit unittimeoutdestroy, Consumer<Menu> aftersoftdestroy, Consumer<Menu> afterdestroy, Button... buttons)
    {
        this.plugin = plugin;
        this.message = message;
        this.buttons = buttons;

        //instantiate destroytime & softdestroytime
        //(really these are used for reference rather than the actual scheduling)
        softdestroytime = timeoutsoftdestroy > 0 ?
            Instant.now().plus(timeoutsoftdestroy, unittimeoutsoftdestroy.toChronoUnit())
            :
            Instant.MIN;

        destroytime = timeoutdestroy > 0 ?
            Instant.now().plus(timeoutdestroy, unittimeoutdestroy.toChronoUnit())
            :
            Instant.MIN;
        

        //schedule destroys if needed
        if(timeoutsoftdestroy > 0)
            softdestroytask = destroyer.schedule(this::softDestroy, timeoutsoftdestroy, unittimeoutsoftdestroy);

        if(timeoutdestroy > 0)
            destroytask = destroyer.schedule(this::destroy, timeoutdestroy, unittimeoutdestroy);


        this.aftersoftdestroy = aftersoftdestroy;
        this.afterdestroy = afterdestroy;

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

    public Instant getDestroyTime()
    {
        return this.destroytime;
    }

    public Instant getSoftDestroyTime()
    {
        return this.softdestroytime;
    }

    //will destroy the menu, deleting the message and removing the button listeners
    public void destroy()
    {
        this.message.delete().queue();

        AMGN.menucache.remove(this); //remove from the cache so the consistency plugin doesn't try to get it
        //remove listeners
        for(Button button : buttons)
            GuildNetwork.unregisterListener(button, this.plugin);

        //execute anything requested to be done after destroying the menu
        if(this.afterdestroy != null)
            this.afterdestroy.accept(this);
    }

    //takes off the buttons without deleting the message
    public void softDestroy()
    {
        for(Button button : buttons)
            GuildNetwork.unregisterListener(button, this.plugin);

        AMGN.menucache.remove(this); //remove so that consistency plugin doesn't check this any more

        if(this.aftersoftdestroy != null)
            this.aftersoftdestroy.accept(this);
    }

    public void cancelDestroy()
    {
        if(destroytask == null)
            return;

        destroytask.cancel(false);
    }

    public void cancelSoftDestroy()
    {
        if(softdestroytask == null)
            return;

        softdestroytask.cancel(false);
    }
}
