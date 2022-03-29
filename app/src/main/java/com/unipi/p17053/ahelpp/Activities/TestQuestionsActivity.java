package com.unipi.p17053.ahelpp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.unipi.p17053.ahelpp.Adapters.QuestionAdapter;
import com.unipi.p17053.ahelpp.Adapters.QuestionInfoGridAdapter;
import com.unipi.p17053.ahelpp.DatabasePannel;
import com.unipi.p17053.ahelpp.R;

import java.util.concurrent.TimeUnit;

import static com.unipi.p17053.ahelpp.DatabasePannel.globalBookmrkIDList;
import static com.unipi.p17053.ahelpp.DatabasePannel.globalLessonsList;
import static com.unipi.p17053.ahelpp.DatabasePannel.globalQuestionList;
import static com.unipi.p17053.ahelpp.DatabasePannel.globalTestList;
import static com.unipi.p17053.ahelpp.DatabasePannel.notAnswered;
import static com.unipi.p17053.ahelpp.DatabasePannel.notVisited;
import static com.unipi.p17053.ahelpp.DatabasePannel.selected_less_index;
import static com.unipi.p17053.ahelpp.DatabasePannel.selected_test_index;


public class TestQuestionsActivity extends AppCompatActivity {

    private RecyclerView testQuestionView;
    private ImageView testBookmark,testQuestionsInfo,closeDrwrBtn;
    private TextView remainingTime,testQInfoLesson,testQInfoTest, tqID;
    private CardView submitTestBtn;
    private DrawerLayout infoDrawer;
    private GridView infoGrid;
    private CountDownTimer timer;
    private int testQID;
    private QuestionInfoGridAdapter questionInfoGridAdapter;
    private long timeLeft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_info_layout);

        //initialize views
        testQInfoLesson = findViewById(R.id.testQInfoLesson);
        testQuestionView = findViewById(R.id.testQuestion);
        testBookmark= findViewById(R.id.testBookmark);
        remainingTime = findViewById(R.id.remainingTime);
        testQuestionsInfo = findViewById(R.id.testQuestionsInfo);
        testQInfoTest = findViewById(R.id.testQInfoTest);
        submitTestBtn= findViewById(R.id.cardToSubmitButton);
        infoDrawer = findViewById(R.id.infoDrawer);
        infoGrid = findViewById(R.id.infoGrid);
        closeDrwrBtn = findViewById(R.id.closeDrwrBtn);
        tqID = findViewById(R.id.tqID);
        testQID=0;

        //Initialize view with their values

        tqID.setText(1+"/"+String.valueOf(globalQuestionList.size()));
        testQInfoLesson.setText(globalLessonsList.get(selected_less_index).getLessonName());
        //testQInfoTest.setText("Challenge "+String.valueOf(DatabasePannel.selected_test_index+1));
        String currentTestID = DatabasePannel.globalTestList.get(DatabasePannel.selected_test_index).getTestId();
        int helper = Integer.parseInt(currentTestID.substring(currentTestID.length()-1));
        testQInfoTest.setText("Challenge "+helper);
        globalQuestionList.get(0).setStatus(notAnswered);

        if(globalQuestionList.get(0).isBookmarked())
        {
            testBookmark.setImageResource(R.drawable.ic_bookmark_filled);
        }
        else
        {
            testBookmark.setImageResource(R.drawable.ic_bookmark_border);
        }




        //Show Test Questions
        QuestionAdapter questionAdapter = new QuestionAdapter(DatabasePannel.globalQuestionList);
        testQuestionView.setAdapter(questionAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        testQuestionView.setLayoutManager(layoutManager);

        //Show Test Questions Information
        questionInfoGridAdapter = new QuestionInfoGridAdapter(this,globalQuestionList.size());
        infoGrid.setAdapter(questionInfoGridAdapter);
        
        //Navigation Helper
        setSnapHelper();

        //For time countdown
        startTimer();

        //for bookmark
        testBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToBookmarked();
            }
        });

        
    }

    //Easier Navigation between questions and current question number demonstration
    private void setSnapHelper() {

        final SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(testQuestionView);

        testQuestionView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());
                testQID = recyclerView.getLayoutManager().getPosition(view);

                if(globalQuestionList.get(testQID).getStatus()==notVisited)
                {
                    globalQuestionList.get(testQID).setStatus(notAnswered);
                }

                tqID.setText(String.valueOf(testQID+1)+"/"+String.valueOf(globalQuestionList.size()));

                if(globalQuestionList.get(testQID).isBookmarked())
                {
                    testBookmark.setImageResource(R.drawable.ic_bookmark_filled);
                }
                else
                {
                    testBookmark.setImageResource(R.drawable.ic_bookmark_border);
                }


            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }


    // On Click Listeners for test progress information
    public void ShowTestQuestionsInfo(View view) {
        if(! infoDrawer.isDrawerOpen(GravityCompat.END))
        {
            questionInfoGridAdapter.notifyDataSetChanged();
            infoDrawer.openDrawer(GravityCompat.END);
        }
    }
    public void HideTestQuestionsInfo(View view) {
        if(infoDrawer.isDrawerOpen(GravityCompat.END)) infoDrawer.closeDrawer(GravityCompat.END);
    }


    public void goToQuestion(int position)
    {
        testQuestionView.smoothScrollToPosition(position);
        if(infoDrawer.isDrawerOpen(GravityCompat.END)) infoDrawer.closeDrawer(GravityCompat.END);
    }

    //On click LISTENERS FOR ACTIONs DURING TEST

    public void submitTest(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Υποβολή");
        builder.setMessage("Είσαι σίγουρος πως έχεις ολοκληρώσει αυτό το Challenge ; ");
        builder.setIcon(getResources().getDrawable(R.drawable.ic_android_face_launcher_foreground));

        //Positive answer
        builder.setPositiveButton("ΝΑΙ", (dialog, which) -> {

            timer.cancel();
            //Dismiss dialog and continue to the score activity
            dialog.dismiss();

            Intent intent = new Intent(TestQuestionsActivity.this,TestScoreActivity.class);
            long totalTime = globalTestList.get(selected_test_index).getTime()*60*1000;
            intent.putExtra("TIME_TAKEN",totalTime-timeLeft);
            startActivity(intent);

            TestQuestionsActivity.this.finish();

        });

        //Negative answer
        builder.setNegativeButton("ΟΧΙ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Dismiss dialog and continue with the test
                dialog.dismiss();

            }
        });

        //show dialog
        builder.show();

    }

    private void startTimer()
    {
        //test's total time in milliseconds
        long totalTime = globalTestList.get(selected_test_index).getTime()*60*1000;

        timer = new CountDownTimer(totalTime,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                timeLeft = millisUntilFinished;

                String time = String.format("%02d:%02d '",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)-
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                );

                remainingTime.setText(time);

            }

            @Override
            public void onFinish() {

                AlertDialog.Builder builder = new AlertDialog.Builder(TestQuestionsActivity.this);
                builder.setTitle(" - Τέλος Χρόνου - ");
                builder.setMessage("Ο διαθέσιμος χρόνος για αυτό το Challenge έφτασε στο τέλος του. Μην ανησυχείς, όλες οι απαντήσεις που έχεις υποβάλλει θα καταμετρηθούν κανονικά! Μπορείς πάντοτε να ξαναπροσπαθήσεις! Καλή επιτυχία! ");
                builder.setIcon(getResources().getDrawable(R.drawable.ic_android_face_launcher_foreground));

                //Positive answer
                builder.setPositiveButton("ΟΚ", (dialog, which) -> {

                    //Dismiss dialog and continue to the score activity
                    dialog.dismiss();
                    Intent intent = new Intent(TestQuestionsActivity.this,TestScoreActivity.class);
                    long totalTime = globalTestList.get(selected_test_index).getTime()*60*1000;
                    intent.putExtra("TIME_TAKEN",totalTime-timeLeft);
                    startActivity(intent);

                    TestQuestionsActivity.this.finish();

                });

                //show dialog
                builder.show();





            }
        };

        timer.start();

    }


    public void addToBookmarked()
    {

        if(globalQuestionList.get(testQID).isBookmarked())
        {
            globalQuestionList.get(testQID).setBookmarked(false);
            testBookmark.setImageResource(R.drawable.ic_bookmark_border);
            //remove from the list of bookmarked questions
            globalBookmrkIDList.remove(DatabasePannel.globalQuestionList.get(testQID).getqID());
            DatabasePannel.profile.setBookmarksCount(globalBookmrkIDList.size());
        }
        else
        {
            globalQuestionList.get(testQID).setBookmarked(true);
            testBookmark.setImageResource(R.drawable.ic_bookmark_filled);
            //add to the list of bookmarked questions
            globalBookmrkIDList.add(DatabasePannel.globalQuestionList.get(testQID).getqID());
            DatabasePannel.profile.setBookmarksCount(globalBookmrkIDList.size());
        }
    }



}