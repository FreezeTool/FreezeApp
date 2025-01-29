package com.john.freezeapp.adb;

import android.util.Base64;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.security.interfaces.RSAPublicKey;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.SourceDebugExtension;
import kotlin.text.Charsets;
import org.jetbrains.annotations.NotNull;

public class AdbKeyKt {
    @NotNull
    private static final String TAG = "AdbKey";
    public static final int ANDROID_PUBKEY_MODULUS_SIZE = 256;
    public static final int ANDROID_PUBKEY_MODULUS_SIZE_WORDS = 64;
    public static final int RSAPublicKey_Size = 524;

    private static final int[] toAdbEncoded(BigInteger $this$toAdbEncoded) {
        int[] endcoded = new int[64];
        BigInteger r32 = BigInteger.ZERO.setBit(32);
        BigInteger tmp = $this$toAdbEncoded.add(BigInteger.ZERO);

        for (int i = 0; i < 64; ++i) {
            BigInteger[] out = tmp.divideAndRemainder(r32);
            tmp = out[0];
            endcoded[i] = out[1].intValue();
        }

        return endcoded;
    }

    private static byte[] adbEncoded(RSAPublicKey $this$adbEncoded, String name) {
        BigInteger r32 = BigInteger.ZERO.setBit(32);
        BigInteger n0inv = $this$adbEncoded.getModulus().remainder(r32).modInverse(r32).negate();
        BigInteger r = BigInteger.ZERO.setBit(2048);
        BigInteger rr = r.modPow(BigInteger.valueOf(2L), $this$adbEncoded.getModulus());
        ByteBuffer buffer = ByteBuffer.allocate(524).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(64);
        buffer.putInt(n0inv.intValue());
        BigInteger var7 = $this$adbEncoded.getModulus();
        Intrinsics.checkNotNullExpressionValue(var7, "modulus");
        int[] $this$forEach$iv = toAdbEncoded(var7);
        int var9 = 0;

        int var10;
        int element$iv;
        int it;
        boolean var13;
        for (var10 = $this$forEach$iv.length; var9 < var10; ++var9) {
            element$iv = $this$forEach$iv[var9];
            it = element$iv;
            var13 = false;
            buffer.putInt(it);
        }

        Intrinsics.checkNotNullExpressionValue(rr, "rr");
        $this$forEach$iv = toAdbEncoded(rr);
        var9 = 0;

        for (var10 = $this$forEach$iv.length; var9 < var10; ++var9) {
            element$iv = $this$forEach$iv[var9];
            it = element$iv;
            var13 = false;
            buffer.putInt(it);
        }

        buffer.putInt($this$adbEncoded.getPublicExponent().intValue());
        byte[] base64Bytes = Base64.encode(buffer.array(), 2);
        String var17 = ' ' + name + '\u0000';
        Charset var18 = Charsets.UTF_8;
        byte[] var10000 = var17.getBytes(var18);
        Intrinsics.checkNotNullExpressionValue(var10000, "this as java.lang.String).getBytes(charset)");
        byte[] nameBytes = var10000;
//        byte[] bytes = new byte[base64Bytes.length + nameBytes.length];
        ByteBuffer byteBuffer = ByteBuffer.allocate(base64Bytes.length + nameBytes.length);
//        ArraysKt.copyInto$default(base64Bytes, bytes, 0, 0, 0, 14, (Object) null);
//        ArraysKt.copyInto$default(nameBytes, bytes, base64Bytes.length, 0, 0, 12, (Object) null);
        byteBuffer.put(base64Bytes);
        byteBuffer.put(nameBytes);
        return byteBuffer.array();
    }

    // $FF: synthetic method
    public static final byte[] access$adbEncoded(RSAPublicKey $receiver, String name) {
        return adbEncoded($receiver, name);
    }

}