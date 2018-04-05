package com.zk.pagingload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener, OnScrollListener{
	private ListView                      mLv;
	private List<News>                    list;
	private MyAdapter                     adapter;
	private Button                        mBtn;
	private ProgressBar                   mBar;
	private List<HashMap<String, String>> map;
	private View                          footerview;//底部的视图
	private int maxNum=50;//模拟数据的最大条数
	private int lastVisIdnex;//当前界面最后可见的条目
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		list=new ArrayList<News>();
		mLv=(ListView) findViewById(R.id.mLv);
		footerview=View.inflate(this, R.layout.footerview, null);
		mLv.addFooterView(footerview);//将底部视图添加到listview
		//mLv.addHeaderView(footerview)；添加头部视图
		mBtn=(Button) footerview.findViewById(R.id.mBtn);
		mBar=(ProgressBar) footerview.findViewById(R.id.mBar);
		//初始化数据源
		initList();
		adapter=new MyAdapter(this, list);
		mLv.setAdapter(adapter);

		//点击分页加载
//		mBtn.setOnClickListener(this);
		//滑动分页加载
		mLv.setOnScrollListener(this);
	}
	private Handler hand=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==0){
				mBar.setVisibility(View.GONE);
				mBtn.setVisibility(View.GONE);
				//调用加载更多的方法，刷新适配器
				loadMore();
				adapter.notifyDataSetChanged();
			}else if (msg.what==1){
				mBar.setVisibility(View.GONE);
				mBtn.setVisibility(View.GONE);
				//调用加载更多的方法，刷新适配器
				loadMore();
				mBtn.setVisibility(View.VISIBLE);
				adapter.notifyDataSetChanged();
			}
		}
	};
	private void initList() {
		for(int i=0;i<10;i++){
			News news=new News();
			news.setTitle("第"+(i+1)+"条数据");
			news.setBody("第"+(i+1)+"条数据");
			list.add(news);
		}
	}
	@Override
	public void onClick(View v) {
		mBtn.setVisibility(View.GONE);
		mBar.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					//子线程延迟发送一条空消息
					hand.sendEmptyMessage(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	//自己写的每次增加多少的方法
	private void loadMore(){
		int count=adapter.getCount();//获得当前总共有多少条目
		if((count+10)<maxNum){
			for(int i=count;i<count+10;i++){
				News nees=new News();
				nees.setTitle("第"+(i+1)+"条数据");
				nees.setBody("第"+(i+1)+"条数据");
				list.add(nees);
			}
		}else{
			//剩余条目不足10条时
			for(int i=count;i<maxNum;i++){
				News nees1=new News();
				nees1.setTitle("第"+(i+1)+"条数据");
				nees1.setBody("第"+(i+1)+"条数据");
				list.add(nees1);
			}
		}

	}
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		//当状态更改为点击滑动时把Button按钮隐藏掉
		if (scrollState==SCROLL_STATE_TOUCH_SCROLL){
			mBtn.setVisibility(View.GONE);
		}
		if(scrollState==SCROLL_STATE_IDLE&&lastVisIdnex==adapter.getCount()){
			//确认滑倒底部 加载更多
			mBar.setVisibility(View.VISIBLE);
			mBtn.setVisibility(View.GONE);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
						hand.sendEmptyMessage(0);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}).start();
		}
	}
	/*
	 * firstVisibleItem:当前界面显示的第一条item的下标，未完全显示也算
	 * visibleItemCount：当前界面内显示的item条数（未完全显示的也算）
	 * totalItemCount：整个ListView全部的item的条目数
	 * */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
						 int visibleItemCount, int totalItemCount) {
		lastVisIdnex=firstVisibleItem+visibleItemCount-1;
		if(totalItemCount==maxNum+1){
			mLv.removeFooterView(footerview);
			Toast.makeText(this, "数据加载完成，无法更新", Toast.LENGTH_SHORT).show();
		}
	}

}
