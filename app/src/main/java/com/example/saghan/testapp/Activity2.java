package com.example.saghan.testapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class Activity2 extends AppCompatActivity {

    String msg_field;
     EditText editText_pos, editText_neg;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);
        Intent intent = getIntent();

//        ImageView pic=(ImageView) findViewById(R.id.profile_pic_activity2);
        editText_pos=(EditText)findViewById(R.id.editText_pos_word);
        editText_neg=(EditText)findViewById(R.id.editText_neg_word);
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        Log.d("saghan",""+cursor.getColumnCount());
        String colnames="";
        // public static final String INBOX = "content://sms/inbox";
// public static final String SENT = "content://sms/sent";
// public static final String DRAFT = "content://sms/draft";
        String msgData = "";
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {

                for(int idx=0;idx<cursor.getColumnCount();idx++)
                {
//                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
                    msgData += " " + ":" + cursor.getString(idx);

                }
                // use msgData

            } while (cursor.moveToNext());

//            editText.setText(msgData);
            Log.d("saghan","activity2 64");

            Intent intent2 = new Intent(this, SentimentAnalyzer.class);
            Bundle b = new Bundle();
            b.putString("msg", msgData);
            intent2.putExtras(b);
//            startActivityForResult(intent2,1);
            SentimentAnalyzer sentimentAnalyzer=new SentimentAnalyzer(this);

            try {
                String pos_words=sentimentAnalyzer.get_pos_words(msgData);
                pos_words = pos_words.replaceAll("\\s+", " ");
                editText_pos.setText(pos_words);
                String neg_words=sentimentAnalyzer.get_neg_words(msgData);
                neg_words = neg_words.replaceAll("\\s+", " ");
                editText_neg.setText(neg_words);
                int pos_count=pos_words.split(" ").length;
                int neg_count=neg_words.split(" ").length;
                Toast mytoast = new Toast(this);
                mytoast=Toast.makeText(this,"",Toast.LENGTH_LONG);
                if(pos_count>neg_count)
                    mytoast.setText("positive sentiment overall");
                else
                    mytoast.setText("negative sentiment overall");
                mytoast.show();


            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // empty box, no SMS
        }


        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {


                //From the received text string you may do string operations to get the required OTP
                //It depends on your SMS format
                Log.e("Message",messageText);
                Toast.makeText(Activity2.this,"Message: "+messageText,Toast.LENGTH_LONG).show();
//                editText.setText(messageText);



            }
        });



    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (requestCode == 1) {
//            if(resultCode == SentimentAnalyzer.RESULT_OK){
//                String result=data.getStringExtra("result");
//                editText.setText(result);
//            }
//            if (resultCode == SentimentAnalyzer.RESULT_CANCELED) {
//                //Write your code if there's no result
//            }
//        }
//    }

}
