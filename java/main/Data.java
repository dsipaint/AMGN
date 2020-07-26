package main;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.json.simple.DeserializationException;
import org.json.simple.JsonArray;
import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

import entities.Guild;

public class Data
{
	
	//reads data in from guilds.json, using default values if not found (see default values in GuildNetwork.java
	public static HashMap<Long, Guild> readGuildData(String path) throws FileNotFoundException, IOException, DeserializationException
	{
		JsonArray guilds_in = (JsonArray) Jsoner.deserialize(new FileReader(new File(path)));
		HashMap<Long, Guild> guilds_out = new HashMap<Long, Guild>();
		
		guilds_in.forEach(obj ->
		{
			JsonObject guild = (JsonObject) obj;
			
			/*objects parsed from the jsonarray that do not exist have a default value of null. As for primitive values, 
			 * the toString method of the wrapper class is called, this causes a nullpointerexception if searching for
			 * a primitive value which does not exist e.g. guild.getLong("modrole"), if no "modrole" has been specified
			 * in that part of the json file. Therefore we must use methods such as getLongOrDefault for these cases,
			 * to account for the fact that the values may not be there:
			 */
			Guild g = new Guild(
					guild.getLongOrDefault("guild_id", GuildNetwork.DEFAULT_ID),
					guild.getLongOrDefault("modlogs", GuildNetwork.DEFAULT_ID),
					guild.getLongOrDefault("modrole", GuildNetwork.DEFAULT_ID),
					guild.getStringOrDefault("prefix", GuildNetwork.DEFAULT_PREFIX)
					);
			
			guilds_out.put(guild.getLong("guild_id"), g);
		});
		
		return guilds_out;
	}
}
