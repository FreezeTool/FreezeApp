package com.john.freezeapp.daemon.monitor;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class DaemonAppMonitorConfig implements Parcelable {
    public int size;

    public DaemonAppMonitorConfig(Parcel in) {
        size = in.readInt();
    }

    public DaemonAppMonitorConfig() {
    }

    public static final Creator<DaemonAppMonitorConfig> CREATOR = new Creator<DaemonAppMonitorConfig>() {
        @Override
        public DaemonAppMonitorConfig createFromParcel(Parcel in) {
            return new DaemonAppMonitorConfig(in);
        }

        @Override
        public DaemonAppMonitorConfig[] newArray(int size) {
            return new DaemonAppMonitorConfig[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(size);
    }
}
