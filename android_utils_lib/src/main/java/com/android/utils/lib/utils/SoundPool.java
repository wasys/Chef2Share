package com.android.utils.lib.utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

/**
 * @author leonardo.otto
 * 
 */
public class SoundPool {
	private static final String TAG = SoundPool.class.getSimpleName();
	private MediaPlayer mp;
	private boolean initialized;

	public SoundPool(Context ctx, int resouce) {
		try {
			mp = MediaPlayer.create(ctx, resouce);
			setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
				}
			});
			initialized = true;
			
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}

	public void setOnCompletionListener(OnCompletionListener listener) {
		mp.setOnCompletionListener(listener);
	}

	public MediaPlayer getMediaPlayer() {
		return mp;
	}

	public boolean play() {
		if (initialized) {
			try {
				if (mp.isPlaying()) {
					mp.seekTo(0);
				}
			} catch (IllegalStateException e) {
				Log.e(TAG, e.getMessage());
			}
			mp.start();
			return true;
		}
		return false;
	}
	
	public void release() {
		try {
			mp.release();
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}
}
