package com.example.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.project.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import info.hoang8f.widget.FButton;

/*
不會debug還移植失敗，我辦不到，謝謝大家
然後我gradle有+東西
*/


public class Cart_main {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    //info.hoang8f.widget.fbutton
    FButton btn_Place;
    TextView txt_TotalPrice;

    List<Order> cart=new ArrayList<>();
    Cart_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        //firebase
        database = FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");

        //Init
        recyclerView = (RecyclerView) findViewById (R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        btn_Place=(FButton)findViewById(R.id.btnPlaceOrder);
        txt_TotalPrice = (TextView)findViewById(R.id.total);


        btn_Place.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                showAlertDialog();
            }
        });

        loadListPruduct();
    }

    private void showAlertDialog()
    {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Cart_main.this);
        alertDialog.setTitle("One more step!");
        alertDialog.setMessage("Enter You Address: ");

        final EditText ProductAddress = new EditText(Cart_main.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        ProductAddress.setLayoutParams(lp);
        alertDialog.setView(ProductAddress);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int i)
            {
                Request request=new Request(
                        Common.currentUser.getMail(),
                        Common.currentUser.getName(),
                        ProductAddress.getText().toString(),
                        txt_TotalPrice.getText().toString(),
                        cart
                );
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                //delete cart
                new Cartdb(getBaseContext()).cleanCart();
                Toast.makeText(Cart_main.this,"Thank you,Order Place",Toast.LENGTH_SHORT).show();
                finish();
            }

        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i)
            {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void loadListPruduct()
    {
        cart=new Cartdb(this).getCarts();
        adapter=new Cart_adapter(cart,this);
        recyclerView.setAdapter(adapter);
        int total=0;

        for(Order order:cart)
        {
            total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
        }
        Locale locale=new Locale("en","US");
        NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);

        txt_TotalPrice.setText(fmt.format(total));

    }


}
