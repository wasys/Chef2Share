package com.android.utils.lib.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author Jonas Baggio
 * @create 12/01/2012
 */
public class AssetsUtils {

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
	public static Bitmap getBitmap(Context context, String path) {
		try {
			AssetManager assets = context.getAssets();
			InputStream in = assets.open(path);
			Bitmap bitmap = BitmapFactory.decodeStream(in);

			if (bitmap != null) {
				int width = bitmap.getWidth();
				int height = bitmap.getHeight();

				Log.i("AssetsUtils",">>> Original " + path + " w/h: " + width + "x" + height + "px");
				bitmap = ImageUtils.resizeBitmap(bitmap, width += width / 2, height += height / 2);
				Log.i("AssetsUtils", ">>> New " + path + " w/h: " + width + "x" + height + "px");
			}

			return bitmap;
		} catch (IOException ioe) {
			Log.i("AssetsUtils", "Erro ao buscar bytes [ " + path + " ] detalhes: " + ioe.getMessage());
			return null;
		}
	}
}
