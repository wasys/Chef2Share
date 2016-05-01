package com.android.utils.lib.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * @author rlecheta
 *
 */
public class VideoUtil implements OnPreparedListener, OnCompletionListener  {

	private final Context context;
	
	public static boolean LOG_ON = false;
	private static final String TAG = "video";
	private VideoView v;
	private boolean running;
	private OnCompletionListener onCompletionListener;
	private OnPreparedListener onPreparedListener;
	private MediaController mediaController;

	private View progress;

	public VideoUtil(Context context, VideoView v) {
		this.context = context;
		this.v = v;
		mediaController = new MediaController(context);
		mediaController.setAnchorView(v);
		v.setMediaController(mediaController);

        v.setOnPreparedListener(this);
        v.setOnCompletionListener(this);
	}
	
	public void play(String url) {
		play(url, null);
	}

	public void play(String url, View progress) {
		this.progress = progress;
		if(progress != null) {
			progress.setVisibility(View.VISIBLE);
		}
		Uri path = Uri.parse(url);
		if(LOG_ON) {
			Log.v(TAG,"Video.play: " + path.toString());
		}
		v.setVisibility(View.VISIBLE);
        v.setVideoURI(path);
		v.start();
		v.requestFocus();
		if(progress != null) {
			progress.setVisibility(View.VISIBLE);
		}
	}

	public void play(int resRaw) {
		play(resRaw, null);
	}

	public void play(int resRaw, View progress) {
		this.progress = progress;
		if(progress != null) {
			progress.setVisibility(View.VISIBLE);
		}
		String pacote = context.getPackageName();
		String s = "android.resource://"+pacote+"/" + resRaw;
		Uri path = Uri.parse(s);
		if(LOG_ON) {
			Log.v(TAG,"Video.play: " + path.toString());
		}
		v.setVisibility(View.VISIBLE);
        v.setVideoURI(path);
		v.start();
		v.requestFocus();
		if(progress != null) {
			progress.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onCompletion(MediaPlayer m) {
		if(LOG_ON) {
			Log.v(TAG,"Video.onCompletion: " + running);
		}
		running = false;
		if(progress != null) {
			progress.setVisibility(View.GONE);
		}
		
		if(onCompletionListener != null) {
			onCompletionListener.onCompletion(m);
		}
	}

	@Override
	public void onPrepared(MediaPlayer m) {
		if(LOG_ON) {
			Log.v(TAG,"Video.onPrepared");
		}
		running = true;
		if(onPreparedListener != null) {
			onPreparedListener.onPrepared(m);
		}
		if(progress != null) {
			progress.postDelayed(new Runnable() {
				@Override
				public void run() {
					progress.setVisibility(View.GONE);
				}
			}, 1000);
		}
	}

	public void stop() {
		try {
			if(running && v.isPlaying()) {
				v.stopPlayback();
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(),e);
		}		
	}

	public void setOnCompletionListener(OnCompletionListener onCompletionListener) {
		this.onCompletionListener = onCompletionListener;
	}

	public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
		this.onPreparedListener = onPreparedListener;
		
	}
	public MediaController getMediaController() {
		return mediaController;
	}
	
	public static void show(Context context, String url) {
		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(uri, "video/*");
		context.startActivity(intent);
	}
}
