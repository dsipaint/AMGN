package entities.plugins.intrinsic.running;

import entities.plugins.Plugin;
import main.GuildNetwork;

public class Running extends Plugin
{
	public void onEnable()
	{
		GuildNetwork.registerCommand(new RunningListener(this), this);
	}
	
	public void onDisable()
	{
		
	}
}
