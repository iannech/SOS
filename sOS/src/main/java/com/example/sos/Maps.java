package com.example.sos;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.drm.DrmManagerClient;
import android.graphics.Path;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;

import android.content.Intent;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.telephony.TelephonyManager;

;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by rOOt on 5/18/2015.
 */
public class Maps extends FragmentActivity{

    /*
    * HOSPITAL,POLICE,FIRESTATION and AMBULANCE are constant variables that are assigned
    * to their respective emergency intent. It helps us to draw icons that perform web services
    * related activities*/

    private static final int HOSPITAL = 1;
    private static final int POLICE = 2;
    private static final int FIRESTATION = 3;
    private static final int AMBULANCE = 4;

    private GoogleMap myMap;
    LocationManager lm;
    LocationListener locationListener;
    Location mLoc;
    int code;
    boolean zoomb = true;

    //LIST VIEW
    ListView listview;
    ArrayList<String> itemlist = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayList<Marker> markerList;

    Direction direction = new Direction();
    ArrayList<LatLng> pline = new ArrayList<>();
    LatLng currentmarker;
    Integer cid;

    String provider = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        /*
        * Check if an of the radios(Mobile/Data) is on
        * */
        final WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(TelephonyManager.getDataState() == 0 && wifiManager.isWifiEnabled() == false && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) == false){
            AlertDialog.Builder ad = new AlertDialog.Builder(Maps.this);
            ad.setTitle("First-Responder");
            ad.setMessage("Your network and gps providers are off. Please enable one of them.");
            ad.setPositiveButton("Network", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    AlertDialog.Builder network = new AlertDialog.Builder(Maps.this);
                    network.setTitle("First-Responder");
                    network.setMessage("Please choose between Wi-Fi and Mobile Data");
                    network.setPositiveButton("Wi-Fi", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                            startActivity(intent);
                            provider = "network";
                        }
                    });
                    network.setNegativeButton("Mobile Data", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent (android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
                            startActivity(intent);
                            provider = "network";
                        }
                    });
                    network.show();
                }
            });
            ad.setNegativeButton("GPS", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    provider = "gps";
                }
            });
            ad.show();
        }

        if(telephonyManager.getDataState() == TelephonyManager.DATA_CONNECTED){
            provider = "network";

        }else if(wifiManager.isWifiEnabled()){
            provider="network";

        }else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            provider="gps";
        }

        listview = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,itemlist)
        listview.setAdapter(adapter);
        markerList = new ArrayList<Marker>();

        FragmentManager myFragmentManager = getSupportFragmentManager();
        SupportMapFragment mySupportMapFragment = (SupportMapFragment) myFragmentManager.findFragmentById(R.id.map)
        myMap = mySupportMapFragment.getMap;
        myMap.setMyLocationEnabled(true);
        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocatiionListener();
        Intent intent= getIntent();
        code = intent.getIntExtra("key", 0);


        if(provider != null){
            mLoc = locationManager.getLastKnownLocation(provider);
        }

        if(mLoc != null){
            query(mLoc);
        }

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LatLng point = markerList.get(position).getPositin();
                CameraUpdate center = CameraUpdateFactory.newLatLng(point);
                myMap.animateCamera(center, 2000, null);

            }
        });

        myMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {
                // TODO Auto-generated method stub
                AlertDialog.Builder ad = new AlertDialog.Builder(Maps.this);
                ad.setTitle("First-Responder");
                ad.setMessage("Would you like directions?");
                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        myMap.clear();
                        query(mLoc);
                        currentmarker = marker.getPosition();
                        LatLng pt = marker.getPosition();
                        direction.getmapsinfo(myMap, pt, mLoc, itemlist, adapter);
                        listview.setClickable(false);
                    }
                });
                ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        //ok, do nothing
                        dialog.cancel();
                    }
                });
                ad.show();
            }
        });


    }

    /**
    * This method takes in a parameter loc which is the users current location. Then according to the view
     * pressed from MainActivity the accirding case is executed. The method performs a GooglePlay nearby
     * search and returns a JSON string for Parsing**/

    public void query(Location loc){
        switch(code){
            case 1:
                new GetHospitals()
                      .execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                                      + Double.toString(mLoc.getLatitude())
                                      + ","
                                      + Double.toString(mLoc.getLongitude())
                                      + "&rankby=distance&types=hospital&sensor=false&key=");
                break;
            case 2:
                new GetHospitals()
                        .execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                                + Double.toString(mLoc.getLatitude())
                                + ","
                                + Double.toString(mLoc.getLongitude())
                                + "&rankby=distance&types=police&sensor=false&key=AIzaSyAPrOxAoTKUdaXtktg4B2QrdPZO5SpM0VQ");

                break;
            case 3:
                new GetHospitals()
                        .execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                                + Double.toString(mLoc.getLatitude())
                                + ","
                                + Double.toString(mLoc.getLongitude())
                                + "&rankby=distance&types=fire_station&sensor=false&key=AIzaSyAPrOxAoTKUdaXtktg4B2QrdPZO5SpM0VQ");
                break;
            case 4:
                new GetHospitals()

                break;



        }
    }
}
