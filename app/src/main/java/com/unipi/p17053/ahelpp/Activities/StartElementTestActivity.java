package com.unipi.p17053.ahelpp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.unipi.p17053.ahelpp.DatabasePannel;
import com.unipi.p17053.ahelpp.MyCompleteListener;
import com.unipi.p17053.ahelpp.R;

import static com.unipi.p17053.ahelpp.DatabasePannel.globalTestList;
import static com.unipi.p17053.ahelpp.DatabasePannel.selected_test_index;

public class StartElementTestActivity extends AppCompatActivity {

    TextView totalTestQuest,testType, challengeWithIm,alert;
    Button startTestB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_element_test);

        //Initialize activity views
        totalTestQuest= findViewById(R.id.totalTestQuestB);
        testType = findViewById(R.id.disclaimer2);
        challengeWithIm = findViewById(R.id.disclaimer1);
        startTestB = findViewById(R.id.startTestB);
        alert = findViewById(R.id.alert);

        if(DatabasePannel.clickedFromElement.equals("Challenge Αποθηκευμένων Ερωτήσεων"))
        {
            DatabasePannel.loadBookmarkedTestQuestions(new MyCompleteListener(){
                @Override
                public void onSuccess() {
                    setData();
                }

                @Override
                public void onFailure() {
                    Toast.makeText(StartElementTestActivity.this,"Something went wrong . . . ",Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if (DatabasePannel.clickedFromElement.equals("Challenge Αδυναμιών"))
        {
            DatabasePannel.loadWrongTestQuestions(new MyCompleteListener(){
                @Override
                public void onSuccess() {
                    setData();
                }

                @Override
                public void onFailure() {
                    Toast.makeText(StartElementTestActivity.this,"Something went wrong . . . ",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void setData()
    {
        if(DatabasePannel.clickedFromElement.equals("Challenge Αποθηκευμένων Ερωτήσεων"))
        {
            testType.setText("Αποθηκευμένων Ερωτήσεων");
            challengeWithIm.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_bookmark_filled_black, 0, 0, 0);

        }
        else if(DatabasePannel.clickedFromElement.equals("Challenge Αδυναμιών"))
        {
            testType.setText("Αδυναμιών");
            challengeWithIm.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_heal_black, 0, 0, 0);

        }

        totalTestQuest.setText(String.valueOf(DatabasePannel.globalQuestionList.size()));
        if (String.valueOf(DatabasePannel.globalQuestionList.size()).equals("0"))
        {
            //Change button color and disable it
            Drawable buttonDrawable = startTestB.getBackground();
            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
            DrawableCompat.setTint(buttonDrawable, getResources().getColor(R.color.disbledBtn));
            startTestB.setBackground(buttonDrawable);
            startTestB.setEnabled(false);

            //Show a disclaimer that there aren't any questions
            alert.setVisibility(View.VISIBLE);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Καλωσήρθες!");
            builder.setMessage("Δεν έχεις ακόμη διαθέσιμες ερωτήσεις για αυτό το Challenge. Ξεκίνα την Εξάσκηση και το Challenge θα διαμορφωθεί ανάλογα με τις ανάγκες σου.");
            builder.setIcon(getResources().getDrawable(R.drawable.ic_android_face_launcher_foreground));

            //Positive answer
            builder.setPositiveButton("Εξάσκηση", (dialog, which) -> {

                //Dismiss dialog and continue to the score activity
                dialog.dismiss();

                Intent intent = new Intent(StartElementTestActivity.this,Test.class);
                startActivity(intent);

                StartElementTestActivity.this.finish();

            });
            //Positive answer
            builder.setNegativeButton("Αρχική", (dialog, which) -> {

                //Dismiss dialog and continue to the score activity
                dialog.dismiss();

                Intent intent = new Intent(StartElementTestActivity.this,Index.class);
                startActivity(intent);

                StartElementTestActivity.this.finish();

            });

            //show dialog
            builder.show();
        }
        else
        {
            //Change button color and enable it
            Drawable buttonDrawable = startTestB.getBackground();
            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
            DrawableCompat.setTint(buttonDrawable, Color.BLACK);
            startTestB.setBackground(buttonDrawable);
            startTestB.setEnabled(true);

            //Hide the disclaimer
            alert.setVisibility(View.INVISIBLE);

        }

    }

    public void clickBackB(View view) {
        finish();
    }

    public void startTheTestB(View view) {

        Index.redirectActivity(this, ElementTestQuestionsActivity.class);
        finish();
    }
}