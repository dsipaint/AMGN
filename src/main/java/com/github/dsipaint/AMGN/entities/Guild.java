package com.github.dsipaint.AMGN.entities;

import com.github.dsipaint.AMGN.main.AMGN;

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
		//if ID can't be resolved, set it to default id
		this.guild_id = (AMGN.bot.getGuildById(guild_id) == null ? DEFAULT_ID : guild_id );
		this.modlogs = (AMGN.bot.getTextChannelById(modlogs) == null ? DEFAULT_ID : modlogs );
		this.modrole = (AMGN.bot.getRoleById(modrole) == null ? DEFAULT_ID : modrole );
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

	
	/** 
	 * @return String prefix Guild prefix
	 */
	public final String getPrefix()
	{
		return prefix;
	}

	
	/** 
	 * @param prefix Guild prefix
	 */
	public final void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

	
	/** 
	 * @return long modlogs channel id
	 */
	public final long getModlogs()
	{
		return modlogs;
	}

	
	/** 
	 * @param modlogs channel id
	 */
	public final void setModlogs(long modlogs)
	{
		this.modlogs = modlogs;
	}

	
	/** 
	 * @return long modrole role id
	 */
	public final long getModrole()
	{
		return modrole;
	}

	
	/** 
	 * @param modrole role id
	 */
	public final void setModrole(long modrole)
	{
		this.modrole = modrole;
	}

	
	/** 
	 * @return long id of the guild
	 */
	public final long getGuild_id()
	{
		return guild_id;
	}
	
	
	
	/** 
	 * @param offset String padding for pretty-printing the json with
	 * @return String json pretty-printed string of the Guild object
	 */
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
	
	
	/** 
	 * @return MessageEmbed
	 */
	public MessageEmbed asEmbed()
	{
		net.dv8tion.jda.api.entities.Guild guild = AMGN.bot.getGuildById(this.getGuild_id());
		
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
