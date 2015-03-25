package com.mojeprzepisy.aplikacja.dodaj_przepis;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mojeprzepisy.aplikacja.R;
import com.mojeprzepisy.aplikacja.narzedzia.MyTypeFace;

public class Krok extends DodajOpis implements OnClickListener, TextWatcher {

	private DodajOpis dodajO;
	private ViewGroup container = null;
	private TextView tittle_tv;
	private EditText tittle_et;
	private EditText opis_et;
	private TextView opis_tv;
	private ImageView usun;
	private ImageView zapisz;
	public String tytul = null, opis = null;
	private View view;

	public Krok(Activity _dodajOpis, ViewGroup _container, DodajOpis _dodajO) {
		super();
		root = _dodajOpis;
		container = _container;
		dodajO = _dodajO;
	}

	public Krok(String _tytul, String _opis) {
		this.tytul = _tytul;
		this.opis = _opis;
		opis = opis.replace("<br>", "\n");
	}

	public Krok(String _tytul, String _opis, Activity _dodajOpis,
			ViewGroup _container, DodajOpis _dodajO) {
		root = _dodajOpis;
		container = _container;
		dodajO = _dodajO;
		this.tytul = _tytul;
		this.opis = _opis;
		opis = opis.replace("<br>", "\n");

	}

	public View toView() {
		LayoutInflater inflater = (LayoutInflater) root
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.krok_layout, container, false);
		view = v;
		tittle_tv = (TextView) v.findViewById(R.id.krok_tittle_textview);
		tittle_et = (EditText) v.findViewById(R.id.krok_tittle_edittext);
		if (tytul != null && !(tytul.equals("null")))
			tittle_tv.setText(tytul);
		else
			tittle_tv.setText(root.getResources().getString(
					R.string.dotknij_aby_edytowac));
		opis_et = (EditText) v.findViewById(R.id.krok_opis);
		if (opis != null)
			opis_et.setText(opis);
		usun = (ImageView) v.findViewById(R.id.krok_tittle_delete);
		zapisz = (ImageView) v.findViewById(R.id.krok_tittle_save);
		tittle_tv.setOnClickListener(this);
		usun.setOnClickListener(this);
		zapisz.setOnClickListener(this);
		opis_et.addTextChangedListener(this);
		zapisz.setVisibility(View.GONE);
		tittle_et.setVisibility(View.GONE);
		return v;
	}

	public View wyswietl(Activity _root, ViewGroup _container) {
		LayoutInflater inflater = (LayoutInflater) _root
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.krok_layout, _container, false);
		view = v;
		tittle_tv = (TextView) v.findViewById(R.id.krok_tittle_textview);
		tittle_et = (EditText) v.findViewById(R.id.krok_tittle_edittext);
		opis_et = (EditText) v.findViewById(R.id.krok_opis);
		opis_tv = (TextView) v.findViewById(R.id.krok_opis_textview);
		usun = (ImageView) v.findViewById(R.id.krok_tittle_delete);
		zapisz = (ImageView) v.findViewById(R.id.krok_tittle_save);
		zapisz.setVisibility(View.GONE);
		tittle_et.setVisibility(View.GONE);
		usun.setVisibility(View.GONE);
		opis_et.setVisibility(View.GONE);
		tittle_tv.setText(tytul);
		opis_tv.setText(opis);
		return v;
	}

	@Override
	public void onClick(View v) {
		if (v == usun) {
			container.removeView(view);
			dodajO.kroki.remove(this);
			// dodajS.wypisz();
		} else if (v == tittle_tv) {
			tytul = tittle_tv.getText().toString();
			if (tytul.equals(root.getString(R.string.dotknij_aby_edytowac)))
				tytul = null;
			tittle_tv.setVisibility(View.GONE);
			tittle_et.setVisibility(View.VISIBLE);
			zapisz.setVisibility(View.VISIBLE);
			tittle_et.setText(tytul);
			tittle_et.requestFocus();
		} else if (v == zapisz) {
			if (tittle_et.getText().toString().length() != 0)
				tytul = tittle_et.getText().toString();
			else tytul = root.getString(R.string.dotknij_aby_edytowac);
			tittle_et.setVisibility(View.GONE);
			zapisz.setVisibility(View.GONE);
			tittle_tv.setVisibility(View.VISIBLE);
			tittle_tv.setText(tytul);
		}
	}

	@Override
	public String toString() {
		return opis_tv.getText().toString();
	}
	public String toShare(){
		String wynik="";
		wynik+=tytul+":\n"+opis+"\n";
		return wynik;
	}

	@Override
	public void afterTextChanged(Editable s) {
		opis = opis_et.getText().toString();

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}
}
