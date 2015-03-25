package com.mojeprzepisy.aplikacja;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.mojeprzepisy.aplikacja.narzedzia.JSONParser;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class ZapytanieNaSerwer {
	JSONParser jsonParser = new JSONParser();
	JSONObject json;
	JSONArray dane = null;
	String adres_url;
	ProgressDialog pDialog;
	private String tekst_ladowania;
	private Context context;
	List<NameValuePair> params;
	JSONArray products = null;

	ZapytanieNaSerwer(String _adres_url, String _tekst_ladowania,
			Context _context) {
		params = new ArrayList<NameValuePair>();
		adres_url = _adres_url;
		tekst_ladowania = _tekst_ladowania;
		context = _context;
	}

	public void dodajParametr(String tag, String wartosc) {
		params.add(new BasicNameValuePair(tag, wartosc));
	}

	public void wyslij() {
		new wyslijZapytanie().execute();
	}

	public JSONObject getWynik() {
		return json;
	}

	class wyslijZapytanie extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setMessage(tekst_ladowania);
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();

		}

		@Override
		protected String doInBackground(String... args) {
			json = jsonParser.makeHttpRequest(adres_url, "POST", params);
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
		}
	}
}
