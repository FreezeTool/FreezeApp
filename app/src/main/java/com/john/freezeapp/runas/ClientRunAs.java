package com.john.freezeapp.runas;

import android.content.Context;
import android.os.IBinder;
import android.os.RemoteException;

import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.daemon.DaemonHelper;
import com.john.freezeapp.daemon.runas.IRunAsBinder;
import com.john.freezeapp.daemon.runas.RunAsProcessModel;
import com.john.freezeapp.fs.ClientFileServer;
import com.john.freezeapp.util.CommonConstant;

import java.util.ArrayList;
import java.util.List;

public class ClientRunAs {

    public static IRunAsBinder getRunAsBinder() {
        try {
            IBinder service = ClientBinderManager.getDaemonBinder().getService(DaemonHelper.DAEMON_BINDER_RUN_AS);
            return IRunAsBinder.Stub.asInterface(service);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void startServer(Context context, String packageName) {
        IRunAsBinder runAsBinder = getRunAsBinder();

        if (runAsBinder != null) {
            try {
                runAsBinder.start(context.getApplicationInfo().sourceDir, packageName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<RunAsModel> getRunAsModel() {

        IRunAsBinder runAsBinder = getRunAsBinder();

        if (runAsBinder != null) {
            try {
                List<RunAsProcessModel> runAsProcess = runAsBinder.getRunAsProcess();

                if (runAsProcess != null) {
                    List<RunAsModel> models = new ArrayList<>();
                    for (RunAsProcessModel asProcess : runAsProcess) {
                        RunAsModel runAsModel = new RunAsModel();
                        runAsModel.runAsProcessModel = asProcess;
                        models.add(runAsModel);
                    }
                    return models;
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<RunAsProcessModel> getRunAsProcessModel() {
        IRunAsBinder runAsBinder = getRunAsBinder();

        if (runAsBinder != null) {
            try {
                runAsBinder.getActiveRunAsProcess();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static void stopServer(String packageName) {
        IRunAsBinder runAsBinder = getRunAsBinder();

        if (runAsBinder != null) {
            try {
                runAsBinder.stop(packageName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getInternalAppFileServerUrl() {
        return String.format("http://%s:%s", ClientFileServer.getLocalIpAddress(), String.valueOf(CommonConstant.INTERNAL_APP_FILE_SERVER_PORT));
    }
}
