package com.github.dsipaint.AMGN.entities.plugins.intrinsic.help;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

public final class Help extends Plugin
{

	public void onEnable()
	{
		GuildNetwork.registerCommand(new HelpListener(this), this);
	}

	public void onDisable()
	{
		
	}
	
}
