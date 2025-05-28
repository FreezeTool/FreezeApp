package com.android.internal.os;

import android.os.Bundle;

public interface IResultReceiver {
    void send(int resultCode, Bundle resultData);
}
