package android.app.smartspace.uitemplatedata;

import android.app.smartspace.SmartspaceTarget;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public final class SubImageTemplateData extends BaseTemplateData {

    /**
     * Texts are shown next to the image as a vertical list
     */
    @NonNull
    private final List<Text> mSubImageTexts;

    /**
     * If multiple images are passed in, they will be rendered as GIF.
     */
    @NonNull
    private final List<Icon> mSubImages;

    /**
     * Tap action for the sub-image secondary card.
     */
    @Nullable
    private final TapAction mSubImageAction;


    private SubImageTemplateData(@SmartspaceTarget.UiTemplateType int templateType,
                                 @Nullable SubItemInfo primaryItem,
                                 @Nullable SubItemInfo subtitleItem,
                                 @Nullable SubItemInfo subtitleSupplementalItem,
                                 @Nullable SubItemInfo supplementalLineItem,
                                 @Nullable SubItemInfo supplementalAlarmItem,
                                 int layoutWeight,
                                 @NonNull List<Text> subImageTexts,
                                 @NonNull List<Icon> subImages,
                                 @Nullable TapAction subImageAction) {
        super(templateType, primaryItem, subtitleItem, subtitleSupplementalItem,
                supplementalLineItem, supplementalAlarmItem, layoutWeight);

        mSubImageTexts = subImageTexts;
        mSubImages = subImages;
        mSubImageAction = subImageAction;
    }

    /**
     * Returns the list of sub-image card's texts. Can be empty if not being set.
     */
    @NonNull
    public List<Text> getSubImageTexts() {
        return mSubImageTexts;
    }

    /**
     * Returns the list of sub-image card's image. It's a single-element list if it's a static
     * image, or a multi-elements list if it's a GIF.
     */
    @NonNull
    public List<Icon> getSubImages() {
        return mSubImages;
    }

    /**
     * Returns the sub-image card's tap action.
     */
    @Nullable
    public TapAction getSubImageAction() {
        return mSubImageAction;
    }


    public static final class Builder extends BaseTemplateData.Builder {

        private final List<Text> mSubImageTexts;
        private final List<Icon> mSubImages;
        private TapAction mSubImageAction;

        /**
         * A builder for {@link SubImageTemplateData}.
         */
        public Builder(@NonNull List<Text> subImageTexts,
                       @NonNull List<Icon> subImages) {
            super(SmartspaceTarget.UI_TEMPLATE_SUB_IMAGE);
            throw new RuntimeException("STUB");
        }

        /**
         * Sets the card tap action.
         */
        @NonNull
        public Builder setSubImageAction(@NonNull TapAction subImageAction) {
            mSubImageAction = subImageAction;
            return this;
        }

        /**
         * Builds a new SmartspaceSubImageUiTemplateData instance.
         */
        @NonNull
        public SubImageTemplateData build() {
            return new SubImageTemplateData(getTemplateType(), getPrimaryItem(),
                    getSubtitleItem(), getSubtitleSupplemtnalItem(),
                    getSupplementalLineItem(), getSupplementalAlarmItem(), getLayoutWeight(),
                    mSubImageTexts,
                    mSubImages,
                    mSubImageAction);
        }
    }
}