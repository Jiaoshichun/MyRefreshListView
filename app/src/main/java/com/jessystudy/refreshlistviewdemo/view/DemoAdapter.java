package com.jessystudy.refreshlistviewdemo.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jessystudy.refreshlistviewdemo.R;


/**
 * Created by chun on 2016/12/7.
 */
public class DemoAdapter extends BaseRefreshAdapter<String, DemoAdapter.ViewHolder> {
    public DemoAdapter(Context context) {
        super(context);
    }

    @Override
    protected  DemoAdapter.ViewHolder getViewHolder(ViewGroup parent) {
        View itemView = getView(parent, R.layout.adapter_demo);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    protected void setItemData(ViewHolder holder, String bean) {
        holder.txt.setText(bean);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt;

        public ViewHolder(View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.txt);
        }
    }
}
