package com.android.utils.lib.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;

import com.android.utils.lib.utils.ImageUtils;

/**
 * @author rlecheta
 * 
 */
public class PageView extends BaseView {

	private static final int MIN_HEIGHT = 15;
	private int pageSize;
	private int page;
	private Bitmap imgOff;
	private Bitmap imgOn;
	private int colorOn = Color.DKGRAY;
	private int colorOff = Color.LTGRAY;
	private boolean imgSize;

	public PageView(Context context) {
		super(context);
	}

	public PageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void init() {
		super.init();

		if (isInEditMode()) {
			pageSize = 3;
		}

		// imgOff = BitmapFactory.decodeResource(getContext().getResources(),
		// R.drawable.page_control_off);
		// imgOn = BitmapFactory.decodeResource(getContext().getResources(),
		// R.drawable.page_control_on);
	}

	protected void doMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (imgSize) {
			// foi definido layout_width="fill_parent", por default vai esticar
			int screenWidth = getScreenWidthPixels();
			int w = getMeasurement(widthMeasureSpec, screenWidth);

			// foi layout_height="wrap_content", vai pegar o valor fornecido=100
			int h = imgOn != null ? imgOn.getHeight() : MIN_HEIGHT;
			h = MeasureSpec.getSize(h);

			// Precisa definir o tamanho da View aqui
			setMeasuredDimension(w, h);
		} else {
			super.doMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// super.onDraw(canvas);

		paintBackground.setColor(Color.TRANSPARENT);
		canvas.drawRect(0, 0, w, h, paintBackground);

		int x = w / 2;

		int offsetX = 0;
		if (imgOn != null) {
			offsetX = (imgOn.getWidth() + 10) * pageSize;
		} else {
			offsetX = 20 * pageSize;
		}

		x = w / 2 - offsetX / 2;

		int imgH = imgOn != null ? imgOn.getHeight() : 6;

		int y = (getMeasuredHeight() / 2) - (imgH / 2);

		for (int i = 0; i < pageSize; i++) {
			if (imgOn != null) {
				canvas.drawBitmap(i == page ? imgOn : imgOff, x, y, paintText);
				x += imgOn.getWidth() + 10;
			} else {
				if (i == page) {
					paintText.setColor(colorOn);
				} else {
					paintText.setColor(colorOff);
				}
				canvas.drawCircle(x, y, 4, paintText);
				x += 16;
			}
		}

		// canvas.drawText("Page " + page, w / 2, h / 2, paintText);
	}

	/**
	 * @param page
	 *            current page
	 * @param pageSize
	 *            PageControl.getPageSize()
	 */
	public boolean setPage(int page) {
		if (this.page == page) {
			return false;
		}
		if (this.pageSize == 0) {
			// throw new
			// IllegalStateException("Please call setPageSize(size) before!");
			return false;
		}
		if (page >= pageSize) {
			// Fix it
			page = pageSize - 1;
		}
		this.page = page;
		invalidate();

		return true;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		invalidate();
	}

	public void setImgOnOff(Context context, int imgOn, int imgOff) {
		this.imgOn = ImageUtils.getBitmap(context, imgOn);
		this.imgOff = ImageUtils.getBitmap(context, imgOff);
	}

	public void setImgOnOff(Bitmap imgOn, Bitmap imgOff) {
		this.imgOn = imgOn;
		this.imgOff = imgOff;
	}

	public void setColorOnOff(int colorOn, int colorOff) {
		this.colorOn = colorOn;
		this.colorOff = colorOff;
	}

	public void setImgSize(boolean imgSize) {
		this.imgSize = imgSize;
	}
}
