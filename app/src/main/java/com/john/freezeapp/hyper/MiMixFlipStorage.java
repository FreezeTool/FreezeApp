package com.john.freezeapp.hyper;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.john.freezeapp.util.SharedPrefUtil;

import java.util.HashMap;
import java.util.Map;

public class MiMixFlipStorage {
    public static final String KEY_MI_MIX_FLIP_SCALE = "key_mi_mix_flip_scale";

    public static Map<String, String> getScale() {
        String string = SharedPrefUtil.getString(KEY_MI_MIX_FLIP_SCALE, null);
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return new Gson().fromJson(string, new TypeToken<Map<String, String>>() {
        }.getType());
    }

    public static void setScale(String packageName, String scale) {
        Map<String, String> scaleMap = getScale();
        if (scaleMap == null) {
            scaleMap = new HashMap<>();
        }
        scaleMap.put(packageName, scale);
        SharedPrefUtil.setString(KEY_MI_MIX_FLIP_SCALE, new Gson().toJson(scaleMap));
    }

    public static void resetScale() {
        SharedPrefUtil.setString(KEY_MI_MIX_FLIP_SCALE, null);
    }
}
