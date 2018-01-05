package com.example.timwu.playone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
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
    private LoginButton fbLoginBtn;
    private Button loginBtn;
    private FirebaseUser user;
    private LoginManager loginManager;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FacebookSdk.setApplicationId("212742772606301");
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView()
    {
        //firebase
        mAuth = FirebaseAuth.getInstance();
        accountEdit = (EditText) findViewById(R.id.edit_account);
        passwordEdit = (EditText) findViewById(R.id.edit_password);
        passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
        accoutLayout = (TextInputLayout) findViewById(R.id.account_layout);
        passwordLayout = (TextInputLayout) findViewById(R.id.password_layout);
        passwordLayout.setErrorEnabled(true);
        accoutLayout.setErrorEnabled(true);
        loginBtn = (Button) findViewById(R.id.button_login);

        //facebook
        callbackManager = CallbackManager.Factory.create();
        fbLoginBtn = (LoginButton) findViewById(R.id.button_fb_login);
        fbLoginBtn.setReadPermissions("email", "public_profile");

        //firebase login
        loginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                loginFirebase();
            }
        });

        //facebook login
        fbLoginBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                loginFB();
            }
        });
    }

    private void loginFirebase()
    {
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

    private void loginFB()
    {
        fbLoginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("Facebook", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
                Log.d("Facebook", "facebook:onCancel:");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Facebook", "facebook:onError:");

            }


        });
    }

    private void handleFacebookAccessToken(AccessToken accessToken)
    {
        Log.d("Facebook", "handleFacebookAccessToken:" + accessToken);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Facebook Authentication Success", Toast.LENGTH_SHORT).show();
                            Log.d("Facebook", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w("Facebook", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Facebook Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onStart()
    {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    //sign up

}
