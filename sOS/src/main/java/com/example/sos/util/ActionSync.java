package com.example.sos.util;

import android.os.AsyncTask;

public class ActionSync extends AsyncTask<String, String, String> {	
		
	private Runnable action = null;
	private Runnable response = null;	
	
	public  ActionSync(Runnable action, Runnable response){		
		this.action = action;
		this.response = response;		
	}
			
	@Override
	protected String doInBackground(String... params) {		
		action.run();		
		return null;
	}	
	
	@Override
	protected void onPostExecute(String result) {		
		super.onPostExecute(result);			
		response.run();
	}
}	

