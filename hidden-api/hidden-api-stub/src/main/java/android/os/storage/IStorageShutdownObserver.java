package android.os.storage;

interface IStorageShutdownObserver {
    /**
     * This method is called when the shutdown of StorageManagerService completed.
     *
     * @param statusCode indicates success or failure of the shutdown.
     */
    void onShutDownComplete(int statusCode);

    /**
     * Don't change the existing transaction Ids as they could be used in the native code.
     * When adding a new method, assign the next available transaction id.
     */
}