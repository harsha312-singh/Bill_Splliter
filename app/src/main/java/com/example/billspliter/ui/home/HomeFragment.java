package com.example.billspliter.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ContentResolver;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.billspliter.Contacts;
import com.example.billspliter.R;
import com.example.billspliter.landingPage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    EditText editText;
    int j=0;
    ListView listView;
    String [] groups;
    static public String phno;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootReference = firebaseDatabase.getReference();
    private DatabaseReference phoneNoReference = mRootReference.child("GroupName");
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        editText=root.findViewById(R.id.editText3);
        listView=root.findViewById(R.id.lv);
        root.findViewById(R.id.add_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gpname=editText.getText().toString();
                Intent i =new Intent(getActivity(), Contacts.class);
                i.putExtra("gpname",gpname);
                 startActivity(i);
            }
        });

        groups=new String[]{
                "goa"
        };

       Toast.makeText(getActivity(),"phone no "+phno,Toast.LENGTH_SHORT).show();
       /*phoneNoReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot phSnapshot: dataSnapshot.getChildren())
                {
                    //Log.d("correction",phSnapshot.child("number").getValue().toString());
                    if(phno.equals(phSnapshot.child("number").getValue().toString()))
                    {
                        String n=phSnapshot.child("gpname").getValue().toString();
                        groups[j]=n;
                        j++;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(HomeFragment.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
*/
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_2,
                android.R.id.text1,groups);
        listView.setAdapter(adapter);

        return root;
    }
}

