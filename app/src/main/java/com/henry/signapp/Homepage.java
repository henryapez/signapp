package com.henry.signapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;

public class Homepage extends AppCompatActivity {
    private FirebaseAuth auth;


    //list arrays for testing
    private String[] decks = {"Alphabet & Numbers", "Shapes", "Colors"};
    private String[] alphabet = {"A", "B", "C", "D"};
    private ArrayList<String> decksList = new ArrayList<String>(Arrays.asList(decks));
    private ArrayList<String> alphabetList = new ArrayList<String>(Arrays.asList(alphabet));

    private Button firebaseButton;
    private Firebase mRef;
    private Firebase mGifsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Firebase.setAndroidContext(this);

        auth = FirebaseAuth.getInstance();
        String user = auth.getCurrentUser().getEmail();
        TextView welcome = (TextView) findViewById(R.id.welcomeText);
        welcome.setText("Welcome " + user);
        final ListView deckListView = (ListView) findViewById(R.id.decks);

        mRef = new Firebase("https://signapp-aab9e.firebaseio.com/");
        mGifsRef = new Firebase("https://signapp-aab9e.firebaseio.com/gifs");
        ArrayAdapter deckAdapter = new ArrayAdapter<String>(this, android.R.layout.activity_list_item,
                android.R.id.text1, decksList);
        deckListView.setAdapter(deckAdapter);


       // storeGifsInDB();




    }




    //practice button listener
    public void practiceClicked(View view){
        Intent practiceIntent = new Intent(this, Practice.class);

        startActivity(practiceIntent);
    }

    //logout button listener
    public void logout(View view){
        auth.signOut();
        finish();
    }

    private void storeGifsInDB() {
        String[][] values = new String[][]{{	"Animals","animal","Animal","0","0"	},
                {	"Animals","bat-flying","Bat (Flying)","0","0"	},
                {	"Animals","bat-vampire","Bat (Vampire)","0","0"	},
                {	"Animals","bear","Bear","0","0"	},
                {	"Animals","bee","Bee","0","0"	},
                {	"Animals","cat","Cat","0","0"	},
                {	"Animals","catb","Cat","0","0"	},
                {	"Animals","cow","Cow","0","0"	},
                {	"Animals","dinosaur","Dinosaur","0","0"	},
                {	"Animals","dog-d-o-g","Dog","0","0"	},
                {	"Animals","doga","Dog","0","0"	},
                {	"Animals","dogb","Dog","0","0"	},
                {	"Animals","donkey","Donkey","0","0"	},
                {	"Animals","eagle","Eagle","0","0"	},
                {	"Animals","elephant","Elephant","0","0"	},
                {	"Animals","elephantversion2","Elephant","0","0"	},
                {	"Animals","fish","Fish","0","0"	},
                {	"Animals","pet","Pet","0","0"	},
                {	"Commands","accept","Accept","0","0"	},
                {	"Commands","become","Become","0","0"	},
                {	"Commands","begin","Begin","0","0"	},
                {	"Commands","calm-down","Calm Down","0","0"	},
                {	"Commands","careful","Careful","0","0"	},
                {	"Commands","chase","Chase","0","0"	},
                {	"Commands","chase2","Chase","0","0"	},
                {	"Commands","cheat","Cheat","0","0"	},
                {	"Commands","climb","Climb","0","0"	},
                {	"Commands","collect","Collect","0","0"	},
                {	"Commands","connect","Connect","0","0"	},
                {	"Commands","contact","Contact","0","0"	},
                {	"Commands","destroy","Destroy","0","0"	},
                {	"Commands","dig","Dig","0","0"	},
                {	"Commands","dig2","Dig","0","0"	},
                {	"Commands","discuss","Discuss","0","0"	},
                {	"Commands","enter","Enter","0","0"	},
                {	"Commands","excuse","Excuse","0","0"	},
                {	"Commands","explain","Explain","0","0"	},
                {	"Commands","fail","Fail","0","0"	},
                {	"Commands","failb","Fail","0","0"	},
                {	"Commands","failc","Fail","0","0"	},
                {	"Commands","faild","Fail","0","0"	},
                {	"Commands","fallbehind","Fall Behind","0","0"	},
                {	"Commands","fall-down","Fall Down","0","0"	},
                {	"Commands","fallinlove","Fall In Love","0","0"	},
                {	"Commands","feed","Feed","0","0"	},
                {	"Commands","find","Find","0","0"	},
                {	"Commands","follow","Follow","0","0"	},
                {	"Commands","force","Force","0","0"	},
                {	"Commands","forgeta","Forget","0","0"	},
                {	"Commands","forgetb","Forget","0","0"	},
                {	"Commands","get","Get","0","0"	},
                {	"Commands","stand-getup","Get Up","0","0"	},
                {	"Commands","give-givetoeverybody","Give To Everybody","0","0"	},
                {	"Commands","give-himgivetome","Give To Me","0","0"	},
                {	"Commands","give-up","Give Up","0","0"	},
                {	"Commands","gomakeyourbed","Go Make Your Bed","0","0"	},
                {	"Commands","give-handtoeachone","Hand To Each One","0","0"	},
                {	"Commands","hit","Hit","0","0"	},
                {	"Commands","increase","Increase","0","0"	},
                {	"Commands","laugh","Laugh","0","0"	},
                {	"Commands","laydown","Laydown","0","0"	},
                {	"Commands","listen","Listen","0","0"	},
                {	"Commands","lookat-that","Look At That","0","0"	},
                {	"Commands","lookat","Lookat","0","0"	},
                {	"Commands","loud","Loud","0","0"	},
                {	"Commands","make","Make","0","0"	},
                {	"Commands","bedmake","Make The Bed","0","0"	},
                {	"Commands","give-megivehim","Me Give To Him","0","0"	},
                {	"Commands","give-megiveyou","Me Give To You","0","0"	},
                {	"Commands","move","Move","0","0"	},
                {	"Commands","no","No","0","0"	},
                {	"Commands","not","Not","0","0"	},
                {	"Commands","put-pack","Pack","0","0"	},
                {	"Commands","play","Play","0","0"	},
                {	"Commands","put","Put","0","0"	},
                {	"Commands","put-away","Put Away","0","0"	},
                {	"Commands","shutup","Shutup","0","0"	},
                {	"Commands","sit","Sit","0","0"	},
                {	"Commands","put-sortout","Sort Out","0","0"	},
                {	"Commands","speak","Speak","0","0"	},
                {	"Commands","spread","Spread","0","0"	},
                {	"Commands","stand-up","Stand Up","0","0"	},
                {	"Commands","call-summon","Summon","0","0"	},
                {	"Commands","talk","Talk","0","0"	},
                {	"Commands","tell","Tell","0","0"	},
                {	"Commands","tell-him","Tell Him","0","0"	},
                {	"Commands","tell-me","Tell Me","0","0"	},
                {	"Commands","throw","Throw","0","0"	},
                {	"Commands","put-inslot","To Put In A Slot","0","0"	},
                {	"Commands","trade","Trade","0","0"	},
                {	"Commands","turn","Turn","0","0"	},
                {	"Commands","visit","Visit","0","0"	},
                {	"Commands","wake-up","Wake Up","0","0"	},
                {	"Commands","walk","Walk","0","0"	},
                {	"Commands","walkto","Walk To","0","0"	},
                {	"Commands","yell","Yell","0","0"	},
                {	"Commands","give-yougiveme","You Give To Me","0","0"	},
                {	"Family & People","accountantag","Accountant","0","0"	},
                {	"Family & People","actor","Actor","0","0"	},
                {	"Family & People","american","American","0","0"	},
                {	"Family & People","americanindian","American Indian","0","0"	},
                {	"Family & People","artist","Artist","0","0"	},
                {	"Family & People","boss","Boss","0","0"	},
                {	"Family & People","bother","Bother","0","0"	},
                {	"Family & People","inlawbrother","Brother Inlaw","0","0"	},
                {	"Family & People","inlawbrotherb","Brother Inlaw","0","0"	},
                {	"Family & People","carpenter","Carpenter","0","0"	},
                {	"Family & People","child","Child","0","0"	},
                {	"Family & People","children","Children","0","0"	},
                {	"Family & People","childrentwohanded","Children (Two Handed)","0","0"	},
                {	"Family & People","chineseold","Chinese","0","0"	},
                {	"Family & People","daughter","Daughter","0","0"	},
                {	"Family & People","daughterb","Daughter","0","0"	},
                {	"Family & People","doctor","Doctor","0","0"	},
                {	"Family & People","farmer","Farmer","0","0"	},
                {	"Family & People","fireman","Fireman","0","0"	},
                {	"Family & People","firemanb","Fireman","0","0"	},
                {	"Family & People","himself","Himself","0","0"	},
                {	"Family & People","inlaw","Inlaw","0","0"	},
                {	"Family & People","girl-littlegirl","Little Girl","0","0"	},
                {	"Family & People","magician","Magician","0","0"	},
                {	"Family & People","manversiona","Man","0","0"	},
                {	"Family & People","manversionb","Man","0","0"	},
                {	"Family & People","manversionc","Man","0","0"	},
                {	"Family & People","myself","Myself","0","0"	},
                {	"Family & People","neighbor","Neighbor","0","0"	},
                {	"Family & People","neighborb","Neighbor","0","0"	},
                {	"Family & People","people","People","0","0"	},
                {	"Family & People","police","Police","0","0"	},
                {	"Family & People","policeb","Police","0","0"	},
                {	"Family & People","president","President","0","0"	},
                {	"Family & People","scientist","Scientist","0","0"	},
                {	"Family & People","scientistb","Scientist","0","0"	},
                {	"Family & People","secretary","Secretary","0","0"	},
                {	"Family & People","secretarya","Secretary","0","0"	},
                {	"Family & People","secretaryc","Secretary","0","0"	},
                {	"Family & People","singer","Singer","0","0"	},
                {	"Family & People","singer2","Singer","0","0"	},
                {	"Family & People","inlawsister","Sister Inlaw","0","0"	},
                {	"Family & People","son","Son","0","0"	},
                {	"Family & People","sonb","Son","0","0"	},
                {	"Family & People","sophomore","Sophomore","0","0"	},
                {	"Family & People","brother-step","Step Brother","0","0"	},
                {	"Family & People","dad-step","Step Dad","0","0"	},
                {	"Family & People","father-step","Step Father","0","0"	},
                {	"Family & People","mother-step","Step Mother","0","0"	},
                {	"Family & People","sister-step","Step Sister","0","0"	},
                {	"Family & People","waitress","Waitress","0","0"	},
                {	"Leisure Activities","bake","Bake","0","0"	},
                {	"Leisure Activities","ball","Ball","0","0"	},
                {	"Leisure Activities","baseball","Baseball","0","0"	},
                {	"Leisure Activities","basketball","Basketball","0","0"	},
                {	"Leisure Activities","bat","Bat","0","0"	},
                {	"Leisure Activities","book","Book","0","0"	},
                {	"Leisure Activities","camera","Camera","0","0"	},
                {	"Leisure Activities","computer","Computer","0","0"	},
                {	"Leisure Activities","cook","Cook","0","0"	},
                {	"Leisure Activities","fishing","Fishing","0","0"	},
                {	"Leisure Activities","hike","Hike","0","0"	},
                {	"Leisure Activities","hockey","Hockey","0","0"	},
                {	"Leisure Activities","internet","Internet","0","0"	},
                {	"Leisure Activities","internetb","Internet","0","0"	},
                {	"Leisure Activities","movie","Movie","0","0"	},
                {	"Leisure Activities","music","Music","0","0"	},
                {	"Leisure Activities","poetry","Poetry","0","0"	},
                {	"Leisure Activities","race","Race","0","0"	},
                {	"Leisure Activities","read","Read","0","0"	},
                {	"Leisure Activities","rollerblade","Rollerblade","0","0"	},
                {	"Leisure Activities","run","Run","0","0"	},
                {	"Leisure Activities","run2","Run","0","0"	},
                {	"Leisure Activities","skate","Skate","0","0"	},
                {	"Leisure Activities","picture-take-a","Take A Picture","0","0"	},
                {	"Leisure Activities","team","Team","0","0"	},
                {	"Leisure Activities","volunteer","Volunteer","0","0"	},
                {	"Numbers","23","23","0","0"	},
                {	"Numbers","25","25","0","0"	},
                {	"Numbers","34","34","0","0"	},
                {	"Numbers","35","35","0","0"	},
                {	"Numbers","40s","40","0","0"	},
                {	"Numbers","100","100","0","0"	},
                {	"Numbers","200","200","0","0"	},
                {	"Numbers","300","300","0","0"	},
                {	"Numbers","400","400","0","0"	},
                {	"Numbers","500","500","0","0"	},
                {	"Numbers","600","600","0","0"	},
                {	"Numbers","700","700","0","0"	},
                {	"Numbers","800","800","0","0"	},
                {	"Numbers","900","900","0","0"	},
                {	"Numbers","2000","2000","0","0"	},
                {	"Numbers","2397","2397","0","0"	},
                {	"Numbers","100c","1 Hundred","0","0"	},
                {	"Numbers","1000000","1 Million","0","0"	},
                {	"Numbers","1000b","1 Thousand","0","0"	},
                {	"Numbers","1000r","1 Thousand","0","0"	},
                {	"Places","africaa","Africa","0","0"	},
                {	"Places","africab","Africa","0","0"	},
                {	"Places","arizona","Arizona","0","0"	},
                {	"Places","atlanta","Atlanta","0","0"	},
                {	"Places","boston","Boston","0","0"	},
                {	"Places","boston2","Boston","0","0"	},
                {	"Places","chicago","Chicago","0","0"	},
                {	"Places","chinanew","China","0","0"	},
                {	"Places","country","Country","0","0"	},
                {	"Places","dallas","Dallas","0","0"	},
                {	"Places","dallasuncommon","Dallas","0","0"	},
                {	"Places","detroit","Detroit","0","0"	},
                {	"Places","earth","Earth","0","0"	},
                {	"Places","europe","Europe","0","0"	},
                {	"Places","german","German","0","0"	},
                {	"Places","ireland","Ireland","0","0"	},
                {	"Places","island","Island","0","0"	},
                {	"Places","nation","Nation","0","0"	},
                {	"Places","newyork","Newyork","0","0"	},
                {	"Places","ocean","Ocean","0","0"	},
                {	"Places","pittsburg","Pittsburg","0","0"	},
                {	"Places","river","River","0","0"	},
                {	"Places","russia","Russia","0","0"	},
                {	"Places","spain","Spain","0","0"	},
                {	"Places","texas","Texas","0","0"	},
                {	"Places","world","World","0","0"	},
                {	"Shapes","large","Large","0","0"	},
                {	"Shapes","small","Small","0","0"	},
                {	"Shapes","small-b","Small","0","0"	},
                {	"Shapes","small-c","Small","0","0"	},
                {	"Time","oclock-1","1 Oclock","0","0"	},
                {	"Time","oclock3","3 Oclock","0","0"	},
                {	"Time","week-3weeks","3 Weeks","0","0"	},
                {	"Time","year-3years","3 Years","0","0"	},
                {	"Time","annually","Annually","0","0"	},
                {	"Time","annually2","Annually","0","0"	},
                {	"Time","christmas","Christmas","0","0"	},
                {	"Time","christmas2","Christmas","0","0"	},
                {	"Time","easter","Easter","0","0"	},
                {	"Time","laborday","Laborday","0","0"	},
                {	"Time","past","Past","0","0"	},
                {	"Time","postpone","Postpone","0","0"	},
                {	"Time","month-sixmonths","Six Months","0","0"	},
                {	"Time","thanksgiving","Thanksgiving","0","0"	},
                {	"Time","then","Then","0","0"	},
                {	"Time","time","Time","0","0"	}};
        Firebase mRefChild;
        for(int i=0; i<values.length; i++){
            mRef = new Firebase("https://signapp-aab9e.firebaseio.com/categories/"+values[i][0]+"/"+values[i][1]);
            mRefChild = mRef.child("title");
            mRefChild.setValue(values[i][2]);
        }
    }

}
