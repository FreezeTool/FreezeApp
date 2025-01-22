package android.app.smartspace.uitemplatedata;

import android.app.smartspace.SmartspaceTarget;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class HeadToHeadTemplateData extends BaseTemplateData {

    @Nullable
    private final Text mHeadToHeadTitle;
    @Nullable
    private final Icon mHeadToHeadFirstCompetitorIcon;
    @Nullable
    private final Icon mHeadToHeadSecondCompetitorIcon;
    @Nullable
    private final Text mHeadToHeadFirstCompetitorText;
    @Nullable
    private final Text mHeadToHeadSecondCompetitorText;

    /** Tap action for the head-to-head secondary card. */
    @Nullable
    private final TapAction mHeadToHeadAction;


    private HeadToHeadTemplateData(@SmartspaceTarget.UiTemplateType int templateType,
            @Nullable SubItemInfo primaryItem,
            @Nullable SubItemInfo subtitleItem,
            @Nullable SubItemInfo subtitleSupplementalItem,
            @Nullable SubItemInfo supplementalLineItem,
            @Nullable SubItemInfo supplementalAlarmItem,
            int layoutWeight,
            @Nullable Text headToHeadTitle,
            @Nullable Icon headToHeadFirstCompetitorIcon,
            @Nullable Icon headToHeadSecondCompetitorIcon,
            @Nullable Text headToHeadFirstCompetitorText,
            @Nullable Text headToHeadSecondCompetitorText,
            @Nullable TapAction headToHeadAction) {
        super(templateType, primaryItem, subtitleItem, subtitleSupplementalItem,
                supplementalLineItem, supplementalAlarmItem, layoutWeight);

        mHeadToHeadTitle = headToHeadTitle;
        mHeadToHeadFirstCompetitorIcon = headToHeadFirstCompetitorIcon;
        mHeadToHeadSecondCompetitorIcon = headToHeadSecondCompetitorIcon;
        mHeadToHeadFirstCompetitorText = headToHeadFirstCompetitorText;
        mHeadToHeadSecondCompetitorText = headToHeadSecondCompetitorText;
        mHeadToHeadAction = headToHeadAction;
    }

    /** Returns the head-to-head card's title. */
    @Nullable
    public Text getHeadToHeadTitle() {
        return mHeadToHeadTitle;
    }

    /** Returns the first competitor's icon. */
    @Nullable
    public Icon getHeadToHeadFirstCompetitorIcon() {
        return mHeadToHeadFirstCompetitorIcon;
    }

    /** Returns the second competitor's icon. */
    @Nullable
    public Icon getHeadToHeadSecondCompetitorIcon() {
        return mHeadToHeadSecondCompetitorIcon;
    }

    /** Returns the first competitor's text. */
    @Nullable
    public Text getHeadToHeadFirstCompetitorText() {
        return mHeadToHeadFirstCompetitorText;
    }

    /** Returns the second competitor's text. */
    @Nullable
    public Text getHeadToHeadSecondCompetitorText() {
        return mHeadToHeadSecondCompetitorText;
    }

    /** Returns the head-to-head card's tap action. */
    @Nullable
    public TapAction getHeadToHeadAction() {
        return mHeadToHeadAction;
    }


    public static final class Builder extends BaseTemplateData.Builder {

        private Text mHeadToHeadTitle;
        private Icon mHeadToHeadFirstCompetitorIcon;
        private Icon mHeadToHeadSecondCompetitorIcon;
        private Text mHeadToHeadFirstCompetitorText;
        private Text mHeadToHeadSecondCompetitorText;
        private TapAction mHeadToHeadAction;

        /**
         * A builder for {@link HeadToHeadTemplateData}.
         */
        public Builder() {
            super(SmartspaceTarget.UI_TEMPLATE_HEAD_TO_HEAD);
        }

        /**
         * Sets the head-to-head card's title
         */
        @NonNull
        public Builder setHeadToHeadTitle(@Nullable Text headToHeadTitle) {
            mHeadToHeadTitle = headToHeadTitle;
            return this;
        }

        /**
         * Sets the head-to-head card's first competitor icon
         */
        @NonNull
        public Builder setHeadToHeadFirstCompetitorIcon(
                @Nullable Icon headToHeadFirstCompetitorIcon) {
            mHeadToHeadFirstCompetitorIcon = headToHeadFirstCompetitorIcon;
            return this;
        }

        /**
         * Sets the head-to-head card's second competitor icon
         */
        @NonNull
        public Builder setHeadToHeadSecondCompetitorIcon(
                @Nullable Icon headToHeadSecondCompetitorIcon) {
            mHeadToHeadSecondCompetitorIcon = headToHeadSecondCompetitorIcon;
            return this;
        }

        /**
         * Sets the head-to-head card's first competitor text
         */
        @NonNull
        public Builder setHeadToHeadFirstCompetitorText(
                @Nullable Text headToHeadFirstCompetitorText) {
            mHeadToHeadFirstCompetitorText = headToHeadFirstCompetitorText;
            return this;
        }

        /**
         * Sets the head-to-head card's second competitor text
         */
        @NonNull
        public Builder setHeadToHeadSecondCompetitorText(
                @Nullable Text headToHeadSecondCompetitorText) {
            mHeadToHeadSecondCompetitorText = headToHeadSecondCompetitorText;
            return this;
        }

        /**
         * Sets the head-to-head card's tap action
         */
        @NonNull
        public Builder setHeadToHeadAction(@Nullable TapAction headToHeadAction) {
            mHeadToHeadAction = headToHeadAction;
            return this;
        }

        /**
         * Builds a new SmartspaceHeadToHeadUiTemplateData instance.
         */
        @NonNull
        public HeadToHeadTemplateData build() {
            return new HeadToHeadTemplateData(getTemplateType(), getPrimaryItem(),
                    getSubtitleItem(), getSubtitleSupplemtnalItem(),
                    getSupplementalLineItem(), getSupplementalAlarmItem(), getLayoutWeight(),
                    mHeadToHeadTitle,
                    mHeadToHeadFirstCompetitorIcon,
                    mHeadToHeadSecondCompetitorIcon, mHeadToHeadFirstCompetitorText,
                    mHeadToHeadSecondCompetitorText,
                    mHeadToHeadAction);
        }
    }
}