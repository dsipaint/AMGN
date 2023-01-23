package com.github.dsipaint.AMGN.entities.listeners;

import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.ExceptionEvent;
import net.dv8tion.jda.api.events.GatewayPingEvent;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.RawGatewayEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.ResumedEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
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
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent;
import net.dv8tion.jda.api.events.emote.EmoteRemovedEvent;
import net.dv8tion.jda.api.events.emote.GenericEmoteEvent;
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateNameEvent;
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateRolesEvent;
import net.dv8tion.jda.api.events.emote.update.GenericEmoteUpdateEvent;
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
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
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
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageEmbedEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEmoteEvent;
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

public class Listener extends ListenerAdapter
{
    //this class provides a wrapper where we can apply blacklist/whitelisting rules
    //before we choose to pass the event to a plugin's listener (if the guild has whitelisted this plugin/listener)

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event)
    {

    }
     
    @Override
    public void onChannelCreate(ChannelCreateEvent event)
    {

    }
     
    @Override
    public void onChannelDelete(ChannelDeleteEvent event)
    {

    }
     
    @Override
    public void onChannelUpdateArchived(ChannelUpdateArchivedEvent event)
    {

    }
     
    @Override
    public void onChannelUpdateArchiveTimestamp(ChannelUpdateArchiveTimestampEvent event)
    {

    }
     
    @Override
    public void onChannelUpdateAutoArchiveDuration(ChannelUpdateAutoArchiveDurationEvent event)
    {

    }
     
    @Override
    public void onChannelUpdateBitrate(ChannelUpdateBitrateEvent event)
    {

    }
     
    @Override
    public void onChannelUpdateInvitable(ChannelUpdateInvitableEvent event)
    {

    }
     
    @Override
    public void onChannelUpdateLocked(ChannelUpdateLockedEvent event)
    {

    }
     
    @Override
    public void onChannelUpdateName(ChannelUpdateNameEvent event)
    {

    }
     
    @Override
    public void onChannelUpdateNSFW(ChannelUpdateNSFWEvent event)
    {

    }
     
    @Override
    public void onChannelUpdateParent(ChannelUpdateParentEvent event)
    {

    }
     
    @Override
    public void onChannelUpdatePosition(ChannelUpdatePositionEvent event)
    {

    }
     
    @Override
    public void onChannelUpdateRegion(ChannelUpdateRegionEvent event)
    {

    }
     
    @Override
    public void onChannelUpdateSlowmode(ChannelUpdateSlowmodeEvent event)
    {

    }
     
    @Override
    public void onChannelUpdateTopic(ChannelUpdateTopicEvent event)
    {

    }
     
    @Override
    public void onChannelUpdateType(ChannelUpdateTypeEvent event)
    {

    }
     
    @Override
    public void onChannelUpdateUserLimit(ChannelUpdateUserLimitEvent event)
    {

    }
     
    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event)
    {

    }
     
    @Override
    public void onDisconnect(DisconnectEvent event)
    {

    }
     
    @Override
    public void onEmoteAdded(EmoteAddedEvent event)
    {

    }
     
    @Override
    public void onEmoteRemoved(EmoteRemovedEvent event)
    {

    }
     
    @Override
    public void onEmoteUpdateName(EmoteUpdateNameEvent event)
    {

    }
     
    @Override
    public void onEmoteUpdateRoles(EmoteUpdateRolesEvent event)
    {

    }
    
    @Override
    public void onException(ExceptionEvent event)
    {

    }
     
    @Override
    public void onGatewayPing(GatewayPingEvent event)
    {

    }
     
    @Override
    public void onGenericAutoCompleteInteraction(GenericAutoCompleteInteractionEvent event)
    {

    }
     
    @Override
    public void onGenericChannel(GenericChannelEvent event)
    {

    }
     
    @Override
    public void onGenericChannelUpdate(GenericChannelUpdateEvent<?> event)
    {

    }
     
    @Override
    public void onGenericCommandInteraction(GenericCommandInteractionEvent event)
    {

    }
     
    @Override
    public void onGenericComponentInteractionCreate(GenericComponentInteractionCreateEvent event)
    {

    }
     
    @Override
    public void onGenericContextInteraction(GenericContextInteractionEvent<?> event)
    {

    }
     
    @Override
    public void onGenericEmote(GenericEmoteEvent event)
    {

    }
     
    @Override
    public void onGenericEmoteUpdate(GenericEmoteUpdateEvent event)
    {

    }
     
    @Override
    public void onGenericEvent(GenericEvent event)
    {

    }
     
    @Override
    public void onGenericGuild(GenericGuildEvent event)
    {

    }
     
    @Override
    public void onGenericGuildInvite(GenericGuildInviteEvent event)
    {

    }
     
    @Override
    public void onGenericGuildMember(GenericGuildMemberEvent event)
    {

    }
     
    @Override
    public void onGenericGuildMemberUpdate(GenericGuildMemberUpdateEvent event)
    {

    }
     
    @Override
    public void onGenericGuildUpdate(GenericGuildUpdateEvent event)
    {

    }
     
    @Override
    public void onGenericGuildVoice(GenericGuildVoiceEvent event)
    {

    }
     
    @Override
    public void onGenericInteractionCreate(GenericInteractionCreateEvent event)
    {

    }
     
    @Override
    public void onGenericMessage(GenericMessageEvent event)
    {

    }
     
    @Override
    public void onGenericMessageReaction(GenericMessageReactionEvent event)
    {

    }
     
    @Override
    public void onGenericPermissionOverride(GenericPermissionOverrideEvent event)
    {

    }
     
    @Override
    public void onGenericRole(GenericRoleEvent event)
    {

    }
     
    @Override
    public void onGenericRoleUpdate(GenericRoleUpdateEvent event)
    {

    }
     
    @Override
    public void onGenericSelfUpdate(GenericSelfUpdateEvent event)
    {

    }
     
    @Override
    public void onGenericStageInstance(GenericStageInstanceEvent event)
    {

    }
     
    @Override
    public void onGenericStageInstanceUpdate(GenericStageInstanceUpdateEvent event)
    {

    }
     
    @Override
    public void onGenericThread(GenericThreadEvent event)
    {

    }
     
    @Override
    public void onGenericThreadMember(GenericThreadMemberEvent event)
    {

    }
     
    @Override
    public void onGenericUpdate(UpdateEvent<?,?> event)
    {

    }
     
    @Override
    public void onGenericUser(GenericUserEvent event)
    {

    }
     
    @Override
    public void onGenericUserPresence(GenericUserPresenceEvent event)
    {

    }
     
    @Override
    public void onGuildAvailable(GuildAvailableEvent event)
    {

    }
     
    @Override
    public void onGuildBan(GuildBanEvent event)
    {

    }
     
    @Override
    public void onGuildInviteCreate(GuildInviteCreateEvent event)
    {

    }
     
    @Override
    public void onGuildInviteDelete(GuildInviteDeleteEvent event)
    {

    }
     
    @Override
    public void onGuildJoin(GuildJoinEvent event)
    {

    }
     
    @Override
    public void onGuildLeave(GuildLeaveEvent event)
    {

    }
     
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event)
    {

    }
     
    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event)
    {

    }
     
    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event)
    {

    }
     
    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event)
    {

    }
     
    @Override
    public void onGuildMemberUpdate(GuildMemberUpdateEvent event)
    {

    }
     
    @Override
    public void onGuildMemberUpdateAvatar(GuildMemberUpdateAvatarEvent event)
    {

    }
     
    @Override
    public void onGuildMemberUpdateBoostTime(GuildMemberUpdateBoostTimeEvent event)
    {

    }
     
    @Override
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event)
    {

    }
     
    @Override
    public void onGuildMemberUpdatePending(GuildMemberUpdatePendingEvent event)
    {

    }
     
    @Override
    public void onGuildMemberUpdateTimeOut(GuildMemberUpdateTimeOutEvent event)
    {

    }
     
    @Override
    public void onGuildReady(GuildReadyEvent event)
    {

    }
     
    @Override
    public void onGuildTimeout(GuildTimeoutEvent event)
    {

    }
     
    @Override
    public void onGuildUnavailable(GuildUnavailableEvent event)
    {

    }
     
    @Override
    public void onGuildUnban(GuildUnbanEvent event)
    {

    }
     
    @Override
    public void onGuildUpdateAfkChannel(GuildUpdateAfkChannelEvent event)
    {

    }
     
    @Override
    public void onGuildUpdateAfkTimeout(GuildUpdateAfkTimeoutEvent event)
    {

    }
     
    @Override
    public void onGuildUpdateBanner(GuildUpdateBannerEvent event)
    {

    }
     
    @Override
    public void onGuildUpdateBoostCount(GuildUpdateBoostCountEvent event)
    {

    }
     
    @Override
    public void onGuildUpdateBoostTier(GuildUpdateBoostTierEvent event)
    {

    }
     
    @Override
    public void onGuildUpdateCommunityUpdatesChannel(GuildUpdateCommunityUpdatesChannelEvent event)
    {

    }
     
    @Override
    public void onGuildUpdateDescription(GuildUpdateDescriptionEvent event)
    {

    }
     
    @Override
    public void onGuildUpdateExplicitContentLevel(GuildUpdateExplicitContentLevelEvent event)
    {

    }
     
    @Override
    public void onGuildUpdateFeatures(GuildUpdateFeaturesEvent event)
    {

    }
     
    @Override
    public void onGuildUpdateIcon(GuildUpdateIconEvent event)
    {

    }
     
    @Override
    public void onGuildUpdateLocale(GuildUpdateLocaleEvent event)
    {

    }
     
    @Override
    public void onGuildUpdateMaxMembers(GuildUpdateMaxMembersEvent event)
    {

    }
     
    @Override
    public void onGuildUpdateMaxPresences(GuildUpdateMaxPresencesEvent event)
    {

    }
     
    @Override
    public void onGuildUpdateMFALevel(GuildUpdateMFALevelEvent event)
    {

    }
     
    @Override
    public void onGuildUpdateName(GuildUpdateNameEvent event)
    {

    }
     
    @Override
    public void onGuildUpdateNotificationLevel(GuildUpdateNotificationLevelEvent event)
    {

    }
     
    @Override
    public void onGuildUpdateNSFWLevel(GuildUpdateNSFWLevelEvent event)
    {

    }
     
    @Override
    public void onGuildUpdateOwner(GuildUpdateOwnerEvent event)
    {

    }
     
    @Override
    public void onGuildUpdateRulesChannel(GuildUpdateRulesChannelEvent event)
    {

    }
     
    @Override
    public void onGuildUpdateSplash(GuildUpdateSplashEvent event)
    {

    }
     
    @Override
    public void onGuildUpdateSystemChannel(GuildUpdateSystemChannelEvent event)
    {

    }
     
    @Override
    public void onGuildUpdateVanityCode(GuildUpdateVanityCodeEvent event)
    {

    }
     
    @Override
    public void onGuildUpdateVerificationLevel(GuildUpdateVerificationLevelEvent event)
    {

    }
     
    @Override
    public void onGuildVoiceDeafen(GuildVoiceDeafenEvent event)
    {

    }
     
    @Override
    public void onGuildVoiceGuildDeafen(GuildVoiceGuildDeafenEvent event)
    {

    }
     
    @Override
    public void onGuildVoiceGuildMute(GuildVoiceGuildMuteEvent event)
    {

    }
     
    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event)
    {

    }
     
    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event)
    {

    }
     
    @Override
    public void onGuildVoiceMove(GuildVoiceMoveEvent event)
    {

    }
     
    @Override
    public void onGuildVoiceMute(GuildVoiceMuteEvent event)
    {

    }
     
    @Override
    public void onGuildVoiceRequestToSpeak(GuildVoiceRequestToSpeakEvent event)
    {

    }
     
    @Override
    public void onGuildVoiceSelfDeafen(GuildVoiceSelfDeafenEvent event)
    {

    }
     
    @Override
    public void onGuildVoiceSelfMute(GuildVoiceSelfMuteEvent event)
    {

    }
     
    @Override
    public void onGuildVoiceStream(GuildVoiceStreamEvent event)
    {

    }
     
    @Override
    public void onGuildVoiceSuppress(GuildVoiceSuppressEvent event)
    {

    }
     
    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event)
    {

    }
     
    @Override
    public void onGuildVoiceVideo(GuildVoiceVideoEvent event)
    {

    }
     
    @Override
    public void onHttpRequest(HttpRequestEvent event)
    {

    }
     
    @Override
    public void onMessageBulkDelete(MessageBulkDeleteEvent event)
    {

    }
     
    @Override
    public void onMessageContextInteraction(MessageContextInteractionEvent event)
    {

    }
     
    @Override
    public void onMessageDelete(MessageDeleteEvent event)
    {

    }
     
    @Override
    public void onMessageEmbed(MessageEmbedEvent event)
    {

    }
     
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event)
    {

    }
     
    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event)
    {

    }
     
    @Override
    public void onMessageReactionRemoveAll(MessageReactionRemoveAllEvent event)
    {

    }
     
    @Override
    public void onMessageReactionRemoveEmote(MessageReactionRemoveEmoteEvent event)
    {

    }
     
    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {

    }
     
    @Override
    public void onMessageUpdate(MessageUpdateEvent event)
    {

    }
     
    @Override
    public void onPermissionOverrideCreate(PermissionOverrideCreateEvent event)
    {

    }
     
    @Override
    public void onPermissionOverrideDelete(PermissionOverrideDeleteEvent event)
    {

    }
     
    @Override
    public void onPermissionOverrideUpdate(PermissionOverrideUpdateEvent event)
    {

    }
     
    @Override
    public void onRawGateway(RawGatewayEvent event)
    {

    }
     
    @Override
    public void onReady(ReadyEvent event)
    {

    }
     
    @Override
    public void onReconnected(ReconnectedEvent event)
    {

    }
     
    @Override
    public void onResumed(ResumedEvent event)
    {

    }
     
    @Override
    public void onRoleCreate(RoleCreateEvent event)
    {

    }
     
    @Override
    public void onRoleDelete(RoleDeleteEvent event)
    {

    }
     
    @Override
    public void onRoleUpdateColor(RoleUpdateColorEvent event)
    {

    }
     
    @Override
    public void onRoleUpdateHoisted(RoleUpdateHoistedEvent event)
    {

    }
     
    @Override
    public void onRoleUpdateIcon(RoleUpdateIconEvent event)
    {

    }
     
    @Override
    public void onRoleUpdateMentionable(RoleUpdateMentionableEvent event)
    {

    }
     
    @Override
    public void onRoleUpdateName(RoleUpdateNameEvent event)
    {

    }
     
    @Override
    public void onRoleUpdatePermissions(RoleUpdatePermissionsEvent event)
    {

    }
     
    @Override
    public void onRoleUpdatePosition(RoleUpdatePositionEvent event)
    {

    }
     
    @Override
    public void onSelectMenuInteraction(SelectMenuInteractionEvent event)
    {

    }
     
    @Override
    public void onSelfUpdateAvatar(SelfUpdateAvatarEvent event)
    {

    }
     
    @Override
    public void onSelfUpdateMFA(SelfUpdateMFAEvent event)
    {

    }
     
    @Override
    public void onSelfUpdateName(SelfUpdateNameEvent event)
    {

    }
     
    @Override
    public void onSelfUpdateVerified(SelfUpdateVerifiedEvent event)
    {

    }
     
    @Override
    public void onShutdown(ShutdownEvent event)
    {

    }
     
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {

    }
     
    @Override
    public void onStageInstanceCreate(StageInstanceCreateEvent event)
    {

    }
     
    @Override
    public void onStageInstanceDelete(StageInstanceDeleteEvent event)
    {

    }
     
    @Override
    public void onStageInstanceUpdatePrivacyLevel(StageInstanceUpdatePrivacyLevelEvent event)
    {

    }
     
    @Override
    public void onStageInstanceUpdateTopic(StageInstanceUpdateTopicEvent event)
    {

    }
     
    @Override
    public void onStatusChange(StatusChangeEvent event)
    {

    }
     
    @Override
    public void onThreadHidden(ThreadHiddenEvent event)
    {

    }
     
    @Override
    public void onThreadMemberJoin(ThreadMemberJoinEvent event)
    {

    }
     
    @Override
    public void onThreadMemberLeave(ThreadMemberLeaveEvent event)
    {

    }
     
    @Override
    public void onThreadRevealed(ThreadRevealedEvent event)
    {

    }
     
    @Override
    public void onUnavailableGuildJoined(UnavailableGuildJoinedEvent event)
    {

    }
     
    @Override
    public void onUnavailableGuildLeave(UnavailableGuildLeaveEvent event)
    {

    }
     
    @Override
    public void onUserActivityEnd(UserActivityEndEvent event)
    {

    }
     
    @Override
    public void onUserActivityStart(UserActivityStartEvent event)
    {

    }
     
    @Override
    public void onUserContextInteraction(UserContextInteractionEvent event)
    {

    }
     
    @Override
    public void onUserTyping(UserTypingEvent event)
    {

    }
     
    @Override
    public void onUserUpdateActivities(UserUpdateActivitiesEvent event)
    {

    }
     
    @Override
    public void onUserUpdateActivityOrder(UserUpdateActivityOrderEvent event)
    {

    }
     
    @Override
    public void onUserUpdateAvatar(UserUpdateAvatarEvent event)
    {

    }
     
    @Override
    public void onUserUpdateDiscriminator(UserUpdateDiscriminatorEvent event)
    {

    }
     
    @Override
    public void onUserUpdateFlags(UserUpdateFlagsEvent event)
    {

    }
     
    @Override
    public void onUserUpdateName(UserUpdateNameEvent event)
    {

    }
     
    @Override
    public void onUserUpdateOnlineStatus(UserUpdateOnlineStatusEvent event)
    {

    }
}
