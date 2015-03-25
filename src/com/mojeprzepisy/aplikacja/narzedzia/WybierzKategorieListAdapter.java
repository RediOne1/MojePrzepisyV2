package com.mojeprzepisy.aplikacja.narzedzia;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mojeprzepisy.aplikacja.R;

public class WybierzKategorieListAdapter extends BaseAdapter {
	private Activity activity;
	private String dane[];
	private static LayoutInflater inflater = null;

	public WybierzKategorieListAdapter(Activity a,
			String wszystkieKategorie[]) {
		activity = a;
		dane = wszystkieKategorie;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return dane.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;

		if (convertView == null)
			vi = inflater.inflate(R.layout.drawer_list_item, null);

		TextView nazwa = (TextView) vi.findViewById(R.id.drawer_list_item_textview);

		nazwa.setText(dane[position]);
		vi.setTag(dane[position]);
		return vi;
	}
};
