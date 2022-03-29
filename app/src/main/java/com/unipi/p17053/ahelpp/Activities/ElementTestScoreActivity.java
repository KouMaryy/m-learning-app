package com.unipi.p17053.ahelpp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unipi.p17053.ahelpp.DatabasePannel;
import com.unipi.p17053.ahelpp.MyCompleteListener;
import com.unipi.p17053.ahelpp.R;

public class ElementTestScoreActivity extends AppCompatActivity {

    private TextView disclaimerB,score,correct,wrong,unattempted;
    private LinearLayout allAnswers,allLessonTests,reAttempt,linearLeaderboard;
    private ImageView scoreBack,scoreBookmarked,elementIm;
    private long totalTimeTaken;
    private int finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element_test_score);

        //Initialize views
        scoreBack = findViewById(R.id.scoreBackB);
        linearLeaderboard = findViewById(R.id.linearLeaderboardB);
        scoreBookmarked = findViewById(R.id.scoreBookmarkedB);
        allLessonTests= findViewById(R.id.backToIndexB);
        reAttempt= findViewById(R.id.reAttemptB);
        allAnswers = findViewById(R.id.allAnswersB);
        disclaimerB = findViewById(R.id.disclaimerB);
        score= findViewById(R.id.scoreB);
        correct= findViewById(R.id.correctB);
        wrong= findViewById(R.id.wrongB);
        unattempted= findViewById(R.id.unattemptedB);
        elementIm = findViewById(R.id.imageViewB);

        disclaimerB.setText(DatabasePannel.clickedFromElement);
        if(DatabasePannel.clickedFromElement.equals("Challenge Αποθηκευμένων Ερωτήσεων")) elementIm.setImageResource(R.drawable.ic_bookmark_filled);
        else if(DatabasePannel.clickedFromElement.equals("Challenge Αδυναμιών"))elementIm.setImageResource(R.drawable.ic_heal_white);



        //load the answers data
        loadData();

        //save element results
        saveResults();
    }


    private void loadData() {

        int correctAn=0; int wrongAn=0;int unattemptedAn=0;
        for(int i = 0; i< DatabasePannel.globalQuestionList.size(); i++)
        {
            if(DatabasePannel.globalQuestionList.get(i).getSelectedAnswer() == -1)
            {
                unattemptedAn ++;
            }
            else
            {
                if(DatabasePannel.globalQuestionList.get(i).getSelectedAnswer() == DatabasePannel.globalQuestionList.get(i).getCorrectAnswer())
                {
                    correctAn++;
                }

                else
                {
                    wrongAn++;
                }

            }
        }

        correct.setText(String.valueOf(correctAn));
        wrong.setText(String.valueOf(wrongAn));
        unattempted.setText(String.valueOf(unattemptedAn));

        finalScore = (correctAn*100)/DatabasePannel.globalQuestionList.size();
        score.setText(String.valueOf(finalScore)+" %");


    }


    private void saveResults() {

        DatabasePannel.saveElementResult(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(ElementTestScoreActivity.this,"Επιτυχία Υποβολής",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure() {
                Toast.makeText(ElementTestScoreActivity.this,"Something went wrong . . . ",Toast.LENGTH_SHORT).show();
            }
        });



    }


    /*
    private void setBookmarks()
    {

        Log. d("ScoreActivity ", String.valueOf(DatabasePannel.globalQuestionList.size()));
        for(int i=0;i<DatabasePannel.globalQuestionList.size();i++)
        {
            QuestionModel question = DatabasePannel.globalQuestionList.get(i);

            if(question.isBookmarked())
            {
                if(! globalBookmrkIDList.contains(question.getqID()))
                {
                    globalBookmrkIDList.add(question.getqID());
                    DatabasePannel.profile.setBookmarksCount(globalBookmrkIDList.size());
                }
            }
            else
            {
                if( globalBookmrkIDList.contains(question.getqID()))
                {
                    globalBookmrkIDList.remove(question.getqID());
                    DatabasePannel.profile.setBookmarksCount(globalBookmrkIDList.size());
                }


            }


        }

    }*/


    //Click Listeners for after-test actions

    public void Reattempt(View view) {
        for(int i =0;i<DatabasePannel.globalQuestionList.size();i++)
        {
            DatabasePannel.globalQuestionList.get(i).setSelectedAnswer(-1);
            DatabasePannel.globalQuestionList.get(i).setStatus(DatabasePannel.notVisited);
        }

        Intent intent = new Intent(ElementTestScoreActivity.this, StartElementTestActivity.class);
        startActivity(intent);
        this.finish();
    }


    public void showAnswers(View view) {

        Intent intent = new Intent(ElementTestScoreActivity.this,AnswersActivity.class);
        startActivity(intent);
    }



    public void backToIndexB(View view) {

        Index.redirectActivity(this,Index.class);
        this.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    public void showLeaderboard(View view) {

        Intent intent = new Intent(ElementTestScoreActivity.this,LeaderboardActivity.class);
        startActivity(intent);
        this.finish();
    }
    public void showBookmarked(View view) {
        Index.redirectActivity(this, BookmarkedQActivity.class);
        this.finish();
    }



}