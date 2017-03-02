package com.dailyasianage.android.TextView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class LobsterTwoButton extends Button {
	public static Typeface m_typeFace = null;

	public LobsterTwoButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		if (isInEditMode()) {
			return;
		}
		loadTypeFace(context);
	}

	public LobsterTwoButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) {
			return;
		}
		loadTypeFace(context);
	}

	public LobsterTwoButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (isInEditMode()) {
			return;
		}
		loadTypeFace(context);
	}

	private void loadTypeFace(Context context) {
		if (m_typeFace == null)
			m_typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/LobsterTwo-BoldItalic.ttf");
		this.setTypeface(m_typeFace);
	}
}
