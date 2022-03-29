package com.unipi.p17053.ahelpp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unipi.p17053.ahelpp.Adapters.LessonAdapter;
import com.unipi.p17053.ahelpp.DatabasePannel;
import com.unipi.p17053.ahelpp.R;

public class Test extends AppCompatActivity {

    DrawerLayout drawerLayout;
    LinearLayout linearHome,linearTheory, linearTests,linearPrevious,linearPerformance,linearBookmarked,linearDrwrLeaderboard,linearAbout, linearLogout;
    GridView lessonView;
    TextView username,userCapital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //Initialize activity views
        lessonView = findViewById(R.id.lessonView);

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

        //Show Lessons
        LessonAdapter adapter = new LessonAdapter(DatabasePannel.globalLessonsList);
        lessonView.setAdapter(adapter);

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
    public void ClickTheory(View view) { Index.redirectActivity(this, Theory.class);
        this.finish();}
    public void ClickTests(View view) {  recreate(); }
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