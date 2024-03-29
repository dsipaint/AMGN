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
