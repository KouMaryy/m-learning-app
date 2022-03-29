package com.unipi.p17053.ahelpp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unipi.p17053.ahelpp.Adapters.LeaderbordAdapter;
import com.unipi.p17053.ahelpp.DatabasePannel;
import com.unipi.p17053.ahelpp.MyCompleteListener;
import com.unipi.p17053.ahelpp.R;

import static com.unipi.p17053.ahelpp.DatabasePannel.globalRankUsersList;
import static com.unipi.p17053.ahelpp.DatabasePannel.global_users_count;
import static com.unipi.p17053.ahelpp.DatabasePannel.performance;

public class LeaderboardActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    AnimationDrawable gradientAnim;
    LinearLayout linearHome,linearTheory,linearTests,linearPrevious,linearPerformance,linearBookmarked,linearDrwrLeaderboard,linearAbout, linearLogout,linearMyPlace;
    TextView username,userCapital;

    private TextView myScoreLeader,myRankLeader,myCapitalLeader;
    private RecyclerView leaderboard_recycler;
    private LeaderbordAdapter leaderbordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

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
        myScoreLeader = findViewById(R.id.myScoreLeader);
        myRankLeader = findViewById(R.id.myRankLeader);
        myCapitalLeader = findViewById(R.id.myCapitalLeader);
        myCapitalLeader.setText(name.toUpperCase().substring(0,1));
        leaderboard_recycler = findViewById(R.id.leaderboard_recycler);

        //Gradient Animation Activation
        linearMyPlace = findViewById(R.id.linearMyPlace);
        gradientAnim = (AnimationDrawable) linearMyPlace.getBackground();
        gradientAnim.setEnterFadeDuration(10);
        gradientAnim.setExitFadeDuration(1000);
        gradientAnim.start();

        //Layout for users rank in the leaderboard
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        leaderboard_recycler.setLayoutManager(linearLayoutManager);

        //Show Leaderboard
        leaderbordAdapter = new LeaderbordAdapter(globalRankUsersList);
        leaderboard_recycler.setAdapter(leaderbordAdapter);

        DatabasePannel.getTopUsers(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                leaderbordAdapter.notifyDataSetChanged();

                if(DatabasePannel.performance.getScore()!= 0)
                {
                    if(! DatabasePannel.imOnBoard)
                    {
                        calculateRank();
                    }

                    myScoreLeader.setText("Score : " + DatabasePannel.performance.getScore());
                    myRankLeader.setText(String.valueOf(DatabasePannel.performance.getRank()));
                }

            }

            @Override
            public void onFailure() {
                Toast.makeText(LeaderboardActivity.this,"Something went wrong . . . ",Toast.LENGTH_SHORT).show();

            }
        });

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
        Index.redirectActivity(this, MyPerformanceActivity.class);
        this.finish();
    }
    public void ClickBookmarked(View view)
    {
        Index.redirectActivity(this, BookmarkedQActivity.class);
        this.finish();
    }
    public void ClickDrwrLeaderboard(View view)
    {
        recreate();
    }
    public void ClickAbout(View view) {
        Index.redirectActivity(this, About.class);
        this.finish();
    }
    public void ClickLogout(View view) {
        Index.logout(this);
    }
}