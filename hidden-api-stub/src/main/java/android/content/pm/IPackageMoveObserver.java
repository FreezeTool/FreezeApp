package android.content.pm;

import android.os.Bundle;

public interface IPackageMoveObserver {
    void onCreated(int moveId, Bundle extras);

    void onStatusChanged(int moveId, int status, long estMillis);
}