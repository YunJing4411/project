package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class product_page extends AppCompatActivity {

    /**/
    private int position=0;
    TextView tvname,tvprice,tvdes;
    ImageView impic;
    Button btnIncrease,btnDecrease;
    private EditText etAmount;
    private int num=1;
    private String name,price,des,pic;
    private share.GoodsArrayAdapter adapter = null;
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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        tvname=findViewById(R.id.product_name);
        tvprice=findViewById(R.id.product_price);
        tvdes= findViewById(R.id.description);
        impic=findViewById(R.id.product_img);
        btnDecrease=findViewById(R.id.btnDecrease);
        btnIncrease=findViewById(R.id.btnIncrease);
        etAmount=findViewById(R.id.etAmount);
        btnIncrease.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(num<11)
                {
                    num++;
                    etAmount.setText(num+"");
                }
            }
        });
        btnDecrease.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(num>1)
                {
                    num--;
                    etAmount.setText(num+"");
                }

            }
        });
        Intent share=getIntent();
        position= share.getIntExtra("商品項目",0);
        getGoodsFromFirebase();

    }


    private void getGoodsFromFirebase()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Goods/"+String.valueOf(position));

            reference.addValueEventListener(new ValueEventListener()
            {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {

                            name = dataSnapshot.child("name").getValue().toString();
                            price=dataSnapshot.child("price").getValue().toString();
                            des=dataSnapshot.child("description").getValue().toString();
                            pic=dataSnapshot.child("pic").getValue().toString();
                            tvname.setText(name);
                            tvprice.setText(price);
                            tvdes.setText(des);
                            impic.setImageBitmap(stringToBitmap(pic));
                            Log.d("product page",name+" "+price+" "+des);
                        }

                        @Override
                        public void onCancelled(DatabaseError error)
                        {
                            // Failed to read value
                            Log.w("product page", error.toException());
                        }
            });
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
