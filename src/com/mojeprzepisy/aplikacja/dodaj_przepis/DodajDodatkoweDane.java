package com.mojeprzepisy.aplikacja.dodaj_przepis;

import com.mojeprzepisy.aplikacja.R;

import android.app.Activity;
import android.widget.Spinner;
import android.widget.Switch;

public class DodajDodatkoweDane extends DodajPrzepisActivity {

	public Switch widoczny;
	public Spinner kategoria;
	public Spinner trudnosc;
	public Spinner czas;

	public DodajDodatkoweDane(Activity dodajPrzepisActivity) {
		root = dodajPrzepisActivity;
		widoczny = (Switch) root.findViewById(R.id.dodatkowe_dane_widoczny);
		kategoria = (Spinner) root.findViewById(R.id.dodatkowe_dane_kategoria);
		trudnosc = (Spinner) root.findViewById(R.id.dodatkowe_dane_trudnosc);
		czas = (Spinner) root.findViewById(R.id.dodatkowe_dane_czas);
	}

	public DodajDodatkoweDane(Activity dodajPrzepisActivity, String _trudnosc,
			String _kategoria, String _czas, boolean publiczny) {

		root = dodajPrzepisActivity;
		widoczny = (Switch) root.findViewById(R.id.dodatkowe_dane_widoczny);
		kategoria = (Spinner) root.findViewById(R.id.dodatkowe_dane_kategoria);
		trudnosc = (Spinner) root.findViewById(R.id.dodatkowe_dane_trudnosc);
		czas = (Spinner) root.findViewById(R.id.dodatkowe_dane_czas);
			widoczny.setChecked(publiczny);
		for (int i = 0; i < kategoria.getCount(); i++) {
			if (kategoria.getItemAtPosition(i).toString().equals(_kategoria)) {
				kategoria.setSelection(i);
				break;
			}
		}
		for (int i = 0; i < trudnosc.getCount(); i++) {
			if (trudnosc.getItemAtPosition(i).toString().equals(_trudnosc)) {
				trudnosc.setSelection(i);
				break;
			}
		}
		for (int i = 0; i < czas.getCount(); i++) {
			if (czas.getItemAtPosition(i).toString().equals(_czas)) {
				czas.setSelection(i);
				break;
			}
		}
	}

}
