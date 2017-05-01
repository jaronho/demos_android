package com.nongyi.nylive.utils;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import com.nongyi.nylive.R;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 飘心路径动画器
 */
public class HeartPathAnimator {
    private final Random mRandom;
    protected final Config mConfig;

    private final AtomicInteger mCounter = new AtomicInteger(0);
    private Handler mHandler;

    public HeartPathAnimator(Config config) {
        mConfig = config;
        mRandom = new Random();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public float randomRotation() {
        return mRandom.nextFloat() * 28.6F - 14.3F;
    }

    public Path createPath(AtomicInteger counter, View view, int factor) {
        Random r = mRandom;
        int x = r.nextInt(mConfig.xRand);
        int x2 = r.nextInt(mConfig.xRand);
        int y = view.getHeight() - mConfig.initY;
        int y2 = counter.intValue() * 15 + mConfig.animLength * factor + r.nextInt(mConfig.animLengthRand);
        factor = y2 / mConfig.bezierFactor;
        x = mConfig.xPointFactor + x;
        x2 = mConfig.xPointFactor + x2;
        int y3 = y - y2;
        y2 = y - y2 / 2;
        Path p = new Path();
        p.moveTo(mConfig.initX, y);
        p.cubicTo(mConfig.initX, y - factor, x, y2 + factor, x, y2);
        p.moveTo(x, y2);
        p.cubicTo(x, y2 - factor, x2, y3 + factor, x2, y3);
        return p;
    }

    public void start(final View child, final ViewGroup parent) {
        parent.addView(child, new ViewGroup.LayoutParams(mConfig.heartWidth, mConfig.heartHeight));
        FloatAnimation anim = new FloatAnimation(createPath(mCounter, parent, 2), randomRotation(), parent, child);
        anim.setDuration(mConfig.animDuration);
        anim.setInterpolator(new LinearInterpolator());
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        parent.removeView(child);
                    }
                });
                mCounter.decrementAndGet();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
                mCounter.incrementAndGet();
            }
        });
        anim.setInterpolator(new LinearInterpolator());
        child.startAnimation(anim);
    }

    private static float scale(double a, double b, double c, double d, double e) {
        return (float) ((a - b) / (c - b) * (e - d) + d);
    }

    public static class Config {
        public int initX;
        public int initY;
        public int xRand;
        public int animLengthRand;
        public int bezierFactor;
        public int xPointFactor;
        public int animLength;
        public int heartWidth;
        public int heartHeight;
        public int animDuration;

        static public Config fromTypeArray(TypedArray typedArray, float x, float y, int pointx, int heartWidth, int heartHeight) {
            Config config = new Config();
            Resources res = typedArray.getResources();
            config.initX = (int) typedArray.getDimension(R.styleable.HeartLayout_initX,
                    x);
            config.initY = (int) typedArray.getDimension(R.styleable.HeartLayout_initY,
                    y);
            config.xRand = (int) typedArray.getDimension(R.styleable.HeartLayout_xRand,
                    res.getDimensionPixelOffset(R.dimen.heart_anim_bezier_x_rand));
            config.animLength = (int) typedArray.getDimension(R.styleable.HeartLayout_animLength,
                    res.getDimensionPixelOffset(R.dimen.heart_anim_length));//动画长度
            config.animLengthRand = (int) typedArray.getDimension(R.styleable.HeartLayout_animLengthRand,
                    res.getDimensionPixelOffset(R.dimen.heart_anim_length_rand));
            config.bezierFactor = typedArray.getInteger(R.styleable.HeartLayout_bezierFactor,
                    res.getInteger(R.integer.heart_anim_bezier_factor));
            config.xPointFactor = pointx;
//            config.heartWidth = (int) typedArray.getDimension(R.styleable.HeartLayout_heart_width,
//                    res.getDimensionPixelOffset(R.dimen.heart_size_width));//动画图片宽度
//            config.heartHeight = (int) typedArray.getDimension(R.styleable.HeartLayout_heart_height,
//                    res.getDimensionPixelOffset(R.dimen.heart_size_height));//动画图片高度
            config.heartWidth = heartWidth;
            config.heartHeight = heartHeight;
            config.animDuration = typedArray.getInteger(R.styleable.HeartLayout_anim_duration,
                    res.getInteger(R.integer.anim_duration));//持续期
            return config;
        }
    }

    static class FloatAnimation extends Animation {
        private PathMeasure mPm;
        private View mView;
        private float mDistance;
        private float mRotation;

        public FloatAnimation(Path path, float rotation, View parent, View child) {
            mPm = new PathMeasure(path, false);
            mDistance = mPm.getLength();
            mView = child;
            mRotation = rotation;
            parent.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        @Override
        protected void applyTransformation(float factor, Transformation transformation) {
            Matrix matrix = transformation.getMatrix();
            mPm.getMatrix(mDistance * factor, matrix, PathMeasure.POSITION_MATRIX_FLAG);
            mView.setRotation(mRotation * factor);
            float scale = 1F;
            if (3000.0F * factor < 200.0F) {
                scale = scale(factor, 0.0D, 0.06666667014360428D, 0.20000000298023224D, 1.100000023841858D);
            } else if (3000.0F * factor < 300.0F) {
                scale = scale(factor, 0.06666667014360428D, 0.10000000149011612D, 1.100000023841858D, 1.0D);
            }
            mView.setScaleX(scale);
            mView.setScaleY(scale);
            transformation.setAlpha(1.0F - factor);
        }
    }
}

