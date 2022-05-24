package com.github.dsipaint.AMGN.entities.plugins.intrinsic.closenetwork;

import com.github.dsipaint.AMGN.entities.Guild;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.Command;
import com.github.dsipaint.AMGN.entities.listeners.DefaultCommand;
import com.github.dsipaint.AMGN.main.AMGN;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public final class CloseListener extends ListenerAdapter
{	
	public void onMessageReceived(MessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");

		if(!e.isFromGuild())
			return;
		
		//^closenetwork
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + DefaultCommand.CLOSE.getLabel())
				&& Command.hasPermission(e.getMember(), DefaultCommand.CLOSE.getGuildPermission()))
		{
			//disable every active plugin
			AMGN.plugin_listeners.forEach((plugin, listenerlist) ->
			{
				plugin.onDisable(); //don't use GuildNetwork.disablePlugin due to concurrency issues accessing this hashmap/arraylists in the hashmap
				listenerlist.forEach(listener -> {AMGN.bot.removeEventListener(listener);});
			});
			//run disable methods and unregister commands/listeners
			
			//log in modlogs
			long id = GuildNetwork.guild_data.containsKey(e.getGuild().getIdLong()) ?  e.getGuild().getIdLong() : Guild.DEFAULT_ID;
			GuildNetwork.sendToModlogs(id, "Network shutdown by " + e.getAuthor().getAsTag());
			
			e.getChannel().sendMessage("Disabled all plugins. Shutting down...").complete();
			e.getJDA().shutdown();
			System.exit(0); //close program
		}
	}
}
