package com.john.freezeapp.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ParceledListSlice;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.text.TextUtils;

import androidx.annotation.IntDef;

import com.john.freezeapp.BuildConfig;
import com.john.freezeapp.client.ClientSystemService;
import com.john.freezeapp.daemon.DaemonShellUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class FreezeAppManager {


    private static final Map<String, AppModel> sAllAppMap = new HashMap<>();

    private static final Map<String, CacheAppModel> sCacheAppModel = new ConcurrentHashMap<>();


    @IntDef(value = {
            TYPE_ALL,
            TYPE_SYSTEM_APP,
            TYPE_NORMAL_APP
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface TYPE {
    }

    @IntDef(value = {
            STATUS_ALL,
            STATUS_ENABLE_APP,
            STATUS_DISABLE_APP,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface STATUS {
    }

    public static final int TYPE_ALL = 0;
    public static final int TYPE_SYSTEM_APP = 1;
    public static final int TYPE_NORMAL_APP = 2;

    public static final int STATUS_ALL = 0;
    public static final int STATUS_ENABLE_APP = 1;
    public static final int STATUS_DISABLE_APP = 2;

    public static class CacheAppModel {
        public String packageName;
        public String name;
        public Drawable icon;
    }

    public static class AppModel {
        public String packageName;
        public long lastUpdateTime;
        public long firstInstallTime;
        public String versionName;
        public int versionCode;

        public AppModel(String packageName) {
            this.packageName = packageName;
        }

        public AppModel(AppModel appModel) {
            this.packageName = appModel.packageName;
            this.lastUpdateTime = appModel.lastUpdateTime;
            this.firstInstallTime = appModel.firstInstallTime;
            this.versionName = appModel.versionName;
            this.versionCode = appModel.versionCode;
        }

    }

    public static class ProcessModel {
        // UID            PID  PPID C STIME TTY          TIME CMD
        //
        public String uid;
        public String pid;
        public String ppid;
        public String c;
        public String tty;
        public String packageName;
        public String processName;
        public String sTime;
        public String time;


    }

    public static class RunningModel extends AppModel {
        public List<ProcessModel> processModels = new ArrayList<>();

        public RunningModel(AppModel appModel) {
            super(appModel);
        }

        public void addProcess(ProcessModel processModel) {
            processModels.add(processModel);
        }
    }

    public interface Callback {
        void success(List<AppModel> list);

        void fail();
    }


    public interface Callback2 {
        void success();

        void fail();
    }

    public interface Callback3 {
        void success(List<RunningModel> list);

        void fail();
    }


    public static void requestForceStopApp(String packageName, Callback2 callback) {
        ThreadPool.execute(() -> {
            try {
                ClientSystemService.getActivityManager().forceStopPackage(packageName, 0);
                if (callback != null) {
                    callback.success();
                }
            } catch (RemoteException e) {
                if (callback != null) {
                    callback.fail();
                }
            }
        });

    }


    public static void requestDefrostApp(String packageName, Callback2 callback) {
        ThreadPool.execute(() -> {
            try {
                ClientSystemService.getPackageManager().setApplicationEnabledSetting(packageName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 0, 0, "");
                callback.success();
            } catch (RemoteException e) {
                e.printStackTrace();
                callback.fail();
            }
        });

    }


    public static void requestFreezeApp(String packageName, Callback2 callback) {
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ClientSystemService.getPackageManager().setApplicationEnabledSetting(packageName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER, 0, 0, "");
                    callback.success();
                } catch (RemoteException e) {
                    e.printStackTrace();
                    callback.fail();
                }
            }
        });
    }

    public static void requestEnableApp(Context context, Callback callback) {
        requestAppList(context, TYPE_NORMAL_APP, STATUS_ENABLE_APP, true, callback);
    }

    public static void requestDisableApp(Context context, Callback callback) {
        requestAppList(context, TYPE_NORMAL_APP, STATUS_DISABLE_APP, true, callback);
    }

    public static void requestAllUserApp(Context context, Callback callback) {
        requestAppList(context, TYPE_NORMAL_APP, STATUS_ALL, false, callback);
    }

    public static void requestAppList(Context context, @TYPE int appType, @STATUS int appStatus, boolean ignoreFreezeApp, Callback callback) {
        ThreadPool.execute(() -> {
            List<AppModel> install = getInstallAppModel(appType, appStatus, ignoreFreezeApp);
            if (install != null) {
                callback.success(install);
            } else {
                callback.fail();
            }
        });
    }

    public static List<AppModel> getInstallAppModel(@TYPE int appType, @STATUS int appStatus, boolean ignoreFreezeApp) {
        List<PackageInfo> packageInfos = getInstallApp(appType, appStatus, ignoreFreezeApp);
        if (packageInfos != null) {
            List<AppModel> list = new ArrayList<>();
            for (PackageInfo packageInfo : packageInfos) {

                AppModel appModel = new AppModel(packageInfo.packageName);
                appModel.lastUpdateTime = packageInfo.lastUpdateTime;
                appModel.firstInstallTime = packageInfo.firstInstallTime;
                appModel.versionName = packageInfo.versionName;
                appModel.versionCode = packageInfo.versionCode;
                list.add(appModel);
            }
            return list;
        }
        return null;
    }


    public static void requestRunningApp(Context context, Callback3 callback) {
        requestRunningApp(context, false, callback);
    }

    public static void requestRunningApp(Context context, boolean force, Callback3 callback) {
        if (force) {
            sAllAppMap.clear();
        }
        if (!sAllAppMap.isEmpty()) {
            requestRunningProcess2(context, callback);
        } else {

            requestAppList(context, TYPE_NORMAL_APP, STATUS_ENABLE_APP, true, new Callback() {
                @Override
                public void success(List<AppModel> list) {
                    sAllAppMap.clear();
                    for (AppModel appModel : list) {
                        sAllAppMap.put(appModel.packageName, appModel);
                    }
                    requestRunningProcess2(context, callback);
                }

                @Override
                public void fail() {

                }
            });
        }
    }

    private static void requestRunningProcess2(Context context, Callback3 callback) {
        ThreadPool.execute(() -> {
            try {
                List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ClientSystemService.getActivityManager().getRunningAppProcesses();
                Map<String, RunningModel> runningModelMap = new HashMap<>();
                for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
                    ProcessModel processModel = new ProcessModel();
                    processModel.processName = runningAppProcess.processName;
                    processModel.time = runningAppProcess.pid + "";
                    for (String packageName : runningAppProcess.pkgList) {
                        if (!TextUtils.equals(packageName, BuildConfig.APPLICATION_ID)) {
                            AppModel appModel = sAllAppMap.get(packageName);
                            if (appModel != null) {
                                RunningModel runningModel = runningModelMap.get(packageName);
                                if (runningModel == null) {
                                    runningModel = new RunningModel(appModel);
                                    runningModelMap.put(packageName, runningModel);
                                }
                                runningModel.addProcess(processModel);
                            }
                        }
                    }
                }

                List<RunningModel> runningModels = new ArrayList<>();

                for (Map.Entry<String, RunningModel> entry : runningModelMap.entrySet()) {
                    runningModels.add(entry.getValue());
                }
                callback.success(runningModels);

            } catch (RemoteException e) {
                e.printStackTrace();
                callback.fail();
            }
        });
    }

    public static CacheAppModel getAppModel(Context context, String packageName) {
        return getAppModel(context, packageName, false);
    }

    public static CacheAppModel getAppModel(Context context, String packageName, boolean onlyCache) {
        CacheAppModel appModel = new CacheAppModel();
        appModel.packageName = packageName;

        CacheAppModel cacheAppModel = sCacheAppModel.get(packageName);

        if (onlyCache) {
            if (cacheAppModel == null) {
                return null;
            }
        }

        if (cacheAppModel == null) {
            synchronized (sCacheAppModel) {
                cacheAppModel = sCacheAppModel.get(packageName);
                if (cacheAppModel == null) {
                    cacheAppModel = new CacheAppModel();
                    try {
                        ApplicationInfo applicationInfo = getApplicationInfo(packageName);
                        cacheAppModel.name = applicationInfo.loadLabel(context.getPackageManager()).toString();
                        cacheAppModel.icon = applicationInfo.loadIcon(context.getPackageManager());
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    sCacheAppModel.put(packageName, cacheAppModel);
                }
            }
        }

        appModel.name = cacheAppModel.name;
        appModel.icon = cacheAppModel.icon;

        return appModel;
    }

    private static ApplicationInfo getApplicationInfo(String packageName) throws RemoteException {
        IPackageManager iPackageManager = ClientSystemService.getPackageManager();
        ApplicationInfo applicationInfo;
        if (DeviceUtil.atLeast33()) {
            applicationInfo = iPackageManager.getApplicationInfo(packageName, 0L, 0);
        } else {
            applicationInfo = iPackageManager.getApplicationInfo(packageName, 0, 0);
        }
        return applicationInfo;
    }

    public static List<PackageInfo> getInstallUserApp() {
        return getInstallApp(TYPE_NORMAL_APP, STATUS_ALL, false);
    }

    public static List<PackageInfo> getInstallApp(@TYPE int appType, @STATUS int appStatus, boolean ignoreFreezeApp) {
        try {
            List<PackageInfo> packageInfos = new ArrayList<>();
            ParceledListSlice<PackageInfo> installedPackages = null;
            if (DeviceUtil.atLeast33()) {
                installedPackages = ClientSystemService.getPackageManager().getInstalledPackages(0L, 0);
            } else {
                installedPackages = ClientSystemService.getPackageManager().getInstalledPackages(0, 0);
            }
            if (installedPackages != null) {
                for (PackageInfo packageInfo : installedPackages.getList()) {


                    if (TextUtils.equals(packageInfo.packageName, BuildConfig.APPLICATION_ID) && ignoreFreezeApp) {
                        continue;
                    }

                    boolean isApex = false;
                    if (DeviceUtil.atLeast29()) {
                        isApex = packageInfo.isApex;
                    }
                    final boolean isSystem = !isApex
                            && (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
                    final boolean isEnabled = !isApex && packageInfo.applicationInfo.enabled;

                    if (appType != TYPE_ALL) {
                        if ((appType == TYPE_SYSTEM_APP && !isSystem) || (appType == TYPE_NORMAL_APP && isSystem)) {
                            continue;
                        }
                    }

                    if (appStatus != STATUS_ALL) {
                        if ((appStatus == STATUS_DISABLE_APP && isEnabled) || (appStatus == STATUS_ENABLE_APP && !isEnabled)) {
                            continue;
                        }
                    }


                    packageInfos.add(packageInfo);
                }
            }

            return packageInfos;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void toRoot(Context context) {
        DaemonShellUtils.execCommand(FreezeUtil.getStartShell(context), true, new DaemonShellUtils.ShellCommandResultCallback() {
            @Override
            public void callback(DaemonShellUtils.ShellCommandResult commandResult) {

            }
        });
    }


}
