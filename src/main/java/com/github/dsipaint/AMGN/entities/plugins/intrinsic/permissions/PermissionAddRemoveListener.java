package com.github.dsipaint.AMGN.entities.plugins.intrinsic.permissions;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.yaml.snakeyaml.Yaml;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.DefaultCommand;
import com.github.dsipaint.AMGN.io.IOHandler;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PermissionAddRemoveListener extends ListenerAdapter
{
    public void onMessageReceived(MessageReceivedEvent e)
    {
        if(!e.isFromGuild())
            return;

        if(!DefaultCommand.LISTPERMS.hasPermission(e.getMember()))
            return;

        String msg = e.getMessage().getContentRaw();
        String[] args = msg.split(" ");

        if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + DefaultCommand.PERMS.getLabel()))
		{
            if(args.length < 4)
            {
                e.getChannel().sendMessage("Error- missing arguments\n" + DefaultCommand.PERMS.getUsage()).queue();
                return;
            }

            //^permission add
            if(args[1].equalsIgnoreCase("add"))
            {
                //if user is not an operator but is trying to give operator perms
                if(!GuildNetwork.isOperator(e.getAuthor()) && args[3].equalsIgnoreCase("AMGN.operator"))
                {
                    e.getChannel().sendMessage("Only operators can delegate operator permissions.").queue();
                    return;
                }

                if(!args[2].matches("\\d{17,19}"))
                {
                    e.getChannel().sendMessage("Invalid ID format\n" + DefaultCommand.PERMS.getUsage()).queue();
                    return;
                }

                //^permission add {member}
                if(e.getGuild().getMemberById(args[2]) != null)
                {
                    //if permission does not exist for the user,
                    //add it
                    HashMap<String, List<String>> perms;
                    try
                    {
                        perms = new Yaml().load(new FileReader(new File(GuildNetwork.PERMISSIONS_PATH)));
                        perms.putIfAbsent(args[2], new ArrayList<String>());
                        if(!perms.get(args[2]).contains(args[3]))
                            perms.get(args[2]).add(args[3]); //add permission

                        IOHandler.writeYamlData(perms, GuildNetwork.PERMISSIONS_PATH); //write data back
                    }
                    catch(IOException e1)
                    {
                        e1.printStackTrace();
                    }

                    e.getChannel().sendMessage("Added permission " + args[3] + " for " + e.getGuild().getMemberById(args[2]).getAsMention())
                        .setAllowedMentions(Collections.emptySet()).queue();
                    return;
                }

                //^permission add {role}
                if(e.getGuild().getRoleById(args[2]) != null)
                {
                    //if permission does not exist for the user,
                    //add it
                    HashMap<String, List<String>> perms;
                    try
                    {
                        perms = new Yaml().load(new FileReader(new File(GuildNetwork.PERMISSIONS_PATH)));
                        perms.putIfAbsent(args[2], new ArrayList<String>());
                        if(!perms.get(args[2]).contains(args[3]))
                            perms.get(args[2]).add(args[3]); //add permission

                        IOHandler.writeYamlData(perms, GuildNetwork.PERMISSIONS_PATH); //write data back
                    }
                    catch(IOException e1)
                    {
                        e1.printStackTrace();
                    }

                    e.getChannel().sendMessage("Added permission " + args[3] + " for " + e.getGuild().getRoleById(args[2]).getAsMention())
                        .setAllowedMentions(Collections.emptySet()).queue();
                    return;
                }

                e.getChannel().sendMessage("Could not find a user or a role with this ID. Check you have the right ID and try again.").queue();
                return;
            }

            //^permission remove
            if(args[1].equalsIgnoreCase("remove"))
            {
                if(!GuildNetwork.isOperator(e.getAuthor()) && args[3].equalsIgnoreCase("AMGN.operator"))
                {
                    e.getChannel().sendMessage("Only operators can delegate operator permissions.").queue();
                    return;
                }

                if(!args[2].matches("\\d{17,19}"))
                {
                    e.getChannel().sendMessage("Invalid ID format\n" + DefaultCommand.PERMS.getUsage()).queue();
                    return;
                }

                //^permission remove {member}
                if(e.getGuild().getMemberById(args[2]) != null)
                {
                    HashMap<String, List<String>> perms;
                    try
                    {
                        perms = new Yaml().load(new FileReader(new File(GuildNetwork.PERMISSIONS_PATH)));
                        perms.putIfAbsent(args[2], new ArrayList<String>());
                        if(perms.get(args[2]).contains(args[3]))
                            perms.get(args[2]).remove(args[3]); //remove permission

                        IOHandler.writeYamlData(perms, GuildNetwork.PERMISSIONS_PATH); //write data back
                    }
                    catch(IOException e1)
                    {
                        e1.printStackTrace();
                    }

                    e.getChannel().sendMessage("Removed permission " + args[3] + " for " + e.getGuild().getMemberById(args[2]).getAsMention())
                        .setAllowedMentions(Collections.emptySet()).queue();
                    return;
                }

                //^permission remove {role}
                if(e.getGuild().getRoleById(args[2]) != null)
                {
                    HashMap<String, List<String>> perms;
                    try
                    {
                        perms = new Yaml().load(new FileReader(new File(GuildNetwork.PERMISSIONS_PATH)));
                        perms.putIfAbsent(args[2], new ArrayList<String>());
                        if(perms.get(args[2]).contains(args[3]))
                            perms.get(args[2]).remove(args[3]); //add permission

                        IOHandler.writeYamlData(perms, GuildNetwork.PERMISSIONS_PATH); //write data back
                    }
                    catch(IOException e1)
                    {
                        e1.printStackTrace();
                    }

                    e.getChannel().sendMessage("Removed permission " + args[3] + " for " + e.getGuild().getRoleById(args[2]).getAsMention())
                        .setAllowedMentions(Collections.emptySet()).queue();
                    return;
                }

                e.getChannel().sendMessage("Could not find a user or a role with this ID. Check you have the right ID and try again.").queue();
                return;
            }
        }
    }    
}
