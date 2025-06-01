package android.net;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.Set;

public final class NetworkTemplate {
    private static final String TAG = NetworkTemplate.class.getSimpleName();


    public @interface TemplateMatchRule {
    }

    /**
     * Match rule to match cellular networks with given Subscriber Ids.
     */
    public static final int MATCH_MOBILE = 1;
    /**
     * Match rule to match wifi networks.
     */
    public static final int MATCH_WIFI = 4;
    /**
     * Match rule to match ethernet networks.
     */
    public static final int MATCH_ETHERNET = 5;
    /**
     * Match rule to match bluetooth networks.
     */
    public static final int MATCH_BLUETOOTH = 8;

    public static final int MATCH_PROXY = 9;
    /**
     * Match rule to match all networks with subscriberId inside the template. Some carriers
     * may offer non-cellular networks like WiFi, which will be matched by this rule.
     */
    public static final int MATCH_CARRIER = 10;

    public static final int MATCH_TEST = 11;

    // TODO: Remove this and replace all callers with WIFI_NETWORK_KEY_ALL.

    public static final String WIFI_NETWORKID_ALL = null;

    /**
     * Wi-Fi Network Key is never supposed to be null (if it is, it is a bug that
     * should be fixed), so it's not possible to want to match null vs
     * non-null. Therefore it's fine to use null as a sentinel for Wifi Network Key.
     */
    public static final String WIFI_NETWORK_KEY_ALL = WIFI_NETWORKID_ALL;

    /**
     * Include all network types when filtering. This is meant to merge in with the
     * {@code TelephonyManager.NETWORK_TYPE_*} constants, and thus needs to stay in sync.
     */
    public static final int NETWORK_TYPE_ALL = -1;


    public @interface OemManaged {
    }

    /**
     * Value to match both OEM managed and unmanaged networks (all networks).
     */
    public static  int OEM_MANAGED_ALL = -1;
    /**
     * Value to match networks which are not OEM managed.
     */
    public static  int OEM_MANAGED_NO ;
    /**
     * Value to match any OEM managed network.
     */
    public static  int OEM_MANAGED_YES = -2;

    public static  int OEM_MANAGED_PAID;

    public static int OEM_MANAGED_PRIVATE;

    public static NetworkTemplate buildTemplateMobileAll(@NonNull String subscriberId) {
        throw new RuntimeException();
    }

    public static NetworkTemplate buildTemplateMobileWildcard() {
        throw new RuntimeException();
    }

    public static NetworkTemplate buildTemplateWifiWildcard() {
        return new NetworkTemplate.Builder(MATCH_WIFI).build();
    }

    public static NetworkTemplate buildTemplateWifi() {
        return buildTemplateWifiWildcard();
    }

    public static NetworkTemplate buildTemplateEthernet() {
        return new NetworkTemplate.Builder(MATCH_ETHERNET).build();
    }


    public static NetworkTemplate buildTemplateBluetooth() {
        // TODO : this is part of hidden-o txt, does that mean it should be annotated with
        // @UnsupportedAppUsage(maxTargetSdk = O) ? If yes, can't throwAtLeastU() lest apps
        // targeting O- crash on those devices.
        return new NetworkTemplate.Builder(MATCH_BLUETOOTH).build();
    }


    public static NetworkTemplate buildTemplateProxy() {
        // TODO : this is part of hidden-o txt, does that mean it should be annotated with
        // @UnsupportedAppUsage(maxTargetSdk = O) ? If yes, can't throwAtLeastU() lest apps
        // targeting O- crash on those devices.
        return new NetworkTemplate(MATCH_PROXY, null, null);
    }

    /**
     * Template to match all metered carrier networks with the given IMSI.
     *
     * @hide
     */
    // TODO(b/273963543): Remove this method. This can only be done after there are no more callers,
    //  including in OEM code which can access this by linking against the framework.
    public static NetworkTemplate buildTemplateCarrierMetered(@NonNull String subscriberId) {
        throw new RuntimeException();
    }


    public static NetworkTemplate buildTemplateMobileWithRatType(String subscriberId,
                                                                 int ratType, int metered) {
        throw new RuntimeException();
    }

    public static NetworkTemplate buildTemplateWifi(@NonNull String wifiNetworkKey) {
        throw new RuntimeException();
    }

    public static NetworkTemplate buildTemplateWifi(String wifiNetworkKey,
                                                    String subscriberId) {
        throw new RuntimeException();
    }


    public NetworkTemplate(int matchRule, String subscriberId, String wifiNetworkKey) {
        // Older versions used to only match MATCH_MOBILE and MATCH_MOBILE_WILDCARD templates
        // to metered networks. It is now possible to match mobile with any meteredness, but
        // in order to preserve backward compatibility of @UnsupportedAppUsage methods, this
        // constructor passes METERED_YES for these types.
        // For backwards compatibility, still accept old wildcard match rules (6 and 7 for
        // MATCH_{MOBILE,WIFI}_WILDCARD) but convert into functionally equivalent non-wildcard
        // ones.
        throw new RuntimeException();
    }


    public NetworkTemplate(int matchRule, String subscriberId, String[] matchSubscriberIds,
                           String wifiNetworkKey) {
        throw new RuntimeException();
    }

    /**
     * @hide
     */
    // TODO(b/269974916): Remove this method after Android U is released.
    //  This is only used by CTS of Android T.
    public NetworkTemplate(int matchRule, String subscriberId, String[] matchSubscriberIds,
                           String[] matchWifiNetworkKeys, int metered, int roaming,
                           int defaultNetwork, int ratType, int oemManaged, int subscriberIdMatchRule) {
        throw new RuntimeException();
    }

    public NetworkTemplate(int matchRule, String[] matchSubscriberIds,
                           String[] matchWifiNetworkKeys, int metered, int roaming, int defaultNetwork,
                           int ratType, int oemManaged) {

    }


    public boolean isMatchRuleMobile() {
        throw new RuntimeException();
    }

    /**
     * Get match rule of the template. See {@code MATCH_*}.
     */
    public int getMatchRule() {
        throw new RuntimeException();
    }

    public String getSubscriberId() {
        throw new RuntimeException();
    }

    /**
     * Get set of subscriber Ids of the template.
     */
    @NonNull
    public Set<String> getSubscriberIds() {
        throw new RuntimeException();
    }


    @NonNull
    public Set<String> getWifiNetworkKeys() {
        throw new RuntimeException();
    }

    /**
     * @hide
     */
    // TODO: Remove this and replace all callers with {@link #getWifiNetworkKeys()}.
    public String getNetworkId() {
        return getWifiNetworkKeys().isEmpty() ? null : getWifiNetworkKeys().iterator().next();
    }

    public int getMeteredness() {
        throw new RuntimeException();
    }

    /**
     * Get roaming filter of the template.
     */
    public int getRoaming() {
        throw new RuntimeException();
    }

    /**
     * Get the default network status filter of the template.
     */
    public int getDefaultNetworkStatus() {
        throw new RuntimeException();
    }

    /**
     * Get the Radio Access Technology(RAT) type filter of the template.
     */
    public int getRatType() {
        throw new RuntimeException();
    }

    /**
     * Get the OEM managed filter of the template. See {@code OEM_MANAGED_*} or
     * {@code android.net.NetworkIdentity#OEM_*}.
     */
    @OemManaged
    public int getOemManaged() {
        throw new RuntimeException();
    }

//    public boolean matches(@NonNull NetworkIdentity ident) {
//        throw new RuntimeException();
//    }


    public boolean matchesSubscriberId(String subscriberId) {
        throw new RuntimeException();
    }

    public static NetworkTemplate normalize(NetworkTemplate template, String[] merged) {
        throw new RuntimeException();
    }

    /**
     * Examine the given template and normalize it.
     * We pick the "lowest" merged subscriber as the primary
     * for key purposes, and expand the template to match all other merged
     * subscribers.
     * <p>
     * There can be multiple merged subscriberIds for multi-SIM devices.
     *
     * <p>
     * For example, given an incoming template matching B, and the currently
     * active merge set [A,B], we'd return a new template that matches both A and B.
     *
     * @hide
     */
    // TODO(b/273963543): Remove this method. This can only be done after there are no more callers,
    //  including in OEM code which can access this by linking against the framework.
    public static NetworkTemplate normalize(NetworkTemplate template, List<String[]> mergedList) {
        throw new RuntimeException();
    }


    /**
     * Builder class for NetworkTemplate.
     */
    public static final class Builder {

        public Builder(@TemplateMatchRule final int matchRule) {
            throw new RuntimeException();
        }

        /**
         * Set the Subscriber Ids. Calling this function with an empty set represents
         * the intention of matching any Subscriber Ids.
         *
         * @param subscriberIds the list of Subscriber Ids.
         * @return this builder.
         */
        @NonNull
        public Builder setSubscriberIds(@NonNull Set<String> subscriberIds) {
            throw new RuntimeException();
        }

        @NonNull
        public Builder setWifiNetworkKeys(@NonNull Set<String> wifiNetworkKeys) {
            throw new RuntimeException();
        }

        /**
         * Set the meteredness filter.
         *
         * @param metered the meteredness filter.
         * @return this builder.
         */
        @NonNull
        public Builder setMeteredness(int metered) {
            throw new RuntimeException();
        }

        /**
         * Set the roaming filter.
         *
         * @param roaming the roaming filter.
         * @return this builder.
         */
        @NonNull
        public Builder setRoaming(int roaming) {
            throw new RuntimeException();
        }

        /**
         * Set the default network status filter.
         *
         * @param defaultNetwork the default network status filter.
         * @return this builder.
         */
        @NonNull
        public Builder setDefaultNetworkStatus(int defaultNetwork) {
            throw new RuntimeException();
        }

        /**
         * Set the Radio Access Technology(RAT) type filter.
         *
         * @param ratType the Radio Access Technology(RAT) type filter. Use
         *                {@link #NETWORK_TYPE_ALL} to include all network types when filtering.
         *                See {@code TelephonyManager.NETWORK_TYPE_*}.
         * @return this builder.
         */
        @NonNull
        public Builder setRatType(int ratType) {
            throw new RuntimeException();
        }

        /**
         * Set the OEM managed filter.
         *
         * @param oemManaged the match rule to match different type of OEM managed network or
         *                   unmanaged networks. See {@code OEM_MANAGED_*}.
         * @return this builder.
         */
        @NonNull
        public Builder setOemManaged(@OemManaged int oemManaged) {
            throw new RuntimeException();
        }


        /**
         * Builds the instance of the NetworkTemplate.
         *
         * @return the built instance of NetworkTemplate.
         */
        @NonNull
        public NetworkTemplate build() {
            throw new RuntimeException();
        }
    }
}