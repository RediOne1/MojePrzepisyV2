package com.mojeprzepisy.aplikacja.wyswietl_przepis;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.mojeprzepisy.aplikacja.Przepis;
import com.mojeprzepisy.aplikacja.R;
import com.mojeprzepisy.aplikacja.dodaj_przepis.Krok;
import com.mojeprzepisy.aplikacja.narzedzia.JSONParser;

public class WyswietlOpis extends WyswietlPrzepis {
	private LinearLayout linearLayout;
	private Przepis przepis;
	private Activity root;
	private ProgressBar progress;
	private List<Krok> kroki;
	private String url_pobierz_opis;
	JSONParser jParser = new JSONParser();
	public JSONArray dane = null;
	public String opis;
	private Krok krok;
	String kroki_tab[];

	public WyswietlOpis(Activity _root, Przepis _przepis) {
		this.root = _root;
		linearLayout = (LinearLayout) root
				.findViewById(R.id.wyswietl_opis_linearLayout);
		progress = (ProgressBar) root
				.findViewById(R.id.wyswietl_opis_progressbar);
		url_pobierz_opis = root.getResources().getString(
				R.string.url_pobierz_opis);
		this.przepis = _przepis;
		kroki = new ArrayList<Krok>();
		new PobierzOpis().execute();
	}

	public String toShare() {
		String wynik = "";
		for (Krok k : kroki)
			wynik += k.toShare();
		return wynik;
	}

	public String getOpis() {
		return opis;
	}

	class PobierzOpis extends AsyncTask<String, Krok, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		/**
		 * getting All products from url
		 * */
		@Override
		protected String doInBackground(String... args) {

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("przepisID", ""
					+ przepis.przepisID));
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(url_pobierz_opis,
						"POST", params);
				// Check your log cat for JSON reponse

				// Checking for SUCCESS TAG
				int success = json.getInt("success");

				if (success == 1) {
					dane = json.getJSONArray("dane");
					JSONObject c = dane.getJSONObject(0);
					opis = c.getString("opis");
					kroki_tab = opis.split(";krok;");
					root.runOnUiThread(new Runnable() {
						public void run() {
							for (int i = 0; i < kroki_tab.length; i++) {
								if (kroki_tab[i].length() == 0)
									continue;
								String temp2[] = kroki_tab[i].split(";opis;");
								if (temp2.length == 1)
									publishProgress(new Krok("", temp2[0]));
								else if(temp2[0].equals("null"))
									publishProgress(new Krok("", temp2[1]));
								else
									publishProgress(new Krok(temp2[0], temp2[1]));
							}
						}

					});
					przepis.opis = opis;
				} else {
				}
			} catch (Exception e) {
				Log.e("Wyswietl opis", "" + e);
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String file_url) {
			progress.setVisibility(View.GONE);
		}

		@Override
		protected void onProgressUpdate(Krok... progress) {
			krok = progress[0];
			kroki.add(krok);
			root.runOnUiThread(new Runnable() {
				public void run() {
					linearLayout.addView(krok.wyswietl(root, linearLayout));
					root.invalidateOptionsMenu();
				}
			});
		}
	}
}