package com.example.billspliter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class profileMaking extends AppCompatActivity{

    private static final int PICK_IMAGE_REQUEST=1;
    private Uri mImageUri;
    private ImageView imageView;
    private EditText name;
    private StorageReference storageReference;
    private DatabaseReference phoneNoReference = FirebaseDatabase.getInstance().getReference().child("Details");
    String phoneNo;
    Details phone = new Details();
    private UploadTask uploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_making);

        name= findViewById(R.id.editText2);
        imageView= findViewById(R.id.imageView3);
        storageReference= FirebaseStorage.getInstance().getReference();
        phoneNo=getIntent().getStringExtra("Details");
    }

    public void chooseImage(View view)
    {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
          && data!=null && data.getData()!=null){

            mImageUri=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),mImageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void saveInfo(View v){
        String n=name.getText().toString();
        if(n.replace(" ","").length()==0)
        {
            Toast.makeText(profileMaking.this, "Enter your name", Toast.LENGTH_SHORT).show();
        }
        else
        {
            uploadFile();
            Intent i=getIntent();
            remove_from_db();
            Toast.makeText(profileMaking.this, "Save It", Toast.LENGTH_LONG).show();
            phone.setNumber(phoneNo);
            phone.setName(name.getText().toString().trim());
            phoneNoReference.push().setValue(phone);
            SharedPreferences prefers= getSharedPreferences("com.example.billspliter_login_status",
                    MODE_PRIVATE);
            SharedPreferences.Editor editor= prefers.edit();
            editor.putString("login_status", "on");
            editor.commit();
            //startActivity(new Intent(profileMaking.this, landingPage.class));
            Intent intent=new Intent(profileMaking.this, landingPage.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra("number",phoneNo);
            intent.putExtra("name",name.getText().toString());
            startActivity(intent);
        }
    }

    private void uploadFile(){
        if(mImageUri!=null) {

            //String n=name.getText().toString();
            //final StorageReference fileReference= storageReference.child("images/"+ UUID.randomUUID().toString());
            final StorageReference fileReference= storageReference.child("images/"+ phoneNo);
            //final StorageReference fR= storageReference.child("names/"+n);
            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(profileMaking.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    public void remove_from_db() {
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference();
        Query query = dr.child("Details").orderByChild("number").equalTo(phoneNo);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot phSnapshot : dataSnapshot.getChildren()) {
                    phSnapshot.getRef().removeValue();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(profileMaking.this, "Error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
