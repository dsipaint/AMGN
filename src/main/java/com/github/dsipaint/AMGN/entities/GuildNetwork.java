package com.github.dsipaint.AMGN.entities;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.listeners.IListener;
import com.github.dsipaint.AMGN.entities.listeners.RestListener;
import com.github.dsipaint.AMGN.entities.listeners.managed.Command;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.io.Permissions;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ISnowflake;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

public class GuildNetwork
{
	//general utility class, like the Bukkit class in spigot- represents actions available across the whole network
	
	public static Map<Long, Guild> guild_data; //placed here to be globally available, set up in the Main class
	public static String clientid, clientsecret, redirecturi;
	public static Map<String, List<Long>> whitelist, blacklist;
	
	public static final String PLUGIN_PATH = AMGN.getWorkingDirectory() + "/plugins"; //default plugin path
	public static final String NETWORKINFO_PATH = AMGN.getWorkingDirectory() + "/network.yml"; //guild info path
	public static final String WHITELIST_PATH = AMGN.getWorkingDirectory() + "/whitelist.yml"; //whitelist file path
	public static final String WEB_PATH = AMGN.getWorkingDirectory() + "/web"; //path for all web assets
	public static final String PERMISSIONS_PATH = AMGN.getWorkingDirectory() + "/permissions.yml"; //for the permissions file

	public static final String DEFAULT_MEMBERCACHEPOLICY = "default"; //default member cache policy
	public static final List<String> DEFAULT_CACHEFLAGS = Arrays.asList("online_status", "activity"); //default cache-flags

	public static final String ID_REGEX = "\\d{17,19}";

	/** 
	 * @param m member to check operator status of
	 * @return boolean true if member is an operator, or has any role that has operator perms, false otherwise
	 */
	public static final boolean isOperator(User u)
	{
		return Permissions.userHasPermission(u, null, "AMGN.operator");
	}

	public static final boolean isOperator(Role r)
	{
		return Permissions.roleHasPermission(r, "AMGN.operator");
	}

	public static User fetchUser(String id)
	{
		User returnuser = null;

		try
		{
			returnuser = AMGN.bot.retrieveUserById(id).complete();
		}
		catch(ErrorResponseException e)
		{
			returnuser = null;
		}

		return returnuser;
	}

	public static Member fetchMember(User u, net.dv8tion.jda.api.entities.Guild guild)
	{
		Member returnmember = null;

		try
		{
			returnmember = guild.retrieveMember(u).complete();
		}
		catch(ErrorResponseException e)
		{
			returnmember = null;
		}

		return returnmember;
	}

	public static Member fetchMember(String user, net.dv8tion.jda.api.entities.Guild guild)
	{
		Member returnmember = null;

		try
		{
			returnmember = guild.retrieveMemberById(user).complete();
		}
		catch(ErrorResponseException e)
		{
			returnmember = null;
		}

		return returnmember;
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
	public static final void registerListener(IListener listener, Plugin plugin)
	{
		if(plugin == null)
			return;
		
		//for RestListeners, don't add to AMGN
		//and also only allow 1 RestListener to be added- replace the old RestListener with the new one if there is already one
		if(listener instanceof RestListener)
		{
			//look for an existing restlistener associated with the plugin
			RestListener alreadyRestListener = null;
			for(IListener checklistener : AMGN.plugin_listeners.get(plugin))
			{
				if(checklistener instanceof RestListener)
				{
					alreadyRestListener = (RestListener) checklistener;
					break;
				}
			}

			//if we find one that already exists, remove it
			if(alreadyRestListener != null)
				AMGN.plugin_listeners.get(plugin).remove(alreadyRestListener);
		}

		AMGN.plugin_listeners.get(plugin).add(listener); //add the listener listed under this name
	}
	
	
	/** 
	 * @param listener ListenerAdapter to formally be unloaded by AMGN
	 * @param plugin Plugin to find the listener with
	 */
	public static final void unregisterListener(IListener listener, Plugin plugin)
	{
		AMGN.bot.removeEventListener(listener);
		
		if(plugin != null)
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
			AMGN.plugin_listeners.put(plugin, new ArrayList<IListener>()); //add this plugin with an empty list of listeners
			//(listeners are then added by GuildNetwork.registerListener method, separately)
			plugin.onEnable(); //run plugin's enable method
			//menus will sort themselves out when the plugin recreates them
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
		//if plugin is already disabled
		if(AMGN.plugin_listeners.get(plugin) == null)
			return false;
		else
		{
			plugin.onDisable(); //disable plugin
			AMGN.plugin_listeners.get(plugin).forEach(AMGN.bot::removeEventListener); //remove listeners
			AMGN.menucache.forEach(menu ->
			{
				if(menu.getPlugin().equals(plugin))
					menu.softDestroy();
			});
			AMGN.menucache.removeIf(menu -> {return menu.getPlugin().equals(plugin);});//remove menus

			AMGN.plugin_listeners.remove(plugin);
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

	public static final ISnowflake resolveEntity(String id)
	{
		ISnowflake entity;

		//I've tried to put these in an order of most likely to be used to
		//least likely to be used, to improve performance a tad
		if((entity = AMGN.bot.getUserById(id)) != null) //TODO update to a retrieve?
			return entity;
		if((entity = AMGN.bot.getGuildChannelById(id)) != null)
			return entity;
		if((entity = AMGN.bot.getRoleById(id)) != null)
			return entity;
		if((entity = AMGN.bot.getTextChannelById(id)) != null)
			return entity;
		if((entity = AMGN.bot.getVoiceChannelById(id)) != null)
			return entity;
		if((entity = AMGN.bot.getEmojiById(id)) != null)
			return entity;
		if((entity = AMGN.bot.getCategoryById(id)) != null)
			return entity;
		if((entity = AMGN.bot.getGuildById(id)) != null)
			return entity;
		if((entity = AMGN.bot.getForumChannelById(id)) != null)
			return entity;
		if((entity = AMGN.bot.getStageChannelById(id)) != null)
			return entity;
		if((entity = AMGN.bot.getNewsChannelById(id)) != null)
			return entity;
		if((entity = AMGN.bot.getThreadChannelById(id)) != null)
			return entity;
		if((entity = AMGN.bot.getPrivateChannelById(id)) != null)
			return entity;

		//after trying everything else, cast to scheduledevent (could also be null if nothing found)
		//and return result.
		entity = AMGN.bot.getScheduledEventById(id);
		return entity;
	}
}
