package com.john.freezeapp.client;

import com.john.freezeapp.IDaemonBinderContainer;
import com.john.freezeapp.Singleton;

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
        ClientBinderManager.registerDaemonBinderContainerListener(new ClientBinderManager.IDaemonBinderContainerListener() {
            @Override
            public void bind(IDaemonBinderContainer daemonBinderContainer) {

            }

            @Override
            public void unbind() {
                destroy();
            }
        });
    }
}
