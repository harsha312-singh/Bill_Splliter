package com.example.billspliter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.billspliter.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class landingPage extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ImageView imageView;
    private TextView tvname;
    private TextView tvphone;
    String Phonenumber;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        Phonenumber=getIntent().getStringExtra("number");
        name=getIntent().getStringExtra("name");
        //Toast.makeText(this, ""+Phonenumber, Toast.LENGTH_SHORT).show();

        View hview= navigationView.getHeaderView(0);
        tvname= hview.findViewById(R.id.textName);
        tvphone= hview.findViewById(R.id.textView);
        //Toast.makeText(landingPage.this,number+","+name,Toast.LENGTH_LONG).show();
        //if(tvname.getText().toString().isEmpty() && tvphone.getText().toString().isEmpty()) {
        if(Phonenumber!=null && name!=null){
            tvname.setText(name);
            tvphone.setText(Phonenumber);
            savedata();
        }

        load();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_archieve, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.landing_page, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void savedata(){
        SharedPreferences sharedPreferences = getSharedPreferences("savedata",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PHONE",tvphone.getText().toString());
        editor.putString("NAME",tvname.getText().toString());
        editor.apply();
    }

    public void load(){
        SharedPreferences sharedPreferences=getSharedPreferences("savedata",MODE_PRIVATE);
        tvphone.setText(sharedPreferences.getString("PHONE",""));
        tvname.setText(sharedPreferences.getString("NAME",""));
        HomeFragment.phno=tvphone.getText().toString();
    }
}
