package android.app.smartspace;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class SmartspaceConfig {

    /**
     * The least number of smartspace targets expected to be predicted by the backend. The backend
     * will always try to satisfy this threshold but it is not guaranteed to always meet it.
     */
    @IntRange(from = 0, to = 50)
    private final int mSmartspaceTargetCount;

    /**
     * A {@link mUiSurface} is the name of the surface which will be used to display the cards. A
     * few examples are homescreen, lockscreen, aod etc.
     */
    @NonNull
    private final String mUiSurface;

    /**
     * Package name of the client.
     */
    @NonNull
    private String mPackageName;

    /**
     * Send other client UI configurations in extras.
     * <p>
     * This can include:
     * <p>
     * - Desired maximum update frequency (For example 1 minute update frequency for AoD, 1 second
     * update frequency for home screen etc).
     * - Request to get periodic updates
     * - Request to support multiple clients for the same UISurface.
     */
    @Nullable
    private final Bundle mExtras;

    private SmartspaceConfig(@NonNull String uiSurface, int numPredictedTargets,
                             @NonNull String packageName, @Nullable Bundle extras) {
        mUiSurface = uiSurface;
        mSmartspaceTargetCount = numPredictedTargets;
        mPackageName = packageName;
        mExtras = extras;
    }

    private SmartspaceConfig(Parcel parcel) {
        mUiSurface = parcel.readString();
        mSmartspaceTargetCount = parcel.readInt();
        mPackageName = parcel.readString();
        mExtras = parcel.readBundle();
    }

    /**
     * Returns the package name of the prediction context.
     */
    @NonNull
    public String getPackageName() {
        return mPackageName;
    }

    /**
     * Returns the number of smartspace targets requested by the user.
     */
    @NonNull
    public int getSmartspaceTargetCount() {
        return mSmartspaceTargetCount;
    }

    /**
     * Returns the UISurface requested by the client.
     */
    @NonNull
    public String getUiSurface() {
        return mUiSurface;
    }

    @Nullable
    public Bundle getExtras() {
        return mExtras;
    }

    public static final class Builder {
        @NonNull
        private int mSmartspaceTargetCount = 5; // Default count is 5
        @NonNull
        private final String mUiSurface;
        @NonNull
        private final String mPackageName;
        @NonNull
        private Bundle mExtras = Bundle.EMPTY;

        public Builder(@NonNull Context context, @NonNull String uiSurface) {
            mPackageName = context.getPackageName();
            this.mUiSurface = uiSurface;
        }

        /**
         * Used to set the expected number of cards for this context.
         */
        @NonNull
        public Builder setSmartspaceTargetCount(
                @IntRange(from = 0, to = 50) int smartspaceTargetCount) {
            this.mSmartspaceTargetCount = smartspaceTargetCount;
            return this;
        }

        /**
         * Used to send a bundle containing extras for the {@link SmartspaceConfig}.
         */
        @NonNull
        public Builder setExtras(@NonNull Bundle extras) {
            this.mExtras = extras;
            return this;
        }

        /**
         * Returns an instance of {@link SmartspaceConfig}.
         */
        @NonNull
        public SmartspaceConfig build() {
            return new SmartspaceConfig(mUiSurface, mSmartspaceTargetCount, mPackageName, mExtras);
        }
    }
}