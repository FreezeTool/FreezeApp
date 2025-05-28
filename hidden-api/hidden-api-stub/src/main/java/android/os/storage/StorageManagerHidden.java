package android.os.storage;

import com.john.hidden.api.Replace;

import java.util.UUID;

@Replace(StorageManager.class)
public class StorageManagerHidden {
    public static final UUID UUID_DEFAULT = null;
    public static final UUID UUID_PRIMARY_PHYSICAL_ = null;
    public static final UUID UUID_SYSTEM_ = null;


    public static UUID convert(String uuid) {
        throw new RuntimeException("STUB");
    }

    public static String convert(UUID storageUuid) {
        throw new RuntimeException("STUB");
    }

}
