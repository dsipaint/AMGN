package com.github.dsipaint.AMGN.entities.plugins.intrinsic.running;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.CommandEvent;
import com.github.dsipaint.AMGN.entities.listeners.managed.menu.ScrollMenuBuilder;
import com.github.dsipaint.AMGN.entities.listeners.managed.menu.MenuBuilder.InvalidMenuException;

import net.dv8tion.jda.api.EmbedBuilder;

public class ShowAllPluginsCommand implements Consumer<CommandEvent>
{
    public void accept(CommandEvent e)
	{
		EmbedBuilder eb = new EmbedBuilder()
				.setTitle("Active plugins in network: ")
				.setColor(GuildNetwork.guild_data.get(e.getGuild().getIdLong()).getAccept_col());
		
		StringBuilder descriptionsb = new StringBuilder();
		AMGN.plugin_listeners.forEach((plugin, listeners) ->
		{
			descriptionsb.append(plugin.getName() + " " + plugin.getVersion() + " (written by " + plugin.getAuthor() + ")\n");
		});
		
		try
		{
			ScrollMenuBuilder scrollmenu = new ScrollMenuBuilder(null, descriptionsb.toString(), "\n",
				eb, e.getTextChannel());
			scrollmenu.getBuilder().setTimeoutSoftDestroy(5, TimeUnit.MINUTES);
			scrollmenu.alphabetiseDescription();
			scrollmenu.build();
		}
		catch(InvalidMenuException e1)
		{
			e1.printStackTrace();
		}
    }
}
