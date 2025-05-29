package com.john.freezeapp.daemon.fs;

import android.net.wifi.IWifiManager;
import android.net.wifi.WifiInfo;
import android.text.format.Formatter;

import com.john.freezeapp.daemon.DaemonLog;
import com.john.freezeapp.daemon.DaemonService;
import com.john.freezeapp.daemon.util.DaemonUtil;
import com.john.freezeapp.util.DeviceUtil;

import java.io.File;

import fi.iki.elonen.NanoHTTPD;

public class FileServerManager {
    private FileServer server;

    String getLocalIpAddress() {
        IWifiManager wifiManager = DaemonService.getWifiManager();
        if (wifiManager != null) {
            WifiInfo connectionInfo;
            if (DeviceUtil.atLeast30()) {
                connectionInfo = wifiManager.getConnectionInfo(DaemonUtil.getCallingPackageName(), null);
            } else if (DeviceUtil.atLeast27()) {
                connectionInfo = wifiManager.getConnectionInfo(DaemonUtil.getCallingPackageName());
            } else {
                connectionInfo = wifiManager.getConnectionInfo();
            }

            if (connectionInfo != null) {
                return Formatter.formatIpAddress(connectionInfo.getIpAddress());
            }
        }
        return null;
    }

    /**
     * 启动文件服务器（使用默认端口和共享目录）
     */
    public boolean startServer() {
        File defaultDir = new File("sdcard");
        return startServer(65001, defaultDir);
    }

    /**
     * 启动文件服务器（自定义端口和目录）
     */
    public boolean startServer(int port, File shareDir) {
        stopServer();
        try {
            // 确保共享目录存在
            if (!shareDir.exists() && !shareDir.mkdirs()) {
                throw new RuntimeException("Failed to create share directory");
            }
            server = new FileServer(port, shareDir, true);
            server.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
            return true;
        } catch (Exception e) {
            DaemonLog.e(e, "startServer");
            return false;
        }
    }

    /**
     * 停止文件服务器
     */
    public void stopServer() {
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    /**
     * 获取服务器访问地址
     */
    public String getAccessUrl() {
        if (server != null) {
            try {
                return "http://" + getLocalIpAddress() + ":" + server.getListeningPort();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 检查服务器是否正在运行
     */
    public boolean isRunning() {
        return server != null && server.isAlive();
    }


}
