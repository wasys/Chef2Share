package com.android.utils.lib.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Integer[] mImageIds;
	private int mGalleryItemBackground;
	private Context mContext;

    public ImageAdapter(Context c,Integer[] mImageIds ) {
        this.mContext = c;
		this.mImageIds = mImageIds;
    }

    public int getCount() {
        return mImageIds != null ? mImageIds.length : 0;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView i = new ImageView(mContext);

        Integer img = mImageIds[position];
		i.setImageResource(img);
        i.setScaleType(ImageView.ScaleType.FIT_XY);
        i.setLayoutParams(new Gallery.LayoutParams(380, 150));

        // The preferred Gallery item background
        i.setBackgroundResource(mGalleryItemBackground);

        return i;
    }
}