package com.github.dsipaint.AMGN.entities.plugins.intrinsic.closenetwork;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

public final class CloseNetwork extends Plugin
{
	public void onEnable()
	{
		GuildNetwork.registerCommand(new CloseListener(this), this);
	}
	
	public void onDisable()
	{
		
	}
}
