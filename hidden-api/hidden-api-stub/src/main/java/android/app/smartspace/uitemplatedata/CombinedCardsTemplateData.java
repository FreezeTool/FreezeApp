package android.app.smartspace.uitemplatedata;

import android.app.smartspace.SmartspaceTarget;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public final class CombinedCardsTemplateData extends BaseTemplateData {

    /** A list of secondary cards. */
    @NonNull
    private final List<BaseTemplateData> mCombinedCardDataList;



    private CombinedCardsTemplateData(@SmartspaceTarget.UiTemplateType int templateType,
            @Nullable SubItemInfo primaryItem,
            @Nullable SubItemInfo subtitleItem,
            @Nullable SubItemInfo subtitleSupplementalItem,
            @Nullable SubItemInfo supplementalLineItem,
            @Nullable SubItemInfo supplementalAlarmItem,
            int layoutWeight,
            @NonNull List<BaseTemplateData> combinedCardDataList) {
        super(templateType, primaryItem, subtitleItem, subtitleSupplementalItem,
                supplementalLineItem, supplementalAlarmItem, layoutWeight);

        mCombinedCardDataList = combinedCardDataList;
    }

    /** Returns the list of secondary cards. Can be null if not being set. */
    @NonNull
    public List<BaseTemplateData> getCombinedCardDataList() {
        return mCombinedCardDataList;
    }


    public static final class Builder extends BaseTemplateData.Builder {

        private final List<BaseTemplateData> mCombinedCardDataList;

        /**
         * A builder for {@link CombinedCardsTemplateData}.
         */
        public Builder(@NonNull List<BaseTemplateData> combinedCardDataList) {
            super(SmartspaceTarget.UI_TEMPLATE_COMBINED_CARDS);
            throw new RuntimeException("STUB");
        }

        /**
         * Builds a new SmartspaceCombinedCardsUiTemplateData instance.
         *
         * @throws IllegalStateException if any required non-null field is null
         */
        @NonNull
        public CombinedCardsTemplateData build() {
            if (mCombinedCardDataList == null) {
                throw new IllegalStateException("Please assign a value to all @NonNull args.");
            }
            return new CombinedCardsTemplateData(getTemplateType(), getPrimaryItem(),
                    getSubtitleItem(), getSubtitleSupplemtnalItem(),
                    getSupplementalLineItem(), getSupplementalAlarmItem(), getLayoutWeight(),
                    mCombinedCardDataList);
        }
    }
}