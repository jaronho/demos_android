package com.nongyi.nylive;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Author:  jaron.ho
 * Date:    2017-04-27
 * Brief:   SpringLayout
 */

public class SpringLayout extends ViewGroup {
    private Scroller mScroller = new Scroller(getContext()); // 滑动辅助类
    private VelocityTracker mVelocityTracker = VelocityTracker.obtain(); // 速度辅助类
    private final int TOUCH_SLOP = ViewConfiguration.get(getContext()).getScaledTouchSlop(); // 当前滑动阀值
    private final int MAX_VELOCITY = ViewConfiguration.get(getContext()).getScaledMaximumFlingVelocity();   // Velocity的阀值
    private boolean mIsHorizontal = false;  // 是否水平滑动(反之垂直滑动)
    private boolean mIsOnDrag = false; // 是否在滑动
    private boolean mIsDragForward = true;  // 是否正向滑动
    private int mLastMontionX = 0;  // 记录上次x的位置
    private int mLastMontionY = 0; // 记录上次y的位置
    private int mTotalLength = 0; // 整个控件的长度
    private int mPointID = 0; // pointID
    private int mMaxPercent = 70;   // 最大滑动距离,[0,100](百分比,相对于SpringLayout的宽度或高度)
    private float mDamp = 0.3f;  // 滑动的阻尼系数,(0, 1]
    private Listener mListener = null;  // 监听器

    public SpringLayout(Context context) {
        super(context);
    }

    public SpringLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpringLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SpringLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int)event.getX();
        int y = (int)event.getY();
        switch (action) {
            case MotionEvent.ACTION_POINTER_DOWN:
                mPointID = event.getPointerId(event.getActionIndex()); // 记录当前pointID
                break;
            case MotionEvent.ACTION_DOWN:
                mLastMontionX = x;
                mLastMontionY = y;
                if (!mScroller.isFinished()) { // 当动画还没有结束的时候强制结束
                    mScroller.abortAnimation();
                    mScroller.forceFinished(true);
                }
                mIsOnDrag = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsOnDrag) {
                    break;
                }
                int xDiff = x - mLastMontionX;
                int yDiff = y - mLastMontionY;
                if (mIsHorizontal) {
                    if (Math.abs(xDiff) > TOUCH_SLOP) {   // x轴超过了最小滑动距离
                        mIsOnDrag = true;
                        mIsDragForward = xDiff > 0;
                    }
                } else {
                    if (Math.abs(yDiff) > TOUCH_SLOP) {   // y轴超过了最小滑动距离
                        mIsOnDrag = true;
                        mIsDragForward = yDiff > 0;
                    }
                }
                if (mIsOnDrag) {
                    if (null != mListener) {
                        mIsOnDrag = !mListener.isCanDrag(mIsHorizontal, mIsDragForward);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsOnDrag = false;
                break;
        }
        return mIsOnDrag;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchIndex = event.getActionIndex();
        mVelocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_POINTER_DOWN:   // 添加多点触控的处理
                mPointID = event.getPointerId(touchIndex);
                mLastMontionX = (int)event.getX(touchIndex); // 记录按下的x点
                mLastMontionY = (int)event.getY(touchIndex); // 记录按下的y点
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if (event.getPointerId(touchIndex) == mPointID) {   // 添加多点触控的支持
                    int newIndex = 0 == touchIndex ? 1 : 0;
                    mPointID = event.getPointerId(newIndex);
                    mLastMontionX = (int)event.getX(newIndex);
                    mLastMontionY = (int)event.getY(newIndex);
                }
                break;
            case MotionEvent.ACTION_DOWN:
                mPointID = event.getPointerId(touchIndex);
                mLastMontionX = (int)event.getX(); // 记录按下的x点
                mLastMontionY = (int)event.getY(); // 记录按下的y点
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mIsOnDrag) {
                    break;
                }
                touchIndex = event.findPointerIndex(mPointID);
                if (touchIndex < 0) {   // 当前index小于0就返false继续接受下一次事件
                    mPointID = event.getPointerId(event.getActionIndex());
                    mIsOnDrag = false;
                    break;
                }
                int detaX = (int)(mLastMontionX - event.getX(touchIndex)); // 计算x滑动的距离
                int detaY = (int)(mLastMontionY - event.getY(touchIndex)); // 计算y滑动的距离
                if (mIsHorizontal) {
                    scrollBy(detaX, 0); // 调用滑动函数
                    if ((mIsDragForward && detaX > 0 && getScrollX() >= 0) || (!mIsDragForward && detaX < 0 && getScrollX() <= 0)) {
                        mIsOnDrag = false;
                    }
                } else {
                    scrollBy(0, detaY); // 调用滑动函数
                    if ((mIsDragForward && detaY > 0 && getScrollY() >= 0) || (!mIsDragForward && detaY < 0 && getScrollY() <= 0)) {
                        mIsOnDrag = false;
                    }
                }
                mLastMontionX = (int)event.getX(touchIndex); // 记录上一次按下的x点
                mLastMontionY = (int)event.getY(touchIndex); // 记录上一次按下的y点
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000);
                if (mIsHorizontal) {
                    if (Math.abs(mVelocityTracker.getXVelocity()) > MAX_VELOCITY && !checkIsBroad()) {
                        mScroller.fling(getScrollX(), getScrollY(), -(int)mVelocityTracker.getXVelocity(), 0, 0, 0, 0, mTotalLength - getWidth());
                    } else {
                        if (null == mListener) {
                            actionBack();
                        } else {
                            mListener.onRelease(getWidth() * mMaxPercent / 100, Math.abs(getScrollX()));
                        }
                    }
                } else {
                    if (Math.abs(mVelocityTracker.getYVelocity()) > MAX_VELOCITY && !checkIsBroad()) {
                        mScroller.fling(getScrollX(), getScrollY(), 0, -(int)mVelocityTracker.getYVelocity(), 0, 0, 0, mTotalLength - getHeight());
                    } else {
                        if (null == mListener) {
                            actionBack();
                        } else {
                            mListener.onRelease(getHeight() * mMaxPercent / 100, Math.abs(getScrollY()));
                        }
                    }
                }
                mIsOnDrag = false;
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) { // 计算当前位置
            if (mIsHorizontal) {
                scrollTo(mScroller.getCurrX(), 0);  // 滚动x轴
            } else {
                scrollTo(0, mScroller.getCurrY());  // 滚动y轴
            }
            postInvalidate();
        }
    }

    @Override
    public void scrollBy(int x, int y) {
        if (mIsHorizontal) {
            float maxOffsetX = getWidth() * mMaxPercent / 100;
            float offsetX = Math.abs(getScrollX());
            if (offsetX < maxOffsetX) {
                super.scrollBy((int)(x * mDamp), y);
                if (null != mListener) {
                    mListener.onDrag(maxOffsetX, offsetX);
                }
            }
        } else {
            float maxOffsetY = getHeight() * mMaxPercent / 100;
            float offsetY = Math.abs(getScrollY());
            if (offsetY < maxOffsetY) {
                super.scrollBy(x, (int)(y * mDamp));
                if (null != mListener) {
                    mListener.onDrag(maxOffsetY, offsetY);
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int paretnHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        for (int i = 0, count = getChildCount(); i < count; ++i) {
            View child = getChildAt(i);
            if (View.GONE != child.getVisibility()) {
                LayoutParams childLp = child.getLayoutParams();
                boolean childWidthWC = LayoutParams.WRAP_CONTENT == childLp.width;
                boolean childHeightWC = LayoutParams.WRAP_CONTENT == childLp.height;
                int childWidthMeasureSpec;
                int childHeightMeasureSpec;
                if (child.getLayoutParams() instanceof MarginLayoutParams) {
                    MarginLayoutParams childMarginLp = (MarginLayoutParams) childLp;
                    childWidthMeasureSpec = childWidthWC ? MeasureSpec.makeMeasureSpec(parentWidthSize, MeasureSpec.UNSPECIFIED) :
                            getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight() + childMarginLp.leftMargin + childMarginLp.rightMargin, childLp.width);
                    childHeightMeasureSpec = childHeightWC ? MeasureSpec.makeMeasureSpec(paretnHeightSize, MeasureSpec.UNSPECIFIED) : getChildMeasureSpec(heightMeasureSpec,
                            getPaddingTop() + getPaddingBottom() + childMarginLp.topMargin + childMarginLp.bottomMargin, childMarginLp.height);
                } else {
                    childWidthMeasureSpec = childWidthWC ? MeasureSpec.makeMeasureSpec(parentWidthSize, MeasureSpec.UNSPECIFIED) :
                            getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight(), childLp.width);
                    childHeightMeasureSpec = childHeightWC ? MeasureSpec.makeMeasureSpec(paretnHeightSize, MeasureSpec.UNSPECIFIED) :
                            getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom(), childLp.height);
                }
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mTotalLength = 0;
        int count = getChildCount();
        if (0 == count) {
            return;
        }
        int childStartPostion;
        if (mIsHorizontal) {
            childStartPostion = getPaddingLeft();
        } else {
            childStartPostion = getPaddingTop();
        }
        for (int i = 0; i < count; ++i) {
            View child = getChildAt(i);
            if (null != child && View.GONE != child.getVisibility()) {
                LayoutParams lp = child.getLayoutParams();
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                int leftMargin = 0;
                int rightMargin = 0;
                int topMargin = 0;
                int bottomMargin = 0;
                if (lp instanceof MarginLayoutParams) {
                    MarginLayoutParams mlp = (MarginLayoutParams)lp;
                    leftMargin = mlp.leftMargin;
                    rightMargin = mlp.rightMargin;
                    topMargin = mlp.topMargin;
                    bottomMargin = mlp.bottomMargin;
                }
                int startX = (getWidth() - leftMargin - rightMargin - childWidth) / 2 + leftMargin;
                int startY = (getHeight() - topMargin - bottomMargin - childHeight) / 2 + topMargin;
                if (mIsHorizontal) {
                    childStartPostion += leftMargin;
                    child.layout(childStartPostion, startY, childStartPostion + childWidth, startY + childHeight);
                    childStartPostion += (childWidth + rightMargin);
                } else {
                    childStartPostion += topMargin;
                    child.layout(startX, childStartPostion, startX + childWidth, childStartPostion + childHeight);
                    childStartPostion += (childHeight + bottomMargin);
                }
            }
        }
        if (mIsHorizontal) {
            childStartPostion += getPaddingRight();
        } else {
            childStartPostion += getPaddingBottom();
        }
        mTotalLength = childStartPostion;
    }

    // 检测当前是否可回弹
    private boolean checkIsBroad() {
        if (mIsHorizontal) {
            return getScrollX() < 0 || getScrollX() + getWidth() > mTotalLength;
        } else {
            return getScrollY() < 0 || getScrollY() + getHeight() > mTotalLength;
        }
    }

    // 回弹函数
    private void actionBack() {
        if (mIsHorizontal) {
            if (getScrollX() < 0 || getWidth() > mTotalLength) {   // 左部回弹
                mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0); // 开启回弹效果
                invalidate();
            } else if (getScrollX() + getWidth() > mTotalLength) { // 右部回弹
                mScroller.startScroll(getScrollX(), 0, -(getScrollX() + getWidth() - mTotalLength), 0);    // 开启底部回弹
                invalidate();
            }
        } else {
            if (getScrollY() < 0 || getHeight() > mTotalLength) {   // 顶部回弹
                mScroller.startScroll(0, getScrollY(), 0, -getScrollY()); // 开启回弹效果
                invalidate();
            } else if (getScrollY() + getHeight() > mTotalLength) { // 底部回弹
                mScroller.startScroll(0, getScrollY(), 0, -(getScrollY() + getHeight() - mTotalLength));    // 开启底部回弹
                invalidate();
            }
        }
    }

    /**
     * 功  能: 是否水平滑动
     * 参  数: 无
     * 返回值: boolean
     */
    public boolean isHorizontal() {
        return mIsHorizontal;
    }

    /**
     * 功  能: 设置是否水平滑动(默认垂直滑动)
     * 参  数: isHorizontal - 是否水平滑动,true:水平滑动,false:垂直滑动
     * 返回值: 无
     */
    public void setHorizontal(boolean isHorizontal) {
        mIsHorizontal = isHorizontal;
    }

    /**
     * 功  能: 设置可以滑动的最大距离,相对于宽度或高度的百分比
     * 参  数: maxPercent - 最大百分比,[0, 100],当为0时,不可滑动
     * 返回值: 无
     */
    public void setMaxPercent(int maxPercent) {
        if (maxPercent < 0) {
            maxPercent = 0;
        } else if (maxPercent > 100) {
            maxPercent = 100;
        }
        mMaxPercent = maxPercent;
    }

    /**
     * 功  能: 设置滑动的阻尼系数,值越大,阻力越小
     * 参  数: damp - 阻尼系数,(0, 1]
     * 返回值: 无
     */
    public void setDamp(float damp) {
        if (damp <= 0) {
            damp = 0.01f;
        } else if (damp > 1) {
            damp = 1;
        }
        mDamp = damp;
    }

    /**
     * 功  能: 复位滑动
     * 参  数: 无
     * 返回值: 无
     */
    public void restore() {
        actionBack();
    }

    /**
     * 功  能: 设置监听器
     * 参  数: listener - 监听器
     * 返回值: 无
     */
    public void setListener(Listener listener) {
        mListener = listener;
    }


    /**
     * Brief:   Drag Listener
     */
    public interface Listener {
        /**
         * 功  能: 是否还可以滑动
         * 参  数: isHorizontal - 是否水平滑动
         *         isForward - 是否正向滑动(向右或向下为正向滑动,即x值递增或y值递增)
         * 返回值: boolean,当返回true时,SpringLayout不可滑动,当返回false时,SpringLayout可滑动
         */
        boolean isCanDrag(boolean isHorizontal, boolean isForward);

        /**
         * 功  能: 正在滑动
         * 参  数: maxOffset - 可滑动的最大偏移值
         *         offset - 当前滑动的偏移值
         * 返回值: 无
         */
        void onDrag(float maxOffset, float offset);

        /**
         * 功  能: 释放滑动
         * 参  数: maxOffset - 可滑动的最大偏移值
         *         offset - 当前滑动的偏移值
         * 返回值: 无
         */
        void onRelease(float maxOffset, float offset);
    }
}
