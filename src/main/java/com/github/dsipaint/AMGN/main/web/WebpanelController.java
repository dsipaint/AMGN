package com.github.dsipaint.AMGN.main.web;

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
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.main.AMGN;

import net.dv8tion.jda.api.entities.Guild;

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
    @GetMapping("/webpanel/api/guilds")
    public List<Guild> getBotGuilds(HttpServletRequest request)
    {
        for(Cookie c : request.getCookies())
        {
            if(c.getName().equals("discord_token")
                && TOKEN_CACHE.contains(c.getValue()))
                return AMGN.bot.getGuilds();
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
}
