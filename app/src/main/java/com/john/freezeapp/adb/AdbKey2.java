package com.john.freezeapp.adb;

import android.annotation.SuppressLint;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.GCMParameterSpec;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedKeyManager;
import javax.net.ssl.X509ExtendedTrustManager;

import kotlin.Lazy;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

public class AdbKey2 {
    @NotNull
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    @NotNull
    private  AdbKeyStore adbKeyStore;
    @NotNull
    private  Key encryptionKey;
    @NotNull
    private  RSAPrivateKey privateKey;
    @NotNull
    private  RSAPublicKey publicKey;
    @NotNull
    private  X509Certificate certificate;
    @NotNull
    private byte[] adbPublicKey$delegate;
    @RequiresApi(30)
    @NotNull
    private SSLContext sslContext$delegate;
    @NotNull
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    @NotNull
    private static final String ENCRYPTION_KEY_ALIAS = "_adbkey_encryption_key_";
    @NotNull
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int IV_SIZE_IN_BYTES = 12;
    private static final int TAG_SIZE_IN_BYTES = 16;
    @NotNull
    private static final byte[] PADDING;

    public AdbKey2(@NotNull AdbKeyStore adbKeyStore, @NotNull final String name) {
        Intrinsics.checkNotNullParameter(adbKeyStore, "adbKeyStore");
        Intrinsics.checkNotNullParameter(name, "name");
        this.adbKeyStore = adbKeyStore;
        Key var3 = null;
        try {
            var3 = this.getOrCreateEncryptionKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (var3 != null) {
            this.encryptionKey = var3;
            try {
                this.privateKey = this.getOrCreatePrivateKey();
            } catch (Exception e) {
                e.printStackTrace();
            }
            PublicKey var6 = null;
            try {
                var6 = KeyFactory.getInstance("RSA").generatePublic((KeySpec) (new RSAPublicKeySpec(this.privateKey.getModulus(), RSAKeyGenParameterSpec.F4)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intrinsics.checkNotNull(var6, "null cannot be cast to non-null type java.security.interfaces.RSAPublicKey");
            this.publicKey = (RSAPublicKey) var6;
            ContentSigner signer = null;
            try {
                signer = (new JcaContentSignerBuilder("SHA256withRSA")).build((PrivateKey) this.privateKey);
            } catch (OperatorCreationException e) {
                throw new RuntimeException(e);
            }
            X509CertificateHolder x509Certificate = (new X509v3CertificateBuilder(new X500Name("CN=00"), BigInteger.ONE, new Date(0L), new Date(2461449600000L), Locale.ROOT, new X500Name("CN=00"), SubjectPublicKeyInfo.getInstance(this.publicKey.getEncoded()))).build(signer);
            Certificate var5 = null;
            try {
                var5 = CertificateFactory.getInstance("X.509").generateCertificate((InputStream) (new ByteArrayInputStream(x509Certificate.getEncoded())));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intrinsics.checkNotNull(var5, "null cannot be cast to non-null type java.security.cert.X509Certificate");
            this.certificate = (X509Certificate) var5;
            Log.d("AdbKey", this.privateKey.toString());
            this.adbPublicKey$delegate =  AdbKeyKt.access$adbEncoded(this.publicKey, name);;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                try {
                    this.sslContext$delegate = getSslCOntextValue();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            throw new IllegalStateException("Failed to generate encryption key with AndroidKeyManager.".toString());
        }
    }

    private SSLContext getSslCOntextValue() throws KeyManagementException, NoSuchAlgorithmException {
        SSLContext sslContext = SSLContext.getInstance("TLSv1.3");
        X509ExtendedKeyManager[]var2 = new X509ExtendedKeyManager[]{getKeyManager()};
        KeyManager[] var10001 = (KeyManager[]) var2;
        X509ExtendedTrustManager[]var3 = new X509ExtendedTrustManager[]{getTrustManager()} ;
        sslContext.init(var10001, (TrustManager[]) var3, new SecureRandom());
        return sslContext;
    }

    @NotNull
    public byte[] getAdbPublicKey() {
        return this.adbPublicKey$delegate;
    }

    private Key getOrCreateEncryptionKey() throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load((KeyStore.LoadStoreParameter) null);
        Key var2 = keyStore.getKey("_adbkey_encryption_key_", (char[]) null);
        Key var10000;
        if (var2 != null) {
            var10000 = var2;
        } else {
            KeyGenParameterSpec.Builder var9 = new KeyGenParameterSpec.Builder("_adbkey_encryption_key_", KeyProperties.PURPOSE_DECRYPT | KeyProperties.PURPOSE_ENCRYPT);
            var9 = var9.setBlockModes(KeyProperties.BLOCK_MODE_GCM);
            KeyGenParameterSpec var7 = var9.setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE).setKeySize(256).build();
            KeyGenParameterSpec parameterSpec = var7;
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", "AndroidKeyStore");
            keyGenerator.init((AlgorithmParameterSpec) parameterSpec);
            var10000 = (Key) keyGenerator.generateKey();
        }

        return var10000;
    }

    private byte[] encrypt(byte[] plaintext, byte[] aad) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, ShortBufferException, BadPaddingException {
        if (plaintext.length > 2147483619) {
            return null;
        } else {
            byte[] ciphertext = new byte[12 + plaintext.length + 16];
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(1, this.encryptionKey);
            cipher.updateAAD(aad);
            cipher.doFinal(plaintext, 0, plaintext.length, ciphertext, 12);
            System.arraycopy(cipher.getIV(), 0, ciphertext, 0, 12);
            return ciphertext;
        }
    }

    private final byte[] decrypt(byte[] ciphertext, byte[] aad) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        if (ciphertext.length < 28) {
            return null;
        } else {
            GCMParameterSpec params = new GCMParameterSpec(128, ciphertext, 0, 12);
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(2, this.encryptionKey, (AlgorithmParameterSpec) params);
            cipher.updateAAD(aad);
            return cipher.doFinal(ciphertext, 12, ciphertext.length - 12);
        }
    }

    private final RSAPrivateKey getOrCreatePrivateKey() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, ShortBufferException, BadPaddingException, InvalidKeyException {
        RSAPrivateKey privateKey = null;
        String var3 = "adbkey";
        byte[] var10000 = var3.getBytes(Charsets.UTF_8);

        ByteBuffer allocate = ByteBuffer.allocate(16);
        allocate.put(var10000);
        byte[] aad = allocate.array();

        byte[] ciphertext = this.adbKeyStore.get();
        PrivateKey var6;
        if (ciphertext != null) {
            try {
                byte[] plaintext = this.decrypt(ciphertext, aad);
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                var6 = keyFactory.generatePrivate((KeySpec) (new PKCS8EncodedKeySpec(plaintext)));
                Intrinsics.checkNotNull(var6, "null cannot be cast to non-null type java.security.interfaces.RSAPrivateKey");
                privateKey = (RSAPrivateKey) var6;
            } catch (Exception var7) {
            }
        }

        if (privateKey == null) {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize((AlgorithmParameterSpec) (new RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4)));
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            var6 = keyPair.getPrivate();
            Intrinsics.checkNotNull(var6, "null cannot be cast to non-null type java.security.interfaces.RSAPrivateKey");
            privateKey = (RSAPrivateKey) var6;
            byte[] var12 = privateKey.getEncoded();
            Intrinsics.checkNotNullExpressionValue(var12, "privateKey.encoded");
            ciphertext = encrypt(var12, aad);
            if (ciphertext != null) {
                this.adbKeyStore.put(ciphertext);
            }
        }

        return privateKey;
    }

    @NotNull
    public final byte[] sign(@Nullable byte[] data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
        cipher.init(1, (Key) this.privateKey);
        cipher.update(PADDING);
        byte[] var3 = cipher.doFinal(data);
        Intrinsics.checkNotNullExpressionValue(var3, "cipher.doFinal(data)");
        return var3;
    }

    private X509ExtendedKeyManager getKeyManager() {
        return new X509ExtendedKeyManager() {
            private final String alias = "key";

            public String chooseClientAlias(String[] keyTypes, Principal[] issuers, Socket socket) {
                Intrinsics.checkNotNullParameter(keyTypes, "keyTypes");
                StringBuilder var10001 = (new StringBuilder()).append("chooseClientAlias: keyType=");
                String var10002 = Arrays.toString(keyTypes);
                Intrinsics.checkNotNullExpressionValue(var10002, "toString(this)");
                var10001 = var10001.append(var10002).append(", issuers=");
                if (issuers != null) {
                    var10002 = Arrays.toString(issuers);
                    Intrinsics.checkNotNullExpressionValue(var10002, "toString(this)");
                } else {
                    var10002 = null;
                }

                Log.d("AdbKey", var10001.append(var10002).toString());
                int var4 = 0;

                for (int var5 = keyTypes.length; var4 < var5; ++var4) {
                    String keyType = keyTypes[var4];
                    if (Intrinsics.areEqual(keyType, "RSA")) {
                        return this.alias;
                    }
                }

                return null;
            }

            public X509Certificate[] getCertificateChain(String alias) {
                Log.d("AdbKey", "getCertificateChain: alias=" + alias);
                X509Certificate[] var10000;
                if (Intrinsics.areEqual(alias, this.alias)) {
                    X509Certificate[] var2 = new X509Certificate[]{AdbKey2.this.certificate};
                    var10000 = var2;
                } else {
                    var10000 = null;
                }

                return var10000;
            }

            public PrivateKey getPrivateKey(String alias) {
                Log.d("AdbKey", "getPrivateKey: alias=" + alias);
                return Intrinsics.areEqual(alias, this.alias) ? (PrivateKey) AdbKey2.this.privateKey : null;
            }

            public String[] getClientAliases(String keyType, Principal[] issuers) {
                return null;
            }

            public String[] getServerAliases(String keyType, Principal[] issuers) {
                Intrinsics.checkNotNullParameter(keyType, "keyType");
                return null;
            }

            public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
                Intrinsics.checkNotNullParameter(keyType, "keyType");
                return null;
            }
        };
    }

    private X509ExtendedTrustManager getTrustManager() {
        return new X509ExtendedTrustManager() {
            @SuppressLint({"TrustAllX509TrustManager"})
            public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) {
            }

            @SuppressLint({"TrustAllX509TrustManager"})
            public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {
            }

            @SuppressLint({"TrustAllX509TrustManager"})
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @SuppressLint({"TrustAllX509TrustManager"})
            public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) {
            }

            @SuppressLint({"TrustAllX509TrustManager"})
            public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {
            }

            @SuppressLint({"TrustAllX509TrustManager"})
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    @NotNull
    public SSLContext getSslContext() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return sslContext$delegate;
        }
        return null;
    }

    static {
        byte[] var0 = new byte[]{0, 1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 48, 33, 48, 9, 6, 5, 43, 14, 3, 2, 26, 5, 0, 4, 20};
        PADDING = var0;
    }

    @Metadata(
            mv = {1, 8, 0},
            k = 1,
            xi = 48,
            d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0007X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\f"},
            d2 = {"Lmoe/shizuku/manager/adb/AdbKey$Companion;", "", "()V", "ANDROID_KEYSTORE", "", "ENCRYPTION_KEY_ALIAS", "IV_SIZE_IN_BYTES", "", "PADDING", "", "TAG_SIZE_IN_BYTES", "TRANSFORMATION", "manager_debug"}
    )
    public static final class Companion {
        private Companion() {
        }

        // $FF: synthetic method
        public Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}


