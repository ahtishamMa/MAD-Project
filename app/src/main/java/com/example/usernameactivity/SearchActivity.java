package com.example.usernameactivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private EditText SearchBar;
    private ImageButton btn_Search,btnBack;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private UsersAdapter usersAdapter;
    private List<String> searchResultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        searchResultsList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("usernames");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersAdapter = new UsersAdapter(searchResultsList);
        recyclerView.setAdapter(usersAdapter);
        btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String queryText = SearchBar.getText().toString().trim();
                if (!queryText.isEmpty()) {
                    searchUsersInFirebase(queryText);
                } else {
                    Toast.makeText(SearchActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        usersAdapter.setOnItemClickListener(new UsersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String username) {
                Intent intent = new Intent(SearchActivity.this, ChatActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }

    private void searchUsersInFirebase(String queryText) {
        Query query = databaseReference.orderByValue().equalTo(queryText);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                searchResultsList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String username = userSnapshot.getValue(String.class);
                    if (username != null) {
                        searchResultsList.add(username);
                    }
                }
                if (searchResultsList.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "No user found", Toast.LENGTH_SHORT).show();
                }
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void init(){
        SearchBar = findViewById(R.id.Searchbar);
        btn_Search = findViewById(R.id.btn_Search);
        recyclerView = findViewById(R.id.SearchUser_recyclerview);
        btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}