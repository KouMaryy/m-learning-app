package com.unipi.p17053.ahelpp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.unipi.p17053.ahelpp.R;

import java.net.URLEncoder;

public class ViewStayPositiveActivity extends AppCompatActivity {

    WebView viewPositivePDF;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stay_positive);

        //get the put extra
        from = getIntent().getStringExtra("FROM");

        //Initialize Activity views
        viewPositivePDF = (WebView) findViewById(R.id.viewPositivePDF);
        viewPositivePDF.getSettings().setJavaScriptEnabled(true);

        //Show the Articles
        String positiveUrl="";
        if(from.equals("stress"))
        {
            positiveUrl = "https://firebasestorage.googleapis.com/v0/b/ahelpp-e2abf.appspot.com/o/stress.pdf?alt=media&token=3a01382f-b9bb-4f4d-82ad-8b8cba359891";

        }
        else if(from.equals("symptoms"))
        {
            positiveUrl = "https://firebasestorage.googleapis.com/v0/b/ahelpp-e2abf.appspot.com/o/symptoms.pdf?alt=media&token=70f847b3-6966-4911-828e-712906579691";
        }
        else if(from.equals("food"))
        {
            positiveUrl = "https://firebasestorage.googleapis.com/v0/b/ahelpp-e2abf.appspot.com/o/food.pdf?alt=media&token=e4c5a560-36bd-482f-a0ce-c9830cdffb5d";
        }
        else if(from.equals("covid"))
        {
            positiveUrl = "https://firebasestorage.googleapis.com/v0/b/ahelpp-e2abf.appspot.com/o/covid.pdf?alt=media&token=f44f9dc5-7240-4c76-982c-36c6bf993191";
        }
        else if(from.equals("music"))
        {
            positiveUrl = "https://firebasestorage.googleapis.com/v0/b/ahelpp-e2abf.appspot.com/o/music.pdf?alt=media&token=05c802a2-d7d8-4db3-bde5-45b8108ea171";
        }


        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Stay Positive");
        pd.setMessage("Loading...");

        viewPositivePDF.setWebViewClient(new WebViewClient()
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

        viewPositivePDF.loadUrl("http://docs.google.com/gview?embedded=true&url="+url);


    }

    public void ClickBackPositive(View view) {
        Intent intent = new Intent(this, StayPositiveActivity.class);
        startActivity(intent);
        this.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}