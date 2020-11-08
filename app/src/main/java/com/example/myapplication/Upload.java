package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Upload extends AppCompatActivity {
    Button Camera;
    Button Gallery;
    StorageReference ref;
    DatabaseReference imageref;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    TextView Artistname;
    int num =0;
    TextView Price;
    DatabaseReference myRef;
    public void askCamerapermit(){
        if(checkSelfPermission(Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA},1);
        }else{
            camera();
        }
    }
    public void askStoragePermit(){
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
        }else{
            galler();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_DENIED) {
                camera();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
            }
        }else if(requestCode==2){
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_DENIED) {
                galler();
            } else {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            }
        }
    }

    public void camera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,1);
    }
    public void galler(){
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,2);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Price = findViewById(R.id.price);
        Artistname = findViewById(R.id.artistname);
        mAuth =FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Camera =findViewById(R.id.uploadthroughcamera);
        Gallery = findViewById(R.id.uploadthroughgallery);
        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCamerapermit();
            }
        });
        Gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askStoragePermit();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode==1) && resultCode ==RESULT_OK &&data!=null){
            String uuid = UUID.randomUUID().toString();
            ref = FirebaseStorage.getInstance().getReference("uuid");
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            if(bitmap!= null){
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] dataa = baos.toByteArray();
                Uri selectedImageUri = data.getData();
                UploadTask uploadTask = ref.putBytes(dataa);
                ref.putFile(selectedImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return ref.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            Log.i("!!!!!!!!!!!!!!!!!!weed!",downloadUri.toString());
//                            FriendlyMessage friendlyMessage = new FriendlyMessage(null, mUsername, downloadUri.toString());
//                            mMessagesDatabaseReference.push().setValue(friendlyMessage);
                        } else {
                            Toast.makeText(getApplicationContext(), "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                Toast.makeText(this, "Upload Unsuccessful", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode==2&& resultCode ==RESULT_OK &&data!=null){
            Uri uri = data.getData();
            String uniqueId = UUID.randomUUID().toString();
            Member member = new Member();
            member.setEmail(mAuth.getCurrentUser().getEmail());
            num++;
            ref = FirebaseStorage.getInstance().getReference("users/"+uniqueId);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                if(bitmap!= null){
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] dataa = baos.toByteArray();
                    ref.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return ref.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                num++;
                                Uri downloadUri = task.getResult();
                                member.setImage(downloadUri.toString());
                                member.setName(Artistname.getText().toString());
                                member.setPrice(Integer.parseInt(Price.getText().toString()));
                                imageref = database.getReference("");
                                myRef = database.getReference("users/"+uniqueId);
                                myRef.setValue(member);
//                            FriendlyMessage friendlyMessage = new FriendlyMessage(null, mUsername, downloadUri.toString());
//                            mMessagesDatabaseReference.push().setValue(friendlyMessage);
                            } else {
                                Toast.makeText(getApplicationContext(), "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(this, "Upload Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}