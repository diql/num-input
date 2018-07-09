package org.diql.numinput;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import org.diql.common.util.DisplayUtil;

/**
 * Created by qinglian on 2018/7/9.
 */
public class NumInputEditText extends android.support.v7.widget.AppCompatEditText {
    private static final String TAG = "NumInputEditText";

    private int mWidth;
    private int mHeight;

    private int mLength;
    private int mItemPadding;

    private Path mPath;
    private Rect[] mRects;

    private Paint mBgPaint;
    private Paint mRoundPaint;
    private int mRoundSize;

    public NumInputEditText(Context context) {
        this(context, null);
    }

    public NumInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLength = 6;
        mItemPadding = DisplayUtil.dip2pxInt(context, 5);
        mRoundSize = DisplayUtil.dip2pxInt(context, 10);

        mPath = new Path();
        mRects = new Rect[mLength];
        for (int i = 0; i < mRects.length; i++) {
            mRects[i] = new Rect();
        }

        mBgPaint = new Paint();
        mBgPaint.setColor(Color.parseColor("#00ff00"));
        mBgPaint.setStrokeWidth(DisplayUtil.dip2pxInt(context, 1));

        mRoundPaint = new Paint();
        mRoundPaint.setColor(Color.BLACK);
        mRoundPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (measuredWidth != mWidth || measuredHeight != mHeight) {
            mWidth = measuredWidth;
            mHeight = measuredHeight;
            resetInputPath(measuredWidth, measuredHeight);
        }
        Log.d(TAG, "onMeasure: width:" + mWidth + ".mHeight:" + mHeight);
    }

    private void resetInputPath(int measuredWidth, int measuredHeight) {
        if (measuredWidth == 0 || measuredHeight == 0) {
            return;
        }
        mPath.addRect(0, 0, measuredWidth, measuredHeight, Path.Direction.CCW);
        int w = (int) ((measuredWidth - mItemPadding * (mLength - 1)) / (double) mLength);
        int i = 0;
        int top = 0;
        int bottom = measuredHeight;
        for (Rect rect : mRects) {
            int left = i * (mItemPadding + w);
            int right = left + w;
            rect.set(left, top, right, bottom);
            i++;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawInputRect(canvas);
        drawInputNum(canvas);
    }

    private void drawInputRect(Canvas canvas) {
        for (Rect rect : mRects) {
            canvas.drawRect(rect, mBgPaint);
        }
    }

    private void drawInputNum(Canvas canvas) {
        int inputSize = getText().length();
        for (int i = 0; i < inputSize && i < mRects.length; i++) {
            Rect rect = mRects[i];
            int cx = (rect.left + rect.right) / 2;
            int cy = (rect.top + rect.bottom) / 2;
            canvas.drawCircle(cx, cy, mRoundSize, mRoundPaint);
        }
    }
}
