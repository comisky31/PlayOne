package com.example.timwu.playone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by TimWu on 2017/12/15.
 */

public class SignUpActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String account;
    private String password;
    private TextInputLayout accountLayout;
    private TextInputLayout passwordLayout;
    private EditText accountEdit;
    private EditText passwordEdit;
    private Button signUpBtn;
    private FirebaseUser user;


    @Override
   protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
    }

    private void initView()
    {
        mAuth = FirebaseAuth.getInstance();
        accountLayout = (TextInputLayout)findViewById(R.id.account_layout);
        passwordLayout = (TextInputLayout)findViewById(R.id.password_layout);
        accountEdit = (EditText)findViewById(R.id.edit_account);
        passwordEdit = (EditText)findViewById(R.id.edit_password);
        passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
        passwordLayout.setErrorEnabled(true);
        accountLayout.setErrorEnabled(true);
        signUpBtn = (Button)findViewById(R.id.button_sign_up);
        signUpBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                account = accountEdit.getText().toString();
                password = passwordEdit.getText().toString();
                if(TextUtils.isEmpty(account))
                {
                    accountLayout.setError(getString(R.string.plz_input_accout));
                    passwordLayout.setError("");
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    accountLayout.setError("");
                    passwordLayout.setError(getString(R.string.plz_input_password));
                    return;
                }

                accountLayout.setError("");
                passwordLayout.setError("");

                mAuth.createUserWithEmailAndPassword(account,password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {

                        if (task.isSuccessful())
                        {
                            Toast.makeText(SignUpActivity.this, R.string.register_success, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setClass(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });


    }

}
