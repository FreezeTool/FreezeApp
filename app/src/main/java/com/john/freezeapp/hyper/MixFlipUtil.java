package com.john.freezeapp.hyper;

import android.content.pm.PackageInfo;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.os.ResultReceiver;
import android.os.ShellCallback;

import com.john.freezeapp.util.FreezeAppManager;
import com.john.freezeapp.util.FreezeUtil;
import com.john.freezeapp.util.ThreadPool;
import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.client.ClientLog;
import com.john.freezeapp.client.ClientRemoteShell;

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


    public static boolean allowStartApps2() {

        List<PackageInfo> packageInfos = FreezeAppManager.getInstallApp(FreezeAppManager.TYPE_NORMAL_APP, FreezeAppManager.STATUS_ENABLE_APP);
        if (packageInfos == null || packageInfos.isEmpty()) {
            return false;
        }

        /**
         *
         *
         *  adb shell dumpsys window -getForceDisplayCompatMode
         * com.miui.fliphome
         *
         * -setForceDisplayCompatMode [packageName] [blocklist|allowstart|allowlist|restartlist|relaunchlist|relaunch|interceptlist|clear]
         * -setForceDisplayCompatMode [packageName:packageName:...] [blocklist|allowstart|allowlist|restartlist|relaunchlist|relaunch|interceptlist|clear]
         */

        List<String> packages = packageInfos.stream().map(packageInfo -> packageInfo.packageName).collect(Collectors.toList());
        String[] args = new String[]{"-setForceDisplayCompatMode", FreezeUtil.listToString(packages, ":"), "allowstart"};

        try {
            ParcelFileDescriptor[] pipe = ParcelFileDescriptor.createPipe();
            ThreadPool.execute(() -> {
                ParcelFileDescriptor.AutoCloseInputStream inputStream = new ParcelFileDescriptor.AutoCloseInputStream(pipe[0]);
                byte[] bytes = new byte[1024];
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    int index = -1;

                    while (true) {
                        index = inputStream.read(bytes);
                        stringBuilder.append(new String(bytes, 0, index, StandardCharsets.UTF_8));
                        if (index == -1 || index < 1024) {
                            break;
                        }
                    }
                    ClientLog.log("allowStartApps - " + stringBuilder.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            ClientBinderManager.dump("window", pipe[1].getFileDescriptor(), args);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * adb shell cmd MiuiSizeCompat update-rule  com.john.freezeapp enable::true
     * adb shell cmd MiuiSizeCompat update-rule  com.john.freezeapp scale::0.5
     * adb shell cmd MiuiSizeCompat dump-rule com.john.freezeapp
     * adb shell cmd MiuiSizeCompat reload-rule
     *
     * @return
     */

    public static boolean configAppScale2() {

        ParcelFileDescriptor[] pipe = null;
        try {
            pipe = ParcelFileDescriptor.createPipe();
            FileDescriptor pipeIn = pipe[0].getFileDescriptor();
            FileDescriptor pipeOut = pipe[1].getFileDescriptor();

            ShellCallback shellCallback = new ShellCallback();
            ResultReceiver resultReceiver = new ResultReceiver((Handler) null);

            ParcelFileDescriptor[] finalPipe = pipe;
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    ParcelFileDescriptor.AutoCloseInputStream inputStream = new ParcelFileDescriptor.AutoCloseInputStream(finalPipe[0]);
                    byte[] bytes = new byte[1024];
                    StringBuilder stringBuilder = new StringBuilder();
                    int index = -1;
                    try {
                        while (true) {
                            index = inputStream.read(bytes);
                            stringBuilder.append(new String(bytes, 0, index, StandardCharsets.UTF_8));
                            Thread.sleep(500);
                            ClientLog.log("configAppScale - index-" + index);
                            if (index == -1) {
                                break;
                            }
                        }
                        ClientLog.log("configAppScale - while false");
                    } catch (Exception e) {
                        e.printStackTrace();
                        ClientLog.log("configAppScale - error");
                    } finally {
                        ClientLog.log("configAppScale - final index=" + index);
                    }
                }
            });

            String[] args = new String[]{"list", "packages", "-3"};

            ClientBinderManager.shellCommand("package", pipeIn, pipeOut, pipeOut, args, shellCallback, resultReceiver);
            executorService.shutdown();
            ClientLog.log("configAppScale - done count = " + Thread.activeCount());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

}
