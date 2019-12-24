package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class share extends AppCompatActivity {
    ListView lvgood;
    ArrayList<String> picture;
    ArrayList<Bitmap> picture1;
    ImageView im;
    private String name,price,des,pic;
    private GoodsArrayAdapter adapter = null;
    private static final int LIST_Goods = 1;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LIST_Goods: {
                    List<GGoods> goods = (List<GGoods>)msg.obj;
                    refreshGoodList(goods);
                    break;
                }
            }
        }
    };
    private void refreshGoodList(List<GGoods> goods) {
        adapter.clear();
        adapter.addAll(goods);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        picture=new ArrayList<>();
        picture1=new ArrayList<>();
        lvgood=findViewById(R.id.lvgoods);
        adapter = new GoodsArrayAdapter(this, new ArrayList<GGoods>());
        lvgood.setAdapter(adapter);
        getGoodsFromFirebase();
        im=findViewById(R.id.imageView6);

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
    public static final int KITKAT_VALUE = 1002;
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode==0)
        {

            Uri uri=null;
            uri=data.getData();
            Intent createGoods=new Intent(this,creategoods.class);
            createGoods.putExtra("上傳商品",uri.toString());
            startActivity(createGoods);

        }

    }



    class FirebaseThread extends Thread
    {
        private DataSnapshot dataSnapshot;
        public FirebaseThread(DataSnapshot dataSnapshot) {
            this.dataSnapshot = dataSnapshot;
        }
        @Override
        public void run() {
            List<GGoods> lsgoods = new ArrayList<>();
            for(DataSnapshot ds : dataSnapshot.getChildren()){
                DataSnapshot dsname = ds.child("name");
                DataSnapshot dsdes = ds.child("description");
                DataSnapshot dsprice = ds.child("price");
                DataSnapshot dspic = ds.child("pic");
                name=(String)dsname.getValue();
                des=(String)dsdes.getValue();
                price=(String)dsprice.getValue();
                pic=(String)dspic.getValue();

                GGoods agoods = new GGoods();
                agoods.setName(name);
                agoods.setPic(stringToBitmap(pic));

                lsgoods.add(agoods);
                Log.v("share", name + " " + price+" "+des+" "+pic);
            }

            Message msg = new Message();
            msg.what = LIST_Goods;
            msg.obj = lsgoods;
            handler.sendMessage(msg);
        }

    }

    private void getGoodsFromFirebase()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Goods");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                new FirebaseThread(dataSnapshot).start();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("share", databaseError.getMessage());
            }
        });
    }

    class GoodsArrayAdapter extends ArrayAdapter<GGoods>
    {
        Context context;

        public GoodsArrayAdapter(Context context, List<GGoods> items) {
            super(context, 0, items);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            ConstraintLayout itemlayout = null;
            if(convertView == null){
                itemlayout = (ConstraintLayout) inflater.inflate(R.layout.goods, null);
            }else{
                itemlayout = (ConstraintLayout)convertView;
            }
            GGoods item = (GGoods)getItem(position);
            ImageView PIC=(ImageView)findViewById(R.id.gpic) ;
            im.setImageBitmap(item.getPic());

            PIC.setImageResource(R.drawable.add);
            TextView tvTxet = (TextView)itemlayout.findViewById(R.id.gname);
            tvTxet.setText(item.getName());
            return itemlayout;
        }



    }

    public Bitmap stringToBitmap(String string)
    {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
