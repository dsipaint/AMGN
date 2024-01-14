# AMGN- Al's Multi-Guild Network

## What is AMGN?
This project was designed to make being a discord bot dev easier- especially if you want to build a large-scale bot serving many many servers. With this system, developers can write plugins for your discord network without needing to know the bot's token. The advantage of plugins is that on a large scale, this provides the flexibility of managing bot features independently, helping to minimise downtime. I used this in the server I code for, of 330,000 users, and it improved the efficiency of our bot by about 600%. Bot features are managed independently, whilst also being controlled centrally by a central repository. This provides complete control without compromising flexibility. This library extends the JDA library, and knowledge of JDA is required to use this library.

There are 2 ways to use this code, and I have written guides for both:
[Run AMGN as a bot owner](https://github.com/dsipaint/AMGN/runtime.md)
[Use AMGN as a development dependency, plugin development](https://github.com/dsipaint/AMGN/plugindevelopment.md)

## Developing plugins for AMGN
I have hinted at intrinsic and external plugins. As a developer, you are able to make external plugins for this network,
as is the entire point of this project. This jar not only runs the network, but contains the API to make a plugin.


Firstly, make sure this jar is in your classpath. There are two things that every plugin in the system needs: an entrypoint and metadata.

#### metadata
The metadata follows a similar format to `network.yml` from above. It must be stored in a file called `plugin.yml`, in the root directory of your jar. If you are using a maven setup, this put `plugin.yml` in `src/main/resources`. As an example, here is the plugin.yml from one of the intrinsic
plugins:

```yaml
  name: "updatemeta"
  author: "al~"
  descriptionW: "allows the updating of a guild's metadata e.g. their prefix via command"
  url: "https://github.com/dsipaint"
  image: "https://cdn.discordapp.com/avatars/475859944101380106/7996d6a5b0db135563981a4000c38e98.png"
  version: "0.0.1"
  commands:
    - label: "updatemetainfo"
      description: "Use to update your guild's metadata for the network"
      usageinfo: "^usageinfo {prefix/modlogs/modrole} {new value}"
      permission: "admin"
    
    - label: "viewmetainfo"
      usageinfo: "Use to view a guild's metadata"
      permission: "staff"
```

| Variable | Required | Type | Description |
| name | yes | string | the name of your plugin. This can be seen in the help command, and is used to enable and disable your plugin from the discord client. |
| author | no | string | The author of the plugin (presumably you). This is seen in the help command again. |
| description | no | string | A description of your plugin- this is again seen in the help command. |
| url | no | string | A url- turns your author-name in the help command into a clickable url, to advertise whatever you want to as a developer. |
| image | no | string | An image url- an image to accompany your plugin in the help command |
| version | no | string | the version of your plugin- also seen in the help command |
| commands | yes if you have programmed commands | list of objects | an array containing command metadata- each object represents a command |
| label | yes if you have programmed this command | string | the keyword used to activate the command as a discord user |
| description (for commands) | yes if you have programmed this command | string | a description of this command's functionality |
| usageinfo | yes if you have programmed this command | string | a helpful message describing how to use the command |
| permission | no, default permission is admin | string of value "operator" "admin" "staff" or "all" | These determine who can access your command |
	

This `plugin.yml` forms the metadata of your plugin. You're then ready to code actual java, the real workings of the plugin.

#### entrypoint
A valid plugin needs an entrypoint, so that the network knows how to execute the plugin. The entry-class of your plugin should therefore extend the `Plugin` class of the AMGN API. It therefore needs to inherit the methods Plugin.onEnable() and Plugin.onDisable().
- Plugin.onEnable() is essentially your main-method of the plugin. This is the real entrypoint, the code the network will run when your 
plugin is enabled.
- Plugin.onDisable() is run when your plugin is disabled- either via command, or as the network shuts down.

The `GuildNetwork` class is designed to represent your network, containing all the essential metadata and methods you may need
over the network.

### Listeners:
In order to add a listener to your plugin, you must use the method `GuildNetwork.registerListener(ListenerAdapter l, Plugin p)`. The network will then create a JDA listener associated with your plugin. **All listeners you register with your plugin are automatically unregistered and deactivated when your plugin is disabled- no need to do this yourself in your Plugin.onDisable()**. The parameters of GuildNetwork.registerListener(ListenerAdapter l, Plugin p) are the listener you wish to register, and the plugin you wish to register it to (i.e. the plugin you are making it for, this plugin). The listeneradapter comes from the JDA API, and is used as specified by JDA.

### Programming Commands:
Commands differ from listeners in AMGN in that they have more rigid support. Technically, one can register a listener, and use that listener as a command. This would mean one would not have to document the command in the plugin's `plugin.yml`, however, this command could then not be found in the intrinsic help command, or be treated formally as a command by AMGN. For formal command support, commands must be registered separately to listeners, using `GuildNetwork.registerCommand(Plugin p, Command c)`. The plugin parameter is the same as for registering a listener- it refers to this plugin instance i.e. `this` in java syntax. The command parameter must be a Command object, which is again found in the API. The Command class is abstract, and is meant to be extended before use.

Create a class which extends this class. It must inherit the JDA method `Command.onCommand(CommandEvent e)`. The constructor of this class must also call its superclass constructor, which accepts a plugin instance (again, the plugin object we registered this listener with), and a string representing the label parameter of the command in the plugin.yml file, already specified. From here, the constructor will ensure that this listener inherits the correct command metadata from plugin.yml. **As for listeners, commands are automatically unregistered and deactivated when the plugin is disabled in any manner, so don't worry about doing this in the onDisable() method.**

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
import com.github.dsipaint.AMGN.entities.listeners.Command;

public class ExampleCommand extends Command
{
	public ExampleCommand(Plugin plugin)
	{
		super(plugin, "example"); //the command label is "example" in plugin.yml
	}
	
	public void onCommand(CommandEvent e)
	{
		System.out.println("Command code goes here as in JDA");
	}
}
```
plugin.yml:
```yaml
  name: "exampleplugin"
  author: "al~"
  description: "Simple plugin to demonstrate the basics of plugin-making"
  url: "https://github.com/dsipaint"
  image: "https://cdn.discordapp.com/avatars/475859944101380106/7996d6a5b0db135563981a4000c38e98.png"
  version: "0.0.1"
  commands:
    - label: "example",
      description: "Just an example command, nothing special",
      usageinfo: "Just use ^example",
      permission: "all"
```
This sets up a basic plugin with as of yet, no function.
The `GuildNetwork` class then contains many methods to make creating your commands (or listeners) much much easier. We can use `GuildNetwork.getPrefix(long guild_id)` to get the prefix for whichever guild the command is used in, or the default prefix if none is chosen. We can also use `Command.getLabel` to get the label of the command we just created. The superconstructor sets all of the command metadata ready to be called by Command's methods in this way. The command is only run if the member has the relevant permissions, and this is checked before firing the event by AMGN.
We can use these two methods like this:
```java
public void onCommand(CommandEvent e)
{	
	//this checks if the command is indeed used
	if(e.getArgs[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + this.getLabel()))
	{
		e.getChannel().sendMessage("The example command was used!").queue();
	}
}
```
The Command class has getter methods for all the metadata you assigned in plugin.json, which you can use for whatever you need. GuildNetwork also contains getters for all guild metadata, just like we saw `GuildNetwork.getPrefix(long guild_id)`.

###  Programming logging
When certain events occur, a user may also wish to send a formal log to the guild's designated modlogs channel. AMGN provides support for this: `GuildNetwork.sendToModlogs(long guild_id, String message)`. This method sends a message with the standard AMGN modlogs format, to the modlogs of the specified guild. You may also access the `AMGN.logger` if you wish to do any console-logging.

### Programming Menus
AMGN now has inbuilt support for menus! A menu is a message with reactions placed on by a bot. When a reaction is pressed, the bot performs some action- this can be anything from giving a role to that user, or "changing the page" of a message backwards and forwards. To do this, use the MenuBuilder class:

```java
MenuBuilder builder = new MenuBuilder(message, new Button(emote, press, unpress ->
	{
		System.out.println(press.getEmote().getName()  + " button was pressed!");
	},
	message ->
	{
		System.out.println(unpress.getEmote().getName() + " button was unpressed!");
	}));

Menu menu = builder.build();
```

This will set up a menu on a pre-existing message, with one button. You can alternatively pass the constructor the text to become a message, or the embed object to become a message. This message will be sent when the build method is called. The button uses an emote of their choice, set in the constructor. The two Consumers in the Button constructor are respectively handlers for what to do when the button is pressed, and then unpressed passing a `MenuButtonClickEvent` object as a parameter to those consumers. This object contains all info about the click or unclick event, accessed with getters.
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

With these tools, you are able to make a plugin with AMGN. You can then compile this plugin as a non-executable jar. All plugins must be placed in a folder called "plugins", which should be located in the same directory as the network. This is where the network will look for plugins. By placing a plugin here and launching the network, the plugin will be automatically enabled. This plugin can also be enabled after launching the network by placing it in the plugins directory and using the `enableplugin` command. Plugins can be disabled similarly with the disableplugin command.

### Running commands programmatically
Commands can also be called not just from a real discord member, but you can simulate a command being run from your code. By using `AMGN.runCommand(commandstring, member)`, the command will be run as if it is run as the member. If the member is `null`, then it is assumed to be a programmed command. This can be checked for in a plugin's onCommand methods by using `event.isProgrammedCommand()`.

### Managing plugin configs
Developers may wish to allow some form of storage/customisation for their plugin settings, and AMGN has inbuilt support for this. Every `Plugin` object comes with a `Config` object that can handle a config setup for a plugin. Configs are usually kept in `plugins/{plugin name}/`, where `{plugin name}` is the name of your plugin defined in `plugin.yml`. To access the Config object for your plugin, simply call `Plugin.getConfig()`. For example inside the onEnable method:

```java
	Config config = this.getConfig();
```
There are two types of config. Plugins can have global configs, and guild-specific configs. Global config files are found in the plugin's root directory i.e. `/plugins/pluginname/`. Guild-specific config files are found in `/plugins/pluginname/guildid/`. Either config can be accesssed with convenience methods. Plugins should prefer to use guild-specific configs first, before attempting to use a global config. If a guild-specific config file does not exist (either by being manually deleted or by being set in the webpanel), the global config file will be used instead. In this way config settings can be easily shared across multiple guilds, or made different from each other.

You can then retrieve values from an existing config file like this:
```java
	String myglobalvalue = (String) config.getValueFromGlobalConfig("config_file.yml", "value name");
	String myguildvalue = (String) config.getValueFromGuildConfig("config_file.yml", "value name", guild);
	String myvalue = (String) config.getValue("config_file.yml", "value name");
```

Note `config.getValue` will first attempt to get a value from a guild config. If this does not exist, it will try to go to the global config. Failing this, the default config value, set inside the jar, will be used.

This method returns a castable Object, the first parameter is the path to the config file relative to the plugin config path (which can also be referenced easily in code with `plugin.getConfigPath()`), and the second parameter is the name of the value you wish to retrieve. The eventually-cast string will be the value associated with this name in the yml file. There is also a method to retrieve a `Map` containing all key-value pairs in the yaml file (including nested ones). this is called like so:
```java
	Map<String, Object> wholeconfigmap = config.getConfig("filename");
```
where the filename is relative as before. For easily accessing values, or even nested values from within these returned Maps, there is the `getValueFromMap`, which accepts the Map and the String key to search for respectively as parameters.

Config files can be saved by calling the `save` method. This requires passing in the file path and the object to be written itself though.
Default config values are values that come from the plugin jar's internal immutable config file, and can be retrieved by calling `getDefaultConfig` and `getDefaultValue` for specific values. If no value is found, these methods return `null`.

Finally, it is possible a config file does not exist for a plugin yet. Developers should account for this themselves by catching `FileNotFoundException`s and not assuming the plugin has a generated config. You may find yourself writing this a lot in your plugins:
```java
		//generate the default config if there is no config present
        File config = new File(this.getConfigPath() + "/config.yml");
        if(!config.exists())
            this.getConfig().generateResource("config.yml");
```
Here we can see that AMGN provides a way to generate a config file that doesn't exist. Just call the `Config.generateResource` method, passing the name of the file you need to generate. How does AMGN know how to generate this file? In the `resources` folder, where you defined the `plugin.yml` file, you create the file here with the same name you wish to generate. Then in the file, write a default config you wish to exist when the file is generated by the plugin. When you call the generateResource method, AMGN will copy your template in `resources` inside the compiled jar, to the config filepath for your plugin. You can then normally use the methods to call and use values from this config. As of now, AMGN cannot write new values to existing configs.

## The webpanel
The purpose of AMGN is to simplify being a bot developer- if you are a bot developer there's a good chance you'd want to include a webpanel for users to configure your bot for their server or network. This is why AMGN comes pre-packaged with an auto-generated webpanel. The website is hosted on the address the bot is run on, on port 8080. You can see this by running the jar locally and visiting `localhost:8080/webpanel`. There are a number of options you will see here.
In order to use the webpanel and be able to login to your network, the application needs to use Discord's OAuth2 methods. This means your network needs to know your application's id and secret in order to log users into your app. You can find this in the developer portal, similar to how you get the token for your bot. Add this to your `network.yml` as a variable called `clientid` and `clientsecret`. Similarly, discord's authentication needs to know the redirect uri. This is where discord will redirect the user after they have signed in with discord. You should add the address you wish to redirect the user to in the developer portal for your bot (this can be found in the oauth2 tab -> "add redirect"), and then add the same address to `network.yml` under the name `redirecturi`. This redirect must take the form of your server's public address/url, followed with `/webpanel/redirect`. After this, AMGN and discord should have enough information to sign users into your webpanel.

Plugin developers make config files for their plugin. These are used to let the user customise their plugin install. This can be done in 2 ways. 1: the user manually edits the YAML file that is either present or created or 2: the user uses the webpanel to remotely change the YAML config file. The webpanel code will do its best to interpret your default config files and diplay them in a sensible way to the user on the webpanel. Please note that to interpret how to display the data, AMGN uses a tiered system. First it will check the type of value in the pre-packaged config file that the plugin jar was developed with. This is why it's important to provide default values, amongst other reasons. Then, it will use `null`. Please also note that though nested types are supported, lists/arrays of just booleans are not supported, because it was deemed weird.

The webpanel also makes use of your preconfigured permissions in the `network.yml` file. Any discord user can attempt to login to your webpanel, however only users specified as staff, people with admin permissions in a server where the bot lives, or operators will be able to login successfully. Operators can configure every setting from the console, however every other authenticated user will *not* be able to configure the `operators` setting, or a guild's `modrole` from the webpanel, as these are considered operator-settings. You can only modify permissions of a guild that you are in, unless you are an operator, in which case you can edit settings for guilds you are not necessarily in.

#### Please note
as a form of best-practice when developing AMGN plugins, it is adviced to represent any IDs (e.g. role ids, channel ids, etc) as `strings` rather than `longs`. This is due to a limitation in [ECMA's long storage](https://developer.mozilla.org/en-US/docs/JavaScript/Reference/Global_Objects/Number/toPrecision):
> ECMA-262 only requires a precision of up to 21 significant digits. Other implementations may not support precisions higher than required by the standard.

These are the basics of using AMGN. For more help, please contact the author on discord (al~#1819) or look at the javadocs- have fun, and get coding!!
