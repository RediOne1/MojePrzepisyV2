package com.mojeprzepisy.aplikacja;

import java.io.Serializable;
import java.util.List;

import android.graphics.drawable.Drawable;

import com.mojeprzepisy.aplikacja.dodaj_przepis.Krok;
import com.mojeprzepisy.aplikacja.dodaj_przepis.Skladnik;

public class Przepis implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8822754243233192174L;
	public int autorID;
	public int przepisID;
	public String tytul=null;
	public String kategoria;
	public float ocena;
	public int ilosc_ocen;
	public String trudnosc;
	public String czas;
	public String zdjecie;
	public String opis=null;
	public String skladniki=null;
	public boolean publiczny = true;
	public List<Skladnik> skladnikiList;
	public List<Krok> opisList;

	public Przepis() {

	}

	/**
	 * Konstruktor obiektu przepis dodajacy dane o przepisie do obiektu
	 * 
	 * @param _autorID
	 *            nr ID autora przepisu
	 * @param _przepisID
	 *            nr ID przepisu
	 * @param _tytul
	 *            Tytul przepisu
	 * @param _kategoria
	 *            kategoria
	 * @param _ocena
	 *            zostanie automatycznie zamieniona i zapisana jako float
	 * @param _ilosc_ocen
	 *            zostanie automatycznie zamieniona na int
	 * @param _trudnosc
	 * @param _ile_osob
	 * @param _zdjecie
	 *            zostanie zapisane jako adres URL w Stringu
	 */
	public Przepis(String _autorID, String _przepisID, String _tytul,
			String _kategoria, String _ocena, String _ilosc_ocen,
			String _trudnosc, String _czas, String _zdjecie, String _skladniki,
			String _opis) {
		this.autorID = String2Int(_autorID);
		this.przepisID = String2Int(_przepisID);
		this.tytul = _tytul;
		this.kategoria = _kategoria;
		this.ocena = String2Float(_ocena);
		this.ilosc_ocen = String2Int(_ilosc_ocen);
		this.trudnosc = _trudnosc;
		this.czas = _czas;
		this.zdjecie = _zdjecie;
		this.opis = _opis;
		this.skladniki = _opis;
	}

	private int String2Int(String string) {
		int Int;
		try {
			Int = Integer.parseInt(string);
		} catch (Exception e) {
			Int = 0;
		}
		return Int;
	}

	private float String2Float(String string) {
		float _Float;
		try {
			_Float = Float.parseFloat(string);
		} catch (Exception e) {
			_Float = 0;
		}
		return _Float;
	}
}
