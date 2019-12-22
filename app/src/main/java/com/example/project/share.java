package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class share extends AppCompatActivity {
    Bitmap pic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);


    }
    public void chat(View v)
    {
        Intent chatroom=new Intent(share.this,Chatroom.class);
        startActivity(chatroom);
    }
    public void newitem(View v)
    {
        //讀取圖片
        Intent photo = new Intent();
        //開啟Pictures畫面Type設定為image
        photo.setType("image/*");
        //使用Intent.ACTION_GET_CONTENT這個Action
        photo.setAction(Intent.ACTION_GET_CONTENT);
        //取得照片後返回此畫面
        startActivityForResult(photo, 0);


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode==0)
        {
            Uri uri=data.getData();
            Intent createGoods=new Intent(this,creategoods.class);
            createGoods.putExtra("上傳商品",uri.toString());
            startActivity(createGoods);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
