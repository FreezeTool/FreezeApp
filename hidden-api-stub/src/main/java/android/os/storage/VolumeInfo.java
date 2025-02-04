package android.os.storage;

import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.util.Comparator;

public class VolumeInfo {
    public static final String ACTION_VOLUME_STATE_CHANGED =
            "android.os.storage.action.VOLUME_STATE_CHANGED";
    public static final String EXTRA_VOLUME_ID =
            "android.os.storage.extra.VOLUME_ID";
    public static final String EXTRA_VOLUME_STATE =
            "android.os.storage.extra.VOLUME_STATE";

    /**
     * Stub volume representing internal private storage
     */
    public static final String ID_PRIVATE_INTERNAL = "private";
    /**
     * Real volume representing internal emulated storage
     */
    public static final String ID_EMULATED_INTERNAL = "emulated";


    public static final int TYPE_PUBLIC = 0;
    public static final int TYPE_PRIVATE = 0;

    public static final int TYPE_EMULATED = 0;
    public static final int TYPE_ASEC = 0;
    public static final int TYPE_OBB = 0;
    public static final int TYPE_STUB = 0;

    public static final int STATE_UNMOUNTED = 0;
    public static final int STATE_CHECKING = 0;
    public static final int STATE_MOUNTED = 0;
    public static final int STATE_MOUNTED_READ_ONLY = 0;
    public static final int STATE_FORMATTING = 0;
    public static final int STATE_EJECTING = 0;
    public static final int STATE_UNMOUNTABLE = 0;
    public static final int STATE_REMOVED = 0;
    public static final int STATE_BAD_REMOVAL = 0;

    public static final int MOUNT_FLAG_PRIMARY = 0;
    public static final int MOUNT_FLAG_VISIBLE_FOR_READ = 0;
    public static final int MOUNT_FLAG_VISIBLE_FOR_WRITE = 0;


    /**
     * vold state
     */
    public String id;

    public int type;

    public DiskInfo disk;
    public String partGuid;
    public int mountFlags = 0;
    public int mountUserId = 0;

    public int state = STATE_UNMOUNTED;
    public String fsType;

    public String fsUuid;

    public String fsLabel;

    public String path;

    public String internalPath;

    public VolumeInfo(String id, int type, DiskInfo disk, String partGuid) {

    }

    public VolumeInfo(VolumeInfo volumeInfo) {
        this.id = volumeInfo.id;
        this.type = volumeInfo.type;
        this.disk = volumeInfo.disk;
        this.partGuid = volumeInfo.partGuid;
        this.mountFlags = volumeInfo.mountFlags;
        this.mountUserId = volumeInfo.mountUserId;
        this.state = volumeInfo.state;
        this.fsType = volumeInfo.fsType;
        this.fsUuid = volumeInfo.fsUuid;
        this.fsLabel = volumeInfo.fsLabel;
        this.path = volumeInfo.path;
        this.internalPath = volumeInfo.internalPath;
    }


    public static String getEnvironmentForState(int state) {
        throw new RuntimeException("STUB");
    }

    public static String getBroadcastForEnvironment(String envState) {
        throw new RuntimeException("STUB");
    }

    public static String getBroadcastForState(int state) {
        throw new RuntimeException("STUB");
    }

    public static Comparator<VolumeInfo> getDescriptionComparator() {
        throw new RuntimeException("STUB");
    }


    public String getId() {
        throw new RuntimeException("STUB");
    }


    public DiskInfo getDisk() {
        throw new RuntimeException("STUB");
    }


    public String getDiskId() {
        throw new RuntimeException("STUB");
    }


    public int getType() {
        throw new RuntimeException("STUB");
    }


    public int getState() {
        throw new RuntimeException("STUB");
    }

    public int getStateDescription() {
        throw new RuntimeException("STUB");
    }


    public String getFsUuid() {
        throw new RuntimeException("STUB");
    }

    public String getNormalizedFsUuid() {
        throw new RuntimeException("STUB");
    }


    public int getMountUserId() {
        throw new RuntimeException("STUB");
    }


    public String getDescription() {
        throw new RuntimeException("STUB");
    }


    public boolean isMountedReadable() {
        throw new RuntimeException("STUB");
    }


    public boolean isMountedWritable() {
        throw new RuntimeException("STUB");
    }


    public boolean isPrimary() {
        throw new RuntimeException("STUB");
    }


    public boolean isPrimaryPhysical() {
        throw new RuntimeException("STUB");
    }

    private boolean isVisibleForRead() {
        throw new RuntimeException("STUB");
    }

    private boolean isVisibleForWrite() {
        throw new RuntimeException("STUB");
    }


    public boolean isVisible() {
        throw new RuntimeException("STUB");
    }

    private boolean isVolumeSupportedForUser(int userId) {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns {@code true} if this volume is visible for {@code userId}, {@code false} otherwise.
     */
    public boolean isVisibleForUser(int userId) {
        throw new RuntimeException("STUB");
    }

    /**
     * Returns {@code true} if this volume is the primary emulated volume for {@code userId},
     * {@code false} otherwise.
     */
    public boolean isPrimaryEmulatedForUser(int userId) {
        throw new RuntimeException("STUB");
    }

    public boolean isVisibleForRead(int userId) {
        return isVolumeSupportedForUser(userId) && isVisibleForRead();
    }

    public boolean isVisibleForWrite(int userId) {
        throw new RuntimeException("STUB");
    }


    public File getPath() {
        throw new RuntimeException("STUB");
    }

    public File getInternalPath() {
        return (internalPath != null) ? new File(internalPath) : null;
    }


    public File getPathForUser(int userId) {
        if (path == null) {
            return null;
        } else if (type == TYPE_PUBLIC || type == TYPE_STUB) {
            return new File(path);
        } else if (type == TYPE_EMULATED) {
            return new File(path, Integer.toString(userId));
        } else {
            return null;
        }
    }


    public File getInternalPathForUser(int userId) {
        if (path == null) {
            return null;
        } else if (type == TYPE_PUBLIC || type == TYPE_STUB) {
            // TODO: plumb through cleaner path from vold
            return new File(path.replace("/storage/", "/mnt/media_rw/"));
        } else {
            return getPathForUser(userId);
        }
    }


    public StorageVolume buildStorageVolume(Context context, int userId, boolean reportUnmounted) {
        throw new RuntimeException("STUB");
    }


    public static int buildStableMtpStorageId(String fsUuid) {
        throw new RuntimeException("STUB");
    }


    /**
     * Build an intent to browse the contents of this volume. Only valid for
     * {@link #TYPE_EMULATED} or {@link #TYPE_PUBLIC}.
     */

    public Intent buildBrowseIntent() {
        throw new RuntimeException("STUB");
    }

    public Intent buildBrowseIntentForUser(int userId) {
        throw new RuntimeException("STUB");
    }
}