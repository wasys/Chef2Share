package com.android.utils.lib.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author rlecheta
 *
 */
public class BaseView extends View {
	protected static final String TAG = "livroandroid";
	protected Paint paintText = new Paint();
	protected Paint paintBackground = new Paint();
	protected int w;
	protected int h;

	public BaseView(Context context) {
		super(context);
		
		init();
	}

	public BaseView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		doMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	protected void doMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// foi definido layout_width="fill_parent", por default vai esticar
		int screenWidth = getScreenWidthPixels();
		int w = getMeasurement(widthMeasureSpec, screenWidth);

		// foi layout_height="wrap_content", vai pegar o valor fornecido=100
		int screenHeight = getScreenHeightPixels();
		int h = getMeasurement(heightMeasureSpec, screenHeight);

		// Precisa definir o tamanho da View aqui
		setMeasuredDimension(w, h);
	}

	/**
	 * Respeita o wrap_content ou fill_parent.
	 * 
	 * Se wrap_content, retorna o valor default preferred
	 * 
	 * @param measureSpec
	 * @param preferred
	 * @return
	 */
	protected int getMeasurement(int measureSpec, int preferred) {
		// specSize ? o total em px disponivel para esta View
		int specSize = MeasureSpec.getSize(measureSpec);
		int measurement = 0;
		int mode = MeasureSpec.getMode(measureSpec);
		switch (mode) {
		case MeasureSpec.EXACTLY:
			// fill_parent
			// This means the width of this view has been given.
			measurement = specSize;
			break;
		case MeasureSpec.AT_MOST:
			// wrap_content
			// Take the minimum of the preferred size and what
			// we were told to be.
			measurement = Math.min(preferred, specSize);
			break;
		default:
			measurement = preferred;
			break;
		}

		return measurement;
	}

	protected void init() {
		paintText.setColor(Color.BLACK);
//		paintText.setTextSize(16);
		paintBackground.setColor(Color.WHITE);
		paintBackground.setStyle(Style.FILL);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		this.w = w;
		this.h = h;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawRect(0, 0, w, h, paintBackground);

		canvas.drawText("Please Override", w / 2, h / 2, paintText);
	}
	
	protected int getTextWidth(String str, Paint p) {
		int i = CanvasUtils.getTextWidth(str, p);
		return i;
	}

	protected int getTextHeight(String str, Paint p) {
		int i = CanvasUtils.getTextHeight(str, p);
		return i;
	}

	/**
	 * Retorna a largura da tela em pixels
	 * 
	 * @param context
	 * @return
	 */
	protected int getScreenWidthPixels() {
		int wpx = getContext().getResources().getDisplayMetrics().widthPixels;
		return wpx;
	}
	
	/**
	 * Retorna a altura da tela em pixels
	 * 
	 * @param context
	 * @return
	 */
	protected int getScreenHeightPixels() {
		int hpx = getContext().getResources().getDisplayMetrics().heightPixels;
		return hpx;
	}
	/**
	 * Retorna o tamanho da fonte em px.
	 * 
	 * Definido no XML como uma dimensao em dip
	 * 
	 * <resources>
	 * 	<dimen name="ticker_size">14sp</dimen>
	 * </resources>
	 * 
	 * @param context
	 * @param resDimen
	 * @return
	 */
	protected int getFontSize(int resDimen) {
		Resources res = getContext().getResources();
		int size = res.getDimensionPixelSize(resDimen);
		return size;
	}
	
	/**
	 * Converte de DIP para Pixels
	 * 
	 * @param context
	 * @param dip
	 * @return
	 */
	protected float toPixels(float dip) {
		Resources r = getContext().getResources();
		//int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
		
		float scale = r.getDisplayMetrics().density;
		int px = (int) (dip * scale + 0.5f);

		return px;
	}
	
	/**
	 * Converte de Pixels para DIP
	 * 
	 * @param context
	 * @param dip
	 * @return
	 */
	protected float toDip(float pixels) {
		Resources r = getContext().getResources();

		float scale = r.getDisplayMetrics().density;

		int dip = (int) (pixels / scale + 0.5f);
		return dip;
	}
}
