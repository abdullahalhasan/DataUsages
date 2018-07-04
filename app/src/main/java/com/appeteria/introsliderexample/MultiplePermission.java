package com.appeteria.introsliderexample;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.appeteria.introsliderexample.Classes.CallLogHelper;
import com.appeteria.introsliderexample.Classes.TaskingTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MultiplePermission extends AppCompatActivity {
    private static final int PERMISSION_CALL_BACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionRequired = new String[] {Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_SMS
    };
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    private TextView txtPermissions;
    private Button btnCheckPermissions;
    private ProgressDialog progress;

    JSONArray callLogjArry;
    private ArrayList<String> conNames;
    private ArrayList<String> conNumbers;
    private ArrayList<String> conTime;
    private ArrayList<String> conDate;
    private ArrayList<String> conType;
    private ArrayList<String> singleCallLog;
    private ArrayList<String> allCallLog;
    Cursor curLog;
    JSONObject callLogObject;

    private static final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
    JSONArray smsjArry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_permission);

        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);

        txtPermissions = (TextView) findViewById(R.id.txtPermissions);
        btnCheckPermissions = (Button) findViewById(R.id.btnCheckPermissions);

        callLogjArry =new JSONArray();
        conNames = new ArrayList<String>();
        conNumbers = new ArrayList<String>();
        conTime = new ArrayList<String>();
        conDate = new ArrayList<String>();
        conType = new ArrayList<String>();
        singleCallLog = new ArrayList<String>();
        allCallLog = new ArrayList<String>();
        callLogObject = new JSONObject();
        callLogjArry =new JSONArray();

        smsjArry =new JSONArray();;

        btnCheckPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MultiplePermission.this,
                        permissionRequired[0]) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(MultiplePermission.this,
                        permissionRequired[1]) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MultiplePermission.this, permissionRequired[0])
                            || ActivityCompat.shouldShowRequestPermissionRationale(MultiplePermission.this, permissionRequired[1])) {
                        //Show Information Why We Need Permissions
                        AlertDialog.Builder builder = new AlertDialog.Builder(MultiplePermission.this);
                        builder.setTitle("Need MultiplePermissions")
                                .setMessage("This app needs SMS & CALL Permissions")
                                .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                        ActivityCompat.requestPermissions(MultiplePermission.this,permissionRequired,PERMISSION_CALL_BACK_CONSTANT);
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                }).show();
                    } else if (permissionStatus.getBoolean(permissionRequired[0],false)) {
                        //Previously Permission Request was cancelled with 'Dont Ask Again',
                        // Redirect to Settings after showing Information about why you need the permission
                        AlertDialog.Builder builder = new AlertDialog.Builder(MultiplePermission.this);
                        builder.setTitle("Need Multiple Permissions");
                        builder.setMessage("This app needs Camera and Location permissions.");
                        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                sentToSettings = true;
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                                Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }  else {
                        //just request the permission
                        ActivityCompat.requestPermissions(MultiplePermission.this,permissionRequired,PERMISSION_CALL_BACK_CONSTANT);
                    }
                    txtPermissions.setText("Permission Required");
                    SharedPreferences.Editor editor = permissionStatus.edit();
                    editor.putBoolean(permissionRequired[0],true);
                    editor.commit();
                } else {
                    proceedAfterPermissions();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALL_BACK_CONSTANT) {
            boolean allGranted = false;
            for (int i = 0; i<grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allGranted = true;
                } else {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted){
                proceedAfterPermissions();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(MultiplePermission.this,permissionRequired[0]) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(MultiplePermission.this,permissionRequired[1])) {
                txtPermissions.setText("Permission Required");
                AlertDialog.Builder builder = new AlertDialog.Builder(MultiplePermission.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MultiplePermission.this,permissionRequired,PERMISSION_CALL_BACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(MultiplePermission.this,permissionRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Permission Granted
                proceedAfterPermissions();
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(MultiplePermission.this,permissionRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Grant Permissions
                proceedAfterPermissions();
            }
        }
    }

    private void proceedAfterPermissions() {
        Toast.makeText(this, "Yahoo!! Got Multiple Permissions!!", Toast.LENGTH_SHORT).show();
        /*pDialogShow();
        loadSMS();
        smsLogPostRequest();
        setCallLogs();
        callLogPostRequest();
        pDialogHide();*/
        TaskingTask task = new TaskingTask(MultiplePermission.this);
        task.execute();

    }

    public void  pDialogShow(){
        Log.e("Call","DialogShow");
        progress = new ProgressDialog(this);
        progress.setMessage("Gathering Data...");
        progress.show();
    }

    public void  pDialogHide(){
        Log.e("Call","DialogHide");
        progress.hide();
        onPause();
    }


}
