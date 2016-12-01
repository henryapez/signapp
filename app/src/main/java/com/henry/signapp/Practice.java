package com.henry.signapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.daprlabs.aaron.swipedeck.SwipeDeck;
import com.bumptech.glide.Glide;
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
    private GestureDetector gestureDetector;
    private ImageView v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        //https://android-arsenal.com/details/1/2970

        WebView wv = (WebView) findViewById(R.id.signGif);
        wv.loadUrl("http://www.lifeprint.com/asl101/gifs-animated/all.gif");
        wv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        //Get ImageView in Practice
               v = (ImageView) findViewById(R.id.imageVieww);

       // Glide.with(this).load("http://www.lifeprint.com/asl101/gifs-animated/all.gif").fitCenter().into(v);
//        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener());
//        v.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (gestureDetector.onTouchEvent(event)) {
//                    return true;
//                }
//                else {
//                    return false;
//                }
//            }
//        });
//        v.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


        //mGifsRef = new Firebase("https://signapp-aab9e.firebaseio.com/gifs");

    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event)
//    {
//        if (gestureDetector.onTouchEvent(event))
//        {
//            return true;
//        }
//        else
//        {
//            return false;
//        }
//    }



    //show answers button listener
    public void showAnswersClicked(View view){
        //Glide.with(this).load("http://www.lifeprint.com/asl101/gifs-animated/all.gif").fitCenter().into(v);
//        TextView answers = (TextView) findViewById(R.id.answersTxt);
//        answers.setText("Answer1\nAnswer2\nAnswer3\nAnswer4 \n");
//        answers.setVisibility(View.VISIBLE);
    }
}










//        String gifs = "100,";
//
//        //649 TERMS TOTAL
//        String[] array =  gifs.split(",");
//        Toast.makeText(this, array.length + "   " + array[0], Toast.LENGTH_SHORT).show();
//        for(int i=0; i<array.length; i++){
//            if(!array[i].contains(".") && !array[i].contains("#")
//                    && !array[i].contains("$") && !array[i].contains("[") && !array[i].contains("]")){
//                Firebase childRef = mGifsRef.child(array[i]);
//                childRef.setValue(array[i] + ".gif");
//            }
//
//
//        }
