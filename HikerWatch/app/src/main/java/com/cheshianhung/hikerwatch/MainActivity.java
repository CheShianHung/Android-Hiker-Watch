package com.cheshianhung.hikerwatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView latitude;
    TextView longitude;
    TextView altitude;
    TextView addressText;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latitude = (TextView) findViewById(R.id.latitude);
        longitude = (TextView) findViewById(R.id.longitude);
        altitude = (TextView) findViewById(R.id.altitude);
        addressText = (TextView) findViewById(R.id.address);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener(){
            @Override
            public void onLocationChanged(Location location) {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try{
                    List<Address> listAddress = geocoder. getFromLocation(location.getLatitude(),location.getLongitude(),1);

                    latitude.setText("Latitude: " + String.format("%.02f", location.getLatitude()));
                    longitude.setText("Longitude: " + String.format("%.02f", location.getLongitude()));
                    altitude.setText("Altitude: " + String.format("%.02f", location.getAltitude()));

//                    latitude.setText("Latitude: " + String.valueOf(location.getLatitude()));
//                    longitude.setText("Longitude: " + String.valueOf(location.getLongitude()));
//                    accuracy.setText("Accuracy: " + String.valueOf(location.getAccuracy()));
//                    altitude.setText("Altitude:" + String.valueOf(location.getAltitude()));


                    if(listAddress.size() > 0){
                        String address = "   ";

                        if(listAddress.get(0).getSubThoroughfare() != null)
                            address += listAddress.get(0).getSubThoroughfare() + ", ";
                        if(listAddress.get(0).getThoroughfare() != null)
                            address += listAddress.get(0).getThoroughfare() + "\n   ";
                        if(listAddress.get(0).getLocality() != null)
                            address += listAddress.get(0).getLocality() + " ";
                        if(listAddress.get(0).getPostalCode() != null)
                            address += listAddress.get(0).getPostalCode() + "\n   ";
                        if(listAddress.get(0).getCountryName() != null)
                            address += listAddress.get(0).getCountryName();

 //                       Toast.makeText(getApplicationContext(), address, Toast.LENGTH_SHORT).show();

                        addressText.setText("Address:\n" + address);
                    }
                }
                catch(IOException e) {
                    e.printStackTrace();
                    addressText.setText("Address: Not Available");
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }
        };

        if(Build.VERSION.SDK_INT < 23)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        else {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }

    }

}
