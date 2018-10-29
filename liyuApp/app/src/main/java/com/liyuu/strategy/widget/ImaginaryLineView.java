package com.liyuu.strategy.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.liyuu.strategy.R;

/**
 * 虚线
 */
public class ImaginaryLineView extends View {

    private Context context;
    private Paint mPaint;
    private Path mPath;
    private PathEffect effects;
    private int width;
    private int height;
    private int defaultColor = 0xffff0000;

    public ImaginaryLineView(Context context) {
        this(context, null);
    }

    public ImaginaryLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ImaginaryLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImaginaryLineView);
        defaultColor = typedArray.getColor(R.styleable.ImaginaryLineView_imaginary_line_color, defaultColor);
        typedArray.recycle();

        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private void init() {
        //初始化，并打开抗锯齿
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(defaultColor);
        mPaint.setStrokeWidth(dip2px(context, 1));
        mPath = new Path();
        //数组含义：里面最少要有2个值，值的个数必须是偶数个。偶数位（包含0），表示实线长度，奇数位表示断开的长度
        effects = new DashPathEffect(new float[]{4, 2}, 0);
    }

    /**
     * 设置线的必要属性
     *
     * @param color     十六进制颜色值
     * @param lineWidth 虚线宽度，单位是dp
     */
    public void setLineAttribute(int color, float lineWidth, float[] f) {

        if (color == 0) {
            color = defaultColor;
        }
        if (lineWidth == 0) {
            lineWidth = 1;
        }
        if (f == null) {
            f = new float[]{4, 2};
        }
        effects = new DashPathEffect(f, 0);
        mPaint.setStrokeWidth(dip2px(context, lineWidth));
        mPaint.setColor(color);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //定义起点
        mPath.moveTo(0, 0);
        //定义终点
        if (width > height) {
            //宽度比高度大，是横线
            mPath.lineTo(width, 0);
        } else {
            //竖线。（根据实际情况，这里不考虑宽高相等情况）
            mPath.lineTo(0, height);
        }
        mPaint.setPathEffect(effects);
        canvas.drawPath(mPath, mPaint);
    }

    private static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
