package android.window;

import android.content.ComponentName;
import android.graphics.ColorSpace;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.HardwareBuffer;

import androidx.annotation.NonNull;

public class TaskSnapshot  {

    public TaskSnapshot(long id,
                        @NonNull ComponentName topActivityComponent, HardwareBuffer snapshot,
                        @NonNull ColorSpace colorSpace, int orientation, int rotation, Point taskSize,
                        Rect contentInsets, Rect letterboxInsets, boolean isLowResolution,
                        boolean isRealSnapshot, int windowingMode,
                         int appearance, boolean isTranslucent,
                        boolean hasImeSurface) {
        throw new UnsupportedOperationException();
    }



    /**
     * @return Identifier of this snapshot.
     */
    public long getId() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return The top activity component for the task at the point this snapshot was taken.
     */
    public ComponentName getTopActivityComponent() {
        throw new UnsupportedOperationException();
    }


//    public GraphicBuffer getSnapshot() {
//        return GraphicBuffer.createFromHardwareBuffer(mSnapshot);
//    }

    /**
     * @return The hardware buffer representing the screenshot.
     */
    public HardwareBuffer getHardwareBuffer() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return The color space of hardware buffer representing the screenshot.
     */
    public ColorSpace getColorSpace() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return The screen orientation the screenshot was taken in.
     */

    public int getOrientation() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return The screen rotation the screenshot was taken in.
     */
    public int getRotation() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return The size of the task at the point this snapshot was taken.
     */
    public Point getTaskSize() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return The system/content insets on the snapshot. These can be clipped off in order to
     *         remove any areas behind system bars in the snapshot.
     */

    public Rect getContentInsets() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return The letterbox insets on the snapshot. These can be clipped off in order to
     *         remove any letterbox areas in the snapshot.
     */
    public Rect getLetterboxInsets() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return Whether this snapshot is a down-sampled version of the full resolution.
     */
    public boolean isLowResolution() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return Whether or not the snapshot is a real snapshot or an app-theme generated snapshot
     * due to the task having a secure window or having previews disabled.
     */
    public boolean isRealSnapshot() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return Whether or not the snapshot is of a translucent app window (non-fullscreen or has
     * a non-opaque pixel format).
     */
    public boolean isTranslucent() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return Whether or not the snapshot has the IME surface.
     */
    public boolean hasImeSurface() {
        throw new UnsupportedOperationException();
    }

    /**
     * @return The windowing mode of the task when this snapshot was taken.
     */
    public int getWindowingMode() {
        throw new UnsupportedOperationException();
    }


    public int getAppearance() {
        throw new UnsupportedOperationException();
    }
}