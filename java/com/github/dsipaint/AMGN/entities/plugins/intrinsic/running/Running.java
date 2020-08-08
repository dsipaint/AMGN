package com.github.dsipaint.AMGN.entities.plugins.intrinsic.running;

import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.main.GuildNetwork;

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
