package com.makoware.keyo.Views.Login;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.makoware.keyo.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by doman412 on 4/14/14.
 */
public class LoginView extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_view);

        final EditText username = (EditText)findViewById(R.id.usernameField);
        final EditText password = (EditText)findViewById(R.id.passwordField);

        Button loginButton = (Button)findViewById(R.id.loginView_loginBtn);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if(parseUser!=null){
                            finish();
                        } else {
                            Log.e("login", "login failed: "+e);
                        }
                    }
                });
            }
        });

        Button signupButton = (Button)findViewById(R.id.loginView_signUpBtn);
        signupButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Log.i("login", "goto signup");
            }
        });
    }

}
