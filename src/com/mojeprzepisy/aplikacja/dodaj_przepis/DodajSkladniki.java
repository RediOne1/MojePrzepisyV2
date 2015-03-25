package com.mojeprzepisy.aplikacja.dodaj_przepis;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.mojeprzepisy.aplikacja.R;

public class DodajSkladniki extends DodajPrzepisActivity implements
		OnClickListener {

	private View dodajSkladnikButton;
	public LinearLayout linearLayout;
	public List<Skladnik> skladniki = new ArrayList<Skladnik>();

	public DodajSkladniki() {

	}

	public DodajSkladniki(Activity dodajPrzepisActivity) {
		root = dodajPrzepisActivity;
		dodajSkladnikButton = (View) root
				.findViewById(R.id.dodaj_skladnik_button);
		linearLayout = (LinearLayout) root
				.findViewById(R.id.skladniki_linearLayout);
		dodajSkladnikButton.setOnClickListener(this);
	}

	public DodajSkladniki(Activity _root, String _skladniki) {
		root = _root;
		dodajSkladnikButton = (View) root
				.findViewById(R.id.dodaj_skladnik_button);
		linearLayout = (LinearLayout) root
				.findViewById(R.id.skladniki_linearLayout);
		String temp[] = _skladniki.split(";");
		for (String s : temp) {
			if (s.length() < 1)
				continue;
			Skladnik skladnik = new Skladnik(root, linearLayout,
					DodajSkladniki.this, s);
			skladniki.add(skladnik);
			linearLayout.addView(skladnik.toView());
		}

		dodajSkladnikButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == dodajSkladnikButton) {
			Skladnik skladnik = new Skladnik(root, linearLayout,
					DodajSkladniki.this);
			skladniki.add(skladnik);
			linearLayout.addView(skladnik.toView());
		}
	}

	@Override
	public String toString() {
		String wynik = "";
		for (int i = 0; i < skladniki.size(); i++) {
			if (skladniki.get(i).toString() == null)
				continue;
			wynik += skladniki.get(i) + ";";
		}
		return wynik;
	}
}
