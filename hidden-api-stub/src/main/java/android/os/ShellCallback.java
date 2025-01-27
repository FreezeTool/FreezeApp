package android.os;

public class ShellCallback {

    public ShellCallback() {

    }

    public ParcelFileDescriptor openFile(String path, String seLinuxContext, String mode) {
        throw new RuntimeException("STUB");
    }

    public ParcelFileDescriptor onOpenFile(String path, String seLinuxContext, String mode) {
        throw new RuntimeException("STUB");
    }

    public IBinder getShellCallbackBinder() {
        throw new RuntimeException("STUB");
    }

    ShellCallback(Parcel in) {
        throw new RuntimeException("STUB");
    }
}