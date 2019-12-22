package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.service.autofill.Dataset;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Chatroom extends AppCompatActivity {
    private ChatArrayAdapter adapter = null;

    private static final int LIST_CHAT = 1;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LIST_CHAT: {
                    List<Chat> chats = (List<Chat>)msg.obj;
                    refreshHotelList(chats);
                    break;
                }
            }
        }
    };
    private void refreshHotelList(List<Chat> chats) {
        adapter.clear();
        adapter.addAll(chats);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        ListView chat = (ListView)findViewById(R.id.c);
        adapter = new ChatArrayAdapter(this, new ArrayList<Chat>());
        chat.setAdapter(adapter);

        getChatFromFirebase();
    }
    class FirebaseThread extends Thread
    {
        private DataSnapshot dataSnapshot;
        public FirebaseThread(DataSnapshot dataSnapshot) {
            this.dataSnapshot = dataSnapshot;
        }
        @Override
        public void run() {
            List<Chat> lsChat = new ArrayList<>();
            for(DataSnapshot ds : dataSnapshot.getChildren()){
                DataSnapshot dsaccount = ds.child("accout");
                DataSnapshot dscontent = ds.child("content");

                String account = (String)dsaccount.getValue();
                String content = (String)dscontent.getValue();

                Chat aChat = new Chat();
                aChat.setUser(account);
                aChat.setContent(content);


                lsChat.add(aChat);

                Log.v("chatroom", account + " " + content );


            }
            Message msg = new Message();
            msg.what = LIST_CHAT;
            msg.obj = lsChat;
            handler.sendMessage(msg);
        }
    }
    private void getChatFromFirebase()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("chatroom");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                new FirebaseThread(dataSnapshot).start();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("chatroom", databaseError.getMessage());
            }
        });
    }


    class ChatArrayAdapter extends ArrayAdapter<Chat>
    {
        Context context;

        public ChatArrayAdapter(Context context, List<Chat> items) {
            super(context, 0, items);
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = LayoutInflater.from(context);
            ConstraintLayout itemlayout = null;
            if(convertView == null){
                itemlayout = (ConstraintLayout) inflater.inflate(R.layout.chatroom_item, null);
            }else{
                itemlayout = (ConstraintLayout)convertView;
            }
            Chat item = (Chat)getItem(position);
            TextView tvTxet = (TextView)itemlayout.findViewById(R.id.tvtext);
            tvTxet.setText(item.getContent());
            TextView tvUser = (TextView)itemlayout.findViewById(R.id.tvuser);
            tvUser.setText(item.getUser());
            return itemlayout;
        }
    }
}
