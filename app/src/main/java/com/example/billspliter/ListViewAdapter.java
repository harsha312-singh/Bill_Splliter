package com.example.billspliter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class ListViewAdapter extends BaseAdapter{

    Context mContext;
    LayoutInflater inflater;
    List<Model> modelList;
    ArrayList<Model> arrayList;
    GroupName gp= new GroupName();
    //String n="GroupName";
    String s=Contacts.st;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRootReference = firebaseDatabase.getReference();
    private DatabaseReference phoneNoReference = mRootReference.child("GroupName");
    //List<String> selected = new ArrayList<>();
    String selected;


    public ListViewAdapter(Context context, List<Model> modelList) {
        mContext = context;
        this.modelList = modelList;
        inflater=LayoutInflater.from(mContext);
        this.arrayList=new ArrayList<Model>();
        this.arrayList.addAll(modelList);
    }

    public  class ViewHolder{

        TextView mName, mPhone;

    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;


        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.row,null);

            holder.mName=convertView.findViewById(R.id.textView8);
            holder.mPhone=convertView.findViewById(R.id.textView9);

            convertView.setTag(holder);
        }
        else{
            holder=(ViewHolder)convertView.getTag();
        }
        if(modelList.get(position).isSelected)
           convertView.setBackgroundColor(Color.parseColor("#FF0000"));
           // convertView.setSelected(true);

        holder.mPhone.setText(modelList.get(position).getNumber());
        holder.mName.setText(modelList.get(position).getName());

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selected=modelList.get(position).getNumber();
                store_in_db();
                //modelList.get(position).isSelected=true;
                //v.setSelected(true);
                Toast.makeText(mContext, ""+selected, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return convertView;
    }

    public void filter(String charText){
        charText=charText.toLowerCase(Locale.getDefault());
        modelList.clear();
        if(charText.isEmpty())
        {
            modelList.addAll(arrayList);
        }
        else{
            for(Model model : arrayList) {
                if (model.getName().toLowerCase(Locale.getDefault()).startsWith(charText)) {
                    modelList.add(model);
                }
            }
        }
        notifyDataSetChanged();
    }
    public void store_in_db(){
        phoneNoReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int check= 1;
                for(DataSnapshot phSnapshot: dataSnapshot.getChildren())
                {
                    Log.i("correction",phSnapshot.child("number").getValue().toString());
                    if(selected.equals(phSnapshot.child("number").getValue().toString()) && s.equals(phSnapshot.child("gpname").getValue().toString()))
                    {
                        check=0;
                        break;
                    }
                }
                if(check == 1)
                {
                    gp.setGpname(s);
                    gp.setNumber(selected);
                    phoneNoReference.push().setValue(gp);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(ListViewAdapter.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
