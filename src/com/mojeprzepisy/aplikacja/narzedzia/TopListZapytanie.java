package com.mojeprzepisy.aplikacja.narzedzia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ListView;

import com.mojeprzepisy.aplikacja.Przepis;

public class TopListZapytanie {

	JSONParser jParser = new JSONParser();
	public JSONArray dane = null;
	private ListView lv;
	private List<Przepis> wszystkiePrzepisy;
	private MyListAdapter adapter;
	private Activity a;

	public TopListZapytanie(Activity _a, ListView _lv) {
		this.lv = _lv;
		this.a = _a;
		wszystkiePrzepisy = new LinkedList<Przepis>();
		adapter = new MyListAdapter(a, wszystkiePrzepisy);
		lv.setAdapter(adapter);
	}

	public void wykonaj(String... params) {
		try {
			new NajwyzejOcenianeZapytania().execute(params);
		} catch (Exception e) {
			wykonaj(params);
		}
	}

	/**
	 * Before starting background thread Show Progress Dialog
	 * */
	class NajwyzejOcenianeZapytania extends AsyncTask<String, Przepis, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		/**
		 * getting All products from url
		 * */
		@Override
		protected String doInBackground(String... args) {
			// Building Parameters
			String URL = args[0];
			String minLimit = args[1];
			String ilePrzepisow = args[2];

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("minLimit", minLimit));
			params.add(new BasicNameValuePair("ilePrzepisow", ilePrzepisow));
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(URL, "POST", params);

				// Check your log cat for JSON reponse

				// Checking for SUCCESS TAG
				int success = json.getInt("success");

				if (success == 1) {
					// products found
					// Getting Array of Products
					dane = json.getJSONArray("dane");

					// looping through All Products
					for (int i = 0; i < dane.length(); i++) {
						JSONObject c = dane.getJSONObject(i);

						// Storing each json item in variable
						String autorID = c.getString("autorID");
						String przepisID = c.getString("przepisID");
						String tytul = c.getString("tytul");
						String kategoria = c.getString("kategoria");
						String StrZdjecie = c.getString("zdjecie");
						String ocena = c.getString("ocena");
						String ilosc_ocen = c.getString("ilosc_ocen");
						String trudnosc = c.getString("trudnosc");
						String czas = c.getString("czas");
						String skladniki = c.getString("skladniki");
						String opis = c.getString("opis");
						
						publishProgress(new Przepis(autorID, przepisID,
								tytul, kategoria, ocena, ilosc_ocen, trudnosc,
								czas, StrZdjecie, skladniki, opis));
					}
				} else {
					// no products found
					// Launch Add New product Activity
					/*
					 * Intent i = new Intent(getApplicationContext(),
					 * NewProductActivity.class); // Closing all previous
					 * activities i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					 * startActivity(i);
					 */
				}
			} catch (Exception e) {

			}
			return null;
		}
		@Override
		protected void onProgressUpdate(Przepis...przepisy){
			wszystkiePrzepisy.add(przepisy[0]);
			adapter.notifyDataSetChanged();
		}

		@Override
		protected void onCancelled() {
			adapter.notifyDataSetChanged();
			super.onCancelled();
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String file_url) {
			adapter.notifyDataSetChanged();
		}
	}
}
