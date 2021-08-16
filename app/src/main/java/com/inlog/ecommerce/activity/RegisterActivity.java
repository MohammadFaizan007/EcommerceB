package com.inlog.ecommerce.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
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

import org.json.JSONException;
import org.json.JSONObject;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;


public class RegisterActivity extends AppCompatActivity {

    CircularProgressButton cirRegisterButton;
    EditText editTextName,editTextEmail,editTextMobile,editTextPassword;
    TextInputLayout textInputName,textInputEmail, textInputMobile,textInputPassword;
    boolean isNameValid,isEmailValid, isPasswordValid,isMobielValid;
    private RelativeLayout coordinatorLayout;
    String responsefinal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();
        cirRegisterButton = findViewById(R.id.cirRegisterButton);
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextMobile = findViewById(R.id.editTextMobile);
        editTextPassword = findViewById(R.id.editTextPassword);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
    }
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }
    public void onRegisterClick(View view){
        SetValidation();
    }
    public void SetValidation() {
        // Check for a valid email address.
        if (editTextName.getText().toString().isEmpty()) {
            snacks_message(getResources().getString(R.string.name_error));
        }
        else if (editTextEmail.getText().toString().isEmpty()) {
            snacks_message(getResources().getString(R.string.email_error));
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString()).matches()) {
            snacks_message(getResources().getString(R.string.error_invalid_email));
        }
        else if (editTextMobile.getText().toString().isEmpty()) {
            snacks_message(getResources().getString(R.string.phone_error));
        }
        else if (editTextMobile.getText().toString().length() <10)
        {
            snacks_message(getResources().getString(R.string.phone_error));
        }
        else if (editTextPassword.getText().toString().isEmpty()) {
            snacks_message(getResources().getString(R.string.password_error));
        } else if (editTextPassword.getText().length() < 6) {
            snacks_message(getResources().getString(R.string.error_invalid_password));
        } else  {
            postData();
        }
    }
    public void postData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject object = new JSONObject();
        JSONObject mainobj = new JSONObject();
        try {
            //input your API parameters
            object.put("clientId", GlobalVariables.clientID);
            object.put("name",editTextName.getText().toString().trim());
            object.put("email",editTextEmail.getText().toString().trim());
            object.put("mobile",editTextMobile.getText().toString().trim());
            object.put("password",editTextPassword.getText().toString().trim());
            object.put("image","");
            object.put("filename","");
            mainobj.put("params", object);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, GlobalVariables.COMMON_URL_SERVICE+GlobalVariables.registermethod, mainobj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("String Response : ", response.toString());

                        responsefinal = null;
                        responsefinal = response.toString();
                        final JSONObject jsonObject;
                        if (responsefinal != null) {
                            try {

                                jsonObject = new JSONObject(responsefinal);
                                JSONObject result = jsonObject.getJSONObject("result");
                                if (result.optString("status").equalsIgnoreCase("OK")) {
                                    new Handler().postDelayed(new Runnable() {
                                        @RequiresApi(api = Build.VERSION_CODES.M)
                                        @Override
                                        public void run() {
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                            overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
                                        }
                                    }, GlobalVariables.delattime);
                                }
                                else
                                {
                                    snacks_message(result.getString("msg"));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                   snacks_message(getString(R.string.noresponse));
                                }
                            });
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void onLoginClick(View view){
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
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
