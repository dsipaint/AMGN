package com.github.dsipaint.AMGN.entities.listeners;

import com.github.dsipaint.AMGN.entities.GuildPermission;

public enum DefaultCommand
{
	//originally from the intrinsic plugins, now are just hard-coded commands, this is where their metadata is stored
	
	HELP("help", "^help, or ^help {command/plugin}", "Returns information on one or all commands of the network", GuildPermission.ALL),
	METAUPDATE("updatemetainfo", "^updatemetainfo {prefix/modlogs/modrole/acceptcol/declinecol/uniquecol} {new value}", "Update the metainfo of the network, for example a guild's prefix", GuildPermission.ADMIN),
	METAVIEW("viewmetainfo", "^viewmetainfo {prefix/modlogs/modrole}", "View the metainfo of the network, for example a guild's prefix", GuildPermission.STAFF),
	OPADD("setoperator", "^setoperator {user id}", "Add a user as an operator of the network", GuildPermission.OPERATOR),
	OPREMOVE("removeoperator", "^removeoperator {user id}", "Remove a user as an operator of the network", GuildPermission.OPERATOR),
	RUNNING("showplugins", "^showplugins", "Displays all active plugins on the network", GuildPermission.STAFF),
	ENABLE("enable", "^enable {plugin name}", "Enable plugins from your discord client!", GuildPermission.ADMIN),
	DISABLE("disable", "^disable {plugin name}", "Disable plugins from your discord client!", GuildPermission.ADMIN),
	RELOAD("reload", "^reload {plugin name}", "Reload plugins from your discord client!", GuildPermission.ADMIN),
	RELOADALL("reloadall", "^reloadall", "Reload all plugins from your discord client!", GuildPermission.ADMIN),
	CLOSE("closenetwork", "^closenetwork", "disables every plugin and shuts down the program", GuildPermission.ADMIN);
	
	//TODO: for now, default commands will have default, unchangeable permissions, but I will change this in the future to allow customisation of permissions for these commands
	//TODO: new intrinsic feature to change bot status
	private String label, usage, desc;
	GuildPermission perm;
	DefaultCommand(String label, String usage, String desc, GuildPermission perm)
	{
		this.label = label;
		this.usage = usage;
		this.desc = desc;
		this.perm = perm;
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
	
	public GuildPermission getGuildPermission()
	{
		return this.perm;
	}
}
