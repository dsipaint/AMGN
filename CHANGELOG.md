## beta-1.0
+ Add webpanel!!
+ Generate network.yml automatically if one is not found
+ Add a program exits if network settings cannot be read
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
