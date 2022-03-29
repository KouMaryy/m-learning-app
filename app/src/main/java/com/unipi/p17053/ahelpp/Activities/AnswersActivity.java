package com.unipi.p17053.ahelpp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.unipi.p17053.ahelpp.Adapters.AnswersAdapter;
import com.unipi.p17053.ahelpp.DatabasePannel;
import com.unipi.p17053.ahelpp.R;

public class AnswersActivity extends AppCompatActivity {

    RecyclerView answersRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        //Initialize Views
        answersRecycler = findViewById(R.id.answers_recycler);

        //Show answers
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        answersRecycler.setLayoutManager(linearLayoutManager);

        AnswersAdapter answersAdapter = new AnswersAdapter(DatabasePannel.globalQuestionList);
        answersRecycler.setAdapter(answersAdapter);


    }

    //Click Listeners for after-test actions


    public void goBack(View view) {

        if(DatabasePannel.clickedFrom =="Element")
        {
            Intent intent = new Intent(AnswersActivity.this, ElementTestScoreActivity.class);
            startActivity(intent);
            this.finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else if(DatabasePannel.clickedFrom =="Challenges"||DatabasePannel.clickedFrom=="ToTheTop")
        {
            Intent intent = new Intent(AnswersActivity.this,TestScoreActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            this.finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        else
        {
            Intent intent = new Intent(AnswersActivity.this,Index.class);
            startActivity(intent);
            this.finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

    }
    public void showLeaderboard(View view) {

        Intent intent = new Intent(AnswersActivity.this,LeaderboardActivity.class);
        startActivity(intent);
        this.finish();
    }
    public void showBookmarked(View view) {
        Index.redirectActivity(this, BookmarkedQActivity.class);
    }
}