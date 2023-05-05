package com.github.dsipaint.AMGN.entities;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.listeners.Command;
import com.github.dsipaint.AMGN.entities.listeners.Listener;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.io.IOHandler;
import com.github.dsipaint.AMGN.io.Permissions;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

public class GuildNetwork
{
	//general utility class, like the Bukkit class in spigot- represents actions available across the whole network
	
	public static Map<Long, Guild> guild_data; //placed here to be globally available, set up in the Main class
	public static String clientid, clientsecret, redirecturi;
	public static Map<String, List<Long>> whitelist, blacklist;
	
	public static final String PLUGIN_PATH = "./plugins"; //default plugin path
	public static final String NETWORKINFO_PATH = "./network.yml"; //guild info path
	public static final String WHITELIST_PATH = "./whitelist.yml"; //whitelist file path
	public static final String WEB_PATH = "./web"; //path for all web assets
	public static final String PERMISSIONS_PATH = "./permissions.yml"; //for the permissions file

	public static final String ID_REGEX = "\\d{17,19}";

	/** 
	 * @param m member to check operator status of
	 * @return boolean true if member is an operator, or has any role that has operator perms, false otherwise
	 */
	@SuppressWarnings("unchecked")
	public static final boolean isOperator(User u)
	{
		//we must manually check operator status, because Command.hasPermission relies
		//on this method. Re-using Command.hasPermission will cause unwanted recursion.
		//read permissions.yml
		Map<String, List<String>> perms = new HashMap<String, List<String>>();
		try
		{
			//check all groups and see if a group has the permission
			HashMap<String, Object> groups = (HashMap<String, Object>) IOHandler.readYamlData(GuildNetwork.PERMISSIONS_PATH, "groups");
			for(String groupname : groups.keySet())
			{
				HashMap<String, List<String>> groupdata = (HashMap<String, List<String>>) groups.get(groupname);
				//if this is the user's id or a role the user has
				if(Permissions.isInGroup(u.getId(), groupname))
				{
					for(String perm : groupdata.get("permissions"))
					{
						if(perm.equalsIgnoreCase("AMGN.operator"))
							return true;
					}
				}
			}

			perms = new Yaml().load(
				new FileInputStream(GuildNetwork.PERMISSIONS_PATH));
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}

		//check to see if an id is either a member, or a role the member has (in any guild)
		//if this ID has been given the specified permission, return true
		
		for(String id : perms.keySet())
		{
			if(id.equalsIgnoreCase("groups"))
				continue;

			//we must check every role that the user may have,
			//if it has operator perms, they are a global operator

			//if this is a role ID and the user has this role
			if(u.getId().equals(id)
				|| (AMGN.bot.getRoleById(id) != null
						&& AMGN.bot.getRoleById(id).getGuild().getMembersWithRoles(AMGN.bot.getRoleById(id))
						.contains(AMGN.bot.getRoleById(id).getGuild().getMember(u)))
				|| (u.getMutualGuilds().contains(AMGN.bot.getGuildById(id))))
			{
				//if this role has op perms, return true
				for(String commandperm : perms.get(id))
				{
					if(commandperm.equals("AMGN.operator"))
						return true;
				}
			}
		}
		
		return false;
	}

	@SuppressWarnings("unchecked")
	public static final boolean isOperator(Role r)
	{
		Map<String, List<String>> perms = new HashMap<String, List<String>>();
		try
		{
			//check all groups and see if a group has the permission
			HashMap<String, Object> groups = (HashMap<String, Object>) IOHandler.readYamlData(GuildNetwork.PERMISSIONS_PATH, "groups");
			for(String groupname : groups.keySet())
			{
				HashMap<String, List<String>> groupdata = (HashMap<String, List<String>>) groups.get(groupname);
				if(Permissions.isInGroup(r.getId(), groupname))
				{
					for(String perm : groupdata.get("permissions"))
					{
						if(perm.equalsIgnoreCase("AMGN.operator"))
							return true;
					}
				}
			}

			perms = new Yaml().load(
				new FileInputStream(GuildNetwork.PERMISSIONS_PATH));
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}

		//check to see if an id is either a member, or a role the member has (in any guild)
		//if this ID has been given the specified permission, return true
		for(String id : perms.keySet())
		{
			if(id.equalsIgnoreCase("groups"))
				continue;
			
			if(r.getId().equals(id)
				|| r.getGuild().getId().equals(id))
			{
				for(String commandperm : perms.get(id))
				{
					if(commandperm.equals("AMGN.operator"))
						return true;
				}
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
