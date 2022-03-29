package com.unipi.p17053.ahelpp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unipi.p17053.ahelpp.Adapters.ChallengesAdapter;
import com.unipi.p17053.ahelpp.DatabasePannel;
import com.unipi.p17053.ahelpp.MyCompleteListener;
import com.unipi.p17053.ahelpp.R;

public class ToTheTopActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    LinearLayout linearHome,linearTheory,linearTests,linearPrevious,linearPerformance,linearBookmarked,linearDrwrLeaderboard,linearAbout, linearLogout;
    TextView username,userCapital;
    ChallengesAdapter adapterTop;

    RecyclerView challengeRecyclerTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_the_top);

        //Initialize activity views
        challengeRecyclerTop = findViewById(R.id.toTheToptest_recycler);

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


        //Show test
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        challengeRecyclerTop.setLayoutManager(linearLayoutManager);

        DatabasePannel.loadDoBetterTests(new MyCompleteListener() {
            @Override
            public void onSuccess() {

                DatabasePannel.globalTestList = DatabasePannel.globalDoBetterTestList;
                adapterTop = new ChallengesAdapter(DatabasePannel.globalTestList);
                challengeRecyclerTop.setAdapter(adapterTop);


            }

            @Override
            public void onFailure() {
                Toast.makeText(ToTheTopActivity.this,"Something went wrong . . . ",Toast.LENGTH_SHORT).show();
            }
        });
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
    }
    public void ClickTheory(View view) { Index.redirectActivity(this, Theory.class); }
    public void ClickTests(View view) {  Index.redirectActivity(this, Test.class); }
    public void ClickPreviousExams(View view)
    {
        Index.redirectActivity(this, PreviousExamsActivity.class);
        this.finish();
    }
    public void ClickPerformance(View view)
    {
        Index.redirectActivity(this, MyPerformanceActivity.class);
    }
    public void ClickBookmarked(View view)
    {
        Index.redirectActivity(this, BookmarkedQActivity.class);
    }
    public void ClickDrwrLeaderboard(View view)
    {
        Index.redirectActivity(this, LeaderboardActivity.class);
    }

    public void ClickAbout(View view) {
        Index.redirectActivity(this, About.class);
    }
    public void ClickLogout(View view) {
        Index.logout(this);
    }
}