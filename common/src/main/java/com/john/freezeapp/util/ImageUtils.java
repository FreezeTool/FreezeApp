package com.john.freezeapp.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    /**
     * 将 res/mipmap 中的图片转换为二进制数据
     * @param context 上下文对象
     * @param resourceId 资源ID（例如 R.mipmap.ic_launcher）
     * @return 图片的二进制数据，失败时返回 null
     */
    public static byte[] convertDrawableToByteArray(Context context, int resourceId) {
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            // 1. 获取资源输入流
            inputStream = context.getResources().openRawResource(resourceId);
            
            // 2. 创建缓冲区
            byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            
            // 3. 循环读写数据
            while ((length = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            
            // 4. 返回字节数组
            return byteArrayOutputStream.toByteArray();
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            // 5. 确保流被关闭
            try {
                if (inputStream != null) inputStream.close();
                if (byteArrayOutputStream != null) byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap bytesToBitmap(byte[] byteArray) {
        if (byteArray == null || byteArray.length == 0) return null;
        try {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 将 Bitmap 转换为二进制数据（字节数组）
     * @param bitmap    要转换的 Bitmap 对象
     * @param format    压缩格式（PNG/JPEG/WEBP）
     * @param quality   压缩质量（0-100），仅对 JPEG/WEBP 有效
     * @return          二进制数据，失败时返回 null
     */
    public static byte[] bitmapToByteArray(Bitmap bitmap, Bitmap.CompressFormat format, int quality) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }

        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            boolean success = bitmap.compress(format, quality, outputStream);
            if (!success) {
                return null;
            }
            return outputStream.toByteArray();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
