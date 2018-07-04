package com.appeteria.introsliderexample;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class PermissionActivity extends AppCompatActivity {

    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 101;
    private static final int REQUEST_PERMISSION_SETTING =101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        Log.e("PermissionActivity","Oncreate");
        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);

    }

    public void multiplePermission(View view) {
        startActivity(new Intent(PermissionActivity.this,MultiplePermission.class));
    }

    public void fragmentPermission(View view) {
    }

    public void downloadFile(View view) {
        Log.e("PermissionActivity","Download File, Begins");
        if (ActivityCompat.checkSelfPermission(PermissionActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(PermissionActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.e("PermissionActivity","Previously Permission request was canceled.");
                //Previously Permission Request was cancelled with 'Don't ask again',
                //Redirect to Settings after showing information about why you need the permissions;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Need Storage Permission")
                        .setMessage("This app needs storage Permission")
                        .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                sentToSettings = true;
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                                Toast.makeText(PermissionActivity.this, "Go to Permissions to grant storage",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).show();
            } else {
                // Just request the permissions
                Log.e("PermissionActivity","Just Request the permission");
                ActivityCompat.requestPermissions(PermissionActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,true);
            editor.commit();

        } else {
            //You Already Have The Permission. just go ahead.
            Log.e("PermissionActivity","Already Have the permission");
            proceedAfterPermission();
        }
    }

    private void  proceedAfterPermission() {
        Log.e("PermissionActivity","Proceed After Permissions");
        Toast.makeText(this, "We got Storage Permissions!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("PermissionActivity","On Request Permission Result Open");
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //External Storage Permission is granted
                Log.e("PermissionActivity","ORPRO, Permission is granted");
                proceedAfterPermission();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Log.e("PermissionActivity","ORPRO, Why wee need permission.");
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Need Storage Permission")
                            .setMessage("This apps need Storage Permission")
                            .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    ActivityCompat.requestPermissions(PermissionActivity.this,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    }).show();
                } else {
                    Log.e("PermissionActivity","ORPRO, Unable to get the permission");
                    Toast.makeText(this, "Unable to get Permissions", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(PermissionActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission to settings
                Log.e("PermissionActivity","OAR, Got permission - settings");
                proceedAfterPermission();
            }
        }
        Log.e("PermissionActivity","On Activity Result");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            Log.e("PermissionActivity","OPR, Sent To Settings is true.");
            if (ActivityCompat.checkSelfPermission(PermissionActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // Got Permission
                Log.e("PermissionActivity","OPR, Got permission onPostResume.");
                proceedAfterPermission();
            }
        }
        Log.e("PermissionActivity","On Post Resume");
    }
}

