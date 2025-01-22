package android.app.smartspace.uitemplatedata;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class TapAction {

    /**
     * A unique Id of this {@link TapAction}.
     */
    @Nullable
    private final CharSequence mId;

    @Nullable
    private final Intent mIntent;

    @Nullable
    private final PendingIntent mPendingIntent;

    @Nullable
    private final UserHandle mUserHandle;

    @Nullable
    private final Bundle mExtras;

    /**
     * Whether the tap action's result should be shown on the lockscreen (e.g. turn off the
     * flashlight can be done on LS bypassing the keyguard). Default value is false.
     */
    private final boolean mShouldShowOnLockscreen;

    private TapAction(@Nullable CharSequence id, @Nullable Intent intent,
                      @Nullable PendingIntent pendingIntent, @Nullable UserHandle userHandle,
                      @Nullable Bundle extras, boolean shouldShowOnLockscreen) {
        mId = id;
        mIntent = intent;
        mPendingIntent = pendingIntent;
        mUserHandle = userHandle;
        mExtras = extras;
        mShouldShowOnLockscreen = shouldShowOnLockscreen;
    }

    /**
     * Returns the unique id of the tap action.
     */
    @Nullable
    public CharSequence getId() {
        return mId;
    }

    /**
     * Returns the intent of the tap action.
     */
    @Nullable
    public Intent getIntent() {
        return mIntent;
    }

    /**
     * Returns the pending intent of the tap action.
     */
    @Nullable
    public PendingIntent getPendingIntent() {
        return mPendingIntent;
    }

    /**
     * Returns the user handle of the tap action.
     */
    @Nullable
    public UserHandle getUserHandle() {
        return mUserHandle;
    }

    /**
     * Returns the extras bundle of the tap action.
     */
    @Nullable
    public Bundle getExtras() {
        return mExtras;
    }

    /**
     * Whether the tap action's result should be shown on the lockscreen. If true, the tap action's
     * handling should bypass the keyguard. Default value is false.
     */
    public boolean shouldShowOnLockscreen() {
        return mShouldShowOnLockscreen;
    }

    public static final class Builder {

        private CharSequence mId;
        private Intent mIntent;
        private PendingIntent mPendingIntent;
        private UserHandle mUserHandle;
        private Bundle mExtras;
        private boolean mShouldShowOnLockScreen;

        /**
         * A builder for {@link TapAction}. By default sets should_show_on_lockscreen to false.
         *
         * @param id A unique Id of this {@link TapAction}.
         */
        public Builder(@NonNull CharSequence id) {
            throw new RuntimeException("STUB");
        }

        /**
         * Sets the action intent.
         */
        @NonNull
        public Builder setIntent(@NonNull Intent intent) {
            throw new RuntimeException("STUB");
        }

        /**
         * Sets the pending intent.
         */
        @NonNull
        public Builder setPendingIntent(@NonNull PendingIntent pendingIntent) {
            mPendingIntent = pendingIntent;
            return this;
        }

        /**
         * Sets the user handle.
         */
        @NonNull
        public Builder setUserHandle(@Nullable UserHandle userHandle) {
            mUserHandle = userHandle;
            return this;
        }

        /**
         * Sets the extras.
         */
        @NonNull
        public Builder setExtras(@NonNull Bundle extras) {
            mExtras = extras;
            return this;
        }

        /**
         * Sets whether the tap action's result should be shown on the lockscreen, to bypass the
         * keyguard when the tap action is triggered.
         */
        @NonNull
        public Builder setShouldShowOnLockscreen(@NonNull boolean shouldShowOnLockScreen) {
            mShouldShowOnLockScreen = shouldShowOnLockScreen;
            return this;
        }

        /**
         * Builds a new SmartspaceTapAction instance.
         *
         * @throws IllegalStateException if the tap action is empty.
         */
        @NonNull
        public TapAction build() {
            if (mIntent == null && mPendingIntent == null && mExtras == null) {
                throw new IllegalStateException("Please assign at least 1 valid tap field");
            }
            return new TapAction(mId, mIntent, mPendingIntent, mUserHandle, mExtras,
                    mShouldShowOnLockScreen);
        }
    }
}