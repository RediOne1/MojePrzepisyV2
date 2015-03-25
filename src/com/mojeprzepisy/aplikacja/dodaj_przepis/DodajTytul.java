package com.mojeprzepisy.aplikacja.dodaj_przepis;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mojeprzepisy.aplikacja.R;
import com.mojeprzepisy.aplikacja.narzedzia.MyTypeFace;

public class DodajTytul extends DodajPrzepisActivity implements OnClickListener {

	private TextView tytul_tv;
	private EditText tytul_et;
	private ImageView save_image;
	public String tytul="";

	public DodajTytul(Activity dodajPrzepisActivity) {
		root = dodajPrzepisActivity;
		tytul_tv = (TextView) root.findViewById(R.id.dodaj_tytul_textview);
		tytul_et = (EditText) root.findViewById(R.id.dodaj_tytul_edittext);
		save_image = (ImageView) root.findViewById(R.id.dodaj_tytul_save);
		save_image.setOnClickListener(this);
		tytul_tv.setOnClickListener(this);
		tytul_et.setOnClickListener(this);
		tytul_tv = (TextView) new MyTypeFace(tytul_tv, root).MyBold();
		tytul_et.setVisibility(View.GONE);
		save_image.setVisibility(View.GONE);
	}

	public DodajTytul(Activity root, String tytul2) {
		this.root = root;
		tytul = tytul2;
		tytul_tv = (TextView) root.findViewById(R.id.dodaj_tytul_textview);
		tytul_et = (EditText) root.findViewById(R.id.dodaj_tytul_edittext);
		save_image = (ImageView) root.findViewById(R.id.dodaj_tytul_save);
		save_image.setOnClickListener(this);
		tytul_tv.setOnClickListener(this);
		tytul_et.setOnClickListener(this);
		tytul_tv = (TextView) new MyTypeFace(tytul_tv, root).MyBold();
		tytul_et.setText(tytul);
		tytul_tv.setText(tytul);
		tytul_et.setVisibility(View.GONE);
		save_image.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		if (v == tytul_tv) {
			tytul = tytul_tv.getText().toString();
			tytul_tv.setVisibility(View.GONE);
			save_image.setVisibility(View.VISIBLE);
			tytul_et.setVisibility(View.VISIBLE);
			// tytul_et.setText(temp_tytul);
			tytul_et.requestFocus();
		} else if (v == save_image) {
			tytul = tytul_et.getText().toString();
			tytul_et.setVisibility(View.GONE);
			tytul_tv.setVisibility(View.VISIBLE);
			if (tytul.length() == 0)
				tytul = root.getResources().getString(
						R.string.dotknij_aby_edytowac);
			tytul_tv.setText(tytul);
			save_image.setVisibility(View.GONE);
		}
	}
	@Override
	public String toString(){
		return ""+tytul;
	}
}
