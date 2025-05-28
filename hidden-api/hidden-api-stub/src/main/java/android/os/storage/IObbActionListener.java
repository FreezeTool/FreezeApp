package android.os.storage;

public interface IObbActionListener {
    /**
     * Return from an OBB action result.
     *
     * @param filename the path to the OBB the operation was performed on
     * @param nonce    identifier that is meaningful to the receiver
     * @param status   status code as defined in {@link OnObbStateChangeListener}
     */
    void onObbResult(String filename, int nonce, int status);

    /**
     * Don't change the existing transaction Ids as they could be used in the native code.
     * When adding a new method, assign the next available transaction id.
     */
}