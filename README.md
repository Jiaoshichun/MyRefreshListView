# MyRefreshListView
支持下拉刷新，上拉加载更多的listview。使用SwipRefreshView和RecylerView来实现。使用更简洁，更易用

##使用方式  
1.view包中的三个类拷贝到自己项目中，并在项目的attrs中添加  
    <declare-styleable name="MyRecyclerAttrs">
        <attr name="divideHeight" format="dimension" />
        <attr name="divideDrawable" format="reference" />
    </declare-styleable>  
    将layout文件夹下的listview_foot拷贝到自己项目中，并将报错的地方拷贝过去即可。  
2.新建自己的adapter  
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
3.在Aciticy中添加MyRefreshListView  
  <?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:custom="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.jessystudy.refreshlistviewdemo.view.MyRefreshListView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:padding="10dp"
        android:id="@+id/mylist"
        custom:divideHeight="4dp"
        custom:divideDrawable="@color/bg"/>
</RelativeLayout>  
4.在activity中使用  
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


