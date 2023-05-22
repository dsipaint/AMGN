package com.github.dsipaint.AMGN.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.yaml.snakeyaml.Yaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.Guild;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.io.IOHandler;
import com.github.dsipaint.AMGN.io.Permissions;

@Controller
public class WebpanelController
{
    static final String TOKEN_URL="https://discord.com/api/oauth2/token",
        API_URL = "https://discord.com/api/v10";

    static List<String> TOKEN_CACHE = new ArrayList<String>(); //yes, this is a bad idea: change this in the future

    @RequestMapping("/webpanel")
    public String getCustomWebpanel(Model model)
    {
        model.addAttribute("botname", AMGN.bot.getSelfUser().getName());
        model.addAttribute("botpfp", AMGN.bot.getSelfUser().getAvatarUrl());
        model.addAttribute("clientid", GuildNetwork.clientid);
        model.addAttribute("redirecturi", GuildNetwork.redirecturi);
        return "homepage";
    }

    @RequestMapping("/webpanel/redirect")
    public String handleloginredirect(Model model, HttpServletResponse response, @RequestParam(name="code") String code)
    {
        //add login cookies
        RestTemplate template = new RestTemplate();
        StringBuilder authparams = new StringBuilder()
            .append("client_id=" + GuildNetwork.clientid)
            .append("&client_secret=" + GuildNetwork.clientsecret)
            .append("&code=" + code)
            .append("&grant_type=authorization_code")
            .append("&redirect_uri=" + GuildNetwork.redirecturi);

        HttpHeaders auth_headers = new HttpHeaders();
        auth_headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> authentity = new HttpEntity<String>(authparams.toString(), auth_headers);
        
        ResponseEntity<JsonNode> tokenresp = template.postForEntity(TOKEN_URL, authentity, JsonNode.class);
        String usertoken = tokenresp.getBody().get("access_token").asText();

        //for username and pfp try GETting /users/me
        HttpHeaders userinfoheaders = new HttpHeaders();
        userinfoheaders.set("Authorization", "Bearer " + usertoken);
        ResponseEntity<JsonNode> userinfo = template.exchange(API_URL + "/users/@me", HttpMethod.GET, new HttpEntity<>("", userinfoheaders), JsonNode.class);

        //if this is a valid authorised user and we don't know this user already:
        if(canViewId(userinfo.getBody().get("id").asLong()) &&
            !TOKEN_CACHE.contains(usertoken))
            TOKEN_CACHE.add(usertoken);


        Cookie token = new Cookie("discord_token", usertoken);
        token.setPath("/webpanel");
        response.addCookie(token);

        //return a redirect page
        return "redirect";
    }

    //returns a list of guilds for this bot, for an authenticated user
    @GetMapping(value="/webpanel/api/guilds", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode getBotGuilds(HttpServletRequest request, HttpServletResponse response)
    {
        if(request.getCookies() == null)
        {
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        if(isAuthenticatedRequest(request))
        {
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode guild_data = mapper.createArrayNode();

            AMGN.bot.getGuilds().forEach(guild -> {
                if(GuildNetwork.isOperator(AMGN.bot.getUserById(resolveIdFromToken(getTokenFromRequest(request)))) || guild.getMemberById(Long.parseLong(resolveIdFromToken(getTokenFromRequest(request)))) != null)
                {
                    ObjectNode objectnode = mapper.createObjectNode();
                    objectnode.put("id", guild.getId());
                    objectnode.put("name", guild.getName());
                    objectnode.put("picture", guild.getIconUrl());
                    guild_data.add(objectnode);   
                }
            });

            response.setStatus(201);
            return guild_data;
        }

        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    //returns a list of plugins that the bot currently has enabled
    @GetMapping(value="/webpanel/api/plugins", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode getBotPlugins(HttpServletRequest request, HttpServletResponse response)
    {
        if(request.getCookies() == null)
        {
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        if(isAuthenticatedRequest(request))
        {
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode plugin_data = mapper.createArrayNode();
            AMGN.plugin_listeners.keySet().forEach(plugin ->
            {
                plugin_data.add(plugin.getName());
            });

            response.setStatus(201);
            return plugin_data;
        }

        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    //returns a list of plugins that the bot currently has enabled
    @GetMapping(value="/webpanel/api/plugininfo", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode getPluginInfo(@RequestParam(name="name") String name, @RequestParam(name="guild", defaultValue="global") String guild, HttpServletRequest request, HttpServletResponse response)
    {
        if(request.getCookies() == null)
        {
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        if(name == null)
        {
            response.setStatus(401);
            return new ObjectMapper().createObjectNode().put("error", "no plugin specified");         
        }

        if(isAuthenticatedRequest(request))
        {
            ObjectMapper mapper = new ObjectMapper();
            for(Plugin plugin : AMGN.plugin_listeners.keySet())
            {
                if(plugin.getName().equalsIgnoreCase(name))
                {
                    ObjectNode obj = mapper.createObjectNode();
                    obj.put("name", plugin.getName());
                    obj.put("author", plugin.getAuthor());
                    obj.put("description", plugin.getDescription());
                    obj.put("picture", plugin.getImageUrl());
                    obj.put("version", plugin.getVersion());
                    try
                    {
                        //to send plugin configs, we send an array of all config files like this
                        //[{file: "config", data: {...whatever}}, {file: "names", data: {...more whatever}}, {file: "winners", data: {...even more whatever}}]

                        //iterate through all files in the config directory that are yml files
                        File configdir;
                        if(guild.equalsIgnoreCase("global")) 
                            configdir = new File(plugin.getGlobalConfigPath());
                        else
                            configdir = new File(plugin.getGuildConfigPath(AMGN.bot.getGuildById(guild)));

                        File[] configfiles = configdir.listFiles((file, filename) -> {
                            return filename.endsWith(".yml");
                        });

                        if(configfiles != null)
                        {
                            //add all of these files and their contents to an array in the above format
                            ArrayNode confignode = mapper.createArrayNode();

                            for(File f : configfiles)
                            {
                                ObjectNode configobj = mapper.createObjectNode();
                                JsonNode configdata = mapper.reader().readTree(mapper.writeValueAsString(plugin.getConfig().getConfig(f.getName())));
                                configobj.put("file", f.getName().replace(".yml", ""));
                                configobj.set("data", configdata);
    
                                confignode.add(configobj);
                            }
                            
                            //this array is then our config data
                            obj.set("config", confignode);
                        }
                    }
                    catch(IOException e)
                    {
                        response.setStatus(500);
                        return new ObjectMapper().createObjectNode().put("error", "problem reading plugin info");
                    }

                    response.setStatus(201);
                    return obj;
                }
            }

            response.setStatus(404);
            return new ObjectMapper().createObjectNode().put("error", "plugin not found");  
        }

        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    //returns the network info for the network
    //for now, any authorised user may use this but I may change it to just operators
    @GetMapping(value="/webpanel/api/networkinfo", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode getNetworkInfo(HttpServletRequest request, HttpServletResponse response)
    {
        if(request.getCookies() == null)
        {
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        if(isAuthenticatedRequest(request))
        {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode network_data = mapper.createObjectNode();
            boolean isoperator = GuildNetwork.isOperator(AMGN.bot.getUserById(resolveIdFromToken(getTokenFromRequest(request))));

            ArrayNode guilds = mapper.createArrayNode();

            AMGN.bot.getGuilds().forEach(guild -> {
                //if they are an operator or if they are in the guild, then they can have this guild
                if(isoperator || guild.getMemberById(Long.parseLong(resolveIdFromToken(getTokenFromRequest(request)))) != null)
                {
                    ObjectNode obj = mapper.createObjectNode();
                    obj.put("guild_id", guild.getId());
                    obj.put("modlogs", Long.toString(GuildNetwork.getModlogs(guild.getIdLong())));
                    obj.put("prefix", GuildNetwork.getPrefix(guild.getIdLong()));
                    obj.put("accept_col", Guild.formatHexString(GuildNetwork.getAccept_col(guild.getIdLong())));
                    obj.put("decline_col", Guild.formatHexString(GuildNetwork.getDecline_col(guild.getIdLong())));
                    obj.put("unique_col", Guild.formatHexString(GuildNetwork.getUnique_col(guild.getIdLong())));
                    guilds.add(obj);
                }
            });
            network_data.set("guild_data", guilds);

            response.setStatus(201);
            return network_data;
        }

        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    //for now, any authorised user may use this but I may change it to just operators
    //allows client to save network data
    @PutMapping(value="/webpanel/api/networkinfo", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode putNetworkInfo(@RequestBody JsonNode body, HttpServletRequest request, HttpServletResponse response)
    {
        if(request.getCookies() == null)
        {
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        if(isAuthenticatedRequest(request))
        {
            boolean isoperator = GuildNetwork.isOperator(AMGN.bot.getUserById(resolveIdFromToken(getTokenFromRequest(request))));

            ArrayNode guild_data = body.withArray("guild_data");
            Map<Long, Guild>  new_guild_data = new HashMap<Long, Guild>();
            guild_data.forEach(guildnode ->{
                //if they are an operator or if they are in the guild, then they can have this guild
                if(isoperator || AMGN.bot.getGuildById(guildnode.get("id").asLong())
                    .getMemberById(Long.parseLong(resolveIdFromToken(getTokenFromRequest(request)))) != null)
                {
                    new_guild_data.put(guildnode.get("guild_id").asLong(),
                        new Guild(guildnode.get("guild_id").asLong(),
                                guildnode.get("modlogs").asLong(),
                                guildnode.get("prefix").asText(),
                                Integer.parseInt(guildnode.get("accept_col").asText().replace("#", ""), 16),
                                Integer.parseInt(guildnode.get("decline_col").asText().replace("#", ""), 16),
                                Integer.parseInt(guildnode.get("unique_col").asText().replace("#", ""), 16)
                    ));
                }
            });
            GuildNetwork.guild_data = new_guild_data;

            try
            {
                IOHandler.writeNetworkData(GuildNetwork.guild_data, GuildNetwork.NETWORKINFO_PATH);
            }
            catch(IOException e)
            {
                response.setStatus(500);
                return new ObjectMapper().createObjectNode().put("error", "problem writing network data");
            }

            response.setStatus(201);
            return new ObjectMapper().createObjectNode().put("success", "network data saved");
        }

        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    //for now, any authorised user may use this but I may change it to just operators
    //allows client to save plugin configs
    @PutMapping(value="/webpanel/api/plugininfo", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode putPluginConfigInfo(@RequestParam(name="name") String name, @RequestParam(defaultValue = "global") String guild, @RequestBody JsonNode body, HttpServletRequest request, HttpServletResponse response)
    {
        if(request.getCookies() == null)
        {
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        if(name == null)
        {
            response.setStatus(401);
            return new ObjectMapper().createObjectNode().put("error", "no plugin specified");         
        }

        if(isAuthenticatedRequest(request))
        {
            ObjectMapper mapper = new ObjectMapper();
            for(Plugin plugin : AMGN.plugin_listeners.keySet())
            {
                if(plugin.getName().equalsIgnoreCase(name))
                {
                    try
                    {
                        //we go through the returned data in the following format (as is given to the user originally in the get method)
                        //[{file: "config", data: {...whatever}}, {file: "names", data: {...more whatever}}, {file: "winners", data: {...even more whatever}}]
                        //then for each config file, we write the data
                        //TODO: add formal config-writing
                        Yaml yaml_out = new Yaml(IOHandler.dumperopts);
                        for(JsonNode config : body)
                        {
                            Map<String, Object> result = mapper.convertValue(config.get("data"), new TypeReference<Map<String, Object>>(){});
                            yaml_out.dump(result, new FileWriter(new File((guild.equalsIgnoreCase("global") ? plugin.getGlobalConfigPath() : plugin.getGuildConfigPath(AMGN.bot.getGuildById(guild))) + "/" + config.get("file").asText() + ".yml")));
                        }
                    }
                    catch(IOException e)
                    {
                        response.setStatus(500);
                        return new ObjectMapper().createObjectNode().put("error", "problem writing data");
                    }

                    //reload plugin
                    plugin.onDisable(); //disable plugin
                    AMGN.plugin_listeners.get(plugin).forEach(AMGN.bot::removeEventListener); //remove listeners
                    AMGN.menucache.forEach(menu ->
                    {
                        if(menu.getPlugin().equals(plugin))
                            menu.softDestroy();
                    });
                    AMGN.menucache.removeIf(menu -> {return menu.getPlugin().equals(plugin);});//remove menus
                    plugin.onEnable(); //re-enable plugin

                    response.setStatus(201);
                    return new ObjectMapper().createObjectNode().put("success", "plugin config saved");
                }
            }

            response.setStatus(404);
            return new ObjectMapper().createObjectNode().put("error", "plugin not found");
        }

        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    //use this API endpoint to request a local config for a plugin that doesn't have one
    //needs a plugin name and a guild
    @PostMapping(value="/webpanel/api/plugininfo", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode requestLocalConfig(@RequestParam(name="name") String name, @RequestParam(name="guild") String guild, HttpServletRequest request, HttpServletResponse response)
    {
        if(request.getCookies() == null)
        {
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        if(isAuthenticatedRequest(request))
        {
            //check to see if there is a config for this plugin
            for(Plugin plugin : AMGN.plugin_listeners.keySet())
            {
                if(plugin.getName().equalsIgnoreCase(name))
                {
                    //make a local one if it's possible
                    //just copy from global settings (we could fish them out of the jar, may come back to do this, but for now this is easier)
                    File globalconfigdir = new File(plugin.getGlobalConfigPath());
                    File[] configfiles = globalconfigdir.listFiles((file, filename) -> {
                        return filename.endsWith(".yml");
                    });

                    ObjectMapper mapper = new ObjectMapper();
                    ArrayNode confignode = mapper.createArrayNode();
                    if(configfiles != null)
                    {
                        for(File file : configfiles)
                            plugin.getConfig().generateLocalResource(file.getName(), AMGN.bot.getGuildById(guild));

                        //return this config
                        try
                        {
                            for(File f : configfiles)
                            {
                                ObjectNode configobj = mapper.createObjectNode();
                                JsonNode configdata = mapper.reader().readTree(mapper.writeValueAsString(plugin.getConfig().getConfig(f.getName())));
                                configobj.put("file", f.getName().replace(".yml", ""));
                                configobj.set("data", configdata);
    
                                confignode.add(configobj);
                            }
                        }
                        catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    return confignode;
                }
            }

            response.setStatus(404);
            return new ObjectMapper().createObjectNode().put("error", "plugin not found");
        }

        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

        //use this API endpoint to request a local config for a plugin that doesn't have one
    //needs a plugin name and a guild
    @DeleteMapping(value="/webpanel/api/plugininfo", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode deleteLocalConfig(@RequestParam(name="name") String name, @RequestParam(name="guild") String guild, HttpServletRequest request, HttpServletResponse response)
    {
        if(request.getCookies() == null)
        {
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        if(isAuthenticatedRequest(request))
        {
            //check to see if there is a config for this plugin
            for(Plugin plugin : AMGN.plugin_listeners.keySet())
            {
                if(plugin.getName().equalsIgnoreCase(name))
                {
                    //delete any local config that may exist
                    File configdir = new File(plugin.getGuildConfigPath(AMGN.bot.getGuildById(guild)));
                    if(configdir.exists())
                    {
                        try
                        {
                            FileUtils.deleteDirectory(configdir);
                        }
                        catch(IOException e)
                        {
                            e.printStackTrace();
                        }
                        
                        response.setStatus(200);
                        return new ObjectMapper().createObjectNode().put("success", "local config deleted");
                    }
                    else
                    {
                        response.setStatus(201);
                        return new ObjectMapper().createObjectNode().put("success", "local config already deleted");
                    }
                }
            }

            response.setStatus(404);
            return new ObjectMapper().createObjectNode().put("error", "plugin not found");
        }

        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    @PutMapping(value = "/webpanel/api/whitelist", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode addGuildToWhitelist(@RequestParam String guild, @RequestParam String plugin, HttpServletRequest request, HttpServletResponse response)
    {
        if(request.getCookies() == null)
        {
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        if(isAuthenticatedRequest(request))
        {
            //also need permission AMGN.commands.whitelist
            if(Permissions.hasPermission(AMGN.bot.getUserById(resolveIdFromToken(getTokenFromRequest(request))), null, "AMGN.commands.whitelist"))
            {
                List<Long> newlist = GuildNetwork.whitelist.getOrDefault(plugin, new ArrayList<Long>());
                if(!newlist.contains(Long.parseLong(guild)))
                    newlist.add(Long.parseLong(guild));
    
                GuildNetwork.whitelist.put(plugin, newlist);
                try
                {
                    IOHandler.writeWhitelistBlacklist();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
    
                response.setStatus(200);
                return new ObjectMapper().createObjectNode().put("success", "whitelist updated successfully");
            }
            else
            {
               
        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "user is missing permission AMGN.commands.whitelist"); 
            }
        }

        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    @DeleteMapping(value = "/webpanel/api/whitelist", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode removeGuildFromWhitelist(@RequestParam String guild, @RequestParam String plugin, HttpServletRequest request, HttpServletResponse response)
    {
        if(request.getCookies() == null)
        {
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        if(isAuthenticatedRequest(request))
        {
            //also need permission AMGN.commands.whitelist
            if(Permissions.hasPermission(AMGN.bot.getUserById(resolveIdFromToken(getTokenFromRequest(request))), null, "AMGN.commands.whitelist"))
            {
                List<Long> newlist = GuildNetwork.whitelist.getOrDefault(plugin, new ArrayList<Long>());
                if(newlist.contains(Long.parseLong(guild)))
                    newlist.remove(Long.parseLong(guild));
    
                GuildNetwork.whitelist.put(plugin, newlist);
                try
                {
                    IOHandler.writeWhitelistBlacklist();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
    
                response.setStatus(200);
                return new ObjectMapper().createObjectNode().put("success", "whitelist updated successfully");
            }
            else
            {
                response.setStatus(403);
                return new ObjectMapper().createObjectNode().put("error", "invalid token");
            }
        }
        
        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    @PutMapping(value = "/webpanel/api/blacklist", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode addGuildToBlacklist(@RequestParam String guild, @RequestParam String plugin, HttpServletRequest request, HttpServletResponse response)
    {
        if(request.getCookies() == null)
        {
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        if(isAuthenticatedRequest(request))
        {
            //also need permission AMGN.commands.blacklist
            if(Permissions.hasPermission(AMGN.bot.getUserById(resolveIdFromToken(getTokenFromRequest(request))), null, "AMGN.commands.blacklist"))
            {
                List<Long> newlist = GuildNetwork.blacklist.getOrDefault(plugin, new ArrayList<Long>());
                if(!newlist.contains(Long.parseLong(guild)))
                    newlist.add(Long.parseLong(guild));
    
                GuildNetwork.blacklist.put(plugin, newlist);
    
                try
                {
                    IOHandler.writeWhitelistBlacklist();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
    
                response.setStatus(200);
                return new ObjectMapper().createObjectNode().put("success", "blacklist updated successfully");
            }
            else
            {
                response.setStatus(403);
                return new ObjectMapper().createObjectNode().put("error", "user is missing permission AMGN.commands.blacklist");
            }
        }
        
        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    @DeleteMapping(value = "/webpanel/api/blacklist", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode removeGuildFromBlacklist(@RequestParam String guild, @RequestParam String plugin, HttpServletRequest request, HttpServletResponse response)
    {
        if(request.getCookies() == null)
        {
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        if(isAuthenticatedRequest(request))
        {
            //also need permission AMGN.commands.blacklist
            if(Permissions.hasPermission(AMGN.bot.getUserById(resolveIdFromToken(getTokenFromRequest(request))), null, "AMGN.commands.blacklist"))
            {
                List<Long> newlist = GuildNetwork.blacklist.getOrDefault(plugin, new ArrayList<Long>());
                if(newlist.contains(Long.parseLong(guild)))
                    newlist.remove(Long.parseLong(guild));
    
                GuildNetwork.blacklist.put(plugin, newlist);
                try
                {
                    IOHandler.writeWhitelistBlacklist();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
    
                response.setStatus(200);
                return new ObjectMapper().createObjectNode().put("success", "blacklist updated successfully");
            }
            else
            {
                response.setStatus(403);
                return new ObjectMapper().createObjectNode().put("error", "user is missing permission AMGN.commands.blacklist"); 
            }
        }
        
        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    @GetMapping(value = "/webpanel/api/whitelist", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode isInWhitelist(@RequestParam String guild, @RequestParam String plugin, HttpServletRequest request, HttpServletResponse response)
    {
        if(request.getCookies() == null)
        {
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        if(isAuthenticatedRequest(request))
        {
            if(GuildNetwork.whitelist.getOrDefault(plugin, new ArrayList<Long>()).contains(Long.parseLong(guild)))
                return new ObjectMapper().createObjectNode().put("result", true);

            return new ObjectMapper().createObjectNode().put("result", false);
        }

        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    @GetMapping(value = "/webpanel/api/blacklist", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode isInBlacklist(@RequestParam String guild, @RequestParam String plugin, HttpServletRequest request, HttpServletResponse response)
    {
        if(request.getCookies() == null)
        {
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        if(isAuthenticatedRequest(request))
        {
            if(GuildNetwork.blacklist.getOrDefault(plugin, new ArrayList<Long>()).contains(Long.parseLong(guild)))
                return new ObjectMapper().createObjectNode().put("result", true);

            return new ObjectMapper().createObjectNode().put("result", false);
        }

        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    //returns the permissions.yml file as a json object
    @GetMapping(value = "/webpanel/api/permissions", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode getPermissions(HttpServletRequest request, HttpServletResponse response)
    {
        if(request.getCookies() == null)
        {
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        if(isAuthenticatedRequest(request))
        {
            //must also have AMGN.commands.listpermissions to get this info
            if(Permissions.hasPermission(AMGN.bot.getUserById(resolveIdFromToken(getTokenFromRequest(request))), null, "AMGN.commands.listpermissions"))
            {
                try
                {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode permissions = mapper.reader().readTree(mapper.writeValueAsString(IOHandler.readAllYamlData(GuildNetwork.PERMISSIONS_PATH)));
                    return permissions;
                }
                catch(JsonProcessingException | FileNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                response.setStatus(403);
                return new ObjectMapper().createObjectNode().put("error", "this user is missing permission AMGN.commands.listpermissions");
            }
        }

        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    @PutMapping(value="/webpanel/api/permissions", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode putPermissionInfo(@RequestBody JsonNode body, HttpServletRequest request, HttpServletResponse response)
    {
        if(request.getCookies() == null)
        {
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        if(isAuthenticatedRequest(request))
        {
            //must also have AMGN.commands.groups and AMGN.commands.permission to do this
            if(Permissions.hasPermission(AMGN.bot.getUserById(resolveIdFromToken(getTokenFromRequest(request))), null, "AMGN.commands.groups")
                && Permissions.hasPermission(AMGN.bot.getUserById(resolveIdFromToken(getTokenFromRequest(request))), null, "AMGN.commands.permission"))
            {
                HashMap<String, Object> parsed_json = new HashMap<String, Object>();
                //parse the json and turn it into a hashmap
                body.fieldNames().forEachRemaining(field ->
                {
                    if(field.equals("groups"))
                    {
                        HashMap<String, Object> parse_groups = new HashMap<String, Object>();
                        body.get("groups").fieldNames().forEachRemaining(groupname ->
                        {
                            HashMap<String, List<String>> group = new HashMap<String, List<String>>();
                            List<String> permissions = new ArrayList<String>();
                            List<String> members = new ArrayList<String>();
    
                            body.get("groups").get(groupname).withArray("permissions")
                            .forEach(permission ->
                            {
                                permissions.add(permission.asText());
                            });
    
                            body.get("groups").get(groupname).withArray("members")
                            .forEach(member ->
                            {
                                members.add(member.asText());
                            });
    
                            group.put("permissions", permissions);
                            group.put("members", members);
    
                            parse_groups.put(groupname, group);
                        });
    
                        parsed_json.put("groups", parse_groups);
                    }
                    else
                    {
                        List<String> permissions = new ArrayList<String>();
                        body.withArray(field).forEach(permission ->
                        {
                            permissions.add(permission.asText());
                        });
                        parsed_json.put(field, permissions);
                    }
                });
    
                try
                {
                    IOHandler.writeYamlData(parsed_json, GuildNetwork.PERMISSIONS_PATH);
                }
                catch(IOException e)
                {
                    response.setStatus(500);
                    return new ObjectMapper().createObjectNode().put("error", "problem writing permission data");
                }
    
                response.setStatus(201);
                return new ObjectMapper().createObjectNode().put("success", "network data saved");
            }
            else
            {
                response.setStatus(403);
                return new ObjectMapper().createObjectNode().put("error", "user must have both permissions AMGN.commands.groups and AMGN.commands.permission");
            }

        }

        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    public static String resolveIdFromToken(String token)
    {
        RestTemplate template = new RestTemplate();
        HttpHeaders userinfoheaders = new HttpHeaders();
        userinfoheaders.set("Authorization", "Bearer " + token);
        ResponseEntity<JsonNode> userinfo = template.exchange(API_URL + "/users/@me", HttpMethod.GET, new HttpEntity<>("", userinfoheaders), JsonNode.class);
        return userinfo.getBody().get("id").asText();
    }

    //will look at a request and see if this is a user who is authorised to view the webpanel
    public static boolean isAuthenticatedRequest(HttpServletRequest request)
    {
        // check to see if we have a discord_token cookie, and if the value of it matches
        // a token we have already deemed as authenticated in TOKEN_CACHE
        for(Cookie c : request.getCookies())
        {
            if(c.getName().equals("discord_token")
                && TOKEN_CACHE.contains(c.getValue()))
                return true;
        }
        return false;
    }

    //checks a token and checks if this discord user has the permission to view the webpanel
    public static boolean canViewToken(String token)
    {
        return canViewId(Long.parseLong(resolveIdFromToken(token)));
    }

    //checks a user id and checks if this discord user has the permission to view the webpanel
    public static boolean canViewId(long id)
    {
        if(Permissions.hasPermission(AMGN.bot.getUserById(id), null, "AMGN.webpanel.access")
            || GuildNetwork.isOperator(AMGN.bot.getUserById(id)))
            return true;
        return false;
    }

    public static String getTokenFromRequest(HttpServletRequest request)
    {
        for(Cookie c : request.getCookies())
        {
            if(c.getName().equals("discord_token"))
                return c.getValue();
        }
        return null;
    }

    @Bean
    public FileTemplateResolver secondaryTemplateResolver()
    {
        FileTemplateResolver templateResolver = new FileTemplateResolver();
        templateResolver.setPrefix("./web/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
                
        return templateResolver;
    }

    @Configuration
    public class CustomContainer implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
    {
        public void customize(ConfigurableServletWebServerFactory factory)
        {
            Integer port = 8080;
            try
            {
                port = (Integer) IOHandler.readYamlData(GuildNetwork.NETWORKINFO_PATH, "port");
            }
            catch(FileNotFoundException e)
            {
                e.printStackTrace();
            }

            if(port == null)
                factory.setPort(8080);
            else
                factory.setPort(port);
        }
    }
}
