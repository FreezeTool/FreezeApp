package android.service.voice;

import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;

import com.android.internal.app.IVoiceInteractionSessionShowCallback;

public interface IVoiceInteractionSession {
    void show(Bundle sessionArgs, int flags, IVoiceInteractionSessionShowCallback showCallback);
    void hide();
    void handleAssist(int taskId, IBinder activityId, Bundle assistData,
                      AssistStructure structure, AssistContent content, int index, int count);
    void handleScreenshot(Bitmap screenshot);
    void taskStarted(Intent intent, int taskId);
    void taskFinished(Intent intent, int taskId);
    void closeSystemDialogs();
    void onLockscreenShown();
    void destroy();
    void notifyVisibleActivityInfoChanged(VisibleActivityInfo visibleActivityInfo, int type);
}