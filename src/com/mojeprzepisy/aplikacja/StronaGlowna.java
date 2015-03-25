package com.mojeprzepisy.aplikacja;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mojeprzepisy.aplikacja.RejestracjaActivity.WyslijMail;
import com.mojeprzepisy.aplikacja.dodaj_przepis.DodajPrzepisActivity;
import com.mojeprzepisy.aplikacja.narzedzia.MyApp;
import com.mojeprzepisy.aplikacja.narzedzia.MyTypeFace;

public class StronaGlowna extends Fragment implements OnClickListener {

	private View root;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.strona_glowna, container,
				false);
		this.root = rootView;
		return rootView;
	}
private MyApp app;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		app = (MyApp) getActivity().getApplicationContext();
		app.sg = this;
		loadActivity();
	}

	@Override
	public void onDestroyView() {
		app.sg=null;
		super.onDestroyView();
	}

	public void loadActivity() {
		new LosowyPrzepis(getActivity());
	}

	@Override
	public void onClick(View v) {
	}

}
