package com.github.dsipaint.AMGN.main.web;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import com.github.dsipaint.AMGN.entities.Guild;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.main.AMGN;

@Controller
public class WebpanelController
{
    @RequestMapping("/webpanel")
    public String getCustomWebpanel(Model model)
    {
        model.addAttribute("botname", AMGN.bot.getSelfUser().getName());
        model.addAttribute("botpfp", AMGN.bot.getSelfUser().getAvatarUrl());
        model.addAttribute("clientid", GuildNetwork.clientid);
        model.addAttribute("redirecturi", GuildNetwork.redirecturi);
        return "homepage";
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
