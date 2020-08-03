package entities.plugins.intrinsic.help;

import entities.listeners.Command;
import entities.plugins.Plugin;
import main.GuildNetwork;
import main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
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
			String prefix = GuildNetwork.getPrefix(e.getGuild().getIdLong());
			
			//^help
			if(args.length == 1)
			{
				EmbedBuilder eb = new EmbedBuilder()
					.setTitle("Commands:")
					.setColor(GuildNetwork.GREEN_EMBED_COLOUR);
				
				
				//go through every plugin
				Main.plugin_listeners.forEach((plugin, list) ->
				{
					//and every listener
					list.forEach(listener ->
					{
						//if this is a command
						if(listener instanceof Command)
						{
							appendBuilder(eb, "**" + prefix + 
									((Command) listener).getLabel() + ":** " + ((Command) listener).getUsageinfo() + "\n");
						}
					});
				});
				
				e.getChannel().sendMessage(eb.build()).queue();
				
				return;
			}
			
			//^help {command}
			//search for command with the name of args[1], and return its help syntax
			
			//loop through all plugins and listeners
			Main.plugin_listeners.forEach((plugin, listeners) ->
			{
				//display all plugins AND commands with the specified name
				if(plugin.getName().equalsIgnoreCase(args[1]))
					e.getChannel().sendMessage(plugin.getDisplayEmbed()).queue();
				
				listeners.forEach(listener ->
				{
					//check registered commands, find one with the same name
					if(listener instanceof Command && ((Command) listener).getLabel().equalsIgnoreCase(args[1]))
					{
						EmbedBuilder eb = new EmbedBuilder()
							.setTitle(prefix + ((Command) listener).getLabel())
							.setColor(GuildNetwork.GREEN_EMBED_COLOUR)
							.setDescription(((Command) listener).getUsageinfo());
						
						eb = eb.addField("Author:", plugin.getAuthor(), true);
						eb = eb.addField("Plugin:", plugin.getName(), true);
						
						e.getChannel().sendMessage(eb.build()).queue();
						return;
					}
				});
			});
		}
	}
	
	
	//not allowed to do this in a lambda expression, so I do it here
	private void appendBuilder(EmbedBuilder eb, String msg)
	{
		eb = eb.appendDescription(msg);
	}
}
