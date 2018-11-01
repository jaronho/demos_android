package com.gsclub.strategy.ui.view.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gsclub.strategy.R;
import com.gsclub.strategy.widget.RoundImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * 广告轮播控件
 * Created by xiwen on 2016/4/12.
 */
public class Banner extends RelativeLayout {
    private static final String TAG = Banner.class.getSimpleName();
    /**
     * 布局参数
     */
    private static final int RMP = LayoutParams.MATCH_PARENT;
    private static final int RWC = LayoutParams.WRAP_CONTENT;
    /**
     * 存放轮播信息的数据集合
     */
    protected List mData = new ArrayList<>();
    /**
     * 当前的页面的位置
     */
    protected int currentPosition;
    /**
     * 任务执行器
     */
    protected ScheduledExecutorService mExecutor;
    /**
     * 存放点的容器
     */
    BezierLinearPointsView pointsView;
    private Context mContext;
    /**
     * 循环轮播的Viewpager
     */
    private SLooperViewPager mViewPager;
    /**
     * 点在容器中的layout的属性
     */
    private int mPointGravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    private int mPointLeftRightMargin;
    private int mPointTopBottomMargin;
    private int mPointContainerLeftRightPadding;
    /**
     * 自动播放的间隔
     */
    private int mAutoPlayInterval = 3;
    /**
     * 页面切换的时间（从下一页开始出现，到完全出现的时间）
     */
    private int mPageChangeDuration = 300;
    /**
     * 是否正在播放
     */
    private boolean mPlaying = false;
    /**
     * Banner控件的适配器
     */
    private BannerAdapter mBannerAdapter;
    /**
     * banner小圆点大小（dp）
     */
    private int pointSize = 4;

    private int pointNormalColor = Color.parseColor("#80ffffff");

    private int pointSelectColor = Color.parseColor("#fd9126");

    private int pointDistanceToBottom = 15;

    private int mViewPagerPadding = 0;//设置banner中viewpager的padding


    /**
     * 播放下一个执行器
     */
    private Handler mPlayHandler = new PlayHandler(this);
    private OnBannerItemClickListener onVpItemClickListener;

    public Banner(Context context) {
        this(context, null);
    }

    public Banner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Banner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //初始化默认属性
        initDefaultAttrs(context);

        //初始化自定义属性
        initCustomAttrs(context, attrs);

        //控件初始化
        initView(context);
    }

    private void initDefaultAttrs(Context context) {

        //默认点指示器的左右Margin3dp
        mPointLeftRightMargin = dp2px(context, 3);
        //默认点指示器的上下margin为6dp
        mPointTopBottomMargin = dp2px(context, 6);
        //默认点容器的左右padding为10dp
        mPointContainerLeftRightPadding = dp2px(context, 0);
    }

    private int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    private int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    /**
     * 初始化自定义属性
     */
    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SivinBanner);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initCustomAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.SivinBanner_banner_pointLeftRightMargin) {
            //指示器左右边距
            mPointLeftRightMargin = typedArray.getDimensionPixelSize(attr, mPointLeftRightMargin);
        } else if (attr == R.styleable.SivinBanner_banner_pointContainerLeftRightPadding) {
            //指示器容器的左右padding
            mPointContainerLeftRightPadding = typedArray.getDimensionPixelSize(attr, mPointContainerLeftRightPadding);
        } else if (attr == R.styleable.SivinBanner_banner_pointTopBottomMargin) {
            //指示器的上下margin
            mPointTopBottomMargin = typedArray.getDimensionPixelSize(attr, mPointTopBottomMargin);
        } else if (attr == R.styleable.SivinBanner_banner_pointGravity) {
            //指示器在容器中的位置属性
            mPointGravity = typedArray.getInt(attr, mPointGravity);
        } else if (attr == R.styleable.SivinBanner_banner_pointAutoPlayInterval) {
            //轮播的间隔
            mAutoPlayInterval = typedArray.getInteger(attr, mAutoPlayInterval);
        } else if (attr == R.styleable.SivinBanner_banner_pageChangeDuration) {
            //页面切换的持续时间
            mPageChangeDuration = typedArray.getInteger(attr, mPageChangeDuration);
        } else if (attr == R.styleable.SivinBanner_banner_ViewPager_padding) {
            //viewPager的padding
            mViewPagerPadding = typedArray.getDimensionPixelSize(attr, mViewPagerPadding);
        } else if (attr == R.styleable.SivinBanner_banner_pointWidth) {
            pointSize = dp2px(getContext(), pointSize);
            pointSize = typedArray.getDimensionPixelSize(attr, pointSize);
        }

    }

    private void initView(Context context) {
        mContext = context;


        //初始化ViewPager
        mViewPager = new SLooperViewPager(context);

        mViewPager.setOffscreenPageLimit(4);

        mViewPager.setPageMargin(dp2px(context, mViewPagerPadding));
        mViewPager.setClipToPadding(false);
        mViewPager.setPadding(mViewPagerPadding, 0, mViewPagerPadding, 0);
        //以matchParent的方式将viewPager填充到控件容器中
        addView(mViewPager, new LayoutParams(RMP, RMP));

        //修正banner页面切换时间
        mPageChangeDuration = mPageChangeDuration > (mAutoPlayInterval * 1000) ? (mAutoPlayInterval * 1000) : mPageChangeDuration;

        // 设置banner轮播的切换时间
        ViewPagerScroller pagerScroller = new ViewPagerScroller(mContext);
        pagerScroller.changScrollDuration(mViewPager, mPageChangeDuration);
    }

    /**
     * 初始化点
     */
    private void initPoints() {
        //获取目标点的数据量
        int dataSize = mData.size();

        if (pointsView != null)
            removeView(pointsView);
        pointsView = null;

        //初始化存放点的容器线性布局
        pointsView = new BezierLinearPointsView(mContext, dataSize);
        pointsView.setPointSize(pointSize);
//        pointsView.setPaintStrokeWidth(dp2px(context, 3));
        pointsView.setSelectedColor(pointSelectColor);
        pointsView.setUnSelectedColor(pointNormalColor);
        pointsView.setCount(dataSize);
        //设置点容器的布局参数
        LayoutParams pointContainerLp = new LayoutParams(RWC, RWC);
        pointContainerLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        pointContainerLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        pointContainerLp.bottomMargin = dp2px(mContext, pointDistanceToBottom);
        //将点容器存放到指示器容器中
        addView(pointsView, pointContainerLp);
    }

    /**
     * 将点切换到指定的位置
     * 就是将指定位置的点设置成Enable
     *
     * @param newCurrentPoint 新位置
     */
    private void switchToPoint(int newCurrentPoint) {
        if (newCurrentPoint < 0 || newCurrentPoint >= pointsView.getCount())
            return;

        pointsView.selectIndex(newCurrentPoint);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                pauseScroll();
                break;
            case MotionEvent.ACTION_UP:
                goScroll();
                break;
            case MotionEvent.ACTION_CANCEL:
                goScroll();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    public void setPageChangeDuration(int duration) {
        mPageChangeDuration = duration;
    }

    private void scrollToNextItem(int position) {
        position++;
        mViewPager.setCurrentItem(position, true);
    }

    /**
     * 设置viewPage的Item点击监听器
     */
    public void setOnBannerItemClickListener(OnBannerItemClickListener listener) {
        this.onVpItemClickListener = listener;
    }

    /**
     * 方法使用状态 ：viewpager处于暂停的状态
     * 开始滚动
     */
    public void goScroll() {
        if (!isValid()) {
            return;
        }
        if (!mPlaying) {
            pauseScroll();
            mExecutor = Executors.newSingleThreadScheduledExecutor();
            //command：执行线程
            //initialDelay：初始化延时
            //period：两次开始执行最小间隔时间
            //unit：计时单位
            mExecutor.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    mPlayHandler.obtainMessage().sendToTarget();
                }
            }, mAutoPlayInterval, mAutoPlayInterval, TimeUnit.SECONDS);
            mPlaying = true;
        }
    }

    /**
     * 暂停滚动
     */
    public void pauseScroll() {
        if (mExecutor != null) {
            mExecutor.shutdown();
            mExecutor = null;
        }
        mPlaying = false;
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            goScroll();
        } else if (visibility == INVISIBLE) {
            pauseScroll();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        pauseScroll();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        goScroll();
    }

    /**
     * 判断控件是否可用
     */
    private boolean isValid() {
        if (mViewPager == null) {
            Log.e(TAG, "ViewPager is not exist!");
            return false;
        }
        if (mData == null || mData.size() == 0) {
            Log.e(TAG, "DataList must be not empty!");
            return false;
        }
        return true;
    }

    /**
     * 设置数据的集合
     */
    private void setSource() {
        List list = mBannerAdapter.getDataList();
        if (list == null) {
            return;
        }
        this.mData = list;
        setAdapter();
    }

    /**
     * 给viewpager设置适配器
     */
    private void setAdapter() {
        mViewPager.setAdapter(new InnerPagerAdapter());
        mViewPager.addOnPageChangeListener(new ChangePointListener());
    }

    public void setBannerAdapter(BannerAdapter adapter) {
        mBannerAdapter = adapter;
        setSource();
    }

    /**
     * 通知数据已经放生改变
     */
    public void notifyDataHasChanged() {
        initPoints();
        mViewPager.getAdapter().notifyDataSetChanged();
        mViewPager.setCurrentItem(0, false);
        if (mData.size() > 1)
            goScroll();
    }

    public interface OnBannerItemClickListener {
        void onItemClick(int position);
    }

    /**
     * 静态内部类，防止发生内存泄露
     */
    private static class PlayHandler extends Handler {
        WeakReference<Banner> mWeakBanner;

        PlayHandler(Banner banner) {
            this.mWeakBanner = new WeakReference<>(banner);
        }

        @Override
        public void handleMessage(Message msg) {
            Banner weakBanner = mWeakBanner.get();
            if (weakBanner != null)
                weakBanner.scrollToNextItem(weakBanner.currentPosition);
        }
    }

    private final class ChangePointListener extends SLooperViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            currentPosition = position % mData.size();
            switchToPoint(currentPosition);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }
    }

    /**
     * viewPager inner adapter
     */
    private final class InnerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
//            RoundImageView view = new RoundImageView(mContext);
            ImageView view = new ImageView(mContext);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            view.setScaleType(ImageView.ScaleType.FIT_CENTER);
            mBannerAdapter.setImageViewSource(view, position);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onVpItemClickListener != null) {
                        onVpItemClickListener.onItemClick(position);
                    }
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }


}
