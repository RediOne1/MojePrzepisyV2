package com.mojeprzepisy.aplikacja.narzedzia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mojeprzepisy.aplikacja.Przepis;
import com.mojeprzepisy.aplikacja.R;

public class Szukaj extends ListActivity {

	private JSONParser jParser = new JSONParser();
	private JSONArray dane = null;
	private String pseudonim;
	private int user_id;
	private final static int ALERT_DIALOG_ID = 2;
	private List<Przepis> znalezionePrzepisy;
	private String url_szukaj_przepis;
	private EditText szukaj;
	private Button szukajButton;
	private MyListAdapter adapter;
	private String komunikat;
	private String tag;
	private ListView lv;
	boolean dialog = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_szukaj);
		lv = getListView();
		lv.setOnItemClickListener(new MyOnItemClickListener(Szukaj.this));
		znalezionePrzepisy = new ArrayList<Przepis>();
		adapter = new MyListAdapter(Szukaj.this, znalezionePrzepisy);
		lv.setAdapter(adapter);
		url_szukaj_przepis = getString(R.string.url_szukaj_przepis);
		setProgressBarIndeterminateVisibility(false);
		Bundle bundle = getIntent().getExtras();
		user_id = bundle.getInt("user_id");
		szukaj = (EditText) findViewById(R.id.wyszukiwarka);
		szukajButton = (Button) findViewById(R.id.szukaj_button);
		szukajButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				tag = szukaj.getText().toString();
				new WyszukajPrzepis().execute();
			}
		});


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.szukaj, menu);
		return true;
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

	class WyszukajPrzepis extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setProgressBarIndeterminateVisibility(true);
			znalezionePrzepisy.clear();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			dialog = false;
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("klucz", tag));
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(url_szukaj_przepis,
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
								tytul, kategoria, ocena, null, null,
								null, StrZdjecie, null, null);
						// adding HashList to ArrayList
						znalezionePrzepisy.add(przepis);
						//adapter.notifyDataSetChanged();
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
				dialog = true;
				komunikat = "B��d w po��czeniu.";
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			adapter.notifyDataSetChanged();
			setProgressBarIndeterminateVisibility(false);
		}
	}
}
