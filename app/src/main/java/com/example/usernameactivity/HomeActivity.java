package com.example.usernameactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ImageButton imageButton;
    ChatFragment chatFragment;
    ProfileFragment profileFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        btnSearch();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.menu_chat){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.FrameLayout,chatFragment)
                            .commit();
                }
                if (item.getItemId()==R.id.menu_profile){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.FrameLayout,profileFragment)
                            .commit();
                }
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.menu_chat);
    }
    private void init(){
        chatFragment=new ChatFragment();
        profileFragment=new ProfileFragment();
        bottomNavigationView=findViewById(R.id.BottomNavigation);
        imageButton=findViewById(R.id.btnSearch);
    }
    private void btnSearch(){
        imageButton.setOnClickListener(v -> {
            Intent i=new Intent(HomeActivity.this,SearchActivity.class);
            startActivity(i);
        });
    }
}