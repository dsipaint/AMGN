import java.io.IOException;

import org.json.simple.DeserializationException;

public class Main
{
	public static void main(String[] args)
	{
		//SETUP
		
		System.out.println("Commencing setup..."); //TODO: change to logging
		
		try
		{
			System.out.println("Reading data..."); //TODO: change to logging
			GuildNetwork.guild_data = Data.readGuildData("guilds.json"); //read guild data from guilds.json
		}
		catch (IOException | DeserializationException e)
		{
			e.printStackTrace();
		}
		
		System.out.println("Finished setup."); //TODO: change to logging
		//END SETUP
		
		
		
		
		//TEST/DEBUG
		GuildNetwork.guild_data.forEach((id, guild) ->
		{
			System.out.println("Guild id: " + id);
			System.out.println("Prefix: " + guild.getPrefix());
			System.out.println("Modlogs: " + guild.getModlogs());
			System.out.println("Modrole: " + guild.getModrole());
			
			System.out.println();
		});
		//END TEST/DEBUG
	}
}
