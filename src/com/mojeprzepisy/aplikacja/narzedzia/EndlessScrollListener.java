package com.mojeprzepisy.aplikacja.narzedzia;

import android.app.Activity;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class EndlessScrollListener implements OnScrollListener {

	private int visibleThreshold = 5;
	private int currentPage = 0;
	private int previousTotal = 0;
	private int przepisow_na_strone = 15;
	private boolean loading = false;
	private TopListZapytanie zapytanie;
	private Activity a;
	private ListView lv;
	String params[];

	public EndlessScrollListener(Activity _a, ListView _lv, String adresURL) {
		this.lv = _lv;
		this.a = _a;
		params = new String[3];
		params[0] = adresURL;
		zapytanie = new TopListZapytanie(a, lv);
	}

	public EndlessScrollListener(int visibleThreshold) {
		this.visibleThreshold = visibleThreshold;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (loading) {
			if (totalItemCount > previousTotal) {
				loading = false;
				previousTotal = totalItemCount;
				currentPage++;
			}
		}
		if (!loading
				&& (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
			// I load the next page of gigs using a background task,
			// but you can call any function here.

			params[1] = "" + (przepisow_na_strone * currentPage);
			params[2] = "" + (przepisow_na_strone);
			Log.d("DEBUG_TAG", "Wczytaj przepisy "
					+ (przepisow_na_strone * currentPage) + "-"
					+ (przepisow_na_strone * currentPage + przepisow_na_strone));
			zapytanie.wykonaj(params);
			loading = true;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}
}
