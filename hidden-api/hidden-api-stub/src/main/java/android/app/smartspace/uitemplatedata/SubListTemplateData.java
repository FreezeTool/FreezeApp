package android.app.smartspace.uitemplatedata;

import android.app.smartspace.SmartspaceTarget;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public final class SubListTemplateData extends BaseTemplateData {

    @Nullable
    private final Icon mSubListIcon;
    @NonNull
    private final List<Text> mSubListTexts;

    /**
     * Tap action for the sub-list secondary card.
     */
    @Nullable
    private final TapAction mSubListAction;


    private SubListTemplateData(@SmartspaceTarget.UiTemplateType int templateType, @Nullable SubItemInfo primaryItem, @Nullable SubItemInfo subtitleItem, @Nullable SubItemInfo subtitleSupplementalItem, @Nullable SubItemInfo supplementalLineItem, @Nullable SubItemInfo supplementalAlarmItem, int layoutWeight, @Nullable Icon subListIcon, @NonNull List<Text> subListTexts, @Nullable TapAction subListAction) {
        super(templateType, primaryItem, subtitleItem, subtitleSupplementalItem, supplementalLineItem, supplementalAlarmItem, layoutWeight);

        mSubListIcon = subListIcon;
        mSubListTexts = subListTexts;
        mSubListAction = subListAction;
    }

    /**
     * Returns the sub-list card's icon.
     */
    @Nullable
    public Icon getSubListIcon() {
        return mSubListIcon;
    }

    /**
     * Returns the sub-list card's texts list.
     */
    @NonNull
    public List<Text> getSubListTexts() {
        return mSubListTexts;
    }

    /**
     * Returns the sub-list card's tap action.
     */
    @Nullable
    public TapAction getSubListAction() {
        return mSubListAction;
    }

    public static final class Builder extends BaseTemplateData.Builder {

        private Icon mSubListIcon;
        private final List<Text> mSubListTexts;
        private TapAction mSubListAction;

        /**
         * A builder for {@link SubListTemplateData}.
         */
        public Builder(@NonNull List<Text> subListTexts) {
            super(SmartspaceTarget.UI_TEMPLATE_SUB_LIST);
            throw new RuntimeException("STUB");
        }

        /**
         * Sets the sub-list card icon.
         */
        @NonNull
        public Builder setSubListIcon(@NonNull Icon subListIcon) {
            mSubListIcon = subListIcon;
            return this;
        }

        /**
         * Sets the card tap action.
         */
        @NonNull
        public Builder setSubListAction(@NonNull TapAction subListAction) {
            mSubListAction = subListAction;
            return this;
        }

        /**
         * Builds a new SmartspaceSubListUiTemplateData instance.
         */
        @NonNull
        public SubListTemplateData build() {
            return new SubListTemplateData(getTemplateType(), getPrimaryItem(), getSubtitleItem(), getSubtitleSupplemtnalItem(), getSupplementalLineItem(), getSupplementalAlarmItem(), getLayoutWeight(), mSubListIcon, mSubListTexts, mSubListAction);
        }
    }
}