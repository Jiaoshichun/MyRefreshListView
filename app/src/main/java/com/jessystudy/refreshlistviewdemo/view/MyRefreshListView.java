package com.jessystudy.refreshlistviewdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.jessystudy.refreshlistviewdemo.R;

/**
 * Created by chun on 2016/12/6.
 */
public class MyRefreshListView extends SwipeRefreshLayout {
    private RecyclerView recyclerView;
    private Context context;
    private int lastVisibleItem;
    private LinearLayoutManager linearLayoutManager;
    private MyItemDecoration myItemDecoration;

    public MyRefreshListView(Context context) {
        this(context, null);
    }

    public MyRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        // 加载自定义属性的值
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
                R.styleable.MyRecyclerAttrs);
        // 根据属性id获取属性值, 方式: R.styleable.名称_属性
        float dimension = typedArray.getDimension(R.styleable.MyRecyclerAttrs_divideHeight, 0);
        Drawable drawable = typedArray.getDrawable(R.styleable.MyRecyclerAttrs_divideDrawable);
        // 回收TypedArray, 释放内存
        typedArray.recycle();
        //根据属性值设置ListView的分割线
        myItemDecoration = new MyItemDecoration(drawable, dimension);
        init();
    }

    private void init() {
        recyclerView = new RecyclerView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        //为recyclerView设置布局管理器
        recyclerView.setLayoutManager(linearLayoutManager);
        //设置分割线
        recyclerView.addItemDecoration(myItemDecoration);
        addView(recyclerView, layoutParams);
        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (myOnRefreshListener != null) {
                    myOnRefreshListener.onRefresh();
                }
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //加载更多数据，最后一个条目显示，并且可以加载更多，并且还有更多数据，则加载更多数据
                if (newState == RecyclerView.SCROLL_STATE_IDLE && refreshAdapter != null && lastVisibleItem + 1 == refreshAdapter.getItemCount()) {
                    if (myOnRefreshListener != null && refreshAdapter.isHasLoadMore() && refreshAdapter.getMode() != BaseRefreshAdapter.MODE_NO_MORE) {
                        myOnRefreshListener.onLoadMore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }

        });

        //处理列表下拉和SwipeRefreshView的下拉刷新的事件冲突。
        recyclerView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (linearLayoutManager.getChildCount() == 0) {
                    setEnabled(true);
                } else if (linearLayoutManager.findFirstVisibleItemPosition() == 0) {
                    View firstVisibleItemView = linearLayoutManager.getChildAt(0);
                    if (firstVisibleItemView != null && firstVisibleItemView.getTop() == linearLayoutManager.getPaddingTop()) {
                        setEnabled(true);
                    } else {
                        setEnabled(false);
                    }
                } else {
                    setEnabled(false);
                }
                return false;
            }
        });

    }

    private BaseRefreshAdapter refreshAdapter;

    public void setAdapter(BaseRefreshAdapter adapter) {
        this.refreshAdapter = adapter;
        recyclerView.setAdapter(adapter);
    }

    public interface MyOnRefreshListener {
        void onRefresh();

        void onLoadMore();
    }

    private MyOnRefreshListener myOnRefreshListener;

    public void setMyOnRefreshListener(MyOnRefreshListener myOnRefreshListener) {
        this.myOnRefreshListener = myOnRefreshListener;
    }

}
