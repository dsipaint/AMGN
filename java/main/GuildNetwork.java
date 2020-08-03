package main;
import java.util.ArrayList;
import java.util.Map;

import entities.Guild;
import entities.listeners.Command;
import entities.listeners.Listener;
import entities.plugins.Plugin;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildNetwork
{
	//general utility class, like the Bukkit class in spigot- represents actions available across the whole network
	
	public static Map<Long, Guild> guild_data; //placed here to be globally available, set up in the Main class
	
	public static final int GREEN_EMBED_COLOUR = 65280, RED_EMBED_COLOUR = 16073282; //Embed colours
	public static final String DEFAULT_PREFIX = "^"; //default prefix
	public static final long DEFAULT_ID = -1; //default long id value
	public static final String PLUGIN_PATH = "./plugins"; //default plugin path
	public static final String GUILDINFO_PATH = "./guilds.json"; //guild info path
	
	//return true if a member has discord mod, admin or is owner
	public static boolean isStaff(Member m)
	{
		//if owner
		if(m.isOwner())
			return true;
		
		//if admin
		if(m.hasPermission(Permission.ADMINISTRATOR))
			return true;
		
		//modrole for the given server
		Role modrole = m.getGuild().getRoleById(guild_data.get(m.getGuild().getIdLong()).getModrole());
		//if this member has the modrole for this guild, return true
		for(Role r : m.getRoles())
		{
			if(r.equals(modrole))
				return true;
		}
		
		return false;
	}
	
	//ease-of-access methods for retrieving guild data
	public static String getPrefix(long guild_id)
	{
		return guild_data.get(guild_id) == null ? DEFAULT_PREFIX : guild_data.get(guild_id).getPrefix();
	}
	
	public static long getModrole(long guild_id)
	{
		return guild_data.get(guild_id) == null ? DEFAULT_ID : guild_data.get(guild_id).getModrole();
	}
	
	public static long getModlogs(long guild_id)
	{
		return guild_data.get(guild_id) == null ? DEFAULT_ID : guild_data.get(guild_id).getModlogs();
	}
	
	public static void registerListener(Listener listener, Plugin plugin)
	{
		Main.jda.addEventListener(listener);
		Main.plugin_listeners.get(plugin).add(listener); //add the listener listed under this name
	}
	
	//works just as registerListener does, just more syntax-sense for a user to register commands separately to listeners
	public static void registerCommand(Command cmd, Plugin plugin)
	{
		Main.jda.addEventListener(cmd);
		Main.plugin_listeners.get(plugin).add(cmd);
	}
	
	//returns TRUE if this plugin was enabled
	public static boolean enablePlugin(Plugin plugin)
	{
		//if plugin is not already enabled
		if(Main.plugin_listeners.get(plugin) == null)
		{
			Main.plugin_listeners.put(plugin, new ArrayList<ListenerAdapter>()); //add this plugin with an empty list of listeners
			//(listeners are then added by GuildNetwork.registerListener method, separately)
			plugin.onEnable(); //run plugin's enable method
			return true;
		}
		else
			return false;
	}
	
	//returns TRUE if this plugin was disabled
	public static boolean disablePlugin(Plugin plugin)
	{
		//if plugin is not already enabled
		if(Main.plugin_listeners.get(plugin) == null)
			return false;
		else
		{
			//disable each of the listeners this plugin registers
			Main.plugin_listeners.get(plugin).forEach(listener ->
			{
				Main.jda.removeEventListener(listener); //NOTE: may not properly remove listener, may need to cast to ListenerAdapter?
			});
			
			Main.plugin_listeners.remove(plugin); //remove plugin from the list
			plugin.onDisable(); //run plugin's onDisable method
			return true;
		}
	}
}
