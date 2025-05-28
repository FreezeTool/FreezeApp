package android.os.storage;

import android.content.Context;

public class VolumeRecord {
    public static final String EXTRA_FS_UUID =
            "android.os.storage.extra.FS_UUID";

    public static final int USER_FLAG_INITED = 1 << 0;
    public static final int USER_FLAG_SNOOZED = 1 << 1;

    public final int type;
    public final String fsUuid;
    public String partGuid;
    public String nickname;
    public int userFlags;
    public long createdMillis;
    public long lastSeenMillis;
    public long lastTrimMillis;
    public long lastBenchMillis;

    public VolumeRecord(int type, String fsUuid) {
        throw new RuntimeException("STUB");
    }


    public int getType() {
        return type;
    }

    public String getFsUuid() {
        return fsUuid;
    }

    public String getNormalizedFsUuid() {
        throw new RuntimeException("STUB");
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isInited() {
        return (userFlags & USER_FLAG_INITED) != 0;
    }

    public boolean isSnoozed() {
        return (userFlags & USER_FLAG_SNOOZED) != 0;
    }

    public StorageVolume buildStorageVolume(Context context) {
        throw new RuntimeException("STUB");
    }
}