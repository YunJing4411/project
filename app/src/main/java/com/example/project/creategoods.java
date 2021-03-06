package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class creategoods extends AppCompatActivity {
    Bitmap pic;
    ImageView IM;
    ArrayList<String> picture;
    EditText edname,edprice,eddes;
    String name,price,des,uri;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategoods);

        picture=new ArrayList<>();
        Intent createGoods=getIntent();
        uri=createGoods.getStringExtra("上傳商品");
        Log.v("上傳",uri);
        Uri myUri = Uri.parse(uri);
        pic=getBitmapFromUri(myUri);
        IM=findViewById(R.id.imageView2);
        IM.setImageBitmap(pic);
        edname=findViewById(R.id.etname);
        eddes=findViewById(R.id.etdescription);
        edprice=findViewById(R.id.etprice);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Goods");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    i++;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("creategoods", databaseError.getMessage());
            }
        });
    }

    public void publish(View v)
    {

        if(edname.getText().toString().isEmpty()| eddes.getText().toString().isEmpty() | edprice.getText().toString().isEmpty())
        {
            Toast.makeText(this,"欄位都要填寫喔~",Toast.LENGTH_SHORT).show();
        }
        else
        {
            updatetofirebase();
            Intent Home=new Intent(this,share.class);
            Home.putStringArrayListExtra("圖片",picture);
            for(int i=0;i<picture.size();i++)
            {
                Log.v("123",picture.get(i));
            }
            startActivity(Home);
        }

    }

    private void updatetofirebase()
    {
        name=edname.getText().toString();
        price=edprice.getText().toString();
        des=eddes.getText().toString();
        Log.v("DB:",name+" "+price+" "+des);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("");

        goodsinfo good1 = new goodsinfo(name,price,des,BitMapToString(pic));
        ref.child("Goods").child(String.valueOf(i)).setValue(good1);
        Log.v("i:",String.valueOf(i));
    }
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream ByteStream=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, ByteStream);
        byte [] b=ByteStream.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }
}
