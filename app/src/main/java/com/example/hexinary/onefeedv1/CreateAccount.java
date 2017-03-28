package com.example.hexinary.onefeedv1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        System.out.println("On creation method of Create Account page");
    }

}

//        System.out.println("Testing click hander");
//        System.out.println(v.getId());
//        ProUtils.getInstance().log("sup bich");
