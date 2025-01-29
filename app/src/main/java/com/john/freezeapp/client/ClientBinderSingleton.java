package com.john.freezeapp.client;

import com.john.freezeapp.IDaemonBinder;
import com.john.freezeapp.util.Singleton;

public abstract class ClientBinderSingleton<T> extends Singleton<T> {

    @Override
    protected T create() {
        if (!ClientBinderManager.isActive()) {
            return null;
        }

        return createBinder();
    }

    protected abstract T createBinder();

    public ClientBinderSingleton() {
        ClientBinderManager.registerDaemonBinderListener(new ClientBinderManager.IDaemonBinderListener() {
            @Override
            public void bind(IDaemonBinder daemonBinder) {

            }

            @Override
            public void unbind() {
                destroy();
            }
        });
    }
}
