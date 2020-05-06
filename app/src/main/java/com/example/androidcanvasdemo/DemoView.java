package com.example.androidcanvasdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class DemoView extends View {
    private VipBackground mVipBg = new VipBackground();
    public DemoView(Context context) {
        super(context);
        mVipBg.initDraw(context);
    }

    public DemoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mVipBg.initDraw(context);
    }

    public DemoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mVipBg.initDraw(context);
    }

    public DemoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mVipBg.initDraw(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mVipBg.drawBanner(canvas);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    public static class VipBackground {

        public enum BannerGravity {
            LeftTop,
            RightTop,
            LeftBottom,
            RightBottom,
        }
        public boolean mIsVip = false;
        public BannerGravity mVipBannerGravity = BannerGravity.RightTop;
        public Paint mPaint = new Paint();
        public Path mPath = new Path();
        public Bitmap mBmpBanner;

        protected static final int RADIUS = 15;
        protected static final int BANNER_WIDTH = 120;
        protected static final int BANNER_HEIGHT = 40;
        private static final String VIP_TEXT = "VIP";
        public void initDraw(Context context) {
            final int colorBg = ContextCompat.getColor(context, android.R.color.holo_purple);
            Bitmap bmp = Bitmap.createBitmap(BANNER_WIDTH, BANNER_HEIGHT, Bitmap.Config.ARGB_8888);

            Canvas bannerCanvas = new Canvas(bmp);

            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(colorBg);
            mPaint.setAntiAlias(true);
            mPaint.setStrokeJoin(Paint.Join.ROUND);
            mPaint.setTextSize(20);
            mPaint.setTextAlign(Paint.Align.CENTER);
            float txtW = mPaint.measureText(VIP_TEXT);
            Paint.FontMetrics fm = mPaint.getFontMetrics();
            float txtH = fm.descent + fm.ascent;
//        ColorFilter filter = new PorterDuffColorFilter(colorBlue, PorterDuff.Mode.SRC_IN);
//        mPaint.setColorFilter(filter);
            bannerCanvas.drawRect(0 ,0, BANNER_WIDTH, BANNER_HEIGHT, mPaint);
            mPaint.setColor(Color.WHITE);

            bannerCanvas.drawText(VIP_TEXT, (BANNER_WIDTH-txtW)/2, (BANNER_HEIGHT-txtH)/2, mPaint);

            mBmpBanner = bmp;

        }

        public void drawBanner(Canvas canvas) {

            PointF center = new PointF(BANNER_HEIGHT-10, BANNER_HEIGHT+10);
            Matrix matrix = new Matrix();
            matrix.setRotate(-45, center.x, center.y);


//            canvas.save();
//        canvas.drawPath(mPath, mPaint);
//        canvas.rotate(45);
            canvas.drawBitmap(mBmpBanner, matrix, null);
//        mPaint.setColor(Color.RED);
//        canvas.drawCircle(center.x, center.y, 5, mPaint);
//        canvas.drawBitmap(bmp, getWidth()/2, getHeight()/2, mPaint);
//        canvas.rotate(45);
//        canvas.drawBitmap(vipBmp, srcRect, destRect, mPaint);
//        canvas.drawRect(left, top, right, bottom, mPaint);

//        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
//            canvas.restore();
        }
    }

}
