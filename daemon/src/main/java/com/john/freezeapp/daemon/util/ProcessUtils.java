package com.john.freezeapp.daemon.util;

import androidx.annotation.Nullable;

import com.john.freezeapp.daemon.DaemonHelper;
import com.john.freezeapp.daemon.runas.RunAsProcessModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ProcessUtils {

    public static List<Integer> getDaemonAppProcess() {
        List<Integer> processes = new ArrayList<>();
        File[] pidDirs = getPidDir();
        if (pidDirs != null) {
            for (File pidDir : pidDirs) {
                String cmdline = getProcessCmdline(pidDir.getName());
                if (cmdline != null && cmdline.contains(DaemonHelper.DAEMON_NICKNAME)) {
                    try {
                        processes.add(Integer.parseInt(pidDir.getName()));
                    } catch (Exception e) {
                        //
                    }
                }
            }
        }
        return processes;
    }


    public static List<RunAsProcessModel> getRunAsAppProcess() {
        List<RunAsProcessModel> processes = new ArrayList<>();
        File[] pidDirs = getPidDir();
        if (pidDirs != null) {
            for (File pidDir : pidDirs) {
                String pid = pidDir.getName();
                String cmdline = getProcessCmdline(pid);
                if (cmdline != null && cmdline.contains(DaemonHelper.DAEMON_RUNAS_PREFIX)) {
                    String packageName = cmdline.replace(DaemonHelper.DAEMON_RUNAS_PREFIX, "").trim();
                    try {
                        RunAsProcessModel runAsProcessModel = new RunAsProcessModel();
                        runAsProcessModel.pid = Integer.parseInt(pid);
                        runAsProcessModel.packageName = packageName;
                        runAsProcessModel.active = true;
                        processes.add(runAsProcessModel);
                    } catch (Exception e) {
                        //
                    }
                }
            }
        }
        return processes;
    }

    private static File[] getPidDir() {
        File procDir = new File("/proc");
        return procDir.listFiles(file -> file.isDirectory() && file.getName().matches("\\d+"));
    }


    private static String getProcessCmdline(String pid) {
        File cmdlineFile = new File("/proc/" + pid + "/cmdline");
        try (BufferedReader reader = new BufferedReader(new FileReader(cmdlineFile))) {
            String cmdline = reader.readLine();
            return cmdline != null ? cmdline.replace('\0', ' ') : null;
        } catch (Exception e) {
            return null;
        }
    }

}