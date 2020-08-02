package entities.plugins.intrinsic.closenetwork;

import entities.plugins.Plugin;
import main.GuildNetwork;

public class CloseNetwork extends Plugin
{
	public void onEnable()
	{
		GuildNetwork.registerCommand(new CloseListener(this), this);
	}
	
	public void onDisable()
	{
		
	}
}
