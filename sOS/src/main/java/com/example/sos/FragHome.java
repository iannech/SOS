package com.example.sos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.sos.data.DbSOS;
import com.example.sos.data.bean.Contact;
import com.example.sos.location.UserLocation;

public class FragHome extends Fragment {

	ImageButton btnSos;
	UserLocation gps;
	double latitude;
	double longitude;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_home, container, false);

		btnSos = (ImageButton) rootView.findViewById(R.id.sos_button);
		
		btnSos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Vibrator vibe = (Vibrator) getActivity().getSystemService(
						Context.VIBRATOR_SERVICE);

				vibe.vibrate(50);

				// Send alert method()
				// Start sendsms class
				// check if GPS enabled
				gps = new UserLocation(getActivity());
				if (gps.canGetLocation()) {

					latitude = gps.getLatitude();
					longitude = gps.getLongitude();
				} else {
					// can't get location
					// GPS or Network is not enabled
					// Ask user to enable GPS/network in settings
					gps.showSettingsAlert();
				}
				sendSms();
			}
		});
		return rootView;

	}

	private void sendSms() {
		try {
			DbSOS db = new DbSOS(getActivity());
			Contact contact = db.contact.get("1");
			String message = db.message.get().getMessage() + " " + latitude
					+ " " + longitude;
			;
			db.close();
			SmsManager manager = SmsManager.getDefault();
			String link = "http://maps.google.com/maps?q=+" + latitude + ","
					+ longitude + "+(My+Point)&z=14&ll=+" + latitude + ","
					+ longitude;
			message += "\n\n" + link;
			manager.sendTextMessage(contact.getPhone(), null, message, null,
					null);
			Toast.makeText(getActivity(), "Sent Successfully",
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(getActivity(), "Unable to send sms",
					Toast.LENGTH_LONG).show();
		}
	}

}
