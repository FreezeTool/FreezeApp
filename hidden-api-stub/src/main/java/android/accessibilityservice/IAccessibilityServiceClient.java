package android.accessibilityservice;

import android.graphics.Region;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityEvent;

public interface IAccessibilityServiceClient {

//    void init(IAccessibilityServiceConnection connection, int connectionId, IBinder windowToken);

    void onAccessibilityEvent(AccessibilityEvent event, boolean serviceWantsEvent);

    void onInterrupt();

    void onGesture(AccessibilityGestureEvent gestureEvent);

    void clearAccessibilityCache();

    void onKeyEvent(KeyEvent event, int sequence);

    void onMagnificationChanged(int displayId, Region region, MagnificationConfig config);

    void onMotionEvent(MotionEvent event);

    void onTouchStateChanged(int displayId, int state);

    void onSoftKeyboardShowModeChanged(int showMode);

    void onPerformGestureResult(int sequence, boolean completedSuccessfully);

    void onFingerprintCapturingGesturesChanged(boolean capturing);

    void onFingerprintGesture(int gesture);

    void onAccessibilityButtonClicked(int displayId);

    void onAccessibilityButtonAvailabilityChanged(boolean available);

    void onSystemActionsChanged();

//    void createImeSession(IAccessibilityInputMethodSessionCallback callback);
//
//    void setImeSessionEnabled(IAccessibilityInputMethodSession session, boolean enabled);

    void bindInput();

    void unbindInput();

//    void startInput(IRemoteAccessibilityInputConnection connection, EditorInfo editorInfo,
//            boolean restarting);
}