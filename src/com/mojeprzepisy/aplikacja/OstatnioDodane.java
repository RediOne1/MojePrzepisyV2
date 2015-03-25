package com.mojeprzepisy.aplikacja;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mojeprzepisy.aplikacja.narzedzia.EndlessScrollListener;
import com.mojeprzepisy.aplikacja.narzedzia.MyApp;
import com.mojeprzepisy.aplikacja.narzedzia.MyOnItemClickListener;
import com.mojeprzepisy.aplikacja.narzedzia.TopListZapytanie;

public class OstatnioDodane extends ListFragment {
	View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.ostatnio_dodane, container, false);
		return rootView;
	}

	ListView lv;
	private MyApp app;
	private String url_ostatnio_dodane;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		app = (MyApp) getActivity().getApplicationContext();
		app.od = this;
		loadActivity();
	}

	@Override
	public void onDestroyView() {
		app.od = null;
		super.onDestroyView();
	}

	public void loadActivity() {
		url_ostatnio_dodane = getResources().getString(
				R.string.url_ostatnio_dodane);
		lv = getListView();
		lv.setOnScrollListener(new EndlessScrollListener(getActivity(), lv,
				url_ostatnio_dodane));
		lv.setOnItemClickListener(new MyOnItemClickListener(getActivity()
				.getApplicationContext()));
	}
}
