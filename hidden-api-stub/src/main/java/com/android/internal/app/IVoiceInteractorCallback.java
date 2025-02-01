package com.android.internal.app;

import android.app.VoiceInteractor;
import android.os.Bundle;

public interface IVoiceInteractorCallback {
    void deliverConfirmationResult(IVoiceInteractorRequest request, boolean confirmed,
            Bundle result);
    void deliverPickOptionResult(IVoiceInteractorRequest request, boolean finished,
                                 VoiceInteractor.PickOptionRequest.Option[] selections, Bundle result);
    void deliverCompleteVoiceResult(IVoiceInteractorRequest request, Bundle result);
    void deliverAbortVoiceResult(IVoiceInteractorRequest request, Bundle result);
    void deliverCommandResult(IVoiceInteractorRequest request, boolean finished, Bundle result);
    void deliverCancel(IVoiceInteractorRequest request);
    void destroy();
}