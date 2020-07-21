import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class GuildNetwork
{
	//general utility class, like the Bukkit class in spigot- represents actions available across the whole network
	
	//return true if a member has discord mod, admin or is owner
		public static boolean isStaff(Member m)
		{
			//if owner
			if(m.isOwner())
				return true;
			
			//if admin
			if(m.hasPermission(Permission.ADMINISTRATOR))
				return true;
			
			//if discord mod TODO: Make discord mod module for all servers
			switch(m.getGuild().getId())
			{
				case "565623426501443584" : //wilbur's discord
					for(Role r : m.getRoles())
					{
						if(r.getId().equals("565626094917648386")) //wilbur discord mod
							return true;
					}
					break;
					
				case "640254333807755304" : //charlie's server
					for(Role r : m.getRoles())
					{
						if(r.getId().equals("640255355401535499")) //charlie discord mod
							return true;
					}
					break;
			}
			
			return false;
		}
}
