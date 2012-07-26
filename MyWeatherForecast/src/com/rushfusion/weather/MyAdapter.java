package com.rushfusion.weather;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	
	private List<String> mList;
	private Context mContext;
	public MyAdapter(Context myWeatherActivity, List<String> list) {
		  mContext =myWeatherActivity;
		  mList=list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int i) {
		// TODO Auto-generated method stub
		return mList.get(i);
	}

	@Override
	public long getItemId(int i) {
		// TODO Auto-generated method stub
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewgroup) {
		ViewHolder holder;
		LayoutInflater inflater = LayoutInflater.from(mContext);
		if(view==null){
		view  = inflater.inflate(R.layout.spiner, null);
		holder = new ViewHolder();
		holder.spinner_textview =(TextView) view.findViewById(R.id.spinner_textview);
		view.setTag(holder);
		}else{
			holder =(ViewHolder) view.getTag();
		}
		holder.spinner_textview.setText(mList.get(i));
		return view;
	}
	static class ViewHolder{
		private TextView  spinner_textview;
	}
}
