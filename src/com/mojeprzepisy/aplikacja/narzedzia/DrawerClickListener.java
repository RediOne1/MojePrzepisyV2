package com.mojeprzepisy.aplikacja.narzedzia;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mojeprzepisy.aplikacja.Lista_przepisy;
import com.mojeprzepisy.aplikacja.R;

public class DrawerClickListener implements OnClickListener,
		ListView.OnItemClickListener {

	private Activity root;

	public DrawerClickListener(Activity _root) {
		this.root = _root;
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		selectItem(position);
	}

	public void selectItem(int position) {
		String kategorie[] = root.getResources().getStringArray(
				R.array.kategorie);
		Intent i = new Intent(root, Lista_przepisy.class);
		i.putExtra("kategoria", kategorie[position]);
		root.startActivity(i);
	}
}
