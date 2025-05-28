package android.window;

import android.os.IBinder;

public interface IWindowOrganizerController {

   
//    void applyTransaction(WindowContainerTransaction t);
//
//
//    int applySyncTransaction(WindowContainerTransaction t,
//            IWindowContainerTransactionCallback callback);
//
//    /**
//     * Starts a new transition.
//     * @param type The transition type.
//     * @param t Operations that are part of the transition.
//     * @return a token representing the transition.
//     */
//    IBinder startNewTransition(int type,  WindowContainerTransaction t);
//
//    /**
//     * Starts the given transition.
//     * @param transitionToken A token associated with the transition to start.
//     * @param t Operations that are part of the transition.
//     */
//    void startTransition(IBinder transitionToken,  WindowContainerTransaction t);
//
//    /**
//     * Starts a legacy transition.
//     * @param type The transition type.
//     * @param adapter The animation to use.
//     * @param syncCallback A sync callback for the contents of `t`
//     * @param t Operations that are part of the transition.
//     * @return sync-id or -1 if this no-op'd because a transition is already running.
//     */
//    int startLegacyTransition(int type, RemoteAnimationAdapter adapter,
//            IWindowContainerTransactionCallback syncCallback, WindowContainerTransaction t);
//
//    /**
//     * Finishes a transition. This must be called for all created transitions.
//     * @param transitionToken Which transition to finish
//     * @param t Changes to make before finishing but the same SF Transaction. Can be null.
//     * @param callback Called when t is finished applying.
//     * @return An ID for the sync operation (see {@link #applySyncTransaction}. This will be
//     *         negative if no sync transaction was attached (null t or callback)
//     */
//    int finishTransition(IBinder transitionToken,
//             WindowContainerTransaction t,
//            IWindowContainerTransactionCallback callback);
//
//    /** @return An interface enabling the management of task organizers. */
//    ITaskOrganizerController getTaskOrganizerController();
//
//    /** @return An interface enabling the management of display area organizers. */
//    IDisplayAreaOrganizerController getDisplayAreaOrganizerController();
//
//    /** @return An interface enabling the management of task fragment organizers. */
//    ITaskFragmentOrganizerController getTaskFragmentOrganizerController();
//
//    /**
//     * Registers a transition player with Core. There is only one of these at a time and calling
//     * this will replace the existing one if set.
//     */
//    void registerTransitionPlayer(ITransitionPlayer player);
//
//    /** @return An interface enabling the transition players to report its metrics. */
//    ITransitionMetricsReporter getTransitionMetricsReporter();
}