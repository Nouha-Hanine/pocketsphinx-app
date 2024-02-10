package edu.cmu.pocketsphinx.demo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    Button loginButton;
    DatabaseHelper DBhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView signin= findViewById(R.id.signupText);

        SpannableString spannableString = new SpannableString("Not registered yet? Sign up here");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(LoginActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        };
        spannableString.setSpan(clickableSpan, 20, spannableString.length(), 0);
        spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 20, spannableString.length(), 0);
        // Set the SpannableString to the TextView
        signin.setText(spannableString);

        // Enable the movement method for the TextView to make the link clickable
        signin.setMovementMethod(LinkMovementMethod.getInstance());
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        DBhelper = new DatabaseHelper(this);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
              boolean isLog= DBhelper.checkAccount(username.getText().toString(), password.getText().toString());
              if(isLog){
                  navigateToChat();
              }else{
                  Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
              }
            }
        });

    }
    private void navigateToChat(){
        Intent i = new Intent(LoginActivity.this, ChatActivity.class);
        startActivity(i);

    }
}