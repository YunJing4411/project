package com.example.project;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText password,username;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        password=findViewById(R.id.password);
        username=findViewById(R.id.username);
        login=findViewById(R.id.login);
        // user=new User();

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference tab_user=database.getReference("User");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final ProgressDialog mDialog=new ProgressDialog(Login.this);
                mDialog.setMessage("Please Wait");
                mDialog.show();
                tab_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.child(username.getText().toString()).exists())
                        {
                            mDialog.dismiss();
                            User user=dataSnapshot.child(username.getText().toString()).getValue(User.class);
                            if(user.getPassword().equals(password.getText().toString()))
                            {
                                Toast.makeText(Login.this,"Sign in successfully!!",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(Login.this,"Sign in failed!!",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            mDialog.dismiss();
                            Toast.makeText(Login.this,"User not exist!!",Toast.LENGTH_SHORT).show();
                        }
/*hi*/
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
