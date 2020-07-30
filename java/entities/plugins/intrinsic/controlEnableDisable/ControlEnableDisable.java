package entities.plugins.intrinsic.controlEnableDisable;

import entities.plugins.Plugin;
import main.GuildNetwork;

public class ControlEnableDisable extends Plugin
{

	public void onEnable()
	{
		GuildNetwork.registerCommand(new EnableListener(this), this);
		GuildNetwork.registerCommand(new DisableListener(this), this);
	}

	public void onDisable()
	{
		
	}

}
