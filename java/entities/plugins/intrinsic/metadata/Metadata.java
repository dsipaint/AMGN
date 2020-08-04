package entities.plugins.intrinsic.metadata;

import entities.plugins.Plugin;
import main.GuildNetwork;

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
