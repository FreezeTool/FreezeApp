package android.content.pm;

import java.util.List;

public interface IOnChecksumsReadyListener {
    void onChecksumsReady(List<ApkChecksum> checksums);
}