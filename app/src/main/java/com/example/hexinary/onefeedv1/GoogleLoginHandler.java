package com.example.hexinary.onefeedv1;

import android.support.v7.app.AppCompatActivity;

import com.facebook.share.widget.LikeView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by hexinary on 17-4-1.
 */

class GoogleHandler {

    private CreateAccount createAccountInstance;
    private UserProfile userProfileInstance;

    private Object<T> currentActivity;

    private GoogleSignInOptions gso;
    private GoogleApiClient mobileGoogleApiClient;
    private static final String TAG = "SigninActivity";
    private static final int RC_SIGN_IN = 9001;

    public <T> GoogleHandler(T ca) {

        this.createAccountInstance = ca;

    }

    public GoogleLoginHandler(UserProfile up) {

        this.userProfileInstance = up;
    }

    public UserProfile getUserProfileInstance() {
        return this.userProfileInstance;
    }

    public CreateAccount getCreateAccountInstance() {
        return this.createAccountInstance;
    }

    public AppCompatActivity getCurrentActivityInstance() {
        if ( this.createAccountInstance == null )
            return getCreateAccountInstance();
        else
            return getUserProfileInstance();

    }

    public void <T> setOnClicks(Object <T>) {

        getCurrentActivityInstance().findViewById(R.id.google_provided_signin_button).setOnClickListener(Object <T>);
        getCreateAccountInstance().findViewById(R.id.google_provided_signout_button).setOnClickListener(getCurrentActivityInstance());

    }

    public void configureGoogleSignInOptions() {

        if (this.gso == null) {

            /* START configure sign in, Configure sign-in to request the user's ID, email address, and basic profile. ID and basic profile are included in DEFAULT_SIGN_IN. */
            this.gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        }
    }

    public void configureMobileGoogleApiClient() {

        if (this.mobileGoogleApiClient == null) {

            /* START build_client Build a GoogleApiClient with access to the Google Sign-In API and the options specified by gso. */
            this.mobileGoogleApiClient = new GoogleApiClient.Builder(this.activityInstance).enableAutoManage(this.activityInstance, this.activityInstance).addApi(Auth.GOOGLE_SIGN_IN_API, this.gso).build();

        }
    }

    public void configureGoogleSignInButton() {

        /* START customize_button Set the dimensions of the sign-in button. */
        SignInButton signInButton = (SignInButton) this.activityInstance.findViewById(R.id.google_provided_signin_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

    }

    public void checkUserBrandNew() {}
    /* Getters and Setters */

    public GoogleApiClient getMobileGoogleApiClient() {

        return this.mobileGoogleApiClient;

    }

    public int getRcSignIn() {

        return this.RC_SIGN_IN;

    }

    public String getTag() {

        return this.TAG;

    }




}
