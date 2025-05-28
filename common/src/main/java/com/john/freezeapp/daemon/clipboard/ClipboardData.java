package com.john.freezeapp.daemon.clipboard;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ClipboardData implements Parcelable, Comparable<ClipboardData> {
    public String id;
    public String content;
    public String packageName;
    public long timestamp;

    public ClipboardData() {
    }


    protected ClipboardData(Parcel in) {
        id = in.readString();
        content = in.readString();
        packageName = in.readString();
        timestamp = in.readLong();
    }

    public static final Creator<ClipboardData> CREATOR = new Creator<ClipboardData>() {
        @Override
        public ClipboardData createFromParcel(Parcel in) {
            return new ClipboardData(in);
        }

        @Override
        public ClipboardData[] newArray(int size) {
            return new ClipboardData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(content);
        dest.writeString(packageName);
        dest.writeLong(timestamp);
    }

    @Override
    public int compareTo(ClipboardData o) {
        return Long.compare(o.timestamp, timestamp);
    }
}
