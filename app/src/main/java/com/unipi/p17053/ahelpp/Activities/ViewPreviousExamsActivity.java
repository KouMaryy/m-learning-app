package com.unipi.p17053.ahelpp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unipi.p17053.ahelpp.DatabasePannel;
import com.unipi.p17053.ahelpp.R;

import java.net.URLEncoder;

public class ViewPreviousExamsActivity extends AppCompatActivity {

    WebView viewPreviousPDF;
    private String fromYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_previous_exams);

        //get the put extra
        fromYear = getIntent().getStringExtra("FROM");

        //Initialize Activity views
        viewPreviousPDF = (WebView) findViewById(R.id.viewPreviousExams);
        viewPreviousPDF.getSettings().setJavaScriptEnabled(true);

        //Show the Articles
        String positiveUrl="";
        if(fromYear.equals("themata2021"))
        {
            positiveUrl = "https://firebasestorage.googleapis.com/v0/b/ahelpp-e2abf.appspot.com/o/2021themata.pdf?alt=media&token=d46376a4-a493-4ee8-a23e-496acb869870";

        }
        else if(fromYear.equals("lyseis2021"))
        {
            positiveUrl = "https://firebasestorage.googleapis.com/v0/b/ahelpp-e2abf.appspot.com/o/2021lyseis.pdf?alt=media&token=abfaaea7-375e-41e1-9ae0-e9359afdbf5f";
        }
        else if(fromYear.equals("themata2020"))
        {
            positiveUrl = "https://firebasestorage.googleapis.com/v0/b/ahelpp-e2abf.appspot.com/o/2020themata.pdf?alt=media&token=336d2913-fedc-4fe6-9256-36445f1c94c3";
        }
        else if(fromYear.equals("lyseis2020"))
        {
            positiveUrl = "https://firebasestorage.googleapis.com/v0/b/ahelpp-e2abf.appspot.com/o/2020lyseis.pdf?alt=media&token=6b2fb3c7-72cc-434e-b662-eae00c4aaec7";
        }
        else if(fromYear.equals("themata2019"))
        {
            positiveUrl = "https://firebasestorage.googleapis.com/v0/b/ahelpp-e2abf.appspot.com/o/2019themata.pdf?alt=media&token=a345869a-6b51-47f3-ba8c-f018041435eb";
        }
        else if(fromYear.equals("lyseis2019"))
        {
            positiveUrl = "https://firebasestorage.googleapis.com/v0/b/ahelpp-e2abf.appspot.com/o/2019lyseis.pdf?alt=media&token=4e7a03b1-3967-44d0-9bbd-1b48543c3172";
        }
        else if(fromYear.equals("themata2018"))
        {
            positiveUrl = "https://firebasestorage.googleapis.com/v0/b/ahelpp-e2abf.appspot.com/o/2018themata.pdf?alt=media&token=3b793874-59ea-4206-a485-e086f93e277d";
        }
        else if(fromYear.equals("lyseis2018"))
        {
            positiveUrl = "https://firebasestorage.googleapis.com/v0/b/ahelpp-e2abf.appspot.com/o/2018lyseis.pdf?alt=media&token=c87d1e03-5a53-4cc0-a595-f746e055c97e";
        }
        else if(fromYear.equals("themata2017"))
        {
            positiveUrl = "https://firebasestorage.googleapis.com/v0/b/ahelpp-e2abf.appspot.com/o/2017themata.pdf?alt=media&token=9fa461fc-7123-4803-b9bf-d0e9c4f6a0bf";
        }
        else if(fromYear.equals("lyseis2017"))
        {
            positiveUrl = "https://firebasestorage.googleapis.com/v0/b/ahelpp-e2abf.appspot.com/o/2017lyseis.pdf?alt=media&token=524d63ff-45b7-49bf-abff-3b0c9cc5a107";
        }



        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading...");

        viewPreviousPDF.setWebViewClient(new WebViewClient()
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
            url = URLEncoder.encode(positiveUrl,"UTF-8");
        }catch (Exception ex){}

        viewPreviousPDF.loadUrl("http://docs.google.com/gview?embedded=true&url="+url);


    }

    public void ClickBackPreviousExams(View view) {
        Intent intent = new Intent(this, PreviousExamsActivity.class);
        startActivity(intent);
        this.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }



}



