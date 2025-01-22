package com.android.internal.app;

import android.os.BatteryUsageStats;
import android.os.BatteryUsageStatsQuery;
import android.os.Binder;
import android.os.BluetoothBatteryStats;
import android.os.Build;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.WakeLockStats;
import android.os.WorkSource;
import android.os.connectivity.CellularBatteryStats;
import android.os.connectivity.GpsBatteryStats;
import android.os.connectivity.WifiBatteryStats;
import android.telephony.SignalStrength;
import android.os.health.HealthStatsParceler;

import androidx.annotation.RequiresApi;

import java.util.List;

public interface IBatteryStats {


    abstract class Stub extends Binder implements IBatteryStats {

        public static IBatteryStats asInterface(IBinder obj) {
            throw new RuntimeException("STUB");
        }
    }

    // These first methods are also called by native code, so must
    // be kept sync with frameworks/native/libs/binder/include_batterystats/batterystats/IBatteryStats.h
    void noteStartSensor(int uid, int sensor);
    void noteStopSensor(int uid, int sensor);
    void noteStartVideo(int uid);
    void noteStopVideo(int uid);
    void noteStartAudio(int uid);
    void noteStopAudio(int uid);
    void noteResetVideo();
    void noteResetAudio();
    void noteFlashlightOn(int uid);
    void noteFlashlightOff(int uid);
    void noteStartCamera(int uid);
    void noteStopCamera(int uid);
    void noteResetCamera();
    void noteResetFlashlight();

    @RequiresApi(Build.VERSION_CODES.S)
    List<BatteryUsageStats> getBatteryUsageStats(List<BatteryUsageStatsQuery> queries) throws RemoteException;


    byte[] getStatistics();

    ParcelFileDescriptor getStatisticsStream(boolean updateAll);

    // Return true if we see the battery as currently charging.

    boolean isCharging();

    // Return the computed amount of time remaining on battery, milliseconds.
    // Returns -1 if nothing could be computed.
    long computeBatteryTimeRemaining();

    // Return the computed amount of time remaining to fully charge, milliseconds.
    // Returns -1 if nothing could be computed.

    long computeChargeTimeRemaining();

    void noteEvent(int code, String name, int uid);

    void noteSyncStart(String name, int uid);
    void noteSyncFinish(String name, int uid);
    void noteJobStart(String name, int uid);
    void noteJobFinish(String name, int uid, int stopReason);

    void noteStartWakelock(int uid, int pid, String name, String historyName,
                           int type, boolean unimportantForLogging);
    void noteStopWakelock(int uid, int pid, String name, String historyName, int type);

    void noteStartWakelockFromSource(WorkSource ws, int pid, String name, String historyName,
                                     int type, boolean unimportantForLogging);
    void noteChangeWakelockFromSource(WorkSource ws, int pid, String name, String histyoryName,
                                      int type, WorkSource newWs, int newPid, String newName,
                                      String newHistoryName, int newType, boolean newUnimportantForLogging);
    void noteStopWakelockFromSource(WorkSource ws, int pid, String name, String historyName,
                                    int type);
    void noteLongPartialWakelockStart(String name, String historyName, int uid);
    void noteLongPartialWakelockStartFromSource(String name, String historyName,
                                                WorkSource workSource);
    void noteLongPartialWakelockFinish(String name, String historyName, int uid);
    void noteLongPartialWakelockFinishFromSource(String name, String historyName,
                                                 WorkSource workSource);

    void noteVibratorOn(int uid, long durationMillis);
    void noteVibratorOff(int uid);
    void noteGpsChanged(WorkSource oldSource, WorkSource newSource);
    void noteGpsSignalQuality(int signalLevel);
    void noteScreenState(int state);
    void noteScreenBrightness(int brightness);
    void noteUserActivity(int uid, int event);
    void noteWakeUp(String reason, int reasonUid);
    void noteInteractive(boolean interactive);
    void noteConnectivityChanged(int type, String extra);
    void noteMobileRadioPowerState(int powerState, long timestampNs, int uid);
    void notePhoneOn();
    void notePhoneOff();
    void notePhoneSignalStrength(SignalStrength signalStrength);
    void notePhoneDataConnectionState(int dataType, boolean hasData, int serviceType, int nrFrequency);
    void notePhoneState(int phoneState);
    void noteWifiOn();
    void noteWifiOff();
    void noteWifiRunning(WorkSource ws);
    void noteWifiRunningChanged(WorkSource oldWs, WorkSource newWs);
    void noteWifiStopped(WorkSource ws);
    void noteWifiState(int wifiState, String accessPoint);
    void noteWifiSupplicantStateChanged(int supplState, boolean failedAuth);
    void noteWifiRssiChanged(int newRssi);
    void noteFullWifiLockAcquired(int uid);
    void noteFullWifiLockReleased(int uid);
    void noteWifiScanStarted(int uid);
    void noteWifiScanStopped(int uid);
    void noteWifiMulticastEnabled(int uid);
    void noteWifiMulticastDisabled(int uid);
    void noteFullWifiLockAcquiredFromSource(WorkSource ws);
    void noteFullWifiLockReleasedFromSource(WorkSource ws);
    void noteWifiScanStartedFromSource(WorkSource ws);
    void noteWifiScanStoppedFromSource(WorkSource ws);
    void noteWifiBatchedScanStartedFromSource(WorkSource ws, int csph);
    void noteWifiBatchedScanStoppedFromSource(WorkSource ws);
    void noteWifiRadioPowerState(int powerState, long timestampNs, int uid);
    void noteNetworkInterfaceForTransports(String iface, int[] transportTypes);
    void noteNetworkStatsEnabled();
    void noteDeviceIdleMode(int mode, String activeReason, int activeUid);
    void setBatteryState(int status, int health, int plugType, int level, int temp, int volt,
                         int chargeUAh, int chargeFullUAh, long chargeTimeToFullSeconds);

    long getAwakeTimeBattery();
    long getAwakeTimePlugged();

    void noteBluetoothOn(int uid, int reason, String packageName);
    void noteBluetoothOff(int uid, int reason, String packageName);
    void noteBleScanStarted(WorkSource ws, boolean isUnoptimized);
    void noteBleScanStopped(WorkSource ws, boolean isUnoptimized);
    void noteBleScanReset();
    void noteBleScanResults(WorkSource ws, int numNewResults);

    CellularBatteryStats getCellularBatteryStats();

    WifiBatteryStats getWifiBatteryStats();

    GpsBatteryStats getGpsBatteryStats();

    WakeLockStats getWakeLockStats();

    BluetoothBatteryStats getBluetoothBatteryStats();

    HealthStatsParceler takeUidSnapshot(int uid);
    HealthStatsParceler[] takeUidSnapshots(int[] uid);

    boolean setChargingStateUpdateDelayMillis(int delay);


    void setChargerAcOnline(boolean online, boolean forceUpdate);

    void setBatteryLevel(int level, boolean forceUpdate);

    void unplugBattery(boolean forceUpdate);

    void resetBattery(boolean forceUpdate);

    void suspendBatteryInput();
}
