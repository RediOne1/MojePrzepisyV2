package com.mojeprzepisy.aplikacja;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mojeprzepisy.aplikacja.narzedzia.JSONParser;

public class DrawerKategorie {

	public ListView lv;
	public Activity root;
	public String kategorie[];
	private JSONParser jParser = new JSONParser();
	private JSONArray dane;
	private String url_ile_przepisow, url_ile_przepisow_user;
	public ArrayAdapter<String> adapter;

	public DrawerKategorie(Activity _root, ListView _lv) {
		this.lv = _lv;
		this.root = _root;
		url_ile_przepisow = root.getResources().getString(
				R.string.url_ile_przepisow);
		kategorie = root.getResources().getStringArray(R.array.kategorie);
		adapter = new ArrayAdapter<String>(root, R.layout.drawer_list_item,
				kategorie);
		lv.setAdapter(adapter);
		new WyswietlKategorie().execute();
	}

	class WyswietlKategorie extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				JSONObject json = jParser.makeHttpRequest(url_ile_przepisow,
						"POST", params);
				int success = json.getInt("success");
				if (success == 1) {
					// products found
					// Getting Array of Products
					dane = json.getJSONArray("dane");
					Map<String, String> map = new HashMap<String, String>();
					// looping through All Products

					for (int i = 0; i < dane.length(); i++) {
						JSONObject c = dane.getJSONObject(i);
						String kategoria = c.getString("kategoria");
						String ile = c.getString("ile");
						map.put(kategoria, ile);
					}
					for (int i = 0; i < kategorie.length; i++) {
						if (map.containsKey(kategorie[i]))
							kategorie[i] += " (" + map.get(kategorie[i]) + ")";
						else
							kategorie[i] += " (0)";
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
				Log.e("DEBUG_TAG", "" + e);
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			lv.invalidateViews();
		}
	}
}
