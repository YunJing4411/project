package com.example.project;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;

public class Signup extends AppCompatActivity {

    EditText Name, Mail, PassWord;
    Button SignUp;

    private int i, j, count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Name = findViewById(R.id.Name);
        Mail =  findViewById(R.id.Email);
        PassWord =  findViewById(R.id.Password);
        SignUp =  findViewById(R.id.SignUp);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("user");

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(Signup.this);
                mDialog.setMessage("Please waiting....");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {

                        if (dataSnapshot.child(Name.getText().toString()).exists())
                        {
                            mDialog.dismiss();
                            Toast.makeText(Signup.this, "Name already exist !", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {

                            mDialog.dismiss();
                            User user = new User(Mail.getText().toString(),Name.getText().toString(),PassWord.getText().toString());
                            table_user.child(Name.getText().toString()).setValue(user);
                            Toast.makeText(Signup.this, "Sign up successful !", Toast.LENGTH_SHORT).show();
                            finish();


                        }


                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}