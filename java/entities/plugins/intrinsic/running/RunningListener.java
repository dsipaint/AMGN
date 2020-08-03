package entities.plugins.intrinsic.running;

import entities.listeners.Command;
import entities.plugins.Plugin;
import main.GuildNetwork;
import main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class RunningListener extends Command
{
	public RunningListener(Plugin plugin)
	{
		super(plugin, "showplugins");
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent e)
	{
		String msg = e.getMessage().getContentRaw();
		String[] args = msg.split(" ");
		
		if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + this.getLabel()))
		{
			EmbedBuilder eb = new EmbedBuilder()
					.setTitle("Active plugins: ")
					.setColor(GuildNetwork.GREEN_EMBED_COLOUR);
			
			Main.plugin_listeners.forEach((plugin, listeners) ->
			{
				appendEmbed(eb, plugin.getName() + " " + plugin.getVersion() + " (written by " + plugin.getAuthor() + ")\n");
			});
			
			e.getChannel().sendMessage(eb.build()).queue();
		}
	}
	
	private void appendEmbed(EmbedBuilder eb, String msg)
	{
		eb = eb.appendDescription(msg);
	}
}
