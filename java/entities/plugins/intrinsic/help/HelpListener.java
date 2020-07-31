package entities.plugins.intrinsic.help;

import entities.listeners.Command;
import entities.plugins.Plugin;
import main.GuildNetwork;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class HelpListener extends Command
{
	public HelpListener(Plugin main)
	{
		super(main, "help");
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		
		//^help
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + this.getLabel()))
		{
			//^help
			if(args.length == 1)
			{
				//all known commands
				return;
			}
			
			//^help {command}
			//search for command with the name of args[1], and return its help syntax
		}
	}
}
