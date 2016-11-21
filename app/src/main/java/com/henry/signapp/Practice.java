package com.henry.signapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Practice extends AppCompatActivity {
    private Button firebaseButton;
    private Firebase mRef;
    private Firebase mGifsRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);


        WebView wv = (WebView) findViewById(R.id.signGif);
        wv.loadUrl("http://www.lifeprint.com/asl101/gifs-animated/all.gif");

        mGifsRef = new Firebase("https://signapp-aab9e.firebaseio.com/gifs");


        String gifs = "100,";
               
        //649 TERMS TOTAL
        String[] array =  gifs.split(",");
        Toast.makeText(this, array.length + "   " + array[3], Toast.LENGTH_SHORT).show();
        for(int i=0; i<array.length; i++){
            if(!array[i].contains(".") && !array[i].contains("#")
                    && !array[i].contains("$") && !array[i].contains("[") && !array[i].contains("]")){
                Firebase childRef = mGifsRef.child(array[i]);
                childRef.setValue(array[i] + ".gif");
            }


        }
    }

    //show answers button listener
    public void showAnswersClicked(View view){
        TextView answers = (TextView) findViewById(R.id.answersTxt);
        answers.setText("Answer1\nAnswer2\nAnswer3\nAnswer4 \n");
        answers.setVisibility(View.VISIBLE);
    }
}
