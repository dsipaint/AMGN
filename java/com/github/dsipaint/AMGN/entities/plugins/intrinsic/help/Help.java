package com.github.dsipaint.AMGN.entities.plugins.intrinsic.help;

import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.main.GuildNetwork;

public class Help extends Plugin
{

	public void onEnable()
	{
		GuildNetwork.registerCommand(new HelpListener(this), this);
	}

	public void onDisable()
	{
		
	}
	
}
