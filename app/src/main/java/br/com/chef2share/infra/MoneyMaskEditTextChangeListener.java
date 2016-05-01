package br.com.chef2share.infra;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.text.NumberFormat;


/**
 * Created by Jonas on 07/01/2016.
 */
public class MoneyMaskEditTextChangeListener implements TextWatcher {
    private static MoneyMaskEditTextChangeListener instance;
    private EditText mEditText;
    private String current = "";

    public MoneyMaskEditTextChangeListener(final EditText editText) {
        this.mEditText = editText;
        this.mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    try {
                        mEditText.setSelection(editText.getText().toString().length());
                    }catch(Exception e){

                    }
                }
            }
        });
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(!s.toString().equals(current)){
            mEditText.removeTextChangedListener(this);

            String cleanString = s.toString().replaceAll("[R$,.]", "");

            double parsed = Double.parseDouble(cleanString);
            String formatted = NumberFormat.getCurrencyInstance().format((parsed/100));

            current = formatted;
            mEditText.setText(formatted);
            mEditText.setSelection(formatted.length());

            mEditText.addTextChangedListener(this);
        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void afterTextChanged(Editable s) {
    }

    public static String unmask(String s) {
        return s.replaceAll("[$,.]", "");
    }

    public static String mask(String format, String text) {
        String maskedText = "";
        int i = 0;
        char[] arr$ = format.toCharArray();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            char m = arr$[i$];
            if(m != 35) {
                maskedText = maskedText + m;
            } else {
                try {
                    maskedText = maskedText + text.charAt(i);
                } catch (Exception var9) {
                    break;
                }

                ++i;
            }
        }

        return maskedText;
    }
}
