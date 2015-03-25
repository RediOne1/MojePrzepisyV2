package com.mojeprzepisy.aplikacja.narzedzia;

import android.app.Application;

import com.mojeprzepisy.aplikacja.NajwyzejOceniane;
import com.mojeprzepisy.aplikacja.OstatnioDodane;
import com.mojeprzepisy.aplikacja.StronaGlowna;
import com.mojeprzepisy.aplikacja.UlubioneActivity;

public class MyApp extends Application {
	private int data = -1;
	private String pseudonim = "";
	public StronaGlowna sg = null;
	public NajwyzejOceniane no = null;
	public OstatnioDodane od = null;
	public UlubioneActivity ua = null;

	public void reloadActivity(){
		if(sg!=null)
			sg.loadActivity();
		if(no!=null)
			no.loadActivity();
		if(od!=null)
			od.loadActivity();
	}
	public void reloadUlubione(){
		if(ua!=null)
			ua.loadActivity();
	}
	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}

	public String getPseudonim() {
		return pseudonim;
	}

	public void setPseudonim(String s) {
		this.pseudonim = s;
	}
}