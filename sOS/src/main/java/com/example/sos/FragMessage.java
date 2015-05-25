package com.example.sos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sos.data.DbSOS;
import com.example.sos.data.bean.Message;

public class FragMessage extends Fragment {

	Button btnSms;
	EditText txtMessage;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_message, container,
				false);
		txtMessage = (EditText) rootView.findViewById(R.id.txtMessage);
		btnSms = (Button) rootView.findViewById(R.id.btnSms);
		loadMessage();
		btnSms.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				save();
			}			
		});
		return rootView;
	}
	
	private void save() {
		Message message = new Message();
		message.setMessage(txtMessage.getText().toString());
		DbSOS db = new DbSOS(getActivity());
		db.message.set(message);
		db.close();
		Toast.makeText(getActivity(), "Message was saved successfully",
				Toast.LENGTH_SHORT).show();
	}

	private void loadMessage() {
		// get details to database
		DbSOS db = new DbSOS(getActivity());
		Message message = db.message.get();
		db.close();

		if (message == null)
			return;

		txtMessage.setText(message.getMessage());
		
	}

}
