package com.github.dsipaint.AMGN.entities.listeners;

import java.util.function.Consumer;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.closenetwork.CloseCommand;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.controlEnableDisable.DisableCommand;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.controlEnableDisable.EnableCommand;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.controlEnableDisable.ReloadAllCommand;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.controlEnableDisable.ReloadCommand;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.help.HelpCommand;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.metadata.UpdateMetaInfoCommand;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.metadata.ViewMetaInfoCommand;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.permissions.GroupsCommand;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.permissions.ListPermissionsCommand;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.permissions.PermissionCommand;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.running.ShowAllPluginsCommand;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.running.ShowPluginsCommand;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.whitelist.BlacklistCommand;
import com.github.dsipaint.AMGN.entities.plugins.intrinsic.whitelist.WhitelistCommand;
import com.github.dsipaint.AMGN.io.Permissions;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public enum DefaultCommand
{
	//originally from the intrinsic plugins, now are just hard-coded commands, this is where their metadata is stored
	
	HELP("help", "${PREFIX}help, or ${PREFIX}help {command/plugin}", "Returns information on one or all commands of the network", new String[]{"AMGN.commands.*", "AMGN.commands.help"}, new HelpCommand()),
	METAUPDATE("updatemetainfo", "${PREFIX}updatemetainfo {prefix/modlogs/acceptcol/declinecol/uniquecol} {new value}", "Update the metainfo of the network, for example a guild's prefix", new String[]{"AMGN.commands.*", "AMGN.commands.updatemetainfo"}, new UpdateMetaInfoCommand()),
	METAVIEW("viewmetainfo", "${PREFIX}viewmetainfo {prefix/modlogs/modrole}", "View the metainfo of the network, for example a guild's prefix", new String[]{"AMGN.commands.*", "AMGN.commands.viewmetainfo"}, new ViewMetaInfoCommand()),
	RUNNING("showplugins", "${PREFIX}showplugins", "Displays all active plugins in this guild (only shows plugins that can act in this guild according to the whitelist/blacklist)", new String[]{"AMGN.commands.*", "AMGN.commands.controlplugins.*", "AMGN.commands.showplugins"}, new ShowPluginsCommand()),
	ENABLE("enable", "${PREFIX}enable {plugin name}", "Enable plugins from your discord client!", new String[]{"AMGN.commands.*", "AMGN.commands.controlplugins.*", "AMGN.commands.enable"}, new EnableCommand()),
	DISABLE("disable", "${PREFIX}disable {plugin name}", "Disable plugins from your discord client!", new String[]{"AMGN.commands.*", "AMGN.commands.controlplugins.*", "AMGN.commands.disable"}, new DisableCommand()),
	RELOAD("reload", "${PREFIX}reload {plugin name}", "Reload plugins from your discord client!", new String[]{"AMGN.commands.*", "AMGN.commands.controlplugins.*", "AMGN.commands.reload"}, new ReloadCommand()),
	RELOADALL("reloadall", "${PREFIX}reloadall", "Reload all plugins from your discord client!", new String[]{"AMGN.commands.*", "AMGN.commands.controlplugins.*", "AMGN.commands.reloadall"}, new ReloadAllCommand()),
	CLOSE("closenetwork", "${PREFIX}closenetwork", "disables every plugin and shuts down the program", new String[]{"AMGN.commands.*", "AMGN.commands.closenetwork"}, new CloseCommand()),
	WHITELIST("whitelist", "${PREFIX}whitelist {add/remove} {plugin name} in the guild you want to change the whitelist for", "Add or remove a guild to the whitelist for a given plugin", new String[]{"AMGN.commands.*", "AMGN.commands.whitelist"}, new WhitelistCommand()),
	BLACKLIST("blacklist", "${PREFIX}blacklist {add/remove} {plugin name} in the guild you want to change the blacklist for", "Add or remove a guild to the blacklist for a given plugin", new String[]{"AMGN.commands.*", "AMGN.commands.blacklist"}, new BlacklistCommand()),
	RUNNINGALL("showallplugins", "${PREFIX}showallplugins", "displays all plugins that are active on the network somewhere, regardless of the whitelist/blacklist settings", new String[]{"AMGN.commands.*", "AMGN.commands.controlplugins.*", "AMGN.commands.showallplugins"}, new ShowAllPluginsCommand()),
	PERMS("permission", "${PREFIX}permission {add/remove} {user/role id} {permission}", "allows you to update the permissions of a user or a role", new String[] {"AMGN.commands.*", "AMGN.commands.permission"}, new PermissionCommand()),
	LISTPERMS("listpermissions", "${PREFIX}listpermissions {user id/role id/plugin (optional)}", "show what permissions a user, role, or plugin has- leave blank to see inbuilt permissions", new String[]{"AMGN.commands.*", "AMGN.commands.listpermissions"}, new ListPermissionsCommand()),
	GROUPS("groups", "${PREFIX}groups {create/destroy/add/remove/addperm/removeperm/list}", "Manage permission groups", new String[]{"AMGN.commands.*", "AMGN.commands.groups"}, new GroupsCommand());

	//TODO: new intrinsic feature to change bot status
	private String label, usage, desc;
	private String[] perms;
	private Consumer<CommandEvent> commandaction;

	DefaultCommand(String label, String usage, String desc, String[] perms, Consumer<CommandEvent> commandaction)
	{
		this.label = label;
		this.usage = usage;
		this.desc = desc;
		this.perms = perms;
		this.commandaction = commandaction;
	}
	
	public String getLabel()
	{
		return this.label;
	}
	
	public String getUsage(Guild g)
	{
		if(g == null)
			return usage;

		return usage == null ? usage
			:
			usage.replace("${PREFIX}", GuildNetwork.getPrefix(g.getIdLong()));
	}
	
	public String getDesc()
	{
		return this.desc;
	}
	
	public String[] getPermissions()
	{
		return this.perms;
	}

	public Consumer<CommandEvent> getCommandAction()
	{
		return this.commandaction;
	}

	//does a member have permission to use a given command?
	public boolean hasPermission(Member m)
	{
		for(String perm : this.getPermissions())
		{
			if(Permissions.userHasPermission(m.getUser(), m.getGuild(), perm))
				return true;
		}

		return false;
	}
}
