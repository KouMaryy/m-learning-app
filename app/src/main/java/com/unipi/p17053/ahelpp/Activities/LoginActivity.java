package com.unipi.p17053.ahelpp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.unipi.p17053.ahelpp.DatabasePannel;
import com.unipi.p17053.ahelpp.MyCompleteListener;
import com.unipi.p17053.ahelpp.R;

import static android.content.ContentValues.TAG;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    CardView loginBtn;
    TextView signUp, emptyEmail,emptyPassword;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize views
        email= findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.cardToButton);
        signUp = findViewById(R.id.signUp);
        emptyEmail = findViewById(R.id.emptyEmail);
        emptyPassword = findViewById(R.id.emptyPass);

        // Initialize Firebase Auth
       mAuth = FirebaseAuth.getInstance();

    }


    //Login action
    public void clickLogin(View view) {

        if(validateInput())
        {
            login();
        }

    }

    private boolean validateInput() {

        if(email.getText().toString().isEmpty())
        {
            if(!password.getText().toString().isEmpty()) emptyPassword.setVisibility(View.INVISIBLE);
            emptyEmail.setVisibility(View.VISIBLE);
            return false;
        }
        if(password.getText().toString().isEmpty())
        {
            if(!email.getText().toString().isEmpty()) emptyEmail.setVisibility(View.INVISIBLE);
            emptyPassword.setVisibility(View.VISIBLE);
            return false;
        }
        emptyEmail.setVisibility(View.INVISIBLE);
        emptyPassword.setVisibility(View.INVISIBLE);
        return true;

    }

    private void login() {

        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Toast.makeText(LoginActivity.this,"Καλώς ήρθες !!",Toast.LENGTH_SHORT).show();

                            DatabasePannel.loadData(new MyCompleteListener() {
                                @Override
                                public void onSuccess() {
                                    Intent intent = new Intent(getApplicationContext(), Index.class);
                                    startActivity(intent);
                                    LoginActivity.this.finish();
                                }

                                @Override
                                public void onFailure() {
                                    Toast.makeText(LoginActivity.this,"Something went wrong . . . ",Toast.LENGTH_SHORT).show();
                                }
                            });



                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "loginUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this,"Αποτυχία Σύνδεσης!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void clickSignup(View view) {
        Index.redirectActivity(this,SignupActivity.class);
    }
}