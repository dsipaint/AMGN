package com.github.dsipaint.AMGN.entities.listeners.managed.menu;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.github.dsipaint.AMGN.entities.listeners.managed.menu.MenuBuilder.InvalidMenuException;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;

public class ScrollMenuBuilder
{
    private String fullbody, delimiter;
    private EmbedBuilder template;
    private LinkedList<String> pages;
    private int currentpage;
    private Menu menu;
    private MenuBuilder builder;

    public ScrollMenuBuilder(Plugin plugin, String fullbody, String delimiter, EmbedBuilder template, TextChannel textchannel) throws InvalidMenuException
    {
        this.fullbody = fullbody;
        this.delimiter = delimiter;

        //template embedbuilder, used for extra details such as colour, title etc.
        this.template = new EmbedBuilder();
        this.template.copyFrom(template);

        //generate the list of pages to be used in the desc.
        generatePages();

        //set up menu to be at page 1 (need to set this.currentpage to a different number to  use the method)
        this.currentpage = -1;
        setCurrentPage(0);

        //construct Menu
        this.builder = new MenuBuilder(plugin, textchannel, this.template.build());

        //add scroll buttons to menu
        //left arrow
        builder.addButton(Emoji.fromUnicode("U+25C0"), press ->
        {
            //unpress button
            press.getMessage().removeReaction(Emoji.fromUnicode("U+25C0"), press.getMember().getUser()).queue();

            //update the page
            setCurrentPage(this.currentpage - 1);
        },
        unpress -> {})
        //right arrow
        .addButton(Emoji.fromUnicode("U+25B6"), press ->
        {
            //unpress button
            press.getMessage().removeReaction(Emoji.fromUnicode("U+25B6"), press.getMember().getUser()).queue();

            //update the page
            setCurrentPage(this.currentpage + 1);
        },
        unpress -> {});
    }

    public String getFullBody()
    {
        return this.fullbody;
    }

    public String getDelimiter()
    {
        return this.delimiter;
    }

    public LinkedList<String> getPages()
    {
        return this.pages;
    }

    public String getCurrentPage()
    {
        return pages.get(currentpage);
    }

    public int getCurrentPageNumber()
    {
        return currentpage;
    }

    public Menu getMenu()
    {
        return menu;
    }

    public MenuBuilder getBuilder()
    {
        return builder;
    }

    public void build()
    {
        try
        {
            this.menu = this.builder.build();   
        }
        catch(InvalidMenuException e)
        {
            e.printStackTrace();
        }
    }

    public void setCurrentPage(int newpagenumber)
    {
        //no need to bother if it's the same page
        if(newpagenumber == this.currentpage)
            return;

        //clamp page number to within range
        this.currentpage = newpagenumber;
        if(this.currentpage < 0)
            currentpage = 0;
        else if(this.currentpage >= pages.size())
            currentpage = pages.size() - 1;

        template.setDescription(pages.get(currentpage))
            .setFooter("Page " + (currentpage + 1) + " of " + pages.size());

        //send the latest page
        //set it on the menu if the menu's already built
        if(menu != null)
            menu.getMessage().editMessageEmbeds(template.build()).queue();
        else if(builder != null)
            builder.setMessage(template.build());
    }

    public ScrollMenuBuilder alphabetiseDescription()
	{
		//split string
		List<String> list = Arrays.asList(fullbody.split(delimiter));

		//aphabetise sections
		list.sort((item1, item2) ->
		{
			//compare all characters of the word

			char[] item1chars = item1.toCharArray(),
				item2chars = item2.toCharArray();

			int shortestlength = item1chars.length < item2chars.length ?
				item1chars.length
				:
				item2chars.length;
			
			//iterate through the shortest word (to avoid indexOutOfBoundsException)
			for(int i = 0; i < shortestlength; i++)
			{
				//first character that differs, we can report back as our answer
				if(item1chars[i] < item2chars[i])
					return -1;
				if(item1chars[i] > item2chars[i])
					return 1;
			}

			//if all characters up to the shortest word are the same, then return
			//the difference. If it is a longer word this ensures  the longer word
			//is put first, otherwise we return  0 to say they're equal.
			return item2chars.length - item1chars.length;
		});

		//reconstruct description string
		StringBuilder sb = new StringBuilder();
		for(String section : list)
			sb.append(section + delimiter);

        //set the new fullbody
        this.fullbody = sb.toString();

        //regenerate all the pages now that we have alphabetised.
		generatePages();

        //regenerate the old page by setting the same page as the current page
        int oldpageno = Integer.valueOf(this.currentpage);
        this.currentpage = -1;
        setCurrentPage(oldpageno);

        return this;
	}

    private void generatePages()
    {
        //remember all the pages of the whole menu
        this.pages = new LinkedList<String>();

        //split body into separate pages and store each page
        String[] delimitedbody = fullbody.split("\n");
        StringBuilder page = new StringBuilder("");

        for(String section : delimitedbody)
        {
            if(page.length() + section.length() + delimiter.length() < MessageEmbed.DESCRIPTION_MAX_LENGTH)
                page.append(section + delimiter);
            else
            {
                pages.add(page.toString());
                page = new StringBuilder("");
            }
        }

        //add whatever is left on the last page
        if(page.length() > 0)
            pages.add(page.toString());

        //if we have no pages, add a default empty page to make scrolling through the menu work nicer, for empty menus
        // i.e. no IndexOutOfBoundsException for having empty pages list, and empty embeds aren't listed as "page 0" due to lack of pages
        if(pages.size() == 0)
            pages.add("");
    }
}
