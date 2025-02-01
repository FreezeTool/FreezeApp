package android.view;

import android.window.TaskSnapshot;

interface IRecentsAnimationController {

    /**
     * Takes a screenshot of the task associated with the given {@param taskId}. Only valid for the
     * current set of task ids provided to the handler.
     */
    TaskSnapshot screenshotTask(int taskId);


//    void setFinishTaskTransaction(int taskId,
//                                  PictureInPictureSurfaceTransaction finishTransaction, SurfaceControl overlay);


    void finish(boolean moveHomeToTop, boolean sendUserLeaveHint);

    void setInputConsumerEnabled(boolean enabled);


    void setAnimationTargetsBehindSystemBars(boolean behindSystemBars);

    void cleanupScreenshot();


    void setDeferCancelUntilNextTransition(boolean defer, boolean screenshot);


    void setWillFinishToHome(boolean willFinishToHome);


    boolean removeTask(int taskId);


    void detachNavigationBarFromApp(boolean moveHomeToTop);

    void animateNavigationBarToApp(long duration);
}