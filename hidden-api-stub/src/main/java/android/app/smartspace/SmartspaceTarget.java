package android.app.smartspace;

import android.app.smartspace.uitemplatedata.BaseTemplateData;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.net.Uri;
import android.os.UserHandle;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public final class SmartspaceTarget {
    public static final int FEATURE_UNDEFINED = 0;
    public static final int FEATURE_WEATHER = 1;
    public static final int FEATURE_CALENDAR = 2;
    public static final int FEATURE_COMMUTE_TIME = 3;
    public static final int FEATURE_FLIGHT = 4;
    public static final int FEATURE_TIPS = 5;
    public static final int FEATURE_REMINDER = 6;
    public static final int FEATURE_ALARM = 7;
    public static final int FEATURE_ONBOARDING = 8;
    public static final int FEATURE_SPORTS = 9;
    public static final int FEATURE_WEATHER_ALERT = 10;
    public static final int FEATURE_CONSENT = 11;
    public static final int FEATURE_STOCK_PRICE_CHANGE = 12;
    public static final int FEATURE_SHOPPING_LIST = 13;
    public static final int FEATURE_LOYALTY_CARD = 14;
    public static final int FEATURE_MEDIA = 15;
    public static final int FEATURE_BEDTIME_ROUTINE = 16;
    public static final int FEATURE_FITNESS_TRACKING = 17;
    public static final int FEATURE_ETA_MONITORING = 18;
    public static final int FEATURE_MISSED_CALL = 19;
    public static final int FEATURE_PACKAGE_TRACKING = 20;
    public static final int FEATURE_TIMER = 21;
    public static final int FEATURE_STOPWATCH = 22;
    public static final int FEATURE_UPCOMING_ALARM = 23;
    public static final int FEATURE_GAS_STATION_PAYMENT = 24;
    public static final int FEATURE_PAIRED_DEVICE_STATE = 25;
    public static final int FEATURE_DRIVING_MODE = 26;
    public static final int FEATURE_SLEEP_SUMMARY = 27;
    public static final int FEATURE_FLASHLIGHT = 28;
    public static final int FEATURE_TIME_TO_LEAVE = 29;
    public static final int FEATURE_DOORBELL = 30;
    public static final int FEATURE_MEDIA_RESUME = 31;
    public static final int FEATURE_CROSS_DEVICE_TIMER = 32;
    public static final int FEATURE_SEVERE_WEATHER_ALERT = 33;
    public static final int FEATURE_HOLIDAY_ALARM = 34;
    public static final int FEATURE_SAFETY_CHECK = 35;
    public static final int FEATURE_MEDIA_HEADS_UP = 36;
    public static final int FEATURE_STEP_COUNTING = 37;
    public static final int FEATURE_EARTHQUAKE_ALERT = 38;
    public static final int FEATURE_STEP_DATE = 39; // This represents a DATE. "STEP" is a typo.
    public static final int FEATURE_BLAZE_BUILD_PROGRESS = 40;
    public static final int FEATURE_EARTHQUAKE_OCCURRED = 41;


    @IntDef(value = {
            FEATURE_UNDEFINED,
            FEATURE_WEATHER,
            FEATURE_CALENDAR,
            FEATURE_COMMUTE_TIME,
            FEATURE_FLIGHT,
            FEATURE_TIPS,
            FEATURE_REMINDER,
            FEATURE_ALARM,
            FEATURE_ONBOARDING,
            FEATURE_SPORTS,
            FEATURE_WEATHER_ALERT,
            FEATURE_CONSENT,
            FEATURE_STOCK_PRICE_CHANGE,
            FEATURE_SHOPPING_LIST,
            FEATURE_LOYALTY_CARD,
            FEATURE_MEDIA,
            FEATURE_BEDTIME_ROUTINE,
            FEATURE_FITNESS_TRACKING,
            FEATURE_ETA_MONITORING,
            FEATURE_MISSED_CALL,
            FEATURE_PACKAGE_TRACKING,
            FEATURE_TIMER,
            FEATURE_STOPWATCH,
            FEATURE_UPCOMING_ALARM,
            FEATURE_GAS_STATION_PAYMENT,
            FEATURE_PAIRED_DEVICE_STATE,
            FEATURE_DRIVING_MODE,
            FEATURE_SLEEP_SUMMARY,
            FEATURE_FLASHLIGHT,
            FEATURE_TIME_TO_LEAVE,
            FEATURE_DOORBELL,
            FEATURE_MEDIA_RESUME,
            FEATURE_CROSS_DEVICE_TIMER,
            FEATURE_SEVERE_WEATHER_ALERT,
            FEATURE_HOLIDAY_ALARM,
            FEATURE_SAFETY_CHECK,
            FEATURE_MEDIA_HEADS_UP,
            FEATURE_STEP_COUNTING,
            FEATURE_EARTHQUAKE_ALERT,
            FEATURE_STEP_DATE,
            FEATURE_BLAZE_BUILD_PROGRESS,
            FEATURE_EARTHQUAKE_OCCURRED
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface FeatureType {
    }

    public static final int UI_TEMPLATE_UNDEFINED = 0;
    // Default template whose data is represented by {@link BaseTemplateData}. The default
    // template is also a base card for the other types of templates.
    public static final int UI_TEMPLATE_DEFAULT = 1;
    // Sub-image template whose data is represented by {@link SubImageTemplateData}
    public static final int UI_TEMPLATE_SUB_IMAGE = 2;
    // Sub-list template whose data is represented by {@link SubListTemplateData}
    public static final int UI_TEMPLATE_SUB_LIST = 3;
    // Carousel template whose data is represented by {@link CarouselTemplateData}
    public static final int UI_TEMPLATE_CAROUSEL = 4;
    // Head-to-head template whose data is represented by {@link HeadToHeadTemplateData}
    public static final int UI_TEMPLATE_HEAD_TO_HEAD = 5;
    // Combined-cards template whose data is represented by {@link CombinedCardsTemplateData}
    public static final int UI_TEMPLATE_COMBINED_CARDS = 6;
    // Sub-card template whose data is represented by {@link SubCardTemplateData}
    public static final int UI_TEMPLATE_SUB_CARD = 7;
    // Reserved: 8
    // Template type used by non-UI template features for sending logging information in the
    // base template data. This should not be used for UI template features.
    // public static final int UI_TEMPLATE_LOGGING_ONLY = 8;

    /**
     * The types of the Smartspace ui templates.
     *
     * @hide
     */
    @IntDef(value = {
            UI_TEMPLATE_UNDEFINED,
            UI_TEMPLATE_DEFAULT,
            UI_TEMPLATE_SUB_IMAGE,
            UI_TEMPLATE_SUB_LIST,
            UI_TEMPLATE_CAROUSEL,
            UI_TEMPLATE_HEAD_TO_HEAD,
            UI_TEMPLATE_COMBINED_CARDS,
            UI_TEMPLATE_SUB_CARD
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface UiTemplateType {
    }

    private SmartspaceTarget(String smartspaceTargetId,
                             SmartspaceAction headerAction, SmartspaceAction baseAction, long creationTimeMillis,
                             long expiryTimeMillis, float score,
                             List<SmartspaceAction> actionChips,
                             List<SmartspaceAction> iconGrid, int featureType, boolean sensitive,
                             boolean shouldShowExpanded, String sourceNotificationKey,
                             ComponentName componentName, UserHandle userHandle,
                             String associatedSmartspaceTargetId, Uri sliceUri,
                             AppWidgetProviderInfo widget, BaseTemplateData templateData) {
    }

    /**
     * Returns the Id of the target.
     */
    @NonNull
    public String getSmartspaceTargetId() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the header action of the target.
     */
    @Nullable
    public SmartspaceAction getHeaderAction() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the base action of the target.
     */
    @Nullable
    public SmartspaceAction getBaseAction() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the creation time of the target.
     */

    public long getCreationTimeMillis() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the expiry time of the target.
     */

    public long getExpiryTimeMillis() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the score of the target.
     */
    public float getScore() {
        throw new RuntimeException("STUB");
    }

    /**
     * Return the action chips of the target.
     */
    @NonNull
    public List<SmartspaceAction> getActionChips() {
        throw new RuntimeException("STUB");
    }

    /**
     * Return the icons of the target.
     */
    @NonNull
    public List<SmartspaceAction> getIconGrid() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the feature type of the target.
     */
    @FeatureType
    public int getFeatureType() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns whether the target is sensitive or not.
     */
    public boolean isSensitive() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns whether the target should be shown in expanded state.
     */
    public boolean shouldShowExpanded() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the source notification key of the target.
     */
    @Nullable
    public String getSourceNotificationKey() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the component name of the target.
     */
    @NonNull
    public ComponentName getComponentName() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the user handle of the target.
     */
    @NonNull
    public UserHandle getUserHandle() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the id of a target associated with this instance.
     */
    @Nullable
    public String getAssociatedSmartspaceTargetId() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the slice uri, if the target is a slice.
     */
    @Nullable
    public Uri getSliceUri() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the AppWidgetProviderInfo, if the target is a widget.
     */
    @Nullable
    public AppWidgetProviderInfo getWidget() {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns the UI template data.
     */
    @Nullable
    public BaseTemplateData getTemplateData() {
        throw new RuntimeException("STUB");
    }

    public static final class Builder {
        private final String mSmartspaceTargetId;
        private final ComponentName mComponentName;
        private final UserHandle mUserHandle;

        private SmartspaceAction mHeaderAction;
        private SmartspaceAction mBaseAction;
        private long mCreationTimeMillis;
        private long mExpiryTimeMillis;
        private float mScore;
        private List<SmartspaceAction> mActionChips = new ArrayList<>();
        private List<SmartspaceAction> mIconGrid = new ArrayList<>();
        private int mFeatureType;
        private boolean mSensitive;
        private boolean mShouldShowExpanded;
        private String mSourceNotificationKey;
        private String mAssociatedSmartspaceTargetId;
        private Uri mSliceUri;
        private AppWidgetProviderInfo mWidget;
        private BaseTemplateData mTemplateData;

        /**
         * A builder for {@link SmartspaceTarget}.
         *
         * @param smartspaceTargetId the id of this target
         * @param componentName      the componentName of this target
         * @param userHandle         the userHandle of this target
         */
        public Builder(@NonNull String smartspaceTargetId,
                       @NonNull ComponentName componentName, @NonNull UserHandle userHandle) {
            this.mSmartspaceTargetId = smartspaceTargetId;
            this.mComponentName = componentName;
            this.mUserHandle = userHandle;
        }

        /**
         * Sets the header action.
         */
        @NonNull
        public Builder setHeaderAction(@NonNull SmartspaceAction headerAction) {
            this.mHeaderAction = headerAction;
            return this;
        }

        /**
         * Sets the base action.
         */
        @NonNull
        public Builder setBaseAction(@NonNull SmartspaceAction baseAction) {
            this.mBaseAction = baseAction;
            return this;
        }

        /**
         * Sets the creation time.
         */
        @NonNull
        public Builder setCreationTimeMillis(long creationTimeMillis) {
            this.mCreationTimeMillis = creationTimeMillis;
            return this;
        }

        /**
         * Sets the expiration time.
         */
        @NonNull
        public Builder setExpiryTimeMillis(long expiryTimeMillis) {
            this.mExpiryTimeMillis = expiryTimeMillis;
            return this;
        }

        /**
         * Sets the score.
         */
        @NonNull
        public Builder setScore(float score) {
            this.mScore = score;
            return this;
        }

        /**
         * Sets the action chips.
         */
        @NonNull
        public Builder setActionChips(@NonNull List<SmartspaceAction> actionChips) {
            this.mActionChips = actionChips;
            return this;
        }

        /**
         * Sets the icon grid.
         */
        @NonNull
        public Builder setIconGrid(@NonNull List<SmartspaceAction> iconGrid) {
            this.mIconGrid = iconGrid;
            return this;
        }

        /**
         * Sets the feature type.
         */
        @NonNull
        public Builder setFeatureType(int featureType) {
            this.mFeatureType = featureType;
            return this;
        }

        /**
         * Sets whether the contents are sensitive.
         */
        @NonNull
        public Builder setSensitive(boolean sensitive) {
            this.mSensitive = sensitive;
            return this;
        }

        /**
         * Sets whether to show the card as expanded.
         */
        @NonNull
        public Builder setShouldShowExpanded(boolean shouldShowExpanded) {
            this.mShouldShowExpanded = shouldShowExpanded;
            return this;
        }

        /**
         * Sets the source notification key.
         */
        @NonNull
        public Builder setSourceNotificationKey(@NonNull String sourceNotificationKey) {
            this.mSourceNotificationKey = sourceNotificationKey;
            return this;
        }

        /**
         * Sets the associated smartspace target id.
         */
        @NonNull
        public Builder setAssociatedSmartspaceTargetId(
                @NonNull String associatedSmartspaceTargetId) {
            this.mAssociatedSmartspaceTargetId = associatedSmartspaceTargetId;
            return this;
        }


        @NonNull
        public Builder setSliceUri(@NonNull Uri sliceUri) {
            this.mSliceUri = sliceUri;
            return this;
        }


        @NonNull
        public Builder setWidget(@NonNull AppWidgetProviderInfo widget) {
            this.mWidget = widget;
            return this;
        }

        /**
         * Sets the UI template data.
         */
        @NonNull
        public Builder setTemplateData(
                @Nullable BaseTemplateData templateData) {
            mTemplateData = templateData;
            return this;
        }

        /**
         * Builds a new {@link SmartspaceTarget}.
         *
         * @throws IllegalStateException when non null fields are set as null.
         */
        @NonNull
        public SmartspaceTarget build() {
            if (mSmartspaceTargetId == null
                    || mComponentName == null
                    || mUserHandle == null) {
                throw new IllegalStateException("Please assign a value to all @NonNull args.");
            }
            return new SmartspaceTarget(mSmartspaceTargetId,
                    mHeaderAction, mBaseAction, mCreationTimeMillis, mExpiryTimeMillis, mScore,
                    mActionChips, mIconGrid, mFeatureType, mSensitive, mShouldShowExpanded,
                    mSourceNotificationKey, mComponentName, mUserHandle,
                    mAssociatedSmartspaceTargetId, mSliceUri, mWidget, mTemplateData);
        }
    }
}
