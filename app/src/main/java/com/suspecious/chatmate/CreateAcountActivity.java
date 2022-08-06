package com.suspecious.chatmate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.suspecious.chatmate.Utility.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateAcountActivity extends AppCompatActivity {

    EditText username, email, password;
    TextView register_tv, msg_reg_tv;
    Button btn_register,btn_login;
    Typeface MR, MRR;
    FirebaseAuth auth;
    ImageView ShowHidePass;
    DatabaseReference reference;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acount);

      MRR = Typeface.createFromAsset(getAssets(), "fonts/myriadregular.ttf");
       MR = Typeface.createFromAsset(getAssets(), "fonts/myriad.ttf");

    /*    Toolbar toolbar = findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
       getSupportActionBar().setTitle("Create Account");
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
*/
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_register = findViewById(R.id.btn_register);

        msg_reg_tv = findViewById(R.id.msg_reg_tv);
        btn_login = findViewById(R.id.btn_login);
        ShowHidePass=findViewById(R.id.show_pass_btn);
        msg_reg_tv.setTypeface(MRR);
        username.setTypeface(MRR);
        email.setTypeface(MRR);
        password.setTypeface(MRR);
        btn_register.setTypeface(MR);

        auth = FirebaseAuth.getInstance();


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateAcountActivity.this,LoginActivity.class));
            }
        });
        ShowHidePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(view.getId()==R.id.show_pass_btn){
                    if(password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                        ((ImageView)(view)).setImageResource(R.drawable.eyeshow);
                        //Show Password
                        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                    else{
                        ((ImageView)(view)).setImageResource(R.drawable.eyehide);
                        //Hide Password
                        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    }
                }
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                Utils.hideKeyboard(CreateAcountActivity.this);

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(CreateAcountActivity.this, "All fileds are required", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6 ){
                    Toast.makeText(CreateAcountActivity.this, "password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    createaccount(txt_username, txt_email, txt_password);
                }
            }
        });
    }

    private void createaccount(final String username, String email, String password) {
        dialog = Utils.showLoader(CreateAcountActivity.this);

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    assert firebaseUser != null;
                    String userid = firebaseUser.getUid();

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("id", userid);
                    hashMap.put("username", username);
                    hashMap.put("imageURL", "default");
                    hashMap.put("status", "offline");
                    hashMap.put("bio", "");
                    hashMap.put("search", username.toLowerCase());
                    if(dialog!=null){
                        dialog.dismiss();
                    }
                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(CreateAcountActivity.this, LoginActivity.class);
                                Toast.makeText(CreateAcountActivity.this, "Success..", Toast.LENGTH_SHORT).show();
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                } else {
                    Toast.makeText(CreateAcountActivity.this, "You can't register woth this email or password", Toast.LENGTH_SHORT).show();
                    if(dialog!=null){
                        dialog.dismiss();
                    }
                }

            }
        });
    }
}