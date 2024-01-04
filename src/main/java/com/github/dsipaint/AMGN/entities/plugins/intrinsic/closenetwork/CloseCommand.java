package com.github.dsipaint.AMGN.entities.plugins.intrinsic.closenetwork;

import java.util.function.Consumer;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.CommandEvent;

public final class CloseCommand implements Consumer<CommandEvent>
{	
	public void accept(CommandEvent e)
	{	
		//disable every active plugin
		AMGN.plugin_listeners.forEach((plugin, listenerlist) ->
		{
			plugin.onDisable(); //don't use GuildNetwork.disablePlugin due to concurrency issues accessing this hashmap/arraylists in the hashmap
			listenerlist.forEach(listener -> {AMGN.bot.removeEventListener(listener);});
		});
		//run disable methods and unregister commands/listeners
		
		//log in modlogs
		GuildNetwork.guild_data.keySet().forEach(guild_id -> {
			GuildNetwork.sendToModlogs(guild_id, "Network shutdown by " + e.getSender().getUser().getName());
		});
		
		e.getTextChannel().sendMessage("Disabled all plugins. Shutting down...").complete();
		AMGN.bot.shutdown();
		System.exit(0); //close program
	}
}
