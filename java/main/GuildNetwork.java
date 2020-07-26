package main;
import java.util.ArrayList;
import java.util.Map;

import entities.Guild;
import entities.Listener;
import entities.Plugin;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class GuildNetwork
{
	//general utility class, like the Bukkit class in spigot- represents actions available across the whole network
	
	static Map<Long, Guild> guild_data; //placed here to be globally available, set up in the Main class
	
	static final int GREEN_EMBED_COLOUR = 65280, RED_EMBED_COLOUR = 16073282; //Embed colours
	static final String DEFAULT_PREFIX = "^"; //default prefix
	static final long DEFAULT_ID = -1; //default long id value
	static final String PLUGIN_PATH = "./plugins"; //plugin path
	
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
	
	public static void registerListener(Listener listener, Plugin plugin)
	{
		Main.jda.addEventListener(listener);
		Main.plugin_listeners.get(plugin).add(listener); //add the listener listed under this name
	}
	
	//returns TRUE if this plugin was enabled
	public static boolean enablePlugin(Plugin plugin)
	{
		//if plugin is not already enabled
		if(Main.plugin_listeners.get(plugin) == null)
		{
			plugin.onEnable(); //run plugin's enable method
			Main.plugin_listeners.put(plugin, new ArrayList<Listener>()); //add this plugin with an empty list of listeners
			//(listeners are then added by GuildNetwork.registerListener method, separately)
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
