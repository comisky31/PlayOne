package com.example.timwu.playone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by TimWu on 2017/12/15.
 */

public class HomeActivity extends AppCompatActivity
{
    private Button button_Login = null;
    private Button button_Sign_Up = null;
    private FirebaseAuth mAuth;

   @Override
    protected void onCreate( Bundle savedInstanceState)
   {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_home);

       button_Login = (Button)findViewById(R.id.home_login);
       button_Sign_Up = (Button)findViewById(R.id.home_sing_up);

       mAuth = FirebaseAuth.getInstance();
       FirebaseUser user = mAuth.getCurrentUser();

       if(user == null)
       {

           button_Login.setOnClickListener(new View.OnClickListener()
           {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent();
                   intent.setClass(HomeActivity.this, LoginActivity.class);
                   startActivity(intent);
                   finish();

               }
           });
           button_Sign_Up.setOnClickListener(new View.OnClickListener()
           {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent();
                   intent.setClass(HomeActivity.this, SignUpActivity.class);
                   startActivity(intent);
                   finish();
               }
           });
       }
       else
       {
           Intent intent = new Intent();
           intent.setClass(HomeActivity.this, MainActivity.class);
           startActivity(intent);
           finish();
       }

   }
   }


