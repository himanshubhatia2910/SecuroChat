package com.suspecious.chatmate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.suspecious.chatmate.Utility.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPaasswordActivity extends AppCompatActivity {
    EditText send_email;
    Button btn_reset;
    Typeface MR, MRR;
    TextView hint_tv;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_paassword);


       /// MRR = Typeface.createFromAsset(getAssets(), "fonts/myriadregular.ttf");
       // MR = Typeface.createFromAsset(getAssets(), "fonts/myriad.ttf");

        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
       // getSupportActionBar().setTitle("Reset Password");
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        send_email = findViewById(R.id.send_email);
        btn_reset = findViewById(R.id.btn_reset);
        hint_tv = findViewById(R.id.hint_tv);

        send_email.setTypeface(MRR);
        btn_reset.setTypeface(MR);
        hint_tv.setTypeface(MR);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = send_email.getText().toString();
                Utils.hideKeyboard(ResetPaasswordActivity.this);

                if (email.equals("")){
                    Toast.makeText(ResetPaasswordActivity.this, "All fileds are required!", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ResetPaasswordActivity.this, "Please check you Email", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ResetPaasswordActivity.this, LoginActivity.class));
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(ResetPaasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}