package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText mloginemail, mloginpass;
    private TextView mforgotpass;
    private RelativeLayout mlogin, mnewuser;

    private FirebaseAuth firebaseAuth;

    private ProgressBar mprogressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mloginemail = findViewById(R.id.loginemail);
        mloginpass = findViewById(R.id.loginpass);
        mforgotpass = findViewById(R.id.forgotpass);
        mlogin = findViewById(R.id.login);
        mnewuser = findViewById(R.id.newUser);
        mprogressbar = findViewById(R.id.progressOfMain);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        //  If the user is already logged in then skip the login process
        if(firebaseUser!=null){
            finish();
            startActivity(new Intent(MainActivity.this, Notes.class));
        }

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = mloginemail.getText().toString().trim();
                String pass = mloginpass.getText().toString().trim();
                // Login code
                if(mail.isEmpty() || pass.isEmpty())
                    Toast.makeText(getApplicationContext(), "All Fields are Required!", Toast.LENGTH_SHORT).show();
                else{
                    // Login the User
                    mprogressbar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                checkMailVerification();
                            }else{
                                mprogressbar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Account doesn't Exists!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
//                Toast.makeText(getApplicationContext(), "Login button Clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        mforgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, forgotpassword.class);
                startActivity(intent);
                finish();
            }
        });

        mnewuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, signup.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void checkMailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser.isEmailVerified()){
            Toast.makeText(getApplicationContext(), "Login Successfull!", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this, Notes.class));
        }else{
            mprogressbar.setVisibility(View.INVISIBLE
            );
            Toast.makeText(getApplicationContext(), "Verify your email to continue login!", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}