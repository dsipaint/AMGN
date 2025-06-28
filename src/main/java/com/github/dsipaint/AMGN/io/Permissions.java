package com.github.dsipaint.AMGN.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;

public class Permissions
{
    /*
        This is a class to house all the permission-checking methods
    */

    /** 
	 * @param m member to check permissions for
	 * @param permission permission to check if the member has
	 * @return boolean true if the member has permission to run this command, as specified in plugin.yml, false otherwise
	 * 
	 * this is guild-agnostic because we're checking a general permission, so Guild can be null
	 * if a guild is passed, then we will check for roles in that guild that the user has, that has the permission
	 * if no guild is passed, then we will not check guilds
	 */
    @SuppressWarnings("unchecked")
	public static final boolean hasPermission(User u, Guild context, String permission)
	{
		//operators always have permission
		if(GuildNetwork.isOperator(u))
			return true;

		//read permissions.yml
		Map<String, List<String>> perms = new HashMap<String, List<String>>();
		try
		{
			//check all groups and see if a group has the permission
			HashMap<String, Object> groups = (HashMap<String, Object>) IOHandler.readYamlData(GuildNetwork.PERMISSIONS_PATH, "groups");
			for(String groupname : groups.keySet())
			{
				if(isInGroup(u.getId(), groupname)
					&& hasPermission(groupname, permission))
					return true;
			}

			perms = new Yaml().load(
				new FileInputStream(GuildNetwork.PERMISSIONS_PATH));
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}

		//check to see if an id is either a member, or a role the member has (in this guild!!)
		//if this ID has been given the specified permission, return true
		for(String id : perms.keySet())
		{
			if(id.equalsIgnoreCase("groups"))
				continue;

			if(u.getId().equals(id) ||
				(context != null && context.getRoleById(id) != null && context.getMember(u) != null && context.getMember(u).getRoles().contains(context.getRoleById(id)))
				|| (context != null && context.getId().equals(id)))
			{
				for(String commandperm : perms.get(id))
				{
					if(commandperm.equals(permission))
						return true;
				}
			}
		}
		
		return false;
	}

    @SuppressWarnings("unchecked")
	public static final boolean hasPermission(Role r, String permission)
	{
		//operators always have permission
		if(GuildNetwork.isOperator(r))
			return true;

		//read permissions.yml
		Map<String, List<String>> perms = new HashMap<String, List<String>>();
		try
		{
			//check all groups and see if a group has the permission
			HashMap<String, Object> groups = (HashMap<String, Object>) IOHandler.readYamlData(GuildNetwork.PERMISSIONS_PATH, "groups");
			for(String groupname : groups.keySet())
			{
				if(isInGroup(r.getId(), groupname)
					&& hasPermission(groupname, permission))
					return true;
			}

			perms = new Yaml().load(
				new FileInputStream(GuildNetwork.PERMISSIONS_PATH));
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}

		//check to see if an id is either a member, or a role the member has (in this guild!!)
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
					if(commandperm.equals(permission))
						return true;
				}
			}
		}
		
		return false;
	}

	//check if a group has permission
	//really this is only designed for internal use
    @SuppressWarnings("unchecked")
	public static final boolean hasPermission(String group, String permission)
	{
		try
		{
			HashMap<String, Object> groups = (HashMap<String, Object>) IOHandler.readYamlData(GuildNetwork.PERMISSIONS_PATH, "groups");
			for(String groupname : groups.keySet())
			{
				if(group.equalsIgnoreCase(groupname))
				{
					HashMap<String, List<String>> groupdata = (HashMap<String, List<String>>) groups.get(groupname);
					for(String perm : groupdata.get("permissions"))
					{
						if(perm.equalsIgnoreCase(permission))
							return true;
					}

					return false;
				}
			}
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}

		return false;
	}

	//really this is only designed for internal use
    @SuppressWarnings("unchecked")
	public static final boolean isInGroup(String id, String group)
	{
		//everyone is in the default group
		if(group.equalsIgnoreCase("default"))
			return true;

		try
		{
			HashMap<String, Object> groups = (HashMap<String, Object>) IOHandler.readYamlData(GuildNetwork.PERMISSIONS_PATH, "groups");
			for(String groupname : groups.keySet())
			{
				if(group.equalsIgnoreCase(groupname))
				{
					HashMap<String, List<String>> groupdata = (HashMap<String, List<String>>) groups.get(groupname);
					for(String groupmember : groupdata.get("members"))
					{
						//check if it directly is a member
						if(groupmember.equalsIgnoreCase(id))
							return true;

						//check if this is a role ID, and if the id is a member of this role
						if(AMGN.bot.getRoleById(groupmember) != null
							&& AMGN.bot.getRoleById(groupmember).getGuild().getMembersWithRoles(AMGN.bot.getRoleById(groupmember))
								.contains(AMGN.bot.getRoleById(groupmember).getGuild().getMemberById(id)))
								return true;

						//check if this is a guild ID, and if the id is a role or a member of this guild
						if(AMGN.bot.getGuildById(groupmember) != null
							&& (AMGN.bot.getGuildById(groupmember).getRoleById(id) != null
								|| AMGN.bot.getGuildById(groupmember).getMemberById(id) != null))
							return true;
					}

					return false;
				}
			}
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}

		return false;
	}
}