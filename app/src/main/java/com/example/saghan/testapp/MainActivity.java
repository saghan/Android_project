package com.example.saghan.testapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private TextView tvfirst_name, tvlast_namee, tvfull_name, tvEmail,helloWorld;
    private CallbackManager callbackManager;
    LoginButton login_button;
    String email,name,first_name,last_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


        setContentView(R.layout.activity_main);

//        tvfirst_name        = (TextView) findViewById(R.id.first_name);
//        tvlast_namee        = (TextView) findViewById(R.id.last_name);
//        tvfull_name         = (TextView) findViewById(R.id.full_name);
        tvEmail             = (TextView) findViewById(R.id.email);
        login_button        = (LoginButton) findViewById(R.id.login_button);
        helloWorld=(TextView)findViewById(R.id.hello_world);

        login_button.setReadPermissions(Arrays.asList("public_profile","email"));
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                helloWorld.setText("login success");
                login_button.setVisibility(View.GONE);


                GraphRequest graphRequest   =   GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback()
                {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response)
                    {
                        Log.d("JSON", ""+response.getJSONObject().toString());

                        try
                        {
                            email       =   object.getString("email");
//                            name        =   object.getString("name");
//                            first_name  =   object.optString("first_name");
//                            last_name   =   object.optString("last_name");

                            tvEmail.setText(email);
//                            tvfirst_name.setText(first_name);
//                            tvlast_namee.setText(last_name);
//                            tvfull_name.setText(name);
                            LoginManager.getInstance().logOut();
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

                Bundle parameters = new Bundle();
//                parameters.putString("fields", "id,name,first_name,last_name,email");
                parameters.putString("fields", "id,email");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel()
            {

            }

            @Override
            public void onError(FacebookException exception)
            {helloWorld.setText(exception.toString());

            }
        });
//        setContentView(R.layout.activity_main);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
