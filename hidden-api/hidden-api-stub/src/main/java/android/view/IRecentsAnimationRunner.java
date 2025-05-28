package android.view;

import android.graphics.Rect;
import android.window.TaskSnapshot;

public interface IRecentsAnimationRunner {


    void onAnimationCanceled(int[] taskIds,
                             TaskSnapshot[] taskSnapshots);

    /**
     * Called when the system is ready for the handler to start animating all the visible tasks.
     *
     * @param homeContentInsets   The current home app content insets
     * @param minimizedHomeBounds Specifies the bounds of the minimized home app, will be
     *                            {@code null} if the device is not currently split screen
     */
    void onAnimationStart(IRecentsAnimationController controller,
                          RemoteAnimationTarget[] apps, RemoteAnimationTarget[] wallpapers,
                          Rect homeContentInsets, Rect minimizedHomeBounds);

    /**
     * Called when the task of an activity that has been started while the recents animation
     * was running becomes ready for control.
     */
    void onTasksAppeared(RemoteAnimationTarget[] app);
}