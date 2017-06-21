package com.example.live;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.view.View;

/**
 * Author:  jaron.ho
 * Date:    2017-04-30
 * Brief:   列表项间隙装饰
 */

public class SpaceItemDecoration extends ItemDecoration {
    private boolean mIsHorizontal = false;  // 是否水平间隙
    private int mSpace = 0; // 间隙值

    public SpaceItemDecoration(boolean isHorizontal, int space) {
        mIsHorizontal = isHorizontal;
        mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (0 != parent.getChildAdapterPosition(view)) {
            if (mIsHorizontal) {
                outRect.left = mSpace;
            } else {
                outRect.top = mSpace;
            }
        }
    }
}
