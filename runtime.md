# Running an AMGN bot

This project is designed to make running your own personal discord bot very easy. If all you want to do is run a bot without worrying about coding new features, this is the guide for you. There will be 3rd-party developers for AMGN who will develop plugins for AMGN, which will essentially be features for your bot. In order to use AMGN, you should have this repository, and any AMGN plugin jars you wish to use on your bot.

## Preparing the environment
1. Build AMGN:
 - clone this repo, run `git clone`
 - run `mvn clean package` to build out the AMGN jar. You will find this jar in the `target` directory as `AMGN-1.0-jar-with-dependencies.jar`. You can then move it wherever you want to have your bot. I recommend having a dedicated directory for it

2. Add AMGN as a plugin dependency
 - run the script `dependency.sh` that you will see in this repo

## Preparing the bot
Before running the network, the network needs to know a bit about the users and servers it will work with. The network
accesses this data by reading `./network.yml`. Without this file, the network will not run. This file contains 
crucial information for your bot, and is written in YAML. This file will be auto-generated for you when you first run the jar, and you will then need to change some values in the file. Here are all the possible values you can include in your `network.yml`:

| Variable | Required | Type |Default | Description |
| -------- | -------- | ---- | ------ | ----------- |
| token | yes | `string` | N/A | your bot's token |
| use_webpanel | no | `boolean` | `true` | If this is set to `false`, the webpanel will not be activated for your installation of AMGN. If this is `true`, the webpanel will be used. |
| clientid | yes if use_webpanel=true | `string` | | The client ID for your application if you wish to use the webpanel |
| clientsecret | yes if use_webpanel=true | `string` | | The client secret for your application if you wish to use the webpanel |
| redirecturi | yes if use_webpanel=true | `string` | | The redirect URI for your application if you wish to use the webpanel |
| port | no | `int` | `8080` | The desired port to host your webpanel on |
| load_order | no | list of strings | empty list (no order) | A list of plugin names in the order you need them to be loaded in |
| cacheflags | no | list of strings | GuildNetwork.DEFAULT_CACHEFLAGS `["online_status", "activity"]` | Which elements of users will be cached when users are cached (this can conflict with gateway intents) |
| membercachepolicy | no | `string` | GuildNetwork.DEFAULT_MEMBERCACHEPOLICY "default" | The policy the bot will use to cache users |
| cache_limit | no | `int` | cache limit is not added if this is missing. | How many users should be cached on top of the cache-policy, according to a "least-recently-used" system. The last x users to be used in the API are cached. |
| guild_data | no | list of objects (fields follow ) | N/A | metadata for a guild on the network |

> To find your bot's token, you will need to go to [the discord developer portal](https://discord.com/developers/applications) and create a new application if you have not done so already. Create a bot if you also haven't done so, and under the bot tab you should see an option for resetting or copying the token. This is how you can grab your token.

Each `guild_data` object of the array represents a single guild and the metadata associated with it. Here are the fields for each object in a `guild_data` array entry:

| Field | Required | Type | Default | Description |
| ----- | -------- | ---- | ------- | ----------- |
| guild_id | no | `long` | `GuildNetwork.DEFAULT_ID` (-1) | id of a guild you wish to specify metadata for |
| prefix | no | `string` | `^` | prefix for commands in the aforementioned guild |
| modlogs | no | `string` | `GuildNetwork.DEFAULT_ID` (-1) | mod-logs channel that is used to send logs to about the bot |
| accept_col | no | string of hex | `GuildNetwork.GREEN_EMBED_COLOUR` (65280) | colour that will be used in this guild for "accepted"-coloured embeds e.g. a successful log |
| decline_col | no | string of hex | `GuildNetwork.RED_EMBED_COLOUR` (16073282) | colour that will be used in this guild for "declined"-coloured embeds e.g. an UNsuccessful log |
| unique_col | no | string of hex | `GuildNetwork.PURPLE_EMBED_COLOUR` (11023006) | colour that will be used in this guild for unique-event embeds e.g. something unexpected or special |

`network.yml` must be in the same directory as the network jar and have the correct name (network.yml). The settings are picked up automatically when you run the jar.

### Environment Variables

The `network.yml` variables `token`, `clientid`, `clientsecret` and `redirecturi` can also not be set, in which case AMGN will attempt to use environment variables for these values rather than setting the values in `network.yml`. The corresponding environment variables if you choose this method are as follows:

| Network.yml | Environment Variable |
| ----------- | -------------------- |
| token | AMGN_TOKEN |
| clientid | AMGN_CLIENTID |
| clientsecret | AMGN_CLIENTSECRET |
| redirecturi | AMGN_REDIRECT |

i.e. if in `network.yml`, you write the following:
```yaml
token: "env"
```
then for example in linux, you need to have run `export AMGN_TOKEN=thisismytoken`. AMGN will then automatically pull your variable from the environment rather than `network.yml`.

## Other generated files
AMGN will also generate 2 other files when run: `permissions.yml` and `whitelist.yml`. These can be left alone if you wish and changed with commands from your discord client, or you can edit them (see below). You can also change `guild_data` by using commands from your discord client.

## Setting up your running bot
If you have added a token to `network.yml`, your bot is now ready to run. You may wish to add plugins to your bot, as currently your bot has extremely limited functionality. Plugins add actual features to your bot, for example:
 - moderation commands
 - music playing
 - giveaways
 - twitch/youtube notifications
 - basically anything you've seen bots be able to do, and more if you wish

Adding a plugin is very simple. If you have the plugin jar from an AMGN plugin developer, simply place it into the `plugins` directory that AMGN automatically generates, and either restart your bot or reload the plugin. Instructions for how to do this are below.

### Customising plugins
Plugins may be able to be customised too, to further make your bot and the way it operates unique- for example a specific message you set to be sent when something happens. To customise plugins, plugin developers will add config files, that will be found in the `plugins` directory in a directory with the same name as the plugin. There will be YAML config files here to be edited. The YAML files in the base directory will affect the GLOBAL plugin settings, and you will also see directories with guild IDs. In here are the same config files, but these will affect guild-specific plugin settings- so guilds can have individual settings for a plugin. A plugin developer may even provide commands to make editing the configs more user-friendly. More information on how to edit plugins should come from the plugin-developers themselves.

## Default AMGN behaviour
AMGN comes with some pre-packaged commands and features to make managing your bot easier:

| Discord Command | Description |
| --------------- | ----------- |
| help (optional command/plugin name) | Displays all commands a user can run, or if specified, gives more information about a given command or plugin |
| updatemetainfo {prefix/acceptcol/declinecol/uniquecol} {new value}| Update the `guild_data` guild entry, changing the guild's metadata. You can change the command prefix used in this guild, the mod-logs channel, or default colours associated with embeds sent in this guild. |
| viewmetainfo | See all the metadata associated with the guild you are currently in |
| showplugins | show the plugins that are active and able to run in the guild you are in, on the bot |
| enable {plugin name} | enable a plugin that is not running, but is present in the `plugins` directory |
| disable {plugin name} | disable a plugin that is running on the bot |
| reload {plugin name} | reload a plugin that is currently running on the bot |
| reloadall | reload every plugin that is currently running on the bot |
| closenetwork | shut down your bot, ceases all activity and brings the bot and the program offline |
| whitelist {add/remove} {plugin name} | Add or remove a plugin to the whitelist for a particular guild- whitelist a plugin's activity to a specific guild or set of guilds |
| blacklist {add/remove} {plugin name}| Add or remove a plugin to the blacklist for a particular guild- blacklist a plugin's activity from a specific guild or set of guilds |
| showallplugins | show all plugins running on the bot, irrespective or whitelisting and blacklisting rules- show all plugins running everywhere |
| permission {add/remove} {user/role id/group} {permission} | add or remove a permission to or from a user, discord role, or pre-defined group of users |
| listpermissions {user id/role id/plugin (optional)} | Show all permissions that have been granted to a certain user, discord role, or pre-defined group of users |
| groups {create/destroy/add/remove/addperm/removeperm/list} | create or modify a group of discord users- put a group of discord users under one single group-name so it is easier to configure permissions for them |

For more information about any of these commands, or plugin-commands, remember the help command can provide more information.

## Configuring plugin whitelists/blacklists
It is simple enough to add a plugin to your bot with AMGN, but you may not want this plugin to be able to be used in every guild that the bot sits in. AMGN allows this to be specified with plugin whitelists/blacklists. There are 2 ways to do that

### Manually edit the whitelist.yml file
The `whitelist.yml` file that sits next to your AMGN jar can be edited to manage the plugin whitelist/blacklist. This will take the following format:
```yaml
whitelist:
   pluginname: [634667687233978388, 12321321321123]

blacklist:
   otherpluginname: [8908932478324, 82391038219]
```

This would whitelist a plugin called `pluginname` to be used in the two guilds specified with their IDs. It would also blacklist `otherpluginname` from being used in the two guilds listed there.

It is possible to whitelist and blacklist a plugin in a single guild. In this case, the whitelisting rules are that the whitelist is applied first, followed by the blacklist. For example, if plugin X is in the whitelist and the blacklist for a guild Y, then first the whitelist is applied. So at first the plugin is whitelisted for this guild. Then the blacklist is applied, so plugin is actually blacklisted from Y. So the overall effect of doing this is effectively blacklisting it in this guild.

If the whitelist/blacklist settings are empty, then every plugin will be used everywhere. If anything is blacklisted or whitelisted, then all plugin access is restricted to what is specified in the blacklist and whitelist.

### Use discord commands
AMGN comes pre-packaged with `whitelist` and `blacklist` commands, see above or use the help command for more help with usage. These can be used to configure the plugin whitelist and blacklist

## Configuring user permissions
When running your bot, you will also probably not want certain users to be able to use certain commands. AMGN has functionality for managing who is allowed to do what with your bot. There are 2 ways to do this:

### Manually edit the permissions.yml file
The `permissions.yml` file that sits next to your AMGN jar can be edited to manage user permissions. This will take the following format, as an example:
```yaml
'475859944101380106':
  - AMGN.operator

'936234314545381406':
  - AMGN.commands.*
  - AMGN.webpanel.access

groups: {
  mygroup:
    permissions: [AMGN.webpanel.access]
    members:
    - '475859944101380106'
}
```

To set permissions for a specific user or discord role, simply create a list with the name as the ID of the user/role. The list is then a list of the permission names.
Groups are an easy way to refer to a scattered group of users and roles. For example here we have a group called "mygroup", with one permission set in it, and one user/role ID set in the group. Though you can add as many permissions or roles/users to the group as you want. It's just an easy way to group users and permissions.

### Use discord commands
AMGN comes pre-packaged with `permission`, `listpermissions` and `groups` commands, see above or use the help command for more help with usage. These can be used to give a permission to a user role or group, list permissions for a user role or group, and display or edit information about the groups you have set up.

## Permissions
Permissions are essentially pieces of text that AMGN knows to associate with certain access to parts of the bot. Plugins come with their own sets of permissions that the plugin developer should document, but AMGN comes configured with some default permissions you can set up for users:

| Permission | Description |
| ---------- | ----------- |
| AMGN.operator | all permissions allowed everywhere |
| AMGN.commands.* | access to all AMGN default commands |
| AMGN.commands.controlplugins.* | access to the showplugins, showallplugins, enable, disable, reload and reloadall commands |
| AMGN.commands.help | access to the help command |
| AMGN.commands.updatemetainfo | access to the updatemetainfo command |
| AMGN.commands.viewmetainfo | access to the viewmetainfo command |
| AMGN.commands.showplugins | access to the showplugins command |
| AMGN.commands.enable | access to the enable command |
| AMGN.commands.disable | access to the disable command |
| AMGN.commands.reload | access to the reload command |
| AMGN.commands.reloadall | access to the reloadall command |
| AMGN.commands.closenetwork | access to the closenetwork command |
| AMGN.commands.whitelist | access to the whitelist command |
| AMGN.commands.blacklist | access to the blacklist command |
| AMGN.commands.showallplugins | access to the showallplugins command |
| AMGN.commands.permission | access to the permission command |
| AMGN.commands.listpermissions | access to the listpermissions command |
| AMGN.webpanel.access | gives access to the webpanel |

These are the permissions you need to use in the commands e.g. `permission add 475859944101380106 AMGN.operator`, and if you manually edit `permissions.yml`.

## The Webpanel

AMGN also comes pre-packaged with a webpanel to easily configure all of these aforementioned settings, and plugin settings from 3rd party plugins. This is optional, and is specified in `network.yml`, as are the variables needed to make the webpanel functional.
This webpanel will be hosted on your bot's host's IP address, and by default on port 8080 (though this can be changed in `network.yml`). The webpanel lives under the path /webpanel. So if hosting this locally, you can visit your bot's webpanel at http://localhost:8080/webpanel.
This webpanel will let you changed all of the previously mentioned settings, `network.yml`, `permissions.yml` and `whitelist.yml`. Except for settings which you would need to restart the whole bot for, i.e. the webpanel settings.
To use this, a user needs the `AMGN.webpanel.access` permission. They then log in- the login is handled by discord OAuth.

For the webpanel to work, you need to provide `clientid`, `clientsecret` and `redirecturl` in your `network.yml`. These can be found in [the discord developer portal](https://discord.com/developers/applications) by finding your bot and clicking on "OAuth2". You will have options to copy/reset your client secret and id from here. You will then need to set a redirect here to point at the address you are hosting your bot on, followed by `/webpanel/redirect`. This will allow users to log in to your webpanel. This value needs to be set to the same value in `network.yml`.

This is probably the easiest way to configure anything about your bot or a plugin. The global network settings and all installed plugins will appear on the left panel, and you can specify global settings or a specific guild at the top using the guild-selector. When you click on a plugin you will see all the options to configure the plugin, including whitelisting/blacklisting it in the guild, or switching to global or local settings.

### Webpanel permissions
Any user with the `AMGN.operator` permission will have unrestricted access to the whole webpanel. Users with the `AMGN.webpanel.access` permission will not be able to edit the bot settings themselves, and can only edit plugin settings for guilds that they are actually in.