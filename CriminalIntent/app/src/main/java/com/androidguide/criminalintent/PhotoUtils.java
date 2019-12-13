package com.androidguide.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

// 照片处理
public class PhotoUtils {

    public static Bitmap getScaledBitmap(String path, int w, int h){
        // 读取原照片文件尺寸
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        float sw = options.outWidth;
        float sh = options.outHeight;

        // 缩放比率
        int scale = 1;
        if (sw > w || sh > h) {
            float wScale = sw/w;
            float hScale = sh/h;
            scale = Math.round(hScale>wScale?hScale:wScale);
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = scale;
        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point();
        // 估算大小，保证缩略图不会太大.
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScaledBitmap(path, size.x, size.y);
    }
}
