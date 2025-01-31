package com.github.dsipaint.AMGN.entities.plugins.intrinsic.permissions;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import org.yaml.snakeyaml.Yaml;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.CommandEvent;
import com.github.dsipaint.AMGN.entities.listeners.DefaultCommand;
import com.github.dsipaint.AMGN.io.IOHandler;

public class PermissionCommand implements Consumer<CommandEvent>
{
    public void accept(CommandEvent e)
    {
        if(e.getArgs().length < 4)
        {
            e.getTextChannel().sendMessage("Error- missing arguments\n" + DefaultCommand.PERMS.getUsage(e.getGuild())).queue();
            return;
        }

        //^permission add
        if(e.getArgs()[1].equalsIgnoreCase("add"))
        {
            //if user is not an operator but is trying to give operator perms
            if(!GuildNetwork.isOperator(e.getSender().getUser()) && e.getArgs()[3].equalsIgnoreCase("AMGN.operator"))
            {
                e.getTextChannel().sendMessage("Only operators can delegate operator permissions.").queue();
                return;
            }

            if(!e.getArgs()[2].matches("\\d{17,19}"))
            {
                e.getTextChannel().sendMessage("Invalid ID format\n" + DefaultCommand.PERMS.getUsage(e.getGuild())).queue();
                return;
            }

            //^permission add {member}
            if(GuildNetwork.fetchMember(e.getArgs()[2], e.getGuild()) != null)
            {
                //if permission does not exist for the user,
                //add it
                HashMap<String, List<String>> perms;
                try
                {
                    perms = new Yaml().load(new FileReader(new File(GuildNetwork.PERMISSIONS_PATH)));
                    perms.putIfAbsent(e.getArgs()[2], new ArrayList<String>());
                    if(!perms.get(e.getArgs()[2]).contains(e.getArgs()[3]))
                        perms.get(e.getArgs()[2]).add(e.getArgs()[3]); //add permission

                    IOHandler.writeYamlData(perms, GuildNetwork.PERMISSIONS_PATH); //write data back
                }
                catch(IOException e1)
                {
                    e1.printStackTrace();
                }

                e.getTextChannel().sendMessage("Added permission " + e.getArgs()[3] + " for " + GuildNetwork.fetchMember(e.getArgs()[2], e.getGuild()).getAsMention())
                    .setAllowedMentions(Collections.emptySet()).queue();
                return;
            }

            //^permission add {role}
            if(e.getGuild().getRoleById(e.getArgs()[2]) != null)
            {
                //if permission does not exist for the user,
                //add it
                HashMap<String, List<String>> perms;
                try
                {
                    perms = new Yaml().load(new FileReader(new File(GuildNetwork.PERMISSIONS_PATH)));
                    perms.putIfAbsent(e.getArgs()[2], new ArrayList<String>());
                    if(!perms.get(e.getArgs()[2]).contains(e.getArgs()[3]))
                        perms.get(e.getArgs()[2]).add(e.getArgs()[3]); //add permission

                    IOHandler.writeYamlData(perms, GuildNetwork.PERMISSIONS_PATH); //write data back
                }
                catch(IOException e1)
                {
                    e1.printStackTrace();
                }

                e.getTextChannel().sendMessage("Added permission " + e.getArgs()[3] + " for " + e.getGuild().getRoleById(e.getArgs()[2]).getAsMention())
                    .setAllowedMentions(Collections.emptySet()).queue();
                return;
            }

            e.getTextChannel().sendMessage("Could not find a user or a role with this ID. Check you have the right ID and try again.").queue();
            return;
        }

        //^permission remove
        if(e.getArgs()[1].equalsIgnoreCase("remove"))
        {
            if(!GuildNetwork.isOperator(e.getSender().getUser()) && e.getArgs()[3].equalsIgnoreCase("AMGN.operator"))
            {
                e.getTextChannel().sendMessage("Only operators can delegate operator permissions.").queue();
                return;
            }

            if(!e.getArgs()[2].matches("\\d{17,19}"))
            {
                e.getTextChannel().sendMessage("Invalid ID format\n" + DefaultCommand.PERMS.getUsage(e.getGuild())).queue();
                return;
            }

            //^permission remove {member}
            if(GuildNetwork.fetchMember(e.getArgs()[2], e.getGuild()) != null)
            {
                HashMap<String, List<String>> perms;
                try
                {
                    perms = new Yaml().load(new FileReader(new File(GuildNetwork.PERMISSIONS_PATH)));
                    perms.putIfAbsent(e.getArgs()[2], new ArrayList<String>());
                    if(perms.get(e.getArgs()[2]).contains(e.getArgs()[3]))
                        perms.get(e.getArgs()[2]).remove(e.getArgs()[3]); //remove permission

                    IOHandler.writeYamlData(perms, GuildNetwork.PERMISSIONS_PATH); //write data back
                }
                catch(IOException e1)
                {
                    e1.printStackTrace();
                }

                e.getTextChannel().sendMessage("Removed permission " + e.getArgs()[3] + " for " + GuildNetwork.fetchMember(e.getArgs()[2], e.getGuild()).getAsMention())
                    .setAllowedMentions(Collections.emptySet()).queue();
                return;
            }

            //^permission remove {role}
            if(e.getGuild().getRoleById(e.getArgs()[2]) != null)
            {
                HashMap<String, List<String>> perms;
                try
                {
                    perms = new Yaml().load(new FileReader(new File(GuildNetwork.PERMISSIONS_PATH)));
                    perms.putIfAbsent(e.getArgs()[2], new ArrayList<String>());
                    if(perms.get(e.getArgs()[2]).contains(e.getArgs()[3]))
                        perms.get(e.getArgs()[2]).remove(e.getArgs()[3]); //add permission

                    IOHandler.writeYamlData(perms, GuildNetwork.PERMISSIONS_PATH); //write data back
                }
                catch(IOException e1)
                {
                    e1.printStackTrace();
                }

                e.getTextChannel().sendMessage("Removed permission " + e.getArgs()[3] + " for " + e.getGuild().getRoleById(e.getArgs()[2]).getAsMention())
                    .setAllowedMentions(Collections.emptySet()).queue();
                return;
            }

            e.getTextChannel().sendMessage("Could not find a user or a role with this ID. Check you have the right ID and try again.").queue();
            return;
        }
    }    
}
