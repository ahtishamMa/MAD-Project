package com.example.usernameactivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

public class LoginNumber extends AppCompatActivity {
    EditText etMobile;
    Button btnOTP;
    CountryCodePicker countryCode;
    ProgressBar ProgressbarPN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_number);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        btnOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = etMobile.getText().toString().trim();
                if (!countryCode.isValidFullNumber()) {
                    etMobile.setError("Phone number is not valid!");
                    return;
                }
                saveNumberToFirebase(phoneNumber);
                ProgressbarPN.setVisibility(View.VISIBLE);
            }
        });
    }
    private void saveNumberToFirebase(String phoneNumber) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("usernames");
        myRef.push().setValue(phoneNumber).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(LoginNumber.this, "Phone number saved", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginNumber.this, OTP_Activity.class);
                intent.putExtra("phoneNumber",countryCode.getFullNumberWithPlus());
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(LoginNumber.this, "Failed to save number", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init(){
        etMobile = findViewById(R.id.etMobile);
        btnOTP = findViewById(R.id.btnOTP);
        countryCode=findViewById(R.id.CountryCode);
        countryCode.registerCarrierNumberEditText(etMobile);
        ProgressbarPN=findViewById(R.id.Login_ProgressBarPN);
        ProgressbarPN.setVisibility(View.GONE);
    }
}