package com.unipi.p17053.ahelpp.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.unipi.p17053.ahelpp.Adapters.ChallengesAdapter;
import com.unipi.p17053.ahelpp.Adapters.IndexElementAdapter;
import com.unipi.p17053.ahelpp.DatabasePannel;
import com.unipi.p17053.ahelpp.Models.ChallengesModel;
import com.unipi.p17053.ahelpp.MyCompleteListener;
import com.unipi.p17053.ahelpp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Index extends AppCompatActivity {

    DrawerLayout drawerLayout;
    LinearLayout linearHome,linearTheory,linearTests,linearPrevious,linearPerformance,linearBookmarked,linearDrwrLeaderboard,linearAbout, linearLogout;
    TextView username,userCapital,hey,theoryToStudyTxt,challengeToTakeTxt,challengeToTakeFromLessonTxt;
    RecyclerView indexRecycler;
    IndexElementAdapter indexElementAdapter;
    LinearLayout theoryToStudyBtn,challengeToTakeBtn;
    boolean allStudied;
    boolean allChallenged;
    int givePosCh = 0;
    List<String> allChallenges = new ArrayList<>();
    List<String> allLessons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        //Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        linearHome = findViewById(R.id.linearHome);
        linearTheory = findViewById(R.id.linearTheory);
        linearTests = findViewById(R.id.linearTests);
        linearPrevious = findViewById(R.id.linearPrevious);
        linearPerformance = findViewById(R.id.linearPerformance);
        linearBookmarked = findViewById(R.id.linearBookmarked);
        linearDrwrLeaderboard = findViewById(R.id.linearDrwrLeaderboard);
        linearAbout = findViewById(R.id.linearAbout);
        linearLogout = findViewById(R.id.linearLogout);
        theoryToStudyTxt = findViewById(R.id.theoryToStudyTxt);
        challengeToTakeTxt = findViewById(R.id.challengeToTakeTxt);
        challengeToTakeFromLessonTxt = findViewById(R.id.challengeToTakeFromLessonTxt);
        theoryToStudyBtn = findViewById(R.id.theoryToStudyBtn);
        challengeToTakeBtn = findViewById(R.id.challengeToTakeBtn);



        //Initialize user's profile
        username = findViewById(R.id.username);
        userCapital = findViewById(R.id.userCapital);
        hey = findViewById(R.id.hey);
        String name = DatabasePannel.profile.getName();
        username.setText(name);
        userCapital.setText(name.toUpperCase().substring(0,1));

        hey.setText("Hey, "+name);

        //Initialize activity views
        indexRecycler = findViewById(R.id.indexElementRecycler);





        //Show Elements in Index
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        indexRecycler.setLayoutManager(layoutManager);



        //Give the right attributes to the elements
        load_ElementTestData();
        load_ToStudy();


    }


    private void load_ToStudy() {

        DatabasePannel.loadLessonToStudy(new MyCompleteListener() {
            @Override
            public void onSuccess() {

                setLessonToStudy();
                load_ToTake();
                Log.d("FEEDBACK load success : ", "okayLoadToStudy" );

            }

            @Override
            public void onFailure() {
                Log.d("FEEDBACK load fail : ", "not okay" );
                Toast.makeText(Index.this,"Something went wrong . . . ",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void load_ToTake() {
        DatabasePannel.loadChallengeToTake(new MyCompleteListener() {
            @Override
            public void onSuccess() {

                setChallengeToTake();
                Log.d("FEEDBACK load success : ", "okayLoadToTake" );

            }

            @Override
            public void onFailure() {
                Log.d("FEEDBACK load fail : ", "not okay to take" );
                Toast.makeText(Index.this,"Something went wrong . . . ",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void load_ElementTestData() {
        DatabasePannel.loadElementTestData(new MyCompleteListener() {
            @Override
            public void onSuccess() {

                Log.d("FEEDBACK load success : ", "okayLoadElements" );

                indexElementAdapter = new IndexElementAdapter(DatabasePannel.globalIndexElementsList);
                indexRecycler.setAdapter(indexElementAdapter);

            }

            @Override
            public void onFailure() {
                Log.d("FEEDBACK load fail : ", "not okay" );
                Toast.makeText(Index.this,"Something went wrong . . . ",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setLessonToStudy() {

        int nextLesson =  0;

        if(DatabasePannel.globalStudiedList.size()>0)
        {
            String latestLesson = DatabasePannel.globalStudiedList.get(DatabasePannel.globalStudiedList.size()-1);
            Log.d("FEEDBACK The MaxStudied : ", latestLesson );

            allLessons.clear();
            for(int i=0;i<DatabasePannel.globalLessonsList.size();i++)
            {
                allLessons.add(DatabasePannel.globalLessonsList.get(i).getLessonName());
                Log.d("All Lessons - Lesson Name : ", DatabasePannel.globalLessonsList.get(i).getLessonName() );
            }
            Collections.sort(allLessons);


            if((allLessons.get(allLessons.size()-1)).equals(latestLesson))
            {
                allStudied = true;
                Log.d("FEEDBACK The MaxStudied was also the last of all lessons : ", latestLesson +" "+(allLessons.get(allLessons.size()-1)) );


                for(int i=0;i<allLessons.size();i++)
                {
                    if(!allLessons.get(i).equals(DatabasePannel.globalStudiedList.get(i)))
                    {
                        allStudied = false;
                        Log.d("FEEDBACK But you didnt study : ", allLessons.get(i) );
                        nextLesson =  Integer.parseInt(allLessons.get(i).substring(0,1));
                        theoryToStudyTxt.setText("Συνέχεια Θεωρίας : Κεφάλαιο "+String.valueOf(nextLesson));
                        break;

                    }

                }
                if(allStudied)
                {
                    Log.d("FEEDBACK ok you study all : ", "check" );
                    theoryToStudyTxt.setText("Ολοκλήρωσες τη Θεωρία : Επανάληψη");
                }

            }
            else
            {

                nextLesson =  Integer.parseInt(latestLesson.substring(0,1));
                theoryToStudyTxt.setText("Συνέχεια Θεωρίας : Κεφάλαιο "+String.valueOf((nextLesson+1)));


            }

        }
        else
        {
            Log.d("FEEDBACK ok first time : ", "check" );
            theoryToStudyTxt.setText("Έναρξη Θεωρίας : Κεφάλαιο "+String.valueOf((nextLesson+1)));

        }


    }

    public void showLessonToStudy(View view) {

        if(allStudied)
        {
            Intent intent = new Intent(Index.this, Theory.class);
            startActivity(intent);
        }
        else
        {
            int posHelper;
            String label = (String) theoryToStudyTxt.getText();
            posHelper = Integer.parseInt(label.substring(label.length()-1));
            int givePos = posHelper-1;

            DatabasePannel.selected_less_index = givePos;
            Intent intent = new Intent(Index.this, ViewTheoryActivity.class);
            startActivity(intent);

        }



    }


    private void setChallengeToTake() {

        int nextChallenge =  0;
        int fromLesson =  0;

        if(DatabasePannel.globalChallengedList.size()>0)
        {
            String latestChallenge = DatabasePannel.globalChallengedList.get(DatabasePannel.globalChallengedList.size()-1);
            Log.d("FEEDBACK The MaxChallenged : ", latestChallenge );

            allChallenges.clear();
            for(int i=0;i<DatabasePannel.globalAllTestsList.size();i++)
            {
                allChallenges.add(DatabasePannel.globalAllTestsList.get(i).getTestId());
                Log.d("All Challenges - Challenge Name : ", DatabasePannel.globalAllTestsList.get(i).getTestId() );
            }
            Collections.sort(allChallenges);


            if((allChallenges.get(allChallenges.size()-1)).equals(latestChallenge))
            {
                allChallenged = true;
                Log.d("FEEDBACK The MaxChallenged was also the last of all challenges : ", latestChallenge +" "+(allChallenges.get(allChallenges.size()-1)) );


                for(int i=0;i<allChallenges.size();i++)
                {
                    if(!allChallenges.get(i).equals(DatabasePannel.globalChallengedList.get(i)))
                    {
                        allChallenged = false;
                        Log.d("FEEDBACK But you didnt take : ", allChallenges.get(i) );
                        nextChallenge =  Integer.parseInt(allChallenges.get(i).substring(6,7));
                        fromLesson = Integer.parseInt(allChallenges.get(i).substring(allChallenges.get(i).length()-1));
                        challengeToTakeTxt.setText("Συνέχεια Εξάσκησης : Challenge "+String.valueOf(nextChallenge));
                        challengeToTakeFromLessonTxt.setText("Κεφάλαιο "+String.valueOf(fromLesson));
                        break;

                    }

                }
                if(allChallenged)
                {
                    Log.d("FEEDBACK ok you challenged all : ", "check" );
                    challengeToTakeTxt.setText("Ολοκλήρωσες όλα τα Challenges");
                    challengeToTakeFromLessonTxt.setText("Βελτίωση Επίδοσης");
                }

            }
            else
            {

                nextChallenge =Integer.parseInt(latestChallenge.substring(latestChallenge.length()-1));
                fromLesson = Integer.parseInt(latestChallenge.substring(6,7));
                int total_tests_count = DatabasePannel.globalLessonsList.get(fromLesson-1).getNumOfTests();
                if(total_tests_count==nextChallenge)
                {
                    challengeToTakeFromLessonTxt.setText("Κεφάλαιο "+String.valueOf((fromLesson+1)));
                    challengeToTakeTxt.setText("Συνέχεια Εξάσκησης : Challenge "+1);
                }
                else
                {
                    challengeToTakeFromLessonTxt.setText("Κεφάλαιο "+String.valueOf((fromLesson)));
                    challengeToTakeTxt.setText("Συνέχεια Εξάσκησης : Challenge "+String.valueOf((nextChallenge+1)));
                }



            }

        }
        else
        {
            Log.d("FEEDBACK ok first time Challenge : ", "check" );

            challengeToTakeTxt.setText("Έναρξη Εξάσκησης : Challenge "+String.valueOf((nextChallenge+1)));
            challengeToTakeFromLessonTxt.setText("Κεφάλαιο "+String.valueOf((fromLesson+1)));

        }


    }

    public void showChallengeToTake(View view) {

        if(allChallenged)
        {
            Intent intent = new Intent(Index.this, Test.class);
            startActivity(intent);

        }
        else
        {
            String labelCh = (String) challengeToTakeTxt.getText();
            int posHelperCh = Integer.parseInt(labelCh.substring(labelCh.length()-1));
            //int givePosCh = posHelperCh-1;

            String labelLess = (String) challengeToTakeFromLessonTxt.getText();
            int posHelperLess = Integer.parseInt(labelLess.substring(labelLess.length()-1));
            int givePosLess = posHelperLess-1;

            String id = "lesson"+posHelperLess+"test"+posHelperCh;
            Log.d("The Challenge-to-take ID is : ", id );
            givePosCh = 0;

            for (int i=0;i<DatabasePannel.globalAllTestsList.size();i++)
            {
                if (DatabasePannel.globalAllTestsList.get(i).getTestId().equals(id) )
                {
                    Log.d("The Challenge-to-take ID is EQUAL TO : ", DatabasePannel.globalAllTestsList.get(i).getTestId() +" and it's in position "+i);
                    givePosCh  = i ;
                    break;
                }
            }

            DatabasePannel.globalTestList = DatabasePannel.globalAllTestsList;

            DatabasePannel.loadMyScores(new MyCompleteListener() {
                @Override
                public void onSuccess() {

                    DatabasePannel.selected_less_index = givePosLess;
                    DatabasePannel.selected_test_index = givePosCh;
                    Intent intent = new Intent(Index.this, StartTestActivity.class);
                    startActivity(intent);

                }

                @Override
                public void onFailure() {
                    Toast.makeText(Index.this,"Something went wrong . . . ",Toast.LENGTH_SHORT).show();
                }
            });



        }


    }

    public void toTheTopClick(View view)
    {

        Intent intent = new Intent(Index.this,ToTheTopActivity.class);
        startActivity(intent);

    }

    public void selfImproClick(View view)
    {
        Intent intent = new Intent(Index.this,StayPositiveActivity.class);
        startActivity(intent);


    }






    //Actions for Navigation
    public void ClickMenu(View View) {
        //call method to open drawer
        openDrawer(drawerLayout);
        //textView.setText(preferences.getString("userNode", FirebaseAuth.getInstance().getCurrentUser().getEmail()));
    }


    //redirect to the desired activity
    public void ClickHome(View view) {
        recreate();
    }

    public void ClickTheory(View view) {

        redirectActivity(this, Theory.class);

    }
    public void ClickTests(View view) {

        redirectActivity(this, Test.class);

    }

    public void ClickPreviousExams(View view)
    {
        redirectActivity(this, PreviousExamsActivity.class);

    }


    public void ClickPerformance(View view)
    {
        redirectActivity(this, MyPerformanceActivity.class);

    }
    public void ClickBookmarked(View view)
    {
        redirectActivity(this, BookmarkedQActivity.class);

    }
    public void ClickDrwrLeaderboard(View view)
    {
        redirectActivity(this, LeaderboardActivity.class);

    }
    public void ClickAbout(View view) {

        redirectActivity(this, About.class);

    }

    public void ClickLogout(View view) {

        logout(this);
    }


    //Functions to recycle in other activities

    public static void openDrawer(DrawerLayout drawerLayout) {
        //Open the menu drawer layout
        drawerLayout.openDrawer(GravityCompat.START);

    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        //Close the menu drawer layout if it is open
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

    }

    public static void redirectActivity(Activity activity, Class theClass) {
        //Initialize intent
        Intent intent = new Intent(activity, theClass);
        //Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //start activity
        activity.startActivity(intent);
    }

    public static void logout(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Αποσύνδεση");
        builder.setIcon(activity.getResources().getDrawable(R.drawable.ic_android_face_launcher_foreground));
        builder.setMessage("Είσαι σίγουρος πως θέλεις να αποσυνδεθείς;");

        //Positive answer
        builder.setPositiveButton("ΝΑΙ", (dialog, which) -> {
            FirebaseAuth.getInstance().signOut();
            //Logout
            redirectActivity(activity, LoginActivity.class);
            activity.finish();
        });

        //Negative answer
        builder.setNegativeButton("ΟΧΙ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Dismiss dialog and continue
                dialog.dismiss();

            }
        });

        //show dialog
        builder.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //close drawer
        closeDrawer(drawerLayout);
    }



}