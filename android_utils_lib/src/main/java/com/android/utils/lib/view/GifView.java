/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.utils.lib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

/**
 * View que exibe um GIF animado
 * 
 * @author ricardo
 * 
 */
public class GifView extends View {

	private static final String TAG = "livro";
	private Movie mMovie;
	private long mMovieStart;

	public GifView(Context context, int resId) {
		super(context);
		setFocusable(true);

		InputStream is = context.getResources().openRawResource(resId);
		// if (true) {
		mMovie = Movie.decodeStream(is);
		// } else {
		// byte[] array = streamToBytes(is);
		// mMovie = Movie.decodeByteArray(array, 0, array.length);
		// }
		
		try {
			is.close();
		} catch (IOException e) {
			Log.e(TAG, "Erro close io gif",e);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {

		Paint p = new Paint();
		p.setAntiAlias(true);

		long now = android.os.SystemClock.uptimeMillis();
		if (mMovieStart == 0) { // first time
			mMovieStart = now;
		}
		if (mMovie != null) {
			int dur = mMovie.duration();
			if (dur == 0) {
				dur = 1000;
			}
			int relTime = (int) ((now - mMovieStart) % dur);
			mMovie.setTime(relTime);
			mMovie.draw(canvas, getWidth() - mMovie.width(), getHeight() - mMovie.height());
			invalidate();
		}
	}
}
