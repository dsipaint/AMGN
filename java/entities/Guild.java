package entities;

public class Guild
{
	private String prefix;
	private long guild_id, modlogs, modrole;
	
	public Guild(long guild_id, long modlogs, long modrole, String prefix)
	{
		this.guild_id = guild_id;
		this.modlogs = modlogs;
		this.modrole = modrole;
		this.prefix = prefix;
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
}
