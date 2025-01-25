// IClientBinder.aidl
package com.john.freezeapp;
import android.os.IBinder;

interface IClientBinder {
    IBinder getClient(String name);
}