package android.app.smartspace.uitemplatedata;

import android.text.TextUtils;

import androidx.annotation.NonNull;

public final class Text {

    @NonNull
    private final CharSequence mText;

    private final TextUtils.TruncateAt mTruncateAtType;

    private final int mMaxLines;

    private Text(@NonNull CharSequence text, TextUtils.TruncateAt truncateAtType, int maxLines) {
        mText = text;
        mTruncateAtType = truncateAtType;
        mMaxLines = maxLines;
    }

    /**
     * Returns the text content.
     */
    @NonNull
    public CharSequence getText() {
        return mText;
    }

    /**
     * Returns the {@link TextUtils.TruncateAt} type of the text content.
     */
    @NonNull
    public TextUtils.TruncateAt getTruncateAtType() {
        return mTruncateAtType;
    }

    /**
     * Returns the allowed max lines for presenting the text content.
     */
    public int getMaxLines() {
        return mMaxLines;
    }


    public static final class Builder {
        /**
         * A builder for {@link Text}, which by default sets TruncateAtType to AT_END, and the max
         * lines to 1.
         */
        public Builder(@NonNull CharSequence text) {

        }

        /**
         * Sets truncateAtType, where the text content should be truncated if not all the content
         * can be presented.
         */
        @NonNull
        public Builder setTruncateAtType(@NonNull TextUtils.TruncateAt truncateAtType) {
            throw new RuntimeException("STUB");
        }

        /**
         * Sets the allowed max lines for the text content.
         */
        @NonNull
        public Builder setMaxLines(int maxLines) {
            throw new RuntimeException("STUB");
        }

        /**
         * Builds a new SmartspaceText instance.
         */
        @NonNull
        public Text build() {
            throw new RuntimeException("STUB");
        }
    }
}