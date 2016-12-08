package com.jessystudy.refreshlistviewdemo.view;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by chun on 2016/12/7.
 */
public class MyItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable divDrawable;
    private float divHeight;

    public MyItemDecoration(Drawable divDrawable, float divHeight) {
        this.divDrawable = divDrawable;
        this.divHeight = divHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            if (childView != null) {
                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childView.getLayoutParams();
                int top = layoutParams.bottomMargin + childView.getHeight();
                int bottom = (int) (top + divHeight + 0.5f);
                divDrawable.setBounds(left, top, right, bottom);
                divDrawable.draw(c);
            }
        }
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, (int) (divHeight + 0.5f));
    }
}
