package com.mojeprzepisy.aplikacja.narzedzia;

import com.mojeprzepisy.aplikacja.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

public class AlertDialogManager {

	public void showAlertDialog(Context context, String title, String message) {
		final AlertDialog alertDialog = new AlertDialog.Builder(context)
				.create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);

		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				alertDialog.dismiss();
			}
		});
		alertDialog.show();
	}

	@SuppressWarnings("deprecation")
	public void showUpdateDialog(final Context context, String title,
			final String message) {
		final AlertDialog alertDialog = new AlertDialog.Builder(context)
				.create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);

		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (message.equals(context.getString(R.string.ocen_aplikacje))) {
					SharedPreferences ocen = context.getSharedPreferences(
							"ocen", 0);
					SharedPreferences.Editor edytorPref = ocen.edit();
					edytorPref.putBoolean("oceniono", true);
					edytorPref.commit();
				}
				Uri uri = Uri
						.parse("market://details?id=com.mojeprzepisy.aplikacja");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				context.startActivity(intent);
				alertDialog.dismiss();
			}
		});
		alertDialog.setButton2(context.getString(android.R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						alertDialog.dismiss();
					}
				});
		alertDialog.show();
	}
}