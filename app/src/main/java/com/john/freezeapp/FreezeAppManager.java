package com.john.freezeapp;

import static android.content.pm.PackageManager.GET_META_DATA;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.john.freezeapp.client.ClientRemoteShell;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FreezeAppManager {


    private static final Map<String, AppModel> sAllAppMap = new HashMap<>();

    private static final Map<String, AppModel> sCacheAppModel = new ConcurrentHashMap<>();


    public static class AppModel {
        public String packageName;
        public String name;
        public Drawable icon;
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

        ClientRemoteShell.execCommand(String.format("am force-stop %s", packageName), new ClientRemoteShell.RemoteShellCommandResultCallback() {
            @Override
            public void callback(ClientRemoteShell.RemoteShellCommandResult commandResult) {
                if(commandResult.result) {
                    callback.success();
                } else {
                    callback.fail();
                }
            }
        });
    }


    public static void requestDefrostApp(String packageName, Callback2 callback) {
        ClientRemoteShell.execCommand(String.format("pm enable %s", packageName), new ClientRemoteShell.RemoteShellCommandResultCallback() {
            @Override
            public void callback(ClientRemoteShell.RemoteShellCommandResult commandResult) {
                if(commandResult.result) {
                    callback.success();
                } else {
                    callback.fail();
                }
            }
        });
    }


    public static void requestFreezeApp(String packageName, Callback2 callback) {

        ClientRemoteShell.execCommand(String.format("pm disable-user %s", packageName), new ClientRemoteShell.RemoteShellCommandResultCallback() {
            @Override
            public void callback(ClientRemoteShell.RemoteShellCommandResult commandResult) {
                if(commandResult.result) {
                    callback.success();
                } else {
                    callback.fail();
                }
            }
        });
    }

    public static void requestEnableApp(Context context, Callback callback) {
        requestAppList(context, "pm list packages -3 -e", callback);
    }

    public static void requestDisableApp(Context context, Callback callback) {
        requestAppList(context, "pm list packages -3 -d", callback);
    }

    private static void requestAppList(Context context, String command, Callback callback) {

        ClientRemoteShell.execCommand(command, new ClientRemoteShell.RemoteShellCommandResultCallback() {
            @Override
            public void callback(ClientRemoteShell.RemoteShellCommandResult commandResult) {
                try {
                    List<AppModel> list = new ArrayList<>();
                    String[] split = commandResult.successMsg.split("\n");
                    for (String s : split) {
                        String[] split1 = s.split(":");
                        if (split1.length == 2 && TextUtils.equals(split1[0], "package") && !TextUtils.equals(split1[1].trim(), context.getPackageName())) {
                            String packageName = split1[1].trim();
                            AppModel appModel = getAppModel(context, packageName);
                            list.add(appModel);
                        }
                    }
                    callback.success(list);
                } catch (Exception e) {
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
            requestRunningProcess(context, callback);
        } else {

            requestAppList(context, "pm list packages -3", new Callback() {
                @Override
                public void success(List<AppModel> list) {
                    sAllAppMap.clear();
                    for (AppModel appModel : list) {
                        sAllAppMap.put(appModel.packageName, appModel);
                    }
                    requestRunningProcess(context, callback);
                }

                @Override
                public void fail() {

                }
            });
        }
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
                                        runningModel = new RunningModel();
                                        runningModel.packageName = appModel.packageName;
                                        runningModel.name = appModel.name;
                                        runningModel.icon = appModel.icon;
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

    public static AppModel getAppModel(Context context, String packageName) {
        AppModel appModel = new AppModel();
        appModel.packageName = packageName;

        AppModel cacheAppModel = sCacheAppModel.get(packageName);

        if (cacheAppModel == null) {
            synchronized (sCacheAppModel) {
                cacheAppModel = sCacheAppModel.get(packageName);
                if (cacheAppModel == null) {
                    cacheAppModel = new AppModel();
                    try {
                        PackageManager packageManager = context.getPackageManager();
                        ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, GET_META_DATA);
                        cacheAppModel.name = applicationInfo.loadLabel(packageManager).toString();
                        cacheAppModel.icon = applicationInfo.loadIcon(packageManager);

                    } catch (PackageManager.NameNotFoundException e) {
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

}
