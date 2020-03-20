package com.example.billspliter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText= findViewById(R.id.editText);
    }

    public void OTPPage(View v)
    {
        Intent i= new Intent(MainActivity.this, OTPActivity.class);
        String phoneNo="+91"+editText.getText().toString();
        i.putExtra("Details", phoneNo);
        startActivity(i);
    }
}
