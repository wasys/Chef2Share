package com.android.utils.lib.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.ProgressBar;

/**
 * @author Ricardo Lecheta
 *
 */
public class ProgressUtils {
	
	public static ProgressDialog showProgressDialog(Activity activity, String title, String aguarde) {
		final ProgressDialog progress = ProgressDialog.show(activity, "", "Carregando...",false, true);
		return progress;
	}
	
	public static void hideProgressDialog(ProgressDialog progress) {
		try {
			if(progress != null) {
				progress.dismiss();
			}
		} catch (Throwable e) {
		}
	}

	public static void showProgress(Activity activity, int progressId) {
		ProgressBar progress = (ProgressBar) activity.findViewById(progressId);
		if(progress != null) {
			progress.setVisibility(View.VISIBLE);
		}
	}

	public static void hideProgress(Activity activity, int progressId) {
		ProgressBar progress = (ProgressBar) activity.findViewById(progressId);
		if(progress != null) {
			progress.setVisibility(View.INVISIBLE);
		}
	}

	public static void showProgress(View view, int progressId) {
		if(view != null) {
			ProgressBar progress = (ProgressBar) view.findViewById(progressId);
			if(progress != null) {
				progress.setVisibility(View.VISIBLE);
			}
		}
	}

	public static void hideProgress(View view, int progressId) {
		if(view != null) {
			ProgressBar progress = (ProgressBar) view.findViewById(progressId);
			if(progress != null) {
				progress.setVisibility(View.INVISIBLE);
			}
		}
	}
	/*
	public static void showProgress(Fragment frag, int progressId) {
		View view = frag.getView();
		showProgress(view, progressId);
	}
	
	public static void hideProgress(Fragment frag, int progressId) {
		View view = frag.getView();
		hideProgress(view, progressId);
	}
	*/
}
