package com.example.sos;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sos.data.DbSOS;
import com.example.sos.data.bean.Details;

public class FragDetails extends Fragment {

	private EditText firstName;
	private EditText secondName;
	private EditText dob;
	private EditText age;
	private EditText gender;
	private EditText phoneNumber;
	private Button btnSave;
	
	//Pattern to validate firstName, lastName, dob, age, gender, phone
		Pattern vfname = Pattern.compile("[A-Za-z]+|[A-Za-z]+\\s[A-Za-z]+");
		Pattern vlname = Pattern.compile("[A-Za-z]+|[A-Za-z]+\\s[A-Za-z]+");
		Pattern vdob = Pattern.compile("[0-9]{10}|[0-9]{7}");
		Pattern vage = Pattern.compile("[0-100]{10}");
		Pattern vgender = Pattern.compile("[A-Za-z]+|[A-Za-z]+\\s[A-Za-z]+");
		Pattern vphone = Pattern.compile("[0-9]{10}|[0-9]{7}");
		

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_details, container,
				false);
		bindViews(rootView);
		loadDetails();
		return rootView;
	}

	private void bindViews(View rootView) {
		firstName = (EditText) rootView.findViewById(R.id.edittext1);
		secondName = (EditText) rootView.findViewById(R.id.edittext2);
		dob = (EditText) rootView.findViewById(R.id.edittext3);
		age = (EditText) rootView.findViewById(R.id.edittext4);
		gender = (EditText) rootView.findViewById(R.id.edittext5);
		phoneNumber = (EditText) rootView.findViewById(R.id.edittext6);
		btnSave = (Button) rootView.findViewById(R.id.btnSignIn);

		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				save();
			}
		});
	}

	private void loadDetails() {
		
		// get details to database
		DbSOS db = new DbSOS(getActivity());
		Details details = db.detail.get();
		db.close();

		if (details == null)
			return;

		firstName.setText(details.getFirstName());
		secondName.setText(details.getLastName());
		dob.setText(details.getDateOfBirth());
		age.setText(details.getAge());
		gender.setText(details.getGender());
		phoneNumber.setText(details.getPhone());

	}
	
	// used to make sure the text fields are valid inputs before saving.
		public boolean validator(){
			boolean ans = false;
			boolean name1 = false;
			boolean name2 = false;
			boolean DOB = false;
			boolean AGE = false;
			boolean GENDER = false;
			boolean PHONE = false;
			
			if(firstName.getText().toString() != null){
				Matcher mname = vfname.matcher(firstName.getText().toString());
				
				if(mname.matches()){
					name1= true;
					firstName.setTextColor(Color.BLACK);
					firstName.setVisibility(TextView.INVISIBLE);
				}else {
					name1 = false;
					Log.v("VALIDATIONS", "INVALID NAME");
					firstName.setTextColor(Color.RED);
					firstName.setHintTextColor(Color.RED);
					
				}
			}
			
			if(secondName.getText().toString() != null){
				Matcher mname = vlname.matcher(secondName.getText().toString());
				
				if(mname.matches()){
					name2= true;
					secondName.setTextColor(Color.BLACK);
					secondName.setVisibility(TextView.INVISIBLE);
				}else {
					name2 = false;
					Log.v("VALIDATIONS", "INVALID NAME");
					secondName.setTextColor(Color.RED);
					secondName.setHintTextColor(Color.RED);
					
					
				}
			}
			
			if(dob.getText().toString() != null){
				Matcher mname = vdob.matcher(dob.getText().toString());
				
				if(mname.matches()){
					DOB= true;
					dob.setTextColor(Color.BLACK);
					dob.setVisibility(TextView.INVISIBLE);
				}else {
					DOB = false;
					Log.v("VALIDATIONS", "INVALID DOB");
					dob.setTextColor(Color.RED);
					dob.setHintTextColor(Color.RED);
					
					
				}
			}
			
			if(age.getText().toString() != null){
				Matcher mname = vage.matcher(age.getText().toString());
				
				if(mname.matches()){
					AGE = true;
					age.setTextColor(Color.BLACK);
					age.setVisibility(TextView.INVISIBLE);
				}else {
					AGE = false;
					Log.v("VALIDATIONS", "INVALID AGE");
					age.setTextColor(Color.RED);
					age.setHintTextColor(Color.RED);
					
					
				}
			}
			
			if(gender.getText().toString() != null){
				Matcher mname = vgender.matcher(gender.getText().toString());
				
				if(mname.matches()){
					GENDER= true;
					gender.setTextColor(Color.BLACK);
					gender.setVisibility(TextView.INVISIBLE);
				}else {
					GENDER = false;
					Log.v("VALIDATIONS", "INVALID GENDER");
					gender.setTextColor(Color.RED);
					dob.setHintTextColor(Color.RED);
					
					
				}
			}
			
			if(phoneNumber.getText().toString() != null){
				Matcher mname = vphone.matcher(phoneNumber.getText().toString());
				
				if(mname.matches()){
					PHONE = true;
					phoneNumber.setTextColor(Color.BLACK);
					phoneNumber.setVisibility(TextView.INVISIBLE);
				}else {
					PHONE = false;
					Log.v("VALIDATIONS", "INVALID NUMBER");
					phoneNumber.setTextColor(Color.RED);
					phoneNumber.setHintTextColor(Color.RED);
					
					
				}
			}
			
			// if both name and number are valid, then ans = true;
					if (name1 && name2 && DOB && AGE && GENDER && PHONE) {
						ans = true;
					}
					return ans;
		}

	private void save() {
		Details details = new Details();
		details.setFirstName(firstName.getText().toString());
		details.setLastName(secondName.getText().toString());
		details.setDateOfBirth(dob.getText().toString());
		details.setAge(age.getText().toString());
		details.setGender(gender.getText().toString());
		details.setPhone(phoneNumber.getText().toString());

		// Set details to database
		DbSOS db = new DbSOS(getActivity());
		db.detail.set(details);
		db.close();

		Toast.makeText(getActivity(), "Details were saved successfully",
				Toast.LENGTH_SHORT).show();
	}

}
