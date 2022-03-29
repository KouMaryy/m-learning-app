package com.unipi.p17053.ahelpp.Activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unipi.p17053.ahelpp.DatabasePannel;
import com.unipi.p17053.ahelpp.MyCompleteListener;
import com.unipi.p17053.ahelpp.R;

public class SplashActivity extends AppCompatActivity {

    AnimationDrawable gradientAnim;
    LinearLayout splash;
    FirebaseAuth mAuth;
    TextView myAppName,myCopyright;
    ImageView myLogo;
    Animation top,bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Initialize views
        splash = findViewById(R.id.splash);
        myAppName = findViewById(R.id.myAppName);
        myCopyright = findViewById(R.id.myCopyright);
        myLogo = findViewById(R.id.myLogo);
        top = AnimationUtils.loadAnimation(this,R.anim.top);
        bottom= AnimationUtils.loadAnimation(this,R.anim.bottom);
        gradientAnim = (AnimationDrawable) splash.getBackground();


        //Movement animation activation
        myLogo.setAnimation(top);
        myAppName.setAnimation(bottom);
        myCopyright.setAnimation(bottom);


        //Gradient animation activation
        gradientAnim.setEnterFadeDuration(10);
        gradientAnim.setExitFadeDuration(1000);
        gradientAnim.start();

        //Initializations for Firebase
        mAuth = FirebaseAuth.getInstance();
        DatabasePannel.globalFirestore = FirebaseFirestore.getInstance();


        //Splash screen projection
        new Thread()
        {
            @Override
            public void run ()
            {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //Avoid login if current user is already connected
                if(mAuth.getCurrentUser() != null)

                {
                    DatabasePannel.loadData(new MyCompleteListener() {
                        @Override
                        public void onSuccess() {
                            Log.d("FEEDBACK : ",mAuth.getCurrentUser().toString());
                            Index.redirectActivity(SplashActivity.this,Index.class);
                            SplashActivity.this.finish();
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(SplashActivity.this,"Something went wrong . . . ",Toast.LENGTH_SHORT).show();
                        }
                    });
                }


                else
                {
                    Index.redirectActivity(SplashActivity.this,LoginActivity.class);
                    SplashActivity.this.finish();

                }


            }
        }.start();

    }
}
