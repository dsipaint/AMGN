package com.github.dsipaint.AMGN.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;

@Controller
public class WebpanelController
{
    static final String TOKEN_URL="https://discord.com/api/oauth2/token",
        API_URL = "https://discord.com/api/v10",
        IMAGE_URL = "https://cdn.discordapp.com/avatars/userid/hash.png";

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

        if(!TOKEN_CACHE.contains(usertoken))
            TOKEN_CACHE.add(usertoken);

        //for username and pfp try GETting /users/me
        HttpHeaders userinfoheaders = new HttpHeaders();
        userinfoheaders.set("Authorization", "Bearer " + usertoken);
        ResponseEntity<JsonNode> userinfo = template.exchange(API_URL + "/users/@me", HttpMethod.GET, new HttpEntity<>("", userinfoheaders), JsonNode.class);


        Cookie token = new Cookie("discord_token", usertoken);
        token.setPath("/webpanel");
        response.addCookie(token);

        Cookie username = new Cookie("discord_username", userinfo.getBody().get("username").asText());
        username.setPath("/webpanel");
        response.addCookie(username);

        Cookie pfp = new Cookie("discord_pfp",
            IMAGE_URL.replace("userid", userinfo.getBody().get("id").asText())
            .replace("hash", userinfo.getBody().get("avatar").asText()));
        pfp.setPath("/webpanel");
        response.addCookie(pfp);

        //return a redirect page
        return "redirect";
    }

    //returns a list of guilds for this bot, for an authenticated user
    @GetMapping(value="/webpanel/api/guilds", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode getBotGuilds(HttpServletRequest request, HttpServletResponse response)
    {
        // if(request.getCookies() == null)
        // {
        //     response.setStatus(403);
        //     return new ObjectMapper().createObjectNode().put("error", "invalid token");
        // }

        // for(Cookie c : request.getCookies())
        // {
        //     if(c.getName().equals("discord_token")
        //         && TOKEN_CACHE.contains(c.getValue()))
        //     {
                ObjectMapper mapper = new ObjectMapper();
                ArrayNode guild_data = mapper.createArrayNode();
                AMGN.bot.getGuilds().forEach(guild -> {
                    ObjectNode objectnode = mapper.createObjectNode();
                    objectnode.put("id", guild.getId());
                    objectnode.put("name", guild.getName());
                    objectnode.put("picture", guild.getIconUrl());
                    guild_data.add(objectnode);
                });

                response.setStatus(201);
                return guild_data;
        //     }
        // }

        // response.setStatus(403);
        // return new ObjectMapper().createObjectNode().put("error", "invalid token");
    }

    //returns a list of plugins that the bot currently has enabled
    @GetMapping(value="/webpanel/api/plugins", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JsonNode getBotPlugins(HttpServletRequest request, HttpServletResponse response)
    {
        // if(request.getCookies() == null)
        // {
        //     response.setStatus(403);
        //     return new ObjectMapper().createObjectNode().put("error", "invalid token");
        // }

        // for(Cookie c : request.getCookies())
        // {
        //     if(c.getName().equals("discord_token")
        //         && TOKEN_CACHE.contains(c.getValue()))
        //     {
                ObjectMapper mapper = new ObjectMapper();
                ArrayNode plugin_data = mapper.createArrayNode();
                AMGN.plugin_listeners.keySet().forEach(plugin ->
                {
                    ObjectNode plugin_obj = mapper.createObjectNode();
                    plugin_obj.put("name", plugin.getName());
                    plugin_obj.put("author", plugin.getAuthor());
                    plugin_obj.put("description", plugin.getDescription());
                    plugin_obj.put("picture", plugin.getImageUrl());
                    plugin_obj.put("version", plugin.getVersion());
                    plugin_data.add(plugin_obj);
                });

                response.setStatus(201);
                return plugin_data;
        //     }
        // }

        // response.setStatus(403);
        // return new ObjectMapper().createObjectNode().put("error", "invalid token");
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
}
