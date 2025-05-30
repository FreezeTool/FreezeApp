// IRunAsBinder.aidl
package com.john.freezeapp.daemon.runas;
import com.john.freezeapp.daemon.runas.RunAsProcessModel;
// Declare any non-default types here with import statements

interface IRunAsBinder {

    void start(String classPath, String packageName);

    void stop(String packageName);

    List<RunAsProcessModel> getActiveRunAsProcess();

    List<RunAsProcessModel> getRunAsProcess();
}