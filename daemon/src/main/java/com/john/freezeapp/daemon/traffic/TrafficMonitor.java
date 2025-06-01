package com.john.freezeapp.daemon.traffic;

import android.net.DataUsageRequest;
import android.net.INetworkStatsService;
import android.net.NetworkTemplate;
import android.net.netstats.IUsageCallback;

import com.john.freezeapp.daemon.DaemonService;
import com.john.freezeapp.daemon.util.DaemonUtil;
import com.john.freezeapp.util.DeviceUtil;

public class TrafficMonitor {

    private static IUsageCallbackWrapper sUsageCallbackWrapper;

    public static void start() {
        if (sUsageCallbackWrapper == null) {
            INetworkStatsService networkStatsService = DaemonService.getNetworkStatsService();
            if (networkStatsService != null) {

                sUsageCallbackWrapper = new IUsageCallbackWrapper() {
                    DataUsageRequest request;

                    @Override
                    public DataUsageRequest getRequest() {
                        return request;
                    }

                    @Override
                    public void onThresholdReached(DataUsageRequest request) {

                    }

                    @Override
                    public void onCallbackReleased(DataUsageRequest request) {

                    }

                    @Override
                    public void setRequest(DataUsageRequest finalRequest) {
                        this.request = finalRequest;
                    }
                };
                if (isNewUsageCallback()) {
                    NetworkTemplate networkTemplate = new NetworkTemplate.Builder(NetworkTemplate.MATCH_WIFI).build();
                    final DataUsageRequest request = new DataUsageRequest(DataUsageRequest.REQUEST_ID_UNSET,
                            networkTemplate, 1024);
                    final DataUsageRequest finalRequest = networkStatsService.registerUsageCallback(DaemonUtil.getCallingPackageName(), request, new android.net.connectivity.android.net.netstats.IUsageCallback.Stub() {
                        @Override
                        public void onThresholdReached(DataUsageRequest request) {
                            sUsageCallbackWrapper.onThresholdReached(request);
                        }

                        @Override
                        public void onCallbackReleased(DataUsageRequest request) {
                            sUsageCallbackWrapper.onCallbackReleased(request);
                        }
                    });
                    sUsageCallbackWrapper.setRequest(finalRequest);
                } else if (DeviceUtil.atLeast33()) {
                    NetworkTemplate networkTemplate = new NetworkTemplate.Builder(NetworkTemplate.MATCH_WIFI).build();
                    final DataUsageRequest request = new DataUsageRequest(DataUsageRequest.REQUEST_ID_UNSET,
                            networkTemplate, 1024);
                    networkStatsService.registerUsageCallback(DaemonUtil.getCallingPackageName(), request, new IUsageCallback.Stub() {
                        @Override
                        public void onThresholdReached(DataUsageRequest request) {
                            sUsageCallbackWrapper.onThresholdReached(request);
                        }

                        @Override
                        public void onCallbackReleased(DataUsageRequest request) {
                            sUsageCallbackWrapper.onCallbackReleased(request);
                        }
                    });
                } else {
//                networkStatsService.registerUsageCallback()
                }
            }
        }
    }

    public static void stop() {
        INetworkStatsService networkStatsService = DaemonService.getNetworkStatsService();
        if (networkStatsService != null && sUsageCallbackWrapper != null) {
            networkStatsService.unregisterUsageRequest(sUsageCallbackWrapper.getRequest());
            sUsageCallbackWrapper = null;
        }
    }

    /**
     * fix 部分手机的IUsageCallback包名有问题
     * @return
     */
    public static boolean isNewUsageCallback() {
        try {
            Class.forName("android.net.connectivity.android.net.netstats.IUsageCallback");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }


    private interface IUsageCallbackWrapper {

        DataUsageRequest getRequest();

        void onThresholdReached(DataUsageRequest request);

        void onCallbackReleased(DataUsageRequest request);

        void setRequest(DataUsageRequest finalRequest);
    }
}
