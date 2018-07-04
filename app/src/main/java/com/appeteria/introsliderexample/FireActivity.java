package com.appeteria.introsliderexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.widget.TextView;
import android.widget.Toast;

import com.appeteria.introsliderexample.Classes.Config;
import com.appeteria.introsliderexample.Classes.NotificationUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.w3c.dom.Text;

import java.util.Random;

public class FireActivity extends AppCompatActivity {

    private  static  final String TAG = FireActivity.class.getSimpleName();
    private BroadcastReceiver registrationBroadcastReciever;
    private TextView txtRegID, txtMessage;
    private String token = FirebaseInstanceId.getInstance().getToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire);

     txtRegID = (TextView) findViewById(R.id.txt_reg_id);
     txtMessage = (TextView) findViewById(R.id.txt_push_message);

     registrationBroadcastReciever = new BroadcastReceiver() {
         @Override
         public void onReceive(Context context, Intent intent) {
             if (intent.getAction().equals(Config.REG_COMPLETE)) {
                 FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                 displayFRegID();
             } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                 String message = intent.getStringExtra("message");
                 Toast.makeText(context,"Notification"+ message,Toast.LENGTH_LONG).show();
                 txtMessage.setText(message);
             }
         }
     };

        displayFRegID();

    }
    private void displayFRegID() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_OREF,MODE_PRIVATE);
        String regId = pref.getString("regId",null);

        if (regId == null)
            Log.e(TAG,"Firebase Reg ID: "+token);
        else
            Log.e(TAG,"Firebase Reg ID: "+regId);

        if (!TextUtils.isEmpty(regId))
            txtRegID.setText("Firebase Reg Id: "+regId);
        else
            txtRegID.setText(token);
        Random random = new Random(5);
        int a = random.nextInt();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(String.valueOf(a)).setValue(token);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(registrationBroadcastReciever,
                new IntentFilter(Config.REG_COMPLETE));

        LocalBroadcastManager.getInstance(this).registerReceiver(registrationBroadcastReciever,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        //NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(registrationBroadcastReciever);
        super.onPause();

    }
}
