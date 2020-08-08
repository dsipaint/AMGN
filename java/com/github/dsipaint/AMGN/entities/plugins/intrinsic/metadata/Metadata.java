package com.github.dsipaint.AMGN.entities.plugins.intrinsic.metadata;

import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.main.GuildNetwork;

public class Metadata extends Plugin
{
	public void onEnable()
	{
		GuildNetwork.registerCommand(new MetaUpdateListener(this), this);
		GuildNetwork.registerCommand(new MetaViewListener(this), this);
	}
	
	public void onDisable()
	{
		
	}
}
