package com.henry.signapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CardSwiping extends AppCompatActivity {

    ArrayList<String> signUrls;
    private SwipeDeck cardStack;
    private SwipeDeckAdapter adapter;
    private Context context = this;
    //Reference to database Helper
    private FirebaseHelper db = getDB();
    //Current User references
    private FirebaseAuth auth;
    //Check Answer button, button displays answer when clicked
    private Button checkAnswer;

    //Firebase database reference for queries
    private String useremail, username;
    float x1,x2;
    float y1, y2;
    private static final String TAG = "CardSwiping";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_swiping);

        //Get the user's local deck stored in FirebaseHelper and store the urls to read
        signUrls = new ArrayList<String>(db.getUserSigns().keySet());


        auth = FirebaseAuth.getInstance();
        useremail = auth.getCurrentUser().getEmail();
        username = useremail.split("@")[0];
        cardStack = (SwipeDeck) findViewById(R.id.swipe_deck);


        System.out.println("size of usersigns is " + Integer.toString(signUrls.size()));
        //Set the Swipe card adapter. Shuffle gifs before sending them to the View
        Collections.shuffle(signUrls);
        adapter = new SwipeDeckAdapter(signUrls, this);
        if(cardStack != null){
            cardStack.setAdapter(adapter);
        }

        cardStack.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                Log.i("MainActivity", "onViewAttachedToWindow " + cardStack.getAdapterIndex());
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                Log.i("MainActivity", "onViewDetachedFromWindow " + cardStack.getAdapterIndex());
            }
        });
        cardStack.setCallback(new SwipeDeck.SwipeDeckCallback() {
            @Override
            public void cardSwipedLeft(long positionInAdapter) {
                Log.i("MainActivity", "card was swiped left, position in adapter: " + cardStack.getAdapterIndex());
                //Log.i("MainActivity", "child count is " + cardStack.getFocusedChild().getId());




            }

            @Override
            public void cardSwipedRight(long positoinInAdapter) {
                Log.i("MainActivity", "card was swiped right, position in adapter: " + positoinInAdapter);



            }

            public void scardSwipedRight(long positoinInAdapter) {
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

       checkAnswer = (Button) findViewById(R.id.checkAnswer);
        checkAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkAnswer.getText().equals("Answer"))
                    checkAnswer.setText(db.getUserSign(signUrls.get(adapter.getSignOnTopPosition())).getTitle());
                else
                    checkAnswer.setText("Answer");
            }
        });




    }

//    // onTouchEvent () method gets called when User performs any touch event on screen
//    // Handled left and right swipe ONLY when one card is present
//    //If multiple card are present, there are multiple View layers. Touch events are detected in the SwipeDeckAdapter
//    @Override
//    public boolean onTouchEvent(MotionEvent touchevent)
//    {
//        switch (touchevent.getAction()) {
//            // when user first touches the screen we get x and y coordinate
//            case MotionEvent.ACTION_DOWN: {
//                x1 = touchevent.getX();
//                y1 = touchevent.getY();
//                break;}
//            case MotionEvent.ACTION_UP: {
//                x2 = touchevent.getX();
//                y2 = touchevent.getY();
//                //if Right swipe when only one card is visible
//                if (x1 < x2) {
//                   // cardStack.swipeTopCardRight(180);
//                    Toast.makeText(this, "Left to Right Swap Performed", Toast.LENGTH_LONG).show();
//                }
//                //Left swipe when only one card is visible
//                if (x1 > x2) {
//                  ///  cardStack.swipeTopCardLeft(180);
//                    Toast.makeText(this, "Right to Left Swap Performed", Toast.LENGTH_LONG).show();
//                }
//                // if UP to Down sweep event on screen
//                if (y1 < y2) {
//                    Toast.makeText(this, "UP to Down Swap Performed", Toast.LENGTH_LONG).show();
//                }
//                //if Down to UP sweep event on screen
//                if (y1 > y2) {
//                    Toast.makeText(this, "Down to UP Swap Performed", Toast.LENGTH_LONG).show();
//                }0
//                break;
//            }
//        }
//        return false;
//    }

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
            signUrls.add(currentGif);
            checkAnswer.setText("Answer");
            adapter.setSignOnTopPosition();
            View currentView = (View) findViewById(R.id.swipeLayout);
            adapter.setOptions(currentView);

            cardStack.swipeTopCardRight(180);
            adapter.notifyDataSetChanged();

        }
        public void onSwipeLeft() {
            checkAnswer.setText("Answer");
            Toast.makeText(context, "Left Swipe detected mastering " + currentGif, Toast.LENGTH_LONG).show();
            adapter.setSignOnTopPosition();
            View currentView = (View) findViewById(R.id.swipeLayout);
            adapter.setOptions(currentView);
            //Set Swiped sign as maastered int he user's deck
            db.getRef("users").child(db.getUsername()).child("myDeck").child(currentGif).child("mastered").setValue(true);
            cardStack.swipeTopCardLeft(180);

        }
        public void onSwipeTop() {
        }
        public void onSwipeBottom() {
        }
    } // END SWIPE CLASS Listener

    public class SwipeDeckAdapter extends BaseAdapter {
        private List<String> data;
        //CardSwiping main Activity context
        private Context context;
        private int position;
        private boolean loadedAllSigns=false;
        //
        private Sign signOnTop;
        private int signOnTopPosition;
        private ArrayList<Sign> options = new ArrayList<Sign>();
        private boolean initialOptions=true;
        //Answer View
        TextView topAnswerView;

        public SwipeDeckAdapter(List<String> data, Context context) {
            this.data = data;
            this.context = context;
            this.signOnTopPosition = 0;
        }

        public int getPosition(){
            return position;
        }



        public void setSignOnTopPosition(){
            signOnTopPosition++;
        }
        public ArrayList<Sign> getOptions(){
            return options;
        }
        public int getSignOnTopPosition(){
            return signOnTopPosition;
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
        public View getView(final int position, View convertView, final ViewGroup parent) {

            View v = convertView;


            if (v == null) {
                LayoutInflater inflater = getLayoutInflater();
                // normally use a viewholder
                v = inflater.inflate(R.layout.test_card2, parent, false);
            }
            this.position = position;
            ImageView imageView = (ImageView) v.findViewById(R.id.offer_image);

                //Picasso.with(context).load(R.drawable.food).fit().centerCrop().into(imageView);
                Glide.with(context).load("http://www.lifeprint.com/asl101/gifs-animated/"+data.get(position)+".gif").into(imageView);
               // viewHolder.add(v);
            if(initialOptions && getCount()>0) {
                View parentView = (View) parent.getParent();
                setOptions(parentView);
                initialOptions=false;
            }

            Log.i("Position of sign " +data.get(position)+ " is ", Integer.toString(position));

            //If the Answer button is pressed, display the sign title on the current sign ontop

            v.setOnTouchListener(new OnSwipeTouchListener(context,data.get(position) ) {
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

            imageView.setOnTouchListener(new OnSwipeTouchListener(context, data.get(position)) {
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

        public void setOptions(final View parentView){
            if(options.size()>0)
                options.clear();
            //Get all of this signs category gifs and get three random signs for options
            System.out.println("SignonTopPosition is  " + adapter.getSignOnTopPosition());
            System.out.println("UserSigns size in helper is  " + db.getUserSigns().size());
            System.out.println("sign url in spot 0 is  " + signUrls.get(0));
            System.out.println("UserSigns in spot 0 is  " + db.getUserSign(signUrls.get(0)));
            System.out.println("The category in the selected usersign is  " + db.getUserSign(signUrls.get(0)).getCategory());
            System.out.println("cat ref is  " + "categories/" + db.getUserSign(signUrls.get(adapter.getSignOnTopPosition())).getCategory());


            System.out.println("Getting options from " + db.getUserSign(signUrls.get(adapter.getSignOnTopPosition())).getCategory());
            DatabaseReference mCatRef = db.getRef("categories/" + db.getUserSign(signUrls.get(adapter.getSignOnTopPosition())).getCategory());
            mCatRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int count=0;
                    Random rand = new Random();
                    int rand1, rand2, rand3;
                    System.out.println(Integer.toString((int) dataSnapshot.getChildrenCount()));
                    do{
                        rand1= rand.nextInt((int) dataSnapshot.getChildrenCount()) + 0;
                    }while(rand1 == signOnTopPosition );
                    do{
                        rand2= rand.nextInt((int) dataSnapshot.getChildrenCount()) + 0;
                    }while(rand2 == signOnTopPosition || rand2 == rand1);
                    do{
                        rand3= rand.nextInt((int) dataSnapshot.getChildrenCount()) + 0;
                    }while(rand3 == signOnTopPosition || rand3 == rand1 || rand3==rand2);

                    System.out.println("SIZE OF CATEGORY=  " + Long.toString(dataSnapshot.getChildrenCount())
                            +" rands are  "+Integer.toString(rand1) + " "
                            + Integer.toString(rand2)+"  "+Integer.toString(rand3));
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        if(count == rand1 || count ==rand2 || count == rand3){
                            System.out.println("FOUND OPTIONS  " + messageSnapshot.getValue(Sign.class).getTitle());
                            options.add((Sign) messageSnapshot.getValue(Sign.class));
                        }
                        count++;
                    }
                    //Get the correct answer sign and add it as an option
                   // options.add(new Sign(db.getUserSign(signUrls.get(signOnTopPosition))));
                    Collections.shuffle(options);
                    System.out.println("signobtop index is " + signOnTopPosition+
                            "  OPTIONS LENGTH IS  " + Integer.toString(options.size()) + "  "+
                            "  ANSWER IS " + db.getUserSign(signUrls.get(signOnTopPosition)));

                    TextView options1 = (TextView) parentView.findViewById(R.id.option1);
                    options1.setText(options.get(0).getTitle());
                    TextView options2 = (TextView) parentView.findViewById(R.id.option2);
                    options2.setText(options.get(1).getTitle());
                    TextView options3 = (TextView) parentView.findViewById(R.id.option3);
                    options3.setText(options.get(2).getTitle());
                    TextView options4 = (TextView) parentView.findViewById(R.id.option4);
                    options4.setText("HEY");


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }
    }
}
