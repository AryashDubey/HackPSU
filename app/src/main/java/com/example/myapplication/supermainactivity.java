package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class supermainactivity extends AppCompatActivity {
    RecyclerView MainList;
    private StorageReference mStorageRef;
    FirebaseDatabase database;
    StorageReference riversRef;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supermainactivity);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("image");
        MainList= findViewById(R.id.recyclerview);
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        MainList.setLayoutManager(new LinearLayoutManager(this));
        List<String> arrayList;
        arrayList =  Arrays.asList(getResources().getStringArray(R.array.Url));
        List<String> arrayList3 =  Arrays.asList(getResources().getStringArray(R.array.Price));
        ArrayList<String> a = new ArrayList<>();
        ArrayList<String> b = new ArrayList<>();
        for (int j = 1; j <= 5; j++) {
            a.add(arrayList3.get(j));
            b.add(arrayList.get(j));
        }
        MainList.setAdapter(new MainAdapter(a,b));
        getData();
    }
    private void getData (){

        mDatabase.child("image").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Looping inside Appointments to get each appointmentsStuts
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    ImagePojo ap = snapshot.getValue(ImagePojo.class);
                    //Getting each appointmentStuts
                    String image = ap.getimage();
                    //To get each father of those appointments
                    String key = snapshot.getKey();

                    Log.i("Data:!!!!!!!!!!!!" , "" + image );

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}