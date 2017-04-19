# TwinklingRefreshLayout
TwinklingRefreshLayout延伸了Google的SwipeRefreshLayout的思想,不在列表控件上动刀,而是使用一个ViewGroup来包含列表控件,以保持其较低的耦合性和较高的通用性。其主要特性有：

1. 支持RecyclerView、ScrollView、AbsListView系列(ListView、GridView)、WebView以及其它可以获取到scrollY的控件
2. 支持加载更多
3. 默认支持 **越界回弹**，随手势速度有不同的效果
4. 可开启没有刷新控件的纯净越界回弹模式
5. setOnRefreshListener中拥有大量可以回调的方法
6. 将Header和Footer抽象成了接口,并回调了滑动过程中的系数,方便实现个性化的Header和Footer
7. 支持NestedScroll,嵌套CoordinatorLayout

**目前已经支持了所有的View，比如是一个FrameLayout，LinearLayout,AnyView。**

![](art/structure_v1.0.png)

## Demo
[下载Demo](art/app-debug.apk)

![](art/gif_recyclerview.gif)  ![](art/gif_listview.gif)  ![](art/gif_gridview.gif) ![](art/gif_recyclerview2.gif) ![](art/gif_scrollview.gif)  ![](art/gif_webview.gif)

You can download these Videos for more details.

- [Music - ListView - FixedHeader](art/gif_listview.mp4)
- [Food - RecyclerView - PureScrollMode](art/gif_recyclerview.mp4)
- [Science - GridView - SinaHeader](art/gif_gridview.mp4)
- [Photo - RecyclerView - BezierLayout](art/gif_recyclerview2.mp4)
- [Story - ScrollView - GoogleDotView](art/gif_scrollview.mp4)
- [Dribbble - WebView - FloatRefresh](art/gif_webview.mp4)

## 使用方法
#### 1.添加gradle依赖
将libray模块复制到项目中,或者直接在build.gradle中依赖:
```
compile 'com.lcodecorex:tkrefreshlayout:1.0.7'
```

#### 2.在xml中添加TwinklingRefreshLayout
```xml
<?xml version="1.0" encoding="utf-8"?>
<com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/refreshLayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:tr_wave_height="180dp"
    app:tr_head_height="100dp">

    <android.support.v7.widget.RecyclerView
        android:id="@+id/recyclerview"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:overScrollMode="never"
        android:background="#fff" />
</com.lcodecore.library.TwinklingRefreshLayout>
```

Android系统为了跟iOS不一样，当界面OverScroll的时候会显示一个阴影。为了达到更好的显示效果，最好禁用系统的overScroll，如上给RecyclerView添加`android:overScrollMode="never"`。

#### 3.在Activity或者Fragment中配置
##### TwinklingRefreshLayout不会自动结束刷新或者加载更多，需要手动控制
```java
refreshLayout.setOnRefreshListener(new RefreshListenerAdapter(){
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefreshing();
                    }
                },2000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishLoadmore();
                    }
                },2000);
            }
        });
    }
```
使用finishRefreshing()方法结束刷新，finishLoadmore()方法结束加载更多。此处OnRefreshListener还有其它方法，可以选择需要的来重写。

如果你想进入到界面的时候主动调用下刷新，可以调用startRefresh()/startLoadmore()方法。

##### setWaveHeight、setHeaderHeight、setBottomHeight、setOverScrollHeight
- setMaxHeadHeight 设置头部可拉伸的最大高度。
- setHeaderHeight 头部固定高度(在此高度上显示刷新状态)
- setMaxBottomHeight
- setBottomHeight 底部高度
- setOverScrollHeight 设置最大的越界高度

#### setEnableRefresh、setEnableLoadmore
灵活的设置是否禁用上下拉。

##### setHeaderView(IHeaderView headerView)、setBottomView(IBottomView bottomView)
设置头部/底部个性化刷新效果，头部需要实现IHeaderView，底部需要实现IBottomView。

#### setEnableOverScroll
是否允许越界回弹。

##### setOverScrollTopShow、setOverScrollBottomShow、setOverScrollRefreshShow
是否允许在越界的时候显示刷新控件，默认是允许的，也就是Fling越界的时候Header或Footer照常显示，反之就是不显示；可能有特殊的情况，刷新控件会影响显示体验才设立了这个状态。

##### setPureScrollModeOn()
开启纯净的越界回弹模式，也就是所有刷新相关的View都不显示，只显示越界回弹效果

##### setAutoLoadMore
是否在底部越界的时候自动切换到加载更多模式

##### addFixedExHeader
添加一个固定在顶部的Header(效果还需要优化)

##### startRefresh、startLoadMore、finishRefreshing、finishLoadmore

##### setFloatRefresh(boolean)
支持切换到像SwipeRefreshLayout一样的悬浮刷新模式了。

##### setTargetView(View view)
设置滚动事件的作用对象。

##### setDefaultHeader、setDefaultFooter
现在已经提供了设置默认的Header、Footer的static方法，可在Application或者一个Activity中这样设置：
```java
TwinklingRefreshLayout.setDefaultHeader(SinaRefreshView.class.getName());
TwinklingRefreshLayout.setDefaultFooter(BallPulseView.class.getName());
```


#### 4.扩展属性
- tr_max_head_height 头部拉伸允许的最大高度
- tr_head_height  头部高度
- tr_max_bottom_height
- tr_bottom_height 底部高度
- tr_overscroll_height 允许越界的最大高度
- tr_enable_refresh 是否允许刷新,默认为true
- tr_enable_loadmore 是否允许加载更多,默认为true
- tr_pureScrollMode_on 是否开启纯净的越界回弹模式
- tr_overscroll_top_show - 否允许顶部越界时显示顶部View
- tr_overscroll_bottom_show 是否允许底部越界时显示底部View
- tr_enable_overscroll 是否允许越界回弹
- tr_floatRefresh 开启悬浮刷新模式
- tr_autoLoadMore 越界时自动加载更多
- tr_enable_keepIView 是否在开始刷新之后保持状态，默认为true；若需要保持原来的操作逻辑，这里设置为false即可
- tr_showRefreshingWhenOverScroll 越界时直接显示正在刷新中的头部
- tr_showLoadingWhenOverScroll 越界时直接显示正在加载更多中的底部

## 其它说明
### 1.默认支持越界回弹，并可以随手势越界不同的高度
这一点很多类似SwipeRefreshLayout的刷新控件都没有做到(包括SwipeRefreshLayout),因为没有拦截下来的时间会传递给列表控件，而列表控件的滚动状态很难获取。解决方案就是给列表控件设置了OnTouchListener并把事件交给GestureDetector处理,然后在列表控件的OnScrollListener中监听View是否滚动到了顶部(没有OnScrollListener的则采用延时监听策略)。

### 2.setOnRefreshListener大量可以回调的方法
- onPullingDown(TwinklingRefreshLayout refreshLayout, float fraction)  正在下拉的过程
- onPullingUp(TwinklingRefreshLayout refreshLayout, float fraction)    正在上拉的过程
- onPullDownReleasing(TwinklingRefreshLayout refreshLayout, float fraction)  下拉释放过程
- onPullUpReleasing(TwinklingRefreshLayout refreshLayout, float fraction)  上拉释放过程
- onRefresh(TwinklingRefreshLayout refreshLayout)  正在刷新
- onLoadMore(TwinklingRefreshLayout refreshLayout)  正在加载更多

其中fraction表示当前下拉的距离与Header高度的比值(或者当前上拉距离与Footer高度的比值)。

### 3.Header和Footer
##### BezierLayout(pic 4)
- setWaveColor
- setRippleColor

##### GoogleDotView(pic 5)
##### SinaRefreshView(pic 3)
- setArrowResource
- setTextColor
- setPullDownStr
- setReleaseRefreshStr
- setRefreshingStr

##### ProgressLayout(SwipeRefreshLayout pic 6)
- setProgressBackgroundColorSchemeResource(@ColorRes int colorRes)
- setProgressBackgroundColorSchemeColor(@ColorInt int color)
- setColorSchemeResources(@ColorRes int... colorResIds)

####Footer
##### BallPulseView(pic 2)
- setNormalColor(@ColorInt int color)
- setAnimatingColor(@ColorInt int color)

##### LoadingView(pic 3)
更多动效可以参考[AVLoadingIndicatorView](https://github.com/81813780/AVLoadingIndicatorView)库。


### 3.实现个性化的Header和Footer
相关接口分别为IHeaderView和IBottomView,代码如下:
```java
public interface IHeaderView {
    View getView();

    void onPullingDown(float fraction,float maxHeadHeight,float headHeight);

    void onPullReleasing(float fraction,float maxHeadHeight,float headHeight);

    void startAnim(float maxHeadHeight,float headHeight);

    void reset();
}
```

其中getView()方法用于在TwinklingRefreshLayout中获取到实际的Header,因此不能返回null。

**实现像新浪微博那样的刷新效果**(有部分修改,具体请看源码),实现代码如下:

1.首先定义SinaRefreshHeader继承自FrameLayout并实现IHeaderView方法

2.getView()方法中返回this

3.在onAttachedToWindow()或者构造函数方法中获取一下需要用到的布局

4. 在onFinish()方法中调用listener.onAnimEnd()。此方法的目的是为了在finish之前可以执行一段动画。

```java
private void init() {
        View rootView = View.inflate(getContext(), R.layout.view_sinaheader, null);
        refreshArrow = (ImageView) rootView.findViewById(R.id.iv_arrow);
        refreshTextView = (TextView) rootView.findViewById(R.id.tv);
        loadingView = (ImageView) rootView.findViewById(R.id.iv_loading);
        addView(rootView);
    }
```

4.实现其它方法
```java
@Override
    public void onPullingDown(float fraction, float maxHeadHeight, float headHeight) {
        if (fraction < 1f) refreshTextView.setText(pullDownStr);
        if (fraction > 1f) refreshTextView.setText(releaseRefreshStr);
        refreshArrow.setRotation(fraction * headHeight / maxHeadHeight * 180);


    }

    @Override
    public void onPullReleasing(float fraction, float maxHeadHeight, float headHeight) {
        if (fraction < 1f) {
            refreshTextView.setText(pullDownStr);
            refreshArrow.setRotation(fraction * headHeight / maxHeadHeight * 180);
            if (refreshArrow.getVisibility() == GONE) {
                refreshArrow.setVisibility(VISIBLE);
                loadingView.setVisibility(GONE);
            }
        }
    }

    @Override
    public void startAnim(float maxHeadHeight, float headHeight) {
        refreshTextView.setText(refreshingStr);
        refreshArrow.setVisibility(GONE);
        loadingView.setVisibility(VISIBLE);
    }

    @Override
        public void onFinish(OnAnimEndListener listener) {
            listener.onAnimEnd();
        }
```

5.布局文件
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="horizontal" android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center">
    <ImageView
        android:id="@+id/iv_arrow"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@drawable/ic_arrow"/>

    <ImageView
        android:id="@+id/iv_loading"
        android:visibility="gone"
        android:layout_width="34dp"
        android:layout_height="34dp"
        android:src="@drawable/anim_loading_view"/>

    <TextView
        android:id="@+id/tv"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginLeft="16dp"
        android:textSize="16sp"
        android:text="下拉刷新"/>
</LinearLayout>
```

注意fraction的使用,比如上面的代码`refreshArrow.setRotation(fraction * headHeight / maxHeadHeight * 180)`，`fraction * headHeight`表示当前头部滑动的距离，然后算出它和最大高度的比例，然后乘以180，可以使得在滑动到最大距离时Arrow恰好能旋转180度。


onPullingDown/onPullingUp表示正在下拉/正在上拉的过程。
onPullReleasing表示向上拉/下拉释放时回调的状态。
startAnim则是在onRefresh/onLoadMore之后才会回调的过程（此处是显示了加载中的小菊花）

如上所示，轻而易举就可以实现一个个性化的Header或者Footer。（更简单的实现请参考Demo中的 **TextHeaderView(图四)**）。

### NestedScroll
#### TwinklingRefreshLayout嵌套CoordinatorLayout
---layout
```xml
<?xml version="1.0" encoding="utf-8"?>
<com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/refresh"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <android.support.design.widget.CoordinatorLayout
        android:id="@+id/coord_container"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:addStatesFromChildren="true"
        android:fitsSystemWindows="true">

        <android.support.design.widget.AppBarLayout
            android:id="@+id/appbar_layout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:clipChildren="false">

            <!--...-->

        </android.support.design.widget.AppBarLayout>

        <android.support.v7.widget.RecyclerView
            android:id="@+id/recyclerview"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layout_behavior="@string/appbar_scrolling_view_behavior" />

    </android.support.design.widget.CoordinatorLayout>
</com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout>
```

--- 代码1
```
refreshLayout.setTargetView(rv);
```
让refreshLayout能够找到RecyclerView/ListView

--- 代码2
```java
AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset >= 0) {
            refreshLayout.setEnableRefresh(true);
            refreshLayout.setEnableOverScroll(false);
        } else {
            refreshLayout.setEnableRefresh(false);
            refreshLayout.setEnableOverScroll(false);
        }
    }
});
```
设置AppBarLayout的移动监听器，需要下拉显示AppBarLayout时需设置setEnableRefresh(false),setEnableOverScroll(false)；AppBarLayout隐藏后还原为原来设置的值即可。

####CoordinatorLayout嵌套TwinklingRefreshLayout
--- layout
```xml
<?xml version="1.0" encoding="utf-8"?>
<android.support.design.widget.CoordinatorLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/coord_container"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:addStatesFromChildren="true"
    android:fitsSystemWindows="true">

    <com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
        android:id="@+id/refresh"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_behavior="@string/appbar_scrolling_view_behavior">

        <android.support.v7.widget.RecyclerView
            android:id="@+id/recyclerview"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />

    </com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout>

</android.support.design.widget.CoordinatorLayout>
```
注意给TwinklingRefreshLayout设置一个layout_behavior="@string/appbar_scrolling_view_behavior"

## TODO
- 制作一个star相关的动效
- 带视差效果的Header

> ps：如有任何问题或者是建议，可以邮箱联系我！（lcodecore@163.com）
> 如有问题或新的需求，请加QQ群202640706讨论，开源库会根据需求持续更新。

开源库消耗了我大量的精力和时间，如果你喜欢这个库或者对自己有所帮助，还请多多支持我。Buy me a coffee!

![](art/alipay.jpg) ![](art/wepay.png)

## 更新日志
#### v1.07
- 你们要的设置默认刷新头/脚的方法来啦
- Demo中集成StrictMode、BlockCanary检测ANR
- 支持NestedScroll
- 修复item点击失效/点击闪烁的问题
- Nested滑动显示刷新头/尾支持
- 支持刷新/加载更多状态保持
- 空白View亦可刷新/加载

#### v1.06
- 修复触摸监听失效问题
- 修复wrap_content时刷新控件显示在屏幕中央问题
- 去除AVLoadingIndicatorView等依赖，改为BallPulseView
- 优化加载更多完成时出现的闪烁问题
- 修复ValueAnimator以及Demo中WebView带来的内存泄漏问题
- 理论上解决了触摸、点击以及滚动监听失效等问题
- 新增setTargetView()方法，可设置滚动事件的作用对象
- 添加了CoordinateLayout demo(暂未在RefreshLayout中添加相关逻辑)
- 修复三星、酷派手机出现的兼容问题
- 修复禁用refresh、loadmore后overscroll不可用的问题
- 修复在顶部、底部fling时页面闪烁问题
- 修复IBottomView中的参数错误，新增max_head_height,max_bottom_height属性，setWaveHeight方法为setMaxHeadHeight

#### v1.05紧急修复版
- 修复底部自动加载更多问题
- 修复FixedHeader遮挡item问题
- RefreshListenerAdapter添加接口onRefreshCanceled()/onLoadmoreCanceled() 回调刷新被取消的状态
- 修复刷新状态重复回调问题
- 添加Apache License 2.0开源协议

#### v1.04
##### 新增功能
- **第二次重构完成**,将核心逻辑拆分为RefreshProcessor、AnimProcessor、OverScrollProcessor、CoProcessor
- **优化越界策越，手势决定越界高度**
- **优化界面流畅度**
- 添加类似SwipeRefreshLayout的**悬浮刷新**功能(ProgressLayout)
- 滑到底部**自动加载更多**or回弹可选，默认为回弹
- 允许在结束刷新之前执行一个动效：IHeadView.onFinish(animEndListener)
- 新增支持Header(Beta)
- 优化BezierLayout、SinaRefreshLayout等的显示并增加调节属性
- 新增支持设置是否允许OverScroll

##### fixed bugs
- 修复刷新或加载更多时，列表item没有铺满列表控件，滑动无效的问题
- 添加主动刷新/加载更多的方法：startRefresh(),startLoadMore()
- 修复顶部和底部越界高度不一致的问题
- 修复WebView在底部fling时不能越界的问题
- 动画执行时间与高度相关，动效更加柔和


#### v1.03
- 扩展了更多的属性
- 修复Fragment回收导致的空指针异常问题
- 加入x方向判断,减小了滑动冲突
- 优化加载更多列表显示问题
- 可以灵活的设置是否禁用上下拉
- 修复GridView滑动过程中出现的白条问题
- Demo中添加轮播条展示

#### v1.02
- 修复加载更多列表控件的显示问题

#### v1.01
- 支持了RecyclerView、ScrollView、AbsListView、WebView
- 支持越界回弹
- 支持个性化Header、Footer


License
-------

    Copyright 2016 lcodecorex

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
