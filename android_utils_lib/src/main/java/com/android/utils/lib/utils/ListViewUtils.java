package com.android.utils.lib.utils;

import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @author Ricardo Lecheta
 *
 */
public class ListViewUtils {

	public static void reload(ListView listView) {
		if(listView != null) {
			ListAdapter adapter = listView.getAdapter();
			
			if(adapter instanceof BaseAdapter) {
				((BaseAdapter) adapter).notifyDataSetChanged();
			}
		}
	}
}
