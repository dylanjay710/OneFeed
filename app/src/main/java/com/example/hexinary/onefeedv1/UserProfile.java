package com.example.hexinary.onefeedv1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class UserProfile extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    /* Declare components used in the user profile */

    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;

    /* Declare our social network handlers passing in a reference to this class */
    private FacebookHandler facebookHandler = new FacebookHandler(this);
    private GoogleHandler googleHandler = new GoogleHandler(this);
    private InstagramHandler instagramHandler = new InstagramHandler(this);
    private TwitterHandler twitterHandler = new TwitterHandler(this);

    /* Declare database handler */
    private DatabaseHandler databaseHandler = new DatabaseHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_profile);

        /* Google Handler Setup */
        configureGoogle();

    }

    public void configureGoogle() {
        this.googleHandler.setOnClicks();
        this.googleHandler.configureGoogleSignInOptions();
        this.googleHandler.configureMobileGoogleApiClient();
        this.googleHandler.configureGoogleSignInButton();
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(this.googleHandler.getMobileGoogleApiClient());

        if (opr.isDone()) {

            ProUtils.getInstance().log("Checking user cached google credentials");

            /* If the user's cached credentials are valid, the OptionalPendingResult will be "done" and the GoogleSignInResult will be available instantly. */
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);

        } else {

             /* If the user has not previously signed in on this device or the sign-in has expired, this asynchronous branch will attempt to sign in the user silently.  Cross-device single sign-on will occur in this branch. */
            ProUtils.getInstance().log("User has expired credentials or is signing in for the first time");
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {

                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }

            });

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == this.googleHandler.getRcSignIn()) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    /* Start signIn */
    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(this.googleHandler.getMobileGoogleApiClient());
        startActivityForResult(signInIntent, this.googleHandler.getRcSignIn());

    }

    /* Start signOut */
    private void signOut() {

        Auth.GoogleSignInApi.signOut(this.googleHandler.getMobileGoogleApiClient()).setResultCallback(

                new ResultCallback<Status>() {

                    @Override
                    public void onResult(Status status) {
                        ProUtils.getInstance().log("signing out ahora");

                        /* Update the UI for a user who is signed out */
                        updateUI(false);

                    }
                });
    }

    /* Start revokeAccess */
    private void revokeAccess() {

        Auth.GoogleSignInApi.revokeAccess(this.googleHandler.getMobileGoogleApiClient()).setResultCallback(

                new ResultCallback<Status>() {

                    @Override
                    public void onResult(Status status) {

                        updateUI(false);

                    }

                });
    }

    /* Start handleSignInResult */
    private void handleSignInResult(GoogleSignInResult result) {

        /* String concatenation means implicit casting ot booleans! */
        ProUtils.getInstance().log("Login Success: " + result.isSuccess());

        if (result.isSuccess()) {

            /* Signed in successfully, show authenticated UI. */
            GoogleSignInAccount acct = result.getSignInAccount();
            String displayName = acct.getDisplayName();
            ProUtils.getInstance().log(displayName);

        } else {

            ProUtils.getInstance().log("User has signed out of google");
        }
    }

    private void showProgressDialog() {

        if (mProgressDialog == null) {

            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("loading...");
            mProgressDialog.setIndeterminate(true);

        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {

            mProgressDialog.hide();

        }
    }

    private void updateUI(boolean signedIn) {

        if (signedIn) {

            findViewById(R.id.google_provided_signin_button).setVisibility(View.GONE);
            findViewById(R.id.google_provided_signout_button).setVisibility(View.VISIBLE);

        } else {

            mStatusTextView.setText("signed out");

            findViewById(R.id.google_provided_signin_button).setVisibility(View.VISIBLE);
            findViewById(R.id.google_provided_signout_button).setVisibility(View.GONE);

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.google_provided_signin_button:
                signIn();
                break;

            case R.id.google_provided_signout_button:
                signOut();
                break;

            case R.id.disconnect_button:
                revokeAccess();
                break;

        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        /* An unresolvable error has occurred and Google APIs (including Sign-In) will not be available. */
        ProUtils.getInstance().log("onConnectionFailed execution");

    }

/* End Google Login */




    public void facebookAuthorize(View v) {
        ProUtils.getInstance().log("authorizing facebook");
    }
    public void twitterAuthorize(View v) {
        ProUtils.getInstance().log("authorizing twitter");
    }
    public void instagramAuthorize(View v) {
        ProUtils.getInstance().log("authorizing instagram");
    }
}
