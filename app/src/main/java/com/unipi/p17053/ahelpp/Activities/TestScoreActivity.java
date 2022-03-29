package com.unipi.p17053.ahelpp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unipi.p17053.ahelpp.DatabasePannel;
import com.unipi.p17053.ahelpp.MyCompleteListener;
import com.unipi.p17053.ahelpp.R;

import java.util.concurrent.TimeUnit;

import static com.unipi.p17053.ahelpp.DatabasePannel.globalWrongQIDList;


public class TestScoreActivity extends AppCompatActivity {

    private TextView timeTaken,score,correct,wrong,unattempted;
    private LinearLayout allAnswers,allLessonTests,reAttempt,linearLeaderboard;
    private ImageView scoreBack,scoreBookmarked;
    private long totalTimeTaken;
    private int finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_score);

        //Initialize views
        scoreBack = findViewById(R.id.scoreBack);
        linearLeaderboard = findViewById(R.id.linearLeaderboard);
        scoreBookmarked = findViewById(R.id.scoreBookmarked);
        allLessonTests= findViewById(R.id.allLessonTests);
        reAttempt= findViewById(R.id.reAttempt);
        allAnswers = findViewById(R.id.allAnswers);
        timeTaken = findViewById(R.id.timeTaken);
        score= findViewById(R.id.score);
        correct= findViewById(R.id.correct);
        wrong= findViewById(R.id.wrong);
        unattempted= findViewById(R.id.unattempted);

        //load the answers data
        loadData();

        //save the test results
        saveResults();
        initToSaveNewResults();
        //setBookmarks();

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
                        if(globalWrongQIDList.contains(DatabasePannel.globalQuestionList.get(i).getqID()))
                        {
                            globalWrongQIDList.remove(DatabasePannel.globalQuestionList.get(i).getqID());
                            DatabasePannel.profile.setWrongsCount( globalWrongQIDList.size());
                        }
                    }

                    else
                    {
                        wrongAn++;
                        if(!(globalWrongQIDList.contains(DatabasePannel.globalQuestionList.get(i).getqID()))) {
                            globalWrongQIDList.add(DatabasePannel.globalQuestionList.get(i).getqID());
                        }
                        DatabasePannel.profile.setWrongsCount( globalWrongQIDList.size());
                    }

                }
        }

        correct.setText(String.valueOf(correctAn));
        wrong.setText(String.valueOf(wrongAn));
        unattempted.setText(String.valueOf(unattemptedAn));

        finalScore = (correctAn*100)/DatabasePannel.globalQuestionList.size();
        score.setText(String.valueOf(finalScore)+" %");

        totalTimeTaken = getIntent().getLongExtra("TIME_TAKEN",0);

        String time = String.format("%02d:%02d '",
                TimeUnit.MILLISECONDS.toMinutes( totalTimeTaken),
                TimeUnit.MILLISECONDS.toSeconds( totalTimeTaken)-
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes( totalTimeTaken))
        );

        timeTaken.setText(time);

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

    private void saveResults() {


        DatabasePannel.saveResult(finalScore, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(TestScoreActivity.this,"Επιτυχία Υποβολής",Toast.LENGTH_SHORT).show();
                markAsChallenged();


            }

            @Override
            public void onFailure() {
                Toast.makeText(TestScoreActivity.this,"Something went wrong . . . ",Toast.LENGTH_SHORT).show();
            }
        });



    }



    private void initToSaveNewResults() {

        DatabasePannel.init_toSaveNewResult(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                saveNewResults();

            }

            @Override
            public void onFailure() {
                Toast.makeText(TestScoreActivity.this,"Something went wrong . . . ",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveNewResults() {

        DatabasePannel.save_NewResult(finalScore, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                calculateTestAverage();

            }

            @Override
            public void onFailure() {
                Toast.makeText(TestScoreActivity.this,"Something went wrong . . . ",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void calculateTestAverage() {
        DatabasePannel.calculate_TestAverage(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                updateAverage();

            }

            @Override
            public void onFailure() {
                Toast.makeText(TestScoreActivity.this,"Something went wrong . . . ",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAverage() {
        DatabasePannel.loadAverages(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                Log.d("FEEDBACK : ","Η νέα σου προσπάθεια καταμετρήθηκε! Μπορείς να δεις το νέο σου ΜΟ στις Επιδόσεις!" );

            }

            @Override
            public void onFailure() {
                Toast.makeText(TestScoreActivity.this,"Something went wrong . . . ",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void markAsChallenged() {

        DatabasePannel.mark_AsChallenged(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                Log.d("FEEDBACK : ","Challenge marked as taken" );
            }

            @Override
            public void onFailure() {
                Toast.makeText(TestScoreActivity.this,"Something went wrong . . . ",Toast.LENGTH_SHORT).show();
            }
        });
    }





    //Click Listeners for after-test actions

    public void Reattempt(View view) {
        for(int i =0;i<DatabasePannel.globalQuestionList.size();i++)
        {
            DatabasePannel.globalQuestionList.get(i).setSelectedAnswer(-1);
            DatabasePannel.globalQuestionList.get(i).setStatus(DatabasePannel.notVisited);
        }

        Intent intent = new Intent(TestScoreActivity.this,StartTestActivity.class);
        startActivity(intent);
        this.finish();
    }


    public void showAnswers(View view) {

        Intent intent = new Intent(TestScoreActivity.this,AnswersActivity.class);
        startActivity(intent);
    }


    public void goBack(View view) {

        Index.redirectActivity(this,ChallengesActivity.class);
        this.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    public void showLeaderboard(View view) {

        Intent intent = new Intent(TestScoreActivity.this,LeaderboardActivity.class);
        startActivity(intent);
        this.finish();
    }
    public void showBookmarked(View view) {
        Index.redirectActivity(this, BookmarkedQActivity.class);
        this.finish();
    }





}