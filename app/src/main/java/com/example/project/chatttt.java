package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.text.format.DateFormat;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class chatttt extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE=1;
    private FirebaseListAdapter<chatu> adapter;
    RelativeLayout activity_chatttt;
    FloatingActionButton fab;
    EditText Name;


    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==R.id.menu_sign_out)
        {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(activity_chatttt,"you out!!",Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == SIGN_IN_REQUEST_CODE)
        {
            if(resultCode==RESULT_OK)
            {
                Snackbar.make(activity_chatttt,"Good!!!!!",Snackbar.LENGTH_SHORT).show();
                displayChatMessage();
            }
            else
            {
                Snackbar.make(activity_chatttt,"Fuck off!!!",Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        final TextView messageText,messageUser,messageTime;

        messageUser=findViewById(R.id.message_user);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatttt);
        activity_chatttt=findViewById(R.id.activity_chattt);
        fab=(FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input=findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference().child("chatroom").push().setValue(new chatu(input.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                input.setText("");
            }
        });

        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(),SIGN_IN_REQUEST_CODE);
        }
        else
        {
            Snackbar.make(activity_chatttt,"Welcome  "+FirebaseAuth.getInstance().getCurrentUser().getEmail(),Snackbar.LENGTH_SHORT).show();
            displayChatMessage();
        }

    }
    private void displayChatMessage()
    {
        ListView listOfMessage=findViewById(R.id.list_of_message);

        Query query = FirebaseDatabase.getInstance().getReference().child("chatroom");

        //adapter=new FirebaseListAdapter<chatu>(this,chatu.class,R.layout.chat_list,FirebaseDatabase.getInstance().getReference())


        FirebaseListOptions<chatu> options = new FirebaseListOptions.Builder<chatu>().setQuery(query, chatu.class).setLayout(R.layout.chat_list).build();
        adapter = new FirebaseListAdapter<chatu>(options)
        {
            @Override
            protected void populateView(@NonNull View v, @NonNull chatu model, int position)
            {
                TextView messageText,messageUser,messageTime;
                messageText=v.findViewById(R.id.message_text);
                messageUser=v.findViewById(R.id.message_user);
                messageTime=v.findViewById(R.id.message_item);

                messageText.setText(model.getText());
                messageUser.setText(model.getUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm;ss)",model.getTime()));
            }
        };
        listOfMessage.setAdapter(adapter);
    }
}
