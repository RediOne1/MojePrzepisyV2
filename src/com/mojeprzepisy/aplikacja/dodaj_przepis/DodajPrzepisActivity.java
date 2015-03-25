package com.mojeprzepisy.aplikacja.dodaj_przepis;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.mojeprzepisy.aplikacja.Przepis;
import com.mojeprzepisy.aplikacja.R;
import com.mojeprzepisy.aplikacja.narzedzia.AlertDialogManager;
import com.mojeprzepisy.aplikacja.narzedzia.ImageLoader;
import com.mojeprzepisy.aplikacja.narzedzia.JSONParser;
import com.mojeprzepisy.aplikacja.narzedzia.MyApp;
import com.mojeprzepisy.aplikacja.narzedzia.WyslijZdjecie;
import com.mojeprzepisy.aplikacja.wyswietl_przepis.WyswietlPrzepis;

public class DodajPrzepisActivity extends Activity implements OnClickListener {

	public DodajZdjecie dodajZdjecie;
	public int user_id;
	private String komunikat;
	public DodajTytul dodajTytul;
	public DodajSkladniki dodajSkladniki;
	public DodajOpis dodajOpis;
	private JSONParser jParser = new JSONParser();
	public DodajDodatkoweDane dodatkoweDane;
	public Button wyslij;
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int REQ_CODE_PICK_IMAGE = 101;
	public Activity root;
	private int success;
	private ProgressDialog pDialog;
	private String dodajPrzepisURL;
	public DodajNowyPrzepis dodajPrzepis;
	private Przepis przepis;
	private boolean edytuj = false;
	private MyApp app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dodaj_przepis_layout);
		app = (MyApp) getApplicationContext();
		user_id = app.getData();
		dodajPrzepisURL = getResources().getString(R.string.url_dodaj_przepis);
		root = DodajPrzepisActivity.this;
		dodajPrzepis = new DodajNowyPrzepis();
		przepis = new Przepis();
		// this.recreate();
		Bundle bundle = getIntent().getExtras();
		if (bundle.getBoolean("edytuj", false)) {
			przepis = (Przepis) getIntent().getSerializableExtra("przepis");
			edytuj = true;
			edytuj();
		} else
			dodaj();
		wyslij = (Button) findViewById(R.id.dodaj_przepis_wyslij_button);
		wyslij.setOnClickListener(this);
	}

	public void edytuj() {
		dodajZdjecie = new DodajZdjecie(root, przepis.zdjecie);
		dodajTytul = new DodajTytul(root, przepis.tytul);
		dodajSkladniki = new DodajSkladniki(root, przepis.skladniki);
		dodajOpis = new DodajOpis(root, przepis.opis);
		dodatkoweDane = new DodajDodatkoweDane(root, przepis.trudnosc,
				przepis.kategoria, przepis.czas, przepis.publiczny);
	}

	public void dodaj() {
		dodajZdjecie = new DodajZdjecie(root);
		dodajTytul = new DodajTytul(root);
		dodajSkladniki = new DodajSkladniki(root);
		dodajOpis = new DodajOpis(root);
		dodatkoweDane = new DodajDodatkoweDane(root);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dodaj_przepis, menu);
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		dodajZdjecie.dodajPrzepisActivityResult(requestCode, resultCode, data);
		try {
			new ImageLoader(root).remove(przepis.zdjecie);
		} catch (Exception e) {
			Log.e("DEBUG_TAG", "" + e);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == wyslij) {
			String message = "";
			if (dodajOpis.toString().equals(""))
				message = getString(R.string.dodaj_opis);
			if (dodajSkladniki.toString().equals(""))
				message = getString(R.string.dodaj_skladnik);
			if (dodajTytul.toString().equals(""))
				message = getString(R.string.dodaj_tytul);
			if (!message.equals(""))
				new AlertDialogManager().showAlertDialog(this, null, message);
			else
				dodajPrzepis.execute();
		}
	}

	class DodajNowyPrzepis extends AsyncTask<String, Integer, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DodajPrzepisActivity.this);
			if (edytuj)
				pDialog.setMessage(DodajPrzepisActivity.this
						.getString(R.string.edytowanie_przepisu));
			else
				pDialog.setMessage(DodajPrzepisActivity.this
						.getString(R.string.dodawanie_przepisu));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					dodajPrzepis.cancel(true);
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
			params.add(new BasicNameValuePair("edytuj", "" + edytuj));
			params.add(new BasicNameValuePair("autorID", "" + user_id));
			if (edytuj)
				params.add(new BasicNameValuePair("przepisID", ""
						+ przepis.przepisID));
			else
				params.add(new BasicNameValuePair("przepisID", "-1"));
			params.add(new BasicNameValuePair("tytul", dodajTytul.toString()));
			params.add(new BasicNameValuePair("kategoria",
					dodatkoweDane.kategoria.getSelectedItem().toString()));
			params.add(new BasicNameValuePair("trudnosc",
					dodatkoweDane.trudnosc.getSelectedItem().toString()));
			params.add(new BasicNameValuePair("czas", dodatkoweDane.czas
					.getSelectedItem().toString()));
			params.add(new BasicNameValuePair("skladniki", dodajSkladniki
					.toString()));
			params.add(new BasicNameValuePair("opis", dodajOpis.toString()));
			params.add(new BasicNameValuePair("publiczny", ""
					+ (dodatkoweDane.widoczny.isChecked() ? 1 : 0)));
			Log.d("DEBUG_TAG", params.toString());
			// getting JSON string from URL
			try {
				JSONObject json = jParser.makeHttpRequest(dodajPrzepisURL,
						"POST", params);
				// Check your log cat for JSON reponse

				// Checking for SUCCESS TAG
				success = json.getInt("success");

				if (success == 1) {
					przepis.przepisID = json.getInt("przepisID");

				} else {
				}
				komunikat = json.getString("message");
			} catch (Exception e) {
				komunikat = getString(R.string.brak_polaczenia);
				Log.e("DEBUG_TAG", "" + e);
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			if (success == 1) {
				Log.d("DEBUG_TAG", komunikat);
				if (dodajZdjecie.getBitmap() != null) {
					WyslijZdjecie wyslij = new WyslijZdjecie(root,
							dodajZdjecie.getBitmap(), "" + przepis.przepisID);
					// params.add(new BasicNameValuePair("zdjecie", "" +
					// przepis.przepisID));
					wyslij.execute();
				}
				Intent i = new Intent(root, WyswietlPrzepis.class);
				i.putExtra("przepis", przepis);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				MyApp app = (MyApp) root.getApplicationContext();
				app.reloadActivity();
				root.finish();
				root.startActivity(i);
			} else {
				Toast.makeText(root, komunikat, Toast.LENGTH_SHORT).show();
			}
			pDialog.dismiss();
		}
	}
}
