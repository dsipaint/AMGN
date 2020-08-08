package com.github.dsipaint.AMGN.entities.plugins.intrinsic.controlEnableDisable;

import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.main.GuildNetwork;

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
