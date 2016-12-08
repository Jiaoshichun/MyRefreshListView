package com.jessystudy.refreshlistviewdemo.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.jessystudy.refreshlistviewdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chun on 2016/12/6.
 */
public abstract class BaseRefreshAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected Context context;
    private ArrayList<T> mData = new ArrayList<>();
    private boolean hasLoadMore = true;
    public static final int MODE_LOADMORE = 10;
    public static final int MODE_NO_MORE = 11;
    public static final int MODE_REFRESH = 12;
    private int current_mode = MODE_REFRESH;
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    private OnItemClickListener<T> listener;

    public BaseRefreshAdapter(Context context) {
        this.context = context;
    }

    /**
     * @param context
     * @param hasLoadMore 当前的adapter是否可以加载更多，默认可以
     */
    public BaseRefreshAdapter(Context context, boolean hasLoadMore) {
        this.context = context;
        this.hasLoadMore = hasLoadMore;
    }

    protected abstract VH getViewHolder(ViewGroup parent);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viwHolder = null;
        if (viewType == TYPE_ITEM)
            //进行判断显示类型，来创建返回不同的View
            viwHolder = getViewHolder(parent);
        else if (viewType == TYPE_FOOTER) {
            View foot_view = LayoutInflater.from(context).inflate(R.layout.listview_foot, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            FootViewHolder footViewHolder = new FootViewHolder(foot_view);

            viwHolder = footViewHolder;
        }
        if (viwHolder != null) {
            final RecyclerView.ViewHolder finalViwHolder = viwHolder;
            viwHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = (Integer) finalViwHolder.itemView.getTag();
                        listener.onItemClick(v, mData.get(position), position);
                    }
                }
            });
        }
        return viwHolder;
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener<T> {
        /**
         *
         * @param view  被点击的itemview
         * @param bean 被点击的itemview绑定的bean对象
         * @param position 被点击的条目位置
         */
        void onItemClick(View view, T bean, int position);
    }

    /**
     * 进行判断是普通Item视图还是FootView视图
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (hasLoadMore && position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < mData.size()) {
            setItemData((VH) holder, mData.get(position));
            holder.itemView.setTag(position);
        } else {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (current_mode) {
                case MODE_NO_MORE:
                    footViewHolder.txtFooter.setText(R.string.txt_listview_no_more);
                    footViewHolder.progress.setVisibility(View.GONE);
                    break;
                case MODE_LOADMORE:
                case MODE_REFRESH:
                    footViewHolder.txtFooter.setText(R.string.txt_listview_loading);
                    footViewHolder.progress.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }


    }

    /**
     * 设置条目内容
     *
     * @param holder
     * @param bean
     */
    protected abstract void setItemData(VH holder, T bean);

    @Override
    public int getItemCount() {
        return hasLoadMore ? mData.size() + 1 : mData.size();
    }

    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        private TextView txtFooter;
        private View progress;

        public FootViewHolder(View view) {
            super(view);
            txtFooter = (TextView) view.findViewById(R.id.txt_list_footer);
            progress = view.findViewById(R.id.progress_list_footer);
        }

    }


    public void addItem(List<T> newDatas) {
        if (current_mode == MODE_LOADMORE) {
            mData.addAll(newDatas);
        } else if (current_mode == MODE_REFRESH) {
            mData.clear();
            mData.addAll(newDatas);
        }
        notifyDataSetChanged();
    }

    //设置当前的模式
    public void setMode(int mode) {
        current_mode = mode;
        notifyDataSetChanged();
    }

    //获取当前的模式
    public int getMode() {
        return current_mode;
    }

    //当前adapter是否可以加载更多
    public boolean isHasLoadMore() {
        return hasLoadMore;
    }

    //用于将adapter的布局文件转换成view
    protected View getView(ViewGroup parent, int layout) {
        return LayoutInflater.from(context).inflate(layout, parent, false);
    }

    /**
     * 获取列表中的全部数据
     *
     * @return
     */
    public ArrayList<T> getAllData() {
        return mData;
    }

    /**
     * 获取指定位置的数据
     *
     * @param position
     * @return
     */
    public T getData(int position) {
        if (position < mData.size()) {
            return mData.get(position);
        }
        return null;
    }
}
