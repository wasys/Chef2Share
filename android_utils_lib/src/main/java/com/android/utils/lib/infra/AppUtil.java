package com.android.utils.lib.infra;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Todas as aplicacoes vao utilizar isso
 * @author Jonas Baggio
 * @create 21/02/2013 09:56:22
 */
public class AppUtil {

	/**
	 * TAG
	 */
	public static String TAG = "AppUtil";
	
	/**
	 * Controla a visualização dos logs referente ao negócio da aplicação
	 */
	public static boolean LOG_APP = true;
	
	/**
	 * Controla a visualização dos logs referente a infra da aplicação
	 */
	public static boolean LOG_INFRA_DESENVOLVIMENTO = true;
	
	/**
	 * Modo Fake
	 */
	public static boolean FAKE = true;

//-----------------------------------------------------------------------------------------
	
	public static void toast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public static void logError(String message) {
		Log.e(TAG, " [ " + message + " ] ");
	}

	public static void logError(Class<? extends Activity> cls, String message) {
		Log.e(TAG, " [ " + cls.getName() + " / " + message + " ] ");
	}

	public static void logError(String message, Throwable e) {
		Log.e(TAG, " [ " +message + " ] ", e);
	}
	
	public static void logError(Context context, String message, Throwable e) {
		Log.e(TAG, " [ " +message + " ] ", e);
	}
    public static void alertDialogSemIcon(Activity context, int message) {
        String s = context.getString(message);
        String titulo = "Informação";
        alertDialogSemIcon(context, titulo, s, null);
    }
    public static void alertDialogSemIcon(Activity context, String message) {
        String titulo = "Informação";
        alertDialogSemIcon(context, titulo, message, null);
    }
    public static void alertDialogSemIcon(Activity context, int message, Runnable runnable ) {
        String titulo = "Informação";
        alertDialogSemIcon(context, titulo,  context.getString(message), runnable);
    }
    public static void alertDialogSemIcon(Activity context, String message, Runnable runnable ) {
        String titulo = "Informação";
        alertDialogSemIcon(context, titulo,message, runnable);
    }
	
	public static void alertDialog(Activity context, int message) {
		String s = context.getString(message);
		int icon = android.R.drawable.ic_dialog_info;
		String titulo = "Informação";
		alertDialog(context, icon, titulo, s);
	}
	
	public static void alertDialog(Activity context, String message) {
		int icon = android.R.drawable.ic_dialog_info;
		String titulo = "Informação";
		alertDialog(context, icon, titulo, message);
	}
	
	public static void alertDialog(Activity context, String titulo, String message) {
		int icon = android.R.drawable.ic_dialog_info;
		alertDialog(context, icon, titulo, message);
	}
	
	public static void alertDialog(final Activity context, final int icon, final String titulo, final String message) {
		context.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					AlertDialog dialog = new AlertDialog.Builder(context)
					.setIcon(icon)
					.setTitle(titulo)
					.setMessage(message)
					.create();
					dialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
					        return;
					      } });
					dialog.show();
				} catch (Exception e) {
					logError(context,e.getMessage(),e);
				}
			}
		});
	}
    public static void alertDialogSemIcon(final Activity context, final String titulo, final String message, final Runnable runnable) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle(titulo)
                            .setMessage(message)
                            .create();
                    dialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(runnable != null) {
                                runnable.run();
                            }
                            return;
                        } });
                    dialog.show();
                } catch (Exception e) {
                    logError(context,e.getMessage(),e);
                }
            }
        });
    }
	public static void alertDialog(final Activity context,final String message, final Runnable runnable) {
		context.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					AlertDialog dialog = new AlertDialog.Builder(context)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle("")
					.setMessage(message)
					.setCancelable(false)
					.create();	
					dialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
					      public void onClick(DialogInterface dialog, int which) {
					    	  if(runnable != null) {
					    		  runnable.run();
					    	  }
					        return;
					      } });
					dialog.show();
				} catch (Exception e) {
					logError(context, e.getMessage(),e);
				}
			}
		});
	}
    public static void alertDialogConfirmacao(final Activity activity, final String msg, final Runnable runnableSim, final Runnable runnableNao) {
        alertDialogConfirmacao(activity,msg,runnableSim,runnableNao,true);
    }
	
	public static void alertDialogConfirmacao(final Activity activity, final String msg, final Runnable runnableSim, final Runnable runnableNao,boolean exibirIcone){
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(msg);
		builder.setTitle("Atenção");
        if(exibirIcone) {
            builder.setIcon(android.R.drawable.ic_dialog_alert);
        }
		builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   if(runnableSim != null){
		        		   runnableSim.run();
		        	   }
		           }
		       });
		builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   if(runnableNao != null){
		        		   runnableNao.run();
		        	   }
		           }
		       });
		AlertDialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.show();
	}

    public static void alertDialogConfirmacao(final Activity activity, final String msg, final int alertOpcaoSim, final Runnable runnableSim, final int alertOpcaoNao, final Runnable runnableNao) {
        alertDialogConfirmacao(activity,msg,alertOpcaoSim,runnableSim,alertOpcaoNao,runnableNao,true);
    }

	public static void alertDialogConfirmacao(final Activity activity, final String msg, final int alertOpcaoSim, final Runnable runnableSim, final int alertOpcaoNao, final Runnable runnableNao,boolean exibirIcone) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setMessage(msg);
		builder.setTitle("Atenção");
        if(exibirIcone) {
            builder.setIcon(android.R.drawable.ic_dialog_alert);
        }
		builder.setPositiveButton(alertOpcaoSim, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   if(runnableSim != null){
		        		   runnableSim.run();
		        	   }
		           }
		       });
		builder.setNegativeButton(alertOpcaoNao, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   if(runnableNao != null){
		        		   runnableNao.run();
		        	   }
		           }
		       });
		AlertDialog dialog = builder.create();
		dialog.setCancelable(false);
		dialog.show();
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void log(Activity activity, String message) {
		if(LOG_APP) {
			Log.d(TAG, " [ " + activity.getClass().getName() + " / " + message + " ] ");
		}
	}
	
	public static void log(String message) {
		if(LOG_APP) {
			Log.d(TAG, " [ " + message + " ] ");
		}
	}
	public static void log(String tag, String string) {
		if(LOG_APP) {
			Log.d(tag, " [ " +string + " ] ");
		}
	}

	@SuppressWarnings("rawtypes") 
	public static void logInfra(Class clazz, String message) {
		if(LOG_INFRA_DESENVOLVIMENTO) {
			Log.d(TAG, " [ " + clazz.getName() + ":" + message + " ] ");
		}
	}

	public static void showTop(Context context, Class<? extends Activity> cls) {
		Intent intent = new Intent(context, cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP/* | Intent.FLAG_ACTIVITY_NEW_TASK*/);
		context.startActivity(intent);
	}
	
	public static void showTop(Context context, Class<? extends Activity> cls, Bundle param) {
		Intent intent = new Intent(context, cls);
		intent.putExtras(param);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	public static void show(Context context, Class<? extends Activity> cls) {
		Intent intent = new Intent(context, cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	public static void show(Context context, Class<? extends Activity> cls, Bundle params) {
		Intent intent = new Intent(context, cls);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		if(params != null){
			log("startActivity com os parametros: " + params.toString());
			intent.putExtras(params);
		}
		context.startActivity(intent);
//		context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	public static void showForResult(Activity context, Class<? extends Activity> cls, Bundle params, int requestCode ) {
		Intent intent = new Intent(context, cls);
		if(params != null){
			intent.putExtras(params);
		}
		context.startActivityForResult(intent, requestCode);
	}
}
