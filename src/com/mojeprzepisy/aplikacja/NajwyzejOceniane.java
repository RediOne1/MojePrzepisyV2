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

public class NajwyzejOceniane extends ListFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.najwyzej_oceniane, container,
				false);
		return rootView;
	}

	ListView lv;
	MyApp app;
	private String url_najwyzej_oceniane;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		app = (MyApp) getActivity().getApplicationContext();
		app.no = this;
		loadActivity();
	}

	@Override
	public void onDestroyView() {
		app.no=null;
		super.onDestroyView();
	}

	public void loadActivity() {
		url_najwyzej_oceniane = getResources().getString(R.string.url_najwyzej_oceniane);
		lv = getListView();
		lv.setOnScrollListener(new EndlessScrollListener(getActivity(), lv,
				url_najwyzej_oceniane));
		lv.setOnItemClickListener(new MyOnItemClickListener(getActivity()
				.getApplicationContext()));
	}
}
