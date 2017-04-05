package com.example.hexinary.onefeedv1;

import android.support.v7.app.AppCompatActivity;

import com.facebook.share.widget.LikeView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by hexinary on 17-4-1.
 */

class GoogleHandler {

    private UserProfile userProfileInstance;
    private MainActivity mainActivityInstance;

    private GoogleSignInOptions gso;
    private GoogleApiClient mobileGoogleApiClient;
    private static final String TAG = "SigninActivity";
    private static final int RC_SIGN_IN = 9001;

    public GoogleHandler(MainActivity ma) {
        this.mainActivityInstance = ma;
    }

    public GoogleHandler(UserProfile up) {
        this.userProfileInstance = up;
    }

    public void signOutGoolgeMainActivity() {
        Auth.GoogleSignInApi.signOut(this.getMobileGoogleApiClient()).setResultCallback(

                new ResultCallback<Status>() {

                    @Override
                    public void onResult(Status status) {
                        ProUtils.getInstance().log("signing out ahora");

                        /* Update the UI for a user who is signed out */
                        mainActivityInstance.updateUI(false);

                    }
                });
    }

    public void setOnClickListenerMainActivity() {
        this.mainActivityInstance.findViewById(R.id.google_provided_signin_button).setOnClickListener(this.mainActivityInstance);
    }

    public void setOnClickListenerUserProfile() {
        this.userProfileInstance.findViewById(R.id.google_provided_signin_button).setOnClickListener(this.userProfileInstance);
    }

    public void configureGoogleSignInOptionsUserProfile() {

        if (this.gso == null) {

            /* START configure sign in, Configure sign-in to request the user's ID, email address, and basic profile. ID and basic profile are included in DEFAULT_SIGN_IN. */
            this.gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        }
    }

    public void configureGoogleSignInOptionsMainActivity() {

        if (this.gso == null) {

            /* START configure sign in, Configure sign-in to request the user's ID, email address, and basic profile. ID and basic profile are included in DEFAULT_SIGN_IN. */
            this.gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        }
    }

    public void configureMobileGoogleApiClientMainActivity() {

        if (this.mobileGoogleApiClient == null) {

            /* START build_client Build a GoogleApiClient with access to the Google Sign-In API and the options specified by gso. */
            this.mobileGoogleApiClient = new GoogleApiClient.Builder(this.mainActivityInstance).enableAutoManage(this.mainActivityInstance, this.mainActivityInstance).addApi(Auth.GOOGLE_SIGN_IN_API, this.gso).build();

        }
    }

    public void configureMobileGoogleApiClientUserProfile() {

        if (this.mobileGoogleApiClient == null) {

            /* START build_client Build a GoogleApiClient with access to the Google Sign-In API and the options specified by gso. */
            this.mobileGoogleApiClient = new GoogleApiClient.Builder(this.userProfileInstance).enableAutoManage(this.userProfileInstance, this.userProfileInstance).addApi(Auth.GOOGLE_SIGN_IN_API, this.gso).build();

        }
    }

    public void configureGoogleSignInButtonMainActivity() {

        /* START customize_button Set the dimensions of the sign-in button. */
        SignInButton signInButton = (SignInButton) this.mainActivityInstance.findViewById(R.id.google_provided_signin_button);
        signInButton.setMinimumHeight(50);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

    }

    public void configureGoogleSignInButtonUserProfile() {

        /* START customize_button Set the dimensions of the sign-in button. */
        SignInButton signInButton = (SignInButton) this.userProfileInstance.findViewById(R.id.google_provided_signin_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

    }

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
