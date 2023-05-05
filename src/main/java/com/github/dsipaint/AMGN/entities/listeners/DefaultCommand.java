package com.github.dsipaint.AMGN.entities.listeners;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.io.IOHandler;
import com.github.dsipaint.AMGN.io.Permissions;

import net.dv8tion.jda.api.entities.Member;

public enum DefaultCommand
{
	//originally from the intrinsic plugins, now are just hard-coded commands, this is where their metadata is stored
	
	HELP("help", "^help, or ^help {command/plugin}", "Returns information on one or all commands of the network", new String[]{"AMGN.commands.*", "AMGN.commands.help"}),
	METAUPDATE("updatemetainfo", "^updatemetainfo {prefix/modlogs/modrole/acceptcol/declinecol/uniquecol} {new value}", "Update the metainfo of the network, for example a guild's prefix", new String[]{"AMGN.commands.*", "AMGN.commands.updatemetainfo"}),
	METAVIEW("viewmetainfo", "^viewmetainfo {prefix/modlogs/modrole}", "View the metainfo of the network, for example a guild's prefix", new String[]{"AMGN.commands.*", "AMGN.commands.viewmetainfo"}),
	RUNNING("showplugins", "^showplugins", "Displays all active plugins in this guild (only shows plugins that can act in this guild according to the whitelist/blacklist)", new String[]{"AMGN.commands.*", "AMGN.commands.controlplugins.*", "AMGN.commands.showplugins"}),
	ENABLE("enable", "^enable {plugin name}", "Enable plugins from your discord client!", new String[]{"AMGN.commands.*", "AMGN.commands.controlplugins.*", "AMGN.commands.enable"}),
	DISABLE("disable", "^disable {plugin name}", "Disable plugins from your discord client!", new String[]{"AMGN.commands.*", "AMGN.commands.controlplugins.*", "AMGN.commands.disable"}),
	RELOAD("reload", "^reload {plugin name}", "Reload plugins from your discord client!", new String[]{"AMGN.commands.*", "AMGN.commands.controlplugins.*", "AMGN.commands.reload"}),
	RELOADALL("reloadall", "^reloadall", "Reload all plugins from your discord client!", new String[]{"AMGN.commands.*", "AMGN.commands.controlplugins.*", "AMGN.commands.reloadall"}),
	CLOSE("closenetwork", "^closenetwork", "disables every plugin and shuts down the program", new String[]{"AMGN.commands.*", "AMGN.commands.closenetwork"}),
	WHITELIST("whitelist", "^whitelist {add/remove} {plugin name} in the guild you want to change the whitelist for", "Add or remove a guild to the whitelist for a given plugin", new String[]{"AMGN.commands.*", "AMGN.commands.whitelist"}),
	BLACKLIST("blacklist", "^blacklist {add/remove} {plugin name} in the guild you want to change the blacklist for", "Add or remove a guild to the blacklist for a given plugin", new String[]{"AMGN.commands.*", "AMGN.commands.blacklist"}),
	RUNNINGALL("showallplugins", "^showallplugins", "displays all plugins that are active on the network somewhere, regardless of the whitelist/blacklist settings", new String[]{"AMGN.commands.*", "AMGN.commands.controlplugins.*", "AMGN.commands.showallplugins"}),
	PERMS("permission", "^permission {add/remove} {user/role id} {permission}", "allows you to update the permissions of a user or a role", new String[] {"AMGN.commands.*", "AMGN.commands.permission"}),
	LISTPERMS("listpermissions", "^listpermissions {user id/role id/plugin (optional)}", "show what permissions a user, role, or plugin has- leave blank to see inbuilt permissions", new String[]{"AMGN.commands.*", "AMGN.commands.listpermissions"}),
	GROUPS("groups", "^groups {create/destroy/add/remove/addperm/removeperm/list}", "Manage permission groups", new String[]{"AMGN.commands.*", "AMGN.commands.groups"});

	//TODO: new intrinsic feature to change bot status
	private String label, usage, desc;
	String[] perms;
	DefaultCommand(String label, String usage, String desc, String[] perms)
	{
		this.label = label;
		this.usage = usage;
		this.desc = desc;
		this.perms = perms;
	}
	
	public String getLabel()
	{
		return this.label;
	}
	
	public String getUsage()
	{
		return this.usage;
	}
	
	public String getDesc()
	{
		return this.desc;
	}
	
	public String[] getPermissions()
	{
		return this.perms;
	}

	//does a member have permission to use a given command?
	@SuppressWarnings("unchecked")
	public boolean hasPermission(Member m)
	{
		//operators always have permission
		if(GuildNetwork.isOperator(m.getUser()))
			return true;
		
		//read permissions.yml
		Map<String, List<String>> perms = new HashMap<String, List<String>>();
		try
		{
			//check all groups and see if a group has the permission
			HashMap<String, Object> groups = (HashMap<String, Object>) IOHandler.readYamlData(GuildNetwork.PERMISSIONS_PATH, "groups");
			for(String groupname : groups.keySet())
			{
				if(Permissions.isInGroup(m.getId(), groupname))
				{
					for(String permission : this.perms)
					{
						if(Permissions.hasPermission(groupname, permission))
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

		//check to see if an id is either a member, or a role the member has (in this guild!!)
		//if this ID has been given any of this command's permissions, return true
		for(String id : perms.keySet())
		{
			if(id.equalsIgnoreCase("groups"))
				continue;

			if(m.getId().equals(id)
				||(m.getGuild().getRoleById(id) != null && m.getRoles().contains(m.getGuild().getRoleById(id)))
				|| m.getGuild().getId().equals(id))
			{
				for(String commandperm : this.perms)
				{
					for(String permission : perms.get(id))
					{
						if(commandperm.equals(permission))
							return true;
					}
				}
			}
		}
		
		return false;
	}
}
