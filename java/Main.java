import java.io.IOException;

import javax.security.auth.login.LoginException;

import org.json.simple.DeserializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class Main
{
	static JDA jda;
	static final Logger logger = LoggerFactory.getLogger("AMGN"); //logger
	
	public static void main(String[] args)
	{
		//SETUP
		logger.info("Commencing setup...");
		try
		{
			jda = new JDABuilder(AccountType.BOT).setToken("NjQyODM5OTg5NjQxNjc0NzUy.Xxoubw.jBwpkEl41CzDkO2kVen1Q_NqH9Q").build();
		}
		catch (LoginException e1)
		{
			e1.printStackTrace();
		}
		
		try
		{
			logger.info("Reading data...");
			GuildNetwork.guild_data = Data.readGuildData("guilds.json"); //read guild data from guilds.json
		}
		catch (IOException | DeserializationException e)
		{
			e.printStackTrace();
		}
		
		logger.info("Finished setup.");
		//END SETUP
		
		
		
		
		//TEST/DEBUG
		GuildNetwork.guild_data.forEach((id, guild) ->
		{
			logger.debug("Guild id: " + id);
			logger.debug("Prefix: " + guild.getPrefix());
			logger.debug("Modlogs: " + guild.getModlogs());
			logger.debug("Modrole: " + guild.getModrole());
			
			System.out.println();
		});
		
		jda.shutdownNow();
		//END TEST/DEBUG
	}
}
