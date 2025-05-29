package android.net.wifi;

import android.annotation.TargetApi;
import android.content.IClipboard;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import java.util.List;
import java.util.Map;

public interface IWifiManager {

//    long getSupportedFeatures();
//
//
//    void getWifiActivityEnergyInfoAsync(IOnWifiActivityEnergyInfoListener listener);
//
//    void setScreenOnScanSchedule(int[] scanScheduleSeconds, int[] scanType);
//
//    void setOneShotScreenOnConnectivityScanDelayMillis(int delayMs);
//
//    ParceledListSlice getConfiguredNetworks(String packageName, String featureId, boolean callerNetworksOnly);
//
//    ParceledListSlice getPrivilegedConfiguredNetworks(String packageName, String featureId, Bundle extras);
//
//    WifiConfiguration getPrivilegedConnectedNetwork(String packageName, String featureId, Bundle extras);
//
//    Map getAllMatchingFqdnsForScanResults(List<ScanResult> scanResult);
//
//    void setSsidsAllowlist(String packageName, List<WifiSsid> ssids);
//
//    List getSsidsAllowlist(String packageName);
//
//    Map getMatchingOsuProviders(List<ScanResult> scanResult);
//
//    Map getMatchingPasspointConfigsForOsuProviders(List<OsuProvider> osuProviders);
//
//    int addOrUpdateNetwork(WifiConfiguration config, String packageName, Bundle extras);
//
//    WifiManager.AddNetworkResult addOrUpdateNetworkPrivileged(WifiConfiguration config, String packageName);
//
//    boolean addOrUpdatePasspointConfiguration(PasspointConfiguration config, String packageName);
//
//    boolean removePasspointConfiguration(String fqdn, String packageName);
//
//    List<PasspointConfiguration> getPasspointConfigurations(String packageName);
//
//    List<WifiConfiguration> getWifiConfigsForPasspointProfiles(List<String> fqdnList);
//
//    void queryPasspointIcon(long bssid, String fileName);
//
//    int matchProviderWithCurrentNetwork(String fqdn);
//
//    boolean removeNetwork(int netId, String packageName);
//
//    boolean removeNonCallerConfiguredNetworks(String packageName);
//
//    boolean enableNetwork(int netId, boolean disableOthers, String packageName);
//
//    boolean disableNetwork(int netId, String packageName);
//
//    void allowAutojoinGlobal(boolean choice);
//
//    void queryAutojoinGlobal(IBooleanListener listener);
//
//    void allowAutojoin(int netId, boolean choice);
//
//    void allowAutojoinPasspoint(String fqdn, boolean enableAutoJoin);
//
//    void setMacRandomizationSettingPasspointEnabled(String fqdn, boolean enable);
//
//    void setPasspointMeteredOverride(String fqdn, int meteredOverride);
//
//    boolean startScan(String packageName, String featureId);
//
//    List<ScanResult> getScanResults(String callingPackage, String callingFeatureId);
//
//    boolean disconnect(String packageName);
//
//    boolean reconnect(String packageName);
//
//    boolean reassociate(String packageName);

    @TargetApi(Build.VERSION_CODES.R)
    WifiInfo getConnectionInfo(String callingPackage, String callingFeatureId);

    @TargetApi(Build.VERSION_CODES.O_MR1)
    WifiInfo getConnectionInfo(String callingPackage);


    WifiInfo getConnectionInfo();

//    boolean setWifiEnabled(String packageName, boolean enable);
//
//    int getWifiEnabledState();
//
//    void registerDriverCountryCodeChangedListener(
//            IOnWifiDriverCountryCodeChangedListener listener, String packageName,
//            String featureId);
//
//    void unregisterDriverCountryCodeChangedListener(
//            IOnWifiDriverCountryCodeChangedListener listener);
//
//    String getCountryCode(String packageName, String featureId);
//
//    void setOverrideCountryCode(String country);
//
//    void clearOverrideCountryCode();
//
//    void setDefaultCountryCode(String country);
//
//    boolean is24GHzBandSupported();
//
//    boolean is5GHzBandSupported();
//
//    boolean is6GHzBandSupported();
//
//    boolean is60GHzBandSupported();
//
//    boolean isWifiStandardSupported(int standard);
//
//    DhcpInfo getDhcpInfo(String packageName);
//
//    void setScanAlwaysAvailable(boolean isAvailable, String packageName);
//
//    boolean isScanAlwaysAvailable();
//
//    boolean acquireWifiLock(IBinder lock, int lockType, String tag, WorkSource ws);
//
//    void updateWifiLockWorkSource(IBinder lock, WorkSource ws);
//
//    boolean releaseWifiLock(IBinder lock);
//
//    void initializeMulticastFiltering();
//
//    boolean isMulticastEnabled();
//
//    void acquireMulticastLock(IBinder binder, String tag);
//
//    void releaseMulticastLock(String tag);
//
//    void updateInterfaceIpState(String ifaceName, int mode);
//
//    boolean isDefaultCoexAlgorithmEnabled();
//
//    void setCoexUnsafeChannels(List<CoexUnsafeChannel>unsafeChannels, int mandatoryRestrictions);
//
//    void registerCoexCallback(ICoexCallback callback);
//
//    void unregisterCoexCallback(ICoexCallback callback);
//
//    boolean startSoftAp(WifiConfiguration wifiConfig, String packageName);
//
//    boolean startTetheredHotspot(SoftApConfiguration softApConfig, String packageName);
//
//    boolean stopSoftAp();
//
//    int startLocalOnlyHotspot(ILocalOnlyHotspotCallback callback, String packageName,
//                              String featureId, SoftApConfiguration customConfig, Bundle extras);
//
//    void stopLocalOnlyHotspot();
//
//    void registerLocalOnlyHotspotSoftApCallback(ISoftApCallback callback, Bundle extras);
//
//    void unregisterLocalOnlyHotspotSoftApCallback(ISoftApCallback callback, Bundle extras);
//
//    void startWatchLocalOnlyHotspot(ILocalOnlyHotspotCallback callback);
//
//    void stopWatchLocalOnlyHotspot();
//
//    @UnsupportedAppUsage
//    int getWifiApEnabledState();
//
//    @UnsupportedAppUsage
//    WifiConfiguration getWifiApConfiguration();
//
//    SoftApConfiguration getSoftApConfiguration();
//
//    boolean setWifiApConfiguration(WifiConfiguration wifiConfig, String packageName);
//
//    boolean setSoftApConfiguration(SoftApConfiguration softApConfig, String packageName);
//
//    void notifyUserOfApBandConversion(String packageName);
//
//    void enableTdls(String remoteIPAddress, boolean enable);
//
//    void enableTdlsWithMacAddress(String remoteMacAddress, boolean enable);
//
//    String getCurrentNetworkWpsNfcConfigurationToken();
//
//    void enableVerboseLogging(int verbose);
//
//    int getVerboseLoggingLevel();
//
//    void disableEphemeralNetwork(String SSID, String packageName);
//
//    void factoryReset(String packageName);
//
//    @UnsupportedAppUsage(maxTargetSdk = 30, trackingBug = 170729553)
//    Network getCurrentNetwork();
//
//    byte[] retrieveBackupData();
//
//    void restoreBackupData(byte[]data);
//
//    byte[] retrieveSoftApBackupData();
//
//    SoftApConfiguration restoreSoftApBackupData(byte[]data);
//
//    void restoreSupplicantBackupData(byte[]supplicantData, byte[]ipConfigData);
//
//    void startSubscriptionProvisioning(OsuProvider provider, IProvisioningCallback callback);
//
//    void registerSoftApCallback(ISoftApCallback callback);
//
//    void unregisterSoftApCallback(ISoftApCallback callback);
//
//    void addWifiVerboseLoggingStatusChangedListener(IWifiVerboseLoggingStatusChangedListener listener);
//
//    void removeWifiVerboseLoggingStatusChangedListener(IWifiVerboseLoggingStatusChangedListener listener);
//
//    void addOnWifiUsabilityStatsListener(IOnWifiUsabilityStatsListener listener);
//
//    void removeOnWifiUsabilityStatsListener(IOnWifiUsabilityStatsListener listener);
//
//    void registerTrafficStateCallback(ITrafficStateCallback callback);
//
//    void unregisterTrafficStateCallback(ITrafficStateCallback callback);
//
//    void registerNetworkRequestMatchCallback(INetworkRequestMatchCallback callback);
//
//    void unregisterNetworkRequestMatchCallback(INetworkRequestMatchCallback callback);
//
//    int addNetworkSuggestions(List<WifiNetworkSuggestion>networkSuggestions, String packageName,
//                              String featureId);
//
//    int removeNetworkSuggestions(List<WifiNetworkSuggestion>networkSuggestions, String packageName, int action);
//
//    List<WifiNetworkSuggestion> getNetworkSuggestions(String packageName);
//
//    String[] getFactoryMacAddresses();
//
//    void setDeviceMobilityState(int state);
//
//    void startDppAsConfiguratorInitiator(IBinder binder, String packageName,
//                                         String enrolleeUri, int selectedNetworkId, int netRole, IDppCallback callback);
//
//    void startDppAsEnrolleeInitiator(IBinder binder, String configuratorUri,
//                                     IDppCallback callback);
//
//    void startDppAsEnrolleeResponder(IBinder binder, String deviceInfo, int curve,
//                                     IDppCallback callback);
//
//    void stopDppSession();
//
//    void updateWifiUsabilityScore(int seqNum, int score, int predictionHorizonSec);
//
//
//
//    void connect(WifiConfiguration config, int netId, IActionListener listener, String packageName);
//
//
//
//    void save(WifiConfiguration config, IActionListener listener, String packageName);
//
//
//
//    void forget(int netId, IActionListener listener);
//
//    void registerScanResultsCallback(IScanResultsCallback callback);
//
//    void unregisterScanResultsCallback(IScanResultsCallback callback);
//
//    void registerSuggestionConnectionStatusListener(ISuggestionConnectionStatusListener listener, String packageName, String featureId);
//
//    void unregisterSuggestionConnectionStatusListener(ISuggestionConnectionStatusListener listener, String packageName);
//
//    int calculateSignalLevel(int rssi);
//
//    List<WifiConfiguration> getWifiConfigForMatchedNetworkSuggestionsSharedWithUser(List<ScanResult>scanResults);
//
//    boolean setWifiConnectedNetworkScorer(IBinder binder, IWifiConnectedNetworkScorer scorer);
//
//    void clearWifiConnectedNetworkScorer();
//
//    void setExternalPnoScanRequest(IBinder binder, IPnoScanResultsCallback callback, List<WifiSsid>ssids, int[]frequencies, String packageName, String featureId);
//
//    void clearExternalPnoScanRequest();
//
//    void getLastCallerInfoForApi(int api, ILastCallerListener listener);
//
//    /**
//     * Return the Map of {@link WifiNetworkSuggestion} and the list of <ScanResult>
//     */
//    Map getMatchingScanResults(List<WifiNetworkSuggestion>networkSuggestions, List<ScanResult>scanResults, String callingPackage, String callingFeatureId);
//
//    void setScanThrottleEnabled(boolean enable);
//
//    boolean isScanThrottleEnabled();
//
//    Map getAllMatchingPasspointProfilesForScanResults(List<ScanResult>scanResult);
//
//    void setAutoWakeupEnabled(boolean enable);
//
//    boolean isAutoWakeupEnabled();
//
//    void startRestrictingAutoJoinToSubscriptionId(int subId);
//
//    void stopRestrictingAutoJoinToSubscriptionId();
//
//    void setCarrierNetworkOffloadEnabled(int subscriptionId, boolean merged, boolean enabled);
//
//    boolean isCarrierNetworkOffloadEnabled(int subscriptionId, boolean merged);
//
//    void registerSubsystemRestartCallback(ISubsystemRestartCallback callback);
//
//    void unregisterSubsystemRestartCallback(ISubsystemRestartCallback callback);
//
//    void restartWifiSubsystem();
//
//    void addSuggestionUserApprovalStatusListener(ISuggestionUserApprovalStatusListener listener, String packageName);
//
//    void removeSuggestionUserApprovalStatusListener(ISuggestionUserApprovalStatusListener listener, String packageName);
//
//    void setEmergencyScanRequestInProgress(boolean inProgress);
//
//    void removeAppState(int targetAppUid, String targetApppackageName);
//
//    boolean setWifiScoringEnabled(boolean enabled);
//
//    void flushPasspointAnqpCache(String packageName);
//
//    List<WifiAvailableChannel> getUsableChannels(int band, int mode, int filter);
//
//    boolean isWifiPasspointEnabled();
//
//    void setWifiPasspointEnabled(boolean enabled);
//
//    int getStaConcurrencyForMultiInternetMode();
//
//    boolean setStaConcurrencyForMultiInternetMode(int mode);
//
//    void notifyMinimumRequiredWifiSecurityLevelChanged(int level);
//
//    void notifyWifiSsidPolicyChanged(int policyType, List<WifiSsid>ssids);
//
//    String[] getOemPrivilegedWifiAdminPackages();
//
//    void replyToP2pInvitationReceivedDialog(int dialogId, boolean accepted, String optionalPin);
//
//    void replyToSimpleDialog(int dialogId, int reply);
//
//    void addCustomDhcpOptions(WifiSsid ssid, byte[]oui, List<DhcpOption>options);
//
//    void removeCustomDhcpOptions(WifiSsid ssid, byte[]oui);
//
//    void reportCreateInterfaceImpact(String packageName, int interfaceType, boolean requireNewInterface, IInterfaceCreationInfoCallback callback);
//

    abstract class Stub extends Binder implements IWifiManager {
        public static IWifiManager asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }

}
