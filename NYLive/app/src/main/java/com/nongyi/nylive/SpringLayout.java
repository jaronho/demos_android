package com.nongyi.nylive;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

import static android.support.v4.widget.ViewDragHelper.INVALID_POINTER;

/**
 * Author:  jaron.ho
 * Date:    2017-04-19
 * Brief:   SpringLayout
 */

public class SpringLayout extends RelativeLayout implements NestedScrollingChild {
    public final static int HORIZONTAL = 0x01;
    public final static int VERTICAL = 0x02;
    private int mSpringDirect = VERTICAL;
    private NestedScrollingChildHelper mChildHelper = null;
    /* 手势事件相关 */
    private final int TOUCH_SLOP = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    private final int MAX_FLING_VELOCITY = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();
    private final int MIN_FLING_VELOCITY = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
    private final int TOUCH_SLOP_SQUARE = TOUCH_SLOP * TOUCH_SLOP;
    private VelocityTracker mVelocityTracker = null;
    private float mLastFocusX = 0;
    private float mLastFocusY = 0;
    private float mDownFocusX = 0;
    private float mDownFocusY = 0;
    private MotionEvent mCurrentDownEvent = null;
    private boolean mAlwaysInTapRegion = false;
    private float mVelocityX = 0;
    private float mVelocityY = 0;
    /* 嵌套事件相关 */
    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];
    private final int[] mNestedOffsets = new int[2];
    private int mActivePointerId = INVALID_POINTER;
    private int mLastTouchX = 0;
    private int mLastTouchY = 0;
    private boolean mIsBeingDragged = false;
    /* 其他变量 */
    private RelativeLayout mHeaderLayout;
    private RelativeLayout mFooterLayout;
    private Creator mHeaderCreator;
    private Creator mFooterCreator;
    private float mMaxHeaderSize;
    private float mMaxFooterSize;
    private float mHeaderSize;
    private float mFooterSize;

    public SpringLayout(Context context) {
        this(context, null);
    }

    public SpringLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpringLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        initialize();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SpringLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        initialize();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        return handleGesture(e, new GestureListener() {
            @Override
            public void onDown(MotionEvent downEvent) {
                Log.d("SpringLayout", "onDown");
            }

            @Override
            public void onScroll(MotionEvent downEvent, MotionEvent moveEvent, float distanceX, float distanceY) {
                Log.d("SpringLayout", "onScroll => distanceX: "+distanceX+", distanceY: "+distanceY+", velocityX: "+mVelocityX+", velocityY: "+mVelocityY);
            }

            @Override
            public void onFling(MotionEvent downEvent, MotionEvent upEvent, float velocityX, float velocityY) {
                Log.d("SpringLayout", "onFling => velocityX: "+velocityX+", velocityY: "+velocityY);
            }

            @Override
            public void onUp(MotionEvent upEvent, boolean isFling) {
                Log.d("SpringLayout", "onUp => isFling: "+String.valueOf(isFling));
            }
        }) || handleNestedScroll(e) || super.dispatchTouchEvent(e);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return super.onTouchEvent(e);
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    // 处理手势
    private boolean handleGesture(MotionEvent ev, GestureListener listener) {
        int action = ev.getAction();
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);
        boolean pointerUp = MotionEvent.ACTION_POINTER_UP == (action & MotionEvent.ACTION_MASK);
        int skipIndex = pointerUp ? ev.getActionIndex() : -1;
        // Determine focal point
        float sumX = 0;
        float sumY = 0;
        int count = ev.getPointerCount();
        for (int i = 0; i < count; ++i) {
            if (skipIndex != i) {
                sumX += ev.getX(i);
                sumY += ev.getY(i);
            }
        }
        int div = pointerUp ? count - 1 : count;
        float focusX = sumX / div;
        float focusY = sumY / div;
        switch (action & MotionEvent.ACTION_MASK) {
        case MotionEvent.ACTION_POINTER_DOWN:
            mDownFocusX = mLastFocusX = focusX;
            mDownFocusY = mLastFocusY = focusY;
            return true;
        case MotionEvent.ACTION_POINTER_UP:
            mDownFocusX = mLastFocusX = focusX;
            mDownFocusY = mLastFocusY = focusY;
            // Check the dot product of current velocities.
            // If the pointer that left was opposing another velocity vector, clear.
            mVelocityTracker.computeCurrentVelocity(1000, MAX_FLING_VELOCITY);
            int upIndex = ev.getActionIndex();
            int id1 = ev.getPointerId(upIndex);
            float x1 = mVelocityTracker.getXVelocity(id1);
            float y1 = mVelocityTracker.getYVelocity(id1);
            for (int i = 0; i < count; ++i) {
                if (i != upIndex) {
                    int id2 = ev.getPointerId(i);
                    float x = x1 * mVelocityTracker.getXVelocity(id2);
                    float y = y1 * mVelocityTracker.getYVelocity(id2);
                    final float dot = x + y;
                    if (dot < 0) {
                        mVelocityTracker.clear();
                        break;
                    }
                }
            }
            return true;
        case MotionEvent.ACTION_DOWN:
            mDownFocusX = mLastFocusX = focusX;
            mDownFocusY = mLastFocusY = focusY;
            if (null != mCurrentDownEvent) {
                mCurrentDownEvent.recycle();
            }
            mCurrentDownEvent = MotionEvent.obtain(ev);
            mAlwaysInTapRegion = true;
            if (null != listener) {
                listener.onDown(ev);
            }
            return true;
        case MotionEvent.ACTION_MOVE:
            float scrollX = mLastFocusX - focusX;
            float scrollY = mLastFocusY - focusY;
            if (mAlwaysInTapRegion) {
                final int deltaX = (int)(focusX - mDownFocusX);
                final int deltaY = (int)(focusY - mDownFocusY);
                int distance = (deltaX * deltaX) + (deltaY * deltaY);
                if (distance > TOUCH_SLOP_SQUARE) {
                    if (null != listener) {
                        listener.onScroll(mCurrentDownEvent, ev, scrollX, scrollY);
                    }
                    mLastFocusX = focusX;
                    mLastFocusY = focusY;
                    mAlwaysInTapRegion = false;
                }
            } else if ((Math.abs(scrollX) >= 1) || (Math.abs(scrollY) >= 1)) {
                if (null != listener) {
                    listener.onScroll(mCurrentDownEvent, ev, scrollX, scrollY);
                }
                mLastFocusX = focusX;
                mLastFocusY = focusY;
            }
            return true;
        case MotionEvent.ACTION_UP:
            int pointerId = ev.getPointerId(0);
            mVelocityTracker.computeCurrentVelocity(1000, MAX_FLING_VELOCITY);
            mVelocityX = mVelocityTracker.getXVelocity(pointerId);
            mVelocityY = mVelocityTracker.getYVelocity(pointerId);
            boolean isFling = false;
            if ((Math.abs(mVelocityY) > MIN_FLING_VELOCITY) || (Math.abs(mVelocityX) > MIN_FLING_VELOCITY)) {
                if (null != listener) {
                    listener.onFling(mCurrentDownEvent, ev, mVelocityX, mVelocityY);
                }
                isFling = true;
            }
            if (null != listener) {
                listener.onUp(ev, isFling);
            }
            if (null != mVelocityTracker) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
            return true;
        case MotionEvent.ACTION_CANCEL:
            mAlwaysInTapRegion = false;
            if (null != mVelocityTracker) {
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            }
            return true;
        }
        return false;
    }

    // 处理嵌套滚动
    private boolean handleNestedScroll(MotionEvent e) {
        MotionEvent vtev = MotionEvent.obtain(e);
        int action = MotionEventCompat.getActionMasked(e);
        int actionIndex = MotionEventCompat.getActionIndex(e);
        if (MotionEvent.ACTION_DOWN == action) {
            mNestedOffsets[0] = mNestedOffsets[1] = 0;
        }
        vtev.offsetLocation(mNestedOffsets[0], mNestedOffsets[1]);
        switch (action) {
            case MotionEventCompat.ACTION_POINTER_DOWN:
                mActivePointerId = e.getPointerId(actionIndex);
                mLastTouchX = (int)e.getX(actionIndex);
                mLastTouchY = (int)e.getY(actionIndex);
                break;
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = e.getPointerId(0);
                mLastTouchX = (int)e.getX();
                mLastTouchY = (int)e.getY();
                if (HORIZONTAL == mSpringDirect) {
                    startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL);
                } else if (VERTICAL == mSpringDirect) {
                    startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int index = e.findPointerIndex(mActivePointerId);
                if (index < 0) {
                    return false;
                }
                int x = (int)e.getX(index);
                int y = (int)e.getY(index);
                int dx = mLastTouchX - x;
                int dy = mLastTouchY - y;
                if (dispatchNestedPreScroll(dx, dy, mScrollConsumed, mScrollOffset)) {
                    dx -= mScrollConsumed[0];
                    dy -= mScrollConsumed[1];
                    vtev.offsetLocation(mScrollOffset[0], mScrollOffset[1]);
                    // Updated the nested offsets
                    mNestedOffsets[0] += mScrollOffset[0];
                    mNestedOffsets[1] += mScrollOffset[1];
                }
                if (!mIsBeingDragged && Math.abs(dy) > TOUCH_SLOP) {
                    if (HORIZONTAL == mSpringDirect) {
                        if (Math.abs(dx) > TOUCH_SLOP) {
                            if (null != getParent()) {
                                getParent().requestDisallowInterceptTouchEvent(true);
                            }
                            if (dx > 0) {
                                dx -= TOUCH_SLOP;
                            } else {
                                dx += TOUCH_SLOP;
                            }
                            mIsBeingDragged = true;
                        }
                    } else if (VERTICAL == mSpringDirect) {
                        if (Math.abs(dy) > TOUCH_SLOP) {
                            if (null != getParent()) {
                                getParent().requestDisallowInterceptTouchEvent(true);
                            }
                            if (dy > 0) {
                                dy -= TOUCH_SLOP;
                            } else {
                                dy += TOUCH_SLOP;
                            }
                            mIsBeingDragged = true;
                        }
                    }
                }
                if (mIsBeingDragged) {
                    if (HORIZONTAL == mSpringDirect) {
                        mLastTouchX = x - mScrollOffset[0];
                    } else if (VERTICAL == mSpringDirect) {
                        mLastTouchY = y - mScrollOffset[1];
                    }
                    int scrolledDeltaX = 0;
                    int unconsumedX = dx - scrolledDeltaX;
                    int scrolledDeltaY = 0;
                    int unconsumedY = dy - scrolledDeltaY;
                    if (dispatchNestedScroll(scrolledDeltaX, scrolledDeltaY, unconsumedX, unconsumedY, mScrollOffset)) {
                        mLastTouchX -= mScrollOffset[0];
                        mLastTouchY -= mScrollOffset[1];
                        vtev.offsetLocation(mScrollOffset[0], mScrollOffset[1]);
                        mNestedOffsets[0] += mScrollOffset[0];
                        mNestedOffsets[1] += mScrollOffset[1];
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                stopNestedScroll();
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
        }
        vtev.recycle();
        return true;
    }

    private void initialize() {
        addHeader();
        addFooter();
    }

    private void addHeader() {
        mHeaderLayout = new RelativeLayout(getContext());
        LayoutParams lp = null;
        if (HORIZONTAL == mSpringDirect) {
            lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            lp.addRule(ALIGN_PARENT_LEFT);
        } else if (VERTICAL == mSpringDirect) {
            lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            lp.addRule(ALIGN_PARENT_TOP);
        }
        mHeaderLayout.setLayoutParams(lp);
        addView(mHeaderLayout);
    }

    private void addFooter() {
        mFooterLayout = new RelativeLayout(getContext());
        LayoutParams lp = null;
        if (HORIZONTAL == mSpringDirect) {
            lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            lp.addRule(ALIGN_PARENT_RIGHT);
        } else if (VERTICAL == mSpringDirect) {
            lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            lp.addRule(ALIGN_PARENT_BOTTOM);
        }
        mFooterLayout.setLayoutParams(lp);
        addView(mFooterLayout);
    }

    private int dp2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }

    /**
     * 功  能: 设置弹性方向
     * 参  数: direct - 弹性方向,HORIZONTAL:横向,VERTICAL:纵向
     * 返回值: 无
     */
    public void setSpringDirect(int direct) {
        if (HORIZONTAL != direct && VERTICAL != direct) {
            direct = VERTICAL;
        }
        mSpringDirect = direct;
    }

    /**
     * 功  能: 设置页眉构造器
     * 参  数: creator - 构造器
     * 返回值: 无
     */
    public void setHeaderCreator(Creator creator) {
        if (null != creator) {
            mHeaderLayout.removeAllViewsInLayout();
            mHeaderLayout.addView(creator.getView());
            mHeaderCreator = creator;
        }
    }

    /**
     * 功  能: 设置页脚构造器
     * 参  数: creator - 构造器
     * 返回值: 无
     */
    public void setFooterCreator(Creator creator) {
        if (null != creator) {
            mFooterLayout.removeAllViewsInLayout();
            mFooterLayout.addView(creator.getView());
            mFooterCreator = creator;
        }
    }

    /**
     * 功  能: 设置页眉最大宽度(或高度)
     * 参  数: maxSizeDp - 最大值(单位dp)
     * 返回值: 无
     */
    public void setMaxHeaderSize(float maxSizeDp) {
        mMaxHeaderSize = dp2px(maxSizeDp);
    }

    /**
     * 功  能: 设置页脚最大宽度(或高度)
     * 参  数: maxSizeDp - 最大值(单位dp)
     * 返回值: 无
     */
    public void setMaxFooterSize(float maxSizeDp) {
        mMaxFooterSize = dp2px(maxSizeDp);
    }

    /**
     * 功  能: 设置页眉宽度(或高度)
     * 参  数: sizeDp - 值(单位dp)
     * 返回值: 无
     */
    public void setHeaderSize(float sizeDp) {
        mHeaderSize = dp2px(sizeDp);
    }

    /**
     * 功  能: 设置页脚宽度(或高度)
     * 参  数: sizeDp - 值(单位dp)
     * 返回值: 无
     */
    public void setFooterSize(float sizeDp) {
        mFooterSize = dp2px(sizeDp);
    }

    /**
     * 功  能: 手势监听器
     */
    public static abstract class GestureListener {
        /**
         * 功  能: 按下
         * 参  数: downEvent - 按下事件
         * 返回值: 无
         */
        public abstract void onDown(MotionEvent downEvent);

        /**
         * 功  能: 拖动
         * 参  数: downEvent - 按下事件
         *         moveEvent - 移动事件
         *         distanceX - x轴拖动距离
         *         distanceY - y轴拖动距离
         * 返回值: 无
         */
        public abstract void onScroll(MotionEvent downEvent, MotionEvent moveEvent, float distanceX, float distanceY);

        /**
         * 功  能: 划动
         * 参  数: downEvent - 按下事件
         *         upEvent - 抬起事件
         *         velocityX - x轴划动速度
         *         velocityY - y轴划动速度
         * 返回值: 无
         */
        public abstract void onFling(MotionEvent downEvent, MotionEvent upEvent, float velocityX, float velocityY);

        /**
         * 功  能: 抬起
         * 参  数: upEvent - 抬起事件
         *         isFling - 是否快速划动
         * 返回值: 无
         */
        public abstract void onUp(MotionEvent upEvent, boolean isFling);
    }

    /**
     * 功  能: 页眉/页脚构造器
     */
    public static abstract class Creator {
        /**
         * 功  能: 获取视图
         * 参  数: 无
         * 返回值: 视图
         */
        public abstract View getView();

        /**
         * 功  能: 拖动
         * 参  数: maxSize - 最大宽度(或高度)
         *         size - 宽度(或高度)
         *         fraction - 当前拖动距离
         * 返回值: 无
         */
        public abstract void onPulling(float maxSize, float size, float offset);

        /**
         * 功  能: 释放
         * 参  数: maxWH - 最大宽度(或高度)
         *         wh - 宽度(或高度)
         *         fraction - 当前拖动距离
         * 返回值: 无
         */
        public abstract void onRelease(float maxSize, float size, float offset);
    }
}
