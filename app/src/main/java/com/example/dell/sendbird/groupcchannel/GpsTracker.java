package com.example.dell.sendbird.groupcchannel;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by DELL on 08-12-2017.
 */

public class GpsTracker extends Service implements LocationListener {

    private final Context context;
    boolean isGPSEnabled = false;
    boolean canGetLocation = false;
    Location location;
    boolean isNetworkEnabled = false;

    double latitude;
    double longitude;

    protected LocationManager locationManager;

    public GpsTracker(Context context) {
        this.context = context;
        getlocation();
    }

    public Location getlocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {


            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {

                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 10, this);
                    if (locationManager != null) {

                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();
                        }
                    }


                }

                if (isGPSEnabled) {

                    if (location == null) {

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 10, this);
                            if (locationManager != null) {
                                location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                                if (location != null) {
                                    longitude = location.getLongitude();
                                    latitude = location.getLatitude();
                                }
                            }
                        }

                    }



            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public void StopUsingGPS() {
        if (location != null) {
            locationManager.removeUpdates(GpsTracker.this);
        }
    }

    public double getLatitude() {
        if(location!=null)
        {
            latitude=location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude() {
        if(location!=null)
        {
            longitude=location.getLongitude();
        }
        return longitude;
    }

    public boolean canGetLocation()
    {
        return this.canGetLocation();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
