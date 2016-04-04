
package com.ivan.circleprogressview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CircleProgressView extends View {
    private static final String TAG = "CircleTimerView";

    // Status
    private static final String INSTANCE_STATUS = "instance_status";
    private static final String STATUS_RADIAN = "status_radian";

    // Default dimension in dp/pt
    private static final int DEFAULT_GAP_BETWEEN_CIRCLE_AND_LINE = 5;
    private static final int DEFAULT_LINE_LENGTH = 10;
    private static final int DEFAULT_LINE_WIDTH = 1;
    private static final int DEFAULT_CIRCLE_STROKE_WIDTH = 5;
    private static final int DEFAULT_MIDDLE_TEXT_SIZE = 60;
    private static final int DEFAULT_TOP_TEXT_SIZE = 40;
    private static final int DEFAULT_BOTTOM_TEXT_SIZE = 40;


    // Default color
    private static final int DEFAULT_CIRCLE_COLOR = 0xFFB6B6B6;
    private static final int DEFAULT_LINE_COLOR = 0xFFB6B6B6;
    private static final int DEFAULT_HIGHLIGHT_LINE_COLOR = 0xFF242424;
    private static final int DEFAULT_MIDDLE_TEXT_COLOR = 0xFF000000;
    private static final int DEFAULT_TOP_TEXT_COLOR = 0xFF000000;
    private static final int DEFAULT_BOTTOM_TEXT_COLOR = 0xFF000000;

    private static final int DEFAULT_CIRCLE_HIGHLIGHT_COLOR = 0xFF242424;

    private static final int DEFAULT_LINE_COUNT = 180;

    private static final float DEFAULT_TARGET = 100.0f;


    // Paint
    private Paint circlePaint;
    private Paint highlightLinePaint;
    private Paint linePaint;
    private Paint middleTextPaint;
    private Paint topTextPaint;
    private Paint bottomTextPaint;
    private Paint progressOvalPaint;

    // Dimension
    private int gapBetweenCircleAndLine;
    private int lineLength;
    private float lineWidth;

    // Color
    private int circleColor;
    private int lineColor;
    private int lineHighlightColor;
    private int circleHighlightColor;

    // Parameters
    private float cx;
    private float cy;
    private float radius;
    private float currentRadian = 0;


    private float target = DEFAULT_TARGET;

    private float currentCount;

    private int lineCount;

    private RectF mProgressOval;

    private boolean middleTextVisible;
    private int middleTextSize;
    private int middleTextColor;

    private boolean topTextVisible;
    private int topTextSize;
    private int topTextColor;
    private String topText;

    private boolean bottomTextVisible;
    private int bottomTextSize;
    private int bottomTextColor;
    private String bottomText;


    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context) {
        this(context, null);
    }

    private void initialize(Context context, AttributeSet attrs) {
        Log.d(TAG, "initialize");

        mProgressOval = new RectF();

        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressView, 0, 0);

        lineCount = t.getInt(R.styleable.CircleProgressView_lineCount, DEFAULT_LINE_COUNT);
        lineLength = t.getDimensionPixelSize(R.styleable.CircleProgressView_lineLength, DEFAULT_LINE_LENGTH);
        lineColor = t.getColor(R.styleable.CircleProgressView_lineColor, DEFAULT_LINE_COLOR);
        lineHighlightColor = t.getColor(R.styleable.CircleProgressView_lineHighlightColor, DEFAULT_HIGHLIGHT_LINE_COLOR);
        lineWidth = t.getDimensionPixelSize(R.styleable.CircleProgressView_lineWidth, DEFAULT_LINE_WIDTH);
        circleColor = t.getColor(R.styleable.CircleProgressView_circleColor, DEFAULT_CIRCLE_COLOR);
        gapBetweenCircleAndLine = t.getDimensionPixelSize(R.styleable.CircleProgressView_gapBetweenCircleAndLine,
                DEFAULT_GAP_BETWEEN_CIRCLE_AND_LINE);
        circleHighlightColor = t.getColor(R.styleable.CircleProgressView_circleHighlightColor, DEFAULT_CIRCLE_HIGHLIGHT_COLOR);
        middleTextVisible = t.getBoolean(R.styleable.CircleProgressView_middleTextVisible, true);
        middleTextSize = t.getDimensionPixelSize(R.styleable.CircleProgressView_middleTextSize, DEFAULT_MIDDLE_TEXT_SIZE);
        middleTextColor = t.getColor(R.styleable.CircleProgressView_middleTextColor, DEFAULT_MIDDLE_TEXT_COLOR);
        topTextColor = t.getColor(R.styleable.CircleProgressView_topTextColor, DEFAULT_TOP_TEXT_COLOR);
        topTextSize = t.getDimensionPixelSize(R.styleable.CircleProgressView_topTextSize, DEFAULT_TOP_TEXT_SIZE);
        topTextVisible = t.getBoolean(R.styleable.CircleProgressView_topTextVisible, false);
        topText = t.getString(R.styleable.CircleProgressView_topText);
        bottomText = t.getString(R.styleable.CircleProgressView_bottomText);
        bottomTextVisible = t.getBoolean(R.styleable.CircleProgressView_bottomTextVisible, false);
        bottomTextColor = t.getColor(R.styleable.CircleProgressView_bottomTextColor, DEFAULT_BOTTOM_TEXT_COLOR);
        bottomTextSize = t.getDimensionPixelSize(R.styleable.CircleProgressView_bottomTextSize, DEFAULT_BOTTOM_TEXT_SIZE);


        // Init all paints
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlightLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        middleTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressOvalPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        topTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bottomTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


        // CirclePaint
        circlePaint.setColor(circleColor);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(DEFAULT_CIRCLE_STROKE_WIDTH);


        // LinePaint
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(lineWidth);

        // HighlightLinePaint
        highlightLinePaint.setColor(lineHighlightColor);
        highlightLinePaint.setStrokeWidth(lineWidth);

        // Middle text Paint
        middleTextPaint.setColor(middleTextColor);
        middleTextPaint.setTextSize(middleTextSize);
        middleTextPaint.setTextAlign(Paint.Align.CENTER);

        // Top text Paint
        topTextPaint.setColor(topTextColor);
        topTextPaint.setTextSize(topTextSize);
        topTextPaint.setTextAlign(Paint.Align.CENTER);

        // Bottom text Paint
        bottomTextPaint.setColor(bottomTextColor);
        bottomTextPaint.setTextSize(bottomTextSize);
        bottomTextPaint.setTextAlign(Paint.Align.CENTER);

        //Progress Paint;
        progressOvalPaint.setStyle(Paint.Style.STROKE);
        progressOvalPaint.setStrokeWidth(DEFAULT_CIRCLE_STROKE_WIDTH + 5);
        progressOvalPaint.setColor(circleHighlightColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Content
        canvas.drawCircle(cx, cy, radius - lineLength - gapBetweenCircleAndLine, circlePaint);


        if (currentRadian != 0) {
            mProgressOval.set(
                    gapBetweenCircleAndLine + lineLength,
                    gapBetweenCircleAndLine + lineLength,
                    cx + radius - gapBetweenCircleAndLine - lineLength,
                    cy + radius - gapBetweenCircleAndLine - lineLength);
            canvas.drawArc(mProgressOval, -90, (float) Math.toDegrees(currentRadian), false, progressOvalPaint);
        }

        canvas.save();
        for (int i = 0; i < lineCount; i++) {
            canvas.save();
            canvas.rotate(360.0f / lineCount * i, cx, cy);
            if (360.0f / lineCount * i < Math.toDegrees(currentRadian)) {

                canvas.drawLine(cx, getMeasuredHeight() / 2 - radius + DEFAULT_CIRCLE_STROKE_WIDTH / 2, cx,
                        getMeasuredHeight() / 2 - radius + DEFAULT_CIRCLE_STROKE_WIDTH / 2 + lineLength,
                        highlightLinePaint);
            } else {
                canvas.drawLine(cx, getMeasuredHeight() / 2 - radius + DEFAULT_CIRCLE_STROKE_WIDTH / 2, cx,
                        getMeasuredHeight() / 2 - radius + DEFAULT_CIRCLE_STROKE_WIDTH / 2 + lineLength,
                        linePaint);
//                }
            }
            canvas.restore();
        }
        canvas.save();

        if (middleTextVisible) {
            canvas.drawText("" + currentCount, cx, cy
                    + getFontHeight(middleTextPaint) / 2, middleTextPaint);
        }

        if (topTextVisible && topText != null) {
            canvas.drawText(topText, cx, (cy - (radius - gapBetweenCircleAndLine) / 2)
                    + getFontHeight(topTextPaint) / 2, topTextPaint);
        }

        if (bottomTextVisible && bottomText != null) {
            canvas.drawText(bottomText, cx, cy + (radius - gapBetweenCircleAndLine) / 2, bottomTextPaint);
        }

        canvas.restore();

        super.onDraw(canvas);
    }

    private float getFontHeight(Paint paint) {
        // FontMetrics sF = paint.getFontMetrics();
        // return sF.descent - sF.ascent;
        Rect rect = new Rect();
        paint.getTextBounds("1", 0, 1, rect);
        return rect.height();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG, "onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // Ensure width = height
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        this.cx = width / 2;
        this.cy = height / 2;
        // Radius
        this.radius = width / 2 - DEFAULT_CIRCLE_STROKE_WIDTH / 2;
        setMeasuredDimension(width, height);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Log.d(TAG, "onSaveInstanceState");
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
        bundle.putFloat(STATUS_RADIAN, currentRadian);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Log.d(TAG, "onRestoreInstanceState");
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
            currentRadian = bundle.getFloat(STATUS_RADIAN);
            currentCount = (float) (60 / (2 * Math.PI) * currentRadian * 60);
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public void setCurrentCount(float currentCount) {
        if (currentCount < 0) {
            Log.e(TAG, "currentCount can't less more 0");
        }

        this.currentCount = currentCount;

        if (currentCount < target) {
            currentRadian = (float) (currentCount * (2 * Math.PI) / target);
        } else {
            currentRadian = (float) (target * (2 * Math.PI) / target);
        }

        invalidate();
    }

    public void setTarget(float target) {
        this.target = target;
        invalidate();
    }

    public float getTarget() {
        return target;
    }

    public float getCurrentCount() {
        return currentCount;
    }

    public void setMiddleTextColor(int color) {
        middleTextPaint.setColor(color);
        invalidate();
    }

    public void setHighLightLineColor(int color) {
        highlightLinePaint.setColor(color);
        invalidate();
    }

    public void setLineColor(int color) {
        linePaint.setColor(color);
        invalidate();
    }

    public void setCircleColor(int color) {
        circlePaint.setColor(color);
        invalidate();
    }

    public void setProgressOvalColor(int color) {
        progressOvalPaint.setColor(color);
    }
}
