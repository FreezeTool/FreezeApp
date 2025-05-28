package android.app.smartspace.uitemplatedata;

import android.annotation.SuppressLint;
import android.app.smartspace.SmartspaceTarget.FeatureType;
import android.app.smartspace.SmartspaceTarget.UiTemplateType;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * Holds all the relevant data needed to render a Smartspace card with the default Ui Template.
 * <ul>
 *     <li> title_text (may contain a start drawable) </li>
 *     <li> subtitle_text (may contain a start drawable) . supplemental_subtitle_text (may
 *     contain a start drawable) </li>
 *
 *     <li> supplemental_text (contain a start drawable) . do_not_disturb_view </li>
 *     Or
 *     <li> next_alarm_text (contain a start drawable) + supplemental_alarm_text .
 *     do_not_disturb_view </li>
 * </ul>
 *
 * @hide
 */
@SuppressLint("ParcelNotFinal")
public class BaseTemplateData {

    /**
     * {@link UiTemplateType} indicating the template type of this template data.
     *
     * @see UiTemplateType
     */
    @UiTemplateType
    private final int mTemplateType;

    /**
     * Title text and title icon are shown at the first row. When both are absent, the date view
     * will be used, which has its own tap action applied to the title area.
     * <p>
     * Primary tap action for the entire card, including the blank spaces, except: 1. When title is
     * absent, the date view's default tap action is used; 2. Subtitle/Supplemental subtitle uses
     * its own tap action if being set; 3. Secondary card uses its own tap action if being set.
     */
    @Nullable
    private final SubItemInfo mPrimaryItem;


    /**
     * Subtitle text and icon are shown at the second row.
     */
    @Nullable
    private final SubItemInfo mSubtitleItem;

    /**
     * Supplemental subtitle text and icon are shown at the second row following the subtitle text.
     * Mainly used for weather info on non-weather card.
     */
    @Nullable
    private final SubItemInfo mSubtitleSupplementalItem;

    /**
     * Supplemental line is shown at the third row.
     */
    @Nullable
    private final SubItemInfo mSupplementalLineItem;

    /**
     * Supplemental alarm item is specifically used for holiday alarm, which is appended to "next
     * alarm". This is also shown at the third row, but won't be shown the same time with
     * mSupplementalLineItem.
     */
    @Nullable
    private final SubItemInfo mSupplementalAlarmItem;

    /**
     * The layout weight info for the card, which indicates how much space it should occupy on the
     * screen. Default weight is 0.
     */
    private final int mLayoutWeight;

    /**
     * Should ONLY used by subclasses. For the general instance creation, please use
     * SmartspaceDefaultUiTemplateData.Builder.
     */
    BaseTemplateData(@UiTemplateType int templateType,
                     @Nullable SubItemInfo primaryItem,
                     @Nullable SubItemInfo subtitleItem,
                     @Nullable SubItemInfo subtitleSupplementalItem,
                     @Nullable SubItemInfo supplementalLineItem,
                     @Nullable SubItemInfo supplementalAlarmItem,
                     int layoutWeight) {
        mTemplateType = templateType;
        mPrimaryItem = primaryItem;
        mSubtitleItem = subtitleItem;
        mSubtitleSupplementalItem = subtitleSupplementalItem;
        mSupplementalLineItem = supplementalLineItem;
        mSupplementalAlarmItem = supplementalAlarmItem;
        mLayoutWeight = layoutWeight;
    }

    /**
     * Returns the template type. By default is UNDEFINED.
     */
    @UiTemplateType
    public int getTemplateType() {
        return mTemplateType;
    }

    /**
     * Returns the primary item (the first line).
     */
    @Nullable
    public SubItemInfo getPrimaryItem() {
        return mPrimaryItem;
    }

    /**
     * Returns the subtitle item (the second line).
     */
    @Nullable
    public SubItemInfo getSubtitleItem() {
        return mSubtitleItem;
    }

    /**
     * Returns the subtitle's supplemental item (the second line following the subtitle).
     */
    @Nullable
    public SubItemInfo getSubtitleSupplementalItem() {
        return mSubtitleSupplementalItem;
    }

    /**
     * Returns the supplemental line item (the 3rd line).
     */
    @Nullable
    public SubItemInfo getSupplementalLineItem() {
        return mSupplementalLineItem;
    }

    /**
     * Returns the supplemental alarm item (the 3rd line).
     */
    @Nullable
    public SubItemInfo getSupplementalAlarmItem() {
        return mSupplementalAlarmItem;
    }

    /**
     * Returns the card layout weight info. Default weight is 0.
     */
    public int getLayoutWeight() {
        return mLayoutWeight;
    }


    public static class Builder {
        @UiTemplateType
        private final int mTemplateType;

        private SubItemInfo mPrimaryItem;
        private SubItemInfo mSubtitleItem;
        private SubItemInfo mSubtitleSupplementalItem;
        private SubItemInfo mSupplementalLineItem;
        private SubItemInfo mSupplementalAlarmItem;
        private int mLayoutWeight;

        /**
         * A builder for {@link BaseTemplateData}. By default sets the layout weight to be 0.
         *
         * @param templateType the {@link UiTemplateType} of this template data.
         */
        public Builder(@UiTemplateType int templateType) {
            mTemplateType = templateType;
            mLayoutWeight = 0;
        }

        /**
         * Should ONLY be used by the subclasses
         */
        @UiTemplateType
        @SuppressLint("GetterOnBuilder")
        int getTemplateType() {
            return mTemplateType;
        }

        /**
         * Should ONLY be used by the subclasses
         */
        @Nullable
        @SuppressLint("GetterOnBuilder")
        SubItemInfo getPrimaryItem() {
            return mPrimaryItem;
        }

        /**
         * Should ONLY be used by the subclasses
         */
        @Nullable
        @SuppressLint("GetterOnBuilder")
        SubItemInfo getSubtitleItem() {
            return mSubtitleItem;
        }

        /**
         * Should ONLY be used by the subclasses
         */
        @Nullable
        @SuppressLint("GetterOnBuilder")
        SubItemInfo getSubtitleSupplemtnalItem() {
            return mSubtitleSupplementalItem;
        }

        /**
         * Should ONLY be used by the subclasses
         */
        @Nullable
        @SuppressLint("GetterOnBuilder")
        SubItemInfo getSupplementalLineItem() {
            return mSupplementalLineItem;
        }

        /**
         * Should ONLY be used by the subclasses
         */
        @Nullable
        @SuppressLint("GetterOnBuilder")
        SubItemInfo getSupplementalAlarmItem() {
            return mSupplementalAlarmItem;
        }

        /**
         * Should ONLY be used by the subclasses
         */
        @SuppressLint("GetterOnBuilder")
        int getLayoutWeight() {
            return mLayoutWeight;
        }

        /**
         * Sets the card primary item.
         */
        @NonNull
        public Builder setPrimaryItem(@NonNull SubItemInfo primaryItem) {
            mPrimaryItem = primaryItem;
            return this;
        }

        /**
         * Sets the card subtitle item.
         */
        @NonNull
        public Builder setSubtitleItem(@NonNull SubItemInfo subtitleItem) {
            mSubtitleItem = subtitleItem;
            return this;
        }

        /**
         * Sets the card subtitle's supplemental item.
         */
        @NonNull
        public Builder setSubtitleSupplementalItem(@NonNull SubItemInfo subtitleSupplementalItem) {
            mSubtitleSupplementalItem = subtitleSupplementalItem;
            return this;
        }

        /**
         * Sets the card supplemental line item.
         */
        @NonNull
        public Builder setSupplementalLineItem(@NonNull SubItemInfo supplementalLineItem) {
            mSupplementalLineItem = supplementalLineItem;
            return this;
        }

        /**
         * Sets the card supplemental alarm item.
         */
        @NonNull
        public Builder setSupplementalAlarmItem(@NonNull SubItemInfo supplementalAlarmItem) {
            mSupplementalAlarmItem = supplementalAlarmItem;
            return this;
        }

        /**
         * Sets the layout weight.
         */
        @NonNull
        public Builder setLayoutWeight(int layoutWeight) {
            mLayoutWeight = layoutWeight;
            return this;
        }

        /**
         * Builds a new SmartspaceDefaultUiTemplateData instance.
         */
        @NonNull
        public BaseTemplateData build() {
            return new BaseTemplateData(
                    mTemplateType,
                    mPrimaryItem,
                    mSubtitleItem,
                    mSubtitleSupplementalItem,
                    mSupplementalLineItem,
                    mSupplementalAlarmItem,
                    mLayoutWeight);
        }
    }

    /**
     * Holds all the rendering and logging info needed for a sub item within the base card.
     */
    public static final class SubItemInfo {

        /**
         * The text information for the subitem, which will be rendered as it's text content.
         */
        @Nullable
        private final Text mText;

        /**
         * The icon for the subitem, which will be rendered as a drawable in front of the text.
         */
        @Nullable
        private final Icon mIcon;

        /**
         * The tap action for the subitem.
         */
        @Nullable
        private final TapAction mTapAction;

        /**
         * The logging info for the subitem.
         */
        @Nullable
        private final SubItemLoggingInfo mLoggingInfo;


        private SubItemInfo(@Nullable Text text,
                            @Nullable Icon icon,
                            @Nullable TapAction tapAction,
                            @Nullable SubItemLoggingInfo loggingInfo) {
            mText = text;
            mIcon = icon;
            mTapAction = tapAction;
            mLoggingInfo = loggingInfo;
        }

        /**
         * Returns the subitem's text.
         */
        @Nullable
        public Text getText() {
            return mText;
        }

        /**
         * Returns the subitem's icon.
         */
        @Nullable
        public Icon getIcon() {
            return mIcon;
        }

        /**
         * Returns the subitem's tap action.
         */
        @Nullable
        public TapAction getTapAction() {
            return mTapAction;
        }

        /**
         * Returns the subitem's logging info.
         */
        @Nullable
        public SubItemLoggingInfo getLoggingInfo() {
            return mLoggingInfo;
        }

        public static final class Builder {

            private Text mText;
            private Icon mIcon;
            private TapAction mTapAction;
            private SubItemLoggingInfo mLoggingInfo;

            /**
             * Sets the sub item's text.
             */
            @NonNull
            public Builder setText(@NonNull Text text) {
                mText = text;
                return this;
            }

            /**
             * Sets the sub item's icon.
             */
            @NonNull
            public Builder setIcon(@NonNull Icon icon) {
                mIcon = icon;
                return this;
            }

            /**
             * Sets the sub item's tap action.
             */
            @NonNull
            public Builder setTapAction(@NonNull TapAction tapAction) {
                mTapAction = tapAction;
                return this;
            }

            /**
             * Sets the sub item's logging info.
             */
            @NonNull
            public Builder setLoggingInfo(@NonNull SubItemLoggingInfo loggingInfo) {
                mLoggingInfo = loggingInfo;
                return this;
            }

            /**
             * Builds a new {@link SubItemInfo} instance.
             *
             * @throws IllegalStateException if all the data field is empty.
             */
            @NonNull
            public SubItemInfo build() {
                throw new RuntimeException("STUB");
            }
        }
    }

    /**
     * Holds all the logging info needed for a sub item within the base card. For example, the
     * supplemental-subtitle part should have its own logging info.
     */
    public static final class SubItemLoggingInfo {

        /**
         * A unique instance id for the sub item.
         */
        private final int mInstanceId;

        /**
         * {@link FeatureType} indicating the feature type of this subitem.
         *
         * @see FeatureType
         */
        @FeatureType
        private final int mFeatureType;

        /**
         * The data source's package name for this sub item.
         */
        @Nullable
        private final CharSequence mPackageName;

        SubItemLoggingInfo(@NonNull Parcel in) {
            mInstanceId = in.readInt();
            mFeatureType = in.readInt();
            mPackageName = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        }

        private SubItemLoggingInfo(int instanceId, @FeatureType int featureType,
                                   @Nullable CharSequence packageName) {
            mInstanceId = instanceId;
            mFeatureType = featureType;
            mPackageName = packageName;
        }

        public int getInstanceId() {
            return mInstanceId;
        }

        @FeatureType
        public int getFeatureType() {
            return mFeatureType;
        }

        @Nullable
        public CharSequence getPackageName() {
            return mPackageName;
        }

        public static final class Builder {

            private final int mInstanceId;
            private final int mFeatureType;
            private CharSequence mPackageName;

            /**
             * A builder for {@link SubItemLoggingInfo}.
             *
             * @param instanceId  A unique instance id for the sub item
             * @param featureType The feature type id for this sub item
             */
            public Builder(int instanceId, @FeatureType int featureType) {
                mInstanceId = instanceId;
                mFeatureType = featureType;
            }

            /**
             * Sets the sub item's data source package name.
             */
            @NonNull
            public Builder setPackageName(@NonNull CharSequence packageName) {
                mPackageName = packageName;
                return this;
            }

            /**
             * Builds a new {@link SubItemLoggingInfo} instance.
             */
            @NonNull
            public SubItemLoggingInfo build() {
                return new SubItemLoggingInfo(mInstanceId, mFeatureType, mPackageName);
            }
        }
    }
}