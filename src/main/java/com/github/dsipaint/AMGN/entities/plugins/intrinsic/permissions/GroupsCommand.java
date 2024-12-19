package com.github.dsipaint.AMGN.entities.plugins.intrinsic.permissions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.CommandEvent;
import com.github.dsipaint.AMGN.entities.listeners.DefaultCommand;
import com.github.dsipaint.AMGN.entities.listeners.menu.ScrollMenuBuilder;
import com.github.dsipaint.AMGN.entities.listeners.menu.MenuBuilder.InvalidMenuException;
import com.github.dsipaint.AMGN.io.IOHandler;

import net.dv8tion.jda.api.EmbedBuilder;

public class GroupsCommand implements Consumer<CommandEvent>
{
    @SuppressWarnings("unchecked")
    public void accept(CommandEvent e)
    {
        if(e.getArgs().length == 1)
            return;

        HashMap<String, Object> groups = null;
        try
        {
            groups = (HashMap<String, Object>) IOHandler.readYamlData(GuildNetwork.PERMISSIONS_PATH, "groups");
        }
        catch(FileNotFoundException e1)
        {
            e1.printStackTrace();
        }

        if(e.getArgs()[1].equalsIgnoreCase("create"))
        {
            if(e.getArgs().length < 3)
                return;

            //if group does not already exist then create
            if(groups.containsKey(e.getArgs()[2].toLowerCase()))
            {
                e.getTextChannel().sendMessage("This group already exists").queue();
                return;
            }

            HashMap<String, List<String>> groupdata = new HashMap<String, List<String>>();
            groupdata.put("members", new ArrayList<String>());
            groupdata.put("permissions", new ArrayList<String>());
            
            groups.put(e.getArgs()[2].toLowerCase(), groupdata);

            //write back to YAML
            writeNewGroupData(groups);

            e.getTextChannel().sendMessage("Created new group " + e.getArgs()[2].toLowerCase()).queue();
            return;
        }

        if(e.getArgs()[1].equalsIgnoreCase("destroy"))
        {
            if(e.getArgs().length < 3)
                return;

            if(!groups.containsKey(e.getArgs()[2].toLowerCase()))
            {
                e.getTextChannel().sendMessage("This group does not exist").queue();
                return;
            }

            groups.remove(e.getArgs()[2].toLowerCase());
            //write back to YAML
            writeNewGroupData(groups);
            e.getTextChannel().sendMessage("Group " + e.getArgs()[2].toLowerCase() + " has been destroyed.").queue();
            return;
        }

        //^group add group id
        if(e.getArgs()[1].equalsIgnoreCase("add"))
        {
            if(e.getArgs().length < 4)
                return;

            if(!groups.containsKey(e.getArgs()[2].toLowerCase()))
            {
                e.getTextChannel().sendMessage("This group does not exist").queue();
                return;
            }

            if(e.getArgs().length < 4)
                return;

            HashMap<String, List<String>> groupdata = (HashMap<String, List<String>>) groups.get(e.getArgs()[2]);
            if(!groupdata.get("members").contains(e.getArgs()[3]))
                groupdata.get("members").add(e.getArgs()[3]);

            //write back to YAML
            writeNewGroupData(groups);

            e.getTextChannel().sendMessage("Added " + e.getArgs()[3] + " to " + e.getArgs()[2].toLowerCase()).queue();

            return;
        }

        if(e.getArgs()[1].equalsIgnoreCase("remove"))
        {
            if(e.getArgs().length < 4)
                return;

            if(!groups.containsKey(e.getArgs()[2].toLowerCase()))
            {
                e.getTextChannel().sendMessage("This group does not exist").queue();
                return;
            }

            if(e.getArgs().length < 4)
                return;

            HashMap<String, List<String>> groupdata = (HashMap<String, List<String>>) groups.get(e.getArgs()[2]);
            if(groupdata.get("members").contains(e.getArgs()[3]))
                groupdata.get("members").remove(e.getArgs()[3]);

            //write back to YAML
            writeNewGroupData(groups);
            
            e.getTextChannel().sendMessage("Removed " + e.getArgs()[3] + " from " + e.getArgs()[2].toLowerCase()).queue();

            return;
        }

        //^groups addperm group perm
        if(e.getArgs()[1].equalsIgnoreCase("addperm"))
        {
            if(e.getArgs().length < 4)
                return;

            if(!groups.containsKey(e.getArgs()[2].toLowerCase()))
            {
                e.getTextChannel().sendMessage("This group does not exist").queue();
                return;
            }

            if(e.getArgs().length < 4)
                return;

            HashMap<String, List<String>> groupdata = (HashMap<String, List<String>>) groups.get(e.getArgs()[2]);
            if(!groupdata.get("permissions").contains(e.getArgs()[3]))
                groupdata.get("permissions").add(e.getArgs()[3]);

            //write back to YAML
            writeNewGroupData(groups);

            e.getTextChannel().sendMessage("Added permission " + e.getArgs()[3] + " to " + e.getArgs()[2].toLowerCase()).queue();
            return;
        }

        //^groups removeperm group perm
        if(e.getArgs()[1].equalsIgnoreCase("removeperm"))
        {
            if(e.getArgs().length < 4)
                return;

            if(!groups.containsKey(e.getArgs()[2].toLowerCase()))
            {
                e.getTextChannel().sendMessage("This group does not exist").queue();
                return;
            }

            if(e.getArgs().length < 4)
                return;

            HashMap<String, List<String>> groupdata = (HashMap<String, List<String>>) groups.get(e.getArgs()[2]);
            if(groupdata.get("permissions").contains(e.getArgs()[3]))
                groupdata.get("permissions").remove(e.getArgs()[3]);

            //write back to YAML
            writeNewGroupData(groups);

            e.getTextChannel().sendMessage("Removed permission " + e.getArgs()[3] + " from " + e.getArgs()[2].toLowerCase()).queue();
            return;
        }

        //^groups list (optional group)
        if(e.getArgs()[1].equalsIgnoreCase("list"))
        {
            if(e.getArgs().length == 2)
            {
                EmbedBuilder eb = new EmbedBuilder()
                    .setTitle("Groups")
                    .setColor(GuildNetwork.getUnique_col(e.getGuild().getIdLong()));

                StringBuilder descriptionsb = new StringBuilder();
                groups.keySet().forEach(groupname ->
                {
                    descriptionsb.append(groupname + "\n");
                });
                
                try
                {
                    ScrollMenuBuilder scrollmenu = new ScrollMenuBuilder(null, descriptionsb.toString(), "\n",
                        eb, e.getTextChannel());
                    scrollmenu.getBuilder().setTimeoutSoftDestroy(5, TimeUnit.MINUTES);
                    scrollmenu.alphabetiseDescription();
                    scrollmenu.build();
                }
                catch(InvalidMenuException e1)
                {
                    e1.printStackTrace();
                }

                return;
            }

            if(!groups.containsKey(e.getArgs()[2].toLowerCase()))
            {
                e.getTextChannel().sendMessage("This group does not exist").queue();
                return;
            }

            HashMap<String, List<String>> groupdata = (HashMap<String, List<String>>) groups.get(e.getArgs()[2]);
            EmbedBuilder eb = new EmbedBuilder()
                .setTitle("Members of group " + e.getArgs()[2].toLowerCase())
                .setColor(GuildNetwork.getUnique_col(e.getGuild().getIdLong()));

            StringBuilder descriptionsb = new StringBuilder();
            for(String member : groupdata.get("members"))
                descriptionsb.append(member + "\n");

            try
            {
                ScrollMenuBuilder scrollmenu = new ScrollMenuBuilder(null, descriptionsb.toString(),
                "\n", eb, e.getTextChannel());
                scrollmenu.getBuilder().setTimeoutSoftDestroy(5, TimeUnit.MINUTES);
                scrollmenu.alphabetiseDescription();
                scrollmenu.build();
            }
            catch(InvalidMenuException e1)
            {
                e1.printStackTrace();
            }
            return;
        }  
    }

    public void writeNewGroupData(HashMap<String, Object> groups)
    {
        try
        {
            HashMap<String, Object> permconf = IOHandler.readAllYamlData(GuildNetwork.PERMISSIONS_PATH);
            permconf.put("groups", groups);
            IOHandler.writeYamlData(permconf, GuildNetwork.PERMISSIONS_PATH);
        }
        catch(IOException e1)
        {
            e1.printStackTrace();
        }
    }
}
