package com.nongyi.nylive;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Author:  Administrator
 * Date:    2017/4/27
 * Brief:   RefreshLayout
 */

public class RefreshLayout extends SpringLayout implements SpringLayout.Listener {
    private View mView = null;
    private Creator mHeaderCreator = null;
    private Creator mFooterCreator = null;
    private View mHeaderView = null;
    private View mFooterView = null;

    public RefreshLayout(Context context) {
        super(context);
        setListener(this);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setListener(this);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RefreshLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setListener(this);
    }

    @Override
    public boolean isCanDrag(boolean isHorizontal, boolean isForward) {
        if (isHorizontal) { // 水平滑动
            if (isForward) {    // 向右滑动
                if (mView instanceof RecyclerView) {
                    return isRecyclerViewCanScrollToLeft((RecyclerView)mView);
                }
            } else {    // 向左滑动
                if (mView instanceof RecyclerView) {
                    return isRecyclerViewCanScrollToRight((RecyclerView)mView);
                }
            }
        } else {    // 垂直滑动
            if (isForward) {    // 向下滑动
                if (mView instanceof RecyclerView) {
                    return isRecyclerViewCanScrollToTop((RecyclerView)mView);
                }
            } else {    // 向上滑动
                if (mView instanceof RecyclerView) {
                    return isRecyclerViewCanScrollToBottom((RecyclerView)mView);
                }
            }
        }
        return false;
    }

    @Override
    public void onDrag(float maxOffset, float offset) {

    }

    @Override
    public void onRelease(float maxOffset, float offset) {

    }

    private boolean isRecyclerViewCanScrollToLeft(RecyclerView view) {
        return view.canScrollHorizontally(-1);
    }

    private boolean isRecyclerViewCanScrollToRight(RecyclerView view) {
        return view.canScrollHorizontally(1);
    }

    private boolean isRecyclerViewCanScrollToTop(RecyclerView view) {
        return view.canScrollVertically(-1);
    }

    private boolean isRecyclerViewCanScrollToBottom(RecyclerView view) {
        return view.canScrollVertically(1);
    }

    public void setHeader(Creator creator) {
        if(null != mHeaderCreator) {
            throw new AssertionError("exist header creator");
        }
        mHeaderCreator = creator;
        mHeaderView = creator.createView(getContext(), this);
    }

    public void setFooter(Creator creator) {
        if(null != mFooterCreator) {
            throw new AssertionError("exist footer creator");
        }
        mFooterCreator = creator;
        mFooterView = creator.createView(getContext(), this);
    }

    /**
     * Brief:   Header/Footer Creator
     */
    public interface Creator {
        /**
         * 功  能: 创建视图
         * 参  数: context - 上下文
         *         parent - 父节点
         * 返回值: View
         */
        View createView(Context context, ViewGroup parent);

        /**
         * 功  能: 在滑动中
         * 参  数: maxOffset - 最大偏移
         *         offset - 当前偏移
         * 返回值: 无
         */
        void onPull(float maxOffset, float offset);

        /**
         * 功  能: 滑动终止
         * 参  数: 无
         * 返回值: 无
         */
        void onPullAbort();

        /**
         * 功  能: 刷新中
         * 参  数: 无
         * 返回值: 无
         */
        void onRefreshing();

        /**
         * 功  能: 刷新结束
         * 参  数: 无
         * 返回值: 无
         */
        void onRefreshOver();
    }
}
