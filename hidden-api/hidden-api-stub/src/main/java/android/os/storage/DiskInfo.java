package android.os.storage;

public class DiskInfo {
    public static final String ACTION_DISK_SCANNED =
            "android.os.storage.action.DISK_SCANNED";
    public static final String EXTRA_DISK_ID =
            "android.os.storage.extra.DISK_ID";
    public static final String EXTRA_VOLUME_COUNT =
            "android.os.storage.extra.VOLUME_COUNT";

    public static final int FLAG_ADOPTABLE = 1 << 0;
    public static final int FLAG_DEFAULT_PRIMARY = 1 << 1;
    public static final int FLAG_SD = 1 << 2;
    public static final int FLAG_USB = 1 << 3;
    /**
     * The FLAG_STUB_VISIBLE is set from vold, which gets the flag from outside (e.g., ChromeOS)
     */
    public static final int FLAG_STUB_VISIBLE = 1 << 6;

    public String id;

    public int flags;

    public long size;

    public String label;
    /**
     * Hacky; don't rely on this count
     */
    public int volumeCount;
    public String sysPath;


    public String getId() {
        throw new RuntimeException("STUB");
    }

    public String getDescription() {
        throw new RuntimeException("STUB");
    }

    public String getShortDescription() {
        throw new RuntimeException("STUB");
    }


    public boolean isAdoptable() {
        throw new RuntimeException("STUB");
    }


    public boolean isDefaultPrimary() {
        throw new RuntimeException("STUB");
    }


    public boolean isSd() {
        throw new RuntimeException("STUB");
    }


    public boolean isUsb() {
        throw new RuntimeException("STUB");
    }

    public boolean isStubVisible() {
        throw new RuntimeException("STUB");
    }

}