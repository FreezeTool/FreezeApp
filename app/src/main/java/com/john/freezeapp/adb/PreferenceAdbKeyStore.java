package com.john.freezeapp.adb;

import android.content.SharedPreferences;
import android.util.Base64;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PreferenceAdbKeyStore implements AdbKeyStore {
    @NotNull
    private final SharedPreferences preference;
    @NotNull
    private final String preferenceKey;

    public PreferenceAdbKeyStore(@NotNull SharedPreferences preference) {
        Intrinsics.checkNotNullParameter(preference, "preference");
        this.preference = preference;
        this.preferenceKey = "adbkey";
    }

    public void put(@NotNull byte[] bytes) {
        Intrinsics.checkNotNullParameter(bytes, "bytes");
        SharedPreferences $this$edit_u24default$iv = this.preference;
        SharedPreferences.Editor editor$iv = $this$edit_u24default$iv.edit();
        Intrinsics.checkNotNullExpressionValue(editor$iv, "editor");
        SharedPreferences.Editor $this$put_u24lambda_u240 = editor$iv;
        String var10001 = this.preferenceKey;
        byte[] var8 = Base64.encode(bytes, 2);
        Intrinsics.checkNotNullExpressionValue(var8, "encode(bytes, Base64.NO_WRAP)");
        $this$put_u24lambda_u240.putString(var10001, new String(var8, Charsets.UTF_8));
        editor$iv.apply();
    }

    @Nullable
    public byte[] get() {
        return !this.preference.contains(this.preferenceKey) ? null : Base64.decode(this.preference.getString(this.preferenceKey, (String) null), 2);
    }
}