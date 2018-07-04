package com.appeteria.introsliderexample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.appeteria.introsliderexample.Classes.URLs;
import com.appeteria.introsliderexample.Classes.User;
import com.appeteria.introsliderexample.Classes.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    // UI references.
    private EditText emailView;
    private EditText passwordView;
    private TextView progressTest;

    private String email;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        emailView = (EditText) findViewById(R.id.email_editText);
        passwordView = (EditText) findViewById(R.id.password_EditText);
        progressTest = (TextView) findViewById(R.id.progressText);
    }

    public void signin(View view) {
        if (emailView.getText().toString().isEmpty() || passwordView.getText().toString().isEmpty()) {
            Toast.makeText(this, "Empty Field", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("LoginActivity","Login Button Press!!");
            email = emailView.getText().toString().trim();
            password = passwordView.getText().toString().trim();
            registerUser();
        }
        //registerUser();
    }

    private void registerUser() {
        JSONObject postObject = new JSONObject();
        RequestQueue queue =  Volley.newRequestQueue(this);
        JSONObject historyObject = new JSONObject();

        String url ="http://helpinghandbd.org/app/api.php";
        try {
            //historyObject.put("id","1");
            historyObject.put("email",email);
            historyObject.put("password",password);
            postObject.put("user",historyObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("LoginActivityJsonObject",""+postObject);
        JsonObjectRequest objRequest = new JsonObjectRequest(Request.Method.POST, url,postObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("LoginActivity","OnResponse: "+response);
                        Toast.makeText(LoginActivity.this, String.valueOf(response), Toast.LENGTH_LONG).show();
                        try {
                            JSONObject newJsonObject =new JSONObject(String.valueOf(response));

                            Log.e("NewJsonArra",""+newJsonObject);
                            String newmala = newJsonObject.getString("user_email");
                            progressTest.setText(newmala);
                        } catch (JSONException e) {
                            progressTest.setText("Execption: "+e.toString());
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("OnError", String.valueOf(error.getMessage()));
            }
        });

        queue.add(objRequest);

    }
    /*private void registerUser() {
        Log.e("LoginActivity","Register User Method Entry");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
                new Response.Listener<String>() {

                    //Log.e("LoginActivity","Into The StringRequest");
                    @Override
                    public void onResponse(String response) {
                        Log.e("LoginActivity","Response: "+response);
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Log.e("LoginActivity","JsonObject: "+obj);
                            //if no error in response
                            if (!obj.getBoolean("error")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("user");
                                Log.e("LoginActivity","UserJson: "+userJson);
                                //creating a new user object
                                User user = new User(
                                        userJson.getString("email"),
                                        userJson.getString("password")
                                );
                                Log.e("LoginActivity","Creating New User: "+user);
                                *//**//*//**//*//*storing the user in shared preferences
                                SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);*//**//*
                                Toast.makeText(LoginActivity.this, ""+user.toString(), Toast.LENGTH_SHORT).show();
                                //starting the profile activity
                                //finish();
                                //startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                            } else {
                                Log.e("LoginActivity","Else in Response: "+obj.getString("message"));
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("email", email);
                params.put("password", password);
                Log.e("LoginActivityParam",params.toString());
                return params;
            }
        };*//*

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }*/
}

