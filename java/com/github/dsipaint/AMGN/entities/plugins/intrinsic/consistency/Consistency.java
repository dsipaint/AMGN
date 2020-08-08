package com.github.dsipaint.AMGN.entities.plugins.intrinsic.consistency;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

public final class Consistency extends Plugin
{
	public void onEnable()
	{
		GuildNetwork.registerListener(new ModroleListener(), this);
		GuildNetwork.registerListener(new ModlogsListener(), this);
	}
	
	public void onDisable()
	{
		
	}
}
