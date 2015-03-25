package com.mojeprzepisy.aplikacja;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.mojeprzepisy.aplikacja.dodaj_przepis.DodajPrzepisActivity;
import com.mojeprzepisy.aplikacja.dodaj_przepis.Krok;
import com.mojeprzepisy.aplikacja.narzedzia.AlertDialogManager;
import com.mojeprzepisy.aplikacja.narzedzia.DrawerClickListener;
import com.mojeprzepisy.aplikacja.narzedzia.JSONParser;
import com.mojeprzepisy.aplikacja.narzedzia.MojePrzepisy;
import com.mojeprzepisy.aplikacja.narzedzia.Szukaj;

public class MainActivity extends FragmentActivity implements OnClickListener,
		ActionBar.TabListener {
	public int user_id = 0;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	private final int OCEN_ILE_URUCHOMIEN = 15;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	private DrawerLayout mDrawerLayout;
	private Zaloguj logowanie;
	private LinearLayout mDrawerLinear;
	private ActionBarDrawerToggle mDrawerToggle;
	public LinearLayout mPrzepisy, dodajPrzepis, ulubione, stworzKonto;
	public EditText login;
	public EditText haslo;
	public FrameLayout zaloguj, wyloguj;
	public ListView lv;
	public DrawerClickListener drawerListener;
	private int versionCode, version;
	JSONParser jParser = new JSONParser();
	public JSONArray dane = null;
	private String url_sprawdz_wersje;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		login = (EditText) findViewById(R.id.drawer_login);
		haslo = (EditText) findViewById(R.id.drawer_haslo);
		zaloguj = (FrameLayout) findViewById(R.id.drawer_login_button);
		wyloguj = (FrameLayout) findViewById(R.id.drawer_logout_button);
		url_sprawdz_wersje = getString(R.string.url_sprawdz_wersje);
		drawerListener = new DrawerClickListener(this);
		logowanie = new Zaloguj(this, login, haslo);
		zaloguj.setOnClickListener(this);
		wyloguj.setOnClickListener(this);
		mPrzepisy = (LinearLayout) findViewById(R.id.drawer_moje_przepisy);
		dodajPrzepis = (LinearLayout) findViewById(R.id.drawer_dodaj_przepis);
		ulubione = (LinearLayout) findViewById(R.id.drawer_ulubione);
		stworzKonto = (LinearLayout) findViewById(R.id.drawer_stworz_konto);
		stworzKonto.setOnClickListener(this);
		ulubione.setOnClickListener(this);
		dodajPrzepis.setOnClickListener(this);
		mPrzepisy.setOnClickListener(this);
		lv = (ListView) findViewById(R.id.drawer_wybierz_kategorie);
		lv.setOnItemClickListener(drawerListener);
		new SprawdzUpdate().execute();
		licznikUruchomien();

		new DrawerKategorie(this, lv);
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			this.versionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			this.versionCode = 0;
		}

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLinear = (LinearLayout) findViewById(R.id.left_linear);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				// getActionBar().setTitle(mTitle);
				// invalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				// getActionBar().setTitle(mDrawerTitle);
				// invalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	public void licznikUruchomien() {
		SharedPreferences ocen = getSharedPreferences("ocen", 0);
		SharedPreferences.Editor edytorPref = ocen.edit();
		int uruchomien = ocen.getInt("uruchomien", 0);

		if (uruchomien > OCEN_ILE_URUCHOMIEN
				&& !ocen.getBoolean("oceniono", false)) {
			new AlertDialogManager().showUpdateDialog(context, null,
					context.getString(R.string.ocen_aplikacje));
			uruchomien = -1;
		}
		uruchomien++;
		edytorPref.putInt("uruchomien", uruchomien);
		edytorPref.commit();

	}

	class SprawdzUpdate extends AsyncTask<String, Krok, String> {
		@Override
		protected String doInBackground(String... args) {
			try {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("user_version", ""
						+ versionCode));
				JSONObject json = jParser.makeHttpRequest(url_sprawdz_wersje,
						"POST", params);
				int success = json.getInt("success");
				if (success == 1) {
					version = json.getInt("version");
				} else {
					version = versionCode;
				}
			} catch (Exception e) {
				Log.e("DEBUG_TAG", "" + e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String file_url) {
			if (version > versionCode)
				new AlertDialogManager().showUpdateDialog(context, null,
						context.getString(R.string.dostepne_update));
		}
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.add("Szukaj").setIcon(android.R.drawable.ic_menu_search)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		if (item.toString() == "Szukaj") {
			Intent i = new Intent(getApplicationContext(), Szukaj.class);
			i.putExtra("user_id", user_id);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = new StronaGlowna();
				break;
			case 1:
				fragment = new NajwyzejOceniane();
				break;
			case 2:
				fragment = new OstatnioDodane();
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	@Override
	public void onClick(View v) {
		if (v == zaloguj) {
			logowanie.zaloguj();
		} else if (v == wyloguj) {
			logowanie.wyloguj();
		} else if (v == mPrzepisy) {
			Intent i = new Intent(this, MojePrzepisy.class);
			startActivity(i);
		} else if (v == dodajPrzepis) {
			Intent i = new Intent(this, DodajPrzepisActivity.class);
			i.putExtra("edytuj", false);
			startActivity(i);
		} else if (v == ulubione) {
			Intent i = new Intent(this, UlubioneActivity.class);
			startActivity(i);
		} else if (v == stworzKonto) {
			Intent i = new Intent(this, RejestracjaActivity.class);
			startActivity(i);
		}
		mDrawerLayout.closeDrawer(mDrawerLinear);

	}

}
