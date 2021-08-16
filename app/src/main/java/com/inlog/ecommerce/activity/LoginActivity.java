package com.inlog.ecommerce.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.inlog.ecommerce.R;
import com.inlog.ecommerce.util.GlobalVariables;
import com.inlog.ecommerce.utility.MyProgressBar;
import com.inlog.ecommerce.utility.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    CircularProgressButton login;
    TextView register;
    boolean isEmailValid, isPasswordValid;
    TextInputLayout emailError, passError;
    String responsefinal;
    private RelativeLayout coordinatorLayout;
    JSONArray detailsarray = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //for changing status bar icon colors
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editTextEmail);
        password =  findViewById(R.id.editTextPassword);
        login = findViewById(R.id.cirLoginButton);
        emailError = findViewById(R.id.textInputEmail);
        passError = findViewById(R.id.textInputPassword);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetValidation();
            }
        });

        findViewById(R.id.txtVForgotPasswordId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1= new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);

            }
        });

    }
    public void SetValidation() {
        // Check for a valid email address.
        if (email.getText().toString().isEmpty()) {
            snacks_message(getResources().getString(R.string.email_error));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            snacks_message(getResources().getString(R.string.error_invalid_email));
        }
        else if (password.getText().toString().isEmpty()) {
            snacks_message(getResources().getString(R.string.password_error));
        } else if (password.getText().length() < 6) {
            snacks_message(getResources().getString(R.string.error_invalid_password));
        } else  {
            postData();
        }
    }
    public void postData() {
        MyProgressBar.getInstance().showProgressDialog(this);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        JSONObject mainobj = new JSONObject();
        try {
            //input your API parameters
            object.put("clientId",GlobalVariables.clientID);
            object.put("email",email.getText().toString().trim());
            object.put("mobile",null);
            object.put("password",password.getText().toString().trim());
            mainobj.put("params", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.loginmethod, mainobj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("String Response : ", response.toString());
                        Utility.log("Response = ",response.toString());

                        responsefinal = null;
                        responsefinal = response.toString();
                        final JSONObject jsonObject;
                        if (responsefinal != null) {
                            try {
                                MyProgressBar.getInstance().dismissProgressDialog();
                                jsonObject = new JSONObject(responsefinal);
                                JSONObject result = jsonObject.getJSONObject("result");
                                if (result.optString("status").equalsIgnoreCase("OK")) {

                                    detailsarray = new JSONArray();
                                    detailsarray = result.getJSONArray("result");

                                    JSONObject tempDict = (JSONObject) detailsarray.get(0);
                                    SharedPreferences sharedPref = getSharedPreferences("globalloginvalues", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putInt("globalid", tempDict.getInt("id"));
                                    editor.putString("password", password.getText().toString().trim());
                                    editor.apply();
                                    startActivity(new Intent(LoginActivity.this, mapactivity.class));
                                    overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
                                }
                                else
                                {

                                    snacks_message(result.optString("msg"));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    MyProgressBar.getInstance().dismissProgressDialog();
                                    snacks_message(getString(R.string.noresponse));
                                }
                            });
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyProgressBar.getInstance().dismissProgressDialog();

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    public void onResume(){
        super.onResume();


    }

    public void onLoginactClick(View View){
        startActivity(new Intent(this, RegisterActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
    }
    private void snacks_message(String message) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, message, Snackbar.LENGTH_SHORT)
                .setAction("", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

        snackbar.setActionTextColor(Color.RED);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.YELLOW);

        snackbar.show();

    }
}
