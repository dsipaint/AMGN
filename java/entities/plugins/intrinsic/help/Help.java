package entities.plugins.intrinsic.help;

import entities.plugins.Plugin;
import main.GuildNetwork;

public class Help extends Plugin
{

	public void onEnable()
	{
		GuildNetwork.registerCommand(new HelpListener(this), this);
	}

	public void onDisable()
	{
		
	}
	
}
