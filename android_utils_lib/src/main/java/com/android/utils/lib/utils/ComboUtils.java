package com.android.utils.lib.utils;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

public class ComboUtils {

	public static Spinner setItems(Activity context, int resId, List<String> items) {
		Spinner combo = (Spinner) context.findViewById(resId);

		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		for (String s : items) {
			adapter.add(s);
		}

		combo.setAdapter(adapter);
		combo.setSelection(0);

		return combo;
	}
}
