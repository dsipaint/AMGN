package com.github.dsipaint.AMGN.entities.listeners;

import java.util.ArrayList;
import java.util.List;

import com.github.dsipaint.AMGN.AMGN;
import com.github.dsipaint.AMGN.entities.GuildNetwork;
import com.github.dsipaint.AMGN.entities.plugins.Plugin;

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
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onButtonInteraction(event);});
            });
            return;
        }

        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onButtonInteraction(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onChannelCreate(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onChannelDelete(event);
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

        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onChannelUpdateArchived(event);
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

        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onChannelUpdateArchiveTimestamp(event);
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

        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onChannelUpdateAutoArchiveDuration(event);
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

        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onChannelUpdateBitrate(event);
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

        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onChannelUpdateInvitable(event);
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

        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onChannelUpdateLocked(event);
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

        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onChannelUpdateName(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onChannelUpdateNSFW(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onChannelUpdateParent(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onChannelUpdatePosition(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onChannelUpdateRegion(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onChannelUpdateSlowmode(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onChannelUpdateTopic(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onChannelUpdateType(event);
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

        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onChannelUpdateUserLimit(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onCommandAutoCompleteInteraction(event);
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
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onEmojiAdded(event);
            });
        });
    }
     
    @Override
    public final void onEmojiRemoved(EmojiRemovedEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onEmojiRemoved(event);
            });
        });
    }
     
    @Override
    public final void onEmojiUpdateName(EmojiUpdateNameEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onEmojiUpdateName(event);
            });
        });
    }
     
    @Override
    public final void onEmojiUpdateRoles(EmojiUpdateRolesEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onEmojiUpdateRoles(event);
            });
        });
    }
    
    @Override
    public final void onException(ExceptionEvent event)
    {
        AMGN.logger.error(event.toString());
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericAutoCompleteInteraction(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericChannel(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericChannelUpdate(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericCommandInteraction(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericComponentInteractionCreate(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericContextInteraction(event);
            });
        });
    }
     
    @Override
    public final void onGenericEmoji(GenericEmojiEvent event)
    {   
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericEmoji(event);
            });
        });
    }
     
    @Override
    public final void onGenericEmojiUpdate(GenericEmojiUpdateEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericEmojiUpdate(event);
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
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericGuild(event);
            });
        });
    }
     
    @Override
    public final void onGenericGuildInvite(GenericGuildInviteEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericGuildInvite(event);
            });
        });
    }
     
    @Override
    public final void onGenericGuildMember(GenericGuildMemberEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericGuildMember(event);
            });
        });
    }
     
    @Override
    public final void onGenericGuildMemberUpdate(GenericGuildMemberUpdateEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericGuildMemberUpdate(event);
            });
        });
    }
     
    @Override
    public final void onGenericGuildUpdate(GenericGuildUpdateEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericGuildUpdate(event);
            });
        });
    }
     
    @Override
    public final void onGenericGuildVoice(GenericGuildVoiceEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericGuildVoice(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericInteractionCreate(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericMessage(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericMessageReaction(event);
            });
        });
    }
     
    @Override
    public final void onGenericPermissionOverride(GenericPermissionOverrideEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericPermissionOverride(event);
            });
        });
    }
     
    @Override
    public final void onGenericRole(GenericRoleEvent event)
    {   
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericRole(event);
            });
        });
    }
     
    @Override
    public final void onGenericRoleUpdate(GenericRoleUpdateEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericRoleUpdate(event);
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
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericStageInstance(event);
            });
        });
    }
     
    @Override
    public final void onGenericStageInstanceUpdate(GenericStageInstanceUpdateEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericStageInstanceUpdate(event);
            });
        });
    }
     
    @Override
    public final void onGenericThread(GenericThreadEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericThread(event);
            });
        });
    }
     
    @Override
    public final void onGenericThreadMember(GenericThreadMemberEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericThreadMember(event);
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
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericUserPresence(event);
            });
        });
    }
     
    @Override
    public final void onGuildAvailable(GuildAvailableEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildAvailable(event);
            });
        });
    }
     
    @Override
    public final void onGuildBan(GuildBanEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildBan(event);
            });
        });
    }
     
    @Override
    public final void onGuildInviteCreate(GuildInviteCreateEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildInviteCreate(event);
            });
        });
    }
     
    @Override
    public final void onGuildInviteDelete(GuildInviteDeleteEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildInviteDelete(event);
            });
        });
    }
     
    @Override
    public final void onGuildJoin(GuildJoinEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildJoin(event);
            });
        });
    }
     
    @Override
    public final void onGuildLeave(GuildLeaveEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildLeave(event);
            });
        });
    }
     
    @Override
    public final void onGuildMemberJoin(GuildMemberJoinEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildMemberJoin(event);
            });
        });
    }
     
    @Override
    public final void onGuildMemberRemove(GuildMemberRemoveEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildMemberRemove(event);
            });
        });
    }
     
    @Override
    public final void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildMemberRoleAdd(event);
            });
        });
    }
     
    @Override
    public final void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildMemberRoleRemove(event);
            });
        });
    }
     
    @Override
    public final void onGuildMemberUpdate(GuildMemberUpdateEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildMemberUpdate(event);
            });
        });
    }
     
    @Override
    public final void onGuildMemberUpdateAvatar(GuildMemberUpdateAvatarEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildMemberUpdateAvatar(event);
            });
        });
    }
     
    @Override
    public final void onGuildMemberUpdateBoostTime(GuildMemberUpdateBoostTimeEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildMemberUpdateBoostTime(event);
            });
        });
    }
     
    @Override
    public final void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildMemberUpdateNickname(event);
            });
        });
    }
     
    @Override
    public final void onGuildMemberUpdatePending(GuildMemberUpdatePendingEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildMemberUpdatePending(event);
            });
        });
    }
     
    @Override
    public final void onGuildMemberUpdateTimeOut(GuildMemberUpdateTimeOutEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildMemberUpdateTimeOut(event);
            });
        });
    }
     
    @Override
    public final void onGuildReady(GuildReadyEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildReady(event);
            });
        });
    }
     
    @Override
    public final void onGuildTimeout(GuildTimeoutEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(AMGN.bot.getGuildById(event.getGuildId()));
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildTimeout(event);
            });
        });
    }
     
    @Override
    public final void onGuildUnavailable(GuildUnavailableEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUnavailable(event);
            });
        });
    }
     
    @Override
    public final void onGuildUnban(GuildUnbanEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUnban(event);
            });
        });
    }
     
    @Override
    public final void onGuildUpdateAfkChannel(GuildUpdateAfkChannelEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUpdateAfkChannel(event);
            });
        });
    }
     
    @Override
    public final void onGuildUpdateAfkTimeout(GuildUpdateAfkTimeoutEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUpdateAfkTimeout(event);
            });
        });
    }
     
    @Override
    public final void onGuildUpdateBanner(GuildUpdateBannerEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUpdateBanner(event);
            });
        });
    }
     
    @Override
    public final void onGuildUpdateBoostCount(GuildUpdateBoostCountEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUpdateBoostCount(event);
            });
        });
    }
     
    @Override
    public final void onGuildUpdateBoostTier(GuildUpdateBoostTierEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUpdateBoostTier(event);
            });
        });
    }
     
    @Override
    public final void onGuildUpdateCommunityUpdatesChannel(GuildUpdateCommunityUpdatesChannelEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUpdateCommunityUpdatesChannel(event);
            });
        });
    }
     
    @Override
    public final void onGuildUpdateDescription(GuildUpdateDescriptionEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUpdateDescription(event);
            });
        });
    }
     
    @Override
    public final void onGuildUpdateExplicitContentLevel(GuildUpdateExplicitContentLevelEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUpdateExplicitContentLevel(event);
            });
        });
    }
     
    @Override
    public final void onGuildUpdateFeatures(GuildUpdateFeaturesEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUpdateFeatures(event);
            });
        });
    }
     
    @Override
    public final void onGuildUpdateIcon(GuildUpdateIconEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUpdateIcon(event);
            });
        });
    }
     
    @Override
    public final void onGuildUpdateLocale(GuildUpdateLocaleEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUpdateLocale(event);
            });
        });
    }
     
    @Override
    public final void onGuildUpdateMaxMembers(GuildUpdateMaxMembersEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUpdateMaxMembers(event);
            });
        });
    }
     
    @Override
    public final void onGuildUpdateMaxPresences(GuildUpdateMaxPresencesEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUpdateMaxPresences(event);
            });
        });
    }
     
    @Override
    public final void onGuildUpdateMFALevel(GuildUpdateMFALevelEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUpdateMFALevel(event);
            });
        });
    }
     
    @Override
    public final void onGuildUpdateName(GuildUpdateNameEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUpdateName(event);
            });
        });
    }
     
    @Override
    public final void onGuildUpdateNotificationLevel(GuildUpdateNotificationLevelEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUpdateNotificationLevel(event);
            });
        });
    }
     
    @Override
    public final void onGuildUpdateNSFWLevel(GuildUpdateNSFWLevelEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUpdateNSFWLevel(event);
            });
        });
    }
     
    @Override
    public final void onGuildUpdateOwner(GuildUpdateOwnerEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUpdateOwner(event);
            });
        });
    }
     
    @Override
    public final void onGuildUpdateRulesChannel(GuildUpdateRulesChannelEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUpdateRulesChannel(event);
            });
        });
    }
     
    @Override
    public final void onGuildUpdateSplash(GuildUpdateSplashEvent event)
    {        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUpdateSplash(event);
            });
        });
    }
     
    @Override
    public final void onGuildUpdateSystemChannel(GuildUpdateSystemChannelEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUpdateSystemChannel(event);
            });
        });
    }
     
    @Override
    public final void onGuildUpdateVanityCode(GuildUpdateVanityCodeEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUpdateVanityCode(event);
            });
        });
    }
     
    @Override
    public final void onGuildUpdateVerificationLevel(GuildUpdateVerificationLevelEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildUpdateVerificationLevel(event);
            });
        });
    }
     
    @Override
    public final void onGuildVoiceDeafen(GuildVoiceDeafenEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildVoiceDeafen(event);
            });
        });
    }
     
    @Override
    public final void onGuildVoiceGuildDeafen(GuildVoiceGuildDeafenEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildVoiceGuildDeafen(event);
            });
        });
    }
     
    @Override
    public final void onGuildVoiceGuildMute(GuildVoiceGuildMuteEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildVoiceGuildMute(event);
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
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildVoiceMute(event);
            });
        });
    }
     
    @Override
    public final void onGuildVoiceRequestToSpeak(GuildVoiceRequestToSpeakEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildVoiceRequestToSpeak(event);
            });
        });
    }
     
    @Override
    public final void onGuildVoiceSelfDeafen(GuildVoiceSelfDeafenEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildVoiceSelfDeafen(event);
            });
        });
    }
     
    @Override
    public final void onGuildVoiceSelfMute(GuildVoiceSelfMuteEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildVoiceSelfMute(event);
            });
        });
    }
     
    @Override
    public final void onGuildVoiceStream(GuildVoiceStreamEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildVoiceStream(event);
            });
        });
    }
     
    @Override
    public final void onGuildVoiceSuppress(GuildVoiceSuppressEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildVoiceSuppress(event);
            });
        });
    }
     
    @Override
    public final void onGuildVoiceUpdate(GuildVoiceUpdateEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildVoiceUpdate(event);
            });
        });
    }
     
    @Override
    public final void onGuildVoiceVideo(GuildVoiceVideoEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGuildVoiceVideo(event);
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
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onMessageBulkDelete(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onMessageContextInteraction(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onMessageDelete(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onMessageEmbed(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onMessageReactionAdd(event);
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

        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onMessageReactionRemove(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onMessageReactionRemoveAll(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onMessageReactionRemoveEmoji(event);
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
            boolean pluginshouldrun = pluginShouldRun(plugin.getName(), event.getGuild());

            //go through all listeners
            listeners.forEach(listener ->
            {
                //pass to the event listener itself
                if(pluginshouldrun)
                    listener.onMessageReceived(event);

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
            AMGN.plugin_listeners.values().forEach(listeners ->
            {
                listeners.forEach(listener -> {listener.onMessageUpdate(event);});
            });
            return;
        }
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onMessageUpdate(event);
            });
        });
    }
     
    @Override
    public final void onPermissionOverrideCreate(PermissionOverrideCreateEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onPermissionOverrideCreate(event);
            });
        });
    }
     
    @Override
    public final void onPermissionOverrideDelete(PermissionOverrideDeleteEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onPermissionOverrideDelete(event);
            });
        });
    }
     
    @Override
    public final void onPermissionOverrideUpdate(PermissionOverrideUpdateEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onPermissionOverrideUpdate(event);
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
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onRoleCreate(event);
            });
        });
    }
     
    @Override
    public final void onRoleDelete(RoleDeleteEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onRoleDelete(event);
            });
        });
    }
     
    @Override
    public final void onRoleUpdateColor(RoleUpdateColorEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onRoleUpdateColor(event);
            });
        });
    }
     
    @Override
    public final void onRoleUpdateHoisted(RoleUpdateHoistedEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onRoleUpdateHoisted(event);
            });
        });
    }
     
    @Override
    public final void onRoleUpdateIcon(RoleUpdateIconEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onRoleUpdateIcon(event);
            });
        });
    }
     
    @Override
    public final void onRoleUpdateMentionable(RoleUpdateMentionableEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onRoleUpdateMentionable(event);
            });
        });
    }
     
    @Override
    public final void onRoleUpdateName(RoleUpdateNameEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onRoleUpdateName(event);
            });
        });
    }
     
    @Override
    public final void onRoleUpdatePermissions(RoleUpdatePermissionsEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onRoleUpdatePermissions(event);
            });
        });
    }
     
    @Override
    public final void onRoleUpdatePosition(RoleUpdatePositionEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onRoleUpdatePosition(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onGenericSelectMenuInteraction(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onSlashCommandInteraction(event);
            });
        });
    }
     
    @Override
    public final void onStageInstanceCreate(StageInstanceCreateEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onStageInstanceCreate(event);
            });
        });
    }
     
    @Override
    public final void onStageInstanceDelete(StageInstanceDeleteEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onStageInstanceDelete(event);
            });
        });
    }
     
    @Override
    public final void onStageInstanceUpdatePrivacyLevel(StageInstanceUpdatePrivacyLevelEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onStageInstanceUpdatePrivacyLevel(event);
            });
        });
    }
     
    @Override
    public final void onStageInstanceUpdateTopic(StageInstanceUpdateTopicEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onStageInstanceUpdateTopic(event);
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
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onThreadHidden(event);
            });
        });
    }
     
    @Override
    public final void onThreadMemberJoin(ThreadMemberJoinEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onThreadMemberJoin(event);
            });
        });
    }
     
    @Override
    public final void onThreadMemberLeave(ThreadMemberLeaveEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onThreadMemberLeave(event);
            });
        });
    }
     
    @Override
    public final void onThreadRevealed(ThreadRevealedEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onThreadRevealed(event);
            });
        });
    }
     
    @Override
    public final void onUnavailableGuildJoined(UnavailableGuildJoinedEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(AMGN.bot.getGuildById(event.getGuildId()));
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onUnavailableGuildJoined(event);
            });
        });
    }
     
    @Override
    public final void onUnavailableGuildLeave(UnavailableGuildLeaveEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(AMGN.bot.getGuildById(event.getGuildId()));
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onUnavailableGuildLeave(event);
            });
        });
    }
     
    @Override
    public final void onUserActivityEnd(UserActivityEndEvent event)
    {        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onUserActivityEnd(event);
            });
        });
    }
     
    @Override
    public final void onUserActivityStart(UserActivityStartEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onUserActivityStart(event);
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
        
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onUserContextInteraction(event);
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

        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onUserTyping(event);
            });
        });
    }
     
    @Override
    public final void onUserUpdateActivities(UserUpdateActivitiesEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onUserUpdateActivities(event);
            });
        });
    }
     
    @Override
    public final void onUserUpdateActivityOrder(UserUpdateActivityOrderEvent event)
    {
        List<Plugin> shouldrunhere = getRunningPlugins(event.getGuild());
        shouldrunhere.forEach(plugin ->
        {
            AMGN.plugin_listeners.get(plugin).forEach(listener ->
            {
                listener.onUserUpdateActivityOrder(event);
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
    public void onUserUpdateGlobalName(UserUpdateGlobalNameEvent event)
    {
        AMGN.plugin_listeners.values().forEach(listeners ->
        {
            listeners.forEach(listener -> {listener.onUserUpdateGlobalName(event);});
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

    //figures out if a plugin should run in a guild, based on the whitelist/blacklist rules
    public static final boolean pluginShouldRun(String plugin, Guild g)
    {
        return applyWhitelistBlacklistRules(plugin, g);
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

    //accepts a plugin and returns a list of guilds that the plugin
    //should run in, according to the whitelist/blacklist rules
    public static final List<Guild> getGuildsToRunIn(Plugin plugin)
    {
        ArrayList<Guild> returnguilds = new ArrayList<Guild>();

        AMGN.bot.getGuilds().forEach(g ->
        {
            if(applyWhitelistBlacklistRules(plugin.getName(), g))
                returnguilds.add(g);
        });

        return returnguilds;
    }

    //look at a plugin and guild and apply whitelisting/blacklisting rules to determine if it should run or not
    //this is the actual whitelisting/blacklisting logic
    private static final boolean applyWhitelistBlacklistRules(String plugin, Guild g)
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
