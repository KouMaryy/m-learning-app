package com.unipi.p17053.ahelpp.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class SignupActivity extends AppCompatActivity {

    EditText name,fullname,email,password,passConf;
    CardView signupBtn;
    TextView  emptyField;
    ImageView ic_back;
    private FirebaseAuth mAuth;
    String nameStr,fullnameStr,emailStr, passwordStr,passConfStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Initialize views
        email= findViewById(R.id.email);
        password = findViewById(R.id.password);
        passConf = findViewById(R.id.passwordConf);
        name= findViewById(R.id.name);
        fullname = findViewById(R.id.fullname);
        signupBtn = findViewById(R.id.cardToButton);
        emptyField = findViewById(R.id.emptyField);
        ic_back = findViewById(R.id.ic_back);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    //SignUp action
    public void clickSignup(View view) {
        if(validateInput())
        {
           signupNewUser();
        }

    }

    private boolean validateInput() {

        nameStr = name.getText().toString().trim();
        fullnameStr = fullname.getText().toString().trim();
        emailStr = email.getText().toString().trim();
        passwordStr = password.getText().toString().trim();
        passConfStr = passConf.getText().toString().trim();

        if(nameStr.isEmpty() || fullnameStr.isEmpty() || emailStr.isEmpty() || passwordStr.isEmpty() || passConfStr.isEmpty())
        {
            emptyField.setVisibility(View.VISIBLE);
            return false;
        }
        if (passwordStr.compareTo(passConfStr) != 0)
        {
            Toast.makeText(SignupActivity.this,"Σιγουρέψου πως οκωδικός σου είναι σωστός!",Toast.LENGTH_SHORT).show();
            return false;
        }

        emptyField.setVisibility(View.INVISIBLE);
        nameStr = nameStr + " " +fullnameStr;
        return true;



    }

    private void signupNewUser() {

        mAuth.createUserWithEmailAndPassword(emailStr, passwordStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Toast.makeText(SignupActivity.this,"Επιτυχής εγγραφή. Καλώς ήρθες!!",Toast.LENGTH_SHORT).show();
                            DatabasePannel.createUserData(emailStr,nameStr, new MyCompleteListener(){

                                @Override
                                public void onSuccess() {

                                    DatabasePannel.loadData(new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {
                                            Index.redirectActivity(SignupActivity.this,Index.class);
                                            SignupActivity.this.finish();
                                        }

                                        @Override
                                        public void onFailure() {
                                            Toast.makeText(SignupActivity.this,"Something went wrong . . . ",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }

                                @Override
                                public void onFailure() {
                                    Toast.makeText(SignupActivity.this,"Something went wrong . . . ",Toast.LENGTH_SHORT).show();

                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this,"Αποτυχία Εγγραφής!",Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    public void clickBack(View view) {
        finish();
    }
}