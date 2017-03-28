package com.example.hexinary.onefeedv1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        System.out.println("On creation method of Create Account page");
    }

    public void nextClick(View v) {
        EditText etUsername = (EditText)findViewById(R.id.username_input_box);
        String username = etUsername.getText().toString();

        EditText etPassword = (EditText)findViewById(R.id.password_input_box);
        String password = etPassword.getText().toString();

        EditText etConfirmPassword = (EditText)findViewById(R.id.confirm_password_input_box);
        String confirmPassword = etConfirmPassword.getText().toString();

        EditText etEmail = (EditText) findViewById(R.id.email_input_box);
        String email = etEmail.getText().toString();

        EditText etConfirmEmail = (EditText) findViewById(R.id.confirm_email_input_box);
        String confirmEmail = etConfirmEmail.getText().toString();

        if( username.length() < 2){
            Toast.makeText(this, "Username must have a length of 2 or more characters", Toast.LENGTH_LONG).show();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password and confirm password mismatch!", Toast.LENGTH_LONG).show();
        } else if (!email.equals(confirmEmail)) {
            Toast.makeText(this, "Email and confirm email mismatch!", Toast.LENGTH_LONG).show();
        } else {
            if (accountCreated(username, password, confirmPassword, email, confirmEmail)) {
                Toast.makeText(this, "Valid Entry! Loading Profile", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                startActivity(intent);
            }
        }
    }

    public boolean accountCreated(String username, String password, String confirmPassword, String email, String confirmEmail) {
        ProUtils.getInstance().logArgs(username, password, confirmPassword, email, confirmEmail);
        return true;
    }
}

//        System.out.println("Testing click hander");
//        System.out.println(v.getId());
//        ProUtils.getInstance().log("sup bich");
