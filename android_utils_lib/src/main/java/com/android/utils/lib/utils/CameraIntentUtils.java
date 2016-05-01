package com.android.utils.lib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Classe utilitaria para chamar a camera e ler a foto de retorno
 * 
 * http://developer.android.com/training/camera/photobasics.html
 * 
 * http://www.netmite.com/android/mydroid/frameworks/base/core/java/android/provider/MediaStore.java
 * 
 * http://www.tutorialforandroid.com/2010/10/take-picture-in-android-with.html?utm_source=feedburner&utm_medium=feed&utm_campaign=Feed%3A+TutorialForAndroid+%28Tutorial+For+Android%29
 * 
 * ------------------------------------------------------------------------------
 * File file = CameraUtils.show(this,"bonsucesso",documento.nome+".png");
 * Bitmap captureBmp = CameraUtils.getFotoBitmap(this, file);
 * ------------------------------------------------------------------------------
 * 
 * @author ricardo
 *
 */
public class CameraIntentUtils {

	private static final String TAG = "camera";

	/**
	 * Abre a c�mera para tirar uma foto, Intent "android.media.action.IMAGE_CAPTURE"
	 * 
	 * @param atividade
	 */
	public static void show(Activity activity){
		// "android.media.action.IMAGE_CAPTURE"
		String uriCapture = MediaStore.ACTION_IMAGE_CAPTURE;
		Intent i = new Intent(uriCapture);
		activity.startActivityForResult(i, 0);
	}
	
	/**
	 * Abre a c�mera para tirar uma foto, Intent "android.media.action.IMAGE_CAPTURE".
	 * 
	 * Salva o arquivo na pasta indicada
	 * 
	 * @param atividade
	 */
	public static File show(Activity activity, String dirName, String fileName){
		// "android.media.action.IMAGE_CAPTURE"
		String uriCapture = MediaStore.ACTION_IMAGE_CAPTURE;
		Intent i = new Intent(uriCapture);
		File dir = new File(android.os.Environment.getExternalStorageDirectory(), dirName);
		if(!dir.exists()) {
			dir.mkdir();
		}
		File file = new File(dir,fileName);
		Uri uri = Uri.fromFile(file);
		i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		activity.startActivityForResult(i, 0);
		return file;
	}

	/**
	 * Retorna o Bitmap salvo no sdcard neste File
	 * 
	 * O File � resultado do metodo show(activity, dirName, fileName)
	 * 
	 * @param context
	 * @param file
	 * @return
	 */
	public static Bitmap getFotoBitmap(Context context, File file) {
		Bitmap bitmap;
		try {
			bitmap = Media.getBitmap(context.getContentResolver(), Uri.fromFile(file) );
			return bitmap;
		} catch (FileNotFoundException e) {
			Log.e(TAG,e.getMessage(), e);
		} catch (IOException e) {
			Log.e(TAG,e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Retorna o Bitmap salvo no sdcard neste File
	 * 
	 * O File � resultado do metodo show(activity, dirName, fileName)
	 * 
	 * @param context
	 * @param file
	 * @param targetW 
	 * @param targetH 
	 * @return
	 */
	public static Bitmap getFotoBitmap(Context context, File file, int targetW, int targetH) {
		Bitmap bitmap = ImageUtils.getBitmap(file, targetW, targetH);
		return bitmap;
	}

	public static byte[] getFotoBytes(Context context, File file) {
		Bitmap bitmap = CameraIntentUtils.getFotoBitmap(context, file);
		byte[] bytes = ImageUtils.toBytes(context, bitmap);
		return bytes;
	}

	/**
	 * Escala o Bitmap ao ler o File, para salvar os bytes do tamanho desejado
	 * 
	 * Utilizado para reduzir trafego ao enviar para o servidor
	 * 
	 * @param context
	 * @param file
	 * @param targetW
	 * @param targetH
	 * @return
	 */
	public static byte[] getFotoBytes(Context context, File file, int targetW, int targetH) {
		Bitmap bitmap = CameraIntentUtils.getFotoBitmap(context, file, targetW, targetH);
		byte[] bytes = ImageUtils.toBytes(context, bitmap);
		return bytes;
	}

	/**
	 * Retorna o thumb da Intent. Utilizado quando a img n�o � salva no sdcard
	 * 
	 * @param intent
	 * @return
	 */
	public static Bitmap getFotoThumbBitmap(Intent intent){
		if (intent != null) {
  	      Bundle bundle = intent.getExtras();
  	      if(bundle != null) {
			  // Recupera o Bitmap retornado pela camera
			  Bitmap bitmap = (Bitmap) bundle.get("data");
			  return bitmap;
  	      }
		}
		return null;
	}

	public static Uri getFotoUri(Intent intent){
		if (intent != null) {
  	      Bundle bundle = intent.getExtras();
  	      if(bundle != null) {
  	    	  Uri uri = intent.getData();
      	      return uri;
  	      }
		}
		return null;
	}
}
