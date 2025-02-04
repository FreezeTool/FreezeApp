package android.os.storage;

public interface IStorageEventListener {

    void onUsbMassStorageConnectionChanged(boolean connected) ;

    void onStorageStateChanged(String path, String oldState, String newState) ;

    void onVolumeStateChanged(VolumeInfo vol, int oldState, int newState) ;

    void onVolumeRecordChanged(VolumeRecord rec) ;

    void onVolumeForgotten(String fsUuid) ;

    void onDiskScanned(DiskInfo disk, int volumeCount) ;

    void onDiskDestroyed(DiskInfo disk) ;


}