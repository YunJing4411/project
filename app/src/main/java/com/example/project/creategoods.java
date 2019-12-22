package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class creategoods extends AppCompatActivity {
    Bitmap pic;
    ImageView IM;
    EditText edname,edprice,eddes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategoods);
        Intent createGoods=getIntent();
        String uri=createGoods.getStringExtra("上傳商品");
        Uri myUri = Uri.parse(uri);
        pic=getBitmapFromUri(myUri);
        IM=findViewById(R.id.imageView2);
        IM.setImageBitmap(pic);
        edname=findViewById(R.id.etname);
        eddes=findViewById(R.id.etdescription);
        edprice=findViewById(R.id.etprice);
    }
    public void publish(View v)
    {
        if(edname.getText().toString().isEmpty()| eddes.getText().toString().isEmpty() | edprice.getText().toString().isEmpty())
        {
            Toast.makeText(this,"欄位都要填寫喔~",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent Home=new Intent(this,share.class);
            startActivity(Home);
        }

    }

    private void updatetofirebase()
    {

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
