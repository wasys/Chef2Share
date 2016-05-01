package com.android.utils.lib.utils;

import android.os.AsyncTask;

/**
 * @author Ricardo Lecheta
 *
 */
public class AsyncTaskUtils {

	@SuppressWarnings("rawtypes")
	public static boolean cancel(AsyncTask t) {
		if(t != null) {
			boolean executando = t.getStatus().equals(AsyncTask.Status.RUNNING);
			if(executando) {
				t.cancel(true);
				return true;
			}
		}
		return false;
	}
}
