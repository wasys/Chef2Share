package com.android.utils.lib.utils;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;

/**
 * <uses-permission android:name="android.permission.FLASHLIGHT" />
 * 
 * @author Ricardo Lecheta
 * 
 */
public class FlashHelper {
	private static final String TAG = "FlashHelper";
	private Camera c = null;

	public void on() {
		try {
			try {
				c = Camera.open();
				Parameters params = c.getParameters();
				params.setFlashMode(Parameters.FLASH_MODE_TORCH);
				c.setParameters(params);
				c.startPreview();
			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}

	public void off() {
		if (c != null) {
			try {
				Parameters params = c.getParameters();
				params.setFlashMode(Parameters.FLASH_MODE_OFF);
				c.setParameters(params);
				c.stopPreview();
				c.release();
			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
			}
		}
	}
}
