package com.android.utils.lib.infra;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;

import com.android.utils.lib.utils.StringUtils;

/**
 * 
 * @author Jonas Baggio
 * @create 02/10/2012
 */
public class Pref {

	private static String PREF_ID = AppUtil.TAG + "_prefs";
	private static final String LOGIN_KEY = "login_key";
	private static final String PROPOSTA_KEY = "proposta_key";

	protected static void setString(Context context, String flag, String valor) {
		SharedPreferences pref = context.getSharedPreferences(PREF_ID, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(flag, valor);
		editor.commit();
	}
	

	protected static boolean getBoolean(Context context, String flag) {
		SharedPreferences pref = context.getSharedPreferences(PREF_ID, 0);
		boolean b = pref.getBoolean(flag, false);
		return b;
	}

	protected static void setBoolean(Context context, String flag, boolean b) {
		SharedPreferences pref = context.getSharedPreferences(PREF_ID, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(flag, b);
		editor.commit();
	}

	protected static String getString(Context context, String flag) {
		SharedPreferences pref = context.getSharedPreferences(PREF_ID, 0);
		String s = pref.getString(flag, "");
		return s;
	}
	
	public static void setAtivado(Context context, boolean ativado) {
		setBoolean(context, "ativado", ativado);
	}
	
	public static boolean isAtivado(Context context) {
		return getBoolean(context, "ativado");
	}

	public static void setLogin(Context context, String login, String senha) {
		setString(context, LOGIN_KEY, login);
	}

	public static String getLogin(Context context){
		return getString(context, LOGIN_KEY);
	}


	public static void setkill(Context context, boolean kill) {
		setBoolean(context, "kill", kill);
	}
	
	public static boolean isKill(Context context) {
		return getBoolean(context, "kill");
	}


	public static String getRegId(Context context) {
		return getString(context, "REG_GCM_ID");
	}
	
	public static void setRegId(Context context, String regId){
		setString(context, "REG_GCM_ID", regId);
	}


	public static void setProposta(Context context, String job) {
		setString(context, PROPOSTA_KEY, job);
	}
	
	public static String getProposta(Context context) {
		return getString(context, PROPOSTA_KEY);
	}
	
	/**
	 * Gera um número de controle para nao salvar propostas repetidas
	 * @param context
	 * @return
	 */
	public static String getProximoNumeroProposta(Context context){
		String atual = getString(context, "n_protocolo");
		if(StringUtils.isEmpty(atual)){
			atual = "0";
		}
		String novo = String.valueOf((Long.parseLong(atual) + 1));
		novo = StringUtils.leftPad(novo, 5, '0');
		setString(context, "n_protocolo", novo);
		return novo;
	}
	
	public static void setCoordenadas(Context context, Location lastLocation) {
		setString(context, "lat", String.valueOf(lastLocation.getLatitude()));
		setString(context, "lng", String.valueOf(lastLocation.getLongitude()));
	}
	
	public static String getLat(Context context) {
		return getString(context, "lat");
	}
	public static String getLng(Context context) {
		return getString(context, "lng");
	}
}
