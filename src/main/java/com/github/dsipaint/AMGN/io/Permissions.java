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
	public static final boolean userHasPermission(User u, Guild context, String permission)
	{
		//if we're not already checking operator permissions, check them now
		//operator perms is an instant return-true
		if(!permission.equalsIgnoreCase("AMGN.operator") && userHasPermission(u, context, "AMGN.operator"))
			return true;

		//read permissions.yml
		Map<String, Object> perms = new HashMap<String, Object>();
		try
		{
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
			{
				HashMap<String, List<String>> groups = (HashMap<String, List<String>>) perms.get(id);

				for(String groupname : groups.keySet())
				{
					//if we're checking operator perms, context is never relevant.
					if(isInGroup(u.getId(), permission.equalsIgnoreCase("AMGN.operator") ? null : context, groupname)
						&& groupHasPermission(groupname, permission))
							return true;
				}

				continue;
			}

			//if the ID we're checking is the user ID, and it has the perm
			if(u.getId().equals(id))
			{
				for(String checkperm : (List<String>) perms.get(id))
				{
					if(checkperm.equals(permission) || checkperm.equalsIgnoreCase("AMGN.operator"))
						return true;
				}
			}

			if(context != null)
			{
				if(
					//if it's a role ID and the role is in the context
					//and the user is in the context
					//and the user has this role
					(
						context.getRoleById(id) != null
							&& context.getMember(u) != null
							&& context.getMember(u).getRoles().contains(context.getRoleById(id))
					)
					|| //OR
					(
						//if it's a guild ID and it's the context-guild
						//and the user is in this guild
						context.getId().equals(id)
							&& context.getMember(u) != null
					))
				{
					for(String checkperm : (List<String>) perms.get(id))
					{
						if(checkperm.equals(permission) || checkperm.equalsIgnoreCase("AMGN.operator"))
							return true;
					}
				}
			}
			else
			{
				if(
					//if it's a role ID
					//and the user is in the role's guild
					//and the user has this role, wherever it is
					(
						AMGN.bot.getRoleById(id) != null
							&& AMGN.bot.getRoleById(id).getGuild().getMember(u) != null
							&& AMGN.bot.getRoleById(id).getGuild().getMember(u).getRoles().contains(AMGN.bot.getRoleById(id))
					)
					|| //OR
					//if it's a guild ID
					//and the user is in this guild at all
					(
						AMGN.bot.getGuildById(id) != null
							&& AMGN.bot.getGuildById(id).getMember(u) != null
					)
				)
				{
					for(String checkperm : (List<String>) perms.get(id))
					{
						if(checkperm.equals(permission) || checkperm.equalsIgnoreCase("AMGN.operator"))
							return true;
					}
				}
			}
		}
		
		return false;
	}

    @SuppressWarnings("unchecked")
	public static final boolean roleHasPermission(Role r, String permission)
	{
		//if we're not already checking operator permissions, check them now
		//operator perms is an instant return-true
		if(!permission.equalsIgnoreCase("AMGN.operator") && roleHasPermission(r, permission))
			return true;

		//read permissions.yml
		Map<String, Object> perms = new HashMap<String, Object>();
		try
		{
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
			{
				HashMap<String, List<String>> groups = (HashMap<String, List<String>>) perms.get(id);

				for(String groupname : groups.keySet())
				{
					if(isInGroup(r.getId(), null, groupname)
						&& groupHasPermission(groupname, permission))
							return true;
				}

				continue;
			}

			if(r.getId().equals(id)
				|| r.getGuild().getId().equals(id))
			{
				for(String checkperm : (List<String>) perms.get(id))
				{
					if(checkperm.equals(permission) || checkperm.equalsIgnoreCase("AMGN.operator"))
						return true;
				}
			}
		}
		
		return false;
	}

	//check if a group has permission
	//really this is only designed for internal use
    @SuppressWarnings("unchecked")
	public static final boolean groupHasPermission(String group, String permission)
	{
		try
		{
			HashMap<String, Object> groups = (HashMap<String, Object>) IOHandler.readYamlData(GuildNetwork.PERMISSIONS_PATH, "groups");
			for(String groupname : groups.keySet())
			{
				if(group.equalsIgnoreCase(groupname))
				{
					HashMap<String, List<String>> groupdata = (HashMap<String, List<String>>) groups.get(groupname);
					for(String checkperm : groupdata.get("permissions"))
					{
						if(checkperm.equalsIgnoreCase(permission) || checkperm.equalsIgnoreCase("AMGN.operator"))
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

    @SuppressWarnings("unchecked")
	public static final boolean isInGroup(String id, Guild context, String group)
	{
		//everyone is in the default group
		if(group.equalsIgnoreCase("default"))
			return true;

		try
		{
			HashMap<String, Object> groups = (HashMap<String, Object>) IOHandler.readYamlData(GuildNetwork.PERMISSIONS_PATH, "groups");
			HashMap<String, List<String>> checkgroup = (HashMap<String, List<String>>) groups.get(group);
			
			for(String groupmember : checkgroup.get("members"))
			{
				//check if the id directly is a member of the group
				if(groupmember.equalsIgnoreCase(id))
					return true;

				//check if this group member a role ID, and if the id is a member of this role


				if(context != null)
				{
					if(
						(
							//if the group member is a role in the context
							//and the id is a member in the context
							//and the member has the role
							context.getRoleById(groupmember) != null
								&& context.getRoleById(groupmember).getGuild().getMemberById(id) != null
								&&  context.getRoleById(groupmember).getGuild().getMemberById(id).getRoles().contains(context.getRoleById(groupmember))
						)
						|| //OR
						(
							//if the group member is the context
							//and the id is a member of the context guild
							//or the id is a role of this context guild
							context.getId().equals(groupmember)
								&& (
									context.getMemberById(id) != null
									|| context.getRoleById(id) != null
								)
						)
					)
					return true;
				}
				else
				{
					if(
						(
							//if the group member is a role id
							//and the id is a member in the role's guild
							//and the member has the role
							AMGN.bot.getRoleById(groupmember) != null
								&& AMGN.bot.getRoleById(groupmember).getGuild().getMemberById(id) != null
								&& AMGN.bot.getRoleById(groupmember).getGuild().getMemberById(id).getRoles().contains(AMGN.bot.getRoleById(groupmember))
						)
						|| //OR
						(
							//if the group member is a guild id
							//and the id is a member of this guild
							//or the id is a role of this guild
							AMGN.bot.getGuildById(groupmember) != null
								&& (
									AMGN.bot.getGuildById(groupmember).getMemberById(id) != null
									|| AMGN.bot.getGuildById(groupmember).getRoleById(id) != null
								)
						)
					)
					return true;
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
