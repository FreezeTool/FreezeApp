package com.android.internal.app;

public interface IAppOpsStartedCallback {
    void opStarted(int op, int uid, String packageName, String attributionTag, int flags, int mode,
    int startedType, int attributionFlags, int attributionChainId);
}