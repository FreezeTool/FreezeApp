package com.john.freezeapp.battery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.BatteryConsumer;
import android.os.BatteryUsageStats;
import android.os.BatteryUsageStats31;
import android.os.BatteryUsageStatsQuery;
import android.os.Build;
import android.os.UidBatteryConsumer;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Range;

import androidx.annotation.RequiresApi;

import com.android.internal.app.IBatteryStats;
import com.john.freezeapp.BuildConfig;
import com.john.freezeapp.client.ClientSystemService;
import com.john.freezeapp.util.FreezeUtil;
import com.john.freezeapp.util.ThreadPool;
import com.john.freezeapp.client.ClientBinderManager;
import com.john.freezeapp.client.ClientLog;
import com.john.hidden.api.ReplaceRef;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class BatteryStats {

    public final static void formatTimeMsNoSpace(StringBuilder sb, long time) {
        long sec = time / 1000;
        formatTimeRaw(sb, sec);
        sb.append(time - (sec * 1000));
        sb.append("ms");
    }

    public final static void formatTimeMs(StringBuilder sb, long time) {
        long sec = time / 1000;
        formatTimeRaw(sb, sec);
        sb.append(time - (sec * 1000));
        sb.append("ms ");
    }

    private final static void formatTimeRaw(StringBuilder out, long seconds) {
        long days = seconds / (60 * 60 * 24);
        if (days != 0) {
            out.append(days);
            out.append("d ");
        }
        long used = days * 60 * 60 * 24;

        long hours = (seconds - used) / (60 * 60);
        if (hours != 0 || used != 0) {
            out.append(hours);
            out.append("h ");
        }
        used += hours * 60 * 60;

        long mins = (seconds - used) / 60;
        if (mins != 0 || used != 0) {
            out.append(mins);
            out.append("m ");
        }
        used += mins * 60;

        if (seconds != 0 || used != 0) {
            out.append(seconds - used);
            out.append("s ");
        }
    }


    public static String formatCharge(double power) {
        return formatValue(power);
    }

    private static String formatValue(double value) {
        if (value == 0) return "0";

        final String format;
        if (value < .00001) {
            format = "%.8f";
        } else if (value < .0001) {
            format = "%.7f";
        } else if (value < .001) {
            format = "%.6f";
        } else if (value < .01) {
            format = "%.5f";
        } else if (value < .1) {
            format = "%.4f";
        } else if (value < 1) {
            format = "%.3f";
        } else if (value < 10) {
            format = "%.2f";
        } else if (value < 100) {
            format = "%.1f";
        } else {
            format = "%.0f";
        }

        // Use English locale because this is never used in UI (only in checkin and dump).
        return String.format(Locale.ENGLISH, format, value);
    }

    public interface Callback {
        void success(List list);

        void fail();
    }

    @RequiresApi(Build.VERSION_CODES.S)
    public static void requestBatteryUsage(Context context, Callback callback) {

        ThreadPool.execute(new Runnable() {
            @SuppressLint("BlockedPrivateApi")
            @Override
            public void run() {
                IBatteryStats batteryStats = ClientSystemService.getBatteryStats();
                if (batteryStats == null) {
                    callback.fail();
                    return;
                }

//                Calendar calendar = Calendar.getInstance();
//                calendar.set(Calendar.HOUR_OF_DAY, 0);
//                calendar.set(Calendar.MINUTE, 0);
//                calendar.set(Calendar.SECOND, 0);
//                Date time = calendar.getTime();
//
//                long from = time.getTime();
//                long to = from + DateUtils.DAY_IN_MILLIS;
                long to = System.currentTimeMillis() - 1;
                long from = to - DateUtils.DAY_IN_MILLIS;

                BatteryUsageStatsQuery query = new BatteryUsageStatsQuery.Builder()
                        .includeBatteryHistory()
                        .includePowerModels()
                        .includeProcessStateData()
                        .includeVirtualUids()
                        .setMaxStatsAgeMs(0)
//                        .aggregateSnapshots(from, to)
                        .build();
                try {
                    List<BatteryUsageData> list = new ArrayList<>();
                    list.add(new BatteryUsageTitleData("Estimated power use (mAh)"));
                    List<BatteryUsageStats> batteryUsageStatsList = ClientSystemService.getBatteryStats().getBatteryUsageStats(Collections.singletonList(query));
                    if (batteryUsageStatsList != null && !batteryUsageStatsList.isEmpty()) {
                        BatteryUsageStats batteryUsageStats = batteryUsageStatsList.get(0);
                        final Range<Double> dischargedPowerRange = batteryUsageStats.getDischargedPowerRange();
                        ClientLog.log(" --------------global-------------");
                        ClientLog.log("  Estimated power use (mAh):");
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("    Capacity: " + BatteryStats.formatCharge(batteryUsageStats.getBatteryCapacity()));
                        stringBuilder.append(", Computed drain: " + BatteryStats.formatCharge(batteryUsageStats.getConsumedPower()));
                        stringBuilder.append(", actual drain: " + BatteryStats.formatCharge(dischargedPowerRange.getLower()));
                        if (!dischargedPowerRange.getLower().equals(dischargedPowerRange.getUpper())) {
                            stringBuilder.append("-" + BatteryStats.formatCharge(dischargedPowerRange.getUpper()));
                        }
                        ClientLog.log(stringBuilder.toString());

                        BatteryUsageSummaryData batteryUsageSummaryData = new BatteryUsageSummaryData();
                        batteryUsageSummaryData.startTimestamp = batteryUsageStats.getStatsStartTimestamp();
                        batteryUsageSummaryData.endTimestamp = batteryUsageStats.getStatsEndTimestamp();
                        batteryUsageSummaryData.capacity = BatteryStats.formatCharge(batteryUsageStats.getBatteryCapacity());
                        batteryUsageSummaryData.computedDrain = BatteryStats.formatCharge(batteryUsageStats.getConsumedPower());
                        StringBuilder actualDrain = new StringBuilder();
                        actualDrain.append(BatteryStats.formatCharge(dischargedPowerRange.getLower()));
                        if (!dischargedPowerRange.getLower().equals(dischargedPowerRange.getUpper())) {
                            actualDrain.append("-" + BatteryStats.formatCharge(dischargedPowerRange.getUpper()));
                        }
                        batteryUsageSummaryData.actualDrain = actualDrain.toString();

                        list.add(batteryUsageSummaryData);

                        ClientLog.log("    Global");
                        BatteryConsumer deviceConsumer;
                        BatteryConsumer appsConsumer;


                        if (FreezeUtil.atLeast34()) {
                            deviceConsumer = batteryUsageStats.getAggregateBatteryConsumer(BatteryUsageStats.AGGREGATE_BATTERY_CONSUMER_SCOPE_DEVICE);
                            appsConsumer = batteryUsageStats.getAggregateBatteryConsumer(BatteryUsageStats.AGGREGATE_BATTERY_CONSUMER_SCOPE_ALL_APPS);
                        } else {
                            deviceConsumer = ReplaceRef.<BatteryUsageStats31>unsafeCast(batteryUsageStats).getAggregateBatteryConsumer(BatteryUsageStats.AGGREGATE_BATTERY_CONSUMER_SCOPE_DEVICE);
                            appsConsumer = ReplaceRef.<BatteryUsageStats31>unsafeCast(batteryUsageStats).getAggregateBatteryConsumer(BatteryUsageStats.AGGREGATE_BATTERY_CONSUMER_SCOPE_ALL_APPS);
                        }

                        list.add(new BatteryUsageTitleData("Device"));
                        ClientLog.log(" --------------device-------------");

                        for (int componentId = 0; componentId < BatteryConsumer.POWER_COMPONENT_COUNT;
                             componentId++) {
                            for (BatteryConsumer.Key key : deviceConsumer.getKeys(componentId)) {
                                final double devicePowerMah = deviceConsumer.getConsumedPower(key);
                                final double appsPowerMah = appsConsumer.getConsumedPower(key);
                                if (devicePowerMah == 0 && appsPowerMah == 0) {
                                    continue;
                                }

                                String label = BatteryConsumer.powerComponentIdToString(componentId);
                                if (key.processState != BatteryConsumer.PROCESS_STATE_UNSPECIFIED) {
                                    label = label
                                            + "(" + BatteryConsumer.processStateToString(key.processState) + ")";
                                }
                                int powerModel = BatteryConsumer.POWER_MODEL_UNDEFINED;

                                StringBuilder sb = new StringBuilder();
                                sb.append("      ").append(label).append(": ")
                                        .append(BatteryStats.formatCharge(devicePowerMah));
                                if (powerModel != BatteryConsumer.POWER_MODEL_UNDEFINED
                                        && powerModel != BatteryConsumer.POWER_MODEL_POWER_PROFILE) {
                                    sb.append(" [");
                                    sb.append(BatteryConsumer.powerModelToString(powerModel));
                                    sb.append("]");
                                }
                                sb.append(" apps: ").append(BatteryStats.formatCharge(appsPowerMah));

                                long durationMs = deviceConsumer.getUsageDurationMillis(key);

                                if (durationMs != 0) {
                                    sb.append(" duration: ");
                                    BatteryStats.formatTimeMs(sb, durationMs);
                                }

                                BatteryUsageDeviceData batteryUsageGlobalData = new BatteryUsageDeviceData();
                                batteryUsageGlobalData.deviceLabel = label;
                                StringBuilder devicePowerMahBuilder = new StringBuilder();
                                devicePowerMahBuilder.append(BatteryStats.formatCharge(devicePowerMah));
                                if (powerModel != BatteryConsumer.POWER_MODEL_UNDEFINED
                                        && powerModel != BatteryConsumer.POWER_MODEL_POWER_PROFILE) {
                                    devicePowerMahBuilder.append(" [");
                                    devicePowerMahBuilder.append(BatteryConsumer.powerModelToString(powerModel));
                                    devicePowerMahBuilder.append("]");
                                }
                                batteryUsageGlobalData.devicePowerMah = devicePowerMahBuilder.toString();

                                batteryUsageGlobalData.appsLabel = "apps";
                                batteryUsageGlobalData.appsPowerMah = BatteryStats.formatCharge(appsPowerMah);

                                batteryUsageGlobalData.durationLabel = "duration";
                                StringBuilder durationBuilder = new StringBuilder();
                                BatteryStats.formatTimeMs(durationBuilder, durationMs);
                                batteryUsageGlobalData.duration = durationBuilder.toString();

                                list.add(batteryUsageGlobalData);

                                ClientLog.log(sb.toString());
                            }
                        }
                        ClientLog.log(" -----------------------------");
                        for (int componentId = BatteryConsumer.FIRST_CUSTOM_POWER_COMPONENT_ID;
                             componentId < BatteryConsumer.FIRST_CUSTOM_POWER_COMPONENT_ID
                                     + batteryUsageStats.getCustomPowerComponentNames().length;
                             componentId++) {
                            final double devicePowerMah =
                                    deviceConsumer.getConsumedPowerForCustomComponent(componentId);
                            final double appsPowerMah =
                                    appsConsumer.getConsumedPowerForCustomComponent(componentId);
                            if (devicePowerMah == 0 && appsPowerMah == 0) {
                                continue;
                            }

                            String label = deviceConsumer.getCustomPowerComponentName(componentId);
                            int powerModel = BatteryConsumer.POWER_MODEL_UNDEFINED;


                            StringBuilder sb = new StringBuilder();
                            sb.append("      ").append(label).append(": ")
                                    .append(BatteryStats.formatCharge(devicePowerMah));
                            if (powerModel != BatteryConsumer.POWER_MODEL_UNDEFINED
                                    && powerModel != BatteryConsumer.POWER_MODEL_POWER_PROFILE) {
                                sb.append(" [");
                                sb.append(BatteryConsumer.powerModelToString(powerModel));
                                sb.append("]");
                            }
                            sb.append(" apps: ").append(BatteryStats.formatCharge(appsPowerMah));
                            long durationMs = deviceConsumer.getUsageDurationForCustomComponentMillis(componentId);
                            if (durationMs != 0) {
                                sb.append(" duration: ");
                                BatteryStats.formatTimeMs(sb, durationMs);
                            }


                            BatteryUsageDeviceData batteryUsageGlobalData = new BatteryUsageDeviceData();
                            batteryUsageGlobalData.deviceLabel = label;
                            StringBuilder devicePowerMahBuilder = new StringBuilder();
                            devicePowerMahBuilder.append(BatteryStats.formatCharge(devicePowerMah));
                            if (powerModel != BatteryConsumer.POWER_MODEL_UNDEFINED
                                    && powerModel != BatteryConsumer.POWER_MODEL_POWER_PROFILE) {
                                devicePowerMahBuilder.append(" [");
                                devicePowerMahBuilder.append(BatteryConsumer.powerModelToString(powerModel));
                                devicePowerMahBuilder.append("]");
                            }
                            batteryUsageGlobalData.devicePowerMah = devicePowerMahBuilder.toString();

                            batteryUsageGlobalData.appsLabel = "apps";
                            batteryUsageGlobalData.appsPowerMah = BatteryStats.formatCharge(appsPowerMah);

                            batteryUsageGlobalData.durationLabel = "duration";
                            StringBuilder durationBuilder = new StringBuilder();
                            BatteryStats.formatTimeMs(durationBuilder, durationMs);
                            batteryUsageGlobalData.duration = durationBuilder.toString();

                            list.add(batteryUsageGlobalData);


                            ClientLog.log(sb.toString());
                        }


                        ClientLog.log(" --------------app-------------");
                        list.add(new BatteryUsageTitleData("App"));
                        List<UidBatteryConsumer> batteryConsumers = batteryUsageStats.getUidBatteryConsumers();
                        batteryConsumers.sort(Comparator.<BatteryConsumer>comparingDouble(BatteryConsumer::getConsumedPower).reversed());
                        for (UidBatteryConsumer uidBatteryConsumer : batteryConsumers) {
                            ClientLog.log("-----------------------------------------------------------------");
                            ClientLog.log("uid - " + uidBatteryConsumer.getUid());
                            ClientLog.log("packageName - " + uidBatteryConsumer.getPackageWithHighestDrain());
                            ClientLog.log("前台时长 - " + uidBatteryConsumer.getTimeInStateMs(UidBatteryConsumer.STATE_FOREGROUND));
                            ClientLog.log("后台时长 - " + uidBatteryConsumer.getTimeInStateMs(UidBatteryConsumer.STATE_BACKGROUND));
                            String separator = "";
                            StringBuilder sb = new StringBuilder();
                            BatteryUsageAppData batteryUsageAppData = new BatteryUsageAppData();
                            batteryUsageAppData.uid = uidBatteryConsumer.getUid();
                            batteryUsageAppData.packageName = uidBatteryConsumer.getPackageWithHighestDrain();
                            if (TextUtils.isEmpty(batteryUsageAppData.packageName)) {
                                String[] packages = ClientSystemService.getPackageManager().getPackagesForUid(batteryUsageAppData.uid);
                                if (packages != null && packages.length > 0) {
                                    batteryUsageAppData.packageName = packages[0];
                                }
                            }

                            if (TextUtils.equals(batteryUsageAppData.packageName, BuildConfig.APPLICATION_ID)) {
                                continue;
                            }

                            batteryUsageAppData.foregroundTime = uidBatteryConsumer.getTimeInStateMs(UidBatteryConsumer.STATE_FOREGROUND);
                            batteryUsageAppData.backgroundTime = uidBatteryConsumer.getTimeInStateMs(UidBatteryConsumer.STATE_BACKGROUND);


                            for (int componentId = 0; componentId < BatteryConsumer.POWER_COMPONENT_COUNT;
                                 componentId++) {

                                for (BatteryConsumer.Key key : uidBatteryConsumer.getKeys(componentId)) {
                                    final double componentPower = uidBatteryConsumer.getConsumedPower(key);
                                    final long durationMs = uidBatteryConsumer.getUsageDurationMillis(key);
                                    if (componentPower == 0 && durationMs == 0) {
                                        continue;
                                    }

                                    sb.append(separator);
                                    separator = " ";
                                    sb.append(key.toShortString());
                                    sb.append("=");
                                    sb.append(BatteryStats.formatCharge(componentPower));

                                    if (durationMs != 0) {
                                        sb.append(" (");
                                        BatteryStats.formatTimeMsNoSpace(sb, durationMs);
                                        sb.append(")");
                                    }

                                    BatteryUsageAppData.HardwareData hardwareData = new BatteryUsageAppData.HardwareData();
                                    hardwareData.label = key.toShortString();
                                    StringBuilder content = new StringBuilder();
                                    content.append(BatteryStats.formatCharge(componentPower));
                                    if (durationMs != 0) {
                                        content.append(" (");
                                        BatteryStats.formatTimeMsNoSpace(content, durationMs);
                                        content.append(")");
                                    }
                                    hardwareData.content = content.toString();
                                    batteryUsageAppData.addHardwareData(hardwareData);
                                }
                            }

                            final int customComponentCount = uidBatteryConsumer.getCustomPowerComponentCount();
                            for (int customComponentId = BatteryConsumer.FIRST_CUSTOM_POWER_COMPONENT_ID;
                                 customComponentId < BatteryConsumer.FIRST_CUSTOM_POWER_COMPONENT_ID + customComponentCount;
                                 customComponentId++) {
                                final double customComponentPower =
                                        uidBatteryConsumer.getConsumedPowerForCustomComponent(customComponentId);
                                if (customComponentPower == 0) {
                                    continue;
                                }
                                sb.append(separator);
                                separator = " ";
                                sb.append(uidBatteryConsumer.getCustomPowerComponentName(customComponentId));
                                sb.append("=");
                                sb.append(BatteryStats.formatCharge(customComponentPower));


                                BatteryUsageAppData.HardwareData hardwareData = new BatteryUsageAppData.HardwareData();
                                hardwareData.label = uidBatteryConsumer.getCustomPowerComponentName(customComponentId);
                                hardwareData.content = BatteryStats.formatCharge(customComponentPower);
                                batteryUsageAppData.addHardwareData(hardwareData);

                            }
                            if (!batteryUsageAppData.isEmpty()) {
                                list.add(batteryUsageAppData);
                            }
                            ClientLog.log("info - " + sb.toString());
                        }
                    }
                    if (callback != null) {
                        callback.success(list);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    if (callback != null) {
                        callback.fail();
                    }
                    ClientLog.log("batteryUsageStats exception ");
                }
            }
        });
    }
}
