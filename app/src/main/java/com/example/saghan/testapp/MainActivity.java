package com.example.saghan.testapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
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
        final Button button_get_friends =(Button) findViewById(R.id.button_find_friends);
        button_get_friends.setClickable(false);


        login_button.setReadPermissions(Arrays.asList("public_profile","email"));
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                button_get_friends.setClickable(true);
                ProfilePictureView profilePictureView;

                profilePictureView = (ProfilePictureView) findViewById(R.id.friendProfilePicture);

                String uid=AccessToken.getCurrentAccessToken().getUserId();
                profilePictureView.setProfileId(uid);

                helloWorld.setText("login success "+uid );

                if(Profile.getCurrentProfile()==null)
                {
                    Log.d("mytag","profile null");
                    return;
                }
                String profile_id=Profile.getCurrentProfile().getId();

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

protected  void activity2(View view){
    Intent intent = new Intent(MainActivity.this, Activity2.class);
    startActivity(intent);

}


    }

