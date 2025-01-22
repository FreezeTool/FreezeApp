package android.app.smartspace;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.UserHandle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class SmartspaceAction {

    private static final String TAG = "SmartspaceAction";

    /**
     * A unique Id of this {@link SmartspaceAction}.
     */
    @NonNull
    private String mId;

    /**
     * An Icon which can be displayed in the UI.
     */
    @Nullable
    private Icon mIcon;

    /**
     * Title associated with an action.
     */
    @NonNull
    private CharSequence mTitle;

    /**
     * Subtitle associated with an action.
     */
    @Nullable
    private CharSequence mSubtitle;

    @Nullable
    private CharSequence mContentDescription;

    @Nullable
    private PendingIntent mPendingIntent;

    @Nullable
    private Intent mIntent;

    @Nullable
    private UserHandle mUserHandle;

    @Nullable
    private Bundle mExtras;


    private SmartspaceAction(
            @NonNull String id,
            @Nullable Icon icon,
            @NonNull CharSequence title,
            @Nullable CharSequence subtitle,
            @Nullable CharSequence contentDescription,
            @Nullable PendingIntent pendingIntent,
            @Nullable Intent intent,
            @Nullable UserHandle userHandle,
            @Nullable Bundle extras) {

    }

    /**
     * Returns the unique id of this object.
     */
    public @NonNull String getId() {
        return mId;
    }

    /**
     * Returns an icon representing the action.
     */
    public @Nullable Icon getIcon() {
        return mIcon;
    }

    /**
     * Returns a title representing the action.
     */
    public @NonNull CharSequence getTitle() {
        return mTitle;
    }

    /**
     * Returns a subtitle representing the action.
     */
    public @Nullable CharSequence getSubtitle() {
        return mSubtitle;
    }

    /**
     * Returns a content description representing the action.
     */
    public @Nullable CharSequence getContentDescription() {
        return mContentDescription;
    }

    /**
     * Returns the action intent.
     */
    public @Nullable PendingIntent getPendingIntent() {
        return mPendingIntent;
    }

    /**
     * Returns the intent.
     */
    public @Nullable Intent getIntent() {
        return mIntent;
    }

    /**
     * Returns the user handle.
     */
    public @Nullable UserHandle getUserHandle() {
        return mUserHandle;
    }

    /**
     * Returns the extra bundle for this object.
     */
    public @Nullable Bundle getExtras() {
        return mExtras;
    }


    public static final class Builder {
        @NonNull
        private String mId;

        @Nullable
        private Icon mIcon;

        @NonNull
        private CharSequence mTitle;

        @Nullable
        private CharSequence mSubtitle;

        @Nullable
        private CharSequence mContentDescription;

        @Nullable
        private PendingIntent mPendingIntent;

        @Nullable
        private Intent mIntent;

        @Nullable
        private UserHandle mUserHandle;

        @Nullable
        private Bundle mExtras;

        /**
         * Id and title are required.
         */
        public Builder(@NonNull String id, @NonNull String title) {

        }

        /**
         * Sets the icon.
         */
        @NonNull
        public Builder setIcon(
                @Nullable Icon icon) {
            mIcon = icon;
            return this;
        }

        /**
         * Sets the subtitle.
         */
        @NonNull
        public Builder setSubtitle(
                @Nullable CharSequence subtitle) {
            mSubtitle = subtitle;
            return this;
        }

        /**
         * Sets the content description.
         */
        @NonNull
        public Builder setContentDescription(
                @Nullable CharSequence contentDescription) {
            mContentDescription = contentDescription;
            return this;
        }

        /**
         * Sets the pending intent.
         */
        @NonNull
        public Builder setPendingIntent(@Nullable PendingIntent pendingIntent) {
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
         * Sets the intent.
         */
        @NonNull
        public Builder setIntent(@Nullable Intent intent) {
            mIntent = intent;
            return this;
        }

        /**
         * Sets the extra.
         */
        @NonNull
        public Builder setExtras(@Nullable Bundle extras) {
            mExtras = extras;
            return this;
        }

        /**
         * Builds a new SmartspaceAction instance.
         *
         * @throws IllegalStateException if no target is set
         */
        @NonNull
        public SmartspaceAction build() {
            return new SmartspaceAction(mId, mIcon, mTitle, mSubtitle, mContentDescription,
                    mPendingIntent, mIntent, mUserHandle, mExtras);
        }
    }
}