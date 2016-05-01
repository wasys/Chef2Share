package br.com.chef2share.infra;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import com.android.utils.lib.utils.AndroidUtils;
import com.android.utils.lib.utils.ExceptionUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

import br.com.chef2share.SuperUtil;
import br.com.chef2share.activity.SuperExceptionHandlerActivity;
import br.com.chef2share.activity.SuperExceptionHandlerActivity_;

/**
 * Created by Jonas on 24/11/2015.
 */
public class SuperExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final Activity myContext;
    private final String LINE_SEPARATOR = "\n";

    public SuperExceptionHandler(Activity context) {
        myContext = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("\n************ Device info ***********\n");
        errorReport.append("Versão Aplicativo: ");
        errorReport.append(AndroidUtils.getAppVersionName(myContext));
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);

        errorReport.append("************ StackTrace ************\n\n");
        errorReport.append(ExceptionUtils.getStackTrace(exception));

        SuperUtil.logError(exception.getMessage(), exception);

        Intent intent = new Intent(myContext, SuperExceptionHandlerActivity_.class);
        intent.putExtra("ERRO", errorReport.toString());
        myContext.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }
}
