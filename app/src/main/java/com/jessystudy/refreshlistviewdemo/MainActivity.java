package com.jessystudy.refreshlistviewdemo;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jessystudy.refreshlistviewdemo.view.BaseRefreshAdapter;
import com.jessystudy.refreshlistviewdemo.view.DemoAdapter;
import com.jessystudy.refreshlistviewdemo.view.MyRefreshListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyRefreshListView.MyOnRefreshListener {

    private MyRefreshListView myList;
    private DemoAdapter demoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myList = (MyRefreshListView) findViewById(R.id.mylist);
        demoAdapter = new DemoAdapter(this);
        demoAdapter.setOnItemClickListener(new BaseRefreshAdapter.OnItemClickListener<String>() {
            @Override
            public void onItemClick(View view, String bean, int position) {
                Toast.makeText(MainActivity.this, bean, Toast.LENGTH_SHORT).show();
            }
        });
        myList.setAdapter(demoAdapter);
        myList.setMyOnRefreshListener(this);
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            strings.add("test:" + i);
        }
        demoAdapter.addItem(strings);
    }

    private Handler handler = new Handler();

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> strings = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    strings.add("refresh:" + i);
                }
                //设置adapter的模式为刷新模式
                demoAdapter.setMode(BaseRefreshAdapter.MODE_REFRESH);
                demoAdapter.addItem(strings);
                myList.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onLoadMore() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> strings = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    strings.add("loadmore:" + i);
                }
                //设置adapter的模式为加载更多模式
                demoAdapter.setMode(BaseRefreshAdapter.MODE_LOADMORE);
                demoAdapter.addItem(strings);
                //如果数据大于50，表示没有更多数据，将adapter的模式设置为没有更多模式
                if (demoAdapter.getAllData().size() > 50) {
                    demoAdapter.setMode(BaseRefreshAdapter.MODE_NO_MORE);
                }
            }
        }, 1000);
    }
}
