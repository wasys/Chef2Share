package com.android.utils.lib.exception;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.android.utils.lib.utils.AndroidUtils;
import com.android.utils.lib.utils.DateUtils;
import com.android.utils.lib.utils.ExceptionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

/**
 * @author Bruno Scrok Brunoro
 * @create 16/4/15 15:17
 * @project TIM_Field_Android
 */
public class ExceptionLogReport  implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler defaultUEH;
    private Context context;


    public ExceptionLogReport(Context context) {
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        WriteFile(context, ExceptionUtils.getStackTrace(e));
        defaultUEH.uncaughtException(t, e);
    }
    public static void WriteFile(Context context, String msg) {
        try {
            File dir = new File(Environment.getExternalStorageDirectory(),context.getPackageName());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir.getAbsolutePath(),context.getPackageName()+".log");

            StringBuilder logErro = new StringBuilder();
            logErro.append("---------------------------------------------------------------\n");
            logErro.append(DateUtils.toString(new Date(),DateUtils.DATE_TIME_24h));
            logErro.append("\n Width = " + String.valueOf(AndroidUtils.getWidthPixels(context)));
            logErro.append("\n Height = " + String.valueOf(AndroidUtils.getHeightPixels(context)));
            logErro.append("\n VersionApp = " + AndroidUtils.getAppVersionName(context));
            logErro.append("\n VersionSO = " + String.valueOf(AndroidUtils.getVersaoOS()));
            logErro.append("\n SO = ANDROID");
            logErro.append("\n IMEI = " + String.valueOf(AndroidUtils.getIMEI(context)));
            logErro.append("\n MODEL = " + Build.MODEL);
            logErro.append("\n\n\n");
            logErro.append(msg);

            if (file.exists()) {
                FileOutputStream out = new FileOutputStream(file, true);
                out.write(logErro.toString().getBytes());
                out.write("\n".getBytes());
                out.flush();
                out.close();
            } else {
                file.createNewFile();
                FileOutputStream out = new FileOutputStream(file, true);
                out.write(logErro.toString().getBytes());
                out.write("\n".getBytes());
                out.flush();
                out.close();
            }

        } catch (Exception e) {
            Log.e("Log", e.toString());
        }
    }
}