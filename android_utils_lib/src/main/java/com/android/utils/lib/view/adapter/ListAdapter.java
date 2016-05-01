package com.android.utils.lib.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Jonas Baggio
 * @create 29/12/2013 12:19:46
 */
public class ListAdapter<T> extends BaseAdapter {
	
	protected List<T> list;
	protected LayoutInflater inflater;
	private Context context;
	
	public ListAdapter(Context context, List<T> list){
		this.context = context;
		this.list = list;
		this.inflater = LayoutInflater.from(context);
	}
	
	public ListAdapter(Context context, T []array){
		this.list = new ArrayList<T>(Arrays.asList(array));
		this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		try{
			return list != null ? list.get(position) : null;
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}
	
	public List<T> getList(){
		return this.list;
	}

	public Context getContext() {
		return context;
	}
}
