package com.mojeprzepisy.aplikacja.narzedzia;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

import com.mojeprzepisy.aplikacja.Przepis;
import com.mojeprzepisy.aplikacja.R;

public class UsunPrzepis {
	private Activity root;
	private Przepis przepis;
	private ProgressDialog pDialog;
	private String url_usun_przepis;
	private static final int USUN_DIALOG_ID = 5;
	private JSONParser jParser = new JSONParser();
	private UsunPrzepisThread usunPrzepis = new UsunPrzepisThread();

	public UsunPrzepis(Activity root, Przepis przepis) {
		this.przepis = przepis;
		this.root = root;
		url_usun_przepis = root.getResources().getString(
				R.string.url_usun_przepis);

	}

	@SuppressWarnings("deprecation")
	public void usun() {
		usunPrzepis.execute();
	}

	class UsunPrzepisThread extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(root);
			pDialog.setMessage("Usuwanie przepisu...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					usunPrzepis.cancel(true);
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
			params.add(new BasicNameValuePair("przepisID", ""
					+ przepis.przepisID));
			// getting JSON string from URL
			try {
				WyslijZdjecie wyslij = new WyslijZdjecie(root);
				jParser.makeHttpRequest(url_usun_przepis, "POST", params);
				wyslij.usunZdjecie("" + przepis.przepisID);
			} catch (Exception e) {
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			MyApp app = (MyApp) root.getApplicationContext();
			app.reloadActivity();
			pDialog.dismiss();
			root.finish();
		}
	}
}
