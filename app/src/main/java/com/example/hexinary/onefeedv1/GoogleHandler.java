package com.example.hexinary.onefeedv1;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by hexinary on 17-4-1.
 */

class GoogleHandler {

    private UserProfile userProfileInstance;
    private GoogleSignInOptions gso;
    private GoogleApiClient mobileGoogleApiClient;

    public GoogleHandler(UserProfile up) {

        this.userProfileInstance = up;

    }

    public void setOnClicks() {

        this.userProfileInstance.findViewById(R.id.google_provided_signin_button).setOnClickListener(this.userProfileInstance);
        this.userProfileInstance.findViewById(R.id.google_provided_signout_button).setOnClickListener(this.userProfileInstance);

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
            this.mobileGoogleApiClient = new GoogleApiClient.Builder(this.userProfileInstance).enableAutoManage(this.userProfileInstance, this.userProfileInstance).addApi(Auth.GOOGLE_SIGN_IN_API, this.gso).build();

        }
    }

}
