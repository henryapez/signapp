package com.henry.signapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.daprlabs.aaron.swipedeck.SwipeDeck;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardSwiping extends AppCompatActivity {
    private ArrayList<String> gifDeck;
    private SwipeDeck cardStack;
    private SwipeDeckAdapter adapter;
    private Context context = this;
    //Reference to User's gif Firebase
    private FirebaseHelper db = FirebaseHelper.getInstance();
    //Current User references
    private FirebaseAuth auth;
    private DatabaseReference mUserGifsRef;
    //Firebase database reference for queries
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String useremail, username;
    float x1,x2;
    float y1, y2;
    private static final String TAG = "CardSwiping";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_swiping);


        auth = FirebaseAuth.getInstance();

        useremail = auth.getCurrentUser().getEmail();
        username = useremail.split("@")[0];
        cardStack = (SwipeDeck) findViewById(R.id.swipe_deck);
        mUserGifsRef = db.getRef("users/"+username+"/myDeck");
        DatabaseReference ref = database.getReference("users/"+username+"/gifs");
        gifDeck = getIntent().getStringArrayListExtra("userGif_list");


//        ref.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
//                //System.out.println("MASTERED: " + Boolean.toString(newGif.mastered));
//                System.out.println("ID: " + prevChildKey);
//                gifDeck.add(dataSnapshot.getKey());
//            }
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
//            }
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//            }
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//            }
//        });





        System.out.println("SIZE OF TESTDATA IS " + gifDeck.size());
        //Set the Swipe card adapter. Shuffle gifs before sending them to the View
        Collections.shuffle(gifDeck);
        adapter = new SwipeDeckAdapter(gifDeck, this);
        if(cardStack != null){
            cardStack.setAdapter(adapter);
        }
        cardStack.setCallback(new SwipeDeck.SwipeDeckCallback() {
            @Override
            public void cardSwipedLeft(long positionInAdapter) {
                Log.i("MainActivity", "card was swiped left, position in adapter: " + positionInAdapter);
            }

            @Override
            public void cardSwipedRight(long positoinInAdapter) {
                Log.i("MainActivity", "card was swiped right, position in adapter: " + positoinInAdapter);

            }
        });



        //example of buttons triggering events on the deck
        Button btn = (Button) findViewById(R.id.knowIt);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStack.swipeTopCardLeft(180);
            }
        });
        Button btn2 = (Button) findViewById(R.id.practiceIt);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardStack.swipeTopCardRight(180);
            }
        });

        Button checkAnswer = (Button) findViewById(R.id.checkAnswer);
        checkAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gifDeck.add("a sample string.");
                adapter.notifyDataSetChanged();
            }
        });




    }

    // onTouchEvent () method gets called when User performs any touch event on screen
    // Handled left and right swipe ONLY when one card is present
    //If multiple card are present, there are multiple View layers. Touch events are detected in the SwipeDeckAdapter
    @Override
    public boolean onTouchEvent(MotionEvent touchevent)
    {
        switch (touchevent.getAction()) {
            // when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN: {
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;}
            case MotionEvent.ACTION_UP: {
                x2 = touchevent.getX();
                y2 = touchevent.getY();
                //if Right swipe when only one card is visible
                if (x1 < x2) {
                   // cardStack.swipeTopCardRight(180);
                    Toast.makeText(this, "Left to Right Swap Performed", Toast.LENGTH_LONG).show();
                }
                //Left swipe when only one card is visible
                if (x1 > x2) {
                  ///  cardStack.swipeTopCardLeft(180);
                    Toast.makeText(this, "Right to Left Swap Performed", Toast.LENGTH_LONG).show();
                }
                // if UP to Down sweep event on screen
                if (y1 < y2) {
                    Toast.makeText(this, "UP to Down Swap Performed", Toast.LENGTH_LONG).show();
                }
                //if Down to UP sweep event on screen
                if (y1 > y2) {
                    Toast.makeText(this, "Down to UP Swap Performed", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
        return false;
    }

    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;
        private String currentGif;
        public OnSwipeTouchListener (Context ctx, String selectedGif){
            gestureDetector = new GestureDetector(ctx, new GestureListener());
            currentGif = selectedGif;
        }
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
            private static final int SWIPE_THRESHOLD = 20;
            private static final int SWIPE_VELOCITY_THRESHOLD = 20;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                        }
                        result = true;
                    }
                    else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                    }
                    result = true;

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }
        public void onSwipeRight() {

            Toast.makeText(context, "Right Swipe detected adding " + currentGif, Toast.LENGTH_LONG).show();

            //Get top card gif and keep it in the deck to view later
            gifDeck.add(currentGif);
            cardStack.swipeTopCardRight(180);
            adapter.notifyDataSetChanged();
        }
        public void onSwipeLeft() {
            Toast.makeText(context, "Left Swipe detected mastering " + currentGif, Toast.LENGTH_LONG).show();

            mUserGifsRef.child(currentGif).child("mastered").setValue(true);
            cardStack.swipeTopCardLeft(180);

        }
        public void onSwipeTop() {
        }
        public void onSwipeBottom() {
        }
    } // END SWIPE CLASS Listener

    public class SwipeDeckAdapter extends BaseAdapter {
        private View vv;
        private List<String> data;
        private Context context;
        private int position;

        public SwipeDeckAdapter(List<String> data, Context context) {
            this.data = data;
            this.context = context;
        }

        public int getPosition(){
            return position;
        }
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = convertView;
            vv = v;
            if (v == null) {
                LayoutInflater inflater = getLayoutInflater();
                // normally use a viewholder
                v = inflater.inflate(R.layout.test_card2, parent, false);
            }
            this.position = position;
            ImageView imageView = (ImageView) v.findViewById(R.id.offer_image);
            //Picasso.with(context).load(R.drawable.food).fit().centerCrop().into(imageView);
            Glide.with(context).load("http://www.lifeprint.com/asl101/gifs-animated/"+data.get(position)+".gif").into(imageView);
            TextView textView = (TextView) v.findViewById(R.id.sample_text);
           // TextView masteredText = (TextView) v.findViewById(R.id.left_image);
            // TextView practiceText = (TextView) v.findViewById(R.id.right_image);
            String item = (String)getItem(position);
            //masteredText.setText("Mastered Sign");
            //practiceText.setText("Keep Practicing");
            textView.setText(item);

            v.setOnTouchListener(new OnSwipeTouchListener(context, item) {
                String answer ="";
                public void onSwipeTop() {
                    answer = "top";
                    Log.i("Hardware Accel type:", answer);
                    Toast.makeText(context, "gggggg", Toast.LENGTH_LONG).show();

                }

                public void onSwipeBottom() {
                    answer = "bottom";
                    Log.i("Hardware Accel type:", answer);
                    Toast.makeText(context, "gggggg", Toast.LENGTH_LONG).show();

                }
            });

            imageView.setOnTouchListener(new OnSwipeTouchListener(context, item) {
                String answer ="";
                public void onSwipeTop() {
                    answer = "top";
                    Log.i("Hardware Accel type:", answer);
                    Toast.makeText(context, "gggggg", Toast.LENGTH_LONG).show();

                }

                public void onSwipeBottom() {
                    answer = "bottom";
                    Log.i("Hardware Accel type:", answer);
                    Toast.makeText(context, "gggggg", Toast.LENGTH_LONG).show();

                }



            });
            return v;
        }
    }
}
