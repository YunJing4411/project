package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    EditText password,username;
    Button login,signup;
    public int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        password=findViewById(R.id.password);
        username=findViewById(R.id.Pusername);
        login=findViewById(R.id.login);
        signup=findViewById(R.id.signup);
        // user=new User();

        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference tab_user=database.getReference("user");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                //  database.getReference().removeValue();
                final ProgressDialog mDialog=new ProgressDialog(MainActivity.this);
                mDialog.setMessage("Please Wait");
                mDialog.show();

                tab_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        i=0;
                        if(dataSnapshot.child(""+i).exists())
                        {
                            mDialog.dismiss();

                            for(i=0;i<(int)dataSnapshot.getChildrenCount();i++)
                            {
                                User user=dataSnapshot.child(""+i).getValue(User.class);
                                if(user.getName().equals(username.getText().toString()))
                                {
                                    if(user.getPassword().equals(password.getText().toString()))
                                    {

                                        Toast.makeText(MainActivity.this,"Sign in successfully!!",Toast.LENGTH_SHORT).show();
                                        Intent  share=new Intent(MainActivity.this,share.class);
                                        startActivity(share);
                                    }
                                    else
                                    {
                                        Toast.makeText(MainActivity.this,"Sign in failed!!",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    if(i>=(int)dataSnapshot.getChildrenCount())
                                    {
                                        mDialog.dismiss();
                                        Toast.makeText(MainActivity.this,"User not exist!!",Toast.LENGTH_SHORT).show();
                                    }

                                }

                            }

                        }
                        else
                        {
                            mDialog.dismiss();
                            Toast.makeText(MainActivity.this,"User not exist!!",Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp=new Intent(MainActivity.this,Signup.class);
                startActivity(signUp);

            }
        });
    }
}
