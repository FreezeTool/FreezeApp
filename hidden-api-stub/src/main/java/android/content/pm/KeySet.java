package android.content.pm;

import android.os.IBinder;

public class KeySet  {

    private IBinder token;

    public KeySet(IBinder token) {
        if (token == null) {
            throw new NullPointerException("null value for KeySet IBinder token");
        }
        this.token = token;
    }

    public IBinder getToken() {
        return token;
    }
}