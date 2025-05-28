package android.content.pm;

public class IncrementalStatesInfo {
    private boolean mIsLoading;
    private float mProgress;

    public IncrementalStatesInfo(boolean isLoading, float progress) {
        mIsLoading = isLoading;
        mProgress = progress;
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    public float getProgress() {
        return mProgress;
    }
}