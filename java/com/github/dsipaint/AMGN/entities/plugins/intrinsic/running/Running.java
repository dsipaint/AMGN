package com.github.dsipaint.AMGN.entities.plugins.intrinsic.running;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

public final class Running extends Plugin
{
	public void onEnable()
	{
		GuildNetwork.registerCommand(new RunningListener(this), this);
	}
	
	public void onDisable()
	{
		
	}
}
