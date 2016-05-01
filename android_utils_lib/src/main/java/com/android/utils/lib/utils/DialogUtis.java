package com.android.utils.lib.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.widget.Toast;

/**
 * Classe utilit�ria para exibir alertas
 * 
 * @author ricardo
 *
 */
public class DialogUtis {

	private static final String TAG = "DialogUtils";

	public static void dialog(Context contexto,String msg){
		new AlertDialog.Builder(contexto).setMessage(msg).show();
	}

	public static void toast(Context contexto,String msg){
		Toast.makeText(contexto, msg, Toast.LENGTH_SHORT).show();
	}

	public static void alertThread(final Activity activity,final int message, final int ok) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				alertDialog(activity, message, ok);		
			}
		});
	}

	public static void alertDialog(Activity activity,int message, int ok) {
		AlertDialog dialog = new AlertDialog.Builder(activity)
		.setTitle("Title")
		.setMessage(message)
        .create();	
		dialog.setButton(activity.getString(ok), new DialogInterface.OnClickListener() {
		      public void onClick(DialogInterface dialog, int which) {
		        return;
		      } });
		dialog.show();
	}

	public static void alertListDialog(final Context context,int title, int resArray, OnClickListener listener) {
		Builder builder = new AlertDialog.Builder(context).setTitle(title);
		builder.setItems(resArray,listener);
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public static void alertListDialog(final Context context,int title, String[] items, OnClickListener listener) {
		Builder builder = new AlertDialog.Builder(context).setTitle(title);
		builder.setItems(items,listener);
		AlertDialog dialog = builder.create();
		dialog.show();
	}

	public static void alertDialogConfirm(final Activity context,final int icon,final  String title,final String message, final String confirmButtonMessage, final Runnable runnableSuccess,final String cancelButtonMessage, final Runnable runnableCancel) {
		context.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {

					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setMessage(message)
					       .setCancelable(false)
					       .setIcon(icon)
					       .setTitle(title)
					       .setPositiveButton(confirmButtonMessage, new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					        	   if(runnableSuccess != null) {
					        		   runnableSuccess.run();
					        	   }
					           }
					       })
					       .setNegativeButton(cancelButtonMessage, new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					                dialog.dismiss();
					                if(runnableCancel != null) {
					                	runnableCancel.run();
						        	   }
					           }
					       });
					AlertDialog alert = builder.create();
					alert.show();
				} catch (Exception e) {
					Log.e(TAG,e.getMessage(),e);
				}
			}
		});
	}
}