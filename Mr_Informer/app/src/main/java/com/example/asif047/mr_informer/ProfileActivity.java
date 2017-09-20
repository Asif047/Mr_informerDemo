package com.example.asif047.mr_informer;

import android.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

//new starts
    private static final String TAG = "Location";

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Geocoder geocoder;
    private List<Address> addressList;

    private TextView emailTV,addressTV,cityTV,countryTV,date_timeTV;

    private String email,latitude,longitude,address,city,country,date_time;

    //new ends


    private TextView profileTV;
    private Button logoutBtn;
    private Button sendInfoBtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //new starts

        if (!isGPSOn(ProfileActivity.this))buildDialog(ProfileActivity.this).show();
        else
        {

        }

        //new ends





        //new 2 starts

        emailTV= (TextView) findViewById(R.id.email_textview);
        //latitudeTV= (TextView) findViewById(R.id.latitude_textview);
        //longitudeTV= (TextView) findViewById(R.id.longitude_textview);

        addressTV= (TextView) findViewById(R.id.address_textview);
        cityTV= (TextView) findViewById(R.id.city_textview);
        countryTV= (TextView) findViewById(R.id.country_textview);
        date_timeTV= (TextView) findViewById(R.id.date_time_textview);


        sendInfoBtn= (Button) findViewById(R.id.send_info_button);


        geocoder = new Geocoder(this);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for(Location location : locationResult.getLocations()){


                    Log.e(TAG, "current Latitude: "+location.getLatitude());
                    Log.e(TAG, "current Longitude: "+location.getLongitude());
                    try {
                        addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

                        Log.e(TAG, "address line: "+addressList.get(0).getAddressLine(0));
                        Log.e(TAG, "city: "+addressList.get(0).getLocality());
                        Log.e(TAG, "country: "+addressList.get(0).getCountryName());
                        Log.e(TAG, "postal code: "+addressList.get(0).getPostalCode());
                        Log.e(TAG, "sublocality: "+addressList.get(0).getSubLocality());
                        Log.e(TAG, "phone: "+addressList.get(0).getPhone());




                        //new 4 starts

                        latitude=""+location.getLatitude();
                        longitude=""+location.getLongitude();
                        address=""+addressList.get(0).getAddressLine(0);
                        city=""+addressList.get(0).getLocality();
                        country=""+addressList.get(0).getCountryName();

                        //new 4 ends


//                        emailTV.setText("email:"+user.getEmail().toString());
//
//                        latitudeTV.setText("Latitude:"+location.getLatitude());
//                        longitudeTV.setText("Longitude:"+location.getLongitude());

                        addressTV.setText("address: "+addressList.get(0).getAddressLine(0));
                        cityTV.setText("city: "+addressList.get(0).getLocality());
                        countryTV.setText("country: "+addressList.get(0).getCountryName());





                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            }
        };




        getLastLocation();
        createLocationUpadates();






        //to get the time


        java.util.Calendar c = java.util.Calendar.getInstance();
        java.text.SimpleDateFormat sd=new java.text.SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
         date_time=sd.format(c.getTime());



        //Toast.makeText(this,dayofweek,Toast.LENGTH_LONG).show();





        date_timeTV.setText("Date_Time:"+date_time);

        //new2 ends








        firebaseAuth=FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()==null)
        {
            finish();
            startActivity(new Intent(this,LogInActivity.class));
        }

        FirebaseUser user=firebaseAuth.getCurrentUser();
        email=""+user.getEmail();
        emailTV.setText("Email: "+user.getEmail());


        profileTV= (TextView) findViewById(R.id.profile_textview);
        logoutBtn= (Button) findViewById(R.id.log_out_button);


        //String temp_name=getIntent().getStringExtra("user_name");
        //Toast.makeText(ProfileActivity.this,temp_name,Toast.LENGTH_LONG).show();
        if(user.getDisplayName()!=null)
            profileTV.setText("Welcome "+user.getDisplayName());
        if(user.getDisplayName()==null)
            profileTV.setText("Welcomeee "+getIntent().getStringExtra("user_name"));










        logoutBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(this,LogInActivity.class));
    }


//new starts
    public boolean isGPSOn(Context context)
    {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE );
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        System.out.print("GPS STATUS"+statusOfGPS);

        return  statusOfGPS;
    }



    public AlertDialog.Builder buildDialog(Context c)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(c);
        builder.setTitle("Turn on the GPS");
        builder.setMessage("You need to turn on the GPS.Press ok to exit");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        return builder;
    }





    //new 3 starts

    @Override
    protected void onPause() {
        super.onPause();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void createLocationUpadates() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},100);
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null)
                {
                    Toast.makeText(ProfileActivity.this, "Latitude: " + location.getLatitude() +
                    "Longitude: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "latitude"+location.getLatitude());
                    Log.e(TAG, "logintude"+location.getLongitude());

                }

            }
        });
    }

    public void sendInfoOnClick(View view) {

        //new 5 starts

        Intent intent=new Intent(ProfileActivity.this,InformActivity.class);


        intent.putExtra("email",email);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
        intent.putExtra("address",address);
        intent.putExtra("city",city);
        intent.putExtra("country",country);
        intent.putExtra("date_time",date_time);

        startActivity(intent);

        //new 5 ends

    }

    //new 3 ends






}
