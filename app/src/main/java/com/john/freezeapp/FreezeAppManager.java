package com.john.freezeapp;

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
import android.view.View;

import com.google.gson.Gson;
import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.client.ClientLog;
import com.john.freezeapp.client.ClientRemoteShell;
import com.john.freezeapp.daemon.DaemonShellUtils;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuRemoteProcess;

public class FreezeAppManager {


    private static final Map<String, AppModel> sAllAppMap = new HashMap<>();

    private static final Map<String, CacheAppModel> sCacheAppModel = new ConcurrentHashMap<>();

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

        public AppModel(String packageName) {
            this.packageName = packageName;
        }

        public AppModel(AppModel appModel) {
            this.packageName = appModel.packageName;
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
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ClientBinderManager.getActivityManager().forceStopPackage(packageName, 0);
                    if (callback != null) {
                        callback.success();
                    }
                } catch (RemoteException e) {
                    if (callback != null) {
                        callback.fail();
                    }
                }
            }
        });

    }


    public static void requestDefrostApp(String packageName, Callback2 callback) {
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ClientBinderManager.getPackageManager().setApplicationEnabledSetting(packageName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, 0, 0, "");
                    callback.success();
                } catch (RemoteException e) {
                    e.printStackTrace();
                    callback.fail();
                }
            }
        });

    }


    public static void requestFreezeApp(String packageName, Callback2 callback) {
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ClientBinderManager.getPackageManager().setApplicationEnabledSetting(packageName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER, 0, 0, "");
                    callback.success();
                } catch (RemoteException e) {
                    e.printStackTrace();
                    callback.fail();
                }
            }
        });
    }

    public static void requestCommand(String command, Callback2 callback) {
        ClientRemoteShell.execCommand(command, new ClientRemoteShell.RemoteShellCommandResultCallback() {
            @Override
            public void callback(ClientRemoteShell.RemoteShellCommandResult commandResult) {
                if (commandResult.result) {
                    callback.success();
                } else {
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

    private static void requestAppList(Context context, int appType, int appStatus, boolean ignoreFreezeApp, Callback callback) {
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                List<PackageInfo> packageInfos = getInstallApp(appType, appStatus);
                if (packageInfos != null) {
                    List<AppModel> list = new ArrayList<>();
                    for (PackageInfo packageInfo : packageInfos) {
                        if (TextUtils.equals(packageInfo.packageName, BuildConfig.APPLICATION_ID) && ignoreFreezeApp) {
                            continue;
                        }
                        list.add(new AppModel(packageInfo.packageName));
                    }
                    callback.success(list);
                } else {
                    callback.fail();
                }
            }
        });
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
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ClientBinderManager.getActivityManager().getRunningAppProcesses();
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
            }
        });
    }

    private static void requestRunningProcess(Context context, Callback3 callback) {

        ClientRemoteShell.execCommand("ps -ef | grep -vE \"root|system|media|shell|radio|nobody|wifi|gps\"", new ClientRemoteShell.RemoteShellCommandResultCallback() {
            @Override
            public void callback(ClientRemoteShell.RemoteShellCommandResult commandResult) {
                if (!TextUtils.isEmpty(commandResult.successMsg)) {
                    try {
                        Map<String, RunningModel> runningModelMap = new HashMap<>();
                        String[] line = commandResult.successMsg.split("\n");
                        for (int i = 0; i < line.length; i++) {
                            if (i == 0) {
                                continue;
                            }
                            // UID        0
                            // PID        1
                            // PPID       2
                            // C          3
                            // STIME      4
                            // TTY        5
                            // TIME       6
                            // CMD        7
                            String[] info = line[i].split("\\s+");
                            if (info.length == 8) {
                                String[] processInfo = info[7].split(":");
                                String packageName = processInfo[0];
                                ProcessModel processModel = new ProcessModel();
                                processModel.packageName = packageName;
                                processModel.uid = info[0];
                                processModel.pid = info[1];
                                processModel.ppid = info[2];
                                processModel.c = info[3];
                                processModel.sTime = info[4];
                                processModel.tty = info[5];
                                processModel.time = info[6];
                                processModel.processName = info[7];

                                RunningModel runningModel = runningModelMap.get(packageName);

                                if (runningModel == null) {
                                    AppModel appModel = sAllAppMap.get(packageName);
                                    if (appModel != null) {
                                        runningModel = new RunningModel(appModel);
                                        runningModel.packageName = appModel.packageName;
                                        runningModelMap.put(packageName, runningModel);
                                    }
                                }

                                if (runningModel != null) {
                                    runningModel.addProcess(processModel);
                                }
                            }
                        }

                        List<RunningModel> runningModels = new ArrayList<>();
                        for (Map.Entry<String, RunningModel> entry : runningModelMap.entrySet()) {
                            runningModels.add(entry.getValue());
                        }
                        callback.success(runningModels);
                    } catch (Exception e) {
                        callback.fail();
                    }
                } else {
                    callback.fail();
                }
            }
        });

    }

    public static CacheAppModel getAppModel(Context context, String packageName) {
        CacheAppModel appModel = new CacheAppModel();
        appModel.packageName = packageName;

        CacheAppModel cacheAppModel = sCacheAppModel.get(packageName);

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
        IPackageManager iPackageManager = ClientBinderManager.getPackageManager();
        ApplicationInfo applicationInfo;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            applicationInfo = iPackageManager.getApplicationInfo(packageName, 0L, 0);
        } else {
            applicationInfo = iPackageManager.getApplicationInfo(packageName, 0, 0);
        }
        return applicationInfo;
    }

    public static List<PackageInfo> getInstallUserApp() {
        return getInstallApp(TYPE_NORMAL_APP, STATUS_ALL);
    }

    public static List<PackageInfo> getInstallApp(int appType, int appStatus) {
        try {
            List<PackageInfo> packageInfos = new ArrayList<>();
            ParceledListSlice<PackageInfo> installedPackages = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                installedPackages = ClientBinderManager.getPackageManager().getInstalledPackages(0L, 0);
            } else {
                installedPackages = ClientBinderManager.getPackageManager().getInstalledPackages(0, 0);
            }
            for (PackageInfo packageInfo : installedPackages.getList()) {

                boolean isApex = false;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
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

            return packageInfos;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void execShizuku(Context context, Callback2 callback) {
        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                String[] arrays = {"sh"};
                ShizukuRemoteProcess shizukuRemoteProcess = Shizuku.newProcess(arrays, null, null);
                DataOutputStream os = null;
                try {
                    os = new DataOutputStream(shizukuRemoteProcess.getOutputStream());
                    os.write(FreezeUtil.getStartShell(context).getBytes());
                    os.writeBytes("\n");
                    os.flush();
                    os.writeBytes("exit\n");
                    os.flush();
                    boolean result = DaemonShellUtils.waitFor(shizukuRemoteProcess, 1, TimeUnit.SECONDS);
                    if (callback != null) {
                        callback.success();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (callback != null) {
                        callback.fail();
                    }
                } finally {
                    try {
                        os.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public static void toRoot(Context context) {
        DaemonShellUtils.execCommand(FreezeUtil.getStartShell(context), true, new DaemonShellUtils.ShellCommandResultCallback() {
            @Override
            public void callback(DaemonShellUtils.ShellCommandResult commandResult) {

            }
        });
    }


}
