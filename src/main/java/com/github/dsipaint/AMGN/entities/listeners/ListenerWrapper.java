package com.github.dsipaint.AMGN.entities.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.ExceptionEvent;
import net.dv8tion.jda.api.events.GatewayPingEvent;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.RawGatewayEvent;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.events.UpdateEvent;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.ChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.GenericChannelEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateArchiveTimestampEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateArchivedEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateAutoArchiveDurationEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateBitrateEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateInvitableEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateLockedEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateNSFWEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateNameEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateParentEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdatePositionEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateRegionEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateSlowmodeEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateTopicEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateTypeEvent;
import net.dv8tion.jda.api.events.channel.update.ChannelUpdateUserLimitEvent;
import net.dv8tion.jda.api.events.channel.update.GenericChannelUpdateEvent;
import net.dv8tion.jda.api.events.emoji.EmojiAddedEvent;
import net.dv8tion.jda.api.events.emoji.EmojiRemovedEvent;
import net.dv8tion.jda.api.events.emoji.GenericEmojiEvent;
import net.dv8tion.jda.api.events.emoji.update.EmojiUpdateNameEvent;
import net.dv8tion.jda.api.events.emoji.update.EmojiUpdateRolesEvent;
import net.dv8tion.jda.api.events.emoji.update.GenericEmojiUpdateEvent;
import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
import net.dv8tion.jda.api.events.guild.GuildAvailableEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildTimeoutEvent;
import net.dv8tion.jda.api.events.guild.GuildUnavailableEvent;
import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildJoinedEvent;
import net.dv8tion.jda.api.events.guild.UnavailableGuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.invite.GenericGuildInviteEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.GenericGuildMemberEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent;
import net.dv8tion.jda.api.events.guild.member.update.GenericGuildMemberUpdateEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateAvatarEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdatePendingEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateTimeOutEvent;
import net.dv8tion.jda.api.events.guild.override.GenericPermissionOverrideEvent;
import net.dv8tion.jda.api.events.guild.override.PermissionOverrideCreateEvent;
import net.dv8tion.jda.api.events.guild.override.PermissionOverrideDeleteEvent;
import net.dv8tion.jda.api.events.guild.override.PermissionOverrideUpdateEvent;
import net.dv8tion.jda.api.events.guild.update.GenericGuildUpdateEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateAfkChannelEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateAfkTimeoutEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBannerEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostCountEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostTierEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateCommunityUpdatesChannelEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateDescriptionEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateExplicitContentLevelEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateFeaturesEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateIconEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateLocaleEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateMFALevelEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateMaxMembersEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateMaxPresencesEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNSFWLevelEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNotificationLevelEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateOwnerEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateRulesChannelEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateSplashEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateSystemChannelEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateVanityCodeEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateVerificationLevelEvent;
import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildMuteEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMuteEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceRequestToSpeakEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceSelfDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceSelfMuteEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceStreamEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceSuppressEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceVideoEvent;
import net.dv8tion.jda.api.events.http.HttpRequestEvent;
import net.dv8tion.jda.api.events.interaction.GenericAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.GenericContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.MessageContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericSelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageEmbedEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEmojiEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.role.GenericRoleEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.GenericRoleUpdateEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateColorEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateHoistedEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateIconEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateMentionableEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdatePermissionsEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdatePositionEvent;
import net.dv8tion.jda.api.events.self.GenericSelfUpdateEvent;
import net.dv8tion.jda.api.events.self.SelfUpdateAvatarEvent;
import net.dv8tion.jda.api.events.self.SelfUpdateMFAEvent;
import net.dv8tion.jda.api.events.self.SelfUpdateNameEvent;
import net.dv8tion.jda.api.events.self.SelfUpdateVerifiedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.session.SessionDisconnectEvent;
import net.dv8tion.jda.api.events.session.SessionRecreateEvent;
import net.dv8tion.jda.api.events.session.SessionResumeEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.events.stage.GenericStageInstanceEvent;
import net.dv8tion.jda.api.events.stage.StageInstanceCreateEvent;
import net.dv8tion.jda.api.events.stage.StageInstanceDeleteEvent;
import net.dv8tion.jda.api.events.stage.update.GenericStageInstanceUpdateEvent;
import net.dv8tion.jda.api.events.stage.update.StageInstanceUpdatePrivacyLevelEvent;
import net.dv8tion.jda.api.events.stage.update.StageInstanceUpdateTopicEvent;
import net.dv8tion.jda.api.events.thread.GenericThreadEvent;
import net.dv8tion.jda.api.events.thread.ThreadHiddenEvent;
import net.dv8tion.jda.api.events.thread.ThreadRevealedEvent;
import net.dv8tion.jda.api.events.thread.member.GenericThreadMemberEvent;
import net.dv8tion.jda.api.events.thread.member.ThreadMemberJoinEvent;
import net.dv8tion.jda.api.events.thread.member.ThreadMemberLeaveEvent;
import net.dv8tion.jda.api.events.user.GenericUserEvent;
import net.dv8tion.jda.api.events.user.UserActivityEndEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.events.user.UserTypingEvent;
import net.dv8tion.jda.api.events.user.update.GenericUserPresenceEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateActivitiesEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateActivityOrderEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateAvatarEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateDiscriminatorEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateFlagsEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ListenerWrapper extends ListenerAdapter
{
    //this class provides a wrapper where we can apply blacklist/whitelisting rules
    //before we choose to pass the event to a plugin's listener (if the guild has whitelisted this plugin/listener)

    @Override
    public final void onButtonInteraction(ButtonInteractionEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onButtonInteraction(event);});
            });
            return;
        }

        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onButtonInteraction(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onChannelCreate(ChannelCreateEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onChannelCreate(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onChannelCreate(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onChannelDelete(ChannelDeleteEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onChannelDelete(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onChannelDelete(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onChannelUpdateArchived(ChannelUpdateArchivedEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onChannelUpdateArchived(event);});
            });
            return;
        }

        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onChannelUpdateArchived(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onChannelUpdateArchiveTimestamp(ChannelUpdateArchiveTimestampEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onChannelUpdateArchiveTimestamp(event);});
            });
            return;
        }

        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onChannelUpdateArchiveTimestamp(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onChannelUpdateAutoArchiveDuration(ChannelUpdateAutoArchiveDurationEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onChannelUpdateAutoArchiveDuration(event);});
            });
            return;
        }

        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onChannelUpdateAutoArchiveDuration(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onChannelUpdateBitrate(ChannelUpdateBitrateEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onChannelUpdateBitrate(event);});
            });
            return;
        }

        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onChannelUpdateBitrate(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onChannelUpdateInvitable(ChannelUpdateInvitableEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onChannelUpdateInvitable(event);});
            });
            return;
        }

        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onChannelUpdateInvitable(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onChannelUpdateLocked(ChannelUpdateLockedEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onChannelUpdateLocked(event);});
            });
            return;
        }

        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onChannelUpdateLocked(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onChannelUpdateName(ChannelUpdateNameEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onChannelUpdateName(event);});
            });
            return;
        }

        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onChannelUpdateName(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onChannelUpdateNSFW(ChannelUpdateNSFWEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onChannelUpdateNSFW(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onChannelUpdateNSFW(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onChannelUpdateParent(ChannelUpdateParentEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onChannelUpdateParent(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onChannelUpdateParent(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onChannelUpdatePosition(ChannelUpdatePositionEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onChannelUpdatePosition(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onChannelUpdatePosition(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onChannelUpdateRegion(ChannelUpdateRegionEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onChannelUpdateRegion(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onChannelUpdateRegion(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onChannelUpdateSlowmode(ChannelUpdateSlowmodeEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onChannelUpdateSlowmode(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onChannelUpdateSlowmode(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onChannelUpdateTopic(ChannelUpdateTopicEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onChannelUpdateTopic(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onChannelUpdateTopic(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onChannelUpdateType(ChannelUpdateTypeEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onChannelUpdateType(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onChannelUpdateType(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onChannelUpdateUserLimit(ChannelUpdateUserLimitEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onChannelUpdateUserLimit(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onChannelUpdateUserLimit(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onCommandAutoCompleteInteraction(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onCommandAutoCompleteInteraction(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onSessionDisconnect(SessionDisconnectEvent event)
    { 
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onSessionDisconnect(event);});
        });
    }
     
    @Override
    public final void onEmojiAdded(EmojiAddedEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onEmojiAdded(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onEmojiRemoved(EmojiRemovedEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onEmojiRemoved(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onEmojiUpdateName(EmojiUpdateNameEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onEmojiUpdateName(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onEmojiUpdateRoles(EmojiUpdateRolesEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onEmojiUpdateRoles(event);
                    });
                }
            });
        });
    }
    
    @Override
    public final void onException(ExceptionEvent event)
    {
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onException(event);});
        });
    }
     
    @Override
    public final void onGatewayPing(GatewayPingEvent event)
    {
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onGatewayPing(event);});
        });
    }
     
    @Override
    public final void onGenericAutoCompleteInteraction(GenericAutoCompleteInteractionEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onGenericAutoCompleteInteraction(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericAutoCompleteInteraction(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericChannel(GenericChannelEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onGenericChannel(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericChannel(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericChannelUpdate(GenericChannelUpdateEvent<?> event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onGenericChannelUpdate(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericChannelUpdate(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericCommandInteraction(GenericCommandInteractionEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onGenericCommandInteraction(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericCommandInteraction(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericComponentInteractionCreate(GenericComponentInteractionCreateEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onGenericComponentInteractionCreate(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericComponentInteractionCreate(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericContextInteraction(GenericContextInteractionEvent<?> event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onGenericContextInteraction(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericContextInteraction(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericEmoji(GenericEmojiEvent event)
    {   
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericEmoji(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericEmojiUpdate(GenericEmojiUpdateEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericEmojiUpdate(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericEvent(GenericEvent event)
    {
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onGenericEvent(event);});
        });
    }
     
    @Override
    public final void onGenericGuild(GenericGuildEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericGuild(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericGuildInvite(GenericGuildInviteEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericGuildInvite(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericGuildMember(GenericGuildMemberEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericGuildMember(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericGuildMemberUpdate(GenericGuildMemberUpdateEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericGuildMemberUpdate(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericGuildUpdate(GenericGuildUpdateEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericGuildUpdate(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericGuildVoice(GenericGuildVoiceEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericGuildVoice(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericInteractionCreate(GenericInteractionCreateEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onGenericInteractionCreate(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericInteractionCreate(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericMessage(GenericMessageEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onGenericMessage(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericMessage(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericMessageReaction(GenericMessageReactionEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onGenericMessageReaction(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericMessageReaction(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericPermissionOverride(GenericPermissionOverrideEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericPermissionOverride(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericRole(GenericRoleEvent event)
    {   
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericRole(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericRoleUpdate(GenericRoleUpdateEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericRoleUpdate(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericSelfUpdate(GenericSelfUpdateEvent event)
    {
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onGenericSelfUpdate(event);});
        });
    }
     
    @Override
    public final void onGenericStageInstance(GenericStageInstanceEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericStageInstance(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericStageInstanceUpdate(GenericStageInstanceUpdateEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericStageInstanceUpdate(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericThread(GenericThreadEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericThread(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericThreadMember(GenericThreadMemberEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericThreadMember(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericUpdate(UpdateEvent<?,?> event)
    {
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onGenericUpdate(event);});
        });
    }
     
    @Override
    public final void onGenericUser(GenericUserEvent event)
    {
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onGenericUser(event);});
        });
    }
     
    @Override
    public final void onGenericUserPresence(GenericUserPresenceEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericUserPresence(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildAvailable(GuildAvailableEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildAvailable(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildBan(GuildBanEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildBan(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildInviteCreate(GuildInviteCreateEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildInviteCreate(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildInviteDelete(GuildInviteDeleteEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildInviteDelete(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildJoin(GuildJoinEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildJoin(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildLeave(GuildLeaveEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildLeave(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildMemberJoin(GuildMemberJoinEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildMemberJoin(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildMemberRemove(GuildMemberRemoveEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildMemberRemove(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildMemberRoleAdd(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildMemberRoleRemove(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildMemberUpdate(GuildMemberUpdateEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildMemberUpdate(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildMemberUpdateAvatar(GuildMemberUpdateAvatarEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildMemberUpdateAvatar(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildMemberUpdateBoostTime(GuildMemberUpdateBoostTimeEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildMemberUpdateBoostTime(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildMemberUpdateNickname(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildMemberUpdatePending(GuildMemberUpdatePendingEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildMemberUpdatePending(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildMemberUpdateTimeOut(GuildMemberUpdateTimeOutEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildMemberUpdateTimeOut(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildReady(GuildReadyEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildReady(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildTimeout(GuildTimeoutEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuildId());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildTimeout(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUnavailable(GuildUnavailableEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUnavailable(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUnban(GuildUnbanEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUnban(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUpdateAfkChannel(GuildUpdateAfkChannelEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUpdateAfkChannel(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUpdateAfkTimeout(GuildUpdateAfkTimeoutEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUpdateAfkTimeout(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUpdateBanner(GuildUpdateBannerEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUpdateBanner(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUpdateBoostCount(GuildUpdateBoostCountEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUpdateBoostCount(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUpdateBoostTier(GuildUpdateBoostTierEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUpdateBoostTier(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUpdateCommunityUpdatesChannel(GuildUpdateCommunityUpdatesChannelEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUpdateCommunityUpdatesChannel(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUpdateDescription(GuildUpdateDescriptionEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUpdateDescription(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUpdateExplicitContentLevel(GuildUpdateExplicitContentLevelEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUpdateExplicitContentLevel(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUpdateFeatures(GuildUpdateFeaturesEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUpdateFeatures(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUpdateIcon(GuildUpdateIconEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUpdateIcon(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUpdateLocale(GuildUpdateLocaleEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUpdateLocale(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUpdateMaxMembers(GuildUpdateMaxMembersEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUpdateMaxMembers(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUpdateMaxPresences(GuildUpdateMaxPresencesEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUpdateMaxPresences(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUpdateMFALevel(GuildUpdateMFALevelEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUpdateMFALevel(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUpdateName(GuildUpdateNameEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUpdateName(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUpdateNotificationLevel(GuildUpdateNotificationLevelEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUpdateNotificationLevel(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUpdateNSFWLevel(GuildUpdateNSFWLevelEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUpdateNSFWLevel(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUpdateOwner(GuildUpdateOwnerEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUpdateOwner(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUpdateRulesChannel(GuildUpdateRulesChannelEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUpdateRulesChannel(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUpdateSplash(GuildUpdateSplashEvent event)
    {        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUpdateSplash(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUpdateSystemChannel(GuildUpdateSystemChannelEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUpdateSystemChannel(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUpdateVanityCode(GuildUpdateVanityCodeEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUpdateVanityCode(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildUpdateVerificationLevel(GuildUpdateVerificationLevelEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildUpdateVerificationLevel(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildVoiceDeafen(GuildVoiceDeafenEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildVoiceDeafen(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildVoiceGuildDeafen(GuildVoiceGuildDeafenEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildVoiceGuildDeafen(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildVoiceGuildMute(GuildVoiceGuildMuteEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildVoiceGuildMute(event);
                    });
                }
            });
        });
    }
     
    // @Override
    // public final void onGuildVoiceJoin(GuildVoiceJoinEvent event)
    // {
    //     HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

    //     //iterate through all plugins on network
    //     AMGN.plugin_listeners.forEach((plugin, listeners) ->
    //     {
    //         //check to see if there are plugins we want to use
    //         callevents.forEach((pluginname, guilds) ->
    //         {
    //             //if there is
    //             if(pluginname.equals(plugin.getName()))
    //             {
    //                 //for every listener, we want to pass this event to it
    //                 listeners.forEach(listener ->
    //                 {
    //                     listener.onGuildVoiceJoin(event);
    //                 });
    //             }
    //         });
    //     });
    // }
     
    // @Override
    // public final void onGuildVoiceLeave(GuildVoiceLeaveEvent event)
    // {
    //     HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

    //     //iterate through all plugins on network
    //     AMGN.plugin_listeners.forEach((plugin, listeners) ->
    //     {
    //         //check to see if there are plugins we want to use
    //         callevents.forEach((pluginname, guilds) ->
    //         {
    //             //if there is
    //             if(pluginname.equals(plugin.getName()))
    //             {
    //                 //for every listener, we want to pass this event to it
    //                 listeners.forEach(listener ->
    //                 {
    //                     listener.onGuildVoiceLeave(event);
    //                 });
    //             }
    //         });
    //     });
    // }
     
    // @Override
    // public final void onGuildVoiceMove(GuildVoiceMoveEvent event)
    // {
    //     HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

    //     //iterate through all plugins on network
    //     AMGN.plugin_listeners.forEach((plugin, listeners) ->
    //     {
    //         //check to see if there are plugins we want to use
    //         callevents.forEach((pluginname, guilds) ->
    //         {
    //             //if there is
    //             if(pluginname.equals(plugin.getName()))
    //             {
    //                 //for every listener, we want to pass this event to it
    //                 listeners.forEach(listener ->
    //                 {
    //                     listener.onGuildVoiceMove(event);
    //                 });
    //             }
    //         });
    //     });
    // }
     
    @Override
    public final void onGuildVoiceMute(GuildVoiceMuteEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildVoiceMute(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildVoiceRequestToSpeak(GuildVoiceRequestToSpeakEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildVoiceRequestToSpeak(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildVoiceSelfDeafen(GuildVoiceSelfDeafenEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildVoiceSelfDeafen(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildVoiceSelfMute(GuildVoiceSelfMuteEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildVoiceSelfMute(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildVoiceStream(GuildVoiceStreamEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildVoiceStream(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildVoiceSuppress(GuildVoiceSuppressEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildVoiceSuppress(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildVoiceUpdate(GuildVoiceUpdateEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildVoiceUpdate(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGuildVoiceVideo(GuildVoiceVideoEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGuildVoiceVideo(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onHttpRequest(HttpRequestEvent event)
    {
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onHttpRequest(event);});
        });
    }
     
    @Override
    public final void onMessageBulkDelete(MessageBulkDeleteEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onMessageBulkDelete(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onMessageContextInteraction(MessageContextInteractionEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onMessageContextInteraction(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onMessageContextInteraction(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onMessageDelete(MessageDeleteEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onMessageDelete(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onMessageDelete(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onMessageEmbed(MessageEmbedEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onMessageEmbed(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onMessageEmbed(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onMessageReactionAdd(MessageReactionAddEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onMessageReactionAdd(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onMessageReactionAdd(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onMessageReactionRemove(MessageReactionRemoveEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onMessageReactionRemove(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onMessageReactionRemove(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onMessageReactionRemoveAll(MessageReactionRemoveAllEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onMessageReactionRemoveAll(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onMessageReactionRemoveAll(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onMessageReactionRemoveEmoji(MessageReactionRemoveEmojiEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onMessageReactionRemoveEmoji(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onMessageReactionRemoveEmoji(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onMessageReceived(MessageReceivedEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener ->
                {
                    listener.onMessageReceived(event);
                });
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onMessageReceived(event);
                        if(listener instanceof Command)
                        {
                            Command cmd = ((Command) listener);
                            String[] args = event.getMessage().getContentRaw().split(" ");
                            if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(event.getGuild().getIdLong()) + cmd.getLabel())
                                && cmd.hasPermission(event.getMember())) //check if the user has permission to run the command
                                cmd.onCommand(new CommandEvent(event.getMessage().getContentRaw(), event.getMember(), (TextChannel) event.getChannel(), event.getMessage()));
                        }
                    });
                }
            });
        });
    }
     
    @Override
    public final void onMessageUpdate(MessageUpdateEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onMessageUpdate(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onMessageUpdate(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onPermissionOverrideCreate(PermissionOverrideCreateEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onPermissionOverrideCreate(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onPermissionOverrideDelete(PermissionOverrideDeleteEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onPermissionOverrideDelete(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onPermissionOverrideUpdate(PermissionOverrideUpdateEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onPermissionOverrideUpdate(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onRawGateway(RawGatewayEvent event)
    {
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onRawGateway(event);});
        });
    }
     
    @Override
    public final void onReady(ReadyEvent event)
    {
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onReady(event);});
        });
    }
     
    @Override
    public final void onSessionRecreate(SessionRecreateEvent event)
    {
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onSessionRecreate(event);});
        });
    }
     
    @Override
    public final void onSessionResume(SessionResumeEvent event)
    {
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onSessionResume(event);});
        });
    }
     
    @Override
    public final void onRoleCreate(RoleCreateEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onRoleCreate(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onRoleDelete(RoleDeleteEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onRoleDelete(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onRoleUpdateColor(RoleUpdateColorEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onRoleUpdateColor(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onRoleUpdateHoisted(RoleUpdateHoistedEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onRoleUpdateHoisted(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onRoleUpdateIcon(RoleUpdateIconEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onRoleUpdateIcon(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onRoleUpdateMentionable(RoleUpdateMentionableEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onRoleUpdateMentionable(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onRoleUpdateName(RoleUpdateNameEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onRoleUpdateName(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onRoleUpdatePermissions(RoleUpdatePermissionsEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onRoleUpdatePermissions(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onRoleUpdatePosition(RoleUpdatePositionEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onRoleUpdatePosition(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onGenericSelectMenuInteraction(GenericSelectMenuInteractionEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onGenericSelectMenuInteraction(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onGenericSelectMenuInteraction(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onSelfUpdateAvatar(SelfUpdateAvatarEvent event)
    {   
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onSelfUpdateAvatar(event);});
        });
    }
     
    @Override
    public final void onSelfUpdateMFA(SelfUpdateMFAEvent event)
    {
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onSelfUpdateMFA(event);});
        });
    }
     
    @Override
    public final void onSelfUpdateName(SelfUpdateNameEvent event)
    {
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onSelfUpdateName(event);});
        });
    }
     
    @Override
    public final void onSelfUpdateVerified(SelfUpdateVerifiedEvent event)
    {
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onSelfUpdateVerified(event);});
        });
    }
     
    @Override
    public final void onShutdown(ShutdownEvent event)
    {
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onShutdown(event);});
        });
    }
     
    @Override
    public final void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onSlashCommandInteraction(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onSlashCommandInteraction(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onStageInstanceCreate(StageInstanceCreateEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onStageInstanceCreate(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onStageInstanceDelete(StageInstanceDeleteEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onStageInstanceDelete(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onStageInstanceUpdatePrivacyLevel(StageInstanceUpdatePrivacyLevelEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onStageInstanceUpdatePrivacyLevel(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onStageInstanceUpdateTopic(StageInstanceUpdateTopicEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onStageInstanceUpdateTopic(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onStatusChange(StatusChangeEvent event)
    {
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onStatusChange(event);});
        });
    }
     
    @Override
    public final void onThreadHidden(ThreadHiddenEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onThreadHidden(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onThreadMemberJoin(ThreadMemberJoinEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onThreadMemberJoin(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onThreadMemberLeave(ThreadMemberLeaveEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onThreadMemberLeave(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onThreadRevealed(ThreadRevealedEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onThreadRevealed(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onUnavailableGuildJoined(UnavailableGuildJoinedEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuildId());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onUnavailableGuildJoined(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onUnavailableGuildLeave(UnavailableGuildLeaveEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuildId());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onUnavailableGuildLeave(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onUserActivityEnd(UserActivityEndEvent event)
    {        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onUserActivityEnd(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onUserActivityStart(UserActivityStartEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onUserActivityStart(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onUserContextInteraction(UserContextInteractionEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onUserContextInteraction(event);});
            });
            return;
        }
        
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onUserContextInteraction(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onUserTyping(UserTypingEvent event)
    {
        if(event.getGuild() == null)
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onUserTyping(event);});
            });
            return;
        }

        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onUserTyping(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onUserUpdateActivities(UserUpdateActivitiesEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onUserUpdateActivities(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onUserUpdateActivityOrder(UserUpdateActivityOrderEvent event)
    {
        HashMap<String, List<Long>> callevents = applyWhitelistBlacklist(event.getGuild());

        //iterate through all plugins on network
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
        {
            //check to see if there are plugins we want to use
            callevents.forEach((pluginname, guilds) ->
            {
                //if there is
                if(pluginname.equals(plugin.getName()))
                {
                    //for every listener, we want to pass this event to it
                    listeners.forEach(listener ->
                    {
                        listener.onUserUpdateActivityOrder(event);
                    });
                }
            });
        });
    }
     
    @Override
    public final void onUserUpdateAvatar(UserUpdateAvatarEvent event)
    {
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onUserUpdateAvatar(event);});
        });
    }
     
    @Override
    public final void onUserUpdateDiscriminator(UserUpdateDiscriminatorEvent event)
    {
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onUserUpdateDiscriminator(event);});
        });
    }
     
    @Override
    public final void onUserUpdateFlags(UserUpdateFlagsEvent event)
    {
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onUserUpdateFlags(event);});
        });
    }
     
    @Override
    public final void onUserUpdateName(UserUpdateNameEvent event)
    {
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onUserUpdateName(event);});
        });
    }
     
    @Override
    public final void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent event)
    {
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onUserUpdateOnlineStatus(event);});
        });
    }

    public static final HashMap<String, List<Long>> applyWhitelistBlacklist(Guild g)
    {
        HashMap<String, List<Long>> callevents = new HashMap<String, List<Long>>();

        if(!GuildNetwork.blacklist.isEmpty() && GuildNetwork.whitelist.isEmpty()) //if there is a blacklist but no whitelist
        {
            //add everything to the callevents
            AMGN.plugin_listeners.keySet().forEach(plugin ->
            {
                List<Long> guild_id_list = new ArrayList<Long>();
                AMGN.bot.getGuilds().forEach(guild -> {guild_id_list.add(guild.getIdLong());});
                callevents.put(plugin.getName(), guild_id_list);
            });

            //then remove the blacklist items
            GuildNetwork.blacklist.forEach((pluginname, guild_ids) ->
            {
                //for each plugin listed
                //iterate through guilds listed
                guild_ids.forEach(guildid ->
                {
                    //if listed guild matches the event's guild
                    if(g.getIdLong() == guildid)
                    {
                        //remove from list of plugin events to call
                        if(callevents.get(pluginname) != null)
                            callevents.get(pluginname).remove(guildid);
                    }
                });
            });

            return callevents;
        }
        else if(!GuildNetwork.whitelist.isEmpty()) //if there is a whitelist
        {
            //start with an empty callevents and add the whitelist items
            GuildNetwork.whitelist.forEach((pluginname, guild_ids) ->
            {
                //for each plugin listed
                //iterate through guilds listed
                guild_ids.forEach(guildid ->
                {
                    //if listed guild matches the event's guild
                    if(g.getIdLong() == guildid)
                    {
                        //add to list of plugin events to call
                        callevents.putIfAbsent(pluginname, new ArrayList<Long>());
                        callevents.get(pluginname).add(guildid);
                    }
                });
            });

            //if there is also a blacklist
            if(!GuildNetwork.blacklist.isEmpty())
            {
                //remove the blacklist items from callevents

                GuildNetwork.blacklist.forEach((pluginname, guild_ids) ->
                {
                    //for each plugin listed
                    //iterate through guilds listed
                    guild_ids.forEach(guildid ->
                    {
                        //if listed guild matches the event's guild
                        if(g.getIdLong() == guildid)
                        {
                            //remove from list of plugin events to call
                            if(callevents.get(pluginname) != null)
                                callevents.get(pluginname).remove(guildid);
                        }
                    });
                });
            }

            return callevents;
        }
        else //if there is no blacklist or whitelist
        {
            //add everything to callevents
            AMGN.plugin_listeners.keySet().forEach(plugin ->
            {
                List<Long> guild_id_list = new ArrayList<Long>();
                AMGN.bot.getGuilds().forEach(guild -> {guild_id_list.add(guild.getIdLong());});
                callevents.put(plugin.getName(), guild_id_list);
            });

            return callevents;
        }
    }

    public static final HashMap<String, List<Long>> applyWhitelistBlacklist(String id)
    {
        HashMap<String, List<Long>> callevents = new HashMap<String, List<Long>>();

        if(!GuildNetwork.blacklist.isEmpty() && GuildNetwork.whitelist.isEmpty()) //if there is a blacklist but no whitelist
        {
            //add everything to the callevents
            AMGN.plugin_listeners.keySet().forEach(plugin ->
            {
                List<Long> guild_id_list = new ArrayList<Long>();
                AMGN.bot.getGuilds().forEach(guild -> {guild_id_list.add(guild.getIdLong());});
                callevents.put(plugin.getName(), guild_id_list);
            });

            //then remove the blacklist items
            GuildNetwork.blacklist.forEach((pluginname, guild_ids) ->
            {
                //for each plugin listed
                //iterate through guilds listed
                guild_ids.forEach(guildid ->
                {
                    //if listed guild matches the event's guild
                    if(Long.parseLong(id) == guildid)
                    {
                        //remove from list of plugin events to call
                        if(callevents.get(pluginname) != null)
                            callevents.get(pluginname).remove(guildid);
                    }
                });
            });

            return callevents;
        }
        else if(!GuildNetwork.whitelist.isEmpty()) //if there is a whitelist
        {
            //start with an empty callevents and add the whitelist items
            GuildNetwork.whitelist.forEach((pluginname, guild_ids) ->
            {
                //for each plugin listed
                //iterate through guilds listed
                guild_ids.forEach(guildid ->
                {
                    //if listed guild matches the event's guild
                    if(Long.parseLong(id) == guildid)
                    {
                        //add to list of plugin events to call
                        callevents.putIfAbsent(pluginname, new ArrayList<Long>());
                        callevents.get(pluginname).add(guildid);
                    }
                });
            });

            //if there is also a blacklist
            if(!GuildNetwork.blacklist.isEmpty())
            {
                //remove the blacklist items from callevents

                GuildNetwork.blacklist.forEach((pluginname, guild_ids) ->
                {
                    //for each plugin listed
                    //iterate through guilds listed
                    guild_ids.forEach(guildid ->
                    {
                        //if listed guild matches the event's guild
                        if(Long.parseLong(id) == guildid)
                        {
                            //remove from list of plugin events to call
                            if(callevents.get(pluginname) != null)
                                callevents.get(pluginname).remove(guildid);
                        }
                    });
                });
            }

            return callevents;
        }
        else //if there is no blacklist or whitelist
        {
            //add everything to callevents
            AMGN.plugin_listeners.keySet().forEach(plugin ->
            {
                List<Long> guild_id_list = new ArrayList<Long>();
                AMGN.bot.getGuilds().forEach(guild -> {guild_id_list.add(guild.getIdLong());});
                callevents.put(plugin.getName(), guild_id_list);
            });

            return callevents;
        }
    }
}
