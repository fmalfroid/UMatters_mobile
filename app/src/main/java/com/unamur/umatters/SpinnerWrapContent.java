package com.unamur.umatters;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;

public class SpinnerWrapContent extends AppCompatSpinner {
    private boolean inOnMeasure;

    public SpinnerWrapContent(Context context) {
        super(context);
    }

    public SpinnerWrapContent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpinnerWrapContent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        inOnMeasure = true;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        inOnMeasure = false;
    }

    public boolean isInOnMeasure() {
        return inOnMeasure;
    }
}
