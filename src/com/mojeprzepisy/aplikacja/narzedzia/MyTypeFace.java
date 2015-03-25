package com.mojeprzepisy.aplikacja.narzedzia;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MyTypeFace {

	private TextView textView = null;
	private Button button = null;
	private Typeface MyNormal;
	private Typeface MyBold;

	public MyTypeFace(TextView _textView, Activity root) {
		textView = _textView;
		MyNormal = Typeface.createFromAsset(root.getBaseContext().getAssets(),
				"fonts/SEGOEPR.TTF");
		MyBold = Typeface.createFromAsset(root.getBaseContext().getAssets(),
				"fonts/SEGOEPRB.TTF");
	}

	public MyTypeFace(Button _button, Activity root) {
		button = _button;
		MyNormal = Typeface.createFromAsset(root.getBaseContext().getAssets(),
				"fonts/SEGOEPR.TTF");
		MyBold = Typeface.createFromAsset(root.getBaseContext().getAssets(),
				"fonts/SEGOEPRB.TTF");
	}

	public View MyBold() {
		if (textView == null) {
			button.setTypeface(MyBold);
			return button;
		} else {
			textView.setTypeface(MyBold);
			return textView;

		}
	}

	public View MyNormal() {
		if (textView == null) {
			button.setTypeface(MyNormal);
			return button;
		} else {
			textView.setTypeface(MyNormal);
			return textView;
		}
	}
}
