package com.example.sos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Launcher extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.launcher);
		
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(2500);
				}catch (InterruptedException e){
					e.printStackTrace();
				}finally{
					Intent openMain = new Intent(getApplicationContext(),MainActivity.class );
					startActivity(openMain);
					
				}
			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	} 
}
