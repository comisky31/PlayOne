package com.example.timwu.playone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.support.design.widget.TextInputLayout;


/**
 * Created by TimWu on 2017/12/15.
 */
public class LoginActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String account;
    private String password;
    private EditText accountEdit;
    private EditText passwordEdit;
    private TextInputLayout accoutLayout;
    private TextInputLayout passwordLayout;
    private Button loginBtn;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView()
    {
        mAuth = FirebaseAuth.getInstance();

        accountEdit = (EditText) findViewById(R.id.edit_account);
        passwordEdit = (EditText) findViewById(R.id.edit_password);
        passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());

        accoutLayout = (TextInputLayout) findViewById(R.id.account_layout);
        passwordLayout = (TextInputLayout) findViewById(R.id.password_layout);

        passwordLayout.setErrorEnabled(true);
        accoutLayout.setErrorEnabled(true);

        loginBtn = (Button) findViewById(R.id.button_login);

        loginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                account = accountEdit.getText().toString();
                password = passwordEdit.getText().toString();

                //帳號為空
                if(TextUtils.isEmpty(account))
                {
                    accoutLayout.setError(getString(R.string.plz_input_accout));
                    return;
                }

                if(TextUtils.isEmpty(password))
                {
                    passwordLayout.setError(getString(R.string.password));
                    return;
                }

                //錯誤訊息預設為空
                accoutLayout.setError("");
                passwordLayout.setError("");

                mAuth.signInWithEmailAndPassword(account, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete( Task<AuthResult> task)
                            {

                                if (task.isSuccessful())
                                {
                                    Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent();
                                    intent.setClass(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
