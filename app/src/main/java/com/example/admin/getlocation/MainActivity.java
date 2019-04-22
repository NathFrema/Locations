package com.example.admin.getlocation;


import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.seismic.ShakeDetector;


public class MainActivity extends AppCompatActivity implements ShakeDetector.Listener{
    private FusedLocationProviderClient fusedLocationClient;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1220;
    private TextView lat, lon;
    Button btnLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lat = (TextView) findViewById(R.id.latitude);
        lon = (TextView) findViewById(R.id.longitude);
        btnLoc = (Button) findViewById(R.id.btnLoc);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        ShakeDetector shakeDetector = new ShakeDetector(this);

        shakeDetector.start(sensorManager);


        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   getLocation();
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(MainActivity.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= 23) { // Marshmallow
                        checkPermission();
                    }
                }

                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                Log.d("Location", "" + location);
                                if (location != null) {
                                    // Logic to handle location object
                                    final double latitude = location.getLatitude();
                                    final double longitude = location.getLongitude();

                                    final String data = "&longitude=" + longitude + "&latitude=" + latitude;
                                    final String url = "https://nathfrema2.000webhostapp.com/location/location.php?location=true" + data;

                                    RequestQueue requestQueue;
                                    Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
                                    Network network = new BasicNetwork(new HurlStack());

                                    requestQueue = new RequestQueue(cache, network);

                                    requestQueue.start();

                                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    lon.setText("" + longitude);
                                                    lat.setText("" + latitude);
                                                    Toast.makeText(MainActivity.this,
                                                            response, Toast.LENGTH_LONG).show();
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Toast.makeText(MainActivity.this, "Error: " + error.getMessage(),
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            });

                                    requestQueue.add(stringRequest);
                                }
                            }
                        })
                        .addOnFailureListener(MainActivity.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Exception", e.getMessage());
                            }
                        });
            }
        });
    }
    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ){//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }

    @Override
    public void hearShake() {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= 23) { // Marshmallow
                checkPermission();
            }
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        Log.d("Location", "" + location);
                        if (location != null) {
                            // Logic to handle location object
                            final double latitude = location.getLatitude();
                            final double longitude = location.getLongitude();

                            final String data = "&longitude=" + longitude + "&latitude=" + latitude;
                            final String url = "https://nathfrema2.000webhostapp.com/location/location.php?location=true" + data;

                            RequestQueue requestQueue;
                            Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
                            Network network = new BasicNetwork(new HurlStack());

                            requestQueue = new RequestQueue(cache, network);

                            requestQueue.start();

                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            lon.setText("" + longitude);
                                            lat.setText("" + latitude);
                                            Toast.makeText(MainActivity.this,
                                                    response, Toast.LENGTH_LONG).show();
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(MainActivity.this, "Error: " + error.getMessage(),
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    });

                            requestQueue.add(stringRequest);
                        }
                    }
                })
                .addOnFailureListener(MainActivity.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Exception", e.getMessage());
                    }
                });

    }
}
