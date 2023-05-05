package com.github.dsipaint.AMGN.entities.plugins.intrinsic.permissions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.yaml.snakeyaml.Yaml;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.DefaultCommand;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;
import com.github.dsipaint.AMGN.io.IOHandler;
import com.github.dsipaint.AMGN.io.Permissions;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ListPermissionsListener extends ListenerAdapter
{
    @SuppressWarnings("unchecked")
    public void onMessageReceived(MessageReceivedEvent e)
    {
        if(!e.isFromGuild())
            return;

        if(!DefaultCommand.LISTPERMS.hasPermission(e.getMember()))
            return;

        String msg = e.getMessage().getContentRaw();
        String[] args = msg.split(" ");

        if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + DefaultCommand.LISTPERMS.getLabel()))
		{
            //^listpermissions
            if(args.length == 1)
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

                e.getChannel().sendMessageEmbeds(eb.build()).queue();
                return;
            }

            //^listpermissions {id} (show all permissions for a role or user)
            if(args[1].matches("\\d{17,19}"))
            {
                HashMap<String, List<String>> perms;
                try
                {
                    perms = new Yaml().load(new FileReader(new File(GuildNetwork.PERMISSIONS_PATH)));
                    EmbedBuilder eb = new EmbedBuilder()
                        .setTitle("Permissions for " + 
                            (e.getGuild().getMemberById(args[1]) != null ? 
                                e.getGuild().getMemberById(args[1]).getUser().getAsTag()
                                : 
                                (e.getGuild().getRoleById(args[1]) != null ?
                                    e.getGuild().getRoleById(args[1]).getName()
                                    : 
                                    args[1]
                                )
                            )
                        )
                        .setColor(GuildNetwork.getUnique_col(e.getGuild().getIdLong()));
                    

                    LinkedList<String> listperms = new LinkedList<String>();

                    //first display perms specifically assigned to this ID
                    for(String perm : perms.getOrDefault(args[1], new ArrayList<String>()))
                        listperms.add(perm + "\n");

                    boolean isUserId = AMGN.bot.getUserById(args[1]) != null;
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
                                && r.getGuild().getMembersWithRoles(r)
                                    .contains(r.getGuild().getMemberById(args[1])))
                            {
                                //append these permissions
                                ((List<String>) perms.get(id)).forEach(perm ->
                                {
                                    listperms.add(perm + " *(inherited from role: \"" + r.getName() + "\" in guild: \"" + r.getGuild().getName() + "\")*\n");
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
                        if(Permissions.isInGroup(args[1], groupname))
                        {
                            parsed_group.get("permissions").forEach(perm ->
                            {
                                listperms.add(perm + " *(inherited from group " + groupname + ")*");
                            });
                        }
                        for(String member : parsed_group.get("members"))
                        {

                            //if our ID is a role id and matches that one
                            if(!isUserId && member.equals(args[1]))
                            {
                                parsed_group.get("permissions").forEach(perm ->
                                {
                                    listperms.add(perm + " *(inherited from group " + groupname + ")*");
                                });
                                break; //break because we only need to record perms once per group
                            }
                        }
                    });


                    //iterate through listperms and send multiple embeds
                    while(!listperms.isEmpty())
                    {
                        String line = listperms.pop();
                        if(eb.build().getDescription() == null || eb.build().getDescription().length() + line.length() < MessageEmbed.DESCRIPTION_MAX_LENGTH)
                            eb.appendDescription(line);
                        else
                        {
                            e.getChannel().sendMessageEmbeds(eb.build()).queue();
                            eb.setDescription(line);
                        }
                    }

                    e.getChannel().sendMessageEmbeds(eb.build()).queue();
                }
                catch(IOException e1)
                {
                    e1.printStackTrace();
                }

                return;
            }

            //^listpermissions {plugin/group}
            LinkedList<String> listperms = new LinkedList<String>();
            for(Plugin p : AMGN.plugin_listeners.keySet())
            {
                if(p.getName().equalsIgnoreCase(args[1]))
                {
                    EmbedBuilder eb = new EmbedBuilder()
                        .setTitle("Permissions for " + p.getName())
                        .setColor(GuildNetwork.getUnique_col(e.getGuild().getIdLong()))
                        .setFooter(p.getName() + " " + p.getVersion());

                    for(String perm : p.getPerms())
                        listperms.add(perm + "\n");

                    while(!listperms.isEmpty())
                    {
                        String line = listperms.pop();
                        if(eb.build().getDescription() == null || eb.build().getDescription().length() + line.length() < MessageEmbed.DESCRIPTION_MAX_LENGTH)
                            eb.appendDescription(line);
                        else
                        {
                            e.getChannel().sendMessageEmbeds(eb.build()).queue();
                            eb.setDescription(line);
                        }
                    }

                    e.getChannel().sendMessageEmbeds(eb.build()).queue();
                    return;
                }   
            }

            //list permissions for a group
            try
            {
                HashMap<String, Object> groups = (HashMap<String, Object>) IOHandler.readYamlData(GuildNetwork.PERMISSIONS_PATH, "groups");
                for(String name : groups.keySet())
                {
                    if(name.equalsIgnoreCase(args[1]))
                    {
                        EmbedBuilder eb = new EmbedBuilder()
                            .setTitle("Permissions for " + name)
                            .setColor(GuildNetwork.getUnique_col(e.getGuild().getIdLong()));

                        HashMap<String, List<String>> groupdata = (HashMap<String, List<String>>) groups.get(name);
                        for(String perm : groupdata.get("permissions"))
                            listperms.add(perm + "\n");

                        while(!listperms.isEmpty())
                        {
                            String line = listperms.pop();
                            if(eb.build().getDescription() == null || eb.build().getDescription().length() + line.length() < MessageEmbed.DESCRIPTION_MAX_LENGTH)
                                eb.appendDescription(line);
                            else
                            {
                                e.getChannel().sendMessageEmbeds(eb.build()).queue();
                                eb.setDescription(line);
                            }
                        }

                        e.getChannel().sendMessageEmbeds(eb.build()).queue();
                    }
                }
            }
            catch (FileNotFoundException e1)
            {
                e1.printStackTrace();
            }
        }
    }    
}
