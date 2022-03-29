package com.unipi.p17053.ahelpp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.unipi.p17053.ahelpp.Adapters.BookmarksAdapter;
import com.unipi.p17053.ahelpp.Adapters.LessonAdapter;
import com.unipi.p17053.ahelpp.DatabasePannel;
import com.unipi.p17053.ahelpp.Models.ChallengesModel;

import com.unipi.p17053.ahelpp.MyCompleteListener;
import com.unipi.p17053.ahelpp.R;

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.unipi.p17053.ahelpp.DatabasePannel.globalLessonsList;
import static com.unipi.p17053.ahelpp.DatabasePannel.globalRankUsersList;
import static com.unipi.p17053.ahelpp.DatabasePannel.global_users_count;
import static com.unipi.p17053.ahelpp.DatabasePannel.performance;

public class MyPerformanceActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    LinearLayout linearHome,linearTheory,linearTests,linearPrevious,linearPerformance,linearBookmarked,linearDrwrLeaderboard,linearAbout, linearLogout;
    TextView username,userCapital;
    TextView myRank,overallScore;
    public int testAvSum;
    public int currentAv;
    public int testsAttempted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_performance);

        //Initialize views for navigation
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

        //Initialize user's profile
        username = findViewById(R.id.username);
        userCapital = findViewById(R.id.userCapital);
        String name = DatabasePannel.profile.getName();
        username.setText(name);
        userCapital.setText(name.toUpperCase().substring(0,1));

        //Initialize activity views
        myRank = findViewById(R.id.myRank);
        overallScore= findViewById(R.id.overallScore);


        PieChart pieChart = findViewById(R.id.pieChart);


        if(globalRankUsersList.size()==0)
        {

            DatabasePannel.getTopUsers(new MyCompleteListener() {
                @Override
                public void onSuccess() {

                    if(performance.getScore()!= 0)
                    {
                        if(! DatabasePannel.imOnBoard)
                        {
                            calculateRank();
                        }

                        overallScore.setText(String.valueOf(performance.getScore()));
                        myRank.setText(String.valueOf(performance.getRank()));
                    }

                }

                @Override
                public void onFailure() {
                    Toast.makeText(MyPerformanceActivity.this,"Something went wrong . . . ",Toast.LENGTH_SHORT).show();

                }
            });

        }
        else
        {
            overallScore.setText(String.valueOf(performance.getScore()));
            if(performance.getScore()!=0) myRank.setText(String.valueOf(performance.getRank()));
        }
        myRank.setText(String.valueOf(performance.getRank()));


        //Average score for each lesson
        ArrayList<PieEntry> scoresAv = new ArrayList<>();
        Map<String, Integer> map = DatabasePannel.globalLessonTestAverageList;



        int lessCount = globalLessonsList.size();
        float lessonAv = 0;

        for(int i=0;i<lessCount;i++) {

            String key = "lesson" + (i + 1);
            int avSum = 0;
            int testsTaken = 0;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {

                if(entry.getKey().contains(key))
                {
                    avSum += entry.getValue();
                    testsTaken++;
                }

            }

            if(testsTaken!=0)
            {
                lessonAv = avSum/testsTaken;
            }

            //Pie Chart
            scoresAv.add(new PieEntry(lessonAv, "Κεφάλαιο " + (i+1) ));
            lessonAv =0;


        }

        PieDataSet pieDataSet = new PieDataSet(scoresAv,"");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(18f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Μ.Ο Βαθμολογίας");
        pieChart.setCenterTextSize(20f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(14f);
        pieChart.animate();



    }


    private void calculateRank() {

        int lowestTopScore = globalRankUsersList.get(globalRankUsersList.size()-1).getScore();
        int remainingPlaces = global_users_count - 20;
        int myPlace = (performance.getScore()*remainingPlaces)/lowestTopScore;
        int rank;
        if(lowestTopScore!=performance.getScore())
        {
            rank = global_users_count - myPlace;
        }
        else
        {
            rank = 21;
        }

        performance.setRank(rank);
    }





    //Actions for Navigation
    public void ClickMenu(View View) {
        //call method to open drawer
        Index.openDrawer(drawerLayout);
        //textView.setText(preferences.getString("userNode", FirebaseAuth.getInstance().getCurrentUser().getEmail()));
    }

    //redirect to the desired activity
    public void ClickHome(View view) {
        Index.redirectActivity(this, Index.class);
        this.finish();
    }
    public void ClickTheory(View view) {
        Index.redirectActivity(this, Theory.class);
        this.finish();
    }
    public void ClickTests(View view) { Index.redirectActivity(this, Test.class);
        this.finish();}
    public void ClickPreviousExams(View view)
    {
        Index.redirectActivity(this, PreviousExamsActivity.class);
        this.finish();
    }

    public void ClickPerformance(View view)
    {
        recreate();
    }
    public void ClickBookmarked(View view)
    {
        Index.redirectActivity(this, BookmarkedQActivity.class);
        this.finish();
    }
    public void ClickDrwrLeaderboard(View view)
    {
        Index.redirectActivity(this, LeaderboardActivity.class);
        this.finish();
    }
    public void ClickAbout(View view) {
        Index.redirectActivity(this, About.class);
        this.finish();
    }
    public void ClickLogout(View view) {
        Index.logout(this);
    }
}