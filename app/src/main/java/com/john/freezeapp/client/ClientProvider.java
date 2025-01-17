package com.john.freezeapp.client;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.john.freezeapp.IDaemonBinderContainer;

public class ClientProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        Log.d("freeze-server", "ClientProvider onCreate");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "";
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        if (TextUtils.equals("sendBinder", method)) {
            IBinder binder = extras.getBinder("binder");
            IDaemonBinderContainer binderContainer = IDaemonBinderContainer.Stub.asInterface(binder); //BinderProxy
            ClientLog.log("sendBinder");
            ClientBinderManager.setDaemonBinderContainer(binderContainer);
        }
        return super.call(method, arg, extras);
    }
}
