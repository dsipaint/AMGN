package com.github.dsipaint.AMGN.entities.listeners;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.GuildPermission;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;

public abstract class Command extends Listener
{
	private String label, usage, desc; //TODO: change from private to protected to allow local referencing?
	private GuildPermission perm;
	
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
					
					switch((String) command_obj.get("permission"))
					{
						case "operator":
							this.perm = GuildPermission.OPERATOR;
							break;
						
						case "admin":
							this.perm = GuildPermission.ADMIN;
							break;
							
						case "staff":
							this.perm = GuildPermission.STAFF;
							break;
							
						case "all":
							this.perm = GuildPermission.ALL;
							break;
							
						default:
							this.perm = GuildPermission.ADMIN; //default to admin if needed
						
					}
				}
			});
	}
	
	public abstract void onCommand(CommandEvent e); //must inherit and implement this method for it to be a command
	
	/** 
	 * @param m member to test permissions for
	 * @return boolean true if the member can run this command, false otherwise
	 */
	//returns true if this member has permission to run this command, as specified in plugin.yml
	public final boolean hasPermission(Member m)
	{
		//operators always have permission
		if(GuildNetwork.isOperator(m.getUser()))
			return true;
		
		switch(this.perm)
		{
			
			case OPERATOR:
				break;
				
			case ADMIN:
				if(m.hasPermission(Permission.ADMINISTRATOR))
					return true;
				
				return false;
				
			case STAFF:
				if(m.hasPermission(Permission.ADMINISTRATOR))
					return true;
				
				//in this case no one can be staff
				if(GuildNetwork.getModrole(m.getGuild().getIdLong()) == -1)
					return false;
				
				//if member has modrole
				if(m.getRoles().contains(m.getGuild().getRoleById(GuildNetwork.getModrole(m.getGuild().getIdLong()))))
					return true;
				
				return false;
				
			case ALL:
				return true;
		}
		
		return false;
	}
	
	
	/** 
	 * @param m member to check permissions for
	 * @param permission permission to check if the member has
	 * @return boolean true if the member has permission to run this command, as specified in plugin.yml, false otherwise
	 */
	public static final boolean hasPermission(Member m, GuildPermission permission)
	{
		//operators always have permission
		if(GuildNetwork.isOperator(m.getUser()))
			return true;
		
		switch(permission)
		{
			
			case OPERATOR:
				break;
				
			case ADMIN:
				if(m.hasPermission(Permission.ADMINISTRATOR))
					return true;
				
				return false;
				
			case STAFF:
				if(m.hasPermission(Permission.ADMINISTRATOR))
					return true;
				
				//in this case no one can be staff
				if(GuildNetwork.getModrole(m.getGuild().getIdLong()) == -1)
					return false;
				
				//if member has modrole
				if(m.getRoles().contains(m.getGuild().getRoleById(GuildNetwork.getModrole(m.getGuild().getIdLong()))))
					return true;
				
				return false;
				
			case ALL:
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
	public final GuildPermission getPerm()
	{
		return perm;
	}
}
