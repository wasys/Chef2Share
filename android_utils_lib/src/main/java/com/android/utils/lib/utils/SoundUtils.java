package com.android.utils.lib.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

/**
 * @author leonardo.otto
 * 
 */
public class SoundUtils {
	public static MediaPlayer playUntilComplete(Context ctx, int resouce) {
		MediaPlayer mediaPlayer = MediaPlayer.create(ctx, resouce);
		mediaPlayer.start();
		mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}
		});
		return mediaPlayer;
	}

	public static MediaPlayer play(Context ctx, Uri toStream) throws IOException {
		final MediaPlayer mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.setDataSource(ctx, toStream);
		mediaPlayer.prepare();
		mediaPlayer.start();
		mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mediaPlayer.release();
			}
		});
		return mediaPlayer;
	}
}
