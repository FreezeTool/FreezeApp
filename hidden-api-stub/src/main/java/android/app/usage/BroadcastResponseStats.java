package android.app.usage;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

public final class BroadcastResponseStats {

    private BroadcastResponseStats(@NonNull Parcel in) {
        throw new RuntimeException("STUB");
    }

    public String getPackageName() {
        throw new RuntimeException("STUB");
    }

    public long getId() {
        throw new RuntimeException("STUB");
    }

    public int getBroadcastsDispatchedCount() {
        throw new RuntimeException("STUB");
    }


    public int getNotificationsPostedCount() {
        throw new RuntimeException("STUB");
    }

    public int getNotificationsUpdatedCount() {
        throw new RuntimeException("STUB");
    }

    public int getNotificationsCancelledCount() {
        throw new RuntimeException("STUB");
    }

    public void incrementBroadcastsDispatchedCount(@IntRange(from = 0) int count) {
        throw new RuntimeException("STUB");
    }

    public void incrementNotificationsPostedCount(@IntRange(from = 0) int count) {
        throw new RuntimeException("STUB");
    }


    public void incrementNotificationsUpdatedCount(@IntRange(from = 0) int count) {
        throw new RuntimeException("STUB");
    }


    public void incrementNotificationsCancelledCount(@IntRange(from = 0) int count) {
        throw new RuntimeException("STUB");
    }


    public void addCounts(@NonNull BroadcastResponseStats stats) {
        throw new RuntimeException("STUB");
    }
}
