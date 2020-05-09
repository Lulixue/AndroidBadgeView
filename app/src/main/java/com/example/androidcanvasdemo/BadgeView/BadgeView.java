package com.example.androidcanvasdemo.BadgeView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;


public class BadgeView extends View {
    private static final String TAG = "BadgeView";
    private static final int DEFAULT_SIZE = 16;
    public static final int DEFAULT_OFFSET_X_PX = -10;
    public static final int DEFAULT_OFFSET_Y_PX = -10;
    public static final int DEFAULT_TEXT_PADDING = 20;
    public static final int DEFAULT_TEXT_SIZE_SP = 13;

    private View mTarget;
    private BadgeContainer mContainer;
    private int mSize = DEFAULT_SIZE;
    private String mText = "1";
    private float mTextSize = DEFAULT_TEXT_SIZE_SP;   // SP
    private Paint mPaint;
    private Paint mCirclePaint;
    private int mTextPadding = Math.abs(DEFAULT_TEXT_PADDING);
    private Rect mTextBound = new Rect();
    private ViewGroup.MarginLayoutParams mMarginLayoutParams;
    private BadgeGravity mGravity = BadgeGravity.EndTop;
    private int mOffsetX = DEFAULT_OFFSET_X_PX;
    private int mOffsetY = DEFAULT_OFFSET_Y_PX;
    private int mTextBgColor = Color.GREEN;
    private int mTextColor = Color.WHITE;
    static Handler BadgeHandler;
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
        if (BadgeHandler == null) {
            BadgeHandler = new Handler();
        }
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    private float getTextSizeInSp(float spSize) {
        final float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return spSize * scale;
    }
    private int getSizeInDp(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
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
        mPaint.getTextBounds(mText.isEmpty() ? "1" : mText, 0, mText.length(), mTextBound);
        mSize = Math.min(mTextBound.width(), mTextBound.height()) + mTextPadding * 2;

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(mSize, mSize);
        lp.gravity = mGravity.toGravity();
        setLayoutParams(lp);
    }
    private void updateLayoutTarget() {
        if (mMarginLayoutParams != null) {
            int y = mOffsetY + mSize / 2;
            int x = mOffsetX + mSize / 2;
            x = Math.max(x, 0);
            y = Math.max(y, 0);
            switch (mGravity) {
                case StartBottom:
                    mMarginLayoutParams.setMargins(x, 0, 0, y);
                    break;
                case StartTop:
                    mMarginLayoutParams.setMargins(x, y, 0, 0);
                    break;
                case EndBottom:
                    mMarginLayoutParams.setMargins(0, 0, x, y);
                    break;
                case EndTop:
                default:
                    mMarginLayoutParams.setMargins(0, y, x, 0);
                    break;
            }
            mTarget.setLayoutParams(mMarginLayoutParams);
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

    public void setBadgeNumber(int i) {
        setBadgeText("" + i);
    }


    private void _setBadgeEmptyText() {
        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setInterpolator(new AccelerateDecelerateInterpolator()); //and this
        anim.setDuration(500);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d(TAG, "onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d(TAG, "onAnimationEnd");
                mText = "";
                calculateSize();
                setVisibility(INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.d(TAG, "onAnimationRepeat");

            }
        });
        startAnimation(anim);
    }

    public void setBadgeText(String text) {
        final String newText = "" + text;

        if (TextUtils.isEmpty(newText)) {
            // fade out
            // set value of mText must be done after animation
            _setBadgeEmptyText();
            return;
        } else if (TextUtils.isEmpty(mText)) {
            // fade in
            AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
            anim.setInterpolator(new AccelerateDecelerateInterpolator()); //and this
            anim.setDuration(1000);
            startAnimation(anim);
            setVisibility(VISIBLE);
        }

        mText = newText;
        calculateSize();
        invalidate();
    }

    public BadgeView setTextPadding(int padding, boolean isDp) {
        mTextPadding = (isDp ? getSizeInDp(padding) : padding);
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
        mOffsetX = ox;
        updateLayoutTarget();
        invalidate();
        return this;
    }
    public BadgeView setOffsetY(int oy) {
        mOffsetY = oy;
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

        if (isParentLayout(target)) {
            container.addView(target, new FrameLayout.LayoutParams(tlp.width, tlp.height));
        } else {
            LinearLayout llayout = new LinearLayout(getContext());
            container.addView(llayout, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            llayout.setOrientation(LinearLayout.HORIZONTAL);
            llayout.setBackgroundColor(Color.RED);
            llayout.addView(target, new LinearLayout.LayoutParams(tlp.width, tlp.height));
        }
        mMarginLayoutParams = (ViewGroup.MarginLayoutParams)target.getLayoutParams();
        container.addView(this);

        tlp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        tlp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        parent.addView(container, index, tlp);

        // need to set id for relative layout
        if (parent instanceof RelativeLayout ||
            parent instanceof ConstraintLayout){
            container.setId(target.getId());
        }

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
        super.onDraw(canvas);
        Log.d(TAG, "onDraw");
        drawBadge(canvas);
    }

    private void drawBadge(Canvas canvas) {
        float center = mSize/2.0F;
        canvas.drawColor(Color.TRANSPARENT);
        if (TextUtils.isEmpty(mText)) {
            return;
        }
        canvas.drawCircle(mSize/2.0F, mSize/2.0F, mSize/2.0F, mCirclePaint);
        canvas.drawText(mText, center, center - mTextBound.exactCenterY() , mPaint);
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
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
