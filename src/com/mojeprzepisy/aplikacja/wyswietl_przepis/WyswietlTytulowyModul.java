package com.mojeprzepisy.aplikacja.wyswietl_przepis;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mojeprzepisy.aplikacja.Przepis;
import com.mojeprzepisy.aplikacja.R;
import com.mojeprzepisy.aplikacja.narzedzia.ImageLoader;
import com.mojeprzepisy.aplikacja.narzedzia.JSONParser;
import com.mojeprzepisy.aplikacja.narzedzia.MyTypeFace;

public class WyswietlTytulowyModul extends WyswietlPrzepis {

	JSONParser jParser = new JSONParser();
	public JSONArray dane = null;

	private final String url_wyswietl_tytulowy_modul;
	private Przepis przepis;
	private ImageView zdjecie;
	private TextView tytul, kategoria, ilosc_ocen, trudnosc, czas;
	private Activity root;
	private ProgressBar progress;
	private RatingBar ocena;

	public WyswietlTytulowyModul(Activity _root, Przepis _przepis) {
		this.przepis = _przepis;
		this.root = _root;
		zdjecie = (ImageView) root.findViewById(R.id.zdjecie_maly_layout);
		tytul = (TextView) root.findViewById(R.id.przepis_tytul_maly_layout);
		kategoria = (TextView) root.findViewById(R.id.kategoria_maly_layout);
		ocena = (RatingBar) root.findViewById(R.id.ratingbar_maly_layout);
		ilosc_ocen = (TextView) root.findViewById(R.id.ilosc_ocen_maly_layout);
		trudnosc = (TextView) root.findViewById(R.id.trudnosc_maly_layout);
		czas = (TextView) root.findViewById(R.id.czas_maly_layout);
		progress = (ProgressBar) root
				.findViewById(R.id.przepis_tytulowy_modul_progressbar);
		url_wyswietl_tytulowy_modul = root.getResources().getString(
				R.string.url_wyswietl_tytulowy_modul);
		tytul = (TextView) new MyTypeFace(tytul, root).MyBold();
		odswiez();
	}

	public void odswiez() {
		new OdswiezDane().execute();
	}

	public String getImage() {
		return przepis.zdjecie;
	}

	public String getTytul() {
		return tytul.getText().toString();
	}

	public String getKategoria() {
		return kategoria.getText().toString();
	}

	public String getTrudnosc() {
		return trudnosc.getText().toString();
	}

	public String getCzas() {
		return czas.getText().toString();
	}

	class OdswiezDane extends AsyncTask<Float, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(Float... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("przepisID", ""
					+ przepis.przepisID));
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(
						url_wyswietl_tytulowy_modul, "POST", params);

				// Check your log cat for JSON reponse

				// Checking for SUCCESS TAG
				int success = json.getInt("success");

				if (success == 1) {
					dane = json.getJSONArray("dane");

					// looping through All Products
					for (int i = 0; i < dane.length(); i++) {
						JSONObject c = dane.getJSONObject(i);
						String ocena = c.getString("ocena");
						if (ocena.equalsIgnoreCase("NULL"))
							ocena = "0";
						przepis.ocena = Float.parseFloat(ocena);
						String ilosc_ocen = c.getString("ilosc_ocen");
						if (ilosc_ocen.equalsIgnoreCase("NULL"))
							ilosc_ocen = "0";
						przepis.ilosc_ocen = Integer.parseInt(ilosc_ocen);
						przepis.publiczny = c.getInt("publiczny") == 1 ? true
								: false;
						przepis.trudnosc = c.getString("trudnosc");
						przepis.czas = c.getString("czas");
						przepis.kategoria = c.getString("kategoria");
						przepis.zdjecie = c.getString("zdjecie");
						przepis.tytul = c.getString("tytul");
					}
				} else {
				}
			} catch (Exception e) {
				Log.e("DEBUG_TAG", "Wyswietl tytulowy modul: " + e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			tytul.setText(przepis.tytul);
			kategoria.setText(przepis.kategoria);
			ocena.setRating(przepis.ocena);
			ilosc_ocen.setText("" + przepis.ilosc_ocen);
			trudnosc.setText(przepis.trudnosc);
			czas.setText(przepis.czas);
			new ImageLoader(root).remove(przepis.zdjecie);
			new ImageLoader(root).DisplayImage(przepis.zdjecie, zdjecie);
			root.invalidateOptionsMenu();
			progress.setVisibility(View.GONE);

		}
	}
}
