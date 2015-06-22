package com.testapp.android.core.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import com.ebankit.android.standardbankmz.R;

/*
 * Rounds a Bitmap or Drawable image corners.
 * Also adds a border if needed.
 */
public class PhotoRounder {
    public static final int DP_BORDER_MENU_SIZE = 3;
    public static final int DP_BORDER_PROFILE_PHOTO_SIZE = 3;
    public static final int DP_CORNER_SIZE = 6;
    public static final int DP_CORNER_OPERATIONS_SIZE = 3;

    public PhotoRounder() {
        super();
    }

    /* Lame code */
    public static int IS_LDPI = 1;
    public static int IS_MDPI = 2;
    public static int IS_HDPI = 3;
    public static int IS_XHDPI = 4;
    public static int IS_XXHDPI = 5;
    public static int IS_XXXHDPI = 6;
    private static int getDensityStandard(Context context) {
        double density = context.getResources().getDisplayMetrics().density;
        if (density <= 0.75) {
            return IS_LDPI;
        } else if (density <= 1.0) {
            return IS_MDPI;
        } else if (density <= 1.5) {
            return IS_HDPI;
        } else if (density <= 2.0) {
            return IS_XHDPI;
        } else if (density <= 3.0) {
            return IS_XXHDPI;
        } else {
            return IS_XXXHDPI;
        }
    }
    public static float getRounderDensity(Context context, int borderDips) {
        int dps = borderDips;
        float factor = 0;
        if (getDensityStandard(context) == IS_LDPI) {
            factor = 0.75f;
        } else if (getDensityStandard(context) == IS_MDPI) {
            factor = 1; //pixels = 27
        } else if (getDensityStandard(context) == IS_HDPI) {
            factor = 1.5f;  //pixels = 40
        } else if (getDensityStandard(context) == IS_XHDPI) {
            factor = 2;
        } else if (getDensityStandard(context) == IS_XXHDPI) {
            factor = 3;
        } else if (getDensityStandard(context) == IS_XXXHDPI) {
            factor = 4;
        }
        if(factor > 1.5f){
            return (dps * factor)/2;
        }
        return (dps * factor);
    }

    /* getRoundedCornerBitmap */
    public static Bitmap getRoundedCornerBitmap(Context context, Bitmap bitmap, int cornerDips) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int cornerSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) cornerDips,
                context.getResources().getDisplayMetrics());
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        // prepare canvas for transfer
        paint.setAntiAlias(true);
        paint.setColor(0xFFFFFFFF);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);

        // draw bitmap
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /* getRoundedCornerBitmapWithBorder */
    public static Bitmap getRoundedCornerBitmapWithBorder(Context context, Bitmap bitmap, int borderDips, int cornerDips) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int borderSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getRounderDensity(context, borderDips),
                context.getResources().getDisplayMetrics());
        final int cornerSizePx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getRounderDensity(context, cornerDips),
                context.getResources().getDisplayMetrics());
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        // prepare canvas for transfer
        paint.setAntiAlias(true);
        paint.setColor(0xFFFFFFFF);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);

        // draw bitmap
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        // draw border
        paint.setColor(context.getResources().getColor(R.color.dashboard_divider));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth((float) borderSizePx);
        canvas.drawRoundRect(rectF, cornerSizePx, cornerSizePx, paint);

        return output;
    }
    
    /* getRoundedCornerBitmapFromDrawable */
    public static Bitmap getRoundedCornerBitmapFromDrawable(Context context, Drawable drawable, int cornerDips) {
        if (drawable instanceof BitmapDrawable) {
            return getRoundedCornerBitmap(context, ((BitmapDrawable) drawable).getBitmap(), cornerDips);
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return getRoundedCornerBitmap(context, bitmap, cornerDips);
    }
    
    /* getRoundedCornerBitmapFromDrawableWithBorder */
    public static Bitmap getRoundedCornerBitmapFromDrawableWithBorder(Context context, Drawable drawable, int borderDips, int cornerDips) {
        if (drawable instanceof BitmapDrawable) {
            return getRoundedCornerBitmapWithBorder(context, ((BitmapDrawable) drawable).getBitmap(), borderDips, cornerDips);
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return getRoundedCornerBitmapWithBorder(context, bitmap, borderDips, cornerDips);
    }

}
