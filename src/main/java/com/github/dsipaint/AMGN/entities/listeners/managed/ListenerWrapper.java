package com.github.dsipaint.AMGN.entities.listeners.managed;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.listeners.CommandEvent;
import com.github.dsipaint.AMGN.entities.listeners.Listener;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.Event;
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
import net.dv8tion.jda.api.events.user.update.UserUpdateFlagsEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateGlobalNameEvent;
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
            runAllListeners(listener -> {listener.onButtonInteraction(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onButtonInteraction(event);
        });
    }
     
    @Override
    public final void onChannelCreate(ChannelCreateEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onChannelCreate(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onChannelCreate(event);
        });
    }
     
    @Override
    public final void onChannelDelete(ChannelDeleteEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onChannelDelete(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onChannelDelete(event);
        });
    }
     
    @Override
    public final void onChannelUpdateArchived(ChannelUpdateArchivedEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onChannelUpdateArchived(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onChannelUpdateArchived(event);
        });
    }
     
    @Override
    public final void onChannelUpdateArchiveTimestamp(ChannelUpdateArchiveTimestampEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onChannelUpdateArchiveTimestamp(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onChannelUpdateArchiveTimestamp(event);
        });
    }
     
    @Override
    public final void onChannelUpdateAutoArchiveDuration(ChannelUpdateAutoArchiveDurationEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onChannelUpdateAutoArchiveDuration(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onChannelUpdateAutoArchiveDuration(event);
        });
    }
     
    @Override
    public final void onChannelUpdateBitrate(ChannelUpdateBitrateEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onChannelUpdateBitrate(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onChannelUpdateBitrate(event);
        });
    }
     
    @Override
    public final void onChannelUpdateInvitable(ChannelUpdateInvitableEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onChannelUpdateInvitable(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onChannelUpdateInvitable(event);
        });
    }
     
    @Override
    public final void onChannelUpdateLocked(ChannelUpdateLockedEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onChannelUpdateLocked(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onChannelUpdateLocked(event);
        });
    }
     
    @Override
    public final void onChannelUpdateName(ChannelUpdateNameEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onChannelUpdateName(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onChannelUpdateName(event);
        });
    }
     
    @Override
    public final void onChannelUpdateNSFW(ChannelUpdateNSFWEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onChannelUpdateNSFW(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onChannelUpdateNSFW(event);
        });
    }
     
    @Override
    public final void onChannelUpdateParent(ChannelUpdateParentEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onChannelUpdateParent(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onChannelUpdateParent(event);
        });
    }
     
    @Override
    public final void onChannelUpdatePosition(ChannelUpdatePositionEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onChannelUpdatePosition(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onChannelUpdatePosition(event);
        });
    }
     
    @Override
    public final void onChannelUpdateRegion(ChannelUpdateRegionEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onChannelUpdateRegion(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onChannelUpdateRegion(event);
        });
    }
     
    @Override
    public final void onChannelUpdateSlowmode(ChannelUpdateSlowmodeEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onChannelUpdateSlowmode(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onChannelUpdateSlowmode(event);
        });
    }
     
    @Override
    public final void onChannelUpdateTopic(ChannelUpdateTopicEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onChannelUpdateTopic(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onChannelUpdateTopic(event);
        });
    }
     
    @Override
    public final void onChannelUpdateType(ChannelUpdateTypeEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onChannelUpdateType(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onChannelUpdateType(event);
        });
    }
     
    @Override
    public final void onChannelUpdateUserLimit(ChannelUpdateUserLimitEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onChannelUpdateUserLimit(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onChannelUpdateUserLimit(event);
        });
    }
     
    @Override
    public final void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onCommandAutoCompleteInteraction(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onCommandAutoCompleteInteraction(event);
        });
    }
     
    @Override
    public final void onSessionDisconnect(SessionDisconnectEvent event)
    {
        runAllListeners(listener -> {listener.onSessionDisconnect(event);});
    }
     
    @Override
    public final void onEmojiAdded(EmojiAddedEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onEmojiAdded(event);
        });
    }
     
    @Override
    public final void onEmojiRemoved(EmojiRemovedEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onEmojiRemoved(event);
        });
    }
     
    @Override
    public final void onEmojiUpdateName(EmojiUpdateNameEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onEmojiUpdateName(event);
        });
    }
     
    @Override
    public final void onEmojiUpdateRoles(EmojiUpdateRolesEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onEmojiUpdateRoles(event);
        });
    }
    
    @Override
    public final void onException(ExceptionEvent event)
    {
        AMGN.logger.error(event.toString());
        runAllListeners(listener -> {listener.onException(event);});
    }
     
    @Override
    public final void onGatewayPing(GatewayPingEvent event)
    {
        runAllListeners(listener -> {listener.onGatewayPing(event);});
    }
     
    @Override
    public final void onGenericAutoCompleteInteraction(GenericAutoCompleteInteractionEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onGenericAutoCompleteInteraction(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericAutoCompleteInteraction(event);
        });
    }
     
    @Override
    public final void onGenericChannel(GenericChannelEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onGenericChannel(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericChannel(event);
        });
    }
     
    @Override
    public final void onGenericChannelUpdate(GenericChannelUpdateEvent<?> event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onGenericChannelUpdate(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericChannelUpdate(event);
        });
    }
     
    @Override
    public final void onGenericCommandInteraction(GenericCommandInteractionEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onGenericCommandInteraction(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericCommandInteraction(event);
        });
    }
     
    @Override
    public final void onGenericComponentInteractionCreate(GenericComponentInteractionCreateEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onGenericComponentInteractionCreate(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericComponentInteractionCreate(event);
        });
    }
     
    @Override
    public final void onGenericContextInteraction(GenericContextInteractionEvent<?> event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onGenericContextInteraction(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericContextInteraction(event);
        });
    }
     
    @Override
    public final void onGenericEmoji(GenericEmojiEvent event)
    {   
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericEmoji(event);
        });
    }
     
    @Override
    public final void onGenericEmojiUpdate(GenericEmojiUpdateEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericEmojiUpdate(event);
        });
    }
     
    @Override
    public final void onGenericEvent(GenericEvent event)
    {
        runAllListeners(listener -> {listener.onGenericEvent(event);});
    }
     
    @Override
    public final void onGenericGuild(GenericGuildEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericGuild(event);
        });
    }
     
    @Override
    public final void onGenericGuildInvite(GenericGuildInviteEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericGuildInvite(event);
        });
    }
     
    @Override
    public final void onGenericGuildMember(GenericGuildMemberEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericGuildMember(event);
        });
    }
     
    @Override
    public final void onGenericGuildMemberUpdate(GenericGuildMemberUpdateEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericGuildMemberUpdate(event);
        });
    }
     
    @Override
    public final void onGenericGuildUpdate(GenericGuildUpdateEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericGuildUpdate(event);
        });
    }
     
    @Override
    public final void onGenericGuildVoice(GenericGuildVoiceEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericGuildVoice(event);
        });
    }
     
    @Override
    public final void onGenericInteractionCreate(GenericInteractionCreateEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onGenericInteractionCreate(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericInteractionCreate(event);
        });
    }
     
    @Override
    public final void onGenericMessage(GenericMessageEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onGenericMessage(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericMessage(event);
        });
    }
     
    @Override
    public final void onGenericMessageReaction(GenericMessageReactionEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onGenericMessageReaction(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericMessageReaction(event);
        });
    }
     
    @Override
    public final void onGenericPermissionOverride(GenericPermissionOverrideEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericPermissionOverride(event);
        });
    }
     
    @Override
    public final void onGenericRole(GenericRoleEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericRole(event);
        });
    }
     
    @Override
    public final void onGenericRoleUpdate(GenericRoleUpdateEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericRoleUpdate(event);
        });
    }
     
    @Override
    public final void onGenericSelfUpdate(GenericSelfUpdateEvent event)
    {
        runAllListeners(listener -> {listener.onGenericSelfUpdate(event);});
    }
     
    @Override
    public final void onGenericStageInstance(GenericStageInstanceEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericStageInstance(event);
        });
    }
     
    @Override
    public final void onGenericStageInstanceUpdate(GenericStageInstanceUpdateEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericStageInstanceUpdate(event);
        });
    }
     
    @Override
    public final void onGenericThread(GenericThreadEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericThread(event);
        });
    }
     
    @Override
    public final void onGenericThreadMember(GenericThreadMemberEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericThreadMember(event);
        });
    }
     
    @Override
    public final void onGenericUpdate(UpdateEvent<?,?> event)
    {
        runAllListeners(listener -> {listener.onGenericUpdate(event);});
    }
     
    @Override
    public final void onGenericUser(GenericUserEvent event)
    {
        runAllListeners(listener -> {listener.onGenericUser(event);});
    }
     
    @Override
    public final void onGenericUserPresence(GenericUserPresenceEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericUserPresence(event);
        });
    }
     
    @Override
    public final void onGuildAvailable(GuildAvailableEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildAvailable(event);
        });
    }
     
    @Override
    public final void onGuildBan(GuildBanEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildBan(event);
        });
    }
     
    @Override
    public final void onGuildInviteCreate(GuildInviteCreateEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildInviteCreate(event);
        });
    }
     
    @Override
    public final void onGuildInviteDelete(GuildInviteDeleteEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildInviteDelete(event);
        });
    }
     
    @Override
    public final void onGuildJoin(GuildJoinEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildJoin(event);
        });
    }
     
    @Override
    public final void onGuildLeave(GuildLeaveEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildLeave(event);
        });
    }
     
    @Override
    public final void onGuildMemberJoin(GuildMemberJoinEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildMemberJoin(event);
        });
    }
     
    @Override
    public final void onGuildMemberRemove(GuildMemberRemoveEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildMemberRemove(event);
        });
    }
     
    @Override
    public final void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildMemberRoleAdd(event);
        });
    }
     
    @Override
    public final void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildMemberRoleRemove(event);
        });
    }
     
    @Override
    public final void onGuildMemberUpdate(GuildMemberUpdateEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildMemberUpdate(event);
        });
    }
     
    @Override
    public final void onGuildMemberUpdateAvatar(GuildMemberUpdateAvatarEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildMemberUpdateAvatar(event);
        });
    }
     
    @Override
    public final void onGuildMemberUpdateBoostTime(GuildMemberUpdateBoostTimeEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildMemberUpdateBoostTime(event);
        });
    }
     
    @Override
    public final void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildMemberUpdateNickname(event);
        });
    }
     
    @Override
    public final void onGuildMemberUpdatePending(GuildMemberUpdatePendingEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildMemberUpdatePending(event);
        });
    }
     
    @Override
    public final void onGuildMemberUpdateTimeOut(GuildMemberUpdateTimeOutEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildMemberUpdateTimeOut(event);
        });
    }
     
    @Override
    public final void onGuildReady(GuildReadyEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildReady(event);
        });
    }
     
    @Override
    public final void onGuildTimeout(GuildTimeoutEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(AMGN.bot.getGuildById(event.getGuildId())), listener -> {
            listener.onGuildTimeout(event);
        });
    }
     
    @Override
    public final void onGuildUnavailable(GuildUnavailableEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUnavailable(event);
        });
    }
     
    @Override
    public final void onGuildUnban(GuildUnbanEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUnban(event);
        });
    }
     
    @Override
    public final void onGuildUpdateAfkChannel(GuildUpdateAfkChannelEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUpdateAfkChannel(event);
        });
    }
     
    @Override
    public final void onGuildUpdateAfkTimeout(GuildUpdateAfkTimeoutEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUpdateAfkTimeout(event);
        });
    }
     
    @Override
    public final void onGuildUpdateBanner(GuildUpdateBannerEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUpdateBanner(event);
        });
    }
     
    @Override
    public final void onGuildUpdateBoostCount(GuildUpdateBoostCountEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUpdateBoostCount(event);
        });
    }
     
    @Override
    public final void onGuildUpdateBoostTier(GuildUpdateBoostTierEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUpdateBoostTier(event);
        });
    }
     
    @Override
    public final void onGuildUpdateCommunityUpdatesChannel(GuildUpdateCommunityUpdatesChannelEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUpdateCommunityUpdatesChannel(event);
        });
    }
     
    @Override
    public final void onGuildUpdateDescription(GuildUpdateDescriptionEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUpdateDescription(event);
        });
    }
     
    @Override
    public final void onGuildUpdateExplicitContentLevel(GuildUpdateExplicitContentLevelEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUpdateExplicitContentLevel(event);
        });
    }
     
    @Override
    public final void onGuildUpdateFeatures(GuildUpdateFeaturesEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUpdateFeatures(event);
        });
    }
     
    @Override
    public final void onGuildUpdateIcon(GuildUpdateIconEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUpdateIcon(event);
        });
    }
     
    @Override
    public final void onGuildUpdateLocale(GuildUpdateLocaleEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUpdateLocale(event);
        });
    }
     
    @Override
    public final void onGuildUpdateMaxMembers(GuildUpdateMaxMembersEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUpdateMaxMembers(event);
        });
    }
     
    @Override
    public final void onGuildUpdateMaxPresences(GuildUpdateMaxPresencesEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUpdateMaxPresences(event);
        });
    }
     
    @Override
    public final void onGuildUpdateMFALevel(GuildUpdateMFALevelEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUpdateMFALevel(event);
        });
    }
     
    @Override
    public final void onGuildUpdateName(GuildUpdateNameEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUpdateName(event);
        });
    }
     
    @Override
    public final void onGuildUpdateNotificationLevel(GuildUpdateNotificationLevelEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUpdateNotificationLevel(event);
        });
    }
     
    @Override
    public final void onGuildUpdateNSFWLevel(GuildUpdateNSFWLevelEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUpdateNSFWLevel(event);
        });
    }
     
    @Override
    public final void onGuildUpdateOwner(GuildUpdateOwnerEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUpdateOwner(event);
        });
    }
     
    @Override
    public final void onGuildUpdateRulesChannel(GuildUpdateRulesChannelEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUpdateRulesChannel(event);
        });
    }
     
    @Override
    public final void onGuildUpdateSplash(GuildUpdateSplashEvent event)
    {        
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUpdateSplash(event);
        });
    }
     
    @Override
    public final void onGuildUpdateSystemChannel(GuildUpdateSystemChannelEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUpdateSystemChannel(event);
        });
    }
     
    @Override
    public final void onGuildUpdateVanityCode(GuildUpdateVanityCodeEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUpdateVanityCode(event);
        });
    }
     
    @Override
    public final void onGuildUpdateVerificationLevel(GuildUpdateVerificationLevelEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildUpdateVerificationLevel(event);
        });
    }
     
    @Override
    public final void onGuildVoiceDeafen(GuildVoiceDeafenEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildVoiceDeafen(event);
        });
    }
     
    @Override
    public final void onGuildVoiceGuildDeafen(GuildVoiceGuildDeafenEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildVoiceGuildDeafen(event);
        });
    }
     
    @Override
    public final void onGuildVoiceGuildMute(GuildVoiceGuildMuteEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildVoiceGuildMute(event);
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
	    //
	//           			if(listener instanceof Listener)
	// 			            	((Listener) listener).onGuildVoiceJoin(event);
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
		//                      if(listener instanceof Listener)
	// 	                        ((Listener) listener).onGuildVoiceLeave(event);
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
	    //                     
	//          			if(listener instanceof Listener)
	//			            	((Listener) listener).onGuildVoiceMove(event);
    //                 });
    //             }
    //         });
    //     });
    // }
     
    @Override
    public final void onGuildVoiceMute(GuildVoiceMuteEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildVoiceMute(event);
        });
    }
     
    @Override
    public final void onGuildVoiceRequestToSpeak(GuildVoiceRequestToSpeakEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildVoiceRequestToSpeak(event);
        });
    }
     
    @Override
    public final void onGuildVoiceSelfDeafen(GuildVoiceSelfDeafenEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildVoiceSelfDeafen(event);
        });
    }
     
    @Override
    public final void onGuildVoiceSelfMute(GuildVoiceSelfMuteEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildVoiceSelfMute(event);
        });
    }
     
    @Override
    public final void onGuildVoiceStream(GuildVoiceStreamEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildVoiceStream(event);
        });
    }
     
    @Override
    public final void onGuildVoiceSuppress(GuildVoiceSuppressEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildVoiceSuppress(event);
        });
    }
     
    @Override
    public final void onGuildVoiceUpdate(GuildVoiceUpdateEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildVoiceUpdate(event);
        });
    }
     
    @Override
    public final void onGuildVoiceVideo(GuildVoiceVideoEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGuildVoiceVideo(event);
        });
    }
     
    @Override
    public final void onHttpRequest(HttpRequestEvent event)
    {
        runAllListeners(listener -> {listener.onHttpRequest(event);});
    }
     
    @Override
    public final void onMessageBulkDelete(MessageBulkDeleteEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onMessageBulkDelete(event);
        });
    }
     
    @Override
    public final void onMessageContextInteraction(MessageContextInteractionEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onMessageContextInteraction(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onMessageContextInteraction(event);
        });
    }
     
    @Override
    public final void onMessageDelete(MessageDeleteEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onMessageDelete(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onMessageDelete(event);
        });
    }
     
    @Override
    public final void onMessageEmbed(MessageEmbedEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onMessageEmbed(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onMessageEmbed(event);
        });
    }
     
    @Override
    public final void onMessageReactionAdd(MessageReactionAddEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onMessageReactionAdd(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onMessageReactionAdd(event);
        });
    }
     
    @Override
    public final void onMessageReactionRemove(MessageReactionRemoveEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onMessageReactionRemove(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onMessageReactionRemove(event);
        });
    }
     
    @Override
    public final void onMessageReactionRemoveAll(MessageReactionRemoveAllEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onMessageReactionRemoveAll(event);});
            return;
        }
        
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onMessageReactionRemoveAll(event);
        });
    }
     
    @Override
    public final void onMessageReactionRemoveEmoji(MessageReactionRemoveEmojiEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onMessageReactionRemoveEmoji(event);});
            return;
        }
        
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onMessageReactionRemoveEmoji(event);
        });
    }
    
    //TODO check carefully
    @Override
    public final void onMessageReceived(MessageReceivedEvent event)
    {
        if(!event.isFromGuild())
        {
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener ->
	                {
	                    
				if(listener instanceof Listener)
					((Listener) listener).onMessageReceived(event);
                });
            });
            return;
        }

        String[] args = event.getMessage().getContentRaw().split(" ");

        //check default commands to be run
        for(DefaultCommand cmd : DefaultCommand.values())
        {
            if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(event.getGuild().getIdLong()) + cmd.getLabel()))
            {
                AMGN.logger.info("Member " + event.getMember().toString() + " is running command \""
                    + event.getMessage().getContentRaw().substring(1) + "\" in channel " + event.getChannel().toString());

                if(cmd.hasPermission(event.getMember()))
                    cmd.getCommandAction().accept(new CommandEvent(event.getMessage().getContentRaw(), event.getMember(), (TextChannel) event.getChannel(), event.getMessage()));
                else
                    AMGN.logger.info("Member " + event.getMember().toString() + " does not have the permission to run command \"" + event.getMessage().getContentRaw().substring(1) + "\"");
            }
        }

        //iterate through all plugins
        AMGN.plugin_listeners.forEach((plugin, listeners) ->
	        {
	            boolean pluginshouldrun = applyWhitelistBlacklistRules(plugin.getName(), event.getGuild());

            //go through all listeners
            listeners.forEach(listener ->
	            {
	                //pass to the event listener itself
                if(pluginshouldrun)
                    
				if(listener instanceof Listener)
					((Listener) listener).onMessageReceived(event);

                //also run as a command if it's a command to be run
                if(listener instanceof Command)
                {
                    Command cmd = ((Command) listener);
                    if(args[0].equalsIgnoreCase(GuildNetwork.getPrefix(event.getGuild().getIdLong()) + cmd.getLabel()))
                    {
                        AMGN.logger.info("Member " + event.getMember().toString() + " is running command \""
                            + event.getMessage().getContentRaw().substring(1) + "\" in channel " + event.getChannel().toString());

                        //if whitelisting/blacklisting doesn't allow, we want to let the user know and do nothing more
                        if(!pluginshouldrun)
                        {
                            AMGN.logger.info("Network whitelist/blacklist rules do not allow the command \"" + event.getMessage().getContentRaw().substring(1) + "\""
					            + " to be run in the guild " + event.getGuild().toString());
                            return;
                        }

                        if(cmd.hasPermission(event.getMember())) //check if the user has permission to run the command
                            cmd.onCommand(new CommandEvent(event.getMessage().getContentRaw(), event.getMember(), (TextChannel) event.getChannel(), event.getMessage()));
                        else
                            AMGN.logger.info("Member " + event.getMember().toString() + " does not have the permission to run command \"" + event.getMessage().getContentRaw().substring(1) + "\"");
                    }
                }
            });
        });
    }
     
    @Override
    public final void onMessageUpdate(MessageUpdateEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onMessageUpdate(event);});
            return;
        }
        
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onMessageUpdate(event);
        });
    }
     
    @Override
    public final void onPermissionOverrideCreate(PermissionOverrideCreateEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onPermissionOverrideCreate(event);
        });
    }
     
    @Override
    public final void onPermissionOverrideDelete(PermissionOverrideDeleteEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onPermissionOverrideDelete(event);
        });
    }
     
    @Override
    public final void onPermissionOverrideUpdate(PermissionOverrideUpdateEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onPermissionOverrideUpdate(event);
        });
    }
     
    @Override
    public final void onRawGateway(RawGatewayEvent event)
    {
        runAllListeners(listener -> {listener.onRawGateway(event);});
    }
     
    @Override
    public final void onReady(ReadyEvent event)
    {
        runAllListeners(listener -> {listener.onReady(event);});
    }
     
    @Override
    public final void onSessionRecreate(SessionRecreateEvent event)
    {
        runAllListeners(listener -> {listener.onSessionRecreate(event);});
    }
     
    @Override
    public final void onSessionResume(SessionResumeEvent event)
    {
        runAllListeners(listener -> {listener.onSessionResume(event);});
    }
     
    @Override
    public final void onRoleCreate(RoleCreateEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onRoleCreate(event);
        });
    }
     
    @Override
    public final void onRoleDelete(RoleDeleteEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onRoleDelete(event);
        });
    }
     
    @Override
    public final void onRoleUpdateColor(RoleUpdateColorEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onRoleUpdateColor(event);
        });
    }
     
    @Override
    public final void onRoleUpdateHoisted(RoleUpdateHoistedEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onRoleUpdateHoisted(event);
        });
    }
     
    @Override
    public final void onRoleUpdateIcon(RoleUpdateIconEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onRoleUpdateIcon(event);
        });
    }
     
    @Override
    public final void onRoleUpdateMentionable(RoleUpdateMentionableEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onRoleUpdateMentionable(event);
        });
    }
     
    @Override
    public final void onRoleUpdateName(RoleUpdateNameEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onRoleUpdateName(event);
        });
    }
     
    @Override
    public final void onRoleUpdatePermissions(RoleUpdatePermissionsEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onRoleUpdatePermissions(event);
        });
    }
     
    @Override
    public final void onRoleUpdatePosition(RoleUpdatePositionEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onRoleUpdatePosition(event);
        });
    }
     
    @Override
    public final void onGenericSelectMenuInteraction(GenericSelectMenuInteractionEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onGenericSelectMenuInteraction(event);});
            return;
        }
        
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onGenericSelectMenuInteraction(event);
        });
    }
     
    @Override
    public final void onSelfUpdateAvatar(SelfUpdateAvatarEvent event)
    {   
        runAllListeners(listener -> {listener.onSelfUpdateAvatar(event);});
    }
     
    @Override
    public final void onSelfUpdateMFA(SelfUpdateMFAEvent event)
    {
        runAllListeners(listener -> {listener.onSelfUpdateMFA(event);});
    }
     
    @Override
    public final void onSelfUpdateName(SelfUpdateNameEvent event)
    {
        runAllListeners(listener -> {listener.onSelfUpdateName(event);});
    }
     
    @Override
    public final void onSelfUpdateVerified(SelfUpdateVerifiedEvent event)
    {
        runAllListeners(listener -> {listener.onSelfUpdateVerified(event);});
    }
     
    @Override
    public final void onShutdown(ShutdownEvent event)
    {
        runAllListeners(listener -> {listener.onShutdown(event);});
    }
     
    @Override
    public final void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onSlashCommandInteraction(event);});
            return;
        }
        
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onSlashCommandInteraction(event);
        });
    }
     
    @Override
    public final void onStageInstanceCreate(StageInstanceCreateEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onStageInstanceCreate(event);
        });
    }
     
    @Override
    public final void onStageInstanceDelete(StageInstanceDeleteEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onStageInstanceDelete(event);
        });
    }
     
    @Override
    public final void onStageInstanceUpdatePrivacyLevel(StageInstanceUpdatePrivacyLevelEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onStageInstanceUpdatePrivacyLevel(event);
        });
    }
     
    @Override
    public final void onStageInstanceUpdateTopic(StageInstanceUpdateTopicEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onStageInstanceUpdateTopic(event);
        });
    }
     
    @Override
    public final void onStatusChange(StatusChangeEvent event)
    {
        runAllListeners(listener -> {listener.onStatusChange(event);});
    }
     
    @Override
    public final void onThreadHidden(ThreadHiddenEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onThreadHidden(event);
        });
    }
     
    @Override
    public final void onThreadMemberJoin(ThreadMemberJoinEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onThreadMemberJoin(event);
        });
    }
     
    @Override
    public final void onThreadMemberLeave(ThreadMemberLeaveEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onThreadMemberLeave(event);
        });
    }
     
    @Override
    public final void onThreadRevealed(ThreadRevealedEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onThreadRevealed(event);
        });
    }
     
    @Override
    public final void onUnavailableGuildJoined(UnavailableGuildJoinedEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(AMGN.bot.getGuildById(event.getGuildId())), listener -> {
            listener.onUnavailableGuildJoined(event);
        });
    }
     
    @Override
    public final void onUnavailableGuildLeave(UnavailableGuildLeaveEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(AMGN.bot.getGuildById(event.getGuildId())), listener -> {
            listener.onUnavailableGuildLeave(event);
        });
    }
     
    @Override
    public final void onUserActivityEnd(UserActivityEndEvent event)
    {        
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onUserActivityEnd(event);
        });
    }
     
    @Override
    public final void onUserActivityStart(UserActivityStartEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onUserActivityStart(event);
        });
    }
     
    @Override
    public final void onUserContextInteraction(UserContextInteractionEvent event)
    {
        if(!event.isFromGuild())
        {
            runAllListeners(listener -> {listener.onUserContextInteraction(event);});
            return;
        }
        
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onUserContextInteraction(event);
        });
    }
     
    @Override
    public final void onUserTyping(UserTypingEvent event)
    {
        if(event.getGuild() == null)
        {
            runAllListeners(listener -> {listener.onUserTyping(event);});
            return;
        }

        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onUserTyping(event);
        });
    }
     
    @Override
    public final void onUserUpdateActivities(UserUpdateActivitiesEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onUserUpdateActivities(event);
        });
    }
     
    @Override
    public final void onUserUpdateActivityOrder(UserUpdateActivityOrderEvent event)
    {
        runListenersInAllowedGuilds(getRunningPlugins(event.getGuild()), listener -> {
            listener.onUserUpdateActivityOrder(event);
        });
    }
     
    @Override
    public final void onUserUpdateAvatar(UserUpdateAvatarEvent event)
    {
        runAllListeners(listener -> {listener.onUserUpdateAvatar(event);});
    }
     
    @Override
    public final void onUserUpdateFlags(UserUpdateFlagsEvent event)
    {
        runAllListeners(listener -> {listener.onUserUpdateFlags(event);});
    }
     
    @Override
    public final void onUserUpdateName(UserUpdateNameEvent event)
    {
        runAllListeners(listener -> {listener.onUserUpdateName(event);});
    }

    @Override
    public void onUserUpdateGlobalName(UserUpdateGlobalNameEvent event)
    {
        runAllListeners(listener -> {listener.onUserUpdateGlobalName(event);});
    }
     
    @Override
    public final void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent event)
    {
        runAllListeners(listener -> {listener.onUserUpdateOnlineStatus(event);});
    }


    //END OF OVERRIDDEN LISTENER METHODS


    //trigger all discord even listeners registered with AMGN
    public static final void runAllListeners(Consumer<Listener> consumer)
    {
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {
                if(listener instanceof Listener)
                    consumer.accept((Listener) listener);
            });
        });
    }

    //trigger only discord even listeners registered with AMGN
    //that make it through whitelist/blacklist rules
    public static final void runListenersInAllowedGuilds(List<Plugin> shouldrunhere, Consumer<Listener> consumer)
    {
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
				if(listener instanceof Listener)
					consumer.accept((Listener) listener);
            });
        });
    }

    //accepts a guild and returns a list of all plugins that make it through the whitelist/blacklsit rules
    //to run in that guild
    public static final List<Plugin> getRunningPlugins(Guild g)
    {
        ArrayList<Plugin> returnplugins = new ArrayList<Plugin>();

        AMGN.plugin_listeners.keySet().forEach(plugin ->
        {
            if(applyWhitelistBlacklistRules(plugin.getName(), g))
                returnplugins.add(plugin);
        });

        return returnplugins;
    }

    //look at a plugin and guild and apply whitelisting/blacklisting rules to determine if it should run or not
    //this is the actual whitelisting/blacklisting logic
    public static final boolean applyWhitelistBlacklistRules(String plugin, Guild g)
    {
        if(!GuildNetwork.blacklist.isEmpty() && GuildNetwork.whitelist.isEmpty()) //if there is a blacklist but no whitelist
            return !GuildNetwork.blacklist.getOrDefault(plugin, new ArrayList<Long>()).contains(g.getIdLong()); //if it is blacklisted, then we should return false otherwise true
        else if(!GuildNetwork.whitelist.isEmpty()) //if there is a whitelist
        {
            //if it is whitelisted, then we should return true
            boolean shouldrun = GuildNetwork.whitelist.getOrDefault(plugin, new ArrayList<Long>()).contains(g.getIdLong());

            //UNLESS it is also blacklisted, as the order we apply in is whitelist -> blacklist
            if(!GuildNetwork.blacklist.isEmpty() && GuildNetwork.blacklist.getOrDefault(plugin, new ArrayList<Long>()).contains(g.getIdLong()))
                shouldrun = !GuildNetwork.blacklist.getOrDefault(plugin, new ArrayList<Long>()).contains(g.getIdLong());

            return shouldrun;
        }
        else //if there is no blacklist or whitelist
            return true; //run everything
    }
}
