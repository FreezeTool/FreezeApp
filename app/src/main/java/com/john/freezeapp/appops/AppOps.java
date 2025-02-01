package com.john.freezeapp.appops;

import android.app.AppOpsManagerHidden;

import com.android.internal.app.IAppOpsService;
import com.john.freezeapp.R;
import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.util.FreezeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.AppOpsManager;
import android.content.Context;
import android.os.RemoteException;

public class AppOps {

    private static Map<String, Integer> sOpName = new HashMap<>();


    static {
        sOpName.put("COARSE_LOCATION", R.string.op_name_COARSE_LOCATION);
        sOpName.put("FINE_LOCATION", R.string.op_name_FINE_LOCATION);
        sOpName.put("GPS", R.string.op_name_GPS);
        sOpName.put("VIBRATE", R.string.op_name_VIBRATE);
        sOpName.put("READ_CONTACTS", R.string.op_name_READ_CONTACTS);
        sOpName.put("WRITE_CONTACTS", R.string.op_name_WRITE_CONTACTS);
        sOpName.put("READ_CALL_LOG", R.string.op_name_READ_CALL_LOG);
        sOpName.put("WRITE_CALL_LOG", R.string.op_name_WRITE_CALL_LOG);
        sOpName.put("READ_CALENDAR", R.string.op_name_READ_CALENDAR);
        sOpName.put("WRITE_CALENDAR", R.string.op_name_WRITE_CALENDAR);
        sOpName.put("WIFI_SCAN", R.string.op_name_WIFI_SCAN);
        sOpName.put("POST_NOTIFICATION", R.string.op_name_POST_NOTIFICATION);
        sOpName.put("NEIGHBORING_CELLS", R.string.op_name_NEIGHBORING_CELLS);
        sOpName.put("CALL_PHONE", R.string.op_name_CALL_PHONE);
        sOpName.put("READ_SMS", R.string.op_name_READ_SMS);
        sOpName.put("WRITE_SMS", R.string.op_name_WRITE_SMS);
        sOpName.put("RECEIVE_SMS", R.string.op_name_RECEIVE_SMS);
        sOpName.put("RECEIVE_EMERGECY_SMS", R.string.op_name_RECEIVE_EMERGECY_SMS);
        sOpName.put("RECEIVE_MMS", R.string.op_name_RECEIVE_MMS);
        sOpName.put("RECEIVE_WAP_PUSH", R.string.op_name_RECEIVE_WAP_PUSH);
        sOpName.put("SEND_SMS", R.string.op_name_SEND_SMS);
        sOpName.put("READ_ICC_SMS", R.string.op_name_READ_ICC_SMS);
        sOpName.put("WRITE_ICC_SMS", R.string.op_name_WRITE_ICC_SMS);
        sOpName.put("WRITE_SETTINGS", R.string.op_name_WRITE_SETTINGS);
        sOpName.put("SYSTEM_ALERT_WINDOW", R.string.op_name_SYSTEM_ALERT_WINDOW);
        sOpName.put("ACCESS_NOTIFICATIONS", R.string.op_name_ACCESS_NOTIFICATIONS);
        sOpName.put("CAMERA", R.string.op_name_CAMERA);
        sOpName.put("RECORD_AUDIO", R.string.op_name_RECORD_AUDIO);
        sOpName.put("PLAY_AUDIO", R.string.op_name_PLAY_AUDIO);
        sOpName.put("READ_CLIPBOARD", R.string.op_name_READ_CLIPBOARD);
        sOpName.put("WRITE_CLIPBOARD", R.string.op_name_WRITE_CLIPBOARD);
        sOpName.put("TAKE_MEDIA_BUTTONS", R.string.op_name_TAKE_MEDIA_BUTTONS);
        sOpName.put("TAKE_AUDIO_FOCUS", R.string.op_name_TAKE_AUDIO_FOCUS);
        sOpName.put("AUDIO_MASTER_VOLUME", R.string.op_name_AUDIO_MASTER_VOLUME);
        sOpName.put("AUDIO_VOICE_VOLUME", R.string.op_name_AUDIO_VOICE_VOLUME);
        sOpName.put("AUDIO_RING_VOLUME", R.string.op_name_AUDIO_RING_VOLUME);
        sOpName.put("AUDIO_MEDIA_VOLUME", R.string.op_name_AUDIO_MEDIA_VOLUME);
        sOpName.put("AUDIO_ALARM_VOLUME", R.string.op_name_AUDIO_ALARM_VOLUME);
        sOpName.put("AUDIO_NOTIFICATION_VOLUME", R.string.op_name_AUDIO_NOTIFICATION_VOLUME);
        sOpName.put("AUDIO_BLUETOOTH_VOLUME", R.string.op_name_AUDIO_BLUETOOTH_VOLUME);
        sOpName.put("WAKE_LOCK", R.string.op_name_WAKE_LOCK);
        sOpName.put("MONITOR_LOCATION", R.string.op_name_MONITOR_LOCATION);
        sOpName.put("MONITOR_HIGH_POWER_LOCATION", R.string.op_name_MONITOR_HIGH_POWER_LOCATION);
        sOpName.put("GET_USAGE_STATS", R.string.op_name_GET_USAGE_STATS);
        sOpName.put("MUTE_MICROPHONE", R.string.op_name_MUTE_MICROPHONE);
        sOpName.put("TOAST_WINDOW", R.string.op_name_TOAST_WINDOW);
        sOpName.put("PROJECT_MEDIA", R.string.op_name_PROJECT_MEDIA);
        sOpName.put("ACTIVATE_VPN", R.string.op_name_ACTIVATE_VPN);
        sOpName.put("WRITE_WALLPAPER", R.string.op_name_WRITE_WALLPAPER);
        sOpName.put("ASSIST_STRUCTURE", R.string.op_name_ASSIST_STRUCTURE);
        sOpName.put("ASSIST_SCREENSHOT", R.string.op_name_ASSIST_SCREENSHOT);
        sOpName.put("READ_PHONE_STATE", R.string.op_name_READ_PHONE_STATE);
        sOpName.put("ADD_VOICEMAIL", R.string.op_name_ADD_VOICEMAIL);
        sOpName.put("USE_SIP", R.string.op_name_USE_SIP);
        sOpName.put("PROCESS_OUTGOING_CALLS", R.string.op_name_PROCESS_OUTGOING_CALLS);
        sOpName.put("USE_FINGERPRINT", R.string.op_name_USE_FINGERPRINT);
        sOpName.put("BODY_SENSORS", R.string.op_name_BODY_SENSORS);
        sOpName.put("READ_CELL_BROADCASTS", R.string.op_name_READ_CELL_BROADCASTS);
        sOpName.put("MOCK_LOCATION", R.string.op_name_MOCK_LOCATION);
        sOpName.put("READ_EXTERNAL_STORAGE", R.string.op_name_READ_EXTERNAL_STORAGE);
        sOpName.put("WRITE_EXTERNAL_STORAGE", R.string.op_name_WRITE_EXTERNAL_STORAGE);
        sOpName.put("TURN_ON_SCREEN", R.string.op_name_TURN_ON_SCREEN);
        sOpName.put("GET_ACCOUNTS", R.string.op_name_GET_ACCOUNTS);
        sOpName.put("RUN_IN_BACKGROUND", R.string.op_name_RUN_IN_BACKGROUND);
        sOpName.put("AUDIO_ACCESSIBILITY_VOLUME", R.string.op_name_AUDIO_ACCESSIBILITY_VOLUME);
        sOpName.put("READ_PHONE_NUMBERS", R.string.op_name_READ_PHONE_NUMBERS);
        sOpName.put("REQUEST_INSTALL_PACKAGES", R.string.op_name_REQUEST_INSTALL_PACKAGES);
        sOpName.put("PICTURE_IN_PICTURE", R.string.op_name_PICTURE_IN_PICTURE);
        sOpName.put("INSTANT_APP_START_FOREGROUND", R.string.op_name_INSTANT_APP_START_FOREGROUND);
        sOpName.put("ANSWER_PHONE_CALLS", R.string.op_name_ANSWER_PHONE_CALLS);
        sOpName.put("RUN_ANY_IN_BACKGROUND", R.string.op_name_RUN_ANY_IN_BACKGROUND);
        sOpName.put("CHANGE_WIFI_STATE", R.string.op_name_CHANGE_WIFI_STATE);
        sOpName.put("REQUEST_DELETE_PACKAGES", R.string.op_name_REQUEST_DELETE_PACKAGES);
        sOpName.put("BIND_ACCESSIBILITY_SERVICE", R.string.op_name_BIND_ACCESSIBILITY_SERVICE);
        sOpName.put("ACCEPT_HANDOVER", R.string.op_name_ACCEPT_HANDOVER);
        sOpName.put("MANAGE_IPSEC_TUNNELS", R.string.op_name_MANAGE_IPSEC_TUNNELS);
        sOpName.put("START_FOREGROUND", R.string.op_name_START_FOREGROUND);
        sOpName.put("BLUETOOTH_SCAN", R.string.op_name_BLUETOOTH_SCAN);
        sOpName.put("USE_BIOMETRIC", R.string.op_name_USE_BIOMETRIC);
        sOpName.put("ACTIVITY_RECOGNITION", R.string.op_name_ACTIVITY_RECOGNITION);
        sOpName.put("SMS_FINANCIAL_TRANSACTIONS", R.string.op_name_SMS_FINANCIAL_TRANSACTIONS);
        sOpName.put("READ_MEDIA_AUDIO", R.string.op_name_READ_MEDIA_AUDIO);
        sOpName.put("WRITE_MEDIA_AUDIO", R.string.op_name_WRITE_MEDIA_AUDIO);
        sOpName.put("READ_MEDIA_VIDEO", R.string.op_name_READ_MEDIA_VIDEO);
        sOpName.put("WRITE_MEDIA_VIDEO", R.string.op_name_WRITE_MEDIA_VIDEO);
        sOpName.put("READ_MEDIA_IMAGES", R.string.op_name_READ_MEDIA_IMAGES);
        sOpName.put("WRITE_MEDIA_IMAGES", R.string.op_name_WRITE_MEDIA_IMAGES);
        sOpName.put("ACCESS_ACCESSIBILITY", R.string.op_name_ACCESS_ACCESSIBILITY);
        sOpName.put("READ_DEVICE_IDENTIFIERS", R.string.op_name_READ_DEVICE_IDENTIFIERS);
        sOpName.put("ACCESS_MEDIA_LOCATION", R.string.op_name_ACCESS_MEDIA_LOCATION);
        sOpName.put("QUERY_ALL_PACKAGES", R.string.op_name_QUERY_ALL_PACKAGES);
        sOpName.put("MANAGE_EXTERNAL_STORAGE", R.string.op_name_MANAGE_EXTERNAL_STORAGE);
        sOpName.put("INTERACT_ACROSS_PROFILES", R.string.op_name_INTERACT_ACROSS_PROFILES);
        sOpName.put("ACTIVATE_PLATFORM_VPN", R.string.op_name_ACTIVATE_PLATFORM_VPN);
        sOpName.put("LOADER_USAGE_STATS", R.string.op_name_LOADER_USAGE_STATS);
        sOpName.put("AUTO_REVOKE_PERMISSIONS_IF_UNUSED", R.string.op_name_AUTO_REVOKE_PERMISSIONS_IF_UNUSED);
        sOpName.put("AUTO_REVOKE_MANAGED_BY_INSTALLER", R.string.op_name_AUTO_REVOKE_MANAGED_BY_INSTALLER);
    }

    public static String getOpNameStr(Context context, String opStr) {
        if (sOpName.containsKey(opStr)) {
            return context.getString(sOpName.get(opStr));
        }
        return opStr;
    }

    public static class AppOpInfo {
        public int code;
        public String name;
        public String permission;
        public int opSwitchCode;

        public AppOpInfo(int code, String name, String permission, int opSwitchCode) {
            this.code = code;
            this.name = name;
            this.permission = permission;
            this.opSwitchCode = opSwitchCode;
        }
    }

    public static class AppOpsDetail extends AppOpInfo {
        public int mode = MODE_UNKNOWN;
        public int defaultMode = MODE_UNKNOWN;
        public String packageName;
        public long lastAccessTime;
        public long lastRejectTime;
        public boolean isRunning;
        public long duration;

        public AppOpsDetail(AppOpInfo appOpInfo) {
            super(appOpInfo.code, appOpInfo.name, appOpInfo.permission, appOpInfo.opSwitchCode);
        }
    }

    private static final Map<Integer, AppOpInfo> appOpInfoMap = new LinkedHashMap<>();
    private static final List<AppOpInfo> appOpInfos = new ArrayList<>();

    public synchronized static Map<Integer, AppOpInfo> getAppOpInfoMap() {
        initAppInfo();
        return appOpInfoMap;
    }

    public synchronized static List<AppOpInfo> getAppOpInfoList() {
        initAppInfo();
        return appOpInfos;
    }

    public static final int MODE_UNKNOWN = -1;
    public static final int MODE_ALLOWED = AppOpsManager.MODE_ALLOWED;
    public static final int MODE_IGNORED = AppOpsManager.MODE_IGNORED;
    public static final int MODE_ERRORED = AppOpsManager.MODE_ERRORED;
    public static final int MODE_DEFAULT = AppOpsManager.MODE_DEFAULT;
    public static final int MODE_FOREGROUND = AppOpsManager.MODE_FOREGROUND;
    public static final int MODE_ASK = 5;

    public static String getModelStr(int mode) {
        switch (mode) {
            case MODE_ALLOWED:
                return "允许";
            case MODE_IGNORED:
                return "忽略";
            case MODE_ERRORED:
                return "拒绝";
            case MODE_DEFAULT:
                return "默认";
            case MODE_FOREGROUND:
                return "仅在前台使用时允许";
            case MODE_ASK:
                return "ask";
            default:
                return "unknown";
        }
    }

    private static void initAppInfo() {
        if (appOpInfoMap.isEmpty()) {
            // aosp
            for (int i = 0; i < 200; i++) {
                insertAOSPCode(i);
            }
//            insertMIUICode();
        }

    }

    private static void insertAOSPCode(int i) {
        String name = AppOpsManagerHidden.opToName(i);
        if (name == null) {
            return;
        }
        if (name.contains("NONE") || name.contains("Unknown")) {
            return;
        }

        AppOpInfo appOpInfo = new AppOpInfo(i, name, AppOpsManagerHidden.opToPermission(i), AppOpsManagerHidden.opToSwitch(i));
        appOpInfoMap.put(i, appOpInfo);
        appOpInfos.add(appOpInfo);
    }


    public static String getOpName(int op) {
        AppOpInfo appOpInfo = appOpInfoMap.get(op);
        if (appOpInfo != null) {
            return appOpInfo.name;
        }
        return null;
    }

    public static List<AppOpsDetail> getAppOpDetail(String packageName) {
        IAppOpsService appOpsService = ClientBinderManager.getAppOpsService();
        if (appOpsService != null) {
            List<AppOpInfo> appOpInfoList = getAppOpInfoList();
            int[] ops = new int[appOpInfoList.size()];
            for (int i = 0; i < appOpInfoList.size(); i++) {
                ops[i] = appOpInfoList.get(i).code;
            }
            try {
                int packageUid = getPackageUid(packageName);


                Map<Integer, AppOpsDetail> appOpsDetailMap = new LinkedHashMap<>();


                // 默认的操作信息
                List<AppOpsManagerHidden.PackageOps> opsForPackage = appOpsService.getOpsForPackage(packageUid, packageName, null);


                for (AppOpsManagerHidden.PackageOps appOpsList : opsForPackage) {
                    List<AppOpsManagerHidden.OpEntry> opEntries = appOpsList.getOps();
                    if (opEntries != null && !opEntries.isEmpty()) {
                        for (AppOpsManagerHidden.OpEntry opEntry : opEntries) {
                            int op = opEntry.getOp();
                            int mode = opEntry.getMode();
                            AppOpInfo appOpInfo = getAppOpInfoMap().get(op);
                            if (appOpInfo == null) {
                                continue;
                            }
                            AppOpsDetail appOpsDetail = new AppOpsDetail(appOpInfo);
                            appOpsDetail.defaultMode = mode;
                            appOpsDetail.packageName = packageName;
                            long lastAccessTime = opEntry.getTime();
                            if (lastAccessTime != -1) {
                                appOpsDetail.lastAccessTime = lastAccessTime;
                            }
                            long lastRejectTime = opEntry.getRejectTime();
                            if (lastRejectTime != -1) {
                                appOpsDetail.lastRejectTime = lastRejectTime;
                            }

                            if (opEntry.isRunning()) {
                                appOpsDetail.isRunning = opEntry.isRunning();
                            }
                            appOpsDetailMap.put(op, appOpsDetail);
                        }
                    }
                }


                // 获取用户修改过的权限
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    opsForPackage = appOpsService.getUidOps(packageUid, null);
                    for (AppOpsManagerHidden.PackageOps appOpsList : opsForPackage) {
                        List<AppOpsManagerHidden.OpEntry> opEntries = appOpsList.getOps();
                        if (opEntries != null && !opEntries.isEmpty()) {
                            for (AppOpsManagerHidden.OpEntry opEntry : opEntries) {
                                int op = opEntry.getOp();
                                int mode = opEntry.getMode();
                                AppOpInfo appOpInfo = getAppOpInfoMap().get(op);
                                if (appOpInfo == null) {
                                    continue;
                                }


                                AppOpsDetail appOpsDetail = appOpsDetailMap.get(op);
                                if (appOpsDetail == null) {
                                    continue;
                                }
                                appOpsDetail.mode = mode;
                            }
                        }
                    }
                }
                List<AppOpsDetail> appOpsDetails = new ArrayList<>();
                for (Map.Entry<Integer, AppOpsDetail> entry : appOpsDetailMap.entrySet()) {
                    appOpsDetails.add(entry.getValue());
                }
                return appOpsDetails;

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static boolean setUidMode(int op, int mode, String packageName) {
        try {
            IAppOpsService appOpsService = ClientBinderManager.getAppOpsService();
            if (appOpsService != null) {
                int packageUid = getPackageUid(packageName);
                appOpsService.setUidMode(op, packageUid, mode);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int checkOperation(int op, String packageName) {
        try {
            IAppOpsService appOpsService = ClientBinderManager.getAppOpsService();
            if (appOpsService != null) {
                int packageUid = getPackageUid(packageName);
                return appOpsService.checkOperation(op, packageUid, packageName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getPackageUid(String packageName) throws RemoteException {
        int packageUid = 0;
        if (FreezeUtil.atLeast33()) {
            packageUid = ClientBinderManager.getPackageManager().getPackageUid(packageName, 0L, 0);
        } else if (FreezeUtil.atLeast24()) {
            packageUid = ClientBinderManager.getPackageManager().getPackageUid(packageName, 0, 0);
        } else {
            packageUid = ClientBinderManager.getPackageManager().getPackageUid(packageName, 0);
        }
        return packageUid;
    }


}
