package android.os.storage;

import android.os.Bundle;

interface IPackageMoveObserver {
    void onCreated(int moveId, Bundle extras);

    void onStatusChanged(int moveId, int status, long estMillis);
}