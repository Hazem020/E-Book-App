package com.example.yourbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Books_list extends AppCompatActivity {
    /**
     * this class generate books from category url,
     * which are saved in firebase
     **/

    // declare variables
    String category;
    DatabaseReference ref;
    MainAdapter mainAdapter;
    RecyclerView recyclerView;
    ImageView back;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);
        //when back button is pressed return to previous activity
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sharedpreferences = getSharedPreferences("MyPrefs", this.MODE_PRIVATE);
        // get category from book categories activity
        category = (sharedpreferences.getString("category",""));
        // make database reference to category child
        ref = FirebaseDatabase.getInstance().getReference("Database").child("Books").child(category);
        // create recyclerView object
        recyclerView = (RecyclerView) findViewById(R.id.bookList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // get current category books from database
        FirebaseRecyclerOptions<Book> options =
                new FirebaseRecyclerOptions.Builder<Book>()
                        .setQuery(ref, Book.class)
                        .build();
        // create mainAdapter object and pass books to it , which will display books in recyclerView
        mainAdapter = new MainAdapter(options);
        // set recyclerView Adapter to Main mainAdapter
        recyclerView.setAdapter(mainAdapter);

    }

    // when activity is started mainAdapter will return each book in current category from database and display it in recyclerView
    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

}