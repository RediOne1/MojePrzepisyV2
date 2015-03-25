package com.mojeprzepisy.aplikacja.wyswietl_przepis;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mojeprzepisy.aplikacja.R;
import com.mojeprzepisy.aplikacja.narzedzia.JSONParser;
import com.mojeprzepisy.aplikacja.narzedzia.MyApp;

public class UlubioneDodajUsun {

	private Activity root;
	private MyApp app;
	private ProgressDialog pDialog;
	private JSONParser jParser = new JSONParser();
	private SharedPreferences ulubioneOffline;
	private boolean ulubiony = false;
	private String url_ulubione, url_czyUlubiony;
	private DodajUsunUlubione dodajUsunUlub;
	private CzyUlubiony czyUlub;

	public UlubioneDodajUsun(Activity root) {
		this.root = root;
		app = (MyApp) root.getApplicationContext();
		ulubioneOffline = root.getSharedPreferences("Ulubione", 0);
		url_ulubione = root.getResources().getString(R.string.url_ulubione);
		url_czyUlubiony = root.getResources().getString(
				R.string.url_czyUlubiony);
	}

	public void sprawdz(String przepisID) {
		if (app.getData() == -1) {
			ulubiony = ulubioneOffline.contains(przepisID);
			root.invalidateOptionsMenu();
		} else {
			czyUlub = new CzyUlubiony();
			czyUlub.execute(przepisID);
		}

	}

	public void dodaj(String przepisID) {
		if (app.getData() == -1) {
			SharedPreferences.Editor edytorPref = ulubioneOffline.edit();
			edytorPref.putBoolean(przepisID, true);
			edytorPref.commit();
			ulubiony = true;
			root.invalidateOptionsMenu();
			Toast.makeText(root, "Dodano do ulubionych", Toast.LENGTH_SHORT)
					.show();
			app.reloadUlubione();
		} else {
			dodajUsunUlub = new DodajUsunUlubione();
			dodajUsunUlub.execute(przepisID);
		}
	}

	public void usun(String przepisID) {
		if (app.getData() == -1) {
			SharedPreferences.Editor edytorPref = ulubioneOffline.edit();
			edytorPref.remove(przepisID);
			edytorPref.commit();
			ulubiony = false;
			root.invalidateOptionsMenu();
			Toast.makeText(root, "Usunięto ulubionych", Toast.LENGTH_SHORT)
					.show();
			app.reloadUlubione();
		} else {
			dodajUsunUlub = new DodajUsunUlubione();
			dodajUsunUlub.execute(przepisID);
		}
	}

	class DodajUsunUlubione extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(root);
			if (!ulubiony)
				pDialog.setMessage("Trwa dodawanie do ulubionych...");
			else
				pDialog.setMessage("Trwa usuwanie z ulubionych...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					dodajUsunUlub.cancel(true);
				}
			});
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("przepisID", "" + args[0]));
			params.add(new BasicNameValuePair("autorID", "" + app.getData()));
			params.add(new BasicNameValuePair("lubie", "" + !ulubiony));
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(url_ulubione, "POST",
						params);

				// Check your log cat for JSON reponse

				// Checking for SUCCESS TAG
				int success = json.getInt("success");
				if (success == 1) {
				} else {
				}
			} catch (Exception e) {
				Log.d("DEBUG_TAG", "" + e);
				// dialog = true;
				// komunikat = "B��d w po��czeniu.";
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			ulubiony = !ulubiony;
			app.reloadUlubione();
			pDialog.dismiss();
			if (ulubiony)
				Toast.makeText(root, "Dodano do ulubionych", Toast.LENGTH_SHORT)
						.show();
			else
				Toast.makeText(root, "Usunięto z ulubionych.",
						Toast.LENGTH_SHORT).show();
			root.invalidateOptionsMenu();
		}
	}

	class CzyUlubiony extends AsyncTask<String, String, String> {

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
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("przepisID", args[0]));
			params.add(new BasicNameValuePair("autorID", "" + app.getData()));
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(url_czyUlubiony,
						"POST", params);

				// Check your log cat for JSON reponse

				// Checking for SUCCESS TAG
				int success = json.getInt("success");
				if (success == 1) {
					ulubiony = true;
				} else {
					ulubiony = false;
				}
				Log.d("DEBUG_TAG",json.getString("success"));
			} catch (Exception e) {
				Log.d("DEBUG_TAG", "" + e);
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			Log.d("DEBUG_TAG", "Ulubiony: " + ulubiony);
			root.invalidateOptionsMenu();
		}
	}

	public boolean getUlub() {
		return ulubiony;
	}
}
