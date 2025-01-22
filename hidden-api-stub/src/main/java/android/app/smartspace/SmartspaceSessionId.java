package android.app.smartspace;

import android.os.UserHandle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class SmartspaceSessionId {

    @NonNull
    private final String mId;

    @NonNull
    private final UserHandle mUserHandle;

    /**
     * Creates a new id for a Smartspace session.
     *
     * @hide
     */
    public SmartspaceSessionId(@NonNull final String id, @NonNull final UserHandle userHandle) {
        mId = id;
        mUserHandle = userHandle;
    }


    /**
     * Returns a {@link String} Id of this sessionId.
     */
    @Nullable
    public String getId() {
        return mId;
    }

    /**
     * Returns the userId associated with this sessionId.
     */
    @NonNull
    public UserHandle getUserHandle() {
        return mUserHandle;
    }
}