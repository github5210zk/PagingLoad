package com.zk.pagingload;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{
	private Context ctx;
	private List<News> list;
	public MyAdapter(Context ctx,List<News> list) {
		this.ctx=ctx;
		this.list=list;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			holder=new ViewHolder();
			convertView=View.inflate(ctx, R.layout.adapter, null);
			holder.titleTv=(TextView) convertView.findViewById(R.id.titleTv);
			holder.bodyTv=(TextView) convertView.findViewById(R.id.bodyTv);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.titleTv.setText(list.get(position).getTitle());
		holder.bodyTv.setText(list.get(position).getBody());
		return convertView;
	}
	private static class ViewHolder{
		private TextView titleTv,bodyTv;
		
	}
}
