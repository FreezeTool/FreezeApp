package com.john.freezeapp.util;

import java.text.DecimalFormat;

public class CommonUtil {

    public static String getAppProcessShell(String classPath, String className, String nickName, String executeArgs, boolean enableDebug) {
        String debugArgs = "";
        if (enableDebug) {
            if (DeviceUtil.atLeast30()) {
                debugArgs = "-Xcompiler-option" + " --debuggable" +
                        " -XjdwpProvider:adbconnection" +
                        " -XjdwpOptions:suspend=n,server=y";
            } else if (DeviceUtil.atLeast28()) {
                debugArgs = "-Xcompiler-option" + " --debuggable" +
                        " -XjdwpProvider:internal" +
                        " -XjdwpOptions:transport=dt_android_adb,suspend=n,server=y";
            } else {
                debugArgs = "-Xcompiler-option" + " --debuggable" +
                        " -agentlib:jdwp=transport=dt_android_adb,suspend=n,server=y";
            }
        }
        return String.format("nohup app_process %s -Djava.class.path=%s /system/bin --nice-name=%s %s %s > /dev/null 2>&1 &",
                debugArgs,
                classPath,
                nickName,
                className,
                executeArgs);
    }


    private final static String[] UNITS = new String[]{"B", "KB", "MB", "GB", "TB"};


    public static String getSizeText(long cacheBytes) {
        if (cacheBytes <= 0) return "0B";
        int digitGroups = (int) (Math.log10(cacheBytes) / Math.log10(1024));
        return new DecimalFormat("#,##0.##").format(cacheBytes / Math.pow(1024, digitGroups)) + " " + UNITS[digitGroups];
    }

}
