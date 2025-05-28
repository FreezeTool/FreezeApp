package android.content.pm.dex;

import android.os.ParcelFileDescriptor;

public interface ISnapshotRuntimeProfileCallback {
    void onSuccess(ParcelFileDescriptor profileReadFd);

    void onError(int errCode);
}