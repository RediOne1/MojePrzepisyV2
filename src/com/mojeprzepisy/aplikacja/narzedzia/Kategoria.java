package com.mojeprzepisy.aplikacja.narzedzia;

import java.io.Serializable;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mojeprzepisy.aplikacja.R;

public class Kategoria implements Serializable {
	public String nazwa;
	public int ilosc = -1;

	/**
	 * Konstruktor tworzący nową kategorie o podanej nazwie
	 * 
	 * @param _nazwa
	 *            Nazwa kategori.
	 */
	public Kategoria(String _nazwa) {
		this.nazwa = _nazwa;
	}

	@Override
	public String toString() {
		if (ilosc != -1)
			return nazwa + "(" + ilosc + ")";
		else
			return nazwa;
	}

	public View toView(Activity root, LinearLayout container) {
		LayoutInflater inflater = (LayoutInflater) root
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.kategoria, container, false);
		TextView tv = (TextView) v.findViewById(R.id.nazwa_kategoria_tv);
		tv.setText(toString());
		return v;
	}
}
