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
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.unipi.p17053.ahelpp.Adapters.QuestionAdapter;
import com.unipi.p17053.ahelpp.Adapters.QuestionInfoGridAdapter;
import com.unipi.p17053.ahelpp.DatabasePannel;
import com.unipi.p17053.ahelpp.R;

import static com.unipi.p17053.ahelpp.DatabasePannel.globalBookmrkIDList;
import static com.unipi.p17053.ahelpp.DatabasePannel.globalQuestionList;
import static com.unipi.p17053.ahelpp.DatabasePannel.notAnswered;
import static com.unipi.p17053.ahelpp.DatabasePannel.notVisited;

public class ElementTestQuestionsActivity extends AppCompatActivity {

    private RecyclerView testQuestionView;
    private ImageView testBookmark,testQuestionsInfo,closeDrwrBtn;
    private TextView  tqID;
    private CardView submitTestBtn;
    private DrawerLayout infoDrawer;
    private GridView infoGrid;
    private int testQID;
    private QuestionInfoGridAdapter questionInfoGridAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.element_questions_info_layout);

        //initialize views
        testQuestionView = findViewById(R.id.testQuestionB);
        testBookmark= findViewById(R.id.testBookmarkB);
        testQuestionsInfo = findViewById(R.id.testQuestionsInfoB);
        submitTestBtn= findViewById(R.id.cardToSubmitButtonB);
        infoDrawer = findViewById(R.id.infoDrawerB);
        infoGrid = findViewById(R.id.infoGrid);
        closeDrwrBtn = findViewById(R.id.closeDrwrBtnB);
        tqID = findViewById(R.id.tqIDB);
        testQID=0;

        //Initialize view with their values

        tqID.setText(1+"/"+String.valueOf(globalQuestionList.size()));
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
    public void ShowTestQuestionsInfoB(View view) {
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
        builder.setIcon(getResources().getDrawable(R.drawable.ic_android_face_launcher_foreground));
        builder.setMessage("Είσαι σίγουρος πως έχεις ολοκληρώσει αυτό το Challenge ; ");

        //Positive answer
        builder.setPositiveButton("ΝΑΙ", (dialog, which) -> {

            //Dismiss dialog and continue to the score activity
            dialog.dismiss();

            Intent intent = new Intent(ElementTestQuestionsActivity.this, ElementTestScoreActivity.class);
            startActivity(intent);

            ElementTestQuestionsActivity.this.finish();

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