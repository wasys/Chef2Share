package com.android.utils.lib.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author ricardo
 *
 */
public class FileUtils {

	private static final String TAG = "FileUtils";

	public static InputStream readRawFile(Context context, int raw) {
		Resources resources = context.getResources();
		InputStream in = resources.openRawResource(raw);
		return in;
	}
	
//	public static String getFileAssetsString(Context context, String assetName, String charset) throws IOException {
//		AssetManager assets = context.getAssets();
//		String[] files = assets.list("");
//		for (String filename : files) {
//			if(assetName.equals(filename)) {
//				InputStream in = assets.open(filename);
//				String s = IOUtils.toString(in);
//				return s;
//			}
//		}
//
//		return null;
//	}
	
	/**
	 * Abre da pasta /assets, pode inserir subpastas
	 * 
	 * Exemplo: "fotos/foto1.png"
	 * 
	 * @param context
	 * @param assetName
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String getFileAssetsString(Context context, String fileName, String charset) throws IOException {
		AssetManager assets = context.getAssets();
		InputStream in = assets.open(fileName);
		String s = IOUtils.toString(in);
		return s;
	}
	
	public static byte[] getFileAssetsBytes(Context context, String fileName, String charset) throws IOException {
		AssetManager assets = context.getAssets();
		InputStream in = assets.open(fileName);
		byte[] bytes = IOUtils.toByteArray(in);
		return bytes;
	}

	public static String getFileString(Context context, File file, String charset) throws IOException {
		InputStream in = new FileInputStream(file);
		String s = IOUtils.toString(in, charset);
		IOUtils.closeQuietly(in);
		return s;
	}

	public static String getFileString(Context context, int raw, String charset) throws IOException {
		Resources resources = context.getResources();
		InputStream in = resources.openRawResource(raw);
		String s = IOUtils.toString(in, charset);
		IOUtils.closeQuietly(in);
		return s;
	}
	
	public static String getFileString(Context context, int raw) throws IOException {
		Resources resources = context.getResources();
		InputStream in = resources.openRawResource(raw);
		String s = IOUtils.toString(in);
		IOUtils.closeQuietly(in);
		return s;
	}
	
	/**
	 * Retorna um arquivo fixo da pasta /res/raw
	 * 
	 * @param resources
	 * @param raw
	 * @param CHARSET 
	 * @return
	 * @throws IOException
	 */
	public static String getFileString(Resources resources, int raw) throws IOException {
		try {
			InputStream in = resources.openRawResource(raw);
			String s = IOUtils.toString(in);
			IOUtils.closeQuietly(in);
			return s;
		} catch (RuntimeException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return null;
	}


	/**
	 * Retorna um arquivo fixo da pasta /res/raw
	 * 
	 * @param resources
	 * @param raw
	 * @param charset 
	 * @return
	 * @throws IOException
	 */
	public static String getFileString(Resources resources, int raw, String charset) throws IOException {
		try {
			InputStream in = resources.openRawResource(raw);
			String s = IOUtils.toString(in, charset);
			IOUtils.closeQuietly(in);
			return s;
		} catch (RuntimeException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * Le o arquivo, ex salvo no /sdcard
	 * 
	 * @param context
	 * @param f
	 * @return 
	 */
	public static Bitmap getBitmapFromFile(Context context, File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale++;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
			return bitmap;
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Escreve o arquivo na pasta dedicada da app
	 * 
	 * @param context
	 * @param fileName
	 * @param string
	 * @throws IOException
	 */
	public static void writeToAppFile(Context context, String fileName,String string) throws IOException {
		FileOutputStream out = context.openFileOutput(fileName,Context.MODE_PRIVATE);
		PrintWriter writer = new PrintWriter(out);
		writer.print(string);
		writer.flush();
		writer.close();
		out.close();
	}

	public static void writeToAppFile(Context context, String fileName,byte[] bytes) throws IOException {
		FileOutputStream out = context.openFileOutput(fileName,Context.MODE_PRIVATE);
		out.write(bytes);
		out.flush();
		out.close();
	}
	
	/**
	 * Write /sdcard
	 * 
	 * @param context
	 * @param file
	 * @param string
	 * @throws IOException
	 */
	public static void writeStringToFile(Context context, File file,String string) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(string.getBytes());
		fos.close(); 
	}
	
	public static void writeStringToFile(Context context, File file,String string, String charset) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(string.getBytes(charset));
		fos.close(); 
	}

	public static void writeBytesToFile(Context context, File file,byte[] bytes) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(bytes);
		fos.close(); 
	}

	/**
	 * Salvar muitos bytes (evita o OutOfMemory)
	 * @param context
	 * @param file
	 * @param bytes
	 * @throws IOException
	 */
	public static void writeBytesLargeToFile(Context context, File file, byte[] bytes) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));  
		bos.write(bytes);  
		bos.close();  
	}
	
	

	/**
	 * Le o arquivo na pasta dedicada da app
	 * 
	 * @param context
	 * @param nome
	 * @param contents
	 * @throws IOException
	 */
	public static String readAppFileToString(Context context, String nome) throws IOException {
		FileInputStream in = context.openFileInput(nome);
		try {
			String s = IOUtils.toString(in);
			return s;
		}finally {
			IOUtils.closeQuietly(in);
		}
	}

	public static byte[] readAppFileToBytes(Context context, String nome) throws IOException {
		FileInputStream in = context.openFileInput(nome);
		try {
			byte[] bytes = IOUtils.toByteArray(in);
			return bytes;
		} finally {
			IOUtils.closeQuietly(in);
		}
	}

	public static List<String> readLines(Context context, int raw) {
		try {
			Resources resources = context.getResources();
			InputStream in = resources.openRawResource(raw);
			List<String> list = IOUtils.readLines(in);
			return list;
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return null;
	}
	
	public static List<String> readLines(Context context, int raw, String charset) {
		try {
			Resources resources = context.getResources();
			InputStream in = resources.openRawResource(raw);
			List<String> list = IOUtils.readLines(in, charset);
			return list;
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * Retorna a pasta montada no /mnt/sdcard
	 * 
	 * @param context
	 * @param preferedDir
	 * @return
	 */
	public static File getSdCardFolder(Context context, String preferedDir) {
		File cacheDir = null;
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), preferedDir);
		} else {
			cacheDir = context.getCacheDir();
		}

		if (!cacheDir.exists()) {
			cacheDir.mkdirs();
		}
		return cacheDir;
	}

	/**
	 * Retorna um File do /mnt/sdcard/${folderName}/${fileName}
	 * 
	 * @param context
	 * @param folderName
	 * @param fileName
	 * @return
	 */
	public static File getSdCardFile(Context context, String folderName,String fileName) {
		File sdcard = getSdCardFolder(context, folderName);
		File f = new File(sdcard,fileName);
		return f;
	}
	
	public static String readFileToString(Context context, File file, String charset) throws IOException {
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			String s = IOUtils.toString(in, charset);
			return s;
		}finally {
			IOUtils.closeQuietly(in);
		}
	}
	
	public static String readFileToString(Context context, Uri uri) throws IOException {
		File f = new File(uri.toString());
		String s = readFileToString(context, f, CharsetUtil.UTF_8);
		return s;
	}
	
	public static byte[] readFileToBytes(Context context, File file) throws IOException {
		InputStream in = null;
		try {
			in = new FileInputStream(file);
			byte[] s = IOUtils.toByteArray(in);
			return s;
		}finally {
			IOUtils.closeQuietly(in);
		}
	}

	public static byte[] readFileToBytes(Context context, Uri uri) throws IOException {
		File f = new File(uri.toString());
		byte bytes[] = readFileToBytes(context, f);
		return bytes;
	}

	public static final InputStream getInputStream(Context context, Uri uri) throws FileNotFoundException {
		ContentResolver contentResolver = context.getContentResolver();
		InputStream in = contentResolver.openInputStream(uri);
		return in;
	}

	public static final OutputStream getOutputStream(Context context, Uri uri) throws FileNotFoundException {
		ContentResolver contentResolver = context.getContentResolver();
		OutputStream out = contentResolver.openOutputStream(uri);
		return out;
	}
	
	public static void delete(File f) {
		try {
			boolean ok = f.delete();
			Log.v(TAG, "File.delete ["+f.getName()+"]: " + ok);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}
	
	/**
	 * Converte um File para byte[]
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFileToBytes(File file) throws IOException {
		ByteArrayOutputStream ous = null;
		InputStream ios = null;
		try {
			byte[] buffer = new byte[1024];
			ous = new ByteArrayOutputStream();
			ios = new FileInputStream(file);
			int read = 0;
			while ((read = ios.read(buffer)) != -1) {
				ous.write(buffer, 0, read);
			}
		} finally {
			try {
				if (ous != null)
					ous.close();
			} catch (IOException e) {
			}

			try {
				if (ios != null)
					ios.close();
			} catch (IOException e) {
			}
		}
		return ous.toByteArray();
	}

	
}
