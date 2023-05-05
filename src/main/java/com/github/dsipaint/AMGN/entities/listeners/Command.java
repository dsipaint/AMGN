package com.github.dsipaint.AMGN.entities.listeners;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.io.IOHandler;
import com.github.dsipaint.AMGN.io.Permissions;

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
	@SuppressWarnings("unchecked")
	public final boolean hasPermission(Member m)
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
					for(String permission : this.permissions)
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
			if(id.equalsIgnoreCase("groups")) //only deal with IDs here, we check groups above
				continue;

			if(m.getId().equals(id) ||
				(m.getGuild().getRoleById(id) != null && m.getRoles().contains(m.getGuild().getRoleById(id)))
				|| m.getGuild().getId().equals(id))
			{
				for(String commandperm : this.permissions)
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
	public final String getUsage()
	{
		return usage;
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
