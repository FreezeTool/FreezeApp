package android.content.pm;

import android.content.ComponentName;

public class LauncherActivityInfoInternal {

    private ActivityInfo mActivityInfo;
    private ComponentName mComponentName;
    private IncrementalStatesInfo mIncrementalStatesInfo;

    public LauncherActivityInfoInternal(ActivityInfo info,
                                        IncrementalStatesInfo incrementalStatesInfo) {
        mActivityInfo = info;
        mComponentName = new ComponentName(info.packageName, info.name);
        mIncrementalStatesInfo = incrementalStatesInfo;
    }


    public ComponentName getComponentName() {
        return mComponentName;
    }

    public ActivityInfo getActivityInfo() {
        return mActivityInfo;
    }

    public IncrementalStatesInfo getIncrementalStatesInfo() {
        return mIncrementalStatesInfo;
    }

}
