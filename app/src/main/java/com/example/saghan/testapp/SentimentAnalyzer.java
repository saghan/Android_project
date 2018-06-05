package com.example.saghan.testapp;
//https://stackoverflow.com/questions/10407159/how-to-manage-startactivityforresult-on-android?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public  class SentimentAnalyzer extends AppCompatActivity {
    private Context context;

    public SentimentAnalyzer(Context current){
        this.context = current;
        Log.d("saghan","senti 22");
    }
//    @Override
//    public void onActivityReenter( int resultCode, Intent data) {
//        Log.d("saghan","senti 26");
//        super.onActivityReenter(resultCode, data);
//
//
//            // Make sure the request was successful
////            if (resultCode == RESULT_OK) {
////
////        }
//        Bundle b=getIntent().getExtras();
//        String msg=b.getString("msg");
//        String bow="";
//
//        try {
//            bow=analyzeSentiment(msg);
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//        Intent return_intent=new Intent(this, Activity2.class);
//            Bundle b2=new Bundle();
//            b2.putString("msg_from_sentiment",bow);
//            return_intent.putExtra("result",bow);
//        setResult(this.RESULT_OK,return_intent);
//        finish();
//            return_intent.putExtras(b2);
//
//
//    }



    public String get_pos_words(String msg) throws IOException {
//        if( getApplicationContext()==null)
//        {
//            Log.d("saghan","app context null");
//            return "";
//        }
        Log.d("saghan","senti 62");
        if(this.context==null)
            Log.d("saghan","context null");
        InputStream inputStream = this.context.getResources().openRawResource(this.context.getResources().getIdentifier("positive_words", "raw", this.context.getPackageName()));
        Vector<String> pos_vocab = new Vector<>();
//        AssetManager assetManager = getAssets();
//        InputStream inputStream= assetManager.open("raw/dict.txt",AssetManager.ACCESS_STREAMING);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((inputStream)));
        StringBuffer stringBuffer = new StringBuffer();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            pos_vocab.add(line);
        }
        inputStream.close();
        String only_alpha="";

        msg = msg.toLowerCase();
        String[] msg_split = msg.split(" ");
        for (String x : msg_split) {
            x = x.replaceAll("[^a-z]", "");
            if (pos_vocab.contains(x))
                only_alpha = only_alpha + " " + x;
        }
        Log.d("saghan", only_alpha);


        return only_alpha;

    }

    public String get_neg_words(String msg) throws IOException {
//        if( getApplicationContext()==null)
//        {
//            Log.d("saghan","app context null");
//            return "";
//        }
        Log.d("saghan","senti 62");
        if(this.context==null)
            Log.d("saghan","context null");
        InputStream inputStream = this.context.getResources().openRawResource(this.context.getResources().getIdentifier("negative_words", "raw", this.context.getPackageName()));

        Vector<String> neg_vocab = new Vector<>();
        BufferedReader bufferedReader;

        bufferedReader = new BufferedReader(new InputStreamReader((inputStream)));
        StringBuffer stringBuffer = new StringBuffer();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            neg_vocab.add(line);
        }
        inputStream.close();


        msg = msg.toLowerCase();
        String[] msg_split = msg.split(" ");
        String only_alpha = "";

        for (String x : msg_split) {
            x = x.replaceAll("[^a-z]", "");
            if (neg_vocab.contains(x))
                only_alpha = only_alpha + " " + x;
        }
        Log.d("saghan", only_alpha);


        return only_alpha;

    }

}
