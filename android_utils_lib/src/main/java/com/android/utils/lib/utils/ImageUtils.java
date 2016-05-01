package com.android.utils.lib.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author ricardo
 *
 */
public class ImageUtils {

	private static final String TAG = "ImageUtils";

	/**
	 * Faz o download da imagem
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static Bitmap doGet(String url) throws IOException {

		URL u = new URL(url);
		InputStream in = u.openStream();
		Bitmap img = getBitmap(in);

		return img;
	}
	
	public static Bitmap getBitmap(InputStream in) {
		Bitmap img = BitmapFactory.decodeStream(in);
		try {
			in.close();
		} catch (IOException e) {
		}
		return img;
	}
	
	public static Bitmap getBitmap(File file) throws IOException {
		Bitmap bitmap = getBitmap(new FileInputStream(file));
		return bitmap;
	}
	
	public static Bitmap getBitmap(Context context, int imgId) {
		return getBitmap(context, imgId, false);
	}

	public static Bitmap getBitmap(Context context, int imgId, boolean inJustDecodeBounds) {
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = inJustDecodeBounds;
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgId);
		return bitmap;
	}

	public static Bitmap getBitmap(Context context, byte[] bytes) {
		if(bytes == null) {
			return null;
		}
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return bitmap;
	}

	/**
	 * Idem codigo chamado por "Bitmap bitmap = Media.getBitmap(context.getContentResolver(), Uri.fromFile(file));"
	 * 
	 * @param cr
	 * @param uri
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static final Bitmap getBitmap(ContentResolver cr, Uri uri) throws FileNotFoundException, IOException {
		 InputStream input = cr.openInputStream(uri);
		 Bitmap bitmap = getBitmap(input);
		 return bitmap;
	}

	public static final Bitmap getBitmap(ContentResolver cr, File file) throws FileNotFoundException, IOException {
		return getBitmap(cr, Uri.fromFile(file));
	}

	public static ImageView getImageView(Context context, byte[] bytes) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		ImageView img = new ImageView(context);
		img.setImageBitmap(bitmap);
		return img;
	}

	public static byte[] toBytes(Context context, Bitmap bitmap) {
		try {
			// Salva o array de bytes
			ByteArrayOutputStream bArray = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bArray);
			bArray.flush();
			bArray.close();
			byte[] byteArray = bArray.toByteArray();
			return byteArray;
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public static byte[] readImage(DataInputStream in) throws IOException{
		int length = in.readInt();
		byte[] bytes = new byte[length];
		in.readFully(bytes);
		return bytes;
	}

	public static void writeImage(byte[] bytes,DataOutputStream out) throws IOException{
		if (bytes != null) {
			out.writeInt(bytes.length);
			out.write(bytes,0,bytes.length);
		}else {
			out.writeInt(0);
		}
	}
	
	public static Bitmap resizeBitmapCreateThumb(Bitmap bitmap) {
		// http://www.netmite.com/android/mydroid/frameworks/base/core/java/android/provider/MediaStore.java
		return resizeBitmap(bitmap, 320F, 240F);
	}
	
	public static Bitmap resizeBitmap(Bitmap bitmap, float newWidth, float newHeight) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		 
		float scaleX = newWidth / bitmap.getWidth();
        float scaleY = newHeight / bitmap.getHeight();
		    
		Matrix matrix = new Matrix();
		matrix.setScale(scaleX, scaleY);

		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		return resizedBitmap;
	}
	
	/**
	 * Resize para a largura da tela mantendo o aspect ratio
	 * 
	 * @param context
	 * @param bmp
	 * @return
	 */
	public static Bitmap resizeFullWitdh(Context context, Bitmap bmp) {
		int width = bmp.getWidth();
		int height = bmp.getHeight();

//		Log.i("img","w1 > " + width);
//		Log.i("img","h1 > " + height);
		
		int newWidth =  AndroidUtils.getWidthPixels(context);
		int newHeight =  height;
		
		if(width == newWidth) {
			return bmp;
		}
		
		int xwPercent = newWidth - width;
		int xhPercent = xwPercent*100/width;
		
//		Log.i("img","w2 > " + newWidth);
//		Log.i("img","h2 > " + newHeight);
		newHeight = xhPercent+height;
//		Log.i("img","h3 > " + newHeight);

		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);

		Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height,
				matrix, true);
		return resizedBitmap;
	}

	/**
	 * http://community.developer.motorola.com/t5/MOTODEV-Blog/Do-Your-Apps-Look-Good-on-the-Big-Screen/ba-p/14714
	 * 
	 * @return
	 */
	public static Bitmap getBitmapSuzanne(Activity context, InputStream in) {
		DisplayMetrics metrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metrics);

		int screenDensity = metrics.densityDpi;

		BitmapFactory.Options ops = new BitmapFactory.Options();
		ops.inJustDecodeBounds = false;
		ops.inSampleSize = 4;
		ops.inScaled = true;
		ops.inTargetDensity = screenDensity;
//		Bitmap bitmap = BitmapFactory.decodeResource(context.get, id)

		Bitmap bitmap = BitmapFactory.decodeStream(in, null, ops);

		return bitmap;
	}

	public static Drawable getDrawable(Context context, int imagem) {
		Drawable d = context.getResources().getDrawable(imagem);
		return d;
	}

	/**
	 * Retorna uma imagem da pasta assets
	 * 
	 * @param context
	 * @param asset
	 * @return
	 */
	public static Bitmap getBitmapAsserts(Context context, String asset) {
		InputStream is = null;
		try {
		    is = context.getResources().getAssets().open(asset);
		} catch (IOException e) {
		}

		Bitmap bitmap = BitmapFactory.decodeStream(is);
		return bitmap;
	}
	
	/**
	 * http://developer.android.com/training/camera/photobasics.html
	 * 
	 * Preencha o ImageView, inflando o Bitmap com o tamanho exato para melhorar performance
	 * 
	 * @param context
	 * @param imageView
	 * @param file
	 * @throws FileNotFoundException 
	 */
	public static void setScaledBitmap(Context context, ImageView imageView, File file) throws FileNotFoundException {
	    // Get the dimensions of the View
	    int targetW = imageView.getWidth();
	    int targetH = imageView.getHeight();

	    Bitmap bitmap = getBitmap(file, targetW, targetH);
	    imageView.setImageBitmap(bitmap);
	}
	
	public static Bitmap getBitmap(File file, int requiredWidth, int requiredHeight) {
		return getBitmap(file, requiredWidth, requiredHeight);
	}

	/**
	 * 1 - original
	 * 2 - metade
	 * 4 - 1/4
	 * 8 - 1/8
	 * 
	 * @param file
	 * @param inSampleSize
	 * @return
	 */
	public static Bitmap getBitmap(File file, int inSampleSize) {
		try {
			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(file), null, o);

			// The new size we want to scale to
			// Decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = inSampleSize;
			Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, o2);
			return bitmap;
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	/**
	 * Preencha o ImageView, inflando o Bitmap com o tamanho exato para melhorar performance
	 * 
	 * @param file
	 * @param requiredWitd
	 * @param requiredHeight
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static Bitmap getBitmap(File file, int requiredWidth, int requiredHeight, boolean log) {
		try {
			// Get the dimensions of the bitmap
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(file), null, o);
			int imgW = o.outWidth;
			int imgH = o.outHeight;

			// Determine how much to scale down the image
			//int scaleFactor = Math.min(imgW / requiredWidth, imgH / requiredHeight);

			// Find the correct scale value. It should be the power of 2.
			int scaleFactor = 1;
			while (o.outWidth / scaleFactor / 2 >= requiredWidth && o.outHeight / scaleFactor / 2 >= requiredHeight) {
				scaleFactor *= 2;
			}
			
			if(log) {
				Log.v(TAG, "ImageUtils.getBitmap() required w/h: " + requiredWidth+"/"+requiredHeight + ", scale: " + scaleFactor + ", real w/h: " + imgW+"/"+imgH);
			}

			// Decode the image file into a Bitmap sized to fill the View
			o.inJustDecodeBounds = false;
			o.inSampleSize = scaleFactor;
			o.inPurgeable = true;

			Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file),null, o);
			return bitmap;
		} catch (FileNotFoundException e) {
			return null;
		}
	}
	
	/**
	 * 1 - original
	 * 2 - metade
	 * 4 - 1/4
	 * 8 - 1/8
	 * 
	 * @param file
	 * @param inSampleSize
	 * @return
	 */
	public static Bitmap getBitmap(InputStream in, int inSampleSize) {
		try {
			// Decode image size
						BitmapFactory.Options o = new BitmapFactory.Options();
						o.inJustDecodeBounds = true;
						BitmapFactory.decodeStream(in, null, o);

						// The new size we want to scale to
						// Decode with inSampleSize
						BitmapFactory.Options o2 = new BitmapFactory.Options();
						o2.inSampleSize = inSampleSize;
						Bitmap bitmap = BitmapFactory.decodeStream(in, null, o2);
						return bitmap;
		} catch (Throwable e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Preencha o ImageView, inflando o Bitmap com o tamanho exato para melhorar performance
	 * 
	 * @param file
	 * @param requiredWitd
	 * @param requiredHeight
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static Bitmap getBitmap(InputStream in, int requiredWidth, int requiredHeight, boolean log) {
		try {
			// Get the dimensions of the bitmap
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, o);
			int imgW = o.outWidth;
			int imgH = o.outHeight;

			// Determine how much to scale down the image
			//int scaleFactor = Math.min(imgW / requiredWidth, imgH / requiredHeight);

			// Find the correct scale value. It should be the power of 2.
			int scaleFactor = 1;
			while (o.outWidth / scaleFactor / 2 >= requiredWidth && o.outHeight / scaleFactor / 2 >= requiredHeight) {
				scaleFactor *= 2;
			}
			
			if(log) {
				Log.v(TAG, "ImageUtils.getBitmap() required w/h: " + requiredWidth+"/"+requiredHeight + ", scale: " + scaleFactor + ", real w/h: " + imgW+"/"+imgH);
			}

			// Decode the image file into a Bitmap sized to fill the View
			o.inJustDecodeBounds = false;
			o.inSampleSize = scaleFactor;
			o.inPurgeable = true;

			Bitmap bitmap = BitmapFactory.decodeStream(in,null, o);
			return bitmap;
		} catch (Throwable e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		}
	}
}