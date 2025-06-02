package android.app;

import android.content.ComponentName;
import android.content.pm.ParceledListSlice;
import android.net.INetworkStatsService;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.UserHandle;
import android.service.notification.Condition;
import android.service.notification.INotificationListener;
import android.service.notification.StatusBarNotification;

import java.util.List;


public interface INotificationManager {

    void cancelAllNotifications(String pkg, int userId);

    void clearData(String pkg, int uid, boolean fromApp);

//    void enqueueTextToast(String pkg, IBinder token, CharSequence text, int duration, int displayId, ITransientNotificationCallback callback);
//
//    void enqueueToast(String pkg, IBinder token, ITransientNotification callback, int duration, int displayId);

    void cancelToast(String pkg, IBinder token);

    void finishToken(String pkg, IBinder token);

    void enqueueNotificationWithTag(String pkg, String opPkg, String tag, int id,
                                    Notification notification, int userId);

    void cancelNotificationWithTag(String pkg, String opPkg, String tag, int id, int userId);

    boolean isInCall(String pkg, int uid);

    void setShowBadge(String pkg, int uid, boolean showBadge);

    boolean canShowBadge(String pkg, int uid);

    boolean hasSentValidMsg(String pkg, int uid);

    boolean isInInvalidMsgState(String pkg, int uid);

    boolean hasUserDemotedInvalidMsgApp(String pkg, int uid);

    void setInvalidMsgAppDemoted(String pkg, int uid, boolean isDemoted);

    boolean hasSentValidBubble(String pkg, int uid);

    void setNotificationsEnabledForPackage(String pkg, int uid, boolean enabled);

    /**
     * Updates the notification's enabled state. Additionally locks importance for all of the
     * notifications belonging to the app, such that future notifications aren't reconsidered for
     * blocking helper.
     */
    void setNotificationsEnabledWithImportanceLockForPackage(String pkg, int uid, boolean enabled);


    boolean areNotificationsEnabledForPackage(String pkg, int uid);

    boolean areNotificationsEnabled(String pkg);

    int getPackageImportance(String pkg);

    boolean isImportanceLocked(String pkg, int uid);

    List<String> getAllowedAssistantAdjustments(String pkg);

    void allowAssistantAdjustment(String adjustmentType);

    void disallowAssistantAdjustment(String adjustmentType);

    boolean shouldHideSilentStatusIcons(String callingPkg);

    void setHideSilentStatusIcons(boolean hide);

    void setBubblesAllowed(String pkg, int uid, int bubblePreference);

    boolean areBubblesAllowed(String pkg);

    boolean areBubblesEnabled(UserHandle user);

    int getBubblePreferenceForPackage(String pkg, int uid);

    void createNotificationChannelGroups(String pkg, ParceledListSlice channelGroupList);

    void createNotificationChannels(String pkg, ParceledListSlice channelsList);

    void createNotificationChannelsForPackage(String pkg, int uid, ParceledListSlice channelsList);

    ParceledListSlice getConversations(boolean onlyImportant);

    ParceledListSlice getConversationsForPackage(String pkg, int uid);

    ParceledListSlice getNotificationChannelGroupsForPackage(String pkg, int uid, boolean includeDeleted);

    NotificationChannelGroup getNotificationChannelGroupForPackage(String groupId, String pkg, int uid);

    NotificationChannelGroup getPopulatedNotificationChannelGroupForPackage(String pkg, int uid, String groupId, boolean includeDeleted);

    void updateNotificationChannelGroupForPackage(String pkg, int uid, NotificationChannelGroup group);

    void updateNotificationChannelForPackage(String pkg, int uid, NotificationChannel channel);

    void unlockNotificationChannel(String pkg, int uid, String channelId);

    void unlockAllNotificationChannels();

    NotificationChannel getNotificationChannel(String callingPkg, int userId, String pkg, String channelId);

    NotificationChannel getConversationNotificationChannel(String callingPkg, int userId, String pkg, String channelId, boolean returnParentIfNoConversationChannel, String conversationId);

    void createConversationNotificationChannelForPackage(String pkg, int uid, NotificationChannel parentChannel, String conversationId);

    NotificationChannel getNotificationChannelForPackage(String pkg, int uid, String channelId, String conversationId, boolean includeDeleted);

    void deleteNotificationChannel(String pkg, String channelId);

    ParceledListSlice getNotificationChannels(String callingPkg, String targetPkg, int userId);

    ParceledListSlice getNotificationChannelsForPackage(String pkg, int uid, boolean includeDeleted);

    int getNumNotificationChannelsForPackage(String pkg, int uid, boolean includeDeleted);

    int getDeletedChannelCount(String pkg, int uid);

    int getBlockedChannelCount(String pkg, int uid);

    void deleteNotificationChannelGroup(String pkg, String channelGroupId);

    NotificationChannelGroup getNotificationChannelGroup(String pkg, String channelGroupId);

    ParceledListSlice getNotificationChannelGroups(String pkg);

    boolean onlyHasDefaultChannel(String pkg, int uid);

    boolean areChannelsBypassingDnd();

    ParceledListSlice getNotificationChannelsBypassingDnd(String pkg, int uid);

    boolean isPackagePaused(String pkg);

    void deleteNotificationHistoryItem(String pkg, int uid, long postedTime);

    boolean isPermissionFixed(String pkg, int userId);

    void silenceNotificationSound();

    // TODO: Remove this when callers have been migrated to the equivalent
    // INotificationListener method.

    StatusBarNotification[] getActiveNotifications(String callingPkg);

    StatusBarNotification[] getActiveNotificationsWithAttribution(String callingPkg,
                                                                  String callingAttributionTag);

    StatusBarNotification[] getHistoricalNotifications(String callingPkg, int count, boolean includeSnoozed);

    StatusBarNotification[] getHistoricalNotificationsWithAttribution(String callingPkg,
                                                                      String callingAttributionTag, int count, boolean includeSnoozed);

//    NotificationHistory getNotificationHistory(String callingPkg, String callingAttributionTag);

    void registerListener(INotificationListener listener, ComponentName component, int userid);

    void unregisterListener(INotificationListener listener, int userid);

    void cancelNotificationFromListener(INotificationListener token, String pkg, String tag, int id);

    void cancelNotificationsFromListener(INotificationListener token, String[] keys);

    void snoozeNotificationUntilContextFromListener(INotificationListener token, String key, String snoozeCriterionId);

    void snoozeNotificationUntilFromListener(INotificationListener token, String key, long until);

    void requestBindListener(ComponentName component);

    void requestUnbindListener(INotificationListener token);

    void requestBindProvider(ComponentName component);

//    void requestUnbindProvider(IConditionProvider token);

    void setNotificationsShownFromListener(INotificationListener token, String[] keys);

    ParceledListSlice getActiveNotificationsFromListener(INotificationListener token, String[] keys, int trim);

    ParceledListSlice getSnoozedNotificationsFromListener(INotificationListener token, int trim);

    void clearRequestedListenerHints(INotificationListener token);

    void requestHintsFromListener(INotificationListener token, int hints);

    int getHintsFromListener(INotificationListener token);

    int getHintsFromListenerNoToken();

    void requestInterruptionFilterFromListener(INotificationListener token, int interruptionFilter);

    int getInterruptionFilterFromListener(INotificationListener token);

    void setOnNotificationPostedTrimFromListener(INotificationListener token, int trim);

    void setInterruptionFilter(String pkg, int interruptionFilter);

    void updateNotificationChannelGroupFromPrivilegedListener(INotificationListener token, String pkg, UserHandle user, NotificationChannelGroup group);

    void updateNotificationChannelFromPrivilegedListener(INotificationListener token, String pkg, UserHandle user, NotificationChannel channel);

    ParceledListSlice getNotificationChannelsFromPrivilegedListener(INotificationListener token, String pkg, UserHandle user);

    ParceledListSlice getNotificationChannelGroupsFromPrivilegedListener(INotificationListener token, String pkg, UserHandle user);

//    void applyEnqueuedAdjustmentFromAssistant(INotificationListener token, Adjustment adjustment);
//
//    void applyAdjustmentFromAssistant(INotificationListener token, Adjustment adjustment);
//
//    void applyAdjustmentsFromAssistant(INotificationListener token, List<Adjustment> adjustments);

    void unsnoozeNotificationFromAssistant(INotificationListener token, String key);

    void unsnoozeNotificationFromSystemListener(INotificationListener token, String key);

    ComponentName getEffectsSuppressor();

    boolean matchesCallFilter(Bundle extras);

    void cleanUpCallersAfter(long timeThreshold);

    boolean isSystemConditionProviderEnabled(String path);

    boolean isNotificationListenerAccessGranted(ComponentName listener);

    boolean isNotificationListenerAccessGrantedForUser(ComponentName listener, int userId);

    boolean isNotificationAssistantAccessGranted(ComponentName assistant);

    void setNotificationListenerAccessGranted(ComponentName listener, boolean enabled, boolean userSet);

    void setNotificationAssistantAccessGranted(ComponentName assistant, boolean enabled);

    void setNotificationListenerAccessGrantedForUser(ComponentName listener, int userId, boolean enabled, boolean userSet);

    void setNotificationAssistantAccessGrantedForUser(ComponentName assistant, int userId, boolean enabled);

    List<String> getEnabledNotificationListenerPackages();

    List<ComponentName> getEnabledNotificationListeners(int userId);

    ComponentName getAllowedNotificationAssistantForUser(int userId);

    ComponentName getAllowedNotificationAssistant();

    ComponentName getDefaultNotificationAssistant();

    void setNASMigrationDoneAndResetDefault(int userId, boolean loadFromConfig);

    boolean hasEnabledNotificationListener(String packageName, int userId);


    int getZenMode();

    //    ZenModeConfig getZenModeConfig();
    NotificationManager.Policy getConsolidatedNotificationPolicy();

    void setZenMode(int mode, Uri conditionId, String reason);

    //    void notifyConditions(String pkg, IConditionProvider provider, Condition[] conditions);
    boolean isNotificationPolicyAccessGranted(String pkg);

    NotificationManager.Policy getNotificationPolicy(String pkg);

    void setNotificationPolicy(String pkg, NotificationManager.Policy policy);

    boolean isNotificationPolicyAccessGrantedForPackage(String pkg);

    void setNotificationPolicyAccessGranted(String pkg, boolean granted);

    void setNotificationPolicyAccessGrantedForUser(String pkg, int userId, boolean granted);

    AutomaticZenRule getAutomaticZenRule(String id);

    //    List<ZenModeConfig.ZenRule> getZenRules();
    String addAutomaticZenRule(AutomaticZenRule automaticZenRule, String pkg);

    boolean updateAutomaticZenRule(String id, AutomaticZenRule automaticZenRule);

    boolean removeAutomaticZenRule(String id);

    boolean removeAutomaticZenRules(String packageName);

    int getRuleInstanceCount(ComponentName owner);

    void setAutomaticZenRuleState(String id, Condition condition);

    byte[] getBackupPayload(int user);

    void applyRestore(byte[] payload, int user);

    ParceledListSlice getAppActiveNotifications(String callingPkg, int userId);

    void setNotificationDelegate(String callingPkg, String delegate);

    String getNotificationDelegate(String callingPkg);

    boolean canNotifyAsPackage(String callingPkg, String targetPkg, int userId);

    void setPrivateNotificationsAllowed(boolean allow);

    boolean getPrivateNotificationsAllowed();

//    long pullStats(long startNs, int report, boolean doAgg, out List<ParcelFileDescriptor>stats);
//
//    NotificationListenerFilter getListenerFilter(ComponentName cn, int userId);
//
//    void setListenerFilter(ComponentName cn, int userId, NotificationListenerFilter nlf);

    void migrateNotificationFilter(INotificationListener token, int defaultTypes, List<String> disallowedPkgs);

    void setToastRateLimitingEnabled(boolean enable);


    abstract class Stub extends Binder implements INotificationManager {
        public static INotificationManager asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }
}