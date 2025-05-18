# 1.2.1
Fixed annoying bug that caused menu-creation to randomly generate exceptions

# 1.2
~ Tidied up some of the authentication methods in the WebpanelController for better input-sanitisation
~ Renamed ListenerWrapper to ListenerProxy, to make its purpose clearer
~ ListenerProxy now passes all events to a single event-handling method, where events are then distributed to plugin listeners. This makes it easier to maintain the event-handling algorithm, including changes to logging and allow-listing rules if they were to happen.
~ Added more verbose logging for any discord event failures, including any failures that occur in the handling of the event by a plugin's listener. This can also be sent to a discord log-channel. This makes plugin- and engine- debugging much more verbose. Commands will now also post messages in the channel they were invoked in, if an exception occurs.
+ Exceptions generated during commands, or during menu interactions, will now also log to stderr
~ Added logging for any plugin API endpoint failures incurred by a plugin's RestListener
+ Improved the verbosity around logging when enabling, disabling or reloading plugins
+ updated JDA to latest version

# 1.1
Added plugin API-forwarding, plugins can now send and receive REST http requests

# 1.0.2
Fixed permissions member caching causing permissions not to be respected.

# 1.0.1
Fixed bug where all events (except commands) fired twice, and would also bypass blacklisting rules

# 1.0!!!
~ AMGN will now continue to run when permissions.yml or whitelist.yml are missing
~ default whitelist.yml no longer includes a fake plugin, which could be annoying for setting up a whitelist
- removed erroneous debug print from config getValue method
~ fixed format of default permissions file
~ changed plugin initialise log messages to before loading rather than after (helps with plugin-debugging)
~ config methods no longer throw exceptions, this is captured internally and the exception messages should make clear that a config file is missing
+ add convenience methods setGlobalConfig, setGuildConfig, setConfig, setGlobalValue, setGuildValue and setValue for easily writing back to config files
+ add a plugin load order to network.yml. This allows bot owners to decide what order plugins are loaded in, if one plugin must be loaded before another for example.
~ Previously when retrieving a config value, AMGN would actually search recursively for values, in nested objects and lists. This behaviour is weird, and has been removed. No recursive-searching now happens in configs.
~ Configs and webpanel now have better reliance on helper methods in GuildNetwork and IOHandler classes
~ Improve reliance on enabling and disabling plugins in GuildNetwork class, used in the enable disable and reload commands
+ Improve whitelist and blacklist helper methods to make them more intuitive. ListenerWrapper.java now has two methods that will tell you which guild has which plugins able to run, and which guilds a plugin can run in, based off the whitelisting rules.
+ Add convenience method that resolves an IMentionable Object from JDA when supplied with an ID (saves typing out code trying to figure out what an ID from a config file represents)
+ Improve AMGN.runCommand logging to show command execution status, and add similar logging for generic command invocations
+ AMGN.runCommand will now respect network whitelist/blacklist rules
+ Improve logging output and automatically store AMGN/plugin error output to `errors.log`
~ Improve the way default commands are understood by the network- this now allows for more consistent behaviour, and DEFAULT COMMANDS CAN BE USED IN AMGN.runCommand()!!!
~ Cleaned up webpanel authentication- sessions may now persist across network restarts, and revoking the permission AMGN.webpanel.access will now properly revoke the user's access to the webpanel
+ Fixed webpanel bugs and performance, added new webpanel buttons for more customisation and gave cosmetic upgrade to webpanel (fields can now be added or removed from config objects, the data type of a list can be chosen, objects and lists are now more clearly represented, reset config button was added, in short anything can now be added or removed from a config on the webpanel)
~ AMGN.runCommand no longer access plugin listeners concurrently, breaking things, and is now more robust
- no more references to old discord tags- all been replaced with new discord usernames
+ add more caching options to network.yml
+ internal network operations will no longer expect users to be cached
~ directory paths referenced within AMGN (i.e. found in the GuildNetwork class) are now absolute, and AMGN can now be run without needing to be in the base AMGN directory.
+ Guilds that the bot joins will now *write* the newly generated guild settings to network.yml immediately. This behaviour has also been updated for guilds joined during runtime, not just in downtime.
~ listpermissions plugin now handles role IDs, this was previously broken
~ commands now support guild-specific prefixes in the usage parameter, by the use of a ${PREFIX} placeholder value
+ Inbuilt "listing" commands e.g. help, listpermissions, listgroups, will now display results alphabetically, and with a less invasive scroll-style menu
+ Inbuilt support for scroll-style menu templates is now included
+ Menus can now have automatic timeouts after which they will cease to function. Actions can be scheduled to occur whenever menus are destroyed or softdestroyed
~ Fixed/improved permission checks and contexts
~ Fixed bug in the webpanel where updating lists will update the config files properly, but display incorrect changes on the GUI. 

## beta-1.4.2
~ Changed the way that config getValue methods work. Previously the only requirement to return a value before deferring down the config hierarchy (local -> global -> default) was for the config file to exist. This caused null values to be returned when they shouldn't have been. Now, if the value is null, this will not be returned and we continue down the hierarchy.
~ Fixed the methods Config.getDefaultConfig and Config.getDefaultValue

## beta-1.4.1
~ Fixed viewmetainfo command which didn't work

## beta-1.4
+ Added permissions section to the webpanel
~ Patched YAML vulnerability by updating snakeyaml dependency version
+ Added permissions to whitelist and blacklist PUT API endpoints

## beta-1.3
Upgraded permission system. Previously any command could have "all", "staff" "admin" or "operator" permissions, and no permission configuration for non-command actions. Now, custom permissions are specified and configured by the plugin developer, allowing permissions for certain commands to be handed out freely

~ Changed command argument `permission` to `permissions`, which now takes a list of string permissions in a recommended format of `pluginname.commands.commandname` or `pluginname.commands.*`

+ Added default AMGN permissions:
    AMGN.operator: all permissions allowed everywhere
    AMGN.commands.*: access to all AMGN default commands
    AMGN.commands.controlplugins.*: access to ^showplugins, ^showallplugins, ^enable, ^disable, ^reload and ^reloadall

    AMGN.commands.help
    AMGN.commands.updatemetainfo
    AMGN.commands.viewmetainfo
    AMGN.commands.showplugins
    AMGN.commands.enable
    AMGN.commands.disable
    AMGN.commands.reload
    AMGN.commands.reloadall
    AMGN.commands.closenetwork
    AMGN.commands.whitelist
    AMGN.commands.blacklist
    AMGN.commands.showallplugins
    AMGN.commands.permission
    AMGN.commands.listpermissions
    (For all default AMGN commands)

    AMGN.webpanel.access: gives access to the webpanel

- dropped support for operators in `network.yml`. This variable is no longer used. Instead, give the user/role the AMGN.operator permission

+ Added support for `permissions` variable in `plugin.yml`, to list all permissions a plugin uses

+ small improvements to IO helper methods

+ added permissions.yml and several new permissions/groups commands to help with managing permissions on servers

## beta-1.2.1
~ Upgrade to JDA 5.0.0-beta.6

## beta-1.2
+ Add channel option to the AMGN.runCommand method, and integrate user permissions to this method. This method will not run a command for a user who doesn't have permission to run the command.
~ Made modlogs message when AMGN is closed down go to every modlogs channel
+ Add modlogs message to every guild when network is started

## beta-1.1.1
Fix whitelist/blacklist breaking if not used

## beta-1.1
+ Add plugin whitelisting and blacklisting feature
+ Add wrapper-classes for Listeners and Commands
+ change showplugins command to only show plugins running in the given guild
+ add showallplugins command to replace old showplugins functionality
+ add whitelist and blacklist commands
+ commands can now be called programmatically, acting as a particular user or as the network
~ Fix default webpanel values being in the wrong format to be parsed by AMGN
+ webpanel values did not always go through previously- this has been fixed
+ add confirmation/error messages to webpanel when saving values

## beta-1.0.1
~ Fix issue where webpanel settings are not saved to network.yml when network settings are updated
~ Fix bug where saving network settings via the webpanel would not remember which guild had which settings
~ Fix bug with webpanel network settings, where IDs are stated and saved incorrectly


## beta-1.0
+ Add webpanel!!
+ Generate network.yml automatically if one is not found
+ Add program exits if network settings cannot be read
+ Add a new default embed colour, and add custom colours for each guild as an option
+ Add support for environment variable usage for the `token`, `clientid`, `clientsecret` and `redirecturi` network.yml variables
+ Operators can now include roles as well as users
+ Config files can now be updated programmatically
+ Add the ability to retrieve default values from configs
+ Add global config settings and guild-specific config settings
+ one plugin crashing is no longer fatal on startup

## alpha-1.4.1
~ Actually made optional plugin.yml parameters optional, in line with the documentation

## alpha-1.4
+ Added Menu native support and implementation

## alpha-1.3.1
### CHANGED
~ Fixed intrinsic, guild-specific commands being able to be called/listened for in private channels

## alpha-1.3.0
### ADDED
+ Added ONLINE_STATUS to the bot's enabled cache flags
+ Added ACTIVITY to the bot's enabled cache flags

## alpha-1.2.1
### ADDED
+ Added verbose logging for config resource generation

### CHANGED
~ Fixed generating config resources, previously not working
~ Fixed retrieving config values, previously bugged

## alpha-1.2
### ADDED
+ Added native config support for generating default config files
+ Added native config support for getting config data

### CHANGED
~ Fixed bug where enabling or disabling nonexistent plugins would crash AMGN

## alpha-1.1
### ADDED
+ Added better support for missing guild data

### CHANGED
~ Moved to yaml from json formats
