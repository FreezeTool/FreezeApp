package com.android.internal.app;

public interface IAppOpsCallback {
    void opChanged(int op, int uid, String packageName);
}