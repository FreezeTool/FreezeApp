package android.app;

import android.graphics.Bitmap;
import android.os.Bundle;

public interface IAssistDataReceiver {
    void onHandleAssistData(Bundle resultData);

    void onHandleAssistScreenshot(Bitmap screenshot);
}