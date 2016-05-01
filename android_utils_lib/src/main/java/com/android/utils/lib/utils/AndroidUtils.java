
package com.android.utils.lib.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.Process;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * M�todos utilit�rios para Android
 * 
 * @author ricardo
 * 
 */
@SuppressLint("NewApi")
public class AndroidUtils {

	private static final String TAG = "AndroidUtils";

	/**
	 * Retorna o IMEI para identificar o celular
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = mTelephonyMgr.getDeviceId();
		return imei;
	}
	
	/**
	 * Retorna o IMEI para identificar o celular
	 * 
	 * @param context
	 * @return
	 */
	public static String getIMEI_UUID(Context context) {
		String s = UUID.randomUUID().toString();
		return s;
	}

	/**
	 * android:versionCode
	 * 
	 * @param context
	 * @return
	 * @throws NameNotFoundException
	 */
	public static int getAppVersionCode(Context context) {
		try {
			PackageManager pack = context.getPackageManager();
			String pacote = context.getPackageName();
			PackageInfo packageInfo = pack.getPackageInfo(pacote, 0);
			int versao = packageInfo.versionCode;
			return versao;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage(), e);
			return 0;
		}
	}

	/**
	 * android:versionName
	 * 
	 * @param context
	 * @return
	 * @throws NameNotFoundException
	 */
	public static String getAppVersionName(Context context) {
		try {
			PackageManager pack = context.getPackageManager();
			String pacote = context.getPackageName();
			PackageInfo packageInfo = pack.getPackageInfo(pacote, 0);
			String versao = packageInfo.versionName;
			return versao;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage(), e);
			return "";
		}
	}
	
	public static String getPackageName(Context context) {
		String packageName = context.getPackageName();
		return packageName;
	}
	
	/**
	 * Check the current running tasks.
	 * 
	 * If our app is not in the top, we know that the user pressed the Home button
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMyApplicationTaskOnTop(Context context) {
		return ActivityStackUtils.isMyApplicationTaskOnTop(context);
	}

	/**
	 * Verifica se existe conex�o dispon�vel
	 * 
	 * android.permission.ACCESS_NETWORK_STATE
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
//		Context context = getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
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

	/**
	 * Retorna a API Level
	 * 
	 * 2 - Android 1.1
	 * 3 - Android 1.5
	 * 4 - Android 1.6
	 * 5 - Android 2.0
	 * 6 - Android 2.0.1
	 * 7 - Android 2.1
	 * 
	 * @return
	 */
	public static int getAPILevel(){
		int apiLevel = Integer.parseInt(Build.VERSION.SDK);
		return apiLevel;
	}

	/**
	 * Retorna se � Android 2.0 ou superior (API Level 5)
	 * 
	 * @return
	 */
	public static boolean isAndroid_2_x(){
		int apiLevel = getAPILevel();
		if(apiLevel >= 5){
			return true;
		}
		return false;
	}

	/**
	 * Retorna se � Android 3.0 ou superior (API Level 11)
	 * 
	 * @return
	 */
	public static boolean isAndroid_3_x() {
		int apiLevel = Build.VERSION.SDK_INT;
		if (apiLevel >= Build.VERSION_CODES.HONEYCOMB) {
			return true;
		}
		return false;
	}
	
	/**
	 * Retorna se � Android 4.0 ou superior (API Level 14)
	 * 
	 * @return
	 */
	public static boolean isAndroid_4_x() {
		int apiLevel = Build.VERSION.SDK_INT;
		if (apiLevel >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return true;
		}
		return false;
	}
	
	/**
	 * Retorna se � tablet 10" com Android 4.0 ou superior (API Level 14)
	 * @param context 
	 * 
	 * @return
	 */
	public static boolean isAndroid4Tablet10(Context context) {
        return isAndroid_4_x() && isTablet10(context);
    }
	
	public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * = large
     * 
     * @param context
     * @return
     */
    public static boolean isTablet7(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                == Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
    
    /**
     * >= large
     * 
     * @param context
     * @return
     */
    public static boolean isTablet10(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                == Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    public static boolean isAndroid3Tablet(Context context) {
        return isAndroid_3_x() && isTablet(context);
    }
    
    public static boolean isAndroid3Tablet7(Context context) {
        return isAndroid_3_x() && isTablet7(context);
    }
    
    public static boolean isAndroid3Tablet10(Context context) {
        return isAndroid_3_x() && isTablet10(context);
    }

	/**
	 * Retorna a vers�o do sistema operacional (1.1, 1.5, 1.6, 2.0, 2.1)
	 * 
	 * @return
	 */
	public static String getVersaoOS(){
		String os = Build.VERSION.RELEASE;
		return os;
	}

	public static String getDeviceModel(){
		String model = StringUtils.capitalize(Build.MANUFACTURER) + " - " + Build.MODEL;
		return model;
	}

	/**
	 * Faz o celular vibrar durante X ms
	 * 
	 * @param context
	 * @param timeMs
	 */
	public static void vibrar(Context context, int timeMs){
		Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
	    vibrator.vibrate(timeMs);
	}
	
	/**
	 * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
	 * 
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * respond to an intent with the specified action. If no suitable package is
	 * found, this method returns false.
	 *
	 * @param context The application's environment.
	 * @param action The Intent action to check for availability.
	 *
	 * @return True if an Intent with the specified action can be sent and
	 *         responded to, false otherwise.
	 */
	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	/**
	 * Verifica se a orienta��o do celular est� na Horizontal
	 * 
	 * @param resources
	 * @return
	 */
	public static boolean isHorizontal(Resources resources) {
		Configuration config = resources.getConfiguration();
		if(config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			return true;
		}
		return false;
	}
	
	/**
	 * Verifica se a orienta��o do celular est� na Vertical
	 * 
	 * @param resources
	 * @return
	 */
	public static boolean isVertical(Resources resources) {
		Configuration config = resources.getConfiguration();
		if(config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			return true;
		}
		return false;
	}

	/**
	 * Fecha o teclado virtual se aberto (view com foque)
	 */
	public static boolean closeVirtualKeyboard(Context context, View view) {
		// Fecha o teclado virtual
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm != null) {
			boolean b = imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			return b;
		}
		return false;
	}
	
	/**
	 * Ao tocar na View fecha o Teclado.
	 * 
	 * Util para fechar o layout no touch de um layout
	 * 
	 * @param context
	 * @param view
	 * @return
	 */
	public static void closeVirtualKeyBoardOnTouch(final Context context, final View view) {
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					IBinder windowToken = view.getWindowToken();
					imm.hideSoftInputFromWindow(windowToken, 0);
				}
			}
		});
	}
	
	/**
	 * Abre o teclado virtual
	 * @return 
	 */
	public static boolean showVirtualKeyboard(Context context, EditText text) {
		// Fecha o teclado virtual
		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm != null) {
			boolean b = imm.showSoftInput(text, 0);
			return b;
		}
		return false;
	}

	/**
	 * Recupera o Locale
	 * 
	 * @param context
	 * @return
	 */
	public static Locale getLocale(Context context) {
		Locale locale = context.getResources().getConfiguration().locale;
		return locale;
	}

	/**
	 * Retorna a largura da tela em pixels
	 * 
	 * @param context
	 * @return
	 */
	public static int getWidthPixels(Context context) {
		DisplayMetrics displayMetrics = getDisplayMetrics(context);
		int wpx = displayMetrics.widthPixels;
		return wpx;
	}

	public static DisplayMetrics getDisplayMetrics(Context context) {
		if(context == null) {
			return null;
		}
		return context.getResources().getDisplayMetrics();
	}
	
	/**
	 * Retorna a largura da tela em dip
	 * 
	 * @param context
	 * @return
	 */
	public static int getWidthDip(Context context) {
		int wpx = getWidthPixels(context);
		int wdip = (int) toDip(context, wpx);
		return wdip;
	}
	
	/**
	 * Retorna a altura da tela em pixels
	 * 
	 * @param context
	 * @return
	 */
	public static int getHeightPixels(Context context) {
		int hpx = getDisplayMetrics(context).heightPixels;
		return hpx;
	}

	/**
	 * Retorna a altura da tela em dip
	 * 
	 * @param context
	 * @return
	 */
	public static int getHeightDip(Context context) {
		int hpx = getHeightPixels(context);
		int hdip = (int) toDip(context, hpx);
		return hdip;
	}

	public static void killProcess() {
		int pid = Process.myPid();
		Process.killProcess(pid);
	}
	
	/**
	 * Retorna o tamanho da fonte em px.
	 * 
	 * Definido no XML como uma dimensao em dip
	 * 
	 * <resources>
	 * 	<dimen name="ticker_size">14sp</dimen>
	 * </resources>
	 * 
	 * @param context
	 * @param resDimen
	 * @return
	 */
	public static float getFont(Context context, int resDimen) {
		float d = DimenUtils.getDimen(context, resDimen);
		return d;
	}
	
	/**
	 * Converte de DIP para Pixels
	 * 
	 * @param context
	 * @param dip
	 * @return
	 */
	public static float toPixels(Context context, float dip) {
		Resources r = context.getResources();
		//int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
		
		float scale = r.getDisplayMetrics().density;
		int px = (int) (dip * scale + 0.5f);

		return px;
	}
	
	/**
	 * Converte de Pixels para DIP
	 * 
	 * @param context
	 * @param dip
	 * @return
	 */
	public static float toDip(Context context, float pixels) {
		Resources r = context.getResources();

		float scale = r.getDisplayMetrics().density;

		int dip = (int) (pixels / scale + 0.5f);
		return dip;
	}
	
	/**
	 * Verifica se esta rodando no emulador
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isEmulador(Context context) {
		String imei = AndroidUtils.getIMEI(context);
		if(StringUtils.isEmpty(imei)) {
			return true;
		}
		if(StringUtils.isInteger(imei)) {
			long n = Long.parseLong(imei);
			boolean emulador = n == 0;
			return emulador;
		}
		return false;
	}

	public static boolean isEmuladororDevice(Context context) {
		String imei = AndroidUtils.getIMEI(context);
		if ("357988021314328".equals(imei)) {
			// HTC Hero
			return true;
		}
		if ("356698031201222".equals(imei)) {
			// Motorola Milestone
			return true;
		}
		if("355031040408409".equals(imei)) {
			// Nexus S
			return true;
		}
		if("355031040408409".equals(imei)) {
			// Xoom Lecheta
			return true;
		}
		return isEmulador(context);
	}
	
	public static void ligar(Context context, String fone) {
		Uri uri = Uri.parse("tel:" + fone);
		Intent intent = new Intent(Intent.ACTION_CALL, uri);
		context.startActivity(intent);
	}
	
	public static void discar(Context context, String fone) {
		Uri uri = Uri.parse("tel:" + fone);
		Intent intent = new Intent(Intent.ACTION_DIAL, uri);
		context.startActivity(intent);
	}

	public void sms(Context contexto, String fone) {
		Uri uri = Uri.parse("sms:" + fone);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		contexto.startActivity(intent);
	}

	public static String getKeyEventString(int keyCode) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_0:
			return "0";
		case KeyEvent.KEYCODE_1:
			return "1";
		case KeyEvent.KEYCODE_2:
			return "2";
		case KeyEvent.KEYCODE_3:
			return "3";
		case KeyEvent.KEYCODE_4:
			return "4";
		case KeyEvent.KEYCODE_5:
			return "5";
		case KeyEvent.KEYCODE_6:
			return "6";
		case KeyEvent.KEYCODE_7:
			return "7";
		case KeyEvent.KEYCODE_8:
			return "8";
		case KeyEvent.KEYCODE_9:
			return "9";
		}
		return null;
	}
	
	/**
	 * Verifica se o celular tem determinada feature
	 * 
	 * PackageManager pm = getPackageManager();
	 * boolean hasTelephony = pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
	 * 
	 * @param context
	 * @param feature
	 * @return
	 */
	/*
	 * Removido devido a erro no Android 1.6
	 * 
	 * 03-30 22:33:57.178: ERROR/dalvikvm(318): Could not find method android.content.pm.PackageManager.hasSystemFeature, referenced from method br.livroandroid.utils.AndroidUtils.hasFeature03-30 22:33:57.198: ERROR/AndroidRuntime(318): Uncaught handler: thread main exiting due to uncaught exception
	 * 03-30 22:33:57.208: ERROR/AndroidRuntime(318): java.lang.VerifyError: br.livroandroid.utils.AndroidUtils
	 * 
	 * public static boolean hasFeature(Context context, String feature) {
		PackageManager pm = context.getPackageManager();
		//		String feature = PackageManager.FEATURE_TELEPHONY;
		boolean hasFeature = pm.hasSystemFeature(feature);
		return hasFeature;
	}*/
	
	/**
	 * activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	 * 
	 * @param activity
	 * @param orientation
	 */
	public static void setOrientation(Activity activity,int orientation) {
//		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		activity.setRequestedOrientation(orientation);
	}
	
	public static void setFullScreen(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

	}
	public static void setDefaultErrorHandler(final String TAG) {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread thread, Throwable e) {
				Log.e(TAG,"DefaultUncaughtExceptionHandler Thread["+thread.getName()+"] > " + e.getClass() + ":" +e.getMessage(), e);
			}
		});
	}

	/**
	 * <uses-permission android:name="android.permission.GET_TASKS"/>
	 * 
	 * "adb shell dumpsys activity"
	 * 
	 * @param context
	 * @param tag
	 */
	public static void printActivityStack(Context context, String logTag) {
		ActivityStackUtils.printTasks(context, logTag);
	}
	
	public static void printMemoryInfo(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(memoryInfo);

		Log.v(TAG," memoryInfo.availMem " + memoryInfo.availMem + "\n" );
		Log.v(TAG," memoryInfo.lowMemory " + memoryInfo.lowMemory + "\n" );
		Log.v(TAG," memoryInfo.threshold " + memoryInfo.threshold + "\n" );
	}
	
	/**
	 * Retorna o email que o usuario possui autenticado no celular
	 * @param context
	 * @return
	 */
	public static String getEmailFromDevice(Context context){
		Pattern emailPattern = Patterns.EMAIL_ADDRESS;
		Account[] accounts = AccountManager.get(context).getAccounts();
		String possibleEmail = null;
		for (Account account : accounts) {
		    if (emailPattern.matcher(account.name).matches()) {
		    	if(StringUtils.equalsIgnoreCase(account.type, "com.google")){
					possibleEmail = account.name;
			    }
		    }
		}
		return possibleEmail;
	}
	
	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 * 
	 * private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	 * 
	 */
//	public static boolean checkPlayServices(Activity activity) {
//	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity.getBaseContext());
//	    if (resultCode != ConnectionResult.SUCCESS) {
//	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//	            GooglePlayServicesUtil.getErrorDialog(resultCode, activity, 9000).show();
//	        } else {
//	            Log.i(TAG, "This device is not supported.");
//	            activity.finish();
//	        }
//	        return false;
//	    }
//	    return true;
//	}
	
	public static void enableView(View view, boolean enable){
		if(view != null){
			view.setEnabled(enable);
		    if (view instanceof ViewGroup ) {
		        ViewGroup group = (ViewGroup) view;

		        for ( int idx = 0 ; idx < group.getChildCount() ; idx++ ) {
		        	enableView(group.getChildAt(idx), enable);
		        }
		    }
		}
	}
}
