package com.inlog.ecommerce.util;

import android.annotation.SuppressLint;
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
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

public class GpsTracker extends Service implements LocationListener
{
    private final Context mContext;

    // Cihazda gps acik mi?
    boolean isGPSEnabled = false;

    // Cihazda veri baglantisi aktif mi?
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    // Konum
    public Location location;
    // Enlem
    double latitude;
    // Boylam
    double longitude;

    // Konum guncellemesi gerektirecek minimum degisim miktari
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;


    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    // LocationManager nesnesi
    protected LocationManager locationManager;

    //
    // Kurucu Metod - Constructor
    //
    public GpsTracker(Context context)
    {
        this.mContext = context;
        getLocation();
    }

    //
    // Konum bilgisini dondurur
    //
    @SuppressLint("MissingPermission")
    public Location getLocation()
    {
        try
        {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            // GPS acik mi?
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Internet acik mi?
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled)
            {
                showSettingsAlert();
            }
            else
            {

                this.canGetLocation = true;
                if (isGPSEnabled)
                {
                    if (location == null)
                    {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null)
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null)
                            {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
                if (isNetworkEnabled) {
                    if (location == null)
                    {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                         Log.d("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                 }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return location;
    }
    // Enlem bilgisini dondurur
    public double getLatitude()
    {
        if(location != null)
        {
            latitude = location.getLatitude();
        }

        return latitude;
    }

    // Boylam bilgisini dondurur
    public double getLongitude()
    {
        if(location != null)
        {
            longitude = location.getLongitude();
        }

        return longitude;
    }

    @Override
    public void onLocationChanged(Location location)
    {
    }

    @Override
    public void onProviderDisabled(String provider)
    {
    }

    @Override
    public void onProviderEnabled(String provider)
    {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    public boolean canGetLocation()
    {
        return this.canGetLocation;
    }

    // Konum bilgisi kapali ise kullaniciya ayarlar sayfasina baglanti iceren bir mesaj goruntulenir
    public void showSettingsAlert()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Mesaj basligi
        alertDialog.setTitle("GPS Settings");

        // Mesaj
        alertDialog.setMessage("Please allow this application to access your location.");

        // Mesaj ikonu
        //alertDialog.setIcon(R.drawable.delete);

        // Ayarlar butonuna tiklandiginda
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // Iptal butonuna tiklandiginda
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        // Mesaj kutusunu goster
        alertDialog.show();
    }

    // LocationManager'in gps isteklerini durdurur
    public void stopUsingGPS()
    {
        if(locationManager != null)
        {
            locationManager.removeUpdates(GpsTracker.this);
        }
    }
}