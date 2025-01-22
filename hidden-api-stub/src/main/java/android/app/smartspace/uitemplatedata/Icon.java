package android.app.smartspace.uitemplatedata;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class Icon {

    @NonNull
    private final android.graphics.drawable.Icon mIcon;

    @Nullable
    private final CharSequence mContentDescription;

    private final boolean mShouldTint;


    private Icon(@NonNull android.graphics.drawable.Icon icon,
            @Nullable CharSequence contentDescription,
            boolean shouldTint) {
        mIcon = icon;
        mContentDescription = contentDescription;
        mShouldTint = shouldTint;
    }

    /** Returns the icon image. */
    @NonNull
    public android.graphics.drawable.Icon getIcon() {
        return mIcon;
    }

    /** Returns the content description of the icon image. */
    @Nullable
    public CharSequence getContentDescription() {
        return mContentDescription;
    }

    /**
     * Return shouldTint value, which means whether should tint the icon with the system's theme
     * color. The default value is true.
     */
    public boolean shouldTint() {
        return mShouldTint;
    }

    public static final class Builder {

        private android.graphics.drawable.Icon mIcon;
        private CharSequence mContentDescription;
        private boolean mShouldTint;

        /**
         * A builder for {@link Icon}, which sets shouldTint to true by default.
         *
         * @param icon the icon image of this {@link Icon} instance.
         */
        public Builder(@NonNull android.graphics.drawable.Icon icon) {
            throw new RuntimeException("STUB");
        }

        /**
         * Sets the icon's content description.
         */
        @NonNull
        public Builder setContentDescription(@NonNull CharSequence contentDescription) {
            mContentDescription = contentDescription;
            return this;
        }

        /**
         * Sets should tint icon with the system's theme color.
         */
        @NonNull
        public Builder setShouldTint(boolean shouldTint) {
            mShouldTint = shouldTint;
            return this;
        }

        /**
         * Builds a new SmartspaceIcon instance.
         */
        @NonNull
        public Icon build() {
            return new Icon(mIcon, mContentDescription, mShouldTint);
        }
    }
}