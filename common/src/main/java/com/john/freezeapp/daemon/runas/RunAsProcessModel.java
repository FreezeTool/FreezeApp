package com.john.freezeapp.daemon.runas;

import android.os.Parcel;
import android.os.Parcelable;

public class RunAsProcessModel implements Parcelable {
    public int pid;
    public String packageName;
    public boolean active;

    public RunAsProcessModel() {
    }


    protected RunAsProcessModel(Parcel in) {
        pid = in.readInt();
        packageName = in.readString();
        active = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pid);
        dest.writeString(packageName);
        dest.writeByte((byte) (active ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RunAsProcessModel> CREATOR = new Creator<RunAsProcessModel>() {
        @Override
        public RunAsProcessModel createFromParcel(Parcel in) {
            return new RunAsProcessModel(in);
        }

        @Override
        public RunAsProcessModel[] newArray(int size) {
            return new RunAsProcessModel[size];
        }
    };
}
