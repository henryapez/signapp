package com.henry.signapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
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
    private FirebaseHelper db = FirebaseHelper.getInstance();
    //Current User references
    private FirebaseAuth auth;
    //Check Answer button, button displays answer when clicked
    private Button showAnswer;
    private Button knowIt, keepIt;
    //Firebase database reference for queries
    private String useremail, username;
    float x1,x2;
    float y1, y2;
    private static final String TAG = "CardSwiping";


    private int signOnTopPosition;
    private boolean loadedAllSigns=false;
    private ArrayList<String> options = new ArrayList<String>();
    private boolean initialOptions=true;
    private String currentGif;

    private Button options2,options1,options3,options4;
    private int chances=0;
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

        System.out.println("size of HELPER HASHMAP is " + Integer.toString(db.getUserSigns().size()));
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




        //example of buttons triggering events on the deck
        knowIt = (Button) findViewById(R.id.knowIt);
        knowIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mastersASignActions();
            }
        });
        keepIt = (Button) findViewById(R.id.practiceIt);
        keepIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keepASignInDeckActions();
            }
        });

      showAnswer = (Button) findViewById(R.id.showAnswer);
        showAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showAnswer.getText().equals("Answer") && !(adapter.getSignOnTopPosition() == signUrls.size()))
                    showAnswer.setText(db.getUserSign(signUrls.get(adapter.getSignOnTopPosition())).getTitle());
                else
                    showAnswer.setText("Answer");
            }
        });




    }

    /*
        Check the user's multiple choice input
        CORRECT: User has mastered that sign and swipe left
        INCORRECT: User has not mastered that sign so keep it in the deck

     */
    public void checkAnswer(View v){
        chances++;
        Button userAnswer = (Button) findViewById(v.getId());
        String answer = (String) userAnswer.getText();
        if(db.getUserSign(signUrls.get(adapter.getSignOnTopPosition())).getTitle().equals(answer)){
           showAnswer.setText("Correct!");
            showAnswer.setBackgroundColor(ContextCompat.getColor(context, R.color.correctGreen));
            options1.setText("Swipe Left to Master this Sign");
            options2.setText("Swipe Right to Keep it in the Deck");
            options3.setText("");
            options4.setText("");
            options1.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            options2.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            options3.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            options4.setBackgroundColor(ContextCompat.getColor(context, R.color.white));

            final Animation animation = new AlphaAnimation(1.0f, 0.7f); // Change alpha from fully visible to invisible
            animation.setDuration(500); // duration - half a second
            animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
            animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
            animation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
            knowIt.startAnimation(animation);
            keepIt.startAnimation(animation);

        }else{
            /*
                USER CLICKED AN INCORRECT ANSWER
                User has two attempts to answer
             */
            if(chances==2){
                Toast.makeText(context, "Incorrect, we'll try later!", Toast.LENGTH_LONG).show();
                keepASignInDeckActions();
                chances=0;
                options1.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                options2.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                options3.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                options4.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            }else{
                Toast.makeText(context, "One more chance and we'll try again later.", Toast.LENGTH_LONG).show();
                userAnswer.setBackgroundColor(ContextCompat.getColor(context, R.color.IncorrectRed));
            }


        }
    }



    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;


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
            keepASignInDeckActions();

        }
        public void onSwipeLeft() {
           mastersASignActions();

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


        public SwipeDeckAdapter(List<String> data, Context context) {
            this.data = data;
            this.context = context;
            signOnTopPosition = 0;
        }

        public int getPosition(){
            return position;
        }



        public void setSignOnTopPosition(){
            signOnTopPosition++;
        }
        public ArrayList<String> getOptions(){
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

            //If the Answer button is pressed, display the sign title on the current sign ontop

            v.setOnTouchListener(new OnSwipeTouchListener(context,data.get(position) ) {
            });

            imageView.setOnTouchListener(new OnSwipeTouchListener(context, data.get(position)) {

            });



            return v;
        }


    }

    public void setOptions(final View parentView) {
        if (options.size() > 0)
            options.clear();
        if (!(signOnTopPosition == signUrls.size())) {
            //Get all of this signs category gifs and get three random signs for options
            DatabaseReference mCatRef = db.getRef("categories/" + db.getUserSign(signUrls.get(adapter.getSignOnTopPosition())).getCategory());
            mCatRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int count = 0;


                    Random rand = new Random();
                    int totalsize = (int) dataSnapshot.getChildrenCount();
                    int rand1, rand2, rand3, rand4;
                    System.out.println(" TOTAL SIZE OF CATEGORY IS  " + Integer.toString(totalsize));
                    do {
                        rand1 = rand.nextInt(totalsize) + 0;
                    } while (rand1 == signOnTopPosition);
                    do {
                        rand2 = rand.nextInt(totalsize) + 0;
                    } while (rand2 == signOnTopPosition || rand2 == rand1);
                    do {
                        rand3 = rand.nextInt(totalsize) + 0;
                    } while (rand3 == signOnTopPosition || rand3 == rand1 || rand3 == rand2);

                    System.out.println("SIZE OF CATEGORY=  " + Long.toString(dataSnapshot.getChildrenCount())
                            + " rands are  " + Integer.toString(rand1) + " "
                            + Integer.toString(rand2) + "  " + Integer.toString(rand3));
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        if (count == rand1 || count == rand2 || count == rand3) {
                            rand4 = rand.nextInt((int) dataSnapshot.getChildrenCount()) + 0;
                            System.out.println("FOUND OPTIONS  " + messageSnapshot.getValue(Sign.class).getTitle());
                            options.add(messageSnapshot.getValue(Sign.class).getTitle());
                        }
                        count++;
                    }
                    //If one of the randon signs obtained is the answer, add 'Sign' instead of the answer (so we dont have two correct options)
                    if (options.contains(db.getUserSign(signUrls.get(signOnTopPosition)).getTitle())) {
                        options.add("Sign");
                    } else {
                        //Get the correct answer sign and add it as an option
                        options.add(db.getUserSign(signUrls.get(signOnTopPosition)).getTitle());
                    }
                    //Randomize the options
                    Collections.shuffle(options);
                    System.out.println("signobtop index is " + signOnTopPosition +
                            "  OPTIONS LENGTH IS  " + Integer.toString(options.size()) + "  " +
                            "  ANSWER IS " + db.getUserSign(signUrls.get(signOnTopPosition)));


                    options1 = (Button) parentView.findViewById(R.id.option1);
                    options1.setText(options.get(0));
                    options2 = (Button) parentView.findViewById(R.id.option2);
                    options2.setText(options.get(1));
                    options3 = (Button) parentView.findViewById(R.id.option3);
                    options3.setText(options.get(2));
                    options4 = (Button) parentView.findViewById(R.id.option4);
                    options4.setText(options.get(3));


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }
    }

    public void keepASignInDeckActions(){
         /*
           Get top card gif and keep it in the deck to view later
         */
        knowIt.clearAnimation();
        keepIt.clearAnimation();
        //Add it to the swipe deck adapter list
        signUrls.add(currentGif);
        //Clear the answer button
        showAnswer.setText("Answer");
        showAnswer.setBackgroundColor(ContextCompat.getColor(context, R.color.purple));
        //Right swiped = User has unmastered or not mastered this sign yet
        db.setSignMastered(currentGif, false);
        //Update the index tracker to be the index of the next sign in the deck
        adapter.setSignOnTopPosition();
        //Set multiple choice options based on the 'new' sign on top's category
        View currentView = (View) findViewById(R.id.swipeLayout);
        setOptions(currentView);
        //Swipe the card right
        cardStack.swipeTopCardRight(180);
        //Notify adapter of card swipe
        adapter.notifyDataSetChanged();
    }

    public void mastersASignActions(){
        knowIt.clearAnimation();
        keepIt.clearAnimation();
        showAnswer.setText("Answer");
        showAnswer.setBackgroundColor(ContextCompat.getColor(context, R.color.purple));
        Toast.makeText(context, "Left Swipe detected mastering " + currentGif, Toast.LENGTH_LONG).show();
        adapter.setSignOnTopPosition();
        View currentView = (View) findViewById(R.id.swipeLayout);
        setOptions(currentView);
        //Set Swiped sign as maastered int he user's deck
        db.setSignMastered(currentGif, true);
        cardStack.swipeTopCardLeft(180);
    }
}
