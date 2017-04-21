package com.nongyi.nylive;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import java.lang.reflect.Field;

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
    private GestureListener mGestureListener = null;
    private RelativeLayout mHeaderLayout = null;
    private RelativeLayout mFooterLayout = null;
    private IView mHeaderView = null;
    private IView mFooterView = null;
    private View mTargetView = null;
    private float mMaxHeaderSize = 120;
    private float mHeaderSize = 80;
    private float mMaxFooterSize = 120;
    private float mFooterSize = 80;

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
    public boolean dispatchTouchEvent(MotionEvent e) {
        super.dispatchTouchEvent(e);
        handleGesture(e, mGestureListener);
        handleNestedScroll(e);
        return true;
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
    private void handleGesture(MotionEvent ev, GestureListener listener) {
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
                break;
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
                break;
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
                break;
            case MotionEvent.ACTION_MOVE:
                float scrollX = focusX - mLastFocusX;
                float scrollY = focusY - mLastFocusY;
                if (mAlwaysInTapRegion) {
                    int deltaX = (int)(focusX - mDownFocusX);
                    int deltaY = (int)(focusY - mDownFocusY);
                    int distance = (deltaX * deltaX) + (deltaY * deltaY);
                    if (distance > TOUCH_SLOP_SQUARE) {
                        if (null != listener) {
                            listener.onScroll(mCurrentDownEvent, ev, scrollX, scrollY, mVelocityX, mVelocityY);
                        }
                        mLastFocusX = focusX;
                        mLastFocusY = focusY;
                        mAlwaysInTapRegion = false;
                    }
                } else if ((Math.abs(scrollX) >= 1) || (Math.abs(scrollY) >= 1)) {
                    if (null != listener) {
                        listener.onScroll(mCurrentDownEvent, ev, scrollX, scrollY, mVelocityX, mVelocityY);
                    }
                    mLastFocusX = focusX;
                    mLastFocusY = focusY;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                mAlwaysInTapRegion = false;
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
                break;
        }
    }

    // 处理嵌套滚动
    private void handleNestedScroll(MotionEvent e) {
        MotionEvent vtev = MotionEvent.obtain(e);
        int action = MotionEventCompat.getActionMasked(e);
        int actionIndex = MotionEventCompat.getActionIndex(e);
        if (MotionEvent.ACTION_DOWN == action) {
            mNestedOffsets[0] = mNestedOffsets[1] = 0;
        }
        vtev.offsetLocation(mNestedOffsets[0], mNestedOffsets[1]);
        switch (action) {
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
            case MotionEventCompat.ACTION_POINTER_DOWN:
                mActivePointerId = e.getPointerId(actionIndex);
                mLastTouchX = (int)e.getX(actionIndex);
                mLastTouchY = (int)e.getY(actionIndex);
                break;
            case MotionEvent.ACTION_MOVE:
                int index = e.findPointerIndex(mActivePointerId);
                if (index < 0) {
                    return;
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
    }

    // 初始化
    private void initialize() {
        mGestureListener = new GestureListener() {
            private DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(8);;

            @Override
            public void onDown(MotionEvent downEvent) {
                Log.d("SpringLayout", "onDown");
            }

            @Override
            public void onScroll(MotionEvent downEvent, MotionEvent moveEvent, float distanceX, float distanceY, float velocityX, float velocityY) {
                Log.d("SpringLayout", "onScroll => distanceX: "+distanceX+", distanceY: "+distanceY+", velocityX: "+velocityX+", velocityY: "+velocityY);
                if (isViewToHead()) {
                    if (HORIZONTAL == mSpringDirect) {
                        if (distanceX > 0) {        // 正方向拖动
                            distanceX = Math.min(mMaxHeaderSize * 2, distanceX);
                            distanceX = Math.max(0, distanceX);
                        } else if (distanceY < 0) { // 反方向拖动
                            distanceX = Math.max(-mMaxHeaderSize * 2, distanceX);
                            distanceX = Math.min(0, distanceX);
                            distanceX = Math.abs(distanceX);
                        }
                        float offsetX = decelerateInterpolator.getInterpolation(distanceX / mMaxHeaderSize / 2) * distanceX / 2;
                    } else if (VERTICAL == mSpringDirect) {
                        if (distanceY > 0) {        // 正方向拖动
                            distanceY = Math.min(mMaxHeaderSize * 2, distanceY);
                            distanceY = Math.max(0, distanceY);
                        } else if (distanceY < 0) { // 反方向拖动
                            distanceY = Math.max(-mMaxHeaderSize * 2, distanceY);
                            distanceY = Math.min(0, distanceY);
                            distanceY = Math.abs(distanceY);
                        }
                        float offsetY = decelerateInterpolator.getInterpolation(distanceY / mMaxHeaderSize / 2) * distanceY / 2;
                        Log.d("SpringLayout", "----- distanceY: "+distanceY+", offsetY: "+offsetY);
                        mHeaderLayout.setTranslationY(offsetY + mHeaderLayout.getLayoutParams().height);
                    }
                }
                if (isViewToFoot()) {
                    if (HORIZONTAL == mSpringDirect) {
                        if (distanceX < 0) {        // 正方向拖动
                            distanceX = Math.max(-mMaxFooterSize * 2, distanceX);
                            distanceX = Math.min(0, distanceX);
                            distanceX = Math.abs(distanceX);
                        } else if (distanceY > 0) { // 反方向拖动
                            distanceX = Math.min(mMaxFooterSize * 2, distanceX);
                            distanceX = Math.max(0, distanceX);
                        }
                        float offsetX = decelerateInterpolator.getInterpolation(distanceX / mMaxFooterSize / 2) * distanceX / 2;
                    } else if (VERTICAL == mSpringDirect) {
                        if (distanceY < 0) {        // 正方向拖动
                            distanceY = Math.max(-mMaxFooterSize * 2, distanceY);
                            distanceY = Math.min(0, distanceY);
                            distanceY = Math.abs(distanceY);
                        } else if (distanceY > 0) { // 反方向拖动
                            distanceY = Math.min(mMaxFooterSize * 2, distanceY);
                            distanceY = Math.max(0, distanceY);
                        }
                        float offsetY = decelerateInterpolator.getInterpolation(distanceY / mMaxFooterSize / 2) * distanceY / 2;
                    }
                }
            }

            @Override
            public void onFling(MotionEvent downEvent, MotionEvent upEvent, float velocityX, float velocityY) {
                Log.d("SpringLayout", "onFling => velocityX: "+velocityX+", velocityY: "+velocityY);
            }

            @Override
            public void onUp(MotionEvent upEvent, boolean isFling) {
                Log.d("SpringLayout", "onUp => isFling: "+String.valueOf(isFling));
            }
        };
        addHeader();
        addFooter();
    }

    // 添加页眉
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

    // 添加页脚
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

    // dp转像素
    private int dp2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }

    // AbsListView是否在页眉位置
    private boolean isAbsListViewToHead(AbsListView view, boolean isHorizontal) {
        if (null != view) {
            int firstChildPadding = 0;
            if (view.getChildCount() > 0) { // 如果AdapterView的子控件数量不为0,获取第一个子控件的padding
                if (isHorizontal) {
                    firstChildPadding = view.getChildAt(0).getLeft() - view.getPaddingLeft();
                } else {
                    firstChildPadding = view.getChildAt(0).getTop() - view.getPaddingTop();
                }
            }
            if (0 == firstChildPadding && 0 == view.getFirstVisiblePosition()) {
                return true;
            }
        }
        return false;
    }

    // RecyclerView是否在页眉位置
    private boolean isRecyclerViewToHead(RecyclerView view, boolean isHorizontal) {
        if (null != view) {
            RecyclerView.LayoutManager manager = view.getLayoutManager();
            if (null == manager || 0 == manager.getItemCount()) {
                return true;
            }
            int firstChildPadding = 0;
            if (view.getChildCount() > 0) {
                View firstChild = view.getChildAt(0);    // 处理item高度超过一屏幕时的情况
                if (null != firstChild) {
                    if (isHorizontal) {
                        if (firstChild.getMeasuredWidth() >= view.getMeasuredWidth()) {
                            return !ViewCompat.canScrollHorizontally(view, -1);
                        }
                    } else {
                        if (firstChild.getMeasuredHeight() >= view.getMeasuredHeight()) {
                            return !ViewCompat.canScrollVertically(view, -1);
                        }
                    }
                    // 解决item的topMargin不为0时不能触发下拉刷新
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)firstChild.getLayoutParams();
                    int leftInset = 0;
                    int topInset = 0;
                    try {
                        Field field = RecyclerView.LayoutParams.class.getDeclaredField("mDecorInsets");
                        field.setAccessible(true);
                        Rect decorInsets = (Rect)field.get(layoutParams);   // 开发者自定义的滚动监听器
                        leftInset = decorInsets.left;
                        topInset = decorInsets.top;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (isHorizontal) {
                        firstChildPadding = firstChild.getLeft() - layoutParams.leftMargin - leftInset - view.getPaddingLeft();
                    } else {
                        firstChildPadding = firstChild.getTop() - layoutParams.topMargin - topInset - view.getPaddingTop();
                    }
                }
            }
            if (manager instanceof LinearLayoutManager) {
                LinearLayoutManager layoutManager = (LinearLayoutManager)manager;
                if (layoutManager.findFirstCompletelyVisibleItemPosition() < 1 && 0 == firstChildPadding) {
                    return true;
                }
            } else if (manager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager)manager;
                int[] out = layoutManager.findFirstCompletelyVisibleItemPositions(null);
                if (out[0] < 1 && 0 == firstChildPadding) {
                    return true;
                }
            }
        }
        return false;
    }

    // AbsListView是否在页脚位置
    private boolean isAbsListViewToFoot(AbsListView view, boolean isHorizontal) {
        if (null != view && null != view.getAdapter() && view.getChildCount() > 0 && view.getLastVisiblePosition() == view.getAdapter().getCount() - 1) {
            View lastChild = view.getChildAt(view.getChildCount() - 1);
            if (isHorizontal) {
                return lastChild.getLeft() <= view.getMeasuredWidth();
            }
            return lastChild.getBottom() <= view.getMeasuredHeight();
        }
        return false;
    }

    // RecyclerView是否在页脚位置
    private boolean isRecyclerViewToFoot(RecyclerView view, boolean isHorizontal) {
        if (null != view) {
            RecyclerView.LayoutManager manager = view.getLayoutManager();
            if (null == manager || 0 == manager.getItemCount()) {
                return false;
            }
            if (manager instanceof LinearLayoutManager) {
                View lastChild = view.getChildAt(view.getChildCount() - 1); // 处理item高度超过一屏幕时的情况
                if (null != lastChild) {
                    if (isHorizontal) {
                        if (lastChild.getMeasuredWidth() >= view.getMeasuredWidth()) {
                            return !ViewCompat.canScrollHorizontally(view, 1);
                        }
                    } else {
                        if (lastChild.getMeasuredHeight() >= view.getMeasuredHeight()) {
                            return !ViewCompat.canScrollVertically(view, 1);
                        }
                    }
                }
                LinearLayoutManager layoutManager = (LinearLayoutManager)manager;
                if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1) {
                    return true;
                }
            } else if (manager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager)manager;
                int[] out = layoutManager.findLastCompletelyVisibleItemPositions(null);
                int lastPosition = layoutManager.getItemCount() - 1;
                for (int position : out) {
                    if (position == lastPosition) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // WebView是否在页脚
    private boolean isWebViewToFoot(WebView view, boolean isHorizontal, int touchSlop) {
        if (null != view) {
            int offset = 0;
            if (isHorizontal) {
                return false;
            } else {
                offset = (int)(view.getContentHeight() * view.getScale()) - view.getHeight() + view.getScrollY();
            }
            return offset <= 2 * touchSlop;
        }
        return false;
    }

    // ViewGroup是否在页脚
    private boolean isViewGroupToFoot(ViewGroup view, boolean isHorizontal) {
        View firstChild = view.getChildAt(0);
        if (null != firstChild) {
            if (isHorizontal) {
                return firstChild.getMeasuredWidth() <= view.getScrollX() + view.getWidth();
            }
            return firstChild.getMeasuredHeight() <= view.getScrollY() + view.getHeight();
        }
        return false;
    }

    // 是否在页眉位置
    private boolean isViewToHead() {
        if (null == mTargetView) {
            return false;
        }
        boolean isHorizontal = HORIZONTAL == mSpringDirect;
        if (mTargetView instanceof AbsListView) {
            return isAbsListViewToHead((AbsListView)mTargetView, isHorizontal);
        } else if (mTargetView instanceof RecyclerView) {
            return isRecyclerViewToHead((RecyclerView)mTargetView, isHorizontal);
        }
        if (isHorizontal) {
            return Math.abs(mTargetView.getScrollX()) <= 2 * TOUCH_SLOP;
        }
        return Math.abs(mTargetView.getScrollY()) <= 2 * TOUCH_SLOP;
    }

    // 是否在页脚位置
    private boolean isViewToFoot() {
        if (null == mTargetView) {
            return false;
        }
        boolean isHorizontal = HORIZONTAL == mSpringDirect;
        if (mTargetView instanceof AbsListView) {
            return isAbsListViewToFoot((AbsListView)mTargetView, isHorizontal);
        } else if (mTargetView instanceof RecyclerView) {
            return isRecyclerViewToFoot((RecyclerView)mTargetView, isHorizontal);
        } else if (mTargetView instanceof WebView) {
            return isWebViewToFoot((WebView)mTargetView, isHorizontal, TOUCH_SLOP);
        } else if (mTargetView instanceof ViewGroup) {
            return isViewGroupToFoot((ViewGroup)mTargetView, isHorizontal);
        }
        return false;
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
     * 功  能: 设置页眉视图
     * 参  数: iView - 视图
     * 返回值: 无
     */
    public void setHeaderView(IView iView) {
        if (null != iView) {
            mHeaderLayout.removeAllViewsInLayout();
            mHeaderLayout.addView(iView.getView());
            mHeaderView = iView;
            if (HORIZONTAL == mSpringDirect) {
                mHeaderSize = iView.getView().getWidth();
            } else if (VERTICAL == mSpringDirect) {
                mHeaderSize = iView.getView().getHeight();
            }
            if (mHeaderSize > mMaxHeaderSize) {
                setMaxHeaderSize(mHeaderSize);
            }
        }
    }

    /**
     * 功  能: 设置页脚视图
     * 参  数: iView - 视图
     * 返回值: 无
     */
    public void setFooterView(IView iView) {
        if (null != iView) {
            mFooterLayout.removeAllViewsInLayout();
            mFooterLayout.addView(iView.getView());
            mFooterView = iView;
            if (HORIZONTAL == mSpringDirect) {
                mFooterSize = iView.getView().getWidth();
            } else if (VERTICAL == mSpringDirect) {
                mFooterSize = iView.getView().getHeight();
            }
            if (mFooterSize > mMaxFooterSize) {
                setMaxFooterSize(mFooterSize);
            }
        }
    }

    /**
     * 功  能: 设置目标视图
     * 参  数: view - 视图
     * 返回值: 无
     */
    public void setTargetView(View view) {
        mTargetView = view;
    }

    /**
     * 功  能: 设置最大页眉宽度(或高度)
     * 参  数: size - 宽度(或高度)
     * 返回值: 无
     */
    public void setMaxHeaderSize(float size) {
        mMaxHeaderSize = Math.max(size, mHeaderSize);
    }

    /**
     * 功  能: 设置最大页脚宽度(或高度)
     * 参  数: size - 宽度(或高度)
     * 返回值: 无
     */
    public void setMaxFooterSize(float size) {
        mMaxFooterSize = Math.max(size, mFooterSize);
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
         *         velocityX - x轴拖动速度
         *         velocityY - y轴拖动速度
         * 返回值: 无
         */
        public abstract void onScroll(MotionEvent downEvent, MotionEvent moveEvent, float distanceX, float distanceY, float velocityX, float velocityY);

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
     * 功  能: 页眉/页脚视图
     */
    public static abstract class IView {
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
