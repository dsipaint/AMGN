import java.util.Map;

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
}
