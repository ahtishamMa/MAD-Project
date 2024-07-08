package com.example.usernameactivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private TextView usernameTextView;
    private RecyclerView messagesRecyclerView;
    private EditText messageEditText;
    private ImageButton btn_Send,btnBack;
    private DatabaseReference databaseReference;
    private List<String> messagesList;
    private MessagesAdapter messagesAdapter;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
        messagesList = new ArrayList<>();

        username = getIntent().getStringExtra("username");
        usernameTextView.setText(username);

        databaseReference = FirebaseDatabase.getInstance().getReference("messages").child(username);
        messagesAdapter = new MessagesAdapter(this, messagesList);
        messagesRecyclerView.setAdapter(messagesAdapter);

        loadMessagesFromFirebase();

        btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString().trim();
                if (!message.isEmpty()) {
                    sendMessageToFirebase(message);
                } else {
                    Toast.makeText(ChatActivity.this, "Please enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadMessagesFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesList.clear();
                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    String message = messageSnapshot.getValue(String.class);
                    if (message != null) {
                        messagesList.add(message);
                    }
                }
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessageToFirebase(String message) {
        String messageId = databaseReference.push().getKey();
        if (messageId != null) {
            databaseReference.child(messageId).setValue(message).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    messageEditText.setText("");
                    loadMessagesFromFirebase();
                } else {
                    Toast.makeText(ChatActivity.this, "Error sending message", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void init(){
        usernameTextView = findViewById(R.id.textView);
        messagesRecyclerView=findViewById(R.id.MessagesRecyclerView);
        messageEditText=findViewById(R.id.WriteMessage);
        btn_Send=findViewById(R.id.btn_Send);
        btnBack=findViewById(R.id.btnBack);
    }
}