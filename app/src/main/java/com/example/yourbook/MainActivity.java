package com.example.yourbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    /**
     * Log in screen to application with e-mail and password
     */

    // declare variables
    TextView sign;
    Button log;
    EditText email, password;
    // create email pattern to limit user input
    // make him write english characters only.
    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    // loading progress dialog to be shown while waiting signing in to complete
    ProgressDialog progressDialog;
    // connect with firebase authentication
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // attach each layout to it's id
        sign = findViewById(R.id.sign);
        email = findViewById(R.id.email1);
        log = findViewById(R.id.login);
        password = findViewById(R.id.password1);
        // get details from firebase
        mAuth = FirebaseAuth.getInstance();
        // make progressDialog to be shown in MainActivity
        progressDialog = new ProgressDialog(this);
        // create a access to firebase
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        // if there is any user information attached, program will be moved to next activity
        if (currentUser != null) {
            nextActivity();
            finish();
        }
        // switch to register activity
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, register.class));
            }
        });
        // log in progress
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                perforLogin();
            }
        });
    }

    // method to check log in progress
    private void perforLogin() {
        // get e-mail and password from user
        String emaill = email.getText().toString();
        String pass = password.getText().toString();

        // check email pattern if it entered in correct way, and show a warning
        if (!emaill.matches(emailpattern)) {
            email.setError("Enter Correct Email");
            return;
        }
        // check password if it entered in correct way, and show a warning
        if (pass.isEmpty() || pass.length() < 6) {
            password.setError("Password must be at least 6 characters");
            return;
        }
        // send a progressDialog to user if there are no errors in patterns
        else {
            progressDialog.setMessage("Loading...");
            progressDialog.setTitle("Logging in");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            // log in with e-mail and password
            mAuth.signInWithEmailAndPassword(emaill, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // if user logged in, send a toast with successfully log in
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        nextActivity();
                        Toast.makeText(MainActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                    }
                    // if e-mail or password did not match, send a toast with invalid inputs
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    // moving from log in screen to book categories after successful log in
    private void nextActivity() {
        Intent intent = new Intent(MainActivity.this, BooksCategories.class);
        startActivity(intent);
        finish();
    }

}


