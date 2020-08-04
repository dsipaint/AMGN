package entities.plugins.intrinsic.metadata;

import entities.Guild;
import entities.listeners.Command;
import entities.plugins.Plugin;
import io.IOHandler;
import main.GuildNetwork;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class MetaUpdateListener extends Command
{
	public MetaUpdateListener(Plugin plugin)
	{
		super(plugin, "updatemetainfo");
	}
	
	//allows the changing of prefix, modlogs and modrole
	//if no guild metadata is found, this command also creates a guild object with default values
	//before using the command, and then writes this back to guilds.json
	//NOTE: defaulting guilds may break system for now- concurrency issue, looping through list and adding new guild to list
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		
		//^updatemetainfo
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + this.getLabel()))
		{
			if(args.length <= 2)
				return;
			
			//allows injection otherwise
			if(args[2].contains("\""))
				return;
			
			long guild_id = e.getGuild().getIdLong();
			
			//^updatemetainfo prefix {new prefix}
			if(args[1].equalsIgnoreCase("prefix"))
			{
				Guild guild = GuildNetwork.guild_data.getOrDefault(guild_id, new Guild(guild_id));
				guild.setPrefix(args[2]);
				GuildNetwork.guild_data.put(e.getGuild().getIdLong(), guild); //*overwrites* if data was already there, or *sets* if data was not
				IOHandler.writeGuildData(GuildNetwork.guild_data, GuildNetwork.GUILDINFO_PATH); //write this to guilds.json
				
				e.getChannel().sendMessage("New prefix for this guild was set to " + args[2]).queue();
				return;
			}
			
			//^updatemetainfo modlogs {modlogs id}
			if(args[1].equalsIgnoreCase("modlogs"))
			{
				//if valid id format
				if(args[2].matches("\\d{18}"))
				{
					for(TextChannel tc : e.getGuild().getTextChannels())
					{
						//and channel does indeed exist
						if(tc.getId().equals(args[2]))
						{
							Guild guild = GuildNetwork.guild_data.getOrDefault(guild_id, new Guild(guild_id));
							guild.setModlogs(Long.parseLong(args[2]));
							GuildNetwork.guild_data.put(e.getGuild().getIdLong(), guild); //*overwrites* if data was already there, or *sets* if data was not
							IOHandler.writeGuildData(GuildNetwork.guild_data, GuildNetwork.GUILDINFO_PATH); //write this to guilds.json
							
							e.getChannel().sendMessage("Modlogs channel for server updated to "+ tc.getAsMention()).queue();
							return;
						}
					}
				}
				
				return;
			}
			
			//^updatemetainfo modrole {modrole id}
			if(args[1].equalsIgnoreCase("modrole"))
			{
				//if valid id format
				if(args[2].matches("\\d{18}"))
				{
					for(Role r : e.getGuild().getRoles())
					{
						//if this role is indeed in this server
						if(r.getId().equals(args[2]))
						{
							Guild guild = GuildNetwork.guild_data.getOrDefault(guild_id, new Guild(guild_id));
							guild.setModrole(Long.parseLong(args[2]));
							GuildNetwork.guild_data.put(e.getGuild().getIdLong(), guild); //*overwrites* if data was already there, or *sets* if data was not
							IOHandler.writeGuildData(GuildNetwork.guild_data, GuildNetwork.GUILDINFO_PATH); //write this to guilds.json
							
							e.getChannel().sendMessage("Modrole updated to " + r.getName()).queue();
							return;
						}
					}
				}
				
				return;
			}
		}
	}
}
