package com.github.dsipaint.AMGN.entities;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.listeners.Command;
import com.github.dsipaint.AMGN.entities.listeners.Listener;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

public class GuildNetwork
{
	//general utility class, like the Bukkit class in spigot- represents actions available across the whole network
	
	public static Map<Long, Guild> guild_data; //placed here to be globally available, set up in the Main class
	public static List<Long> operators;
	public static String clientid, clientsecret, redirecturi;
	public static Map<String, List<Long>> whitelist, blacklist;
	
	public static final String PLUGIN_PATH = "./plugins"; //default plugin path
	public static final String NETWORKINFO_PATH = "./network.yml"; //guild info path
	public static final String WHITELIST_PATH = "./whiteslist.yml"; //whitelist file path
	public static final String WEB_PATH = "./web"; //path for all web assets

	public static final String ID_REGEX = "\\d{17,19}";
	
	
	/** 
	 * @param m member to check staff status of
	 * @return boolean true if member has discord mod, admin or is owner, false otherwise
	 */
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

	/** 
	 * @param m member to check operator status of
	 * @return boolean true if member is an operator, false otherwise
	 */
	public static final boolean isOperator(User u)
	{
		for(long id : operators)
		{
			if(AMGN.bot.getUserById(id) != null)
			{
				if(AMGN.bot.getUserById(id).equals(u))
					return true;
				continue;
			}
			
			if(AMGN.bot.getRoleById(id) != null)
			{
				Member m = AMGN.bot.getRoleById(id).getGuild().getMember(u);
				if(m != null && m.getRoles().contains(AMGN.bot.getRoleById(id)))
					return true;
			}
		}

		return false;
	}
	
	//ease-of-access methods for retrieving guild data
	
	/** 
	 * @param guild_id guild to find prefix for
	 * @return String prefix for the guild
	 */
	public static final String getPrefix(long guild_id)
	{
		return guild_data.get(guild_id) == null ? Guild.DEFAULT_PREFIX : guild_data.get(guild_id).getPrefix();
	}
	
	
	/** 
	 * @param guild_id guild to find modrole for
	 * @return long id of mod role
	 */
	public static final long getModrole(long guild_id)
	{
		return guild_data.get(guild_id) == null ? Guild.DEFAULT_ID : guild_data.get(guild_id).getModrole();
	}
	
	
	/** 
	 * @param guild_id guild to find modlogs id for
	 * @return long id of modlogs channel
	 */
	public static final long getModlogs(long guild_id)
	{
		return guild_data.get(guild_id) == null ? Guild.DEFAULT_ID : guild_data.get(guild_id).getModlogs();
	}

	/** 
	 * @param guild_id guild to find modlogs id for
	 * @return int accept colour set for the guild
	 */
	public static final int getAccept_col(long guild_id)
	{
		return guild_data.getOrDefault(guild_id, new Guild(guild_id)).getAccept_col();
	}

	/** 
	 * @param guild_id guild to find modlogs id for
	 * @return int decline colour set for the guild
	 */
	public static final int getDecline_col(long guild_id)
	{
		return guild_data.getOrDefault(guild_id, new Guild(guild_id)).getDecline_col();
	}

	/** 
	 * @param guild_id guild to find modlogs id for
	 * @return int unique colour set for the guild
	 */
	public static final int getUnique_col(long guild_id)
	{
		return guild_data.getOrDefault(guild_id, new Guild(guild_id)).getUnique_col();
	}
	
	
	/** 
	 * @param listener ListenerAdapter to formally be loaded by AMGN
	 * @param plugin Plugin to associate the listener with
	 */
	public static final void registerListener(Listener listener, Plugin plugin)
	{
		AMGN.bot.addEventListener(listener);
		AMGN.plugin_listeners.get(plugin).add(listener); //add the listener listed under this name
	}
	
	
	/** 
	 * @param listener ListenerAdapter to formally be unloaded by AMGN
	 * @param plugin Plugin to find the listener with
	 */
	public static final void unregisterListener(Listener listener, Plugin plugin)
	{
		AMGN.bot.removeEventListener(listener);
		AMGN.plugin_listeners.get(plugin).remove(listener); //remove listener from hashmap and from jda
	}
	
	
	/**
	 * works just as registerListener does, just more syntax-sense for a user to register commands separately to listeners 
	 * @param cmd Command to be formally loaded and recognised by AMGN and its support features
	 * @param plugin Plugin to associate the command with
	 */
	public static final void registerCommand(Command cmd, Plugin plugin)
	{
		AMGN.bot.addEventListener(cmd);
		AMGN.plugin_listeners.get(plugin).add(cmd);
	}
	
	
	/** 
	 * @param plugin Plugin object to enable
	 * @return boolean true if this plugin was enabled, false otherwise
	 */
	public static final boolean enablePlugin(Plugin plugin)
	{
		//if plugin is not already enabled
		if(AMGN.plugin_listeners.get(plugin) == null)
		{
			AMGN.plugin_listeners.put(plugin, new ArrayList<Listener>()); //add this plugin with an empty list of listeners
			//(listeners are then added by GuildNetwork.registerListener method, separately)
			plugin.onEnable(); //run plugin's enable method
			return true;
		}
		else
			return false;
	}
	
	
	/** 
	 * @param plugin Plugin object to disable
	 * @return boolean true if this plugin was disabled, false otherwise
	 */
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
	
	
	/** 
	 * @param guild_id the id of the guild you wish to write a mod log to
	 * @param msg the log to send to the modlogs channel
	 */
	public static final void sendToModlogs(long guild_id, String msg)
	{
		if(guild_id == Guild.DEFAULT_ID) //for now, don't do anything with default values
			return;
		
		long modlogs = getModlogs(guild_id);
		
		if(modlogs == Guild.DEFAULT_ID)
			return;
		
		AMGN.bot.getTextChannelById(modlogs).sendMessageEmbeds(new EmbedBuilder()
				.setTitle("AMGN")
				.setColor(guild_data.get(guild_id).getAccept_col())
				.setDescription(msg)
				.setTimestamp(Instant.now())
				.build()).queue();
	}
}
