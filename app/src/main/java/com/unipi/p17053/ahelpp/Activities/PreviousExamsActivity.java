package com.unipi.p17053.ahelpp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unipi.p17053.ahelpp.DatabasePannel;
import com.unipi.p17053.ahelpp.R;

public class PreviousExamsActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    LinearLayout linearHome,linearTheory,linearTests,linearPrevious,linearPerformance,linearBookmarked,linearDrwrLeaderboard,linearAbout, linearLogout;
    TextView username,userCapital;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_exams);

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
    }

    //Previous Exams Clicks
    public void open2021themata(View view) {
        Intent intent = new Intent(this, ViewPreviousExamsActivity.class);
        intent.putExtra("FROM","themata2021");
        startActivity(intent);

    }

    public void open2021lyseis(View view) {

        Intent intent = new Intent(this, ViewPreviousExamsActivity.class);
        intent.putExtra("FROM","lyseis2021");
        startActivity(intent);

    }

    public void open2020themata(View view) {

        Intent intent = new Intent(this, ViewPreviousExamsActivity.class);
        intent.putExtra("FROM","themata2020");
        startActivity(intent);

    }
    public void open2020lyseis(View view) {

        Intent intent = new Intent(this, ViewPreviousExamsActivity.class);
        intent.putExtra("FROM","lyseis2020");
        startActivity(intent);

    }

    public void open2019themata(View view) {
        Intent intent = new Intent(this, ViewPreviousExamsActivity.class);
        intent.putExtra("FROM","themata2019");
        startActivity(intent);

    }
    public void open2019lyseis(View view) {

        Intent intent = new Intent(this, ViewPreviousExamsActivity.class);
        intent.putExtra("FROM","lyseis2019");
        startActivity(intent);

    }

    public void open2018themata(View view) {

        Intent intent = new Intent(this, ViewPreviousExamsActivity.class);
        intent.putExtra("FROM","themata2018");
        startActivity(intent);


    }
    public void open2018lyseis(View view) {

        Intent intent = new Intent(this, ViewPreviousExamsActivity.class);
        intent.putExtra("FROM","lyseis2018");
        startActivity(intent);

    }
    public void open2017themata(View view) {

        Intent intent = new Intent(this, ViewPreviousExamsActivity.class);
        intent.putExtra("FROM","themata2017");
        startActivity(intent);


    }
    public void open2017lyseis(View view) {

        Intent intent = new Intent(this, ViewPreviousExamsActivity.class);
        intent.putExtra("FROM","lyseis2017");
        startActivity(intent);

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
        recreate();
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