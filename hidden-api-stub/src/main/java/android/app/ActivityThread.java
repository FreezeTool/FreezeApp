package android.app;

import android.content.pm.IPackageManager;

public class ActivityThread {

    public static ActivityThread systemMain() {
        throw new RuntimeException();
    }

    public static ActivityThread currentActivityThread() {
        throw new RuntimeException();
    }

    public static IPackageManager getPackageManager() {
        throw new RuntimeException();
    }

    public Application getApplication() {
        throw new RuntimeException();
    }

    public ContextImpl getSystemContext() {
        throw new RuntimeException();
    }
}
