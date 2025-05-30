package com.john.freezeapp.storage;

import android.app.usage.IStorageStatsManager;
import android.app.usage.StorageStats;
import android.content.Context;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageManager;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageStats;
import android.content.pm.ParceledListSlice;
import android.os.Build;
import android.os.UserHandleHidden;
import android.os.storage.IStorageManager;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.os.storage.StorageVolumeHidden;
import android.text.TextUtils;

import androidx.annotation.RequiresApi;

import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.client.ClientDaemonService;
import com.john.freezeapp.client.ClientLog;
import com.john.freezeapp.client.ClientRemoteShell;
import com.john.freezeapp.client.ClientSystemService;
import com.john.freezeapp.daemon.DaemonHelper;
import com.john.freezeapp.usagestats.UsageStats;
import com.john.freezeapp.usagestats.UsageStatsData;
import com.john.freezeapp.util.FreezeAppManager;
import com.john.freezeapp.util.PackageUtil;
import com.john.freezeapp.util.ThreadPool;
import com.john.hidden.api.ReplaceRef;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class Storage {

    private static final String INTERNAL_PATH = "/storage/emulated/0";

    interface Callback {
        void updateStorageStat(String packageName, StorageStats storageStats);

        void updatePackageInfo(List<PackageInfo> packageInfos);

        void complete();

        void fail();
    }

    interface Callback2 {
        void success(List<Model> models);

        void fail();
    }

    @RequiresApi(Build.VERSION_CODES.O)
    public static void requestStorageStats(Context context, Callback2 callback) {
        ThreadPool.execute(() -> {
            try {
                List<PackageInfo> installApp = FreezeAppManager.getInstallApp(PackageUtil.TYPE_NORMAL_APP, PackageUtil.STATUS_ENABLE_APP, true);
                StorageVolume storageVolume = getStorageVolume(context);
                List<Model> models = new ArrayList<>();
                String daemonPackageName = ClientDaemonService.getDaemonPackageName();
                if (storageVolume != null && installApp != null) {
                    IStorageStatsManager storageStatsManager = ClientSystemService.getStorageStatsManager();
                    for (PackageInfo packageInfo : installApp) {
                        StorageStats storageStats = storageStatsManager.queryStatsForPackage(storageVolume.getUuid(), packageInfo.packageName, 0, daemonPackageName);
                        if (storageStats != null) {
                            models.add(new Model(packageInfo.packageName, packageInfo, storageStats));
                        }
                    }
                }
                if (callback != null) {
                    callback.success(models);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (callback != null) {
                callback.fail();
            }
        });
    }

    @RequiresApi(Build.VERSION_CODES.O)
    public static void requestStorageStats(Context context, Callback callback) {
        ThreadPool.execute(() -> {
            try {
                List<PackageInfo> installApps = FreezeAppManager.getInstallApp(PackageUtil.TYPE_NORMAL_APP, PackageUtil.STATUS_ENABLE_APP, true);

                List<PackageInfo> packageInfos = new ArrayList<>();

                List<UsageStatsData> usageStatsData = UsageStats.getUsageStatsData();
                Collections.sort(usageStatsData);
                if (installApps != null) {
                    for (UsageStatsData usageStats : usageStatsData) {
                        PackageInfo currentPackageInfo = installApps.stream().filter(packageInfo -> TextUtils.equals(packageInfo.packageName, usageStats.packageName)).findFirst().orElse(null);
                        if (currentPackageInfo != null) {
                            packageInfos.add(currentPackageInfo);
                            installApps.remove(currentPackageInfo);
                        }
                    }
                    packageInfos.addAll(installApps);
                }
                if (callback != null) {
                    callback.updatePackageInfo(packageInfos);
                }
                String daemonPackageName = ClientDaemonService.getDaemonPackageName();
                StorageVolume storageVolume = getStorageVolume(context);
                IStorageStatsManager storageStatsManager = ClientSystemService.getStorageStatsManager();
                if (storageVolume != null) {
                    for (PackageInfo packageInfo : packageInfos) {
                        StorageStats storageStats = storageStatsManager.queryStatsForPackage(storageVolume.getUuid(), packageInfo.packageName, 0, daemonPackageName);
                        if (storageStats != null) {
                            if (callback != null) {
                                callback.updateStorageStat(packageInfo.packageName, storageStats);
                            }
                        }

                    }
                }
                if (callback != null) {
                    callback.complete();
                }
            } catch (Exception e) {
                //
                if (callback != null) {
                    callback.fail();
                }
            }
        });
    }

    private static StorageVolume getStorageVolume(Context context) {
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);

        List<StorageVolume> volumeList = storageManager.getStorageVolumes();
        if (volumeList.isEmpty()) {
            return null;
        }

        if (volumeList.size() == 1) {
            return volumeList.get(0);
        }
        for (StorageVolume storageVolume : volumeList) {
            if (ReplaceRef.<StorageVolumeHidden>unsafeCast(storageVolume).isExternallyManaged()) {
                return storageVolume;
            }
        }
        return volumeList.get(0);
    }


    public static class Model {
        public PackageInfo packageInfo;
        public StorageStats storageStats;
        public String packageName;


        public Model(String packageName, PackageInfo packageInfo, StorageStats storageStats) {
            this.packageInfo = packageInfo;
            this.packageName = packageName;
            this.storageStats = storageStats;
        }
    }

    interface Callback3 {
        void success(String packageName, StorageStats storageStats);

        void fail();
    }


    public static void clearCache2(Context context, boolean onlyCache, String packageName, Callback3 callback) {
        String cmd = String.format("pm clear %s", packageName);
        if (onlyCache) {
            cmd = String.format("pm clear--cache-only %s", packageName);
        }
        ClientRemoteShell.execCommand(cmd, new ClientRemoteShell.RemoteShellCommandResultCallback() {
            @Override
            public void callback(ClientRemoteShell.RemoteShellCommandResult commandResult) {
                if (!TextUtils.isEmpty(commandResult.successMsg)) {
                    String daemonPackageName = ClientDaemonService.getDaemonPackageName();
                    StorageVolume storageVolume = getStorageVolume(context);
                    IStorageStatsManager storageStatsManager = ClientSystemService.getStorageStatsManager();
                    if (storageStatsManager != null) {
                        StorageStats storageStats = storageStatsManager.queryStatsForPackage(storageVolume.getUuid(), packageName, 0, daemonPackageName);
                        if (storageStats != null) {
                            if (callback != null) {
                                callback.success(packageName, storageStats);
                                return;
                            }
                        }
                    }
                }
                if (callback != null) {
                    callback.fail();
                    return;
                }
            }
        });
    }

    public static void clearCache(Context context, String packageName, Callback3 callback) {
        ThreadPool.execute(() -> {
            String daemonPackageName = ClientDaemonService.getDaemonPackageName();
            StorageVolume storageVolume = getStorageVolume(context);
            if (storageVolume != null) {
                int runClear = translateUserId(2000, UserHandleHidden.USER_NULL, "runClear");
                ClientSystemService.getPackageManager().deleteApplicationCacheFiles(packageName, new IPackageDataObserver.Stub() {
                    @Override
                    public void onRemoveCompleted(String packageName, boolean succeeded) {
                        if (succeeded) {
                            IStorageStatsManager storageStatsManager = ClientSystemService.getStorageStatsManager();
                            if (storageStatsManager != null) {
                                StorageStats storageStats = storageStatsManager.queryStatsForPackage(storageVolume.getUuid(), packageName, 0, daemonPackageName);
                                if (storageStats != null) {
                                    if (callback != null) {
                                        callback.success(packageName, storageStats);
                                        return;
                                    }
                                }
                            }
                        }
                        if (callback != null) {
                            callback.fail();
                        }
                    }
                });
            }
        });
    }

    public static void requestAppSize(Context context, String packageName, Callback3 callback) {
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                String daemonPackageName = ClientDaemonService.getDaemonPackageName();
                StorageVolume storageVolume = getStorageVolume(context);
                if (storageVolume != null) {
                    IStorageStatsManager storageStatsManager = ClientSystemService.getStorageStatsManager();
                    if (storageStatsManager != null) {
                        StorageStats storageStats = storageStatsManager.queryStatsForPackage(storageVolume.getUuid(), packageName, 0, daemonPackageName);
                        if (storageStats != null) {
                            if (callback != null) {
                                callback.success(packageName, storageStats);
                                return;
                            }
                        }
                    }
                }
                if (callback != null) {
                    callback.fail();
                }
            }
        });
    }


    private static int translateUserId(int userId, int allUserId, String logContext) {

        final boolean allowAll = (allUserId != UserHandleHidden.USER_NULL);
        final int translatedUserId = ClientSystemService.getActivityManager().handleIncomingUser(ClientDaemonService.getDaemonPid(), ClientDaemonService.getDaemonUid(), userId, allowAll, true, logContext, "pm command");
        return translatedUserId == UserHandleHidden.USER_ALL ? allUserId : translatedUserId;

    }


}
