package com.example.hexinary.onefeedv1;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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
            } else {
                Toast.makeText(this, "Form Invalid", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean accountCreated(String username, String password, String confirmPassword, String email, String confirmEmail) {
        ProUtils.getInstance().logArgs(username, password, confirmPassword, email, confirmEmail);
        try{
            URL url = new URL("https://djcodes-onefeed-backend.appspot.com/create_account");
            HttpURLConnection client = (HttpURLConnection) url.openConnection();
            client.setRequestMethod("POST");
            String postDataUrl = "username=" + username + "&"+ "password=" + password + "&" + "email=" + email + "&" + "confirm_password=" + confirmPassword + "&" + "confirm_email=" + confirmEmail;
            client.setDoOutput(true);
            DataOutputStream dataOutputStream = new DataOutputStream(client.getOutputStream());
            dataOutputStream.writeBytes(postDataUrl);
            dataOutputStream.flush();
            dataOutputStream.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                ProUtils.getInstance().log(inputLine.toString());
            in.close();

            return true;
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}

//        System.out.println("Testing click hander");
//        System.out.println(v.getId());
//        ProUtils.getInstance().log("sup bich");
