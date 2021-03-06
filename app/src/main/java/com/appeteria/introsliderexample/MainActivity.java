package com.appeteria.introsliderexample;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.appeteria.introsliderexample.Classes.ForceUpdateChecker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.AppOpsManager.MODE_ALLOWED;

public class MainActivity extends Activity implements ForceUpdateChecker.OnUpdateNeededListener{

    TextView locationTV;
    TextView dataUsage;

    LocationManager locationManager;
    LocationListener locationListener;
    Location location;
    Geocoder geocoder;

    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    private static final String NET_TAG = "Network Status";
    String locationProvider;
    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationTV = (TextView) findViewById(R.id.locationTextView);
        dataUsage = (TextView) findViewById(R.id.dataUsageTextView);
        ForceUpdateChecker.with(this).onUpdateNeeded(this).check();
        //connectivity manager
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiCon = networkInfo.isConnected();

        networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobCon = networkInfo.isConnected();

        if (isMobCon) {
            Toast.makeText(this, "Mobile Data Connected!!", Toast.LENGTH_SHORT).show();
        } else if (isWifiCon) {
            Toast.makeText(this, "Wi-Fi Data Connected!!", Toast.LENGTH_SHORT).show();
        }

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationProvider = LocationManager.NETWORK_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation(locationProvider);
        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    public void checkDataUsages() {
        SharedPreferences sp = getSharedPreferences("DataUsage",MODE_PRIVATE);
        dataUsage.setText("Total Data "+String.valueOf(sp.getFloat("latest_stx",TrafficMonitorActivity.latest_stx)+" " +
                "Moble Data "+String.valueOf(sp.getFloat("latest_smtx",TrafficMonitorActivity.latest_smtx))+" "+
                "WifiData "+ String.valueOf(sp.getFloat("latest_swtx",TrafficMonitorActivity.latest_swtx))));
    }

    public void checkDataUsage(View view) {
        //onLocationChanged(location);
        //startActivity(new Intent(MainActivity.this,TrafficMonitorActivity.class));
        checkDataUsages();
    }

    public void apibtn(View view) {
        startActivity(new Intent(MainActivity.this,APITestActivity.class));
    }



    public void dataUsageBtn(View view) {
        startActivity(new Intent(MainActivity.this,TrafficMonitorActivity.class));
    }

    public void firebaseNoti(View view) {
        startActivity(new Intent(MainActivity.this,FireActivity.class));
    }

    public void cardviewClick(View view) {
        checkDataUsages();
    }

    public void gpsTesting(View view) {
        startActivity(new Intent(MainActivity.this, Loc.class));
    }

    public void permissionsTesting(View view) {
        startActivity(new Intent(this,PermissionActivity.class));
    }

    public void login(View view) {
        startActivity(new Intent(this,LoginActivity.class));
    }

    @Override
    public void onUpdateNeeded(final String updateUrl) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setMessage("Please, update app to new version to continue reposting.")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                redirectStore(updateUrl);
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).create();
        dialog.show();
    }
    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
