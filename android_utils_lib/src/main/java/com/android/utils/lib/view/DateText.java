package com.android.utils.lib.view;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.utils.lib.text.format.DateFormatTextWatcher;
import com.android.utils.lib.utils.DateUtils;
import com.android.utils.lib.utils.StringUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * DateText ao fazer touch abre DatePicker
 * 
 * Formata com pattern customizado
 * 
 * @author ricardo
 *
 */
public class DateText extends EditText implements OnDateSetListener, OnTouchListener {
	private static final String TAG = "DateText";
	private boolean dialogOpen;
	private int mYear;
	private int mMonth;
	private int mDay;
	private String format = "dd/MM/yyyy";

	public DateText(Context context) {
		super(context);

		init();
	}

	public DateText(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	private void init() {
		// Listener
		addTextChangedListener(new DateFormatTextWatcher(this));

		setSingleLine();

		/**
		 * Abre DatePicker
		 */
		setOnTouchListener(this);
	}
	
	@Override
	public boolean onTouch(View view, MotionEvent arg) {
		if(dialogOpen) {
			return true;
		}
		
		dialogOpen = true;

		final Calendar c = Calendar.getInstance();

		Date date = getDate();
		if(date != null) {
			// Ja existe a data setada
			c.setTime(date);
		}
		
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		
		DatePickerDialog dialog = new DatePickerDialog(getContext(), this, mYear,mMonth, mDay);

		dialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				dialogOpen = false;
			}
		});
		dialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				dialogOpen = false;						
			}
		});
		dialog.show();
		return true;
	}

	private void updateDisplay() {
		StringBuilder sb = new StringBuilder().append(mDay).append("/").append(mMonth + 1)
		.append("/").append(mYear);
		Date data = DateUtils.toDate(sb.toString());
		String format = getFormat();
		String s = DateUtils.toString(data,format);
		setText(s);
	}

	private String getFormat() {
		return format;
	}
	
	public void setFormat(String format) {
		this.format = format;
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
		dialogOpen = false;
		mYear = year;
		mMonth = monthOfYear;
		mDay = dayOfMonth;
		updateDisplay();
	}

	public Date getDate() {
		String s = getText().toString();
		if(StringUtils.isEmpty(s)) {
			return null;
		}
		try {
			Date date = DateUtils.toDate(s,format);
			return date;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			return null;
		}
	}
}
