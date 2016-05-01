package com.android.utils.lib.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.utils.lib.utils.ImageDownloaderTask;

import java.util.List;

/**
 * @author Ricardo Lecheta
 *
 */
public class ImageUrlAdapter extends BaseAdapter {
	private Context mContext;
	private final ImageDownloaderTask downloader;
	private final List<String> urls;

    public ImageUrlAdapter(Context c,List<String> urls, ImageDownloaderTask downloader ) {
        this.mContext = c;
		this.urls = urls;
		this.downloader = downloader;
    }

    public int getCount() {
        return urls != null ? urls.size() : 0;
    }

    public Object getItem(int idx) {
        return idx;
    }

    public long getItemId(int idx) {
        return idx;
    }

    public View getView(int idx, View convertView, ViewGroup parent) {
    	FrameLayout frame = new FrameLayout(mContext);
    	ImageView imageView= new ImageView(mContext);
        ProgressBar progress= new ProgressBar(mContext);
        
        frame.addView(imageView);
        frame.addView(progress);
        
        progress.setVisibility(View.GONE);

        String url = urls.get(idx);

        downloader.download(url, imageView, progress);

        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        
        frame.setLayoutParams(new Gallery.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        imageView.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.MATCH_PARENT, Gallery.LayoutParams.MATCH_PARENT));

        return frame;
    }
}