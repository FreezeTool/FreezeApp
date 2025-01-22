package android.app.smartspace.uitemplatedata;

import android.app.smartspace.SmartspaceTarget;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public final class CarouselTemplateData extends BaseTemplateData {

    /**
     * Lists of {@link CarouselItem}.
     */
    @NonNull
    private final List<CarouselItem> mCarouselItems;

    /**
     * Tap action for the entire carousel secondary card, including the blank space
     */
    @Nullable
    private final TapAction mCarouselAction;


    private CarouselTemplateData(@SmartspaceTarget.UiTemplateType int templateType,
                                 @Nullable SubItemInfo primaryItem,
                                 @Nullable SubItemInfo subtitleItem,
                                 @Nullable SubItemInfo subtitleSupplementalItem,
                                 @Nullable SubItemInfo supplementalLineItem,
                                 @Nullable SubItemInfo supplementalAlarmItem,
                                 int layoutWeight,
                                 @NonNull List<CarouselItem> carouselItems,
                                 @Nullable TapAction carouselAction) {
        super(templateType, primaryItem, subtitleItem, subtitleSupplementalItem,
                supplementalLineItem, supplementalAlarmItem, layoutWeight);

        mCarouselItems = carouselItems;
        mCarouselAction = carouselAction;
    }

    /**
     * Returns the list of {@link CarouselItem}. Can be empty if not being set.
     */
    @NonNull
    public List<CarouselItem> getCarouselItems() {
        return mCarouselItems;
    }

    /**
     * Returns the card's tap action.
     */
    @Nullable
    public TapAction getCarouselAction() {
        return mCarouselAction;
    }


    public static final class Builder extends BaseTemplateData.Builder {

        private final List<CarouselItem> mCarouselItems;
        private TapAction mCarouselAction;

        /**
         * A builder for {@link CarouselTemplateData}.
         */
        public Builder(@NonNull List<CarouselItem> carouselItems) {
            super(SmartspaceTarget.UI_TEMPLATE_CAROUSEL);
            throw new RuntimeException("STUB");
        }

        /**
         * Sets the card tap action.
         */
        @NonNull
        public Builder setCarouselAction(@NonNull TapAction carouselAction) {
            mCarouselAction = carouselAction;
            return this;
        }

        /**
         * Builds a new {@link CarouselTemplateData} instance.
         *
         * @throws IllegalStateException if the carousel data is invalid.
         */
        @NonNull
        public CarouselTemplateData build() {
            if (mCarouselItems.isEmpty()) {
                throw new IllegalStateException("Carousel data is empty");
            }

            return new CarouselTemplateData(getTemplateType(), getPrimaryItem(),
                    getSubtitleItem(), getSubtitleSupplemtnalItem(),
                    getSupplementalLineItem(), getSupplementalAlarmItem(), getLayoutWeight(),
                    mCarouselItems, mCarouselAction);
        }
    }

    /**
     * Holds all the relevant data needed to render a carousel item.
     *
     * <ul>
     *     <li> upper text </li>
     *     <li> image </li>
     *     <li> lower text </li>
     * </ul>
     */
    public static final class CarouselItem {

        /**
         * Text which is above the image item.
         */
        @Nullable
        private final Text mUpperText;

        /**
         * Image item. Can be empty.
         */
        @Nullable
        private final Icon mImage;

        /**
         * Text which is under the image item.
         */
        @Nullable
        private final Text mLowerText;

        /**
         * Tap action for this {@link CarouselItem} instance. {@code mCarouselAction} is used if not
         * being set.
         */
        @Nullable
        private final TapAction mTapAction;


        private CarouselItem(@Nullable Text upperText, @Nullable Icon image,
                             @Nullable Text lowerText, @Nullable TapAction tapAction) {
            mUpperText = upperText;
            mImage = image;
            mLowerText = lowerText;
            mTapAction = tapAction;
        }

        @Nullable
        public Text getUpperText() {
            return mUpperText;
        }

        @Nullable
        public Icon getImage() {
            return mImage;
        }

        @Nullable
        public Text getLowerText() {
            return mLowerText;
        }

        @Nullable
        public TapAction getTapAction() {
            return mTapAction;
        }

        public static final class Builder {

            private Text mUpperText;
            private Icon mImage;
            private Text mLowerText;
            private TapAction mTapAction;

            /**
             * Sets the upper text.
             */
            @NonNull
            public Builder setUpperText(@Nullable Text upperText) {
                mUpperText = upperText;
                return this;
            }

            /**
             * Sets the image.
             */
            @NonNull
            public Builder setImage(@Nullable Icon image) {
                mImage = image;
                return this;
            }


            /**
             * Sets the lower text.
             */
            @NonNull
            public Builder setLowerText(@Nullable Text lowerText) {
                mLowerText = lowerText;
                return this;
            }

            /**
             * Sets the tap action.
             */
            @NonNull
            public Builder setTapAction(@Nullable TapAction tapAction) {
                mTapAction = tapAction;
                return this;
            }

            /**
             * Builds a new CarouselItem instance.
             *
             * @throws IllegalStateException if all the rendering data is empty.
             */
            @NonNull
            public CarouselItem build() {
                throw new RuntimeException("STUB");
            }
        }
    }
}