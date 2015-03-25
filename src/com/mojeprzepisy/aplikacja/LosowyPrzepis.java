package com.mojeprzepisy.aplikacja;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.mojeprzepisy.aplikacja.narzedzia.JSONParser;
import com.mojeprzepisy.aplikacja.wyswietl_przepis.WyswietlPrzepis;
import com.mojeprzepisy.aplikacja.wyswietl_przepis.WyswietlTytulowyModul;

@SuppressLint("ValidFragment")
public class LosowyPrzepis implements OnClickListener{

	JSONParser jParser = new JSONParser();
	private Handler mHandler = new Handler();
	public JSONArray dane = null;
	private Przepis przepis = null;
	private Activity root;
	private View include;
	private String url_losowy_przepis;

	public LosowyPrzepis(Activity _root) {
		this.root = _root;
		url_losowy_przepis = root.getResources().getString(R.string.url_losowy_przepis);
		include = (View) root.findViewById(R.id.include6);
		include.setOnClickListener(this);
		new Thread(new Runnable() {
			public void run() {
				List<NameValuePair> params = new ArrayList<NameValuePair>();

				// getting JSON string from URL
				try {
					JSONObject json = jParser.makeHttpRequest(url_losowy_przepis, "POST",
							params);
					int success = json.getInt("success");

					if (success == 1) {
						dane = json.getJSONArray("dane");

						// looping through All Products
						for (int i = 0; i < dane.length(); i++) {
							JSONObject c = dane.getJSONObject(i);
							String przepisID = c.getString("przepisID");
							String autorID = c.getString("autorID");
							przepis = new Przepis(autorID, przepisID, null,	null, null, null, null, null, null, null,null);
							mHandler.post(new Runnable() {
								public void run() {
									new WyswietlTytulowyModul(root, przepis);
									include.setTag(przepis);
								}
							});
						}
					} else {
					}

				} catch (Exception e) {
					Log.e("DEBUG_TAG", "Losowy_przepis: " + e);
				}
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		if(v == include && przepis != null){
			// Starting new intent
			Intent i = new Intent(root, WyswietlPrzepis.class);
			i.putExtra("przepis", przepis);
			Log.d("MyOnItemClickListener", "Wybrano przepis "+przepis.tytul+", przepisID: "+przepis.przepisID);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			root.startActivity(i);	
		}		
	}
}
