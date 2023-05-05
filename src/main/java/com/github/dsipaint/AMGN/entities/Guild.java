package com.github.dsipaint.AMGN.entities;

import com.github.dsipaint.AMGN.AMGN;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class Guild
{
	public static final String DEFAULT_PREFIX = "^"; //default prefix
	public static final long DEFAULT_ID = -1; //default long id value
	public static final int DEFAULT_ACCEPT_COL = 65280, DEFAULT_DECLINE_COL = 16073282, DEFAULT_UNIQUE_COL = 11023006; //Embed colours
	
	private String prefix;
	private long guild_id, modlogs;
	private int accept_col, decline_col, unique_col;
	
	public Guild(long guild_id, long modlogs, String prefix, int accept_col, int decline_col, int unique_col)
	{
		//if ID can't be resolved, set it to default id
		this.guild_id = (AMGN.bot.getGuildById(guild_id) == null ? DEFAULT_ID : guild_id );
		this.modlogs = (AMGN.bot.getTextChannelById(modlogs) == null ? DEFAULT_ID : modlogs );
		this.prefix = prefix;
		this.accept_col = accept_col;
		this.decline_col = decline_col;
		this.unique_col = unique_col;
	}
	
	//constructor makes a guild with default values
	public Guild(long guild_id)
	{
		this.guild_id = guild_id;
		this.modlogs = DEFAULT_ID;
		this.prefix = DEFAULT_PREFIX;
		this.accept_col = DEFAULT_ACCEPT_COL;
		this.decline_col = DEFAULT_DECLINE_COL;
		this.unique_col = DEFAULT_UNIQUE_COL;
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
	 * @return long id of the guild
	 */
	public final long getGuild_id()
	{
		return guild_id;
	}
	
	/** 
	 * @return int int representation of the accept colour (default if missing)
	 */
	public final int getAccept_col()
	{
		return accept_col;
	}

	/** 
	 * @param accept_col new colour
	 */
	public final void setAccept_col(int accept_col)
	{
		this.accept_col = accept_col;
	}

	/** 
	 * @return int int representation of the decline colour (default if missing)
	 */
	public final int getDecline_col()
	{
		return decline_col;
	}

	/** 
	 * @param decline_col new decline colour
	 */
	public final void setDecline_col(int decline_col)
	{
		this.decline_col = decline_col;
	}

	/** 
	 * @return int int representation of the unique colour (default if missing)
	 */
	public final int getUnique_col()
	{
		return unique_col;
	}

	/** 
	 * @param unique_col new colour
	 */
	public final void setUnique_col(int unique_col)
	{
		this.unique_col = unique_col;
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
				.setColor(this.unique_col)
				.addField("Owner: ", guild.getOwner().getEffectiveName(), true)
				.addField("Prefix: ", this.getPrefix(), true)
				.addField("Modlogs: ", this.getModlogs() == DEFAULT_ID ? "no modlogs set" : guild.getTextChannelById(this.getModlogs()).getAsMention(), true)
				.addField("Accept colour: ", formatHexString(this.accept_col), true)
				.addField("Decline colour: ", formatHexString(this.decline_col), true)
				.addField("Unique colour: ", formatHexString(this.unique_col), true)
				.build();
	}

	public static String formatHexString(int colour)
	{
		StringBuilder sb = new StringBuilder("#");
		for(int i = 0; i < 6 - Integer.toHexString(colour).length(); i++)
			sb.append("0");
		sb.append(Integer.toHexString(colour));
		return sb.toString().toUpperCase();
	}
}
