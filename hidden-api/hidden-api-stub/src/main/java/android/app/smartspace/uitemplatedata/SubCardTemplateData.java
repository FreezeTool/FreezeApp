package android.app.smartspace.uitemplatedata;

import android.app.smartspace.SmartspaceTarget;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class SubCardTemplateData extends BaseTemplateData {

    /** Icon for the sub-card. */
    @NonNull
    private final Icon mSubCardIcon;

    /** Text for the sub-card, which shows below the icon when being set. */
    @Nullable
    private final Text mSubCardText;

    /** Tap action for the sub-card secondary card. */
    @Nullable
    private final TapAction mSubCardAction;


    private SubCardTemplateData(int templateType,
            @Nullable SubItemInfo primaryItem,
            @Nullable SubItemInfo subtitleItem,
            @Nullable SubItemInfo subtitleSupplementalItem,
            @Nullable SubItemInfo supplementalLineItem,
            @Nullable SubItemInfo supplementalAlarmItem,
            int layoutWeight,
            @NonNull Icon subCardIcon,
            @Nullable Text subCardText,
            @Nullable TapAction subCardAction) {
        super(templateType, primaryItem, subtitleItem, subtitleSupplementalItem,
                supplementalLineItem, supplementalAlarmItem, layoutWeight);

        mSubCardIcon = subCardIcon;
        mSubCardText = subCardText;
        mSubCardAction = subCardAction;
    }

    /** Returns the sub-card card's icon. */
    @NonNull
    public Icon getSubCardIcon() {
        return mSubCardIcon;
    }

    /** Returns the sub-card card's text. */
    @Nullable
    public Text getSubCardText() {
        return mSubCardText;
    }

    /** Returns the sub-card card's tap action. */
    @Nullable
    public TapAction getSubCardAction() {
        return mSubCardAction;
    }

    public static final class Builder extends BaseTemplateData.Builder {

        private final Icon mSubCardIcon;
        private Text mSubCardText;
        private TapAction mSubCardAction;

        /**
         * A builder for {@link SubCardTemplateData}.
         */
        public Builder(@NonNull Icon subCardIcon) {
            super(SmartspaceTarget.UI_TEMPLATE_SUB_CARD);
            throw new RuntimeException("STUB");
        }

        /**
         * Sets the card text.
         */
        @NonNull
        public Builder setSubCardText(@NonNull Text subCardText) {
            mSubCardText = subCardText;
            return this;
        }

        /**
         * Sets the card tap action.
         */
        @NonNull
        public Builder setSubCardAction(@NonNull TapAction subCardAction) {
            mSubCardAction = subCardAction;
            return this;
        }

        /**
         * Builds a new SmartspaceSubCardUiTemplateData instance.
         */
        @NonNull
        public SubCardTemplateData build() {
            return new SubCardTemplateData(getTemplateType(), getPrimaryItem(),
                    getSubtitleItem(), getSubtitleSupplemtnalItem(),
                    getSupplementalLineItem(), getSupplementalAlarmItem(), getLayoutWeight(),
                    mSubCardIcon,
                    mSubCardText,
                    mSubCardAction);
        }
    }
}