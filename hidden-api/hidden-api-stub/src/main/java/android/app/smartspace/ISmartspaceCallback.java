package android.app.smartspace;

import android.content.pm.ParceledListSlice;

public interface ISmartspaceCallback {

    void onResult(ParceledListSlice result);
}