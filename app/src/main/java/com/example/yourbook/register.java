package com.example.yourbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class register extends AppCompatActivity {
    /**
     * register screen to application with fullName, userName, email and  password
     * each account has a unique id created by firebase
     */

    // declare variables
    EditText fullName, userName, Email, password, confPas;
    Button signUp;
    TextView HaveAccount;
    // create email pattern to limit user input
    // make him write english characters only.
    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    // loading progress dialog to be shown while waiting signing in to complete
    ProgressDialog progressDialog;
    // connect with firebase authentication
    FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("Database");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // attach each layout to it's id
        HaveAccount = findViewById(R.id.HaveAccount);
        fullName = findViewById(R.id.name);
        userName = findViewById(R.id.user);
        Email = findViewById(R.id.email);
        password = findViewById(R.id.pas);
        confPas = findViewById(R.id.pas2);
        signUp = findViewById(R.id.sign);
        // create a access to firebase
        mAuth = FirebaseAuth.getInstance();
        // make progressDialog to be shown in register Activity
        progressDialog = new ProgressDialog(this);

        // if user already have account he can ignore registration and move to log in activity
        HaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(register.this, MainActivity.class));
            }
        });
        // sign up button to create account
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerforAuth();
            }
        });
    }

    // we created function to check important constraints to sign up
    private void PerforAuth() {
        // get e-mail and password
        String email = Email.getText().toString();
        String pas = password.getText().toString();
        String confirmpas = confPas.getText().toString();

        // if e-mail entered with a incorrect way, send a warning to user
        if (!email.matches(emailpattern)) {
            Email.setError("Enter Correct Email");
            return;
        }
        // if password entered with a incorrect way, send a warning to user
        if (pas.isEmpty() || pas.length() < 6) {
            password.setError("Password must be at least 6 characters");
            return;
        }
        // if confirmation does not match with password, send a warning
        if (!pas.equals(confirmpas)) {
            confPas.setError("password doesn't match");
            return;
        }
        // send a progressDialog to user if there are no errors in patterns
        else {
            progressDialog.setMessage("Registering...");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            // create account with e-mail and password only
            mAuth.createUserWithEmailAndPassword(email, pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    // if task completed, send a toast to user
                    if (task.isSuccessful()) {
                        Toast.makeText(register.this, "Email created", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        // move to categories activity
                        nextActivity();
                        // store the data we have entered to the database
                        StoreData();
                    }
                    // send a warning to user if e-mail were used before
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(register.this, "Email address is already taken!", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

    }

    // method to store data in database
    private void StoreData() {
        // get Full-name, username and email of user
        String Fullname = fullName.getText().toString();
        String Username = userName.getText().toString();
        String email = Email.getText().toString();
        // create an object of current user to connect to firebase
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        // create an object of Class user
        User user = new User();
        // set name, username and email
        user.setFullName(Fullname);
        user.setUsername(Username);
        user.setEmail(email);
        // set values at database
        reference.child("user").child(fuser.getUid()).setValue(user);
    }

    // switch from this activity to categories activity
    private void nextActivity() {
        Intent intent = new Intent(register.this, BooksCategories.class);
        startActivity(intent);
        finish();
    }

}

