package com.github.dsipaint.AMGN.entities.plugins.intrinsic.operators;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

public final class Operators extends Plugin
{
	public void onEnable()
	{
		GuildNetwork.registerCommand(new OpAddListener(this), this);
		GuildNetwork.registerCommand(new OpRemoveListener(this), this);
	}
	
	public void onDisable()
	{
		
	}
}
