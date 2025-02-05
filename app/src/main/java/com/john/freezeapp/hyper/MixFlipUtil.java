package com.john.freezeapp.hyper;

import android.content.pm.PackageInfo;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.os.ResultReceiver;
import android.os.ShellCallback;

import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.client.ClientLog;
import com.john.freezeapp.client.ClientRemoteShell;
import com.john.freezeapp.util.FreezeAppManager;
import com.john.freezeapp.util.FreezeUtil;
import com.john.freezeapp.util.ThreadPool;

import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class MixFlipUtil {
    /**
     * adb shell dumpsys window -setForceDisplayCompatMode [packageName] allowstart [blocklist|allowstart|allowlist|restartlist|relaunchlist|relaunch|interceptlist|clear]
     * adb shell dumpsys window -setForceDisplayCompatMode [packageName:packageName:...] [blocklist|allowstart|allowlist|restartlist|relaunchlist|relaunch|interceptlist|clear]
     * @param packageName
     */
    public static void allowStartApps(String packageName) {
        ClientRemoteShell.execCommand(String.format("dumpsys window -setForceDisplayCompatMode %s allowstart", packageName), new ClientRemoteShell.RemoteShellCommandResultCallback() {
            @Override
            public void callback(ClientRemoteShell.RemoteShellCommandResult commandResult) {
                ClientLog.log("allowStartApps - " + commandResult.toString());
            }
        });
    }


    public static void allowStartApps(List<String> packages) {
        ClientRemoteShell.execCommand(String.format("dumpsys window -setForceDisplayCompatMode %s allowstart", FreezeUtil.listToString(packages, ":")), new ClientRemoteShell.RemoteShellCommandResultCallback() {
            @Override
            public void callback(ClientRemoteShell.RemoteShellCommandResult commandResult) {
                ClientLog.log("allowStartApps - " + commandResult.toString());
            }
        });
    }

    /**
     * adb shell cmd MiuiSizeCompat update-rule [packageName] enable::true
     * adb shell cmd MiuiSizeCompat update-rule [packageName] scale::0.5
     *
     * @return
     */
    public static void configAppScale(String packageName, String scale) {
        ClientRemoteShell.execCommand(String.format("cmd MiuiSizeCompat update-rule %s enable::true", packageName), new ClientRemoteShell.RemoteShellCommandResultCallback() {
            @Override
            public void callback(ClientRemoteShell.RemoteShellCommandResult commandResult) {

            }
        });

        ClientRemoteShell.execCommand(String.format("cmd MiuiSizeCompat update-rule %s scale::%s", packageName, scale), new ClientRemoteShell.RemoteShellCommandResultCallback() {
            @Override
            public void callback(ClientRemoteShell.RemoteShellCommandResult commandResult) {

            }
        });
    }

    /**
     * adb shell cmd MiuiSizeCompat reload-rule
     */
    public static void resetConfigAppScale() {
        ClientRemoteShell.execCommand(String.format("cmd MiuiSizeCompat reload-rule"), new ClientRemoteShell.RemoteShellCommandResultCallback() {
            @Override
            public void callback(ClientRemoteShell.RemoteShellCommandResult commandResult) {

            }
        });
    }

}
