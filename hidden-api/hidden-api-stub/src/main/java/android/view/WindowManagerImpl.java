package android.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.IBinder;

public class WindowManagerImpl implements WindowManager {



    public WindowManagerImpl(Context context) {

    }

    /**
     * Sets the window token to assign when none is specified by the client or
     * available from the parent window.
     *
     * @param token The default token to assign.
     */
    public void setDefaultToken(IBinder token) {
        throw new RuntimeException("STUB");
    }



    @TargetApi(Build.VERSION_CODES.S)
    public static WindowManager createWindowContextWindowManager(Context context) {
        throw new RuntimeException();
    }

    @Override
    public Display getDefaultDisplay() {
        return null;
    }

    @Override
    public void removeViewImmediate(View view) {

    }

    @Override
    public void addView(View view, ViewGroup.LayoutParams params) {

    }

    @Override
    public void updateViewLayout(View view, ViewGroup.LayoutParams params) {

    }

    @Override
    public void removeView(View view) {

    }
}
