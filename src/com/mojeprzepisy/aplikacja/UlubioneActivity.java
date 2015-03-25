package com.mojeprzepisy.aplikacja;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.widget.ListView;

import com.mojeprzepisy.aplikacja.narzedzia.JSONParser;
import com.mojeprzepisy.aplikacja.narzedzia.MyApp;
import com.mojeprzepisy.aplikacja.narzedzia.MyListAdapter;
import com.mojeprzepisy.aplikacja.narzedzia.MyOnItemClickListener;

public class UlubioneActivity extends ListActivity {

	private ListView lv;
	private JSONParser jParser = new JSONParser();
	private JSONArray dane = null;
	private SharedPreferences ulubione;
	private List<Przepis> znalezionePrzepisy;
	private String url_pobierzUlubione, url_pobierz_przepis;
	private String ulubioneID[];
	private MyListAdapter adapter;
	private MyApp mApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		mApp = (MyApp) getApplicationContext();
		mApp.ua = this;
		loadActivity();
	}

	@Override
	protected void onDestroy() {
		mApp.ua = null;
		super.onDestroy();
	}

	public void loadActivity() {
		url_pobierzUlubione = getResources().getString(
				R.string.url_pobierzUlubione);
		url_pobierz_przepis = getResources().getString(
				R.string.url_pobierz_przepis);
		znalezionePrzepisy = new ArrayList<Przepis>();
		setContentView(R.layout.activity_ulubione);
		ulubione = getSharedPreferences("Ulubione", 0);
		adapter = new MyListAdapter(UlubioneActivity.this, znalezionePrzepisy);
		lv = getListView();
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new MyOnItemClickListener(getApplicationContext()));
		if (mApp.getData() != -1) {
			new UlubionePrzepisy().execute();
		} else {
			getUlubId();
			new UlubionePrzepisyOffline().execute();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ulubione, menu);
		return true;
	}

	public void getUlubId() {
		Map<String, ?> ulub = ulubione.getAll();
		ulubioneID = new String[ulub.size()];
		int i = 0;
		for (String s : ulub.keySet()) {
			ulubioneID[i] = s;
			i++;
		}
	}

	class UlubionePrzepisy extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setProgressBarIndeterminateVisibility(true);
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("userID", "" + mApp.getData()));

			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(url_pobierzUlubione,
						"POST", params);
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
						// creating new HashMap
						HashMap<String, String> map = new HashMap<String, String>();
						// adding each child node to HashMap key => value
						map.put("autorID", autorID);
						map.put("przepisID", przepisID);
						map.put("tytul", tytul);
						map.put("kategoria", kategoria);
						map.put("zdjecie", StrZdjecie);
						map.put("ocena", ocena);
						Przepis przepis = new Przepis(autorID, przepisID,
								tytul, kategoria, ocena, null, null, null,
								StrZdjecie, null, null);
						// adding HashList to ArrayList
						znalezionePrzepisy.add(przepis);
						// adapter.notifyDataSetChanged();
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

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			setProgressBarIndeterminateVisibility(false);
			lv.invalidateViews();
		}
	}

	class UlubionePrzepisyOffline extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setProgressBarIndeterminateVisibility(true);
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			for (int a = 0; a < ulubioneID.length; a++) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("przepisID", ulubioneID[a]));
				// getting JSON string from URL
				try {
					JSONObject json = jParser.makeHttpRequest(
							url_pobierz_przepis, "POST", params);

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
							// creating new HashMap
							HashMap<String, String> map = new HashMap<String, String>();
							// adding each child node to HashMap key => value
							map.put("autorID", autorID);
							map.put("przepisID", przepisID);
							map.put("tytul", tytul);
							map.put("kategoria", kategoria);
							map.put("zdjecie", StrZdjecie);
							map.put("ocena", ocena);
							Przepis przepis = new Przepis(autorID, przepisID,
									tytul, kategoria, ocena, null, null, null,
									StrZdjecie, null, null);
							// adding HashList to ArrayList
							znalezionePrzepisy.add(przepis);
						}
					} else {
					}
				} catch (Exception e) {
				}
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			setProgressBarIndeterminateVisibility(false);
			lv.invalidateViews();
		}
	}

}
