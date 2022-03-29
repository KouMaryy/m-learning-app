package com.unipi.p17053.ahelpp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unipi.p17053.ahelpp.DatabasePannel;
import com.unipi.p17053.ahelpp.MyCompleteListener;
import com.unipi.p17053.ahelpp.R;

import java.net.URLEncoder;

public class ViewTheoryActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    LinearLayout linearHome,linearTheory,linearTests,linearPrevious,linearPerformance,linearBookmarked,linearDrwrLeaderboard,linearAbout, linearLogout;
    TextView username,userCapital;
    WebView viewPDF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_theory);

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

        //Initialize Activity views
        viewPDF = (WebView) findViewById(R.id.viewPDF);
        viewPDF.getSettings().setJavaScriptEnabled(true);

        //Show the pdf file with the theory
        String lessonName = DatabasePannel.globalLessonsList.get(DatabasePannel.selected_less_index).getLessonName();
        String lessonUrl = DatabasePannel.globalLessonsList.get(DatabasePannel.selected_less_index).getUrl();
        Log.d("FEEDBACK : ",lessonUrl );
        Log.d("FEEDBACK : ",lessonName );

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle(lessonName);
        pd.setMessage("Loading...");

        viewPDF.setWebViewClient(new WebViewClient()
                                 {
                                     @Override
                                     public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                         super.onPageStarted(view, url, favicon);
                                         pd.show();
                                     }

                                     @Override
                                     public void onPageFinished(WebView view, String url) {
                                         super.onPageFinished(view, url);
                                         pd.dismiss();
                                     }
                                 }
        );

        String url ="";
        try {
            url = URLEncoder.encode(lessonUrl,"UTF-8");
        }catch (Exception ex){}

        viewPDF.loadUrl("http://docs.google.com/gview?embedded=true&url="+url);

        markAsStudied();

    }

    private void markAsStudied() {

        DatabasePannel.mark_AsStudied(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                Log.d("FEEDBACK : ","Lesson marked as studied" );
            }

            @Override
            public void onFailure() {
                Toast.makeText(ViewTheoryActivity.this,"Something went wrong . . . ",Toast.LENGTH_SHORT).show();
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