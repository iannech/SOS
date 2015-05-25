package com.example.sos.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class ActionSyncMsg extends AsyncTask<String, String, String> {	
	
	private Context ctx = null;	
	private String msg = null;
	private Runnable action = null;
	private Runnable response = null;
	private ProgressDialog dialog = null;
	
	public  ActionSyncMsg(Context ctx, String msg, Runnable action, Runnable response){
		
		this.ctx = ctx;
		this.action =action;
		this.response = response;
		this.msg = msg;
		
		dialog = new ProgressDialog(ctx);
		dialog.setCancelable(false);
		dialog.setMessage(msg);
	}
	
	
	@Override
	protected void onPreExecute() {		
		super.onPreExecute();      
        dialog.show();
	}
		
	@Override
	protected String doInBackground(String... params) {		
		action.run();
		return null;
	}	
	
	@Override
	protected void onPostExecute(String result) {		
		super.onPostExecute(result);	
		dialog.dismiss();
		response.run();
	}
}	
