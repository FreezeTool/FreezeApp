// IDaemonFileServer.aidl
package com.john.freezeapp.daemon.fs;

interface IDaemonFileServer {
    boolean startServer();

    boolean startFileServer(int port, String shareDir);

    void stopServer();

    boolean isActive();

    String getAccessUrl();
}