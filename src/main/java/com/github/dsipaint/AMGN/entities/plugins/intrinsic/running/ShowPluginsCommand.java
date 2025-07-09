package com.github.dsipaint.AMGN.entities.plugins.intrinsic.running;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.CommandEvent;
import com.github.dsipaint.AMGN.entities.listeners.managed.ListenerProxy;
import com.github.dsipaint.AMGN.entities.listeners.managed.menu.MenuBuilder.InvalidMenuException;
import com.github.dsipaint.AMGN.entities.listeners.managed.menu.ScrollMenuBuilder;
import com.github.dsipaint.AMGN.io.IOHandler;

import net.dv8tion.jda.api.EmbedBuilder;

public final class ShowPluginsCommand implements Consumer<CommandEvent>
{
	public void accept(CommandEvent e)
	{
		EmbedBuilder eb = new EmbedBuilder()
			.setTitle("Active plugins in " + e.getGuild().getName() + ": ")
			.setColor(GuildNetwork.guild_data.get(e.getGuild().getIdLong()).getAccept_col())
			.setAuthor("AMGN " + getAMGNVersion());

		StringBuilder descriptionsb = new StringBuilder();
		ListenerProxy.getRunningPlugins(e.getGuild()).forEach(plugin ->
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

	public static String getAMGNVersion()
	{
		ClassLoader cl = new IOHandler().getClass().getClassLoader();
		InputStream inputstream = cl.getResourceAsStream("META-INF/MANIFEST.MF");

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
		for(String line : reader.lines().toList())
		{
			if(line.startsWith("version: "))
				return line.split(": ")[1];
		}
		
		return "";
	}
}
