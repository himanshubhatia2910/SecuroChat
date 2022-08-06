package com.suspecious.chatmate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.firebase.auth.FirebaseUser;
import com.suspecious.chatmate.Utility.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button btn_login,btn_signup;
    Typeface MR,MRR;
    ProgressDialog dialog;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String Email = "emailKey";
    ImageView ShowHidePass;
    FirebaseAuth auth;
    TextView forgot_password, msg_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       //MRR = Typeface.createFromAsset(getAssets(), "font/myriadregular.ttf");
       // MR = Typeface.createFromAsset(getAssets(), "font/myriad.ttf");
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Login");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();

        btn_signup = findViewById(R.id.btn_signup);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        forgot_password = findViewById(R.id.forgot_password);
        msg_tv = findViewById(R.id.msg_tv);
        ShowHidePass=findViewById(R.id.show_pass_btn);
        msg_tv.setTypeface(MRR);

        email.setTypeface(MRR);
        password.setTypeface(MRR);
        btn_login.setTypeface(MRR);
        forgot_password.setTypeface(MRR);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),CreateAcountActivity.class);
                startActivity(intent);
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

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPaasswordActivity.class));

            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                Utils.hideKeyboard(LoginActivity.this);

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else {

                    dialog = Utils.showLoader(LoginActivity.this);
                    auth.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        FirebaseUser user = auth.getCurrentUser();
                                        Save(user);


                                     /*   Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        if(dialog!=null){
                                            dialog.dismiss();
                                        }
                                        startActivity(intent);
                                        finish();*/
                                    } else {
                                        if(dialog!=null){
                                            dialog.dismiss();
                                        }
                                        Toast.makeText(LoginActivity.this, "Authentication failed!", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }

            }
        });

    }

    private void Save(FirebaseUser user) {
        String email = user.getEmail();
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Email, email);
        editor.commit();


        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        if(dialog!=null){
                                            dialog.dismiss();
                                        }
                                        startActivity(intent);
                                        finish();
    }
}