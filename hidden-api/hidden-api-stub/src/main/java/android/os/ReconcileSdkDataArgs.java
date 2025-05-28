package android.os;

import java.util.List;

public class ReconcileSdkDataArgs {
    String uuid;
    String packageName;
    List<String> subDirNames;
    int userId;
    int appId;
    int previousAppId;
    String seInfo;
    int flags;
}
