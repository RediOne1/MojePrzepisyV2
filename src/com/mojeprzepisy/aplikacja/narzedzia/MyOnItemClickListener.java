package com.mojeprzepisy.aplikacja.narzedzia;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.mojeprzepisy.aplikacja.Przepis;
import com.mojeprzepisy.aplikacja.wyswietl_przepis.WyswietlPrzepis;

public class MyOnItemClickListener implements OnItemClickListener {
	Context context;
	
	public MyOnItemClickListener(Context _context){
		this.context = _context;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Przepis przepis = (Przepis)view.getTag();
		// Starting new intent
		Intent i = new Intent(context, WyswietlPrzepis.class);
		i.putExtra("przepis", przepis);
		Log.d("MyOnItemClickListener", "Wybrano przepis "+przepis.tytul+", przepisID: "+przepis.przepisID);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}
}
