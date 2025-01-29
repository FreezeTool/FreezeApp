package com.john.freezeapp.util;

public abstract class Singleton<T> {

    protected T mInstance;

    protected abstract T create();

    public final T get() {
        synchronized (this) {
            if (mInstance == null) {
                mInstance = create();
            }
            return mInstance;
        }
    }

    public final void destroy() {
        mInstance = null;
    }
}
