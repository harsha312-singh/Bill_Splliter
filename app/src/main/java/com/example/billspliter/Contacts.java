package com.example.billspliter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.MimeTypeFilter;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billspliter.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Contacts extends AppCompatActivity{

    ListView listView;
    TextView textView2;
    Button btn;
    String text;
    SearchView searchView;
    ListViewAdapter adapter;
    String[] namecollection;
    String[] phonecollection;
    GroupName gp= new GroupName();
    static String st;
    ArrayList<Model> arrayList= new ArrayList<Model>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        listView = findViewById(R.id.listView);
        textView2 = findViewById(R.id.textView7);
        searchView=findViewById(R.id.search_bar);

        st = getIntent().getStringExtra("gpname");
        textView2.setText(st);

        namecollection= new String[1000];
        phonecollection=new String[1000];

        ContentResolver resolver=getContentResolver();
        Cursor cursor= resolver.query(ContactsContract.Contacts.CONTENT_URI,null,null,null, null);
        int j=0;
        while(cursor.moveToNext()){
            String id= cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name= cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? ", new String[]{ id} ,null);
            //Log.i("MY INFO", id + " = " +name);
            namecollection[j]=name;
            while(phoneCursor.moveToNext()){
                String phoneNumber= phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                //Log.i("MY INFO",phoneNumber);
                phonecollection[j]=phoneNumber;
                break;
            }
            j++;
        }

        for(int i=0; i<j; i++)
        {
            Model model= new Model(namecollection[i],phonecollection[i]);
            arrayList.add(model);
        }

        adapter=new ListViewAdapter(this,arrayList);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //adapter.getFilter().filter(newText);
                if(TextUtils.isEmpty(newText)){
                    adapter.filter("");
                    listView.clearTextFilter();
                }
                else{
                    adapter.filter(newText);
                }
                return true;
            }
        });
    }
}
