package com.example.hexinary.onefeedv1;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
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

public class SigninActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }



//    public void loginUser(View v) {
//        EditText etUsername = (EditText)findViewById(R.id.username_signin);
//        String username = etUsername.getText().toString();
//
//        EditText etPassword = (EditText)findViewById(R.id.password_signin);
//        String password = etPassword.getText().toString();
//
//        if (credentialsValid(username, password)) {
//            Toast.makeText(this, "Login Successful! Loading Profile", Toast.LENGTH_LONG).show();
//            Intent intent = new Intent(getApplicationContext(), UserProfile.class);
//            startActivity(intent);
//        } else {
//            Toast.makeText(this, "Login Invalid", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    public boolean credentialsValid(String username, String password) {
//        try {
//            URL url = new URL("https://djcodes-onefeed-backend.appspot.com/login");
//            HttpURLConnection client = (HttpURLConnection) url.openConnection();
//            client.setRequestMethod("POST");
//            String postDataUrl = "username=" + username + "&" + "password=" + password;
//            client.setDoOutput(true);
//            DataOutputStream dataOutputStream = new DataOutputStream(client.getOutputStream());
//            dataOutputStream.writeBytes(postDataUrl);
//            dataOutputStream.flush();
//            dataOutputStream.close();
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
//            String inputLine = in.readLine();
//            ProUtils.getInstance().log(inputLine);
////            while ((inputLine = in.readLine()) != null)
////                ProUtils.getInstance().log(inputLine.toString());
////            in.close();
//
//            if (inputLine.equals("login,true")) {
//                return true;
//            } else {
//                return false;
//            }
//        } catch (ProtocolException e) {
//            e.printStackTrace();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }
}
