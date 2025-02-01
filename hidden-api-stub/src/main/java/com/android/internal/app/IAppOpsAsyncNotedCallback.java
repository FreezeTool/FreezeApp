package com.android.internal.app;

import android.app.AsyncNotedAppOp;

public interface IAppOpsAsyncNotedCallback {
    void opNoted(AsyncNotedAppOp op);
}