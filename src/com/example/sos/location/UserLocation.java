package com.example.sos.location;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;;

  


   /*@ Author
    * r00t*/
/*In order to access Android location services we need an instance of
 *LocationManager. To do this, the [getSystemService(Context.LOCATION_SERVICE)] method of our Activity is called
 *We don't need a criteria definition since a suitable location will be selected by default after testing for both GPS and NetworkProvider
 *In addition, we use [getLastKnownLocation(String)] method, which returns the last known Location from
 *the given provider
 *[Location] object provide us useful information about geographic location and help us to specify the
 *current location of the user. 
 *[LocationListener] is used to handle user movement and show current position
 *[LocationManager] can request callbacks of [LocationListener], in order to handle location or provider's status
 *changes
 *through [requestLocationUpdates]we receive location updates procuring the current provider & location listener */


public class UserLocation extends Service implements LocationListener {

	  /*private TextView latitude;
	  private TextView longitude;
	  private TextView choice;
	  private CheckBox fineAcc;
	  private Button choose;
	  private TextView provText;
	  private LocationManager locationManager;
	  private String provider;
	  private MyLocationListener mylistener;
	  private Criteria criteria;
	  */
	
	  private final Context context;
	  
	  // flag for GPS status
	  boolean isGPSEnabled = false;
	  
	  // flag for Network status
	  boolean isNetworkEnabled = false;
	  
	// flag for GPS status
	  boolean canGetLocation = false;

      public Location location; // location
	  double latitude; // latitude
	  double longitude; // longitude
	  
	
	  // The minimum distance to change Updates in meters
	  private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; //10 METERS
	  
	  // The minimum time between updates in milliseconds
	  private static final long MIN_TIME_BTW_UPDATES = 1000 * 60 *1; //1 MINUTE
	  
	  // Declare the Location manager
	  protected LocationManager locationManager;
	  
	  public UserLocation(Context context){
		  this.context = context;
		  getLocation();
	  }
	  
	  public  Location getLocation(){
		  try{
			  locationManager = (LocationManager)context.getSystemService(LOCATION_SERVICE);
			  
			  // getting GPS status
			  isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			  
			  // getting Network status
			  isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			
			  if(!isGPSEnabled && !isNetworkEnabled){
				  // no network provider is enabled
			  }else{
				  this.canGetLocation = true;
				  if(isNetworkEnabled){
					  locationManager.requestLocationUpdates(
							  LocationManager.NETWORK_PROVIDER,
							  MIN_TIME_BTW_UPDATES ,MIN_DISTANCE_CHANGE_FOR_UPDATES ,this );
					  Log.d("Network","Network");
					  if(locationManager != null){
						  location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						  if(location != null){
							  latitude = location.getLatitude();
							  longitude = location.getLongitude();
						  }
					  }
				  }
				  // if GPS is enabled get lat/long using GPS service
				  if(isGPSEnabled){
					  if(location == null){
						  locationManager.requestLocationUpdates(
								  LocationManager.GPS_PROVIDER, MIN_TIME_BTW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						  Log.d("GPS Enabled","GPS Enabled");
						  if(locationManager != null){
							  location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							  if(location != null){
								  latitude = location.getLatitude();
								  longitude = location.getLongitude();
							  }
						  }
					  }
				  }
			  }
		  }catch(Exception e){
			  e.printStackTrace();
		  }
		  
		  return location;
	  }
	  
	  /**
		 * Stop using GPS listener
		 * Calling this function will stop using GPS in your app
		 * */
	  public void stopGPS(){
		  if(locationManager != null){
			  locationManager.removeUpdates(UserLocation.this);
		  }
	  }
	  
	    /**
		 * Function to get latitude
		 * */
	  public double getLatitude(){
			if(location != null){
				latitude = location.getLatitude();
			}
			
			// return latitude
			return latitude;
		}
	  
	    /**
		 * Function to get longitude
		 * */
	
	  public double getLongitude(){
			if(location != null){
				longitude = location.getLongitude();
			}
			
			// return longitude
			return longitude;
		}

	    /**
		 * Function to check GPS/wifi enabled
		 * @return boolean
		 * */
	  
	  public boolean canGetLocation(){
		  return this.canGetLocation;
	  }
	  
	    /**
		 * Function to show settings alert dialog
		 * On pressing Settings button will lauch Settings Options
		 * */
	  public void showSettingsAlert(){
		  AlertDialog.Builder alertDialog =  new AlertDialog.Builder(context);
		  
		  //Set Dialog title
		  alertDialog.setTitle("GPS settings");
		  
		 // Setting Dialog Message
	      alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
	      
	   // On pressing Settings button
	        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog,int which) {
	            	Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	            	context.startActivity(intent);
	            }
	        });
	        
	        // on pressing cancel button
	        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();	
				}
			});
	        
	        // showing alert messsage
	        alertDialog.show();
	  }


	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	  
	  
	  
	  
	  
	  
}
