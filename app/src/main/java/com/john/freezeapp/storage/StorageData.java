package com.john.freezeapp.storage;

import android.app.usage.StorageStats;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.john.freezeapp.recyclerview.CardData;


@RequiresApi(Build.VERSION_CODES.O)
public class StorageData extends CardData implements Parcelable, Comparable<StorageData> {
    public PackageInfo packageInfo;
    public StorageStats storageStats;
    public String packageName;

    public StorageData(String packageName, PackageInfo packageInfo, StorageStats storageStats) {
        this.packageInfo = packageInfo;
        this.packageName = packageName;
        setStorageStats(storageStats);
    }

    public long codeBytes = -1;
    public long dataBytes = -1;
    public long cacheBytes = -1;

    protected StorageData(Parcel in) {
        packageInfo = in.readParcelable(PackageInfo.class.getClassLoader());
        storageStats = in.readParcelable(StorageStats.class.getClassLoader());
        packageName = in.readString();
        codeBytes = in.readLong();
        dataBytes = in.readLong();
        cacheBytes = in.readLong();
    }

    public static final Creator<StorageData> CREATOR = new Creator<StorageData>() {
        @Override
        public StorageData createFromParcel(Parcel in) {
            return new StorageData(in);
        }

        @Override
        public StorageData[] newArray(int size) {
            return new StorageData[size];
        }
    };

    @Override
    public int compareTo(StorageData o) {
        return Long.compare(o.cacheBytes, cacheBytes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(packageInfo, flags);
        dest.writeParcelable(storageStats, flags);
        dest.writeString(packageName);
        dest.writeLong(codeBytes);
        dest.writeLong(dataBytes);
        dest.writeLong(cacheBytes);
    }

    public void setStorageStats(StorageStats storageStats) {
        if (storageStats != null) {
            this.storageStats = storageStats;
            this.codeBytes = storageStats.getAppBytes();
            this.dataBytes = storageStats.getDataBytes();
            this.cacheBytes = storageStats.getCacheBytes();
        }
    }
}
