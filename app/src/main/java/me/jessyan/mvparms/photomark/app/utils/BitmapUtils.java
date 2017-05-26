package me.jessyan.mvparms.photomark.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;


import java.io.ByteArrayOutputStream;

/**
 * Created by zhiPeng.S on 2016/6/1.
 */
public class BitmapUtils {

    public static Bitmap drawableToBitamp(Drawable drawable){

         int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        System.out.println("Drawable转Bitmap");
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w,h,config);
             //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap drawableToBitamp_2(Drawable drawable)
    {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap bitmap = bd.getBitmap();
        return bitmap;
    }

    public static byte[] Bitmap2Bytes(Bitmap bm, int ratio) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, ratio, baos);
        return baos.toByteArray();
    }

    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    public static Bitmap compressBitmap(Bitmap bm, int ratio){
        return Bytes2Bimap(Bitmap2Bytes(bm,ratio));
    }


    //计算图片的缩放值
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    //把bitmap转换成String
    public static String bitmapToString(String filePath) {

        Bitmap bm = getSmallBitmap(filePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    /**
     * dp2px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 生成圆形头像图片
     *
     * @param context
     * @param source
     * @return
     */
    public static Bitmap getHeaderCircleImage(Context context, Bitmap source) {
        int min = dip2px(context,50);
        return createCircleImage(source, min);
    }

    /**
     * 转成圆形图片，默认背景色
     *
     * @param source
     * @param min
     * @return
     */
    public static Bitmap createCircleBackGroundImage(Bitmap source, int min) {
        return createCircleBackGroundImage(source, min, -1);
    }

    /**
     * 转成圆形图片，指定背景色
     *
     * @param source
     * @param min
     * @return
     */
    public static Bitmap createCircleBackGroundImage(Bitmap source, int min, int color) {
        if (source != null) {
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            if (color != -1) {
                paint.setColor(color);
            } else {
                paint.setColor(Color.WHITE);
            }
            Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
            /**
             * 产生�?个同样大小的画布
             */
            Canvas canvas = new Canvas(target);
            /**
             * 首先绘制圆形
             */
            canvas.drawCircle(min / 2, min / 2, min / 2, paint);

            /**
             * 绘制图片
             */
            int w = source.getWidth();
            int h = source.getHeight();
            float scale = 1.0f;
            boolean scaled = false;
            if (w > h) {
                if (w > min) {
                    scale = (1.0f * w) / min;
                    scaled = true;
                }

            } else {
                if (h > min) {
                    scale = (1.0f * h) / min;
                    scaled = true;
                }
            }
            float cx;
            float cy;
            if (scaled) {
                Bitmap zoomed = zoomBitmapByScale(source, scale);
                w = zoomed.getWidth();
                h = zoomed.getHeight();
                cx = (1.0f * (min - w)) / 2.0f;
                cy = (1.0f * (min - h)) / 2.0f;
                canvas.drawBitmap(zoomed, cx, cy, paint);
            } else {
                cx = (1.0f * (min - w)) / 2.0f;
                cy = (1.0f * (min - h)) / 2.0f;
                canvas.drawBitmap(source, cx, cy, paint);
            }
            return target;
        }
        return null;
    }

    /**
     * 转成圆形图片，指定背景色
     *
     * @param source
     * @param min
     * @return
     */
    public static Bitmap createCircleImage(Bitmap source, int min) {
        if (source != null) {

            Bitmap target = Bitmap.createBitmap(min, min, Bitmap.Config.ARGB_8888);
            /**
             * 产生�?个同样大小的画布
             */
            Canvas canvas = new Canvas(target);

            /**
             * 绘制图片
             */
            int w = source.getWidth();
            int h = source.getHeight();
            float scale = 1.0f;
            boolean scaled = false;
            // float roundPx = 0;
            if (w > h) {
                /*
                 * if(w > min){ roundPx = h; }else{ roundPx = min; }
				 */
                scale = (1.0f * min) / w;
                scaled = true;

            } else {
                /*
                 * if(h > min){ roundPx = w; }else{ roundPx = min; }
				 */
                scale = (1.0f * min) / h;
                scaled = true;
            }

            Bitmap displayBitmap = source;
            if (scaled) {
                Bitmap zoomed = zoomBitmapByScale(source, scale);
                displayBitmap = zoomed;
            }

            final Paint paint = new Paint();
            paint.setColor(Color.GRAY);
            // 设置边缘光滑，去掉锯�?
            paint.setAntiAlias(true);
            // 宽高相等，即正方�?
            final RectF rect = new RectF(0, 0, min, min);
            // 通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时，
            // 且都等于r/2时，画出来的圆角矩形就是圆形
            canvas.drawRoundRect(rect, min / 2, min / 2, paint);
            // 设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去�?
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            // canvas将bitmap画在backgroundBmp�?
            canvas.drawBitmap(displayBitmap, null, rect, paint);
            return target;
        }
        return null;
    }

    /**
     * 使用长宽缩放比缩�?
     *
     * @param srcBitmap
     * @return
     */
    public static Bitmap zoomBitmapByScale(Bitmap srcBitmap, float scale) {
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap resizedBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcWidth, srcHeight, matrix, true);
        if (resizedBitmap != null) {
            return resizedBitmap;
        } else {
            return srcBitmap;
        }
    }
}
