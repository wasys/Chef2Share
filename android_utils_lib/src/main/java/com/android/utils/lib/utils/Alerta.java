package com.android.utils.lib.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

/**
 * Classe utilit�ria para exibir alertas
 * 
 * @author ricardo
 *
 */
public class Alerta {

	public static void show(Context contexto,String msg){
		AlertDialog a = new AlertDialog.Builder(contexto).setMessage(msg).show();
		a.show();
	}

	public static void showToast(Context contexto,String msg){
		Toast.makeText(contexto, msg, Toast.LENGTH_SHORT).show();
	}
}