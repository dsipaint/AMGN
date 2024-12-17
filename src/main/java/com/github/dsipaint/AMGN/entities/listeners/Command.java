package com.github.dsipaint.AMGN.entities.listeners;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.io.Permissions;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

public abstract class Command extends Listener
{
	private String label, usage, desc; //TODO: change from private to protected to allow local referencing?
	private String[] permissions;
	
	@SuppressWarnings("unchecked")
	public Command(Plugin plugin, String label)
	{
		Map<String, Object> plugin_metadata = new Yaml().load(
					new InputStreamReader(getClass().getResourceAsStream("/plugin.yml")));
			
			List<Object> command_metadata = (List<Object>) plugin_metadata.get("commands");
			
			command_metadata.forEach(command ->
			{
				Map<String, Object> command_obj = (Map<String, Object>) command;
				
				//if this is the desired command within the metadata
				if(command_obj.containsValue(label))
				{	
					this.label = label.toLowerCase(); //take the command data we need
					this.usage = (String) command_obj.get("usageinfo");
					this.desc = (String) command_obj.get("description");
					
					//parse custom permissions
					List<String> perms = (List<String>) command_obj.get("permissions");
					this.permissions = new String[perms.size()];
					for(int i = 0; i < perms.size(); i++)
						this.permissions[i] = perms.get(i);
				}
			});
	}
	
	public abstract void onCommand(CommandEvent e); //must inherit and implement this method for it to be a command
	
	/** 
	 * @param m member to test permissions for
	 * @return boolean true if the member can run this command, false otherwise
	 */
	//returns true if this member has permission to run this command, as specified in plugin.yml
	//this uses guild-specific "members" because commands are always used in a guild-context
	public final boolean hasPermission(Member m)
	{
		for(String perm : this.getPerms())
		{
			if(Permissions.userHasPermission(m.getUser(), m.getGuild(), perm))
				return true; 
		}

		return false;
	}
	
	
	/** 
	 * @return String
	 */
	public final String getLabel()
	{
		return label;
	}

	
	/** 
	 * @return String
	 */
	public final String getUsage(Guild g)
	{
		if(g == null)
			return usage;

		return usage == null ? usage
			:
			usage.replace("${PREFIX}", GuildNetwork.getPrefix(g.getIdLong()));
	}

	
	/** 
	 * @return String
	 */
	public final String getDesc()
	{
		return desc;
	}

	
	/** 
	 * @return GuildPermission
	 */
	public final String[] getPerms()
	{
		return permissions;
	}
}
