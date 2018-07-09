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

    private Rect[] mRects;

    private Paint mBgPaint;
    private Paint mRoundPaint;
    private int mRoundSize;
    private int mStokeWidth;

    public NumInputEditText(Context context) {
        this(context, null);
    }

    public NumInputEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLength = 6;
        mItemPadding = DisplayUtil.dip2pxInt(context, 5);
        mRoundSize = DisplayUtil.dip2pxInt(context, 10);
        mStokeWidth = DisplayUtil.dip2pxInt(context, 1);

        mRects = new Rect[mLength];
        for (int i = 0; i < mRects.length; i++) {
            mRects[i] = new Rect();
        }

        mBgPaint = new Paint();
        mBgPaint.setColor(Color.GRAY);
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setStrokeWidth(mStokeWidth);

        mRoundPaint = new Paint();
        mRoundPaint.setColor(Color.BLACK);
        mRoundPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        mWidth = measuredWidth;
        mHeight = measuredHeight;
        Log.d(TAG, "onMeasure: width:" + mWidth + ".mHeight:" + mHeight);
    }

    private void resetInputPath(int width, int height) {
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        width = width - paddingLeft - paddingRight;
        height = height - paddingTop - paddingBottom;
        int w = (int) ((width - mItemPadding * (mLength - 1)) / (double) mLength);
        int i = 0;
        int halfStokeWidth = mStokeWidth / 2;
        int top = getPaddingTop() + halfStokeWidth;
        int bottom = height - getPaddingBottom() - halfStokeWidth;
        for (Rect rect : mRects) {
            int left = paddingLeft + i * (mItemPadding + w) + halfStokeWidth;
            int right = left + w - halfStokeWidth;
            rect.set(left, top, right, bottom);
            i++;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        resetInputPath(mWidth, mHeight);
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
