package com.example.androidcanvasdemo.BadgeView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class BadgeView extends View {
    private View mTarget;

    public BadgeView(Context context) {
        super(context);
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public static BadgeView build(Context context) {
        return new BadgeView(context);
    }

    public BadgeView bind(View target) {
         mTarget = target;

         return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = mTarget.getMeasuredHeight();
        int width = mTarget.getMeasuredWidth();

    }
}
