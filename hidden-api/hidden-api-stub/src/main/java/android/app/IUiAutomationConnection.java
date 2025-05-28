package android.app;

import android.accessibilityservice.IAccessibilityServiceClient;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.ParcelFileDescriptor;
import android.view.InputEvent;
import android.view.SurfaceControl;
import android.view.WindowAnimationFrameStats;
import android.view.WindowContentFrameStats;

import java.util.List;

interface IUiAutomationConnection {
    void connect(IAccessibilityServiceClient client, int flags);

    void disconnect();

    boolean injectInputEvent(InputEvent event, boolean sync, boolean waitForAnimations);

    void syncInputTransactions(boolean waitForAnimations);

    boolean setRotation(int rotation);

    Bitmap takeScreenshot(Rect crop);

    Bitmap takeSurfaceControlScreenshot(SurfaceControl surfaceControl);

    boolean clearWindowContentFrameStats(int windowId);

    WindowContentFrameStats getWindowContentFrameStats(int windowId);

    void clearWindowAnimationFrameStats();

    WindowAnimationFrameStats getWindowAnimationFrameStats();

    void executeShellCommand(String command, ParcelFileDescriptor sink, ParcelFileDescriptor source);

    void grantRuntimePermission(String packageName, String permission, int userId);

    void revokeRuntimePermission(String packageName, String permission, int userId);

    void adoptShellPermissionIdentity(int uid, String[] permissions);

    void dropShellPermissionIdentity();

    // Called from the system process.
    void shutdown();

    void executeShellCommandWithStderr(String command, ParcelFileDescriptor sink,
                                       ParcelFileDescriptor source, ParcelFileDescriptor stderrSink);

    List<String> getAdoptedShellPermissions();
}