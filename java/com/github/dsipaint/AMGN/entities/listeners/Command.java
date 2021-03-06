package com.github.dsipaint.AMGN.entities.listeners;

import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.DeserializationException;
import org.json.simple.JsonArray;
import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.GuildPermission;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public abstract class Command extends ListenerAdapter
{
	private String label, usage, desc;
	private GuildPermission perm;
	
	public Command(Plugin plugin, String label)
	{
		try
		{
			JsonObject plugin_metadata = (JsonObject) Jsoner.deserialize(
					new InputStreamReader(getClass().getResourceAsStream("/plugin.json")));
			
			JsonArray command_metadata = (JsonArray) plugin_metadata.get("commands");
			
			command_metadata.forEach(command ->
			{
				JsonObject command_obj = (JsonObject) command;
				
				//if this is the desired command within the metadata
				if(command_obj.containsValue(label))
				{	
					this.label = label.toLowerCase(); //take the command data we need
					this.usage = command_obj.getString("usageinfo");
					this.desc = command_obj.getString("description");
					
					switch(command_obj.getString("permission"))
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
		catch (DeserializationException | IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public abstract void onGuildMessageReceived(GuildMessageReceivedEvent e); //must inherit and implement this method for it to be a command
	
	//returns true if this member has permission to run this command, as specified in plugin.json
	public final boolean hasPermission(Member m)
	{
		//operators always have permission
		for(long op : GuildNetwork.operators)
		{
			if(op == m.getIdLong())
				return true;
		}
		
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
	
	//returns true if this member has permission to run this command, as specified in plugin.json
	public static final boolean hasPermission(Member m, GuildPermission permission)
	{
		//operators always have permission
		for(long op : GuildNetwork.operators)
		{
			if(op == m.getIdLong())
				return true;
		}
		
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
	
	public final String getLabel()
	{
		return label;
	}

	public final String getUsage()
	{
		return usage;
	}

	public final String getDesc()
	{
		return desc;
	}

	public final GuildPermission getPerm()
	{
		return perm;
	}
}
