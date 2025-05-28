package android.content.pm.dex;

public interface IArtManager {
   
    void snapshotRuntimeProfile(int profileType, String packageName,
        String codePath, ISnapshotRuntimeProfileCallback callback, String callingPackage);


    boolean isRuntimeProfilingEnabled(int profileType, String callingPackage);
}