package br.com.chef2share;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.widget.TextView;

import com.android.utils.lib.infra.AppUtil;

import org.androidannotations.annotations.App;

/**
 * Created by Jonas on 16/11/2015.
 */
public class SuperUtil extends AppUtil{

    public static final String TAG = "Chef2Share.log";
    static {
        AppUtil.TAG = SuperUtil.TAG;
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
        try {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setIcon(icon)
                    .setTitle(titulo)
                    .setMessage(message)
                    .create();
            dialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            dialog.show();
        } catch (Exception e) {
            logError(context, e.getMessage(), e);
        }
    }

    public static void setUnderlineTextView(TextView textView) {
        if(textView != null) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }
    public static double toDoublePrimitivo(Double valor) {
        return valor != null ? (double)valor : 0;
    }
}
