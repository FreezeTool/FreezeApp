// IFrpBinder.aidl
package com.john.freezeapp;

// Declare any non-default types here with import statements

interface IFrpBinder {

    boolean startFrpClient(String frpClientConfig);

    boolean startFrpServer(String frpServerConfig);
}