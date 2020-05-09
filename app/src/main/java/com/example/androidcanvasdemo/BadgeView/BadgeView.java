package com.example.androidcanvasdemo.BadgeView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
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
    public static final int DEFAULT_OFFSET_X_Y = -10;

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
    private BadgeGravity mGravity = BadgeGravity.EndTop;
    private int offsetX = DEFAULT_OFFSET_X_Y;
    private int offsetY = DEFAULT_OFFSET_X_Y;
    private int mTextBgColor = Color.GREEN;
    private int mTextColor = Color.WHITE;
    private Paint.FontMetrics mFontMetrics;
    public BadgeView(Context context) {
        this(context, null);
    }

    public enum BadgeGravity {
        StartTop,
        EndTop,
        StartBottom,
        EndBottom;

        public int toGravity() {
            switch (this) {
                case StartBottom:
                    return Gravity.START | Gravity.BOTTOM;
                case StartTop:
                    return Gravity.START | Gravity.TOP;
                case EndBottom:
                    return Gravity.END | Gravity.BOTTOM;
                case EndTop:
                default:
                    return Gravity.END | Gravity.TOP;
            }
        }
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
        mPaint.setColor(mTextColor);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(getTextSizeInSp(mTextSize));
        mPaint.setTextAlign(Paint.Align.CENTER);

        mCirclePaint = new Paint();
        mCirclePaint.setColor(mTextBgColor);
        mCirclePaint.setStyle(Paint.Style.FILL);
        calculateSize();
    }

    private void calculateSize() {
        mFontMetrics = mPaint.getFontMetrics();
        mPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
        mSize = Math.max(mTextBound.width(), mTextBound.height()) + mTextPadding;

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(mSize, mSize);
        lp.gravity = mGravity.toGravity();
        setLayoutParams(lp);
    }
    private void updateLayoutTarget() {
        if (mLayoutTarget != null) {
            int y = offsetY + mSize / 2;
            int x = offsetX + mSize / 2;
            switch (mGravity) {
                case StartBottom:
                    mLayoutTarget.setPadding(x, 0, 0, y);
                    break;
                case StartTop:
                    mLayoutTarget.setPadding(x, y, 0, 0);
                    break;
                case EndBottom:
                    mLayoutTarget.setPadding(0, 0, x, y);
                    break;
                case EndTop:
                default:
                    mLayoutTarget.setPadding(0, y, x, 0);
                    break;
            }
        }
    }

    public static BadgeView build(Context context) {
        return new BadgeView(context);
    }

    private boolean isParentLayout(View view) {
        return view instanceof LinearLayout ||
                view instanceof ConstraintLayout ||
                view instanceof FrameLayout ||
                view instanceof GridLayout ||
                view instanceof  RelativeLayout;
    }

    public BadgeView setBadgeNumber(int i) {
        return setBadgeText("" + i);
    }
    public BadgeView setBadgeText(String text) {
        mText = "" + text;
        calculateSize();
        invalidate();
        return this;
    }

    public BadgeView setTextColor(int color) {
        mTextColor = color;
        mPaint.setColor(color);
        invalidate();
        return this;
    }
    public BadgeView setTextSizeSp(float size) {
        mTextSize = size;
        mPaint.setTextSize(getTextSizeInSp(size));
        calculateSize();
        updateLayoutTarget();
        invalidate();
        return this;
    }


    public BadgeView setGravity(BadgeGravity gravity) {
        mGravity = gravity;
        calculateSize();
        updateLayoutTarget();
        invalidate();
        return this;
    }

    public BadgeView setTextBgColor(int color) {
        mTextBgColor = color;
        mCirclePaint.setColor(color);
        invalidate();
        return this;
    }

    public BadgeView setOffsetX(int ox) {
        offsetX = ox;
        updateLayoutTarget();
        invalidate();
        return this;
    }
    public BadgeView setOffsetY(int oy) {
        offsetY = oy;
        updateLayoutTarget();
        invalidate();
        return this;
    }

    public BadgeView bind(View target) throws Exception {
         if (mContainer != null) {
             if (target != mTarget) {
                 throw new Exception("target already bind!");
             }
             return this;
         }

        mTarget = target;
        ViewGroup parent = (ViewGroup)target.getParent();
        ViewGroup.LayoutParams tlp = target.getLayoutParams();

        int index = parent.indexOfChild(target);

        BadgeContainer container = new BadgeContainer(getContext());
        parent.removeView(target);
        parent.addView(container, index, tlp);

        LinearLayout llayout = new LinearLayout(getContext());
        llayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        target.setLayoutParams(new LinearLayout.LayoutParams(tlp.width, tlp.height));
        llayout.addView(target);

        container.addView(llayout);
        container.addView(this);
        // need to set id for relative layout
        if (parent instanceof RelativeLayout ||
            parent instanceof ConstraintLayout){
            container.setId(target.getId());
        }

        mContainer = container;
        mLayoutTarget = llayout;
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
        canvas.drawColor(Color.TRANSPARENT);
//        if (TextUtils.isEmpty(mText)) {
//            return;
//        }
        canvas.drawCircle(mSize/2.0F, mSize/2.0F, mSize/2.0F, mCirclePaint);
        canvas.drawText(mText, center, center - mTextBound.exactCenterY() , mPaint);
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
