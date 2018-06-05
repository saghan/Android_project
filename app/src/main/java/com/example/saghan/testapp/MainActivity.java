package com.example.saghan.testapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private TextView tvfirst_name, tvlast_namee, tvfull_name, tvEmail,helloWorld;
    private CallbackManager callbackManager;
    LoginButton login_button;
    String email,name,uid;
    private ImageView profile_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        Intent intent=getIntent();
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

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
                //MediaStore.Images.Media.insertImage(getContentResolver(), yourBitmap, yourTitle , yourDescription);
                //https://stackoverflow.com/questions/10920396/saving-facebook-profile-picture-and-username-android?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
                //URL image_value= new URL("http://graph.facebook.com/" + userName + "/picture" );
                button_get_friends.setClickable(true);
                ProfilePictureView profilePictureView;

                profilePictureView = (ProfilePictureView) findViewById(R.id.friendProfilePicture);
                ImageView fbimg=(ImageView)profilePictureView.getChildAt(0);
                Bitmap bitmap=((BitmapDrawable)fbimg.getDrawable()).getBitmap();
                 uid=AccessToken.getCurrentAccessToken().getUserId();
                profilePictureView.setProfileId(uid);
                profile_pic=(ImageView) findViewById(R.id.profile_pic);
                profile_pic.setImageBitmap(bitmap);
                int smile_id=getResources().getIdentifier("drawable/img_smile.jpg",null,null);
                profile_pic.setImageResource(smile_id);

                helloWorld.setText("login success "+uid );
                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "fb_prof" , "fb prof");

                String filename = "pic_file";
                String fileContents = "Hello world!";
                FileOutputStream outputStream;

                try {
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }


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
    intent.putExtra("Bitmap",profile_pic.getDrawingCache());
//    Bundle bundle=new Bundle();
//    bundle.putInt("image",R.id.profile_pic);
    startActivity(intent);
    //asa

}


    }




