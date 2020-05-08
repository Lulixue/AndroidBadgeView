package com.example.androidcanvasdemo.BadgeView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.lang.reflect.MalformedParameterizedTypeException;

public class BadgeView extends View {
    private static final String TAG = "BadgeView";
    private static final int DEFAULT_SIZE = 16;
    private static final int DEFAULT_GRAVITY = Gravity.TOP | Gravity.END;

    private View mTarget;
    private BadgeContainer mContainer;
    private int mSize = DEFAULT_SIZE;
    private String mText = "1";
    private float mTextSize = 13;   // SP
    private Paint mPaint;
    private Paint mCirclePaint;
    private int mTextPadding = 20;
    private Rect mTextBound = new Rect();
    private LinearLayout mLayoutTarget;
    private int mGravity = DEFAULT_GRAVITY;
    private int offsetX = -mTextPadding/2;
    private int offsetY = -mTextPadding/2;
    public BadgeView(Context context) {
        this(context, null);
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    private float getTextSizeInSp(float spSize) {
        final float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return spSize * scale;
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(getTextSizeInSp(mTextSize));
        mPaint.setTextAlign(Paint.Align.CENTER);

        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.GREEN);
        mCirclePaint.setStyle(Paint.Style.FILL);
        calculateSize();
    }

    private void calculateSize() {
        mPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
        mSize = Math.max(mTextBound.width(), mTextBound.height()) + mTextPadding;

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(mSize, mSize);
        lp.gravity = mGravity;
        setLayoutParams(lp);

    }
    private void updateLayoutTarget() {
        mLayoutTarget.setPadding(0, offsetY + mSize/2, offsetX + mSize/2, 0);
    }

    public static BadgeView build(Context context) {
        return new BadgeView(context);
    }

    private static final int MIN_WIDTH = 20;
    private static final int MIN_HEIGHT = 20;
    private boolean isParentLayout(View view) {
        return view instanceof LinearLayout ||
                view instanceof ConstraintLayout ||
                view instanceof FrameLayout ||
                view instanceof GridLayout ||
                view instanceof  RelativeLayout;
    }

    public void setBadgeNumber(int i) {
        setBadgeText("" + i);
    }
    public void setBadgeText(String text) {
        mText = "" + text;
        calculateSize();
        invalidate();
    }

    public BadgeView bind(View target) throws Exception {
         if (mContainer != null) {
             if (target != mTarget) {
                 throw new Exception("change binding not supported!");
             }
             return this;
         }

        mTarget = target;
        ViewGroup parent = (ViewGroup)target.getParent();
        ViewGroup.LayoutParams tlp = target.getLayoutParams();

        int index = parent.indexOfChild(target);

        BadgeContainer container = new BadgeContainer(getContext());
        parent.removeViewAt(index);
        parent.addView(container, index, tlp);

//        if (isParentLayout(target)) {
//            container.addView(target);
//        } else {
            LinearLayout llayout = new LinearLayout(getContext());
            container.addView(llayout, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            llayout.addView(target, tlp);
            LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) target.getLayoutParams();
            llp.setMargins(0, 0, 0, 0);
            target.setLayoutParams(llp);

            mLayoutTarget = llayout;
//        }
        container.addView(this);
        mContainer = container;
        updateLayoutTarget();
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = this.getMeasuredHeight();
        int width = this.getMeasuredWidth();

        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);

        Log.d(TAG, "badge: " + height + "," + width);
        setMeasuredDimension(mSize, mSize);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "onLayout");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBadge(canvas);
        super.onDraw(canvas);
        Log.d(TAG, "onDraw");
    }

    private void drawBadge(Canvas canvas) {
        float center = mSize/2.0F;
//        canvas.drawColor(Color.RED);
        canvas.drawCircle(mSize/2.0F, mSize/2.0F, mSize/2.0F, mCirclePaint);
        canvas.drawText(mText, center, center + mTextBound.height() * 0.5F , mPaint);
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        drawBadge(canvas);
        super.onDrawForeground(canvas);
        Log.d(TAG, "onDrawForeground");
    }

    static class BadgeContainer extends FrameLayout {

        FrameLayout.LayoutParams mLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        public BadgeContainer(@NonNull Context context) {
            super(context);
            setLayoutParams(mLayoutParams);
        }

        public BadgeContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public BadgeContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public BadgeContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }
    }
}
