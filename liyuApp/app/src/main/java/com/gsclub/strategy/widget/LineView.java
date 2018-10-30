package com.gsclub.strategy.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LineView extends View {

    /**
     * 折线图的宽和高
     */
    private int width, height;
    /**
     * 折线画笔
     */
    private Paint linePaint;
    /**
     * x轴用户设置坐标数值
     */
    private List<Float> datas = new ArrayList<>();
    /**
     * 展示折线图区域宽度
     */
    private float showLineAreaWidth;
    /**
     * 展示折线图区域高度
     */
    private float showLineAreaHeight;
    /**
     * 折线颜色
     */
    private int lineColor = 0xff2791dc;
    /**
     * 折线粗细层度
     */
    private final float lindWidth = 0.6f;
    private float xGap;
    private Float min;
    private Float max;

    public LineView(Context context) {
        this(context, null);
    }

    public LineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    public void setLineColor(int lineColor) {
        linePaint.setColor(lineColor);
        invalidate();
    }

    private void initPaint() {
        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setDither(true);
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth((float) dp2px(this.getContext(), lindWidth));
        linePaint.setStyle(Paint.Style.FILL);
    }

    public void setData(List<Float> datas) {
        if (datas == null || datas.size() == 0)
            return;
        this.datas.clear();
        this.datas.addAll(datas);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        height = MeasureSpec.getSize(heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        long startTime = System.currentTimeMillis(), endTime;

        showLineAreaWidth = width;
        showLineAreaHeight = height;

        canvas.save();
        canvas.translate(width - showLineAreaWidth, showLineAreaHeight);
        xGap = showLineAreaWidth / (/*xList.size()*/242 + 1);
//        max = getUpOrDownNum(getMaxOrMinNum(yList, true), 0, true);
        max = getMaxOrMinNum(datas, true);
//        min = getUpOrDownNum(getMaxOrMinNum(yList, false), 0, false);
        min = getMaxOrMinNum(datas, false);
        canvas.restore();

        drawPointAndLine(canvas);

//        endTime = System.currentTimeMillis();
//        Log.e("draw lineview", this.hashCode() + "  draw time : " + (endTime - startTime));
    }


    private static float points[] = new float[242 * 4];
    @SuppressLint("Range")
    private void drawPointAndLine(final Canvas canvas) {
        if (datas == null || datas.size() == 0) {
            return;
        }

        float nx = -1f, ny = -1f, lx = -1f, ly = -1f;
        for (int i = 0; i < datas.size(); i++) {
            if (i <= datas.size() - 2 && i <= 242) {
                if (nx == -1f) {
                    nx = getXPix(i);
                }

                if (ny == -1f) {
                    ny = getYPix(datas.get(i));
                }

                lx = getXPix(i + 1);
                ly = getYPix(datas.get(i + 1));
                points[i * 4] = nx;
                points[i * 4 + 1] = ny;
                points[i * 4 + 2] = lx;
                points[i * 4 + 3] = ly;

                nx = lx;
                ny = ly;
            }
        }

        canvas.drawLines(points, 0, points.length, linePaint);

        datas.clear();

    }

    public Float getYPix(Float y) {
        return showLineAreaHeight - ((y - min) / (max - min)) * (showLineAreaHeight /*- yGap*/);
    }

    public Float getXPix(int i) {
        return width - showLineAreaWidth + xGap * (i + 1);
    }

    private Float getMaxOrMinNum(List<Float> list, boolean isMax) {
        if (list == null || list.size() == 0)
            return 0.f;
        return isMax ? Collections.max(list) : Collections.min(list);
    }

    private int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    private int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

}