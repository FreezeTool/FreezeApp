package android.view;


import android.os.Build;

import androidx.annotation.RequiresApi;

public interface WindowManagerHidden {

    public static class LayoutParams {

        @RequiresApi(Build.VERSION_CODES.O)
        public static int PRIVATE_FLAG_IS_ROUNDED_CORNERS_OVERLAY;

        public int privateFlags;
    }
}
