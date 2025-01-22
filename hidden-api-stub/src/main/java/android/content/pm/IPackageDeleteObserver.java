package android.content.pm;

public interface IPackageDeleteObserver {
    void packageDeleted(String packageName, int returnCode);
}