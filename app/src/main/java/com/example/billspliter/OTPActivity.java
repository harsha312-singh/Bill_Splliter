package com.example.billspliter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.database.ValueEventListener;


import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity{


    private String verificationId;
    private FirebaseAuth mAuth;
    private PinView pinView;
    String phoneNo;
    Details phone = new Details();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootReference = firebaseDatabase.getReference();
    private DatabaseReference phoneNoReference = mRootReference.child("Details");
   // Details phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        pinView = findViewById(R.id.pinid);

        mAuth = FirebaseAuth.getInstance();
        phoneNo = getIntent().getStringExtra("Details");
        //sendVerificationCode(phoneNo);

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = pinView.getText().toString();
                //String code="123456";
                if(code.isEmpty() || code.length()<6){
                    Toast.makeText(OTPActivity.this,"Enter OTP...", Toast.LENGTH_LONG).show();
                    pinView.requestFocus();
                    return;
                }
                //Toast.makeText(OTPActivity.this,code,Toast.LENGTH_SHORT).show();
                verifyCode(code);
                store_in_db();
            }
        });
    }

    private void verifyCode(String code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        //Toast.makeText(this,code,Toast.LENGTH_LONG).show();
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    store_in_db();
                }
                else
                {
                    Toast.makeText(OTPActivity.this, task.getException().getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendVerificationCode(String number){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if(code!=null)
            {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    public void store_in_db(){
        phoneNoReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int check= 1;
                for(DataSnapshot phSnapshot: dataSnapshot.getChildren())
                {

                    Log.d("correction",phSnapshot.child("number").getValue().toString());
                    if(phoneNo.equals(phSnapshot.child("number").getValue().toString()))
                    {
                            if(!TextUtils.isEmpty(phSnapshot.child("name").getValue().toString())) {

                                Intent intent = new Intent(OTPActivity.this, landingPage.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                        }
                        else{
                            Intent intent = new Intent(OTPActivity.this, profileMaking.class);
                            intent.putExtra("Details",phoneNo);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }

                        check=0;
                        break;
                    }
                }
               if(check==1)
                {
                    phone.setNumber(phoneNo);
                    phoneNoReference.push().setValue(phone);
                    Intent intent = new Intent(OTPActivity.this, profileMaking.class);
                    intent.putExtra("Details",phoneNo);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(OTPActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
