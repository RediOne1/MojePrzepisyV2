package com.mojeprzepisy.aplikacja.narzedzia;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.mojeprzepisy.aplikacja.R;
import com.mojeprzepisy.aplikacja.dodaj_przepis.DodajPrzepisActivity;
import com.mojeprzepisy.aplikacja.wyswietl_przepis.WyswietlPrzepis;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class WyslijZdjecie {

	Bitmap bm;
	String name, stary;
	private JSONParser jParser = new JSONParser();
	private String url_zmien_tytul, url_usun_zdjecie, url_wyslij_zdjecie;
	private ExecuteMultipartPost executeMultipartPost;

	public WyslijZdjecie(Activity root, Bitmap bm, String name) {
		this.bm = bm;
		this.name = name;
		url_zmien_tytul = root.getResources().getString(
				R.string.url_zmien_tytul);
		url_usun_zdjecie = root.getResources().getString(
				R.string.url_usun_zdjecie);
		url_wyslij_zdjecie = root.getResources().getString(
				R.string.url_wyslij_zdjecie);
		executeMultipartPost = new ExecuteMultipartPost();
	}

	public WyslijZdjecie(Activity root) {
		url_zmien_tytul = root.getResources().getString(
				R.string.url_zmien_tytul);
		url_usun_zdjecie = root.getResources().getString(
				R.string.url_usun_zdjecie);
		url_wyslij_zdjecie = root.getResources().getString(
				R.string.url_wyslij_zdjecie);
		executeMultipartPost = new ExecuteMultipartPost();
	}

	public String execute() {
		String result = "";
		try {
			result = executeMultipartPost.execute().get();
		} catch (Exception e) {
			Log.e("Wyslij zdjecie", "" + e);
		}
		Log.d("DEBUG_TAG",result);
		return result;
	}

	class ExecuteMultipartPost extends AsyncTask<String, Integer, String> {

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
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bm.compress(CompressFormat.JPEG, 100, bos);
				byte[] data = bos.toByteArray();
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost(url_wyslij_zdjecie);
				ByteArrayBody bab = new ByteArrayBody(data, name);
				MultipartEntity reqEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				reqEntity.addPart("file", bab);
				// reqEntity.addPart("podpis", new StringBody("sfsdfsdf"));
				postRequest.setEntity(reqEntity);
				HttpResponse response = httpClient.execute(postRequest);
			} catch (Exception e) {
				Log.e("Wyslij zdjecie", "" + e);
				Log.e("Wyslij zdjecie", "" + e.getMessage());
			}
			return "wysłano zdjecie";
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
		}
	}

	public void usunZdjecie(String tytul) {
		stary = tytul;
		new usunZdjecieClass().execute();
	}

	class usunZdjecieClass extends AsyncTask<String, String, String> {

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
			params.add(new BasicNameValuePair("stary", stary));

			try {
				JSONObject json = jParser.makeHttpRequest(url_usun_zdjecie,
						"POST", params);

			} catch (Exception e) {
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {

		}
	}
}
