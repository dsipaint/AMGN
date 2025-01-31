package com.github.dsipaint.AMGN.web;

import java.io.File;
import java.io.FileNotFoundException;
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

    static HashMap<String, Long> TOKEN_CACHE = new HashMap<String, Long>();

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
        AMGN.logger.info("Authorization attempt on webpanel");

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

        Cookie token = new Cookie("discord_token", usertoken);
        token.setPath("/webpanel");
        response.addCookie(token);

        AMGN.logger.info("User " + userinfo.getBody().get("id").asText() + " authenticated via the webpanel");

        //return a redirect page
        return "redirect";
    }

    //returns a list of guilds for this bot, for an authenticated user
    @GetMapping(value="/webpanel/api/guilds", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode getBotGuilds(HttpServletRequest request, HttpServletResponse response)
    {
        AMGN.logger.info("GET request made to /webpanel/api/guilds");

        if(request.getCookies() == null)
        {
            AMGN.logger.error("GET request made to /webpanel/api/guilds failed (no cookies present)");
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        String userid = resolveIdFromToken(getTokenFromRequest(request));

        if(isAuthenticatedRequest(request))
        {
            AMGN.logger.info("Authenticated GET request made to /webpanel/api/guilds from user " + AMGN.bot.getUserById(userid));

            ObjectMapper mapper = new ObjectMapper();
            ArrayNode guild_data = mapper.createArrayNode();

            AMGN.bot.getGuilds().forEach(guild -> {
                if(GuildNetwork.isOperator(AMGN.bot.getUserById(userid)) || GuildNetwork.fetchMember(userid, guild) != null)
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

        AMGN.logger.error("Unauthenticated GET request made to /webpanel/api/guilds from user " + AMGN.bot.getUserById(userid));
        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    //returns a list of plugins that the bot currently has enabled
    @GetMapping(value="/webpanel/api/plugins", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode getBotPlugins(HttpServletRequest request, HttpServletResponse response)
    {
        AMGN.logger.info("GET request made to /webpanel/api/plugins");

        if(request.getCookies() == null)
        {
            AMGN.logger.error("GET request made to /webpanel/api/plugins failed (no cookies present)");
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        String userid = resolveIdFromToken(getTokenFromRequest(request));

        if(isAuthenticatedRequest(request))
        {
            AMGN.logger.info("Authenticated GET request made to /webpanel/api/plugins from user " + AMGN.bot.getUserById(userid));

            ObjectMapper mapper = new ObjectMapper();
            ArrayNode plugin_data = mapper.createArrayNode();
            AMGN.plugin_listeners.keySet().forEach(plugin ->
            {
                plugin_data.add(plugin.getName());
            });

            response.setStatus(201);
            return plugin_data;
        }

        AMGN.logger.error("Unauthenticated GET request made to /webpanel/api/plugins from user " + AMGN.bot.getUserById(userid));
        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    //returns a list of plugins that the bot currently has enabled
    @GetMapping(value="/webpanel/api/plugininfo", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode getPluginInfo(@RequestParam(name="name") String name, @RequestParam(name="guild", defaultValue="global") String guild, HttpServletRequest request, HttpServletResponse response)
    {
        AMGN.logger.info("GET request made to /webpanel/api/plugininfo");

        if(request.getCookies() == null)
        {
            AMGN.logger.error("GET request made to /webpanel/api/plugininfo failed (no cookies present)");
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        String userid = resolveIdFromToken(getTokenFromRequest(request));

        if(name == null)
        {
            AMGN.logger.error("GET request made to /webpanel/api/plugininfo failed (no plugin specified) from user " + AMGN.bot.getUserById(userid)
                + " (" + request.getRequestURI() + ")");
            response.setStatus(401);
            return new ObjectMapper().createObjectNode().put("error", "no plugin specified");         
        }

        if(isAuthenticatedRequest(request))
        {
            AMGN.logger.info("Authenticated GET request made to /webpanel/api/plugininfo from user " + AMGN.bot.getUserById(userid));

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
                        AMGN.logger.error("GET request made to /webpanel/api/plugininfo failed (problem reading plugin info) from user " + AMGN.bot.getUserById(userid)
                            + " (" + request.getRequestURI() + ")");
                        response.setStatus(500);
                        return new ObjectMapper().createObjectNode().put("error", "problem reading plugin info");
                    }

                    response.setStatus(201);
                    return obj;
                }
            }
            
            AMGN.logger.error("GET request made to /webpanel/api/plugininfo failed (plugin " + name + "not found) from user " + AMGN.bot.getUserById(userid)
                + " (" + request.getRequestURI() + ")");
            response.setStatus(404);
            return new ObjectMapper().createObjectNode().put("error", "plugin not found");  
        }

        AMGN.logger.error("Unauthenticated GET request made to /webpanel/api/plugininfo from user " + AMGN.bot.getUserById(userid));
        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    //returns the network info for the network
    //for now, any authorised user may use this but I may change it to just operators
    @GetMapping(value="/webpanel/api/networkinfo", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode getNetworkInfo(HttpServletRequest request, HttpServletResponse response)
    {
        AMGN.logger.info("GET request made to /webpanel/api/networkinfo");

        if(request.getCookies() == null)
        {
            AMGN.logger.error("GET request made to /webpanel/api/networkinfo failed (no cookies present)");
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        String userid = resolveIdFromToken(getTokenFromRequest(request));
        if(isAuthenticatedRequest(request))
        {
            AMGN.logger.info("Authenticated GET request made to /webpanel/api/networkinfo from user " + AMGN.bot.getUserById(userid));

            ObjectMapper mapper = new ObjectMapper();
            ObjectNode network_data = mapper.createObjectNode();
            boolean isoperator = GuildNetwork.isOperator(AMGN.bot.getUserById(resolveIdFromToken(getTokenFromRequest(request))));

            ArrayNode guilds = mapper.createArrayNode();

            AMGN.bot.getGuilds().forEach(guild -> {
                //if they are an operator or if they are in the guild, then they can have this guild
                if(isoperator || GuildNetwork.fetchMember(resolveIdFromToken(getTokenFromRequest(request)), guild) != null)
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

        AMGN.logger.error("Unauthenticated GET request made to /webpanel/api/networkinfo from user " + AMGN.bot.getUserById(userid));
        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    //for now, any authorised user may use this but I may change it to just operators
    //allows client to save network data
    @PutMapping(value="/webpanel/api/networkinfo", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode putNetworkInfo(@RequestBody JsonNode body, HttpServletRequest request, HttpServletResponse response)
    {
        AMGN.logger.info("PUT request made to /webpanel/api/networkinfo");

        if(request.getCookies() == null)
        {
            AMGN.logger.error("PUT request made to /webpanel/api/networkinfo failed (no cookies present)");
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        String userid = resolveIdFromToken(getTokenFromRequest(request));
        if(isAuthenticatedRequest(request))
        {
            AMGN.logger.info("Authenticated PUT request made to /webpanel/api/networkinfo from user " + AMGN.bot.getUserById(userid));

            boolean isoperator = GuildNetwork.isOperator(AMGN.bot.getUserById(resolveIdFromToken(getTokenFromRequest(request))));

            ArrayNode guild_data = body.withArray("guild_data");
            Map<Long, Guild>  new_guild_data = new HashMap<Long, Guild>();
            guild_data.forEach(guildnode ->{
                //if they are an operator or if they are in the guild, then they can have this guild
                if(isoperator || GuildNetwork.fetchMember(resolveIdFromToken(getTokenFromRequest(request)),
                                    AMGN.bot.getGuildById(guildnode.get("id").asLong()))
                    != null)
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
                AMGN.logger.error("Error writing network data for PUT /webpanel/api/networkinfo from user " + AMGN.bot.getUserById(userid)
                    + " (" + request.getRequestURI() + ")");
                response.setStatus(500);
                return new ObjectMapper().createObjectNode().put("error", "problem writing network data");
            }

            response.setStatus(201);
            return new ObjectMapper().createObjectNode().put("success", "network data saved");
        }

        AMGN.logger.error("Unauthenticated PUT request made to /webpanel/api/networkinfo from user " + AMGN.bot.getUserById(userid));
        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    //for now, any authorised user may use this but I may change it to just operators
    //allows client to save plugin configs
    @PutMapping(value="/webpanel/api/plugininfo", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode putPluginConfigInfo(@RequestParam(name="name") String name, @RequestParam(defaultValue = "global") String guild, @RequestBody JsonNode body, HttpServletRequest request, HttpServletResponse response)
    {
        AMGN.logger.info("PUT request made to /webpanel/api/plugininfo");

        if(request.getCookies() == null)
        {
            AMGN.logger.error("PUT request made to /webpanel/api/plugininfo failed (no cookies present)");
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        String userid = resolveIdFromToken(getTokenFromRequest(request));

        if(name == null)
        {
            AMGN.logger.error("PUT request made to /webpanel/api/plugininfo failed (no plugin specified) from user " + AMGN.bot.getUserById(userid)
                + " (" + request.getRequestURI() + ")");
            response.setStatus(401);
            return new ObjectMapper().createObjectNode().put("error", "no plugin specified");         
        }

        if(isAuthenticatedRequest(request))
        {
            AMGN.logger.info("Authenticated PUT request made to /webpanel/api/plugininfo from user " + AMGN.bot.getUserById(userid));

            ObjectMapper mapper = new ObjectMapper();
            for(Plugin plugin : AMGN.plugin_listeners.keySet())
            {
                if(plugin.getName().equalsIgnoreCase(name))
                {
                    //we go through the returned data in the following format (as is given to the user originally in the get method)
                    //[{file: "config", data: {...whatever}}, {file: "names", data: {...more whatever}}, {file: "winners", data: {...even more whatever}}]
                    //then for each config file, we write the data
                    for(JsonNode config : body)
                    {
                        Map<String, Object> result = mapper.convertValue(config.get("data"), new TypeReference<Map<String, Object>>(){});
                        if(guild.equalsIgnoreCase("global"))
                            plugin.getConfig().setGlobalConfig(config.get("file").asText() + ".yml", result);
                        else
                            plugin.getConfig().setGuildConfig(config.get("file").asText() + ".yml", result, AMGN.bot.getGuildById(guild));
                    }

                    //reload plugin
                    GuildNetwork.disablePlugin(plugin);
                    GuildNetwork.enablePlugin(plugin);

                    response.setStatus(201);
                    return new ObjectMapper().createObjectNode().put("success", "plugin config saved");
                }
            }

            AMGN.logger.error("Plugin not found for PUT request at /webpanel/api/plugininfo for user " + AMGN.bot.getUserById(userid)
                + "( " + request.getRequestURI() + ")");
            response.setStatus(404);
            return new ObjectMapper().createObjectNode().put("error", "plugin not found");
        }

        AMGN.logger.error("Unauthenticated PUT request made to /webpanel/api/networkinfo from user " + AMGN.bot.getUserById(userid));
        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    //use this API endpoint to request a local config for a plugin that doesn't have one
    //needs a plugin name and a guild
    @PostMapping(value="/webpanel/api/plugininfo", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode requestLocalConfig(@RequestParam(name="name") String name, @RequestParam(name="guild") String guild, HttpServletRequest request, HttpServletResponse response)
    {
        AMGN.logger.info("POST request made to /webpanel/api/plugininfo");

        if(request.getCookies() == null)
        {
            AMGN.logger.error("POST request made to /webpanel/api/plugininfo failed (no cookies present)");
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        String userid = resolveIdFromToken(getTokenFromRequest(request));

        if(isAuthenticatedRequest(request))
        {
            AMGN.logger.info("Authenticated POST request made to /webpanel/api/plugininfo from user " + AMGN.bot.getUserById(userid));

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

                        //return config as json
                        confignode = getConfigAsJson(plugin, guild);
                    }

                    return confignode;
                }
            }

            AMGN.logger.error("Plugin not found for POST request at /webpanel/api/plugininfo for user " + AMGN.bot.getUserById(userid)
                + "(" + request.getRequestURI() + ")");
            response.setStatus(404);
            return new ObjectMapper().createObjectNode().put("error", "plugin not found");
        }

        AMGN.logger.error("Unauthenticated POST request made to /webpanel/api/networkinfo from user " + AMGN.bot.getUserById(userid));
        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

        //use this API endpoint to request a local config for a plugin that doesn't have one
    //needs a plugin name and a guild
    @DeleteMapping(value="/webpanel/api/plugininfo", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode deleteLocalConfig(@RequestParam(name="name") String name, @RequestParam(name="guild") String guild, HttpServletRequest request, HttpServletResponse response)
    {
        AMGN.logger.info("DELETE request made to /webpanel/api/plugininfo");

        if(request.getCookies() == null)
        {
            AMGN.logger.error("DELETE request made to /webpanel/api/plugininfo failed (no cookies present)");
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        String userid = resolveIdFromToken(getTokenFromRequest(request));
        if(isAuthenticatedRequest(request))
        {
            AMGN.logger.info("Authenticated DELETE request made to /webpanel/api/plugininfo from user " + AMGN.bot.getUserById(userid));

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
                            AMGN.logger.error("Error deleting local network data for DELETE /webpanel/api/plugininfo from user " + AMGN.bot.getUserById(userid)
                                + "(" + request.getRequestURI() + ")");
                            e.printStackTrace();
                        }
                        
                        response.setStatus(200);
                        return new ObjectMapper().createObjectNode().put("success", "local config deleted");
                    }
                    else
                    {
                        AMGN.logger.info("Success, local config already deleted for DELETE at /webpanel/api/plugininfo for " + AMGN.bot.getUserById(userid) + " with " + name);
                        response.setStatus(201);
                        return new ObjectMapper().createObjectNode().put("success", "local config already deleted");
                    }
                }
            }

            AMGN.logger.error("Plugin not found for DELETE request at /webpanel/api/plugininfo for user " + AMGN.bot.getUserById(userid)
                + "(" + request.getRequestURI() + ")");
            response.setStatus(404);
            return new ObjectMapper().createObjectNode().put("error", "plugin not found");
        }

        AMGN.logger.error("Unauthenticated POST request made to /webpanel/api/networkinfo from user " + AMGN.bot.getUserById(userid));
        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    //reset a plugin config for a guild or even globally
    @PostMapping(value = "/webpanel/api/resetpluginconfig", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode resetConfig(@RequestParam String guild, @RequestParam String plugin, HttpServletRequest request, HttpServletResponse response)
    {
        AMGN.logger.info("POST request made to /webpanel/api/resetpluginconfig");

        if(request.getCookies() == null)
        {
            AMGN.logger.error("POST request made to /webpanel/api/resetpluginconfig failed (no cookies present)");
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        String userid = resolveIdFromToken(getTokenFromRequest(request));
        if(isAuthenticatedRequest(request))
        {
            //look for plugin
            for(Plugin p : AMGN.plugin_listeners.keySet())
            {
                if(p.getName().equalsIgnoreCase(plugin))
                {
                    //use global files as a reference
                    File globalconfigdir = new File(p.getGlobalConfigPath());
                    File[] configfiles = globalconfigdir.listFiles((file, name) ->{
                        return name.endsWith(".yml");
                    });

                    if(guild.equalsIgnoreCase("global"))
                    {
                        for(File config : configfiles)
                            p.getConfig().generateGlobalResource(config.getName());
                    }
                    else if(AMGN.bot.getGuildById(guild) != null)
                    {
                        for(File config : configfiles)
                            p.getConfig().generateLocalResource(config.getName(), AMGN.bot.getGuildById(guild));
                    }

                    response.setStatus(200);
                    return getConfigAsJson(p, guild);
                }
            }

            AMGN.logger.error("Plugin not found for POST request at /webpanel/api/resetpluginconfig for user " + AMGN.bot.getUserById(userid)
                + "(" + request.getRequestURI() + ")");
            response.setStatus(404);
            return new ObjectMapper().createObjectNode().put("error", "plugin not found");
        }

        AMGN.logger.error("Unauthenticated PUT request made to /webpanel/api/whitelist from user " + AMGN.bot.getUserById(userid));
        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");

    }

    @PutMapping(value = "/webpanel/api/whitelist", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode addGuildToWhitelist(@RequestParam String guild, @RequestParam String plugin, HttpServletRequest request, HttpServletResponse response)
    {
        AMGN.logger.info("PUT request made to /webpanel/api/whietlist");

        if(request.getCookies() == null)
        {
            AMGN.logger.error("PUT request made to /webpanel/api/whitelist failed (no cookies present)");
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        String userid = resolveIdFromToken(getTokenFromRequest(request));
        if(isAuthenticatedRequest(request))
        {
            AMGN.logger.info("Authenticated PUT request made to /webpanel/api/whitelist from user " + AMGN.bot.getUserById(userid));

            //also need permission AMGN.commands.whitelist
            if(Permissions.userHasPermission(AMGN.bot.getUserById(resolveIdFromToken(getTokenFromRequest(request))), null, "AMGN.commands.whitelist"))
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
                    AMGN.logger.error("Error writing whitelist data for PUT /webpanel/api/whitelist from user " + AMGN.bot.getUserById(userid)
                        + " (" + request.getRequestURI() + ")");
                    e.printStackTrace();
                }
    
                response.setStatus(200);
                return new ObjectMapper().createObjectNode().put("success", "whitelist updated successfully");
            }
            else
            {
                AMGN.logger.error("User " + AMGN.bot.getUserById(userid) + "does not have permission to use PUT on /webpanel/api/whitelist");
                response.setStatus(403);
                return new ObjectMapper().createObjectNode().put("error", "user is missing permission AMGN.commands.whitelist"); 
            }
        }

        AMGN.logger.error("Unauthenticated PUT request made to /webpanel/api/whitelist from user " + AMGN.bot.getUserById(userid));
        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    @DeleteMapping(value = "/webpanel/api/whitelist", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode removeGuildFromWhitelist(@RequestParam String guild, @RequestParam String plugin, HttpServletRequest request, HttpServletResponse response)
    {
        AMGN.logger.info("DELETE request made to /webpanel/api/whietlist");

        if(request.getCookies() == null)
        {
            AMGN.logger.error("DELETE request made to /webpanel/api/whitelist failed (no cookies present)");
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        String userid = resolveIdFromToken(getTokenFromRequest(request));
        if(isAuthenticatedRequest(request))
        {
            AMGN.logger.info("Authenticated DELETE request made to /webpanel/api/whitelist from user " + AMGN.bot.getUserById(userid));
            //also need permission AMGN.commands.whitelist
            if(Permissions.userHasPermission(AMGN.bot.getUserById(resolveIdFromToken(getTokenFromRequest(request))), null, "AMGN.commands.whitelist"))
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
                    AMGN.logger.error("Error writing whitelist data for DELETE /webpanel/api/whitelist from user " + AMGN.bot.getUserById(userid)
                        + "(" + request.getRequestURI() + ")");
                    e.printStackTrace();
                }
    
                response.setStatus(200);
                return new ObjectMapper().createObjectNode().put("success", "whitelist updated successfully");
            }
            else
            {
                AMGN.logger.error("User " + AMGN.bot.getUserById(userid) + "does not have permission to use DELETE on /webpanel/api/whitelist");
                response.setStatus(403);
                return new ObjectMapper().createObjectNode().put("error", "invalid token");
            }
        }
        
        AMGN.logger.error("Unauthenticated DELETE request made to /webpanel/api/whitelist from user " + AMGN.bot.getUserById(userid));
        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    @PutMapping(value = "/webpanel/api/blacklist", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode addGuildToBlacklist(@RequestParam String guild, @RequestParam String plugin, HttpServletRequest request, HttpServletResponse response)
    {
        AMGN.logger.info("PUT request made to /webpanel/api/blacklist");

        if(request.getCookies() == null)
        {
            AMGN.logger.error("PUT request made to /webpanel/api/blacklist failed (no cookies present)");
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        String userid = resolveIdFromToken(getTokenFromRequest(request));
        if(isAuthenticatedRequest(request))
        {
            AMGN.logger.info("Authenticated PUT request made to /webpanel/api/blacklist from user " + AMGN.bot.getUserById(userid));

            //also need permission AMGN.commands.blacklist
            if(Permissions.userHasPermission(AMGN.bot.getUserById(resolveIdFromToken(getTokenFromRequest(request))), null, "AMGN.commands.blacklist"))
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
                    AMGN.logger.error("Error writing whitelist data for PUT /webpanel/api/blacklist from user " + AMGN.bot.getUserById(userid)
                        + " (" + request.getRequestURI() + ")");
                    e.printStackTrace();
                }
    
                response.setStatus(200);
                return new ObjectMapper().createObjectNode().put("success", "blacklist updated successfully");
            }
            else
            {
                AMGN.logger.error("User " + AMGN.bot.getUserById(userid) + "does not have permission to use PUT on /webpanel/api/blacklist");
                response.setStatus(403);
                return new ObjectMapper().createObjectNode().put("error", "user is missing permission AMGN.commands.blacklist");
            }
        }
        
        AMGN.logger.error("Unauthenticated PUT request made to /webpanel/api/blacklist from user " + AMGN.bot.getUserById(userid));
        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    @DeleteMapping(value = "/webpanel/api/blacklist", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode removeGuildFromBlacklist(@RequestParam String guild, @RequestParam String plugin, HttpServletRequest request, HttpServletResponse response)
    {
        AMGN.logger.info("DELETE request made to /webpanel/api/blacklist");

        if(request.getCookies() == null)
        {
            AMGN.logger.error("DELETE request made to /webpanel/api/blacklist failed (no cookies present)");
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        String userid = resolveIdFromToken(getTokenFromRequest(request));
        if(isAuthenticatedRequest(request))
        {
            AMGN.logger.info("Authenticated DELETE request made to /webpanel/api/blacklist from user " + AMGN.bot.getUserById(userid));

            //also need permission AMGN.commands.blacklist
            if(Permissions.userHasPermission(AMGN.bot.getUserById(resolveIdFromToken(getTokenFromRequest(request))), null, "AMGN.commands.blacklist"))
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
                    AMGN.logger.error("Error writing whitelist data for DELETE /webpanel/api/blacklist from user " + AMGN.bot.getUserById(userid)
                        + "( " + request.getRequestURI() + ")");
                    e.printStackTrace();
                }
    
                response.setStatus(200);
                return new ObjectMapper().createObjectNode().put("success", "blacklist updated successfully");
            }
            else
            {
                AMGN.logger.error("User " + AMGN.bot.getUserById(userid) + "does not have permission to use DELETE on /webpanel/api/blacklist");
                response.setStatus(403);
                return new ObjectMapper().createObjectNode().put("error", "user is missing permission AMGN.commands.blacklist"); 
            }
        }
        
        AMGN.logger.error("Unauthenticated DELETE request made to /webpanel/api/blacklist from user " + AMGN.bot.getUserById(userid));
        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    @GetMapping(value = "/webpanel/api/whitelist", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode isInWhitelist(@RequestParam String guild, @RequestParam String plugin, HttpServletRequest request, HttpServletResponse response)
    {
        AMGN.logger.info("GET request made to /webpanel/api/whitelist");

        if(request.getCookies() == null)
        {
            AMGN.logger.error("GET request made to /webpanel/api/whitelist failed (no cookies present)");
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        String userid = resolveIdFromToken(getTokenFromRequest(request));
        if(isAuthenticatedRequest(request))
        {
            AMGN.logger.info("Authenticated GET request made to /webpanel/api/whitelist from user " + AMGN.bot.getUserById(userid));

            //global is always whitelisted
            if(guild.equals("global"))
            {
                return new ObjectMapper().createObjectNode().put("result", true);
            }

            if(GuildNetwork.whitelist.getOrDefault(plugin, new ArrayList<Long>()).contains(Long.parseLong(guild)))
                return new ObjectMapper().createObjectNode().put("result", true);

            return new ObjectMapper().createObjectNode().put("result", false);
        }

        AMGN.logger.error("Unauthenticated GET request made to /webpanel/api/whitelist from user " + AMGN.bot.getUserById(userid));
        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    @GetMapping(value = "/webpanel/api/blacklist", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode isInBlacklist(@RequestParam String guild, @RequestParam String plugin, HttpServletRequest request, HttpServletResponse response)
    {
        AMGN.logger.info("GET request made to /webpanel/api/blacklist");

        if(request.getCookies() == null)
        {
            AMGN.logger.error("GET request made to /webpanel/api/blacklist failed (no cookies present)");
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        String userid = resolveIdFromToken(getTokenFromRequest(request));
        if(isAuthenticatedRequest(request))
        {
            AMGN.logger.info("Authenticated GET request made to /webpanel/api/blacklist from user " + AMGN.bot.getUserById(userid));

            //global is never blacklisted
            if(guild.equals("global"))
            {
                return new ObjectMapper().createObjectNode().put("result", false);
            }

            if(GuildNetwork.blacklist.getOrDefault(plugin, new ArrayList<Long>()).contains(Long.parseLong(guild)))
                return new ObjectMapper().createObjectNode().put("result", true);

            return new ObjectMapper().createObjectNode().put("result", false);
        }

        AMGN.logger.error("Unauthenticated GET request made to /webpanel/api/blacklist from user " + AMGN.bot.getUserById(userid));
        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    //returns the permissions.yml file as a json object
    @GetMapping(value = "/webpanel/api/permissions", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode getPermissions(HttpServletRequest request, HttpServletResponse response)
    {
        AMGN.logger.info("GET request made to /webpanel/api/permissions");

        if(request.getCookies() == null)
        {
            AMGN.logger.error("GET request made to /webpanel/api/permissions failed (no cookies present)");
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        String userid = resolveIdFromToken(getTokenFromRequest(request));
        if(isAuthenticatedRequest(request))
        {
            AMGN.logger.info("Authenticated GET request made to /webpanel/api/permissions from user " + AMGN.bot.getUserById(userid));

            //must also have AMGN.commands.listpermissions to get this info
            if(Permissions.userHasPermission(AMGN.bot.getUserById(resolveIdFromToken(getTokenFromRequest(request))), null, "AMGN.commands.listpermissions"))
            {
                try
                {
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode permissions = mapper.reader().readTree(mapper.writeValueAsString(IOHandler.readAllYamlData(GuildNetwork.PERMISSIONS_PATH)));
                    return permissions;
                }
                catch(JsonProcessingException | FileNotFoundException e)
                {
                    AMGN.logger.error("Error processing json response from " + AMGN.bot.getUserById(userid)
                        + " (" + request.getRequestURI() + ")");
                    e.printStackTrace();
                }
            }
            else
            {
                AMGN.logger.error("User " + AMGN.bot.getUserById(userid) + " does not have permission to use GET /webpanel/api/permissions");
                response.setStatus(403);
                return new ObjectMapper().createObjectNode().put("error", "this user is missing permission AMGN.commands.listpermissions");
            }
        }

        AMGN.logger.error("Unauthenticated GET request made to /webpanel/api/permissions from user " + AMGN.bot.getUserById(userid));
        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    @PutMapping(value="/webpanel/api/permissions", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode putPermissionInfo(@RequestBody JsonNode body, HttpServletRequest request, HttpServletResponse response)
    {
        AMGN.logger.info("PUT request made to /webpanel/api/permissions");

        if(request.getCookies() == null)
        {
            AMGN.logger.error("PUT request made to /webpanel/api/permissions failed (no cookies present)");
            response.setStatus(403);
            return new ObjectMapper().createObjectNode().put("error", "invalid token");
        }

        String userid = resolveIdFromToken(getTokenFromRequest(request));
        if(isAuthenticatedRequest(request))
        {
            AMGN.logger.info("Authenticated PUT request made to /webpanel/api/permissions from user " + AMGN.bot.getUserById(userid));
            //must also have AMGN.commands.groups and AMGN.commands.permission to do this
            if(Permissions.userHasPermission(AMGN.bot.getUserById(resolveIdFromToken(getTokenFromRequest(request))), null, "AMGN.commands.groups")
                && Permissions.userHasPermission(AMGN.bot.getUserById(resolveIdFromToken(getTokenFromRequest(request))), null, "AMGN.commands.permission"))
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
                    AMGN.logger.error("Error processing json response from " + AMGN.bot.getUserById(userid)
                        + " (" + request.getRequestURI() + ")");
                    response.setStatus(500);
                    return new ObjectMapper().createObjectNode().put("error", "problem writing permission data");
                }
    
                response.setStatus(201);
                return new ObjectMapper().createObjectNode().put("success", "network data saved");
            }
            else
            {
                AMGN.logger.error("User " + AMGN.bot.getUserById(userid) + " does not have permission to use PUT /webpanel/api/permissions");
                response.setStatus(403);
                return new ObjectMapper().createObjectNode().put("error", "user must have both permissions AMGN.commands.groups and AMGN.commands.permission");
            }
        }

        AMGN.logger.error("Unauthenticated PUT request made to /webpanel/api/permissions from user " + AMGN.bot.getUserById(userid));
        response.setStatus(403);
        return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    public static String resolveIdFromToken(String token)
    {
        RestTemplate template = new RestTemplate();
        HttpHeaders userinfoheaders = new HttpHeaders();
        userinfoheaders.set("Authorization", "Bearer " + token);
        ResponseEntity<JsonNode> userinfo = template.exchange(API_URL + "/users/@me", HttpMethod.GET, new HttpEntity<>("", userinfoheaders), JsonNode.class);
        JsonNode body = userinfo.getBody();
        return body.get("id") != null ? 
            body.get("id").asText()
            :
            null;
    }

    public static ArrayNode getConfigAsJson(Plugin plugin, String guild)
    {
        File configdir;
        if(guild.equalsIgnoreCase("global")) //use global config if specified
            configdir = new File(plugin.getGlobalConfigPath());
        else if(guild.matches(GuildNetwork.ID_REGEX) && AMGN.bot.getGuildById(guild) != null) //else check if guild really exists
            configdir = new File(plugin.getGuildConfigPath(AMGN.bot.getGuildById(guild)));
        else //otherwise return nothing
            return new ObjectMapper().createArrayNode();
        
        File[] configfiles = configdir.listFiles((file, filename) -> {
            return filename.endsWith(".yml");
        });

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode confignode = mapper.createArrayNode();
        if(configfiles != null)
        {
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
                AMGN.logger.error("Error reading config for plugin " + plugin.getName() + " and guild " + guild + " in getConfigAsJson");
                e.printStackTrace();
            }
        }

        return confignode;
    }

    //will look at a request and see if this is a valid token
    //and if this user has permission to view the webpanel
    //returns true if they can, false otherwise
    public static boolean isAuthenticatedRequest(HttpServletRequest request)
    {
        AMGN.logger.info("Checking authentication of request");

        String token = getTokenFromRequest(request);
        long id;
        //either retrieve id from token from the cache, or query the API for the id and then add that id to the cache 
        if(TOKEN_CACHE.containsKey(token))
            id = TOKEN_CACHE.get(token);
        else
        {
            String id_str = resolveIdFromToken(token);
            if(id_str == null)
            {
                AMGN.logger.error("Invalid token supplied to request, no ID was found from request token");
                return false;
            }

            id = Long.parseLong(id_str);
            TOKEN_CACHE.put(token, id);
        }



        if(Permissions.userHasPermission(AMGN.bot.getUserById(id), null, "AMGN.webpanel.access"))
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
