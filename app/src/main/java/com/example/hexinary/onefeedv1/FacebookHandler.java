package com.example.hexinary.onefeedv1;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * Created by hexinary on 17-4-1.
 */

class FacebookHandler {

    private MainActivity mainActivityInstance;
    private UserProfile userProfileInstance;
    private CallbackManager callbackManager;
    private LoginButton loginButton;

    public FacebookHandler(MainActivity ma) {

        this.mainActivityInstance = ma;

    }

    public FacebookHandler(UserProfile up) {

        this.userProfileInstance = up;

    }

    public void signIn() {
        ProUtils.getInstance().log("signing in facebook");
    }

    public void registerLoginCallback() {

        this.getLoginButton().registerCallback(this.getCallbackManager(), new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                ProUtils.getInstance().log("User has successfully logged in with facebook");
            }

            @Override
            public void onCancel() {
                ProUtils.getInstance().log("User has canceled facebook login");
            }

            @Override
            public void onError(FacebookException exception) {
                ProUtils.getInstance().log("Facebook Exception encountered: " + exception.toString());
            }

        });
    }

    public void initializeCallBackManager() {
        ProUtils.getInstance().log("Setting facebooks callback manager");
        this.callbackManager = CallbackManager.Factory.create();
    }

    public CallbackManager getCallbackManager() {
        return this.callbackManager;
    }

    public void configureLoginButton(MainActivity ma) {
        this.loginButton = (LoginButton) ma.findViewById(R.id.facebook_provided_signin_button);
        this.loginButton.setReadPermissions("email");
    }

    public LoginButton getLoginButton() {
        return this.loginButton;
    }
}
