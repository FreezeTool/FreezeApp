package com.android.internal.app;

import android.app.VoiceInteractor;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ICancellationSignal;

public interface IVoiceInteractor {
    IVoiceInteractorRequest startConfirmation(String callingPackage,
                                              IVoiceInteractorCallback callback, VoiceInteractor.Prompt prompt, Bundle extras);
    IVoiceInteractorRequest startPickOption(String callingPackage,
            IVoiceInteractorCallback callback, VoiceInteractor.Prompt prompt,
            VoiceInteractor.PickOptionRequest.Option[] options, Bundle extras);
    IVoiceInteractorRequest startCompleteVoice(String callingPackage,
            IVoiceInteractorCallback callback, VoiceInteractor.Prompt prompt, Bundle extras);
    IVoiceInteractorRequest startAbortVoice(String callingPackage,
            IVoiceInteractorCallback callback, VoiceInteractor.Prompt prompt, Bundle extras);
    IVoiceInteractorRequest startCommand(String callingPackage,
            IVoiceInteractorCallback callback, String command, Bundle extras);
    boolean[] supportsCommands(String callingPackage, String[] commands);
    void notifyDirectActionsChanged(int taskId, IBinder assistToken);
    void setKillCallback(ICancellationSignal callback);
}