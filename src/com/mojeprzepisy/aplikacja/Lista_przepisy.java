package com.mojeprzepisy.aplikacja;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;

import com.mojeprzepisy.aplikacja.narzedzia.JSONParser;
import com.mojeprzepisy.aplikacja.narzedzia.MyListAdapter;
import com.mojeprzepisy.aplikacja.narzedzia.MyOnItemClickListener;
import com.mojeprzepisy.aplikacja.narzedzia.Szukaj;

public class Lista_przepisy extends ListActivity {

	private String pseudonim;
	private int user_id;
	private JSONParser jParser = new JSONParser();
	private JSONArray dane = null;
	private ArrayList<Przepis> wszystkiePrzepisy;
	private ImageView image;
	private String kategoria;
	private final static int ALERT_DIALOG_ID = 2;
	private String url_wszystkie_przepisy;
	private String url_user_przepisy;
	private String komunikat;
	private boolean dialog = false;
	private MyListAdapter adapter;
	private ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_przepisy);
		url_wszystkie_przepisy = getString(R.string.url_wszystkie_przepisy);
		url_user_przepisy = getString(R.string.url_user_przepisy);
		Bundle bundle = getIntent().getExtras();
		kategoria = bundle.getString("kategoria");
		pseudonim = bundle.getString("pseudonim", null);
		user_id = bundle.getInt("user_id", 0);
		wszystkiePrzepisy = new ArrayList<Przepis>();
		adapter = new MyListAdapter(Lista_przepisy.this, wszystkiePrzepisy);
		lv = getListView();
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new MyOnItemClickListener(Lista_przepisy.this));
		new WyswietlWszystkiePrzepisy().execute();
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {

		case ALERT_DIALOG_ID:
			AlertDialog alertDialog = new AlertDialog.Builder(this)
					.setMessage(komunikat)
					.setNeutralButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
								}
							}).create();
			return alertDialog;
		}
		return null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lista_przepisy, menu);

		menu.add("Szukaj").setIcon(android.R.drawable.ic_menu_search)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.toString() == "Szukaj") {
			Intent i = new Intent(getApplicationContext(), Szukaj.class);
			i.putExtra("pseudonim", pseudonim);
			i.putExtra("user_id", user_id);
			startActivity(i);
		}

		return super.onOptionsItemSelected(item);
	}

	class WyswietlWszystkiePrzepisy extends AsyncTask<String, String, String> {

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
			dialog = false;
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("kategoria", kategoria));
			// getting JSON string from URL
			try {
				JSONObject json;
				if (kategoria.equals("user")) {
					params.add(new BasicNameValuePair("autorID", "" + user_id));
					json = jParser.makeHttpRequest(url_user_przepisy, "POST",
							params);
				} else
					json = jParser.makeHttpRequest(url_wszystkie_przepisy,
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

						wszystkiePrzepisy.add(new Przepis(autorID, przepisID,
								tytul, kategoria, ocena, null, null, null,
								StrZdjecie, null, null));
						publishProgress();
					}
				} else {
				}
			} catch (Exception e) {
				Log.e("DEBUG_TAG", "" + e);
				dialog = true;
				komunikat = "Błąd w połączeniu." + e;
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			setProgressBarIndeterminateVisibility(false);
		}

		@Override
		protected void onProgressUpdate(String... progress) {
			try {
				lv.invalidateViews();
			} catch (Exception e) {
			}
		}
	}
}
