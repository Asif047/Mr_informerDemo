package com.example.asif047.mr_informer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.provider.Settings;

/**
 * Created by Asif on 8/22/2017.
 */

public class NetworkUtil {


    private Context context;

    public NetworkUtil(Context context) {
        this.context = context;
    }



    public boolean isGPSon()
    {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE );
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        System.out.print("GPS STATUS"+statusOfGPS);

        return  statusOfGPS;
    }



    public void showNoNetworkDialog(String msg)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(msg);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }



}
