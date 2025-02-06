package com.github.dsipaint.AMGN.entities.plugins.intrinsic.permissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.yaml.snakeyaml.Yaml;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.CommandEvent;
import com.github.dsipaint.AMGN.entities.listeners.managed.menu.ScrollMenuBuilder;
import com.github.dsipaint.AMGN.entities.listeners.managed.menu.MenuBuilder.InvalidMenuException;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.io.IOHandler;
import com.github.dsipaint.AMGN.io.Permissions;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;

public class ListPermissionsCommand implements Consumer<CommandEvent>
{
    @SuppressWarnings("unchecked")
    public void accept(CommandEvent e)
    {
        //^listpermissions
        if(e.getArgs().length == 1)
        {
            EmbedBuilder eb = new EmbedBuilder()
                .setTitle("Permissions for " + AMGN.bot.getSelfUser().getName())
                .setColor(GuildNetwork.getUnique_col(e.getGuild().getIdLong()))
                .setDescription("AMGN.operator"
                + "\nAMGN.commands.*"
                + "\nAMGN.commands.controlplugins.*"
                + "\nAMGN.commands.help"
                + "\nAMGN.commands.updatemetainfo"
                + "\nAMGN.commands.viewmetainfo"
                + "\nAMGN.commands.showplugins"
                + "\nAMGN.commands.enable"
                + "\nAMGN.commands.disable"
                + "\nAMGN.commands.reload"
                + "\nAMGN.commands.reloadall"
                + "\nAMGN.commands.closenetwork"
                + "\nAMGN.commands.whitelist"
                + "\nAMGN.commands.blacklist"
                + "\nAMGN.commands.showallplugins"
                + "\nAMGN.commands.permission"
                + "\nAMGN.commands.listpermissions"
                + "\n");

            e.getTextChannel().sendMessageEmbeds(eb.build()).queue();
            return;
        }

        //^listpermissions {id} (show all permissions for a role or user)
        if(e.getArgs()[1].matches("\\d{17,19}"))
        {
            boolean isUserId = GuildNetwork.resolveEntity(e.getArgs()[1]) instanceof UserSnowflake;
            
            HashMap<String, List<String>> perms;
            try
            {
                perms = new Yaml().load(new FileReader(new File(GuildNetwork.PERMISSIONS_PATH)));                

                StringBuilder listperms = new StringBuilder();

                //first display perms specifically assigned to this ID
                for(String perm : perms.getOrDefault(e.getArgs()[1], new ArrayList<String>()))
                    listperms.append(perm + "\n");

                //then if this is a user ID, find perms inherited by having a role with the perms
                if(isUserId)
                {
                    perms.keySet().forEach(id ->
                    {
                        if(id.equalsIgnoreCase("groups"))
                            return;

                        //if this is a role and the user has this role
                        Role r = AMGN.bot.getRoleById(id);
                        if(r != null
                            && r.getGuild().retrieveMemberById(e.getArgs()[1]).complete()
                                .getRoles().contains(r))
                        {
                            //append these permissions
                            ((List<String>) perms.get(id)).forEach(perm ->
                            {
                                listperms.append(perm + " *(inherited from role: \"" + r.getName() + "\" in guild: \"" + r.getGuild().getName() + "\")*\n");
                            });
                        }
                    });
                }

                //finally display any perms inherited from a group (remember groups can reference users and roles a user may have)
                HashMap<String, Object> groups = (HashMap<String, Object>) perms.get("groups");
                groups.forEach((groupname, group) ->
                {
                    HashMap<String, List<String>> parsed_group = (HashMap<String, List<String>>) group;
                    //check if parsed group contains an id associated to our id
                    if(Permissions.isInGroup(e.getArgs()[1], e.getGuild(), groupname))
                    {
                        parsed_group.get("permissions").forEach(perm ->
                        {
                            listperms.append(perm + " *(inherited from group " + groupname + ")*");
                        });
                    }
                    for(String member : parsed_group.get("members"))
                    {

                        //if our ID is a role id and matches that one
                        if(!isUserId && member.equals(e.getArgs()[1]))
                        {
                            parsed_group.get("permissions").forEach(perm ->
                            {
                                listperms.append(perm + " *(inherited from group " + groupname + ")*");
                            });
                            break; //break because we only need to record perms once per group
                        }
                    }
                });


                String name =
                    (
                        isUserId ? 
                        e.getGuild().retrieveMemberById(e.getArgs()[1]).complete().getUser().getName()
                        : 
                        (e.getGuild().getRoleById(e.getArgs()[1]) != null ?
                            e.getGuild().getRoleById(e.getArgs()[1]).getName()
                            : 
                            e.getArgs()[1]
                        )
                    );

                //build out the menu of results
                EmbedBuilder template = returnPartiallyFormattedEmbedList(name, e.getGuild().getIdLong());
                
                ScrollMenuBuilder scrollmenu = new ScrollMenuBuilder(null, listperms.toString(), "\n",
                    template, e.getTextChannel());
                
                scrollmenu.getBuilder().setTimeoutSoftDestroy(5, TimeUnit.MINUTES);
                scrollmenu.alphabetiseDescription();
                scrollmenu.build();
            }
            catch(IOException | InvalidMenuException e1)
            {
                e1.printStackTrace();
            }

            return;
        }

        //^listpermissions {plugin/group}
        StringBuilder listperms = new StringBuilder();
        for(Plugin p : AMGN.plugin_listeners.keySet())
        {
            if(p.getName().equalsIgnoreCase(e.getArgs()[1]))
            {
                for(String perm : p.getPerms())
                    listperms.append(perm + "\n");

                try
                {
                    //build out the menu of results
                    EmbedBuilder template = returnPartiallyFormattedEmbedList(p.getName(), e.getGuild().getIdLong());
                    
                    ScrollMenuBuilder scrollmenu = new ScrollMenuBuilder(null, listperms.toString(), "\n",
                        template, e.getTextChannel());
                    
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

        //list permissions for a group
        try
        {
            HashMap<String, Object> groups = (HashMap<String, Object>) IOHandler.readYamlData(GuildNetwork.PERMISSIONS_PATH, "groups");
            for(String name : groups.keySet())
            {
                if(name.equalsIgnoreCase(e.getArgs()[1]))
                {
                    HashMap<String, List<String>> groupdata = (HashMap<String, List<String>>) groups.get(name);
                    for(String perm : groupdata.get("permissions"))
                        listperms.append(perm + "\n");

                    //build out the menu of results
                    EmbedBuilder template = returnPartiallyFormattedEmbedList(name, e.getGuild().getIdLong());
                    
                    ScrollMenuBuilder scrollmenu = new ScrollMenuBuilder(null, listperms.toString(), "\n",
                        template, e.getTextChannel());

                    scrollmenu.getBuilder().setTimeoutSoftDestroy(5, TimeUnit.MINUTES);
                    scrollmenu.alphabetiseDescription();
                    scrollmenu.build();
                }
            }
        }
        catch (FileNotFoundException | InvalidMenuException e1)
        {
            e1.printStackTrace();
        }
    }

    private EmbedBuilder returnPartiallyFormattedEmbedList(String name, long guild)
    {
        return new EmbedBuilder()
            .setTitle("Permissions for " + name)
            .setColor(GuildNetwork.getUnique_col(guild));
    }
}
