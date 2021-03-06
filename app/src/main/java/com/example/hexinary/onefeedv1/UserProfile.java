package com.example.hexinary.onefeedv1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class UserProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
    }

    public void facebookAuthorize(View v) {
        ProUtils.getInstance().log("authorizing facebook");
    }
    public void twitterAuthorize(View v) {
        ProUtils.getInstance().log("authorizing twitter");
    }
    public void instagramAuthorize(View v) {
        ProUtils.getInstance().log("authorizing instagram");
    }

    public void googleAuthorize(View v) {
        ProUtils.getInstance().log("authorizing google");
    }

}
