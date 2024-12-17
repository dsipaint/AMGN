# Developing custom plugins for your bot

A large part of AMGN is the ability to use plugins to have custom features running on your bot. To do this, AMGN comes with a development kit. First we need to add this kit as a development dependency to our project.

You should first read [Run AMGN as a bot owner](https://github.com/dsipaint/AMGN/runtime.md) if you haven't already, as it will help with some of the principles here.

AMGN is written in java and uses maven. To use AMGN to develop plugins you must first make a maven project. Then clone this repo and run `./dependency.sh`
```bash
git clone https://github.com/dsipaint/AMGN.git
cd AMGN
./dependency.sh
```

**This project is heavily modelled on the JDA library- it is strongly recommended you become familiar with this before developing plugins as the library and its principles are used throughout.**

After this, in your plugin folder's `pom.xml`, you need to add the following dependency:
```xml
  <dependencies>
    <dependency>
      <groupId>com.github.dsipaint</groupId>
      <artifactId>AMGN</artifactId>
      <version>1.0</version>
    </dependency>
  </dependencies>
```

You are now officially ready to start developing your plugin.

## plugin.yml

All AMGN plugins must include a `plugin.yml` file in the `src/main/resources` directory. This file contains information about your plugin and will help your project be recognised as a plugin at runtime.
The general form of `plugin.yml` is this:

```yaml
  name: "plugin name"
  author: "author name"
  description: "plugin description"
  url: "url for the author"
  image: "image url representing the plugin"
  version: "plugin version"
  commands:
    - label: "command name (one word)"
      description: "command description"
      usageinfo: "explain how to use the command"
      permission: "name of permission to associate with this command"
    
    - label: "othercommand"
      usageinfo: "just run &othercommand"
      permission: "pluginname.commands.othercommand"

  permissions:
    - "pluginname.commands.othercommand"
    - "pluginname.commands.newcommand"
    - "pluginname.admin"
```

`permissions` specifies permissions that your plugin intends on making use of at some point. They can be called any string, but as best practice, try to follow the naming convention `pluginname.category.optionalsubcategory.use`. This will make it easier for users and yourself to identify and understand permissions.
The `permission` of a command must be one of these permissions.
If in the `usageinfo` parameter of your commands, you use the template value `${PREFIX}` then  your usage command will always use the set prefix of the guild the help command is used in. 

Image, url, description and author are technically not needed to run the plugin, but everything else is. If you are having problems running your plugin, check your plugin.yml includes all the data it needs. Almost all fields are mandatory except these. AMGN will use this metadata to manage permissions for your plugin automatically, as well as display information in discord and on the webpanel about your plugin, and how your commands work, when the help command is used.

## Writing your plugin entrypoint

Now you are ready to start adding some actual functionality to your bot plugin. To do this, your plugin needs an entrypoint. This is defined by AMGN and is done by extending the main class of your plugin as a `Plugin` object. This also requires inheriting the `onEnable` and `onDisable` methods. Your plugin should look something like this:

```java
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

public class MyPlugin extends Plugin
{
    public void onEnable()
    {

    }

    public void onDisable()
    {

    }
}
```

`onEnable` is the true entry-method of your plugin. This defines what will happen when the plugin is loaded by AMGN. This is where you define your plugin's action. Similarly, `onDisable` is called when your plugin is disabled or the bot shuts down. So `onEnable` is where we mainly initialise our plugin.

## Listeners
If you're using this guide you likely have experience with bot development and/or the JDA library. You will be familiar with the concept of Listeners and how essential they are to making a discord bot. A Listener will listen for an action on discord to occur and then respond in a certain way to the event, decided by you. AMGN has formal support for listeners. To create a listener in your plugin, create a new class and have it extend `Listener` from the AMGN library:

```java
import com.github.dsipaint.AMGN.entities.listeners.Listener;

public class MyListener extends Listener
{

}
```

In this class, you can then add any methods that the JDA `ListenerAdapter` supports- [see here](https://javadoc.io/doc/net.dv8tion/JDA/latest/net/dv8tion/jda/api/hooks/ListenerAdapter.html). For example, if you wish to listen for bans in a guild, your listener will look like this:
```java
import com.github.dsipaint.AMGN.entities.listeners.Listener;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;

public class MyListener extends Listener
{
    @Override
    public void onGuildBan(GuildBanEvent e)
    {
        System.out.println("A user was just banned");
    }
}
```

In this way you can create listeners for any event that occurs in discord, and act on them as you wish. Again, refer to the link above to understand what methods and events are available.
You now need to add your listener to AMGN and have it used, and associated with your plugin. To do this, you must call the `GuildNetwork.registerListener` method. This is almost always done in the `onEnable` method of your plugin as you want your listener to be created when your plugin is:
```java
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.entities.GuildNetwork;

public class MyPlugin extends Plugin
{
    public void onEnable()
    {
        GuildNetwork.registerListener(new MyListener(), this);
    }

    public void onDisable()
    {
        
    }
}
```

You can register as many or as few listeners to your plugin as you want, and each listener can inherit as many or as few methods as you want. More often than not, you will want your plugin to include a listener, however.

## Commands
**Programming commands is NOT done using listeners in AMGN. Although you can use listeners to program commands, your command will not be properly integrated with AMGN if you do this**. For example, your command will not be available on the help command and will have reduced permissions support. You have to program your own checks for these, and check manually for the guild's specific command prefix. It is highly recommended to use the below technique for programming commands into your plugin to save you time and effort.

First, your command should already be defined in your `plugin.yml` file. Once this is done, the process is relatively similar to creating a listener. Begin by creating a class that extends `Command`. This class will also need to inherit the method `onCommand` and have a constructor which calls its superconstructor. This superconstructor needs the `Plugin` object passed to it. Here is an example of how this all looks:

```java
import com.github.dsipaint.AMGN.entities.listeners.Command;
import com.github.dsipaint.AMGN.entities.listeners.CommandEvent;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

public class MyCommand extends Command
{
    public MyCommand(Plugin plugin)
    {
        super(plugin, "commandname");
    }

    public void onCommand(CommandEvent e)
    {
        System.out.println("Command was fired!");
    }
}
```
The command name passed to the super constructor *must* be present as a command in `plugin.yml`.

From here, you add your command to AMGN in a very similar way:
```java
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.entities.GuildNetwork;

public class MyPlugin extends Plugin
{
    public void onEnable()
    {
        GuildNetwork.registerCommand(new MyCommand(this), this);
    }

    public void onDisable()
    {
        
    }
}
```

### Advantages of AMGN commands:
This is superior to the old way of coding discord bots, using a normal Listener to write a bot command, for several reasons:

- You do not need to program a check to see if the message sent starts with the guild's set command prefix. This is handled automatically by AMGN

- You do not need to program a check to see if the message sent starts with the command label after the command prefix. This is handled automatically by AMGN.

- You do not need to program a check to see if the sender has permission to run the command. This is handled automatically by AMGN.

This event will not fire if these conditions are not met, which is automatically checked by AMGN before calling your listener. So in your `onCommand` method you can *just* focus on programming command functionality without having to add all these extra steps. Also,

- Your command is automatically added to the help command. Unloading or loading the plugin will automatically update the help command.
- Your command can now be run programmatically (see below)

### CommandEvent
The argument in this method, the `CommandEvent`, comes with the following methods, for information about the invoked command event:

| Method | Type | Description |
| ------ | ---- | ----------- |
| getArgs | `String[]` | array of arguments the command was passed with |
| getGuild | `net.dv8tion.jda.api.entities.Guild` | the guild the command was used in |
| getMessage | `net.dv8tion.jda.api.entities.Message` | the message used to send the command to discord |
| getSender | `net.dv8tion.jda.api.entities.Member` | the guild member that sent this command |
| getTextChannel | `net.dv8tion.jda.api.entities.channel.concrete.TextChannel` | the text channel that this command was sent in |
| isProgrammedCommand | `boolean` | returns `true` if this command was sent programmatically, `false` otherwise |



### Note about Listeners and Commands
> If you have set up your listeners and commands properly with AMGN, you do **NOT** need to unregister/unload your commands and listeners in your plugin's `onDisable` method. Listener and command unloading is handled automatically whenever your plugin is unloaded and this does **NOT** need to be handled by you.

## Logging
When certain events occur, a user may also wish to send a formal log to the guild's designated modlogs channel. AMGN provides support for this: `GuildNetwork.sendToModlogs(long guild_id, String message)`. This method sends a message with the standard AMGN modlogs format, to the modlogs of the specified guild. You may also access the `AMGN.logger` if you wish to do any console-logging.
The AMGN console logger has a default level of INFO, and any ERROR logs are automatically outputted to `errors.log` in your bot jar's directory.

## Menus
There are 2 big functionalities of most discord bots: commands, and menus.

A menu is a discord message with emotes attached- when the emotes are clicked or unclicked, a functionality is performed by the discord bot.

Setting these up manually using listeners is a lot of effort as it requires setting up specific listeners for specific messages with specific emotes, and can get messy very quickly and easily keeping track of everything. AMGN provides inbuilt support for making menu-creation much simpler.
To do this we use the MenuBuilder class:

```java
MenuBuilder builder = new MenuBuilder(message, new Button(emote, press ->
	{
		System.out.println(press.getEmote().getName()  + " button was pressed!");
	},
	unpress ->
	{
		System.out.println(unpress.getEmote().getName() + " button was unpressed!");
	}));

Menu menu = builder.build();
```

This will set up a menu on a pre-existing message, with one button. You can alternatively pass the constructor the text to become a message, or the embed object to become a message. This message will be sent when the build method is called. The button uses an emote of your choice, set in the constructor. The two Consumers in the Button constructor are respectively handlers for what to do when the button is pressed, and then unpressed passing a `MenuButtonClickEvent` object as a parameter to those consumers. This object contains all info about the click or unclick event, accessed with getters.
A menu is immutable once made, but contains some getters for getting the message and buttons associated with it. To add more buttons or remove buttons before the Menu is built, we can do this:

```java
builder.addButton(emote2, message, press ->
	{
		System.out.println("Our other button was pressed!");
	},
	unpress ->
	{
		System.out.println("Our other button was unpressed!");
	});
```

we can also use the `builder.removeButton` method to remove a button that was added. These commands can be chained too, like so:
```java
MenuBuilder builder = new MenuBuilder(emote, message, press ->
	{
		System.out.println(press.getEmote().getName() + " button was pressed!");
	},
	unpress ->
	{
		System.out.println(unpress.getEmote().getName() + " button was unpressed!");
	})
	.addButton(emote2, message, press ->
	{
		System.out.println("Our other button was pressed!");
	},
	unpress ->
	{
		System.out.println("Our other button was unpressed!");
	});
```
And so on for how many more buttons you need to add. To then build the Menu, which will send the message if not already sent, and add the buttons and their functionality. Finally, if you want to destroy an existing menu, simply reference the menu and call its `destroy` method.
AMGN does not require that you handle any listeners for menus at all- it is all handled for you so you can solely focus on the functionality of your menu.

## Running commands programmatically
Commands can also be called not just from a real discord member, but you can simulate a command being run from your code. By using `AMGN.runCommand(commandstring, member)`, the command will be run as if it is run as the member. If the member is `null`, then it is assumed to be a programmed command. This can be checked for in a plugin's onCommand methods by using `event.isProgrammedCommand()`.

## Managing plugin configs
Developers may wish to allow some form of storage/customisation for their plugin settings, and AMGN has inbuilt support for this. Every `Plugin` object comes with a `Config` object that can handle a config setup for a plugin. Configs are usually kept in `plugins/{plugin name}/`, where `{plugin name}` is the name of your plugin defined in `plugin.yml`. To access the Config object for your plugin, simply call `Plugin.getConfig()`. For example from within your Plugin class:

```java
	Config config = this.getConfig();
```

There are two types of config. Plugins can have global configs, and guild-specific configs. Global config files are found in the plugin's root directory i.e. `/plugins/pluginname/`. Guild-specific config files are found in `/plugins/pluginname/guildid/`. Either config can be accesssed with convenience methods. In this way config settings can be easily shared across multiple guilds, or made different from each other.

You can then retrieve values from an existing config file like this:
```java
	String myglobalvalue = (String) config.getValueFromGlobalConfig("config_file.yml", "value name");
	String myguildvalue = (String) config.getValueFromGuildConfig("config_file.yml", "value name", guild);
	String myvalue = (String) config.getValue("config_file.yml", "value name");
```

This also demonstrates the *config hierarchy* within AMGN. the first two methods shown are self-explanatory, the last one needs some explaining. If you simply tell AMGN to fetch a config value like this, AMGN will first query a guild config if it is present (and if a guild is specified in the method). It will then query the global config for a value if none is found. If still no value is found, it will try the default config you have stored in your `src/main/resources` directory. If still nothing is found, `null` is returned.

If you wish to retrieve an entire config file, this can be done by using `Config.getGlobalConfig`, `Config.getGuildConfig` and `Config.getConfig`. These work according to the hierarchy we just talked about. This will return a `Map<String, Object>` object containing the entire config file. 

There also exists `getDefaultConfig` and `getDefaultValue` methods, though I don't recommend relying on these.
There are also equivalent *setters* for all of these methods, to save values back to a config.

### Generating config files
Before values can be retrieved, written and/or processed from a config file, the file must first exist. You must first write a default config file. Try to make this as out-the-box compatible as you can, by accounting for good default values and input-sanitisation in your code. These default config files exist in `src/main/resources` in your project, and are YAML files. There can be as many config files as you want to use for your plugin.

To generate a config file in the actual AMGN directory, call `Config.generateGlobalResource` and `Config.generateLocalResource`, passing in your config file name as an argument. AMGN will then copy the default config to either `plugins/pluginname/` or `plugins/pluginname/guildid` if a local resource is created.

This is an example of a couple of lines you might write in your Plugin's onEnable method
```java
		//generate the default config if there is no config present
        File config = new File(this.getGlobalConfigPath() + "/config.yml");
        if(!config.exists())
            this.getConfig().generateGlobalResource("config.yml");
```

## The webpanel
AMGN is designed to handle all the peripherals of being a bot developer, so you can focus solely on the functionality of your bot. This is why AMGN can automatically take your plugin's config files and display them on its webpanel for authorised users to edit. This is a more user-friendly approach to editing plugin config files and works out of the box- so there's nothing for a plugin developer to worry about. As long as the person running the bot has set up the webpanel correctly, your plugin's config will be displayed on the webpanel.

#### Please note
as a form of best-practice when developing AMGN plugins, it is adviced to represent any IDs (e.g. role ids, channel ids, etc) as `strings` rather than `longs`. This is due to a limitation in [ECMA's long storage](https://developer.mozilla.org/en-US/docs/JavaScript/Reference/Global_Objects/Number/toPrecision):
> ECMA-262 only requires a precision of up to 21 significant digits. Other implementations may not support precisions higher than required by the standard.