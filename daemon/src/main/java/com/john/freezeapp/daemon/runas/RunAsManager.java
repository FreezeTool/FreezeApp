package com.john.freezeapp.daemon.runas;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ParceledListSlice;
import android.os.RemoteException;
import android.text.TextUtils;

import com.john.freezeapp.daemon.CommonShellUtils;
import com.john.freezeapp.daemon.DaemonHelper;
import com.john.freezeapp.daemon.DaemonLog;
import com.john.freezeapp.daemon.DaemonService;
import com.john.freezeapp.daemon.util.ProcessUtils;
import com.john.freezeapp.util.CommonUtil;
import com.john.freezeapp.util.DeviceUtil;
import com.john.freezeapp.util.PackageUtil;

import java.util.ArrayList;
import java.util.List;

public class RunAsManager {

    public static void startRunAs(String classPath, String packageName) {
        killAllRunAsProcess();
        try {
            String command = CommonUtil.getAppProcessShell(classPath, RunAs.class.getName(), DaemonHelper.DAEMON_RUNAS_PREFIX + packageName, packageName, true);
//            String userArg = "--user " + android.os.Process.myUid() + " ";
            String userArg = "";
            CommonShellUtils.execCommand(String.format("run-as %s %s %s", packageName, userArg, command), false, null);
        } catch (Exception e) {
            DaemonLog.e(e, "DaemonRunAsBinder");
        }
    }


    public static void requestAllRunAsProcessModel(Callback callback) {
        requestRunAsProcessModel(null, callback);
    }

    public static void requestRunAsProcessModel(String pkg, Callback callback) {
        String queryRunAsProcessCommand = String.format("ps -A -o pid,args | grep '%s'", DaemonHelper.DAEMON_RUNAS_FILTER_PREFIX + (pkg != null ? pkg : ""));
        DaemonLog.log("requestRunAsProcessModel queryRunAsProcessCommand=" + queryRunAsProcessCommand);
        CommonShellUtils.execCommand(queryRunAsProcessCommand, false, commandResult -> {
            DaemonLog.log("requestRunAsProcessModel commandResult=" + commandResult.toString());
            try {
                if (commandResult.result && !TextUtils.isEmpty(commandResult.successMsg)) {
                    String[] processInfoList = commandResult.successMsg.trim().split("\n");
                    List<RunAsProcessModel> list = new ArrayList<>();
                    for (String processInfo : processInfoList) {
                        String[] freezeAppRunAs = processInfo.trim().split(" ");
                        if (freezeAppRunAs.length == 2) {
                            DaemonLog.log("requestRunAsProcessModel pid=" + freezeAppRunAs[0] + ", args=" + freezeAppRunAs[1]);
                            String packageName = freezeAppRunAs[1].replace(DaemonHelper.DAEMON_RUNAS_PREFIX, "");
                            if (!TextUtils.isEmpty(packageName)) {
                                try {
                                    RunAsProcessModel runAsProcessModel = new RunAsProcessModel();
                                    runAsProcessModel.pid = Integer.parseInt(freezeAppRunAs[0]);
                                    runAsProcessModel.packageName = packageName;
                                    runAsProcessModel.active = true;
                                    list.add(runAsProcessModel);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    callback.success(list);
                }
            } catch (Exception e) {
                DaemonLog.e(e, "requestRunAsProcessModel");
            }

            callback.fail();
        });
    }

    public static void killAllRunAsProcess() {
//        requestAllRunAsProcessModel(new Callback() {
//            @Override
//            public void success(List<RunAsQueryModel> models) {
//                for (RunAsQueryModel model : models) {
//                    killRunAsProcess(model.pid, model.packageName);
//                }
//            }
//
//            @Override
//            public void fail() {
//
//            }
//        });

        List<RunAsProcessModel> runAsAppProcess = ProcessUtils.getRunAsAppProcess();
        for (RunAsProcessModel asAppProcess : runAsAppProcess) {
            DaemonLog.log("killAllRunAsProcess pid=" + asAppProcess.pid + ",pkg=" + asAppProcess.packageName);
            killRunAsProcess(asAppProcess.pid, asAppProcess.packageName);
        }

    }


    public static void killRunAsProcess(int pid, String packageName) {
        String command = String.format("run-as %s kill -9 %s", packageName, String.valueOf(pid));
        DaemonLog.log("killRunAsProcess pid=" + pid + ", command=" + command);
        try {
            CommonShellUtils.execCommand(command, false, commandResult1 -> DaemonLog.log("killRunAsProcess pid=" + pid + ",commandResult=" + commandResult1));
        } catch (Exception e) {
            DaemonLog.e(e, "killRunAsProcess");
        }
    }

    public static void stopRunAs(String packageName) {
        List<RunAsProcessModel> runAsAppProcess = ProcessUtils.getRunAsAppProcess();
        for (RunAsProcessModel asAppProcess : runAsAppProcess) {
            if (TextUtils.equals(asAppProcess.packageName, packageName)) {
                killRunAsProcess(asAppProcess.pid, asAppProcess.packageName);
            }
        }
    }


    public interface Callback {
        void success(List<RunAsProcessModel> models);

        void fail();
    }

    public static List<RunAsProcessModel> getRunAsProcess() {
        List<RunAsProcessModel> runAsProcessModels = new ArrayList<>();
        List<PackageInfo> debugRunAsPackage = getInstallPackage();
        for (PackageInfo packageInfo : debugRunAsPackage) {
            boolean isAppDebuggable =
                    (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            if (isAppDebuggable) {
                RunAsProcessModel model = new RunAsProcessModel();
                model.packageName = packageInfo.packageName;
                model.active = false;
                runAsProcessModels.add(model);
            }
        }

        List<RunAsProcessModel> runAsAppRunningProcessModels = ProcessUtils.getRunAsAppProcess();
        for (RunAsProcessModel runAsProcessModel : runAsProcessModels) {
            RunAsProcessModel runningModel = runAsAppRunningProcessModels.stream().filter(runAsRunningProcessModel -> TextUtils.equals(runAsProcessModel.packageName, runAsRunningProcessModel.packageName)).findFirst().orElse(null);
            if (runningModel != null) {
                runAsProcessModel.pid = runningModel.pid;
                runAsProcessModel.active = true;
            }
        }

        return runAsProcessModels;
    }


    public static List<PackageInfo> getInstallPackage() {

        try {
            ParceledListSlice<PackageInfo> installedPackages = null;
            if (DeviceUtil.atLeast33()) {
                installedPackages = DaemonService.getPackageManager().getInstalledPackages(0L, 0);
            } else {
                installedPackages = DaemonService.getPackageManager().getInstalledPackages(0, 0);
            }
            if (installedPackages != null) {
                return PackageUtil.filterApp(installedPackages.getList(), PackageUtil.TYPE_NORMAL_APP, PackageUtil.STATUS_ALL, false);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;

    }


}
