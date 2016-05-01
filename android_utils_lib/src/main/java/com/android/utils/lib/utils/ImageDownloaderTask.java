 /*
  * Copyright (C) 2010 The Android Open Source Project
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

package com.android.utils.lib.utils;

 import android.content.Context;
 import android.graphics.Bitmap;
 import android.graphics.BitmapFactory;
 import android.graphics.Color;
 import android.graphics.drawable.ColorDrawable;
 import android.graphics.drawable.Drawable;
 import android.net.http.AndroidHttpClient;
 import android.os.AsyncTask;
 import android.os.Handler;
 import android.util.Log;
 import android.view.View;
 import android.widget.ImageView;
 import android.widget.ProgressBar;

 import org.apache.http.HttpEntity;
 import org.apache.http.HttpResponse;
 import org.apache.http.HttpStatus;
 import org.apache.http.client.HttpClient;
 import org.apache.http.client.methods.HttpGet;

 import java.io.File;
 import java.io.FileInputStream;
 import java.io.FileNotFoundException;
 import java.io.FileOutputStream;
 import java.io.FilterInputStream;
 import java.io.IOException;
 import java.io.InputStream;
 import java.io.OutputStream;
 import java.lang.ref.SoftReference;
 import java.lang.ref.WeakReference;
 import java.net.URI;
 import java.net.URISyntaxException;
 import java.util.HashMap;
 import java.util.LinkedHashMap;
 import java.util.concurrent.ConcurrentHashMap;

/**
 * This helper class download images from the Internet and binds those with the provided ImageView.
 *
 * <p>It requires the INTERNET permission, which should be added to your application's manifest
 * file.</p>
 *
 * A local cache of downloaded images is maintained internally to improve performance.
 */
public class ImageDownloaderTask {
	private static final String TAG = "ImageDownloader";
	private static final String LOG_TAG = TAG;
	public static boolean LOG_ON = false;
	public static boolean TRACE_ON = false;
	private File cacheDir;
	private final Context context;
	private DownloadInfo defaultDownloadInfo; 

	public ImageDownloaderTask(Context context, String cacheDirName) {
		this.context = context;
		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), cacheDirName);
		} else {
			cacheDir = context.getCacheDir();
		}
		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		defaultDownloadInfo = getDefaultDownloadInfo();
	}

	public static class DownloadInfo {
		public boolean cacheSdCardOn 	= true;
		public boolean cacheMemoryOn 	= true;
		public int tempColor 			= Color.BLACK;
		/**
		 * For performance reansos, pass the known image dimensions.
		 * So we can create exactly a imagem with this size.
		 */
		public int requiredImgWidth = 0;
		public int requiredImgHeight = 0;
		/**
		 * 1 - original
		 * 2 - 1/2
		 * 4 - 1/4
		 * 8 - 1/8
		 */
		public int scaleSize;
		/**
		 * Convert 320x480 img to 480x720
		 */
		public boolean dipMode;

		public boolean isCacheOn() {
			return cacheMemoryOn || cacheSdCardOn;
		}
		public boolean hasRequiredSize() {
			return requiredImgWidth > 0 && requiredImgHeight > 0;
		}
		public DownloadCompleteListener downloadCompleteListener;
	}
	
	public static interface DownloadCompleteListener{
		public void downloadFinished(ImageView img, Bitmap bitmap, boolean cachedImg);
	}

	/**
	 * Cache-related fields and methods.
	 * 
	 * We use a hard and a soft cache. A soft reference cache is too aggressively cleared by the
	 * Garbage Collector.
	 */
	private static final int HARD_CACHE_CAPACITY = 10;
	private static final int DELAY_BEFORE_PURGE = 10 * 1000; // in milliseconds

	// Hard cache, with a fixed maximum capacity and a life duration
	private final HashMap<String, Bitmap> sHardBitmapCache = new LinkedHashMap<String, Bitmap>(HARD_CACHE_CAPACITY / 2, 0.75f, true) {
		private static final long serialVersionUID = -899622047235056839L;

		@Override
		protected boolean removeEldestEntry(LinkedHashMap.Entry<String, Bitmap> eldest) {
			if (size() > HARD_CACHE_CAPACITY) {
				// Entries push-out of hard reference cache are transferred to soft reference cache
				sSoftBitmapCache.put(eldest.getKey(), new SoftReference<Bitmap>(eldest.getValue()));
				return true;
			} else
				return false;
		}
	};

	// Soft cache for bitmaps kicked out of hard cache
	private final ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(HARD_CACHE_CAPACITY / 2);

	private final Handler purgeHandler = new Handler();

	private final Runnable purger = new Runnable() {
		public void run() {
			clearCache();
		}
	};

	public DownloadInfo getDefaultDownloadInfo() {
		DownloadInfo d = new DownloadInfo();
		return d;
	}

	public void download(String url) {
		download(url, null, null, this.defaultDownloadInfo);
	}
	
	public void download(String url, DownloadInfo downloadInfo) {
		download(url, null, null, downloadInfo);
	}
	
	public void download(String url, ImageView imageView, ProgressBar progress) {
		download(url, imageView, progress, this.defaultDownloadInfo);
	}

	/**
	 * Download the specified image from the Internet and binds it to the provided ImageView. The
	 * binding is immediate if the image is found in the cache and will be done asynchronously
	 * otherwise. A null bitmap will be associated to the ImageView if an error occurs.
	 *
	 * @param url The URL of the image to download.
	 * @param imageView The ImageView to bind the downloaded image to.
	 */
	public void download(String url, ImageView imageView, ProgressBar progress, DownloadInfo downloadInfo) {
		try {
			resetPurgeTimer();

			Bitmap bitmap = null;

			if(downloadInfo.isCacheOn()) {
				bitmap = getBitmapFromCache(url, downloadInfo);
			}

			if (bitmap == null) {
				forceDownload(url, imageView, progress, downloadInfo);
			} else {
				if(TRACE_ON) {
					Log.v(TAG, "<< download getBitmapFromCache ["+bitmap.getWidth()+"/"+bitmap.getHeight()+"]" + url);
				}

				if(imageView != null) {
					cancelPotentialDownload(url, imageView);
					if(progress != null) {
						progress.setVisibility(View.GONE);
					}

					imageView.setImageBitmap(bitmap);
					
					if(downloadInfo != null && downloadInfo.downloadCompleteListener != null) {
						downloadInfo.downloadCompleteListener.downloadFinished(imageView, bitmap, true);
					}
				}
			}
		} catch (Throwable e) {
			Log.e(getClass().getSimpleName(), "[" + url + "] > " + e.getMessage(), e);
		}
	}

	/**
	 * Same as download but the image is always downloaded and the cache is not used.
	 * Kept private at the moment as its interest is not clear.
	 * @param progress 
	 * @param downloadInfo 
	 */
	private void forceDownload(String url, ImageView imageView, ProgressBar progress, DownloadInfo downloadInfo) {
		// State sanity: url is guaranteed to never be null in DownloadedDrawable and cache keys.
		if (url == null) {
			if(imageView != null) {
				imageView.setImageDrawable(null);
			}
			return;
		}

		if (cancelPotentialDownload(url, imageView)) {
			BitmapDownloaderTask task = new BitmapDownloaderTask(imageView, progress, downloadInfo);
			if(imageView != null) {
				DownloadedDrawable downloadedDrawable = new DownloadedDrawable(task, downloadInfo);
				imageView.setImageDrawable(downloadedDrawable);
			}
			task.execute(url);
		}
	}

	/**
	 * Returns true if the current download has been canceled or if there was no download in
	 * progress on this image view.
	 * Returns false if the download in progress deals with the same url. The download is not
	 * stopped in that case.
	 */
	private boolean cancelPotentialDownload(String url, ImageView imageView) {
		BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);

		if (bitmapDownloaderTask != null) {
			String bitmapUrl = bitmapDownloaderTask.url;
			if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
				bitmapDownloaderTask.cancel(true);
			} else {
				// The same URL is already being downloaded.
				return false;
			}
		}
		return true;
	}

	/**
	 * @param imageView Any imageView
	 * @return Retrieve the currently active download task (if any) associated with this imageView.
	 * null if there is no such task.
	 */
	private BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof DownloadedDrawable) {
				DownloadedDrawable downloadedDrawable = (DownloadedDrawable) drawable;
				return downloadedDrawable.getBitmapDownloaderTask();
			}
		}
		return null;
	}

	private Bitmap downloadBitmap(String url, DownloadInfo downloadInfo) {
		if (url.startsWith("file:")) {
			try {
				File file = new File(new URI(url));
				byte[] bytes = FileUtils.readFileToBytes(context, file);
				return ImageUtils.getBitmap(context, bytes);
			} catch (URISyntaxException e) {
				Log.w(LOG_TAG, "Incorrect URL: " + url, e);
				return null;
			} catch (IOException e) {
				Log.w(LOG_TAG, "Unable to read file: " + url, e);
				return null;
			}
		}

		// AndroidHttpClient is not allowed to be used from the main thread
		final HttpClient client = AndroidHttpClient.newInstance("Android");
		final HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w(TAG, "Error " + statusCode + " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();

					Bitmap bitmap = null;
					if(downloadInfo.cacheSdCardOn) {
						bitmap = saveBitmapToFile(inputStream, url, downloadInfo);
					} else {
						bitmap = getBitmap(inputStream, downloadInfo);
					}

					return bitmap;
					// return BitmapFactory.decodeStream(inputStream);
					// Bug on slow connections, fixed in future release.
//					return BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (IOException e) {
			getRequest.abort();
			Log.w(LOG_TAG, "I/O error while retrieving bitmap from " + url, e);
		} catch (IllegalStateException e) {
			getRequest.abort();
			Log.w(LOG_TAG, "Incorrect URL: " + url);
		} catch (Exception e) {
			getRequest.abort();
			Log.w(LOG_TAG, "Error while retrieving bitmap from " + url, e);
		} finally {
			if ((client instanceof AndroidHttpClient)) {
				((AndroidHttpClient) client).close();
			}
		}
		return null;
	}

	private Bitmap getBitmap(InputStream in, DownloadInfo downloadInfo) {
//		if(downloadInfo.scaleSize > 0) {
//			Bitmap bitmap = ImageUtils.getBitmap(in, downloadInfo.scaleSize);
//			return bitmap;
//		}
//		else if(downloadInfo.hasRequiredSize()) {
//			Bitmap bitmap = ImageUtils.getBitmap(in, downloadInfo.requiredImgWidth, downloadInfo.requiredImgHeight, true);
//			return bitmap;
//			
//		} else {
			Bitmap bitmap = ImageUtils.getBitmap(in);
			return bitmap;
//		}
	}

	private Bitmap saveBitmapToFile(InputStream inputStream, String url, DownloadInfo downloadInfo) {
		// I identify images by hashcode. Not a perfect solution, good for the demo.
		String filename = String.valueOf(url.hashCode());
		File f = new File(cacheDir, filename);

		// from web
		try {
//			InputStream is = new URL(url).openStream();
			
			try {
				
				OutputStream os = new FileOutputStream(f);
				CopyStream(inputStream, os);
				os.close();

				Bitmap bitmap = downloadFile(f, downloadInfo);
				return bitmap;
				
			} catch (FileNotFoundException e) {
				Bitmap bitmap = getBitmap(inputStream, downloadInfo);
				return bitmap;
			}
		} catch (Exception ex) {
			Log.e(LOG_TAG,ex.getMessage(), ex);
		}
		return null;
	}

	/*
	 * An InputStream that skips the exact number of bytes provided, unless it reaches EOF.
	 */
	static class FlushedInputStream extends FilterInputStream {
		public FlushedInputStream(InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip(long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int b = read();
					if (b < 0) {
						break; // we reached EOF
					} else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

	/**
	 * The actual AsyncTask that will asynchronously download the image.
	 */
	class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
		private String url;
		private WeakReference<ImageView> imageViewReference;
		private final ProgressBar progress;
		private DownloadInfo downloadInfo;

		public BitmapDownloaderTask(ImageView imageView, ProgressBar progress, DownloadInfo downloadInfo) {
			this.progress = progress;
			this.downloadInfo = downloadInfo;
			if(imageView != null) {
				imageViewReference = new WeakReference<ImageView>(imageView);
			}
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(progress != null) {
				progress.setVisibility(View.VISIBLE);
			}
		}
		/**
		 * Actual download method.
		 */
		@Override
		protected Bitmap doInBackground(String... params) {
			url = params[0];
			Bitmap bitmap = downloadBitmap(url, downloadInfo);
			return bitmap;
		}
		/**
		 * Once the image is downloaded, associates it to the imageView
		 */
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if(progress != null) {
				progress.setVisibility(View.GONE);
			}

			if (isCancelled()) {
				bitmap = null;
			}

			if(downloadInfo.cacheMemoryOn) {
				addBitmapToCache(url, bitmap, downloadInfo);
			}

			if (imageViewReference != null) {
				ImageView imageView = imageViewReference.get();

				if(imageView != null) {
					BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
					// Change bitmap only if this process is still associated with it
					// Or if we don't use any bitmap to task association (NO_DOWNLOADED_DRAWABLE mode)
					if (this == bitmapDownloaderTask) {

						if(LOG_ON) {
							Log.v(TAG, "downloadPost onPostExecute: " + url + " - " + bitmap + ", cache: " + downloadInfo.cacheMemoryOn);
						}

						imageView.setImageBitmap(bitmap);

						if(downloadInfo != null && downloadInfo.downloadCompleteListener != null) {
							downloadInfo.downloadCompleteListener.downloadFinished(imageView, bitmap, true);
						}
					}
				}
			}
			
			if(LOG_ON) {
				Log.v(TAG, "Download ok: " + url);
			}
		}
	}

	/**
	 * A fake Drawable that will be attached to the imageView while the download is in progress.
	 *
	 * <p>Contains a reference to the actual download task, so that a download task can be stopped
	 * if a new binding is required, and makes sure that only the last started download process can
	 * bind its result, independently of the download finish order.</p>
	 */
	static class DownloadedDrawable extends ColorDrawable {
		private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

		public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask, DownloadInfo downloadInfo) {
			super(downloadInfo.tempColor);
			bitmapDownloaderTaskReference = new WeakReference<BitmapDownloaderTask>(bitmapDownloaderTask);
		}

		public BitmapDownloaderTask getBitmapDownloaderTask() {
			return bitmapDownloaderTaskReference.get();
		}
	}

	/**
	 * Adds this bitmap to the cache.
	 * @param bitmap The newly downloaded bitmap.
	 */
	private void addBitmapToCache(String url, Bitmap bitmap, DownloadInfo downloadInfo) {
		if(!downloadInfo.cacheMemoryOn) {
			return;
		}
		if (bitmap != null) {
			synchronized (sHardBitmapCache) {
				sHardBitmapCache.put(url, bitmap);
			}
		}
	}

	public Bitmap getBitmapFromCache(String url) {
		return getBitmapFromCache(url, getDefaultDownloadInfo());
	}

	/**
	 * @param url The URL of the image that will be retrieved from the cache.
	 * @param downloadInfo 
	 * @return The cached bitmap or null if it was not found.
	 */
	public Bitmap getBitmapFromCache(String url, DownloadInfo downloadInfo) {
		if(!downloadInfo.isCacheOn()) {
			return null;
		}
		
		if(downloadInfo.cacheMemoryOn) {
			// First try the hard reference cache
			synchronized (sHardBitmapCache) {
				final Bitmap bitmap = sHardBitmapCache.get(url);
				if (bitmap != null) {
					// Bitmap found in hard cache
					// Move element to first position, so that it is removed last
					sHardBitmapCache.remove(url);
					sHardBitmapCache.put(url, bitmap);
					
					if(TRACE_ON) {
						Log.v(TAG, "getBitmapFromCache [hard cache] >> " + url);
					}
					
					return bitmap;
				}
			}

			// Then try the soft reference cache
			SoftReference<Bitmap> bitmapReference = sSoftBitmapCache.get(url);
			if (bitmapReference != null) {
				final Bitmap bitmap = bitmapReference.get();
				if (bitmap != null) {
					if(TRACE_ON) {
						Log.v(TAG, "getBitmapFromCache [soft cache] >> " + url);
					}
					
					// Bitmap found in soft cache
					return bitmap;
				} else {
					// Soft reference has been Garbage Collected
					sSoftBitmapCache.remove(url);
				}
			}
		}

		Bitmap bitmap = null;

		if(downloadInfo.cacheSdCardOn) {
			bitmap = getBitmapFromFile(url, downloadInfo);
			if(TRACE_ON) {
				Log.v(TAG, "getBitmapFromCache [getBitmapFromFile] >> " + bitmap);
			}
		}

		if(bitmap != null) {
			addBitmapToCache(url, bitmap, downloadInfo);
		}

		return bitmap;
	}

	private Bitmap getBitmapFromFile(String url, DownloadInfo downloadInfo) {
		// I identify images by hashcode. Not a perfect solution, good for the demo.
		String filename = String.valueOf(url.hashCode());
		File f = new File(cacheDir, filename);

		// from SD cache
		Bitmap b = downloadFile(f, downloadInfo);

		return b;
	}

	private void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap downloadFile(File f, DownloadInfo downloadInfo) {
		try {
			if(downloadInfo.scaleSize > 0) {
				Bitmap bitmap = ImageUtils.getBitmap(f, downloadInfo.scaleSize);
				return bitmap;
			}
			else if(downloadInfo.hasRequiredSize()) {
				Bitmap bitmap = ImageUtils.getBitmap(f, downloadInfo.requiredImgWidth, downloadInfo.requiredImgHeight, true);
				return bitmap;				
			}
			else {
				// don�t resize
				// Find the correct scale value. It should be the power of 2.
				BitmapFactory.Options o2 = new BitmapFactory.Options();
				Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
				return bitmap;
			}

		} catch (FileNotFoundException e) {
			//Log.e(TAG, e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Clears the image cache used internally to improve performance. Note that for memory
	 * efficiency reasons, the cache will automatically be cleared after a certain inactivity delay.
	 */
	public void clearCache() {
		sHardBitmapCache.clear();
		sSoftBitmapCache.clear();
	}

	/**
	 * Allow a new delay before the automatic cache clear is done.
	 */
	private void resetPurgeTimer() {
		purgeHandler.removeCallbacks(purger);
		purgeHandler.postDelayed(purger, DELAY_BEFORE_PURGE);
	}
	
	public void setCache(boolean cacheOn) {
		setCacheMemoryOn(cacheOn);
		setCacheSdCardOn(cacheOn);
	}

	public void setCacheMemoryOn(boolean cacheMemoryOn) {
		this.defaultDownloadInfo.cacheMemoryOn = cacheMemoryOn;
	}

	public void setCacheSdCardOn(boolean cacheSdCardOn) {
		this.defaultDownloadInfo.cacheSdCardOn = cacheSdCardOn;
	}

	public void setTempColor(int tempColor) {
		this.defaultDownloadInfo.tempColor = tempColor;
	}
}