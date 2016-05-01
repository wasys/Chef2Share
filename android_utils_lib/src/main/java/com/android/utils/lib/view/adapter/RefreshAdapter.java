package com.android.utils.lib.view.adapter;

import android.content.Context;

import java.util.List;

/**
 * 
 * @author Jonas Baggio
 * @create 29/12/2013 12:19:46
 */
public class RefreshAdapter<T> extends ListAdapter<T> {
	
	public RefreshAdapter(Context context, List<T> list){
		super(context, list);
	}
	
	public synchronized void refresAdapter(List<T> novosItens) {
		list.clear();
		list.addAll(novosItens);
	    notifyDataSetChanged();
	}
}
