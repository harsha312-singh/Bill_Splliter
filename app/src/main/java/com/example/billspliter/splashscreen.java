package com.example.billspliter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.common.util.SharedPreferencesUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.utilities.Utilities;

public class splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences= getSharedPreferences("com.example.billspliter_login_status",
                        MODE_PRIVATE);
                String status = preferences.getString("login_status", "off");

                if(status.equals("on"))
                {
                    startActivity(new Intent(splashscreen.this, landingPage.class));
                }
                else
                {
                    startActivity(new Intent(splashscreen.this, MainActivity.class));
                }
                finish();
            }
        }, 2000);
    }
}
