package com.john.freezeapp.util;

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

}
