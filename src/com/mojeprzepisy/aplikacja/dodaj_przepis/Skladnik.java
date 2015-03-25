package com.mojeprzepisy.aplikacja.dodaj_przepis;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mojeprzepisy.aplikacja.R;
import com.mojeprzepisy.aplikacja.narzedzia.MyTypeFace;

public class Skladnik extends DodajSkladniki implements OnClickListener {

	private String nazwa = null;
	private ViewGroup container;
	private View usun;
	private TextView nazwa_textview;
	private EditText nazwa_edittext;
	private ImageView zapisz;
	private DodajSkladniki dodajS;

	public Skladnik(Activity _root, ViewGroup _container, DodajSkladniki _dodajS) {
		super();
		dodajS = _dodajS;
		root = _root;
		container = _container;
	}

	public Skladnik(Activity _root, ViewGroup _container,
			DodajSkladniki _dodajS, String _nazwa) {
		super();
		dodajS = _dodajS;
		root = _root;
		container = _container;
		this.nazwa=_nazwa;
	}

	public Skladnik(String _nazwa) {
		this.nazwa = _nazwa;
	}

	public View toView() {
		LayoutInflater inflater = (LayoutInflater) root
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.single_skladnik_layout, container,
				false);
		usun = v.findViewById(R.id.skladnik_usun_button);
		nazwa_textview = (TextView) v
				.findViewById(R.id.single_skladnik_textview);
		nazwa_edittext = (EditText) v
				.findViewById(R.id.single_skladnik_edittext);
		zapisz = (ImageView) v.findViewById(R.id.single_skladnik_save);
		zapisz.setOnClickListener(this);
		nazwa_textview.setOnClickListener(this);
		if(nazwa!=null)
			nazwa_textview.setText(nazwa);
		usun.setOnClickListener(this);
		nazwa_edittext.setVisibility(View.GONE);
		zapisz.setVisibility(View.GONE);
		return v;
	}

	public View wyswietl(Activity _root, ViewGroup _container) {
		LayoutInflater inflater = (LayoutInflater) _root
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.single_skladnik_layout, _container,
				false);
		usun = v.findViewById(R.id.skladnik_usun_button);
		nazwa_textview = (TextView) v
				.findViewById(R.id.single_skladnik_textview);
		nazwa_edittext = (EditText) v
				.findViewById(R.id.single_skladnik_edittext);
		zapisz = (ImageView) v.findViewById(R.id.single_skladnik_save);
		nazwa_textview.setText("- "+nazwa);
		nazwa_edittext.setVisibility(View.GONE);
		zapisz.setVisibility(View.GONE);
		usun.setVisibility(View.GONE);
		return v;
	}

	@Override
	public void onClick(View v) {
		if (v == usun) {
			container.removeView((View) v.getParent());
			dodajS.skladniki.remove(this);
			// dodajS.wypisz();
		} else if (v == nazwa_textview) {
			// nazwa = nazwa_textview.getText().toString();
			nazwa_textview.setVisibility(View.GONE);
			nazwa_edittext.setVisibility(View.VISIBLE);
			zapisz.setVisibility(View.VISIBLE);
			nazwa_edittext.setText(nazwa);
			nazwa_edittext.requestFocus();
		} else if (v == zapisz) {
			nazwa = nazwa_edittext.getText().toString();
			nazwa_edittext.setVisibility(View.GONE);
			zapisz.setVisibility(View.GONE);
			nazwa_textview.setVisibility(View.VISIBLE);
			if (nazwa.length() == 0)
				nazwa = root.getResources().getString(
						R.string.dotknij_aby_edytowac);
			nazwa_textview.setText(nazwa);
		}
	}

	@Override
	public String toString() {
		return nazwa;
	}
}
