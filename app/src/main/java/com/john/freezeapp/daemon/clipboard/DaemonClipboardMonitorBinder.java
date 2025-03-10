package com.john.freezeapp.daemon.clipboard;

import android.app.AppOpsManager;
import android.app.AppOpsManagerHidden;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.IClipboard;
import android.content.IOnPrimaryClipChangedListener;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.RemoteException;
import android.text.TextUtils;

import com.android.internal.app.IAppOpsService;
import com.john.freezeapp.daemon.Daemon;
import com.john.freezeapp.daemon.DaemonHelper;
import com.john.freezeapp.daemon.DaemonLog;
import com.john.freezeapp.daemon.DaemonService;
import com.john.freezeapp.daemon.util.DaemonSharedPrefUtils;
import com.john.freezeapp.daemon.util.DaemonUtil;
import com.john.freezeapp.daemon.util.SharedPreferencesImpl;
import com.john.freezeapp.util.FreezeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DaemonClipboardMonitorBinder extends IDaemonClipboardMonitorBinder.Stub {

    Map<String, ClipboardData> mClipboardDataMap = new LinkedHashMap<>();

    ExecutorService mExecutor = Executors.newSingleThreadExecutor();

    IOnPrimaryClipChangedListener.Stub iOnPrimaryClipChangedListener = new IOnPrimaryClipChangedListener.Stub() {
        @Override
        public void dispatchPrimaryClipChanged() {
            mExecutor.execute(() -> updateClipData());
        }
    };

    SharedPreferences sharedPreferences;

    final List<IDaemonClipboardChange> iDaemonClipboardChanges = new ArrayList<>();

    public DaemonClipboardMonitorBinder() {
        boolean isOpen = DaemonSharedPrefUtils.getBoolean(DaemonHelper.SP_KEY_CLIPBOARD_SWITCHER, false);
        DaemonLog.log("DaemonClipboardMonitorBinder isOpen = " + isOpen);
        if (isOpen) {
            try {
                init();
                startMonitor();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        init();
        return super.onTransact(code, data, reply, flags);
    }

    public boolean isInit = false;

    private void init() {
        if (isInit) {
            return;
        }
        isInit = true;
        File file = new File(DaemonHelper.DAEMON_CLIPBOARD_PATH);
        sharedPreferences = new SharedPreferencesImpl(file);

        Map<String, ?> all = sharedPreferences.getAll();
        if (all != null) {
            List<ClipboardData> clipboardDataList = new ArrayList<>();
            for (Map.Entry<String, ?> entry : all.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof String && !TextUtils.isEmpty((String) value)) {
                    try {
                        ClipboardData clipboardData = DaemonUtil.getGson().fromJson((String) value, ClipboardData.class);
                        clipboardDataList.add(clipboardData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            Collections.sort(clipboardDataList);

            for (ClipboardData clipboardData : clipboardDataList) {
                mClipboardDataMap.put(clipboardData.id, clipboardData);
            }
        }
    }

    public static int checkOperation(int op,String packageName) {
        try {
            IAppOpsService appOpsService = DaemonService.getAppOps();
            if (appOpsService != null) {
                return appOpsService.checkOperation(op, android.os.Process.myUid() , packageName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    public static int setOperation(int op) {
        try {
            IAppOpsService appOpsService = DaemonService.getAppOps();
            if (appOpsService != null) {
                appOpsService.setUidMode(op, android.os.Process.myUid(), AppOpsManager.MODE_ALLOWED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    private synchronized void updateClipData() {
        try {
            IClipboard clipboard = getClipboard();
            String daemonPackageName = DaemonUtil.getDaemonPackageName();
            DaemonLog.log("DaemonClipboardMonitorBinder updateClipData 1 - checkOperation - " + checkOperation(AppOpsManagerHidden.OP_READ_CLIPBOARD, daemonPackageName));
            DaemonLog.log("DaemonClipboardMonitorBinder updateClipData 1 - package - " + daemonPackageName);
            setOperation(AppOpsManagerHidden.OP_READ_CLIPBOARD);
            if (clipboard != null) {
                ClipData primaryClipData;
                String primaryClipSource;
                if (FreezeUtil.atLeast34()) {
                    DaemonLog.log("DaemonClipboardMonitorBinder updateClipData 2");
                    if (!clipboard.hasPrimaryClip(daemonPackageName, null, 0, 0)) {
                        return;
                    }

                    primaryClipSource = clipboard.getPrimaryClipSource(daemonPackageName, null, 0, 0);
                    primaryClipData = clipboard.getPrimaryClip(daemonPackageName, null, 0, 0);
                    DaemonLog.log("DaemonClipboardMonitorBinder updateClipData 3");
                } else {
                    DaemonLog.log("DaemonClipboardMonitorBinder updateClipData 4");
                    if (!clipboard.hasPrimaryClip(daemonPackageName, null, 0)) {
                        return;
                    }
                    DaemonLog.log("DaemonClipboardMonitorBinder updateClipData 5");
                    primaryClipSource = clipboard.getPrimaryClipSource(daemonPackageName, null, 0);
                    primaryClipData = clipboard.getPrimaryClip(daemonPackageName, null, 0);
                }

                DaemonLog.log("DaemonClipboardMonitorBinder updateClipData primaryClipSource=" + primaryClipSource);
                DaemonLog.log("DaemonClipboardMonitorBinder updateClipData primaryClipData=" + primaryClipData);

                if (primaryClipData != null) {
                    DaemonLog.log("DaemonClipboardMonitorBinder updateClipData 6");
                    ClipDescription description = primaryClipData.getDescription();
                    if (description != null) {
                        if (TextUtils.equals(description.getLabel(), TAG)) {
                            return;
                        }
                    }
                    DaemonLog.log("DaemonClipboardMonitorBinder updateClipData 7");
                    if (primaryClipData.getItemCount() > 0) {
                        CharSequence text = primaryClipData.getItemAt(0).coerceToText(Daemon.getDaemon().mActivityThread.getApplication());
                        DaemonLog.log(String.format("dispatchPrimaryClipChanged text=%s", text));
                        if (text != null) {
                            String id = DaemonUtil.md5(text.toString());

                            if (!TextUtils.isEmpty(id)) {
                                ClipboardData clipboardData = mClipboardDataMap.get(id);
                                if (clipboardData != null) {
                                    clipboardData.timestamp = System.currentTimeMillis();
                                } else {
                                    clipboardData = new ClipboardData();
                                    clipboardData.id = id;
                                    clipboardData.packageName = primaryClipSource;
                                    clipboardData.content = text.toString();
                                    clipboardData.timestamp = System.currentTimeMillis();
                                }
                                put(id, clipboardData);
                                notifyDaemonClipboardDataChange();
//                            DaemonLog.log(String.format("dispatchPrimaryClipChanged add %s", clipboardData));
                            }

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            DaemonLog.e(e, "updateClipData");
        }
    }

    private void notifyDaemonClipboardDataChange() {
        synchronized (iDaemonClipboardChanges) {
            for (IDaemonClipboardChange iDaemonClipboardChange : iDaemonClipboardChanges) {
                try {
                    iDaemonClipboardChange.change();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public synchronized void put(String id, ClipboardData clipboardData) {
        mClipboardDataMap.put(id, clipboardData);
        try {
            sharedPreferences.edit().putString(id, DaemonUtil.getGson().toJson(clipboardData)).commit();
        } catch (Throwable e) {
            DaemonLog.e(e, "put");
        }
    }

    private synchronized void remove(String id) {
        mClipboardDataMap.remove(id);
        sharedPreferences.edit().putString(id, "").commit();
    }

    private synchronized void clear() {
        mClipboardDataMap.clear();
        sharedPreferences.edit().clear().commit();
    }


    private static final String TAG = "FreezeApp_";

    IClipboard iClipboard;


    public IClipboard getClipboard() {
        if (iClipboard == null) {
            iClipboard = DaemonService.getClipboard();
        }
        return iClipboard;
    }

    boolean isActive = false;

    @Override
    public void startMonitor() throws RemoteException {
        DaemonLog.log("startMonitor");
        if (isActive) {
            return;
        }
        DaemonLog.log("startMonitor start");
        try {
            IClipboard clipboard = getClipboard();
            if (clipboard != null) {
                if (FreezeUtil.atLeast34()) {
                    clipboard.addPrimaryClipChangedListener(iOnPrimaryClipChangedListener, DaemonUtil.getDaemonPackageName(), null, 0, 0);
                } else {
                    clipboard.addPrimaryClipChangedListener(iOnPrimaryClipChangedListener, DaemonUtil.getDaemonPackageName(), null, 0);
                }
            }
            isActive = true;
            DaemonSharedPrefUtils.setBoolean(DaemonHelper.SP_KEY_CLIPBOARD_SWITCHER, true);
        } catch (Throwable e) {
            DaemonLog.e(e, "startMonitor");
        }
    }

    @Override
    public void endMonitor() throws RemoteException {
        DaemonLog.log("endMonitor");
        if (!isActive) {
            return;
        }
        DaemonLog.log("endMonitor start");
        try {
            IClipboard clipboard = getClipboard();
            if (clipboard != null) {
                if (FreezeUtil.atLeast34()) {
                    clipboard.removePrimaryClipChangedListener(iOnPrimaryClipChangedListener, DaemonUtil.getDaemonPackageName(), null, 0, 0);
                } else {
                    clipboard.removePrimaryClipChangedListener(iOnPrimaryClipChangedListener, DaemonUtil.getDaemonPackageName(), null, 0);
                }
            }
            isActive = false;
            DaemonSharedPrefUtils.setBoolean(DaemonHelper.SP_KEY_CLIPBOARD_SWITCHER, false);
        } catch (Throwable e) {
            DaemonLog.e(e, "endMonitor");
        }
    }

    @Override
    public List<ClipboardData> getClipboardData() throws RemoteException {
        return new ArrayList<>(mClipboardDataMap.values());
    }

    @Override
    public void removeClipboardData(String id) throws RemoteException {
        mExecutor.execute(() -> remove(id));
    }

    @Override
    public void clearClipboardData() throws RemoteException {
        mExecutor.execute(this::clear);
    }

    @Override
    public boolean setClipboardData(String id) throws RemoteException {

        ClipboardData clipboardData = mClipboardDataMap.get(id);
        if (clipboardData == null) {
            return false;
        }

        IClipboard clipboard = getClipboard();
        if (clipboard != null) {
            ClipData clipData = ClipData.newPlainText(TAG, clipboardData.content);
            String daemonPackageName = DaemonUtil.getDaemonPackageName();
            if (FreezeUtil.atLeast34()) {
                clipboard.setPrimaryClip(clipData, daemonPackageName, null, 0, 0);
            } else {
                clipboard.setPrimaryClip(clipData, daemonPackageName, null, 0);
            }
        }

        return true;
    }

    @Override
    public boolean isMonitor() throws RemoteException {
        return isActive;
    }

    @Override
    public void addClipboardDataChange(IDaemonClipboardChange iDaemonClipboardChange) throws RemoteException {
        synchronized (iDaemonClipboardChange) {
            iDaemonClipboardChanges.add(iDaemonClipboardChange);
            iDaemonClipboardChange.asBinder().linkToDeath(() -> iDaemonClipboardChanges.remove(iDaemonClipboardChange), 0);
        }
    }
}
