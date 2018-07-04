package com.appeteria.introsliderexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class APITestActivity extends AppCompatActivity {

    TextView checkingTextView;
    String finalUpoadString = "";

    String URL_LOC_HISTORY = "https://www.mindscape.com.bd/api/v1/location/history";
    String URL_USAGE = "https://www.mindscape.com.bd/api/v1/data/usage";
    String URL_PAYMENT = "https://www.mindscape.com.bd/api/v1/user/payment";
    String URL_PROFILE = "https://www.mindscape.com.bd/api/v1/app/get/profile";
    String URL_PROFILE_UP = "https://www.mindscape.com.bd/api/v1/app/get/profile/update";

    JSONObject historyObject;
    JSONArray historyArray;

    RequestQueue queue;
    JsonObjectRequest objRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apitest);

        checkingTextView = (TextView) findViewById(R.id.checkTextView);
        queue =  Volley.newRequestQueue(this);
        historyObject = new JSONObject();
        historyArray = new JSONArray();
    }

    public void uploadBTN(View view) {
        paymentHistoryUpload();
          /*dataUsageUpload();
          locationUpdate();
        */
        //addProfile();

    }

    private void addProfile() {
        JSONObject postObject = new JSONObject();
        Date date = new Date();

        try {
            historyObject.put("gender","male");
            //historyObject.put("date_of_birth", date.getDate());
            historyObject.put("occupation","Student");
            historyObject.put("approximate_household_income","90654");
            postObject.put("user_profile",historyObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        historyArray.put(postObject);

        objRequest = new JsonObjectRequest(Request.Method.POST, URL_PROFILE_UP,postObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("OnResponse",String.valueOf(response));
                        Toast.makeText(APITestActivity.this, String.valueOf(response), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("On Error", String.valueOf(error.getMessage()));
            }
        });

        queue.add(objRequest);

        finalUpoadString = String.valueOf(historyArray);
        checkingTextView.setText(finalUpoadString);
    }

    private void locationUpdate() {
        JSONObject postObject = new JSONObject();
        try {
            historyObject.put("user_id","147");
            historyObject.put("user_mobile", "01925443469");
            historyObject.put("lat","47.3698");
            historyObject.put("lon","90.3654");
            postObject.put("userinfo",historyObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        historyArray.put(postObject);

        objRequest = new JsonObjectRequest(Request.Method.POST, URL_LOC_HISTORY,postObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("OnResponse",String.valueOf(response));
                        Toast.makeText(APITestActivity.this, String.valueOf(response), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("On Error", String.valueOf(error.getMessage()));
            }
        });

        queue.add(objRequest);

        finalUpoadString = String.valueOf(historyArray);
        checkingTextView.setText(finalUpoadString);
    }

    private void dataUsageUpload() {
        JSONObject postObject = new JSONObject();
        try {
            historyObject.put("user_id","47");
            historyObject.put("user_mobile", "01925443469");
            historyObject.put("connectivity","Wifi");
            historyObject.put("type","internet");
            historyObject.put("volume","958763");
            postObject.put("userinfo",historyObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        historyArray.put(postObject);

        objRequest = new JsonObjectRequest(Request.Method.POST, URL_USAGE,postObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("OnResponse",String.valueOf(response));
                        Toast.makeText(APITestActivity.this, String.valueOf(response), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("On Error", String.valueOf(error.getMessage()));
            }
        });

        queue.add(objRequest);

        finalUpoadString = String.valueOf(postObject);
        checkingTextView.setText(finalUpoadString);
    }

    private void paymentHistoryUpload() {
        JSONObject postObject = new JSONObject();
        try {
            historyObject.put("user_id","47");
            historyObject.put("user_mobile", "01710288078");
            historyObject.put("payment_method","Bkash");
            historyObject.put("amount","50");
            postObject.put("userinfo",historyObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        historyArray.put(postObject);

        objRequest = new JsonObjectRequest(Request.Method.POST, URL_PAYMENT,postObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("OnResponse",String.valueOf(response));
                        Toast.makeText(APITestActivity.this, String.valueOf(response), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("On Error", String.valueOf(error.getMessage()));
            }
        });

        queue.add(objRequest);

        finalUpoadString = String.valueOf(postObject);
        checkingTextView.setText(finalUpoadString);
    }
}
