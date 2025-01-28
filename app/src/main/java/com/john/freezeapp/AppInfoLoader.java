package com.john.freezeapp;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class AppInfoLoader {

    public interface LoadAppInfoCallback {
        void callback(FreezeAppManager.CacheAppModel cacheAppModel);
    }

    public static void load(Context context, String packageName, LoadAppInfoCallback callback) {
        if (!TextUtils.isEmpty(packageName)) {
            ThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    callback.callback(FreezeAppManager.getAppModel(context, packageName));
                }
            });
        }
    }

    public static void load(Context context, String packageName, ImageView imageView, TextView textView) {
        load(context, packageName, imageView, textView, null);
    }

    public static void load(Context context, String packageName, ImageView imageView, TextView textView, LoadAppInfoCallback callback) {
        if (!TextUtils.isEmpty(packageName)) {
            imageView.setTag(R.id.load_image_id, packageName);
            textView.setTag(R.id.load_text_id, packageName);
            load(context, packageName, cacheAppModel -> {
                UIExecutor.post(() -> {
                    if (TextUtils.equals(String.valueOf(imageView.getTag(R.id.load_image_id)), cacheAppModel.packageName)) {
                        if (cacheAppModel.icon != null) {
                            imageView.setImageDrawable(cacheAppModel.icon);
                        }
                    }

                    if (TextUtils.equals(String.valueOf(textView.getTag(R.id.load_text_id)), cacheAppModel.packageName)) {
                        if (!TextUtils.isEmpty(cacheAppModel.name)) {
                            textView.setText(cacheAppModel.name);
                        }
                    }
                    if (callback != null) {
                        callback.callback(cacheAppModel);
                    }
                });
            });
        }
    }
}
