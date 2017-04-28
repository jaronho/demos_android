package com.nongyi.nylive;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Author:  Administrator
 * Date:    2017/4/27
 * Brief:   RefreshLayout
 */

public class RefreshView extends RelativeLayout implements SpringLayout.Listener {
    private RelativeLayout mHeader = null;
    private RelativeLayout mFooter = null;
    private SpringLayout mViewLayout = null;
    private RecyclerView mView = null;
    private Creator mHeaderCreator = null;
    private Creator mFooterCreator = null;
    private View mHeaderView = null;
    private View mFooterView = null;
    private int mHeaderViewSize = 0;
    private int mFooterViewSize = 0;

    public RefreshView(Context context) {
        super(context);
        initialize();
    }

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public RefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RefreshView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && null != mHeaderView && mHeaderViewSize <= 0) {
            mHeaderViewSize = mViewLayout.isHorizontal() ? mHeaderView.getMeasuredWidth() : mHeaderView.getMeasuredHeight();
            setHeaderMargin(-mHeaderViewSize);
        }
        if (changed && null != mFooterView && mFooterViewSize <= 0) {
            mFooterViewSize = mViewLayout.isHorizontal() ? mFooterView.getMeasuredWidth() : mFooterView.getMeasuredHeight();
            setFooterMargin(-mFooterViewSize);
        }
    }

    @Override
    public boolean isCanDrag(boolean isHorizontal, boolean isForward) {
        if (isHorizontal) { // 水平滑动
            return isForward ? mView.canScrollHorizontally(-1) : mView.canScrollHorizontally(1);
        } else {    // 垂直滑动
            return isForward ? mView.canScrollVertically(-1) : mView.canScrollVertically(1);
        }
    }

    @Override
    public void onDrag(float maxOffset, float offset) {
        Log.d(RefreshView.class.getSimpleName(), "onDrag == maxOffset: "+maxOffset+", offet: "+offset);
    }

    @Override
    public void onRelease(float maxOffset, float offset) {
        Log.d(RefreshView.class.getSimpleName(), "onRelease == maxOffset: "+maxOffset+", offet: "+offset);
        mViewLayout.restore();
    }

    // 初始化
    private void initialize() {
        // 页眉
        mHeader = new RelativeLayout(getContext());
        addView(mHeader);
        // 页脚
        mFooter = new RelativeLayout(getContext());
        addView(mFooter);
        // 视图
        mViewLayout = new SpringLayout(getContext());
        mViewLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mViewLayout.setListener(this);
        addView(mViewLayout);
        mView = new RecyclerView(getContext());
        mView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mViewLayout.addView(mView);

        setHorizontal(mViewLayout.isHorizontal());
    }

    // 设置页眉marign
    private void setHeaderMargin(int margin) {
        if (margin < -mHeaderViewSize) {
            margin = -mHeaderViewSize;
        }
        Log.d(RefreshView.class.getSimpleName(), "setHeaderMargin == margin: "+margin);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)mHeader.getLayoutParams();
        if (mViewLayout.isHorizontal()) {
            lp.leftMargin = margin;
        } else {
            lp.topMargin = margin;
        }
        mHeader.setLayoutParams(lp);
    }

    // 设置页脚marign
    private void setFooterMargin(int margin) {
        if (margin < -mFooterViewSize) {
            margin = -mFooterViewSize;
        }
        Log.d(RefreshView.class.getSimpleName(), "setFooterMargin == margin: "+margin);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)mFooter.getLayoutParams();
        if (mViewLayout.isHorizontal()) {
            lp.rightMargin = margin;
        } else {
            lp.bottomMargin = margin;
        }
        mFooter.setLayoutParams(lp);
    }

    /**
     * 功  能: 获取视图
     * 参  数: 无
     * 返回值: RecyclerView
     */
    public RecyclerView getView() {
        return mView;
    }

    /**
     * 功  能: 设置页眉构造器
     * 参  数: creator - 页眉构造器
     * 返回值: 无
     */
    public void setHeaderCreator(Creator creator) {
        if (null != mHeaderCreator) {
            throw new AssertionError("exist header creator");
        }
        mHeaderCreator = creator;
        mHeaderView = creator.createView(getContext(), mHeader);
        if (null != mHeaderView) {
            mHeader.addView(mHeaderView);
        }
    }

    /**
     * 功  能: 设置页脚构造器
     * 参  数: creator - 页脚构造器
     * 返回值: 无
     */
    public void setFooterCreator(Creator creator) {
        if (null != mFooterCreator) {
            throw new AssertionError("exist footer creator");
        }
        mFooterCreator = creator;
        mFooterView = creator.createView(getContext(), mFooter);
        if (null != mFooterView) {
            mFooter.addView(mFooterView);
        }
    }

    /**
     * 功  能: 设置水平滑动
     * 参  数: isHorizontal - 水平滑动
     * 返回值: 无
     */
    public void setHorizontal(boolean isHorizontal) {
        mViewLayout.setHorizontal(isHorizontal);
        // 页眉位置
        RelativeLayout.LayoutParams headerLP = (RelativeLayout.LayoutParams)mHeader.getLayoutParams();
        if (isHorizontal) {
            headerLP.width = LayoutParams.WRAP_CONTENT;
            headerLP.height = LayoutParams.MATCH_PARENT;
            headerLP.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        } else {
            headerLP.width = LayoutParams.MATCH_PARENT;
            headerLP.height = LayoutParams.WRAP_CONTENT;
            headerLP.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        }
        mHeader.setLayoutParams(headerLP);
        // 页脚位置
        RelativeLayout.LayoutParams footerLP = (RelativeLayout.LayoutParams)mFooter.getLayoutParams();
        if (isHorizontal) {
            footerLP.width = LayoutParams.WRAP_CONTENT;
            footerLP.height = LayoutParams.MATCH_PARENT;
            footerLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        } else {
            footerLP.width = LayoutParams.MATCH_PARENT;
            footerLP.height = LayoutParams.WRAP_CONTENT;
            footerLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }
        mFooter.setLayoutParams(footerLP);
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
        View createView(Context context, RelativeLayout parent);

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
