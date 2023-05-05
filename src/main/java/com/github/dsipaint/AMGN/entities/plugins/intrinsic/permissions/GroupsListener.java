package com.github.dsipaint.AMGN.entities.plugins.intrinsic.permissions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.DefaultCommand;
import com.github.dsipaint.AMGN.io.IOHandler;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GroupsListener extends ListenerAdapter
{
    @SuppressWarnings("unchecked")
    public void onMessageReceived(MessageReceivedEvent e)
    {
        if(!e.isFromGuild())
            return;

        if(!DefaultCommand.GROUPS.hasPermission(e.getMember()))
            return;

        String msg = e.getMessage().getContentRaw();
        String[] args = msg.split(" ");

        if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(e.getGuild().getIdLong()) + DefaultCommand.GROUPS.getLabel()))
		{
            if(args.length == 1)
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

            if(args[1].equalsIgnoreCase("create"))
            {
                if(args.length < 3)
                    return;

                //if group does not already exist then create
                if(groups.containsKey(args[2].toLowerCase()))
                {
                    e.getChannel().sendMessage("This group already exists").queue();
                    return;
                }

                HashMap<String, List<String>> groupdata = new HashMap<String, List<String>>();
                groupdata.put("members", new ArrayList<String>());
                groupdata.put("permissions", new ArrayList<String>());
                
                groups.put(args[2].toLowerCase(), groupdata);

                //write back to YAML
                writeNewGroupData(groups);

                e.getChannel().sendMessage("Created new group " + args[2].toLowerCase()).queue();
                return;
            }

            if(args[1].equalsIgnoreCase("destroy"))
            {
                if(args.length < 3)
                    return;

                if(!groups.containsKey(args[2].toLowerCase()))
                {
                    e.getChannel().sendMessage("This group does not exist").queue();
                    return;
                }

                groups.remove(args[2].toLowerCase());
                //write back to YAML
                writeNewGroupData(groups);
                e.getChannel().sendMessage("Group " + args[2].toLowerCase() + " has been destroyed.").queue();
                return;
            }

            //^group add group id
            if(args[1].equalsIgnoreCase("add"))
            {
                if(args.length < 4)
                    return;

                if(!groups.containsKey(args[2].toLowerCase()))
                {
                    e.getChannel().sendMessage("This group does not exist").queue();
                    return;
                }

                if(args.length < 4)
                    return;

                HashMap<String, List<String>> groupdata = (HashMap<String, List<String>>) groups.get(args[2]);
                if(!groupdata.get("members").contains(args[3]))
                    groupdata.get("members").add(args[3]);

                //write back to YAML
                writeNewGroupData(groups);

                e.getChannel().sendMessage("Added " + args[3] + " to " + args[2].toLowerCase()).queue();

                return;
            }

            if(args[1].equalsIgnoreCase("remove"))
            {
                if(args.length < 4)
                    return;

                if(!groups.containsKey(args[2].toLowerCase()))
                {
                    e.getChannel().sendMessage("This group does not exist").queue();
                    return;
                }

                if(args.length < 4)
                    return;

                HashMap<String, List<String>> groupdata = (HashMap<String, List<String>>) groups.get(args[2]);
                if(groupdata.get("members").contains(args[3]))
                    groupdata.get("members").remove(args[3]);

                //write back to YAML
                writeNewGroupData(groups);
                
                e.getChannel().sendMessage("Removed " + args[3] + " from " + args[2].toLowerCase()).queue();

                return;
            }

            //^groups addperm group perm
            if(args[1].equalsIgnoreCase("addperm"))
            {
                if(args.length < 4)
                    return;

                if(!groups.containsKey(args[2].toLowerCase()))
                {
                    e.getChannel().sendMessage("This group does not exist").queue();
                    return;
                }

                if(args.length < 4)
                    return;

                HashMap<String, List<String>> groupdata = (HashMap<String, List<String>>) groups.get(args[2]);
                if(!groupdata.get("permissions").contains(args[3]))
                    groupdata.get("permissions").add(args[3]);

                //write back to YAML
                writeNewGroupData(groups);

                e.getChannel().sendMessage("Added permission " + args[3] + " to " + args[2].toLowerCase()).queue();
                return;
            }

            //^groups removeperm group perm
            if(args[1].equalsIgnoreCase("removeperm"))
            {
                if(args.length < 4)
                    return;

                if(!groups.containsKey(args[2].toLowerCase()))
                {
                    e.getChannel().sendMessage("This group does not exist").queue();
                    return;
                }

                if(args.length < 4)
                    return;

                HashMap<String, List<String>> groupdata = (HashMap<String, List<String>>) groups.get(args[2]);
                if(groupdata.get("permissions").contains(args[3]))
                    groupdata.get("permissions").remove(args[3]);

                //write back to YAML
                writeNewGroupData(groups);

                e.getChannel().sendMessage("Removed permission " + args[3] + " from " + args[2].toLowerCase()).queue();
                return;
            }

            //^groups list (optional group)
            if(args[1].equalsIgnoreCase("list"))
            {
                if(args.length == 2)
                {
                    EmbedBuilder eb = new EmbedBuilder()
                        .setTitle("Groups")
                        .setColor(GuildNetwork.getUnique_col(e.getGuild().getIdLong()));

                    groups.keySet().forEach(groupname ->
                    {
                        eb.appendDescription(groupname + "\n");
                    });

                    e.getChannel().sendMessageEmbeds(eb.build()).queue();
                    return;
                }

                if(!groups.containsKey(args[2].toLowerCase()))
                {
                    e.getChannel().sendMessage("This group does not exist").queue();
                    return;
                }

                HashMap<String, List<String>> groupdata = (HashMap<String, List<String>>) groups.get(args[2]);
                EmbedBuilder eb = new EmbedBuilder()
                    .setTitle("Members of group " + args[2].toLowerCase())
                    .setColor(GuildNetwork.getUnique_col(e.getGuild().getIdLong()));
                for(String member : groupdata.get("members"))
                    eb.appendDescription(member + "\n");

                e.getChannel().sendMessageEmbeds(eb.build()).queue();
                return;
            }                 
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
