package com.example.yourbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BooksCategories extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * This Activity Is for Showing Book Categories
     */


    DrawerLayout drawerLayout;
    TextView Nav_FullName, Nav_email, Nav_username;
    SharedPreferences sharedpreferences;

    // get current user
    FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    // get user Uid to be used in database reference
    final String user_uid = fuser.getUid();
    // accessing Firebase Database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //reference database to current user
    DatabaseReference reference = database.getReference("Database").child("user").child(user_uid);

    CardView history, children, sci_fi, mystery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        drawerLayout = findViewById(R.id.drawer_layout);
        //when menu icon is pressed open drawer
        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        // object of navigation View
        NavigationView navigationView = findViewById(R.id.NavigationMenu);
        // get View object of Navigation drawer header
        View headerView = navigationView.getHeaderView(0);
        Nav_FullName = (TextView) headerView.findViewById(R.id.nav_name);
        Nav_username = (TextView) headerView.findViewById(R.id.nav_username);
        Nav_email = (TextView) headerView.findViewById(R.id.nav_email);

        // call loadData function to display user info in the navigation drawer
        loadData();

        // get mystery card
        mystery = findViewById(R.id.mystery);
        mystery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call toActivity function and pass the category name to it which will be used in next activity
                toActivity("Mystery");
            }
        });
        sci_fi = findViewById(R.id.sci_fi);
        sci_fi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call toActivity function and pass the category name to it which will be used in next activity
                toActivity("Sci-Fi");
            }
        });
        history = findViewById(R.id.history_card);
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call toActivity function and pass the category name to it which will be used in next activity
                toActivity("History");
            }
        });
        children = findViewById(R.id.children);
        children.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // call toActivity function and pass the category name to it which will be used in next activity
                toActivity("Children's");
            }
        });


        //listen if any navigation menu button is pressed
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // if logout button in navigation drawer is pressed then call logout function
        switch (item.getItemId()) {
            case R.id.menuLogout: {
                logout();
                break;
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //function to start book list activity
    public void toActivity(String category) {
        Intent i = new Intent(this, Books_list.class);
        // pass category to book list activity
        sharedpreferences = getSharedPreferences("MyPrefs", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("category", category);
        editor.commit();
        startActivity(i);
    }

    // function to preform logout
    public void logout() {
        FirebaseAuth.getInstance().signOut();
        ToLoginPage();
    }

    //function to start login activity
    public void ToLoginPage() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    // Get data from database and update Navigation drawer header
    public void loadData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // create object of class User and store into it fetched data from database
                User user;
                user = dataSnapshot.getValue(User.class);
                // display user full name in navigation drawer
                Nav_FullName.setText(user.getFullName());
                Nav_username.setText(user.getUsername());
                // display user email in navigation drawer
                Nav_email.setText(user.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}