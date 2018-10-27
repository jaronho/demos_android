package com.github.mikephil.charting.renderer;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;


/**
 * Created by Philipp Jahoda on 25/01/16.
 */
public abstract class LineRadarRenderer extends LineScatterCandleRadarRenderer {

    public LineRadarRenderer(ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(animator, viewPortHandler);
    }

    /**
     * Draws the provided path in filled mode with the provided drawable.
     *
     * @param c
     * @param filledPath
     * @param drawable
     */
    protected void drawFilledPath(Canvas c, Path filledPath, Drawable drawable) {

        if (clipPathSupported()) {
            c.save();
            c.clipPath(filledPath);

            drawable.setBounds((int) mViewPortHandler.contentLeft(),
                    (int) mViewPortHandler.contentTop(),
                    (int) mViewPortHandler.contentRight(),
                    (int) mViewPortHandler.contentBottom());
            drawable.draw(c);

            c.restore();
        } else {
            throw new RuntimeException("Fill-drawables not (yet) supported below API level 18, " +
                    "this code was run on API level " + Utils.getSDKInt() + ".");
        }
    }

    /**
     * Draws the provided path in filled mode with the provided color and alpha.
     * Special thanks to Angelo Suzuki (https://github.com/tinsukE) for this.
     *
     * @param c
     * @param filledPath
     * @param fillColor
     * @param fillAlpha
     */
    protected void drawFilledPath(Canvas c, Path filledPath, int fillColor, int fillAlpha) {

//        int color = (fillAlpha << 24) | (fillColor & 0xffffff);
        int color = getPaintRender().getColor();
        // TODO: 2018/2/7 mpandroid 绘制线条
        if (clipPathSupported()) {
            c.save();
            c.clipPath(filledPath);
            Paint paint = new Paint();
            LinearGradient linearGradient =
                    new LinearGradient(0, 0, 0, c.getClipBounds().bottom,
                            ColorTemplate.adjustAlpha(color, 120), ColorTemplate.adjustAlpha(color, 255), Shader.TileMode.CLAMP);
            paint.setShader(linearGradient);
//            c.drawColor(color);
            c.drawPaint(paint);
            c.restore();
        } else {
            // save
            Paint.Style previous = mRenderPaint.getStyle();
            int previousColor = mRenderPaint.getColor();

            // set
            mRenderPaint.setStyle(Paint.Style.FILL);
            mRenderPaint.setColor(color);

            c.drawPath(filledPath, mRenderPaint);

            // restore
            mRenderPaint.setColor(previousColor);
            mRenderPaint.setStyle(previous);
        }
    }

    /**
     * Clip path with hardware acceleration only working properly on API level 18 and above.
     *
     * @return
     */
    private boolean clipPathSupported() {
        return Utils.getSDKInt() >= 18;
    }
}
