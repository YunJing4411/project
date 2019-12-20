package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    private ChatArrayAdapter adapter=null;
    private static final int LIST_CHAT=1;
    private Handler handler=new Handler()
    {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LIST_CHAT: {
                    List<Chat> chat = (List<Chat>)msg.obj;
                    refreshChatList(chat);
                    break;
                }
            }
        }
    };
    private void refreshChatList(List<Chat> chat) {
        if(adapter!=null)
        {
            adapter.clear();
            adapter.addAll(chat);
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        ListView lv=(ListView)findViewById(R.id.c);
        ChatArrayAdapter A=new ChatArrayAdapter(this,new ArrayList<Chat>());
        lv.setAdapter(A);

        getChatFromFirebase();
    }
    class FirebaseThread extends Thread
    {
        private DataSnapshot dataSnapshot;
        public  FirebaseThread(DataSnapshot dataSnapshot) { this.dataSnapshot=dataSnapshot; }
        @Override
        public void run()
        {
            List<Chat> lschat=new ArrayList<>();
            for(DataSnapshot ds : dataSnapshot.getChildren())
            {
                DataSnapshot account = ds.child("accout");
                DataSnapshot content = ds.child("content");
                String Account = (String)account.getValue();
                String Content = (String)content.getValue();

                Chat achat=new Chat();
                achat.setContent(Content);
                achat.setUser(Account);
                lschat.add(achat);
                Log.v("chatroom", Account + ";" + Content);

                Message msg=new Message();
                msg.what=LIST_CHAT;
                msg.obj=lschat;
                handler.sendMessage(msg);
            }
        }
    }
    private void getChatFromFirebase()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chatroom");
        myRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void
            onDataChange(DataSnapshot dataSnapshot)
            {
                new FirebaseThread(dataSnapshot).start();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Log.v("chatroom",databaseError.getMessage());
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
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            LinearLayout itemlayout = null;
            if (convertView == null) {
                itemlayout = (LinearLayout) inflater.inflate(R.layout.chatroom_item, null);
            } else {
                itemlayout = (LinearLayout) convertView;
            }
            Chat item = (Chat) getItem(position);
            TextView tvUser = (TextView) itemlayout.findViewById(R.id.tvuser);
            tvUser.setText(item.getUser());
            TextView tvContent = (TextView) itemlayout.findViewById(R.id.tvtext);
            tvContent.setText(item.getContent());
            return itemlayout;
        }
    }

}
