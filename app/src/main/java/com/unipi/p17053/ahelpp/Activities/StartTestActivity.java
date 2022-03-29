package com.unipi.p17053.ahelpp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.unipi.p17053.ahelpp.DatabasePannel;
import com.unipi.p17053.ahelpp.MyCompleteListener;
import com.unipi.p17053.ahelpp.R;

public class StartTestActivity extends AppCompatActivity {

    TextView lessonName_st,testName,totalTestQuest,testScore, testTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);

        //Initialize activity views
        lessonName_st = findViewById(R.id.lessonName_st);
        testName = findViewById(R.id.testName);
        totalTestQuest= findViewById(R.id.totalTestQuest);
        testScore= findViewById(R.id.testScore);
        testTime= findViewById(R.id.testTime);

        DatabasePannel.loadTestQuestions(new MyCompleteListener(){
            @Override
            public void onSuccess() {
                setData();
            }

            @Override
            public void onFailure() {
                Toast.makeText(StartTestActivity.this,"Something went wrong . . . ",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setData()
    {
        String currentTestID = DatabasePannel.globalTestList.get(DatabasePannel.selected_test_index).getTestId();
        Log.d("FEEDBACK : Start Test Activity Test ID ", currentTestID);

        lessonName_st.setText(DatabasePannel.globalLessonsList.get(DatabasePannel.selected_less_index).getLessonName());
        //testName.setText("Challenge "+String.valueOf(DatabasePannel.selected_test_index+1));

        int helper2 = Integer.parseInt(currentTestID.substring(currentTestID.length()-1));
        testName.setText("Challenge "+helper2);
        totalTestQuest.setText(String.valueOf(DatabasePannel.globalQuestionList.size()));
        testScore.setText(String.valueOf(DatabasePannel.globalTestList.get(DatabasePannel.selected_test_index).getTopScore()));
        testTime.setText(String.valueOf(DatabasePannel.globalTestList.get(DatabasePannel.selected_test_index).getTime())+ " '");


    }

    public void clickBack(View view) {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void startTheTest(View view) {

        Index.redirectActivity(this, TestQuestionsActivity.class);
        finish();

    }
}