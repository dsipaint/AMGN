package entities;

import main.GuildNetwork;
import main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class Guild
{
	public static final String DEFAULT_PREFIX = "^"; //default prefix
	public static final long DEFAULT_ID = -1; //default long id value
	
	private String prefix;
	private long guild_id, modlogs, modrole;
	
	public Guild(long guild_id, long modlogs, long modrole, String prefix)
	{
		this.guild_id = guild_id;
		this.modlogs = modlogs;
		this.modrole = modrole;
		this.prefix = prefix;
	}
	
	//constructor makes a guild with default values
	public Guild(long guild_id)
	{
		this.guild_id = guild_id;
		this.modlogs = DEFAULT_ID;
		this.modrole = DEFAULT_ID;
		this.prefix = DEFAULT_PREFIX;
	}

	public String getPrefix()
	{
		return prefix;
	}

	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

	public long getModlogs()
	{
		return modlogs;
	}

	public void setModlogs(long modlogs)
	{
		this.modlogs = modlogs;
	}

	public long getModrole()
	{
		return modrole;
	}

	public void setModrole(long modrole)
	{
		this.modrole = modrole;
	}

	public long getGuild_id()
	{
		return guild_id;
	}
	
	
	//returns json string for this object
	public String asJson(String offset)
	{
		String json = offset + "{\n";
		json += offset + "\t\"guild_id\": " + this.getGuild_id() + ",\n";
		json += offset +"\t\"prefix\": \"" + this.getPrefix() + "\",\n";
		json += offset +"\t\"modlogs\": " + this.getModlogs() + ",\n";
		json += offset +"\t\"modrole\": " + this.getModrole() + "\n";
		json += offset +"}";
		return json;
	}
	
	public MessageEmbed asEmbed()
	{
		net.dv8tion.jda.api.entities.Guild guild = Main.jda.getGuildById(this.getGuild_id());
		
		return new EmbedBuilder()
				.setTitle(guild.getName())
				.setImage(guild.getIconUrl())
				.setColor(GuildNetwork.GREEN_EMBED_COLOUR)
				.addField("Owner: ", guild.getOwner().getEffectiveName(), true)
				.addField("Prefix: ", this.getPrefix(), true)
				.addField("Modlogs: ", this.getModlogs() == DEFAULT_ID ? "no modlogs set" : guild.getTextChannelById(this.getModlogs()).getAsMention(), true)
				.addField("Modrole: ", this.getModrole() == DEFAULT_ID ? "no modrole set" : guild.getRoleById(this.getModrole()).getName(), true)
				.build();
	}
}
