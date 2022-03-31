# AMGN- Al's Multi-Guild Network

## What is AMGN?
This project was designed to make being a discord bot dev easier- especially if you want to build a large-scale bot
serving many many servers. With this system, developers can write plugins for your discord network without needing
to know the bot's token. The advantage of plugins is that on a large scale, this provides the flexibility of managing bot features independently, helping to minimise downtime. I used this in the server I code for, of 330,000 users, and it improved the efficiency of our bot by about 600%. Bot features are managed independently, whilst also being controlled centrally by a central repository. This provides complete control without compromising flexibility. This library extends the JDA library, and knowledge of JDA is required to use this library.

## Howto
### Preparing the network:
Before running the network, the network needs to know a bit about the users and servers it will work with. The network
accesses this data by reading "./network.json". Without this file, the network will not run. This file contains 
crucial information for your bot, and takes this form:
```javascript
{
	"operators": [475859944101380106],
	"guild_data": [
		{
			"guild_id": 12345,
			"prefix": "^",
			"modlogs": 12345,
			"modrole": -1
		},
		{
			"guild_id": 634667687233978388,
			"prefix": "&",
			"modlogs": 634667687233978390,
			"modrole": -1
		},
		{
			"guild_id": 123456789,
			"prefix": ":",
			"modlogs": 23456455,
			"modrole": 12343545
		}
	]

}
```

The "token" field is your bot token- it is required in order to start the network, or the network closes.
The fields "operators" and "guild_data" are not strictly required. Guild data will be written to this file as the
network runs, in this format. If you yourself write in the guild data, each object in the "guild_data" array MUST
contain the field "guild_id", with a valid guild id. The other fields in this object are optional, but are written
with either set values or default values as the network runs. Here is a formal overview of the fields:
token: token of the bot account. This is a required field.
operators: tan array of longs, representing discord user-ids of operators of the network. An operator has full
	access to all commands on the network.
guild_data: contains all network metadata for guilds in the network. Each object of the array represents a single
	guild and the metadata associated with it. "guild_id" is the discord id of the guild as a long, "prefix" is the
	chosen command-prefix for commands made in this network, and used in this guild, "modlogs" is the discord id
	of a text channel in this guild- important updates about the network are posted in here, and "modrole" is the
	discord id of a role in the server, designated as a staff role, giving partial access to commands in the network,
	in this guild.
network.json must be in the same directory as the network, have the correct name (network.json) and contain the
required fields, in the above format, or the network will close. Otherwise, you are now able to run the network by 
simply running the jar file as normal.

## Plugins:
Included in this API are intrinsic plugins, adding quality-of-life commands and features that a plugin outside of this
library may struggle to facilitate for. This includes handling external plugins and the network metadata. For more info,
use the help command in a guild with your network's bot in, when you launch the network with no external plugins.
I have hinted at intrinsic and external plugins. As a developer, you are able to make external plugins for this network,
as is the entire point of this project. This jar not only runs the network, but contains the API to make a plugin. Here
I will teach you how to do this.
## Creating a Plugin:
Firstly, make sure this jar is in your classpath. There are two things that every plugin in the system needs:
an entrypoint and metadata. The metadata follows a similar format to network.json from above. It must be stored in a file
called plugin.json, in the root directory of your jar. As an example, here is the plugin.json from one of the intrinsic
plugins:

```javascript
{
	"name": "updatemeta",
	"author": "al~",
	"description": "allows the updating of a guild's metadata e.g. their prefix via command",
	"url": "https://github.com/dsipaint",
	"image": "https://cdn.discordapp.com/avatars/475859944101380106/7996d6a5b0db135563981a4000c38e98.png",
	"version": "0.0.1",
	"commands":[
		{
			"label": "updatemetainfo",
			"description": "Use to update your guild's metadata for the network",
			"usageinfo": "^usageinfo {prefix/modlogs/modrole} {new value}",
			"permission": "admin"
		},
		
		{
			"label": "viewmetainfo",
			"usageinfo": "Use to view a guild's metadata",
			"permission": "staff"
		}
	]
}
```

Here is a description of the fields:
name: the name of your plugin. This can be seen in the help command, and is used to enable and disable your plugin from the discord client. (required)
author: The author of the plugin (presumably you). This is seen in the help command again. (not required)
description: A description of your plugin- this is again seen in the help command. (not required)
url: A url- turns your author-name in the help command into a clickable url, to advertise whatever you want to as a developer. (not required)
image: An image url- an image to accompany your plugin in the help command (not required)
version: the version of your plugin- also seen in the help command (not required)

commands: an array containing command metadata- each object represents a command (not required)
	label: the keyword used to activate the command as a discord user (not required)
	description: a description of this command's functionality (not required)
	usageinfo: a helpful message describing how to use the command (not required)
	permission: can take several values, "operator", "admin", "staff", or "all". These determine who can access your command. (not required, default is admin)
	

This plugin.json forms the metadata of your plugin. You're then ready to code actual java, the real workings of the plugin.
A valid plugin needs an entrypoint, so that the network knows how to execute the plugin. The entry-class of your plugin
should therefore extend the Plugin class of the API. It therefore needs to inherit the methods Plugin.onEnable() and Plugin.onDisable().
Plugin.onEnable() is essentially your main-method of the plugin. This is the real entrypoint, the code the network will run when your 
plugin is enabled. Plugin.onDisable() is run when your plugin is disabled- either via command, or as the network shuts down.
The GuildNetwork class is designed to represent your network, containing all the essential data and methods you may need
over the network.
### Listeners:
In order to add a listener to your plugin, you must use the method GuildNetwork.registerListener(ListenerAdapter l, Plugin p);
The network will then create a JDA listener associated with your plugin. All listeners you register with your plugin are automatically
unregistered and deactivated when your plugin is disabled- no need to do this yourself in your Plugin.onDisable(). The parameters of
GuildNetwork.registerListener(ListenerAdapter l, Plugin p) are the listener you wish to register, and the plugin you wish to register it to
(i.e. the plugin you are making it for, this plugin). The listeneradapter comes from the JDA API, and is used as specified
by JDA.
### Commands:
Commands differ from listeners in AMGN in that they have more rigid support. Technically, one can register a listener,
and use that listener as a command. This would mean one would not have to document the command in plugin.json, however,
this command could then not be found in the intrinsic help command. For formal command support, commands must be registered
separately to listeners, using GuildNetwork.registerCommand(Plugin p, Command c). The plugin parameter is the same as for
registering a listener- it refers to this plugin instance i.e. "this" in java syntax. The command parameter must be a
Command object, which is again found in the API. The Command class is abstract, and is meant to be extended before use.
create a class which extends this class. It must inherit the JDA method ListenerAdapter.onMessageReceived(MessageReceivedEvent)
the constructor of this class must also call its superclass constructor, which accepts a plugin instance (again, the plugin object we registered
this listener with), and a string representing the label parameter of the command in the plugin.json file. From here, the constructor
will ensure that this listener inherits the correct command metadata from plugin.json. As for listeners, commands are automatically
unregistered and deactivated when the plugin is disabled in any manner, so don't worry about doing this in the onDisable() method.

An example plugin, which registers an example Listener and an example Command is shown here, including the plugin.json for this plugin:

ExamplePlugin.java:

```java
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.main.GuildNetwork;

public class ExamplePlugin extends Plugin
{
	public void onEnable()
	{
		GuildNetwork.registerListener(new ExampleListener(this), this);
		GuildNetwork.registerCommand(new ExampleCommand(this), this);
	}
	
	public void onDisable()
	{
	
	}
}
```
ExampleListener.java:
```java
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ExampleListener extends ListenerAdapter
{
	public void onTextChannelDelete(TextChannelDeleteEvent e)
	{
		System.out.println("A channel was deleted");
	}
}
```
ExampleCommand.java:
```java
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.entities.listeners.Command;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ExampleCommand extends Command
{
	public ExampleCommand(Plugin plugin)
	{
		super(plugin, "example"); //the command label is "example" in plugin.json
	}
	
	public void onMessageReceived(MessageReceivedEvent e)
	{
		System.out.println("Command code goes here as in JDA");
	}
}
```
plugin.json:
```javascript
{
	"name": "exampleplugin",
	"author": "al~",
	"description": "Simple plugin to demonstrate the basics of plugin-making",
	"url": "https://github.com/dsipaint",
	"image": "https://cdn.discordapp.com/avatars/475859944101380106/7996d6a5b0db135563981a4000c38e98.png",
	"version": "0.0.1",
	"commands":[
		{
			"label": "example",
			"description": "Just an example command, nothing special",
			"usageinfo": "Just use ^example",
			"permission": "all"
		}
	]
}
```
This sets up a basic plugin with as of yet, no function.
The GuildNetwork class then contains many methods to make creating your commands (or listeners) much much easier.
We can use GuildNetwork.getPrefix(long guild_id) to get the prefix for whichever guild the command is used in, or
the default prefix if none is chosen. We can also use Command.getLabel to get the label of the command we just
created. The superconstructor sets all of the command metadata ready to be called by Command's methods in this way.
We can use these two methods like this:
```java
public void onMessageReceived(MessageReceivedEvent e)
{
	String msg = e.getMessage().getContentRaw();
	String[] args = msg.split(" ");
	
	//this checks if the command is indeed used
	if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + this.getLabel()))
	{
		e.getChannel().sendMessage("The example command was used!").queue();
	}
}
```
The Command class also comes with a method to check if a member has permission to use the command, as opposed to doing
this manually, with Command.hasPermission(Member m):
```java
public void onMessageReceived(MessageReceivedEvent e)
{
	String msg = e.getMessage().getContentRaw();
	String[] args = msg.split(" ");
	
	//this checks if the command is indeed used
	if(args[0].equalsIgnoreCase((GuildNetwork.getPrefix(e.getGuild().getIdLong()) + this.getLabel())
		&& this.hasPermission(e.getMember()))
	{
		e.getChannel().sendMessage("The example command was used and the user had permission!").queue();
	}
}
```
The Command class has getter methods for all the metadata you assigned in plugin.json, which you can use for
whatever you need. GuildNetwork also contains getters for all guild metadata, similar to GuildNetwork.getPrefix(long guild_id).

When certain events occur, a user may also wish to send a formal log to the guild's designated modlogs channel.
AMGN provides support for this. GuildNetwork.sendToModlogs(long guild_id, String message). This method sends a message
with the standard AMGN modlogs format, to the modlogs of the specified guild.

With these tools, you are able to make a plugin with AMGN. You can then compile this plugin as a non-executable jar.
All plugins must be placed in a folder called "plugins", which should be located in the same directory as the network.
This is where the network will look for plugins. By placing a plugin here and launching the network, the plugin will
be automatically enabled. This plugin can also be enabled after launching the network by placing it in the plugins
directory and using the enableplugin command. Plugins can be disabled similarly with the disableplugin command.

These are the basics of using AMGN. For more help, please contact the author on discord (al~#1819) or look at the
javadocs- have fun, and get coding!!
