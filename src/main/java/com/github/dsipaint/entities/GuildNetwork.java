package com.github.dsipaint.AMGN.entities;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.dsipaint.AMGN.entities.listeners.Command;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.main.AMGN;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildNetwork
{
	//general utility class, like the Bukkit class in spigot- represents actions available across the whole network
	
	public static Map<Long, Guild> guild_data; //placed here to be globally available, set up in the Main class
	public static List<Long> operators;
	
	public static final int GREEN_EMBED_COLOUR = 65280, RED_EMBED_COLOUR = 16073282; //Embed colours
	public static final String PLUGIN_PATH = "./plugins"; //default plugin path
	public static final String NETWORKINFO_PATH = "./network.json"; //guild info path
	
	//return true if a member has discord mod, admin or is owner
	public static final boolean isStaff(Member m)
	{
		//if owner
		if(m.isOwner())
			return true;
		
		//if admin
		if(m.hasPermission(Permission.ADMINISTRATOR))
			return true;
		
		//modrole for the given server
		Role modrole = m.getGuild().getRoleById(guild_data.get(m.getGuild().getIdLong()).getModrole());
		
		//no modrole, means the user can't be a mod
		if(modrole == null)
			return false;
		
		//if this member has the modrole for this guild, return true
		for(Role r : m.getRoles())
		{
			if(r.equals(modrole))
				return true;
		}
		
		return false;
	}
	
	//ease-of-access methods for retrieving guild data
	public static final String getPrefix(long guild_id)
	{
		return guild_data.get(guild_id) == null ? Guild.DEFAULT_PREFIX : guild_data.get(guild_id).getPrefix();
	}
	
	public static final long getModrole(long guild_id)
	{
		return guild_data.get(guild_id) == null ? Guild.DEFAULT_ID : guild_data.get(guild_id).getModrole();
	}
	
	public static final long getModlogs(long guild_id)
	{
		return guild_data.get(guild_id) == null ? Guild.DEFAULT_ID : guild_data.get(guild_id).getModlogs();
	}
	
	public static final void registerListener(ListenerAdapter listener, Plugin plugin)
	{
		AMGN.bot.addEventListener(listener);
		AMGN.plugin_listeners.get(plugin).add(listener); //add the listener listed under this name
	}
	
	public static final void unregisterListener(ListenerAdapter listener, Plugin plugin)
	{
		AMGN.bot.removeEventListener(listener);
		AMGN.plugin_listeners.get(plugin).remove(listener); //remove listener from hashmap and from jda
	}
	
	//works just as registerListener does, just more syntax-sense for a user to register commands separately to listeners
	public static final void registerCommand(Command cmd, Plugin plugin)
	{
		AMGN.bot.addEventListener(cmd);
		AMGN.plugin_listeners.get(plugin).add(cmd);
	}
	
	//returns TRUE if this plugin was enabled
	public static final boolean enablePlugin(Plugin plugin)
	{
		//if plugin is not already enabled
		if(AMGN.plugin_listeners.get(plugin) == null)
		{
			AMGN.plugin_listeners.put(plugin, new ArrayList<ListenerAdapter>()); //add this plugin with an empty list of listeners
			//(listeners are then added by GuildNetwork.registerListener method, separately)
			plugin.onEnable(); //run plugin's enable method
			return true;
		}
		else
			return false;
	}
	
	//returns TRUE if this plugin was disabled
	public static final boolean disablePlugin(Plugin plugin)
	{
		//if plugin is not already enabled
		if(AMGN.plugin_listeners.get(plugin) == null)
			return false;
		else
		{
			//disable each of the listeners this plugin registers
			AMGN.plugin_listeners.get(plugin).forEach(listener ->
			{
				AMGN.bot.removeEventListener(listener); //NOTE: may not properly remove listener, may need to cast to ListenerAdapter?
			});
			
			AMGN.plugin_listeners.remove(plugin); //remove plugin from the list
			plugin.onDisable(); //run plugin's onDisable method
			return true;
		}
	}
	
	public static final void sendToModlogs(long guild_id, String msg)
	{
		if(guild_id == Guild.DEFAULT_ID) //for now, don't do anything with default values
			return;
		
		long modlogs = getModlogs(guild_id);
		
		if(modlogs == Guild.DEFAULT_ID)
			return;
		
		AMGN.bot.getTextChannelById(modlogs).sendMessage(new EmbedBuilder()
				.setTitle("AMGN")
				.setColor(GREEN_EMBED_COLOUR)
				.setDescription(msg)
				.setTimestamp(Instant.now())
				.build()).queue();
	}
}
