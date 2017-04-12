package com.nongyi.nylive;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Author:  jaron.ho
 * Date:    2017-04-11
 * Brief:   RefreshRecyclerView(支持头部和底部拖动更新)
 */

public class RefreshRecyclerView extends WrapRecyclerView {
    public static final int STATUS_NORMAL = 0x01;		// 默认状态
    public static final int STATUS_PULL = 0x02;			// 拖动状态
    public static final int STATUS_READY = 0x03;		// 就绪状态
    public static final int STATUS_REFRESHING = 0x04;	// 刷新状态

    public static final int KEY_HEAD_VIEW = Integer.MIN_VALUE;  // 头部键值
    public static final int KEY_FOOT_VIEW = Integer.MAX_VALUE;  // 底部键值

    private boolean mIsHorizontal = false;           // 横向布局
    private float mDragResistance = 0.35f;              // 拖动的阻力指数
    private float mTouchPos = 0;                           // 触摸的位置

    private Creator mHeadCreator = null;				// 头部刷新视图构造器
    private View mHeadView = null;						// 头部刷新视图
    private int mHeadViewSize = 0;					// 头部刷新视图的大小
    private boolean mIsHeadDrag = false;				// 头部是否正在拖动
    private int mCurrentHeadStatus = STATUS_NORMAL;		// 头部刷新状态

    private Creator mFootCreator = null;				// 底部刷新视图构造器
    private View mFootView = null;						// 底部刷新视图
    private int mFootViewSize = 0;					// 底部刷新视图的大小
    private boolean mIsFootDrag = false;				// 底部是否正在拖动
    private int mCurrentFootStatus = STATUS_NORMAL;		// 底部刷新状态

    public RefreshRecyclerView(Context context) {
        super(context);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (null != getAdapter()) {
            throw new AssertionError("exist adapter");
        }
        if (!(adapter instanceof WrapViewAdapter)) {
            throw new AssertionError("adapter must instance of WrapViewAdapter");
        }
        super.setAdapter(adapter);
        addHeaderView();
        addFooterView();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录手指按下的位置,之所以写在dispatchTouchEvent那是因为如果我们处理了条目点击事件,那么就不会进入onTouchEvent里面,所以只能在这里获取
                mTouchPos = mIsHorizontal ? e.getRawX() : e.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                if (mIsHeadDrag) {
                    restoreHeadView();
                }
                if (mIsFootDrag) {
                    restoreFootView();
                }
                break;
        }
        return super.dispatchTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int distance = (int)(((mIsHorizontal ? e.getRawX() : e.getRawY()) - mTouchPos) * mDragResistance);  // 获取手指触摸拖拽的距离
                if (isScrollToHead()) {
                    scrollToPosition(0);    // 解决自动滚动问题
                    if (STATUS_REFRESHING == mCurrentHeadStatus) {
                        if (distance < 0) {
                            mCurrentHeadStatus = STATUS_NORMAL;
                            restoreHeadView();
                        }
                    } else {
                        int margin = distance - mHeadViewSize;
                        setHeadViewMargin(margin);
                        updateHeadStatus(margin);
                        mIsHeadDrag = true;
                    }
                }
                if (isScrollToFoot()) {
                    scrollToPosition(getAdapter().getItemCount() - 1);  // 解决底部刷新自动滚动问题
                    if (null != mFootView && mFootViewSize <= 0) {
                        mFootViewSize = mIsHorizontal ? mFootView.getMeasuredWidth() : mFootView.getMeasuredHeight();
                    }
                    if (STATUS_REFRESHING == mCurrentFootStatus) {
                        if (distance > 0) {
                            mCurrentFootStatus = STATUS_NORMAL;
                            restoreFootView();
                        }
                    } else {
                        if (distance < 0) {
                            setFootViewMargin(-distance);
                            updateFootStatus(-distance);
                            mIsFootDrag = true;
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(e);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            if (null != mHeadView && mHeadViewSize <= 0) {
                mHeadViewSize = mIsHorizontal ? mHeadView.getMeasuredWidth() : mHeadView.getMeasuredHeight();
                if (mHeadViewSize > 0) {
                    setHeadViewMargin(-mHeadViewSize + 1);  // 隐藏头部刷新的视图,多留出1px防止无法判断是否滚动到头部问题
                }
            }
        }
    }

    // 添加头部的刷新视图
    private void addHeaderView() {
        WrapViewAdapter adapter = (WrapViewAdapter)getAdapter();
        if (null != adapter && null != mHeadCreator && null == mHeadView) {
            View headView = mHeadCreator.getView(getContext(), this);
            if (null != headView) {
                addHeaderView(KEY_HEAD_VIEW, headView);
                mHeadView = headView;
            }
        }
    }

    // 添加底部的刷新视图
    private void addFooterView() {
        WrapViewAdapter adapter = (WrapViewAdapter)getAdapter();
        if (null != adapter && null != mFootCreator && null == mFootView) {
            View footView = mFootCreator.getView(getContext(), this);
            if (null != footView) {
                addFooterView(KEY_FOOT_VIEW, footView);
                mFootView = footView;
            }
        }
    }

    // 重置当前头部刷新状态
    private void restoreHeadView() {
        if (null != mHeadView) {
            MarginLayoutParams layoutParams = (MarginLayoutParams)mHeadView.getLayoutParams();
            int currentMargin = mIsHorizontal ? layoutParams.leftMargin : layoutParams.topMargin;
            int finalMargin = -mHeadViewSize + 1;
            if (STATUS_PULL == mCurrentHeadStatus) {
                mCurrentHeadStatus = STATUS_NORMAL;
                if (null != mHeadCreator) {
                    mHeadCreator.onPullAbort();
                }
            } else if (STATUS_READY == mCurrentHeadStatus) {
                finalMargin = 0;
                mCurrentHeadStatus = STATUS_REFRESHING;
                if (null != mHeadCreator) {
                    mHeadCreator.onRefreshing();
                }
            }
            int offset = currentMargin - finalMargin;
            // 回弹到指定位置
            ValueAnimator animator = ObjectAnimator.ofFloat(currentMargin, finalMargin).setDuration(Math.abs(offset));
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float margin = (float)animation.getAnimatedValue();
                    setHeadViewMargin((int)margin);
                }
            });
            animator.start();
        }
        mIsHeadDrag = false;
    }

    // 重置当前底部刷新状态
    private void restoreFootView() {
        if (null != mFootView) {
            MarginLayoutParams layoutParams = (MarginLayoutParams)mFootView.getLayoutParams();
            int currentMargin = mIsHorizontal ? layoutParams.rightMargin : layoutParams.bottomMargin;
            int finalMargin = 0;
            if (STATUS_PULL == mCurrentFootStatus) {
                mCurrentFootStatus = STATUS_NORMAL;
                if (null != mFootCreator) {
                    mFootCreator.onPullAbort();
                }
            } else if (STATUS_READY == mCurrentFootStatus) {
                mCurrentFootStatus = STATUS_REFRESHING;
                if (null != mFootCreator) {
                    mFootCreator.onRefreshing();
                }
            }
            int offset = currentMargin - finalMargin;
            // 回弹到指定位置
            ValueAnimator animator = ObjectAnimator.ofFloat(currentMargin, finalMargin).setDuration(Math.abs(offset));
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float margin = (float)animation.getAnimatedValue();
                    setFootViewMargin((int)margin);
                }
            });
            animator.start();
        }
        mIsFootDrag = false;
    }

    // 设置头部刷新视图的margin
    private void setHeadViewMargin(int margin) {
        if (null != mHeadView) {
            MarginLayoutParams params = (MarginLayoutParams)mHeadView.getLayoutParams();
            if (margin < -mHeadViewSize + 1) {
                margin = -mHeadViewSize + 1;
            }
            if (mIsHorizontal) {
                params.leftMargin = margin;
            } else {
                params.topMargin = margin;
            }
            mHeadView.setLayoutParams(params);
        }
    }

    // 设置底部刷新视图的margin
    private void setFootViewMargin(int margin) {
        if (null != mFootView) {
            MarginLayoutParams params = (MarginLayoutParams)mFootView.getLayoutParams();
            if (margin < 0) {
                margin = 0;
            }
            if (mIsHorizontal) {
                params.rightMargin = margin;
            } else {
                params.bottomMargin = margin;
            }
            mFootView.setLayoutParams(params);
        }
    }

    // 更新头部刷新的状态
    private void updateHeadStatus(int margin) {
        if (margin <= -mHeadViewSize) {
            mCurrentHeadStatus = STATUS_NORMAL;
        } else if (margin < 0) {
            mCurrentHeadStatus = STATUS_PULL;
        } else {
            mCurrentHeadStatus = STATUS_READY;
        }
        if (null != mHeadCreator) {
            mHeadCreator.onPull(mHeadViewSize, margin, mCurrentHeadStatus);
        }
    }

    // 更新底部刷新的状态
    private void updateFootStatus(int margin) {
        if (margin <= 0) {
            mCurrentFootStatus = STATUS_NORMAL;
        } else if (margin < mFootViewSize) {
            mCurrentFootStatus = STATUS_PULL;
        } else {
            mCurrentFootStatus = STATUS_READY;
        }
        if (null != mFootCreator) {
            mFootCreator.onPull(mFootViewSize, margin, mCurrentFootStatus);
        }
    }
	
	// 是否滚动到了最头部
    private boolean isScrollToHead() {
        return !(mIsHorizontal ? canScrollHorizontally(-1) : canScrollVertically(-1));
    }

    // 是否滚动到了最底部
    public boolean isScrollToFoot() {
        return !(mIsHorizontal ? canScrollHorizontally(1) : canScrollVertically(1));
    }

    // 设置是否横向布局
    public void setHorizontal(boolean flag) {
        mIsHorizontal = flag;
    }

    // 设置手指拖动的阻力系数
    public void setDragResistance(float dragResistance) {
        mDragResistance = dragResistance;
    }

    // 设置头部刷新视图的构造器
    public void setHeadViewCreator(Creator creator) {
        if (null != mHeadCreator) {
           throw new AssertionError("exist head view creator");
        }
        mHeadCreator = creator;
        addHeaderView();
    }

    // 设置底部刷新视图的构造器
    public void setFootViewCreator(Creator creator) {
        if (null != mFootCreator) {
            throw new AssertionError("exist foot view creator");
        }
        mFootCreator = creator;
        addFooterView();
    }

    // 停止头部刷新
    public void stopHeadRefresh() {
        mCurrentHeadStatus = STATUS_NORMAL;
        restoreHeadView();
        if (null != mHeadCreator) {
            mHeadCreator.onStopRefresh();
        }
    }

    // 停止底部刷新
    public void stopFootRefresh() {
        mCurrentFootStatus = STATUS_NORMAL;
        restoreFootView();
        if (null != mFootCreator) {
            mFootCreator.onStopRefresh();
        }
    }

    public static abstract class Creator {
        // 获取刷新视图
        public abstract View getView(Context context, RefreshRecyclerView parent);

        /**
         * 功  能: 正在拖动
         * 参  数: viewSize - 视图大小
         *         dragOffset - 拖动偏移
         *         status - 当前状态
         * 返回值: 无
         */
        public abstract void onPull(int viewSize, int dragOffset, int status);

        // 中断拖动
        public abstract void onPullAbort();

        // 正在刷新
        public abstract void onRefreshing();

        // 停止刷新
        public abstract void onStopRefresh();
    }
}
