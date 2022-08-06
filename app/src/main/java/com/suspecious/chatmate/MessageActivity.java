package com.suspecious.chatmate;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.suspecious.chatmate.Adapter.MessageAdapter;
import com.suspecious.chatmate.Api.APIService;
import com.suspecious.chatmate.Api.ApiPath;
import com.suspecious.chatmate.Api.ApiServicePath;
import com.suspecious.chatmate.Model.Chat;
import com.suspecious.chatmate.Model.MonitoringModel;
import com.suspecious.chatmate.Model.SuspiciousWord;
import com.suspecious.chatmate.Model.User;
import com.suspecious.chatmate.Utility.Client;
import com.suspecious.chatmate.Utility.Data;
import com.suspecious.chatmate.Utility.MyResponse;
import com.suspecious.chatmate.Utility.Sender;
import com.suspecious.chatmate.Utility.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    ApiServicePath apiInterface;
    private static final String TAG = "DATA";
    CircleImageView profile_image;
    TextView username;
    boolean frodmessage;
    Typeface MR, MRR;

    FirebaseUser fuser;
    DatabaseReference reference;

    ImageButton btn_send;
    EditText text_send;
//    String[] myStringArray = {"Abysmal", "Adverse", "Alarming", "Angry", "Annoy", "Anxious", "Apathy", "Appalling", "Atrocious", "Awful", "Bad", "Banal", "Barbed", "Belligerent", "Contrary", "Corrosive", "Corrupt", "Crazy", "Creepy", "Criminal", "Cruel",
//            "Dreadful", "Dreary", "Fear", "Feeble", "Fight", "Filthy", "Foul", "Ghastly", "Grave", "Greed", "Grim", "Grimace", "Harmful", "Hate", "Hideous", "Immature", "Lumpy", "Malicious", "Naive ", "Nasty", "Naughty", "Negate", "Negative", "Odious", "Offensive",
//            "Perturb", "Pessimistic", "Petty", "Poisonous", "Repellant", "Reptilian", "Repugnant", "Repulsive", "Revenge", "Scare", "Scary", "Suspicious", "Threatening", "Ugly", "Undermine", "Unfair", "Unfavorable", "Vindictive"};

    String[] myStringArray;

    MessageAdapter messageAdapter;
    List<Chat> mchat;

    RecyclerView recyclerView;

    Intent intent;

    ValueEventListener seenListener;

    String userid;

    APIService apiService;

    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        MRR = Typeface.createFromAsset(getAssets(), "fonts/myriadregular.ttf");
        MR = Typeface.createFromAsset(getAssets(), "fonts/myriad.ttf");

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // and this
                startActivity(new Intent(MessageActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });*/
        apiInterface = ApiPath.getClient().create(ApiServicePath.class);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);

        username.setTypeface(MR);
        text_send.setTypeface(MRR);


        intent = getIntent();
        userid = intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String msg = text_send.getText().toString();
                String time = String.valueOf(System.currentTimeMillis());
                if (!msg.equals("")) {

                    /**
                     * Note : Added code for fetching dynamic array of words
                     */
                    getWordsArray(msg, time);

//                    sendMessage(fuser.getUid(), userid, msg, time);
//
//                    for (int i = 0; i < myStringArray.length; i++) {
//                        Log.d(TAG, "messageMeeeeeeee" + myStringArray[i]);
//                        frodmessage = (msg.indexOf(myStringArray[i]) > 0);
//                        //Log.d(TAG, "messageMeeeeeeee"+  (msg.indexOf(myStringArray[i]) > 0));
//                        if (frodmessage == true) {
//
//                            Log.d(TAG, "messageMeee" + "right" + msg);
//
//                            Call<MonitoringModel> responseCall = apiInterface.doGetMonitoring(userid, "harshal@gmail.com", msg, "1.576666", "2.45646");
//                            responseCall.enqueue(new Callback<MonitoringModel>() {
//                                @Override
//                                public void onResponse(Call<MonitoringModel> call, Response<MonitoringModel> response) {
//                                    //  Toast.makeText(MessageActivity.this, "data", Toast.LENGTH_SHORT).show();
//                                    // Log.d(TAG, "onResponsesfsd"+response.body().getStatus());
//                                    if (response.code() == 200) {
//                                        if (response.body() != null) {
//                                            Log.d(TAG, "onResponsesfsd" + response.body().getStatus());
//                                            if (response.body().getStatus()) {
//                                                Log.d(TAG, "onResponsesfsd" + response.body().getMessage());
//                                            } else {
//                                                Toast.makeText(MessageActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                                            }
//
//
//                                        }
//
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<MonitoringModel> call, Throwable t) {
//                                    Toast.makeText(MessageActivity.this, "" + t, Toast.LENGTH_SHORT).show();
//
//                                }
//                            });
//
//
//                            AlertDialog alertDialog = new AlertDialog.Builder(MessageActivity.this).create();
//                            alertDialog.setTitle("warning");
//                            alertDialog.setMessage("Your Message Contains Bad Words");
//                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int which) {
//
//                                            dialog.dismiss();
//                                        }
//                                    });
//                            alertDialog.show();
//                            break;
//                        }
//
//                        //         Log.d(TAG, "messageMeeeeeeee"+  (msg.indexOf(myStringArray[i]) > 0));
////                        if (myStringArray[i].equals (msg.indexOf(myStringArray[i]) > 0))
////                        {
////                            Log.d(TAG, "messageMeee"+"right" +msg);
////                        }
//
//                    }
//                    Log.d(TAG, "messageMeee" + msg);

                } else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });


        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    profile_image.setImageResource(R.drawable.profile_img);
                } else {
                    //and this
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                }

                readMesagges(fuser.getUid(), userid, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        seenMessage(userid);
    }

    private void getWordsArray(String msg, String time) {
        reference = FirebaseDatabase.getInstance().getReference("Suspicious Word");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String[] tempArr = new String[(int) snapshot.getChildrenCount()];
                int i = 0;
                for (DataSnapshot singleData : snapshot.getChildren()
                ) {
                    SuspiciousWord suspiciousWord = singleData.getValue(SuspiciousWord.class);
                    tempArr[i] = suspiciousWord.getWord();
                    i++;
                }
                myStringArray = new String[tempArr.length];
                myStringArray = tempArr;
                Log.e(TAG, "getWordsArray() -> onDataChange: WordArr -> " + myStringArray[i-1] + ", Count of i -> " + i);
                findSuspiciousWordsAndSendMail(msg, time);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: Error Msg -> " + error.getMessage() + " and Code -> " + error.getCode());
            }
        });
    }

    private void findSuspiciousWordsAndSendMail(String msg, String time) {

        sendMessage(fuser.getUid(), userid, msg, time);

        for (int i = 0; i < myStringArray.length; i++) {
            Log.d(TAG, "messageMeeeeeeee: " + myStringArray[i]);
            frodmessage = (msg.toLowerCase().indexOf(myStringArray[i].toLowerCase()) >= 0);
            //Log.d(TAG, "messageMeeeeeeee"+  (msg.indexOf(myStringArray[i]) > 0));
            if (frodmessage == true) {
                Log.d(TAG, "messageMeee" + "right" + msg);

                Call<MonitoringModel> responseCall = apiInterface.doGetMonitoring(userid, "harshal@gmail.com", msg, "1.576666", "2.45646");
                responseCall.enqueue(new Callback<MonitoringModel>() {
                    @Override
                    public void onResponse(Call<MonitoringModel> call, Response<MonitoringModel> response) {
                        //  Toast.makeText(MessageActivity.this, "data", Toast.LENGTH_SHORT).show();
                        // Log.d(TAG, "onResponsesfsd"+response.body().getStatus());
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                Log.d(TAG, "onResponsesfsd" + response.body().getStatus());
                                if (response.body().getStatus()) {
                                    Log.d(TAG, "onResponsesfsd" + response.body().getMessage());
                                } else {
                                    Toast.makeText(MessageActivity.this, "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }


                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<MonitoringModel> call, Throwable t) {
                        Toast.makeText(MessageActivity.this, "" + t, Toast.LENGTH_SHORT).show();

                    }
                });


                AlertDialog alertDialog = new AlertDialog.Builder(MessageActivity.this).create();
                alertDialog.setTitle("WARNING!!!");
                alertDialog.setIcon(R.drawable.app_icon);
                alertDialog.setMessage("Your Message Contains Inappropriate Words");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
                break;
            }

            //         Log.d(TAG, "messageMeeeeeeee"+  (msg.indexOf(myStringArray[i]) > 0));
//                        if (myStringArray[i].equals (msg.indexOf(myStringArray[i]) > 0))
//                        {
//                            Log.d(TAG, "messageMeee"+"right" +msg);
//                        }

        }
        Log.d(TAG, "messageMeee" + msg);
    }

    private void seenMessage(final String userid) {
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(userid)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, final String receiver, String message, String time) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);
        hashMap.put("time", time);

        reference.child("Chats").push().setValue(hashMap);


        // add user to chat fragment
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(fuser.getUid())
                .child(userid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(userid)
                .child(fuser.getUid());
        chatRefReceiver.child("id").setValue(fuser.getUid());

        final String msg = message;

        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (notify) {
                    sendNotifiaction(receiver, user.getUsername(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotifiaction(String receiver, final String username, final String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(fuser.getUid(), R.drawable.profile_img, username + ": " + message, "New Message",
                            userid);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200) {
                                        if (response.body().success != 1) {
                                            //Toast.makeText(MessageActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMesagges(final String myid, final String userid, final String imageurl) {
        mchat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                        mchat.add(chat);
                        Log.d(TAG, "chat..." + chat);

                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this, mchat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void currentUser(String userid) {
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();
    }

    private void status(String status) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
        currentUser(userid);
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");
        currentUser("none");
    }
}
