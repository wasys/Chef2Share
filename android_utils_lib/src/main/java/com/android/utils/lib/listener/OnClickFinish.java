package com.android.utils.lib.listener;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

public class OnClickFinish implements OnClickListener {

	private final Activity context;

	public OnClickFinish(Activity context) {
		this.context = context;
	}

	@Override
	public void onClick(View view) {
		context.finish();
	}

}
