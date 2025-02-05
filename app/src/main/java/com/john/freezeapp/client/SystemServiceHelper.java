package com.john.freezeapp.client;

import android.annotation.SuppressLint;
import android.os.IBinder;
import android.os.ServiceManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("PrivateApi")
public class SystemServiceHelper {

    private static final Map<String, IBinder> SYSTEM_SERVICE_CACHE = new HashMap<>();
    private static final Map<String, Integer> TRANSACT_CODE_CACHE = new HashMap<>();

    static {

    }

    public static IBinder getSystemService(@NonNull String name) {
        IBinder binder = SYSTEM_SERVICE_CACHE.get(name);
        if (binder == null) {
            binder = ServiceManager.getService(name);
            SYSTEM_SERVICE_CACHE.put(name, binder);
        }
        return binder;
    }
    @Deprecated
    public static Integer getTransactionCode(@NonNull String className, @NonNull String methodName) {
        final String fieldName = "TRANSACTION_" + methodName;
        final String key = className + "." + fieldName;

        Integer value = TRANSACT_CODE_CACHE.get(key);
        if (value != null) return value;

        try {
            final Class<?> cls = Class.forName(className);
            Field declaredField = null;
            try {
                declaredField = cls.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                for (Field f : cls.getDeclaredFields()) {
                    if (f.getType() != int.class)
                        continue;

                    String name = f.getName();
                    if (name.startsWith(fieldName + "_")
                            && TextUtils.isDigitsOnly(name.substring(fieldName.length() + 1))) {
                        declaredField = f;
                        break;
                    }
                }
            }
            if (declaredField == null) {
                return null;
            }

            declaredField.setAccessible(true);
            value = declaredField.getInt(cls);

            TRANSACT_CODE_CACHE.put(key, value);
            return value;
        } catch (ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
