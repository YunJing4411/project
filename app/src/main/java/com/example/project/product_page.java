package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class product_page extends AppCompatActivity {

    /**/
    Button btnIncrease,btnDecrease;
    private EditText etAmount;
    private int num=1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);
        btnDecrease=findViewById(R.id.btnDecrease);
        btnIncrease=findViewById(R.id.btnIncrease);
        etAmount=findViewById(R.id.etAmount);

        btnIncrease.setOnClickListener(new View.OnClickListener() {
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

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(num>1)
                {
                    num--;
                    etAmount.setText(num+"");
                }

            }
        });

    }
}
