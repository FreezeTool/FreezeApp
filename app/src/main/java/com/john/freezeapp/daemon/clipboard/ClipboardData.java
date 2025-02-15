package com.john.freezeapp.daemon.clipboard;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ClipboardData implements Parcelable {
    public String id;
    public String content;
    public String packageName;

    public ClipboardData() {
    }

    protected ClipboardData(Parcel in) {
        id = in.readString();
        content = in.readString();
        packageName = in.readString();
    }

    @Override
    public String toString() {
        return "ClipboardData{" +
                "content='" + content + '\'' +
                ", id='" + id + '\'' +
                ", packageName='" + packageName + '\'' +
                '}';
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
    }
}
