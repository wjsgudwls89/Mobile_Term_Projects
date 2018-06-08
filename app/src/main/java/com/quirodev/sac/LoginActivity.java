package com.quirodev.sac;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.common.api.ApiException;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.quirodev.sac.MainActivity;
import com.quirodev.sac.R;


/**
 * Created by hj on 2018. 5. 23..
 */

public class LoginActivity  extends AppCompatActivity implements View.OnClickListener {

        private static final int RC_SIGN_IN = 9001;
        private static final String TAG = "SignInActivity";

        private GoogleSignInClient mGoogleSignInClient;
        GoogleSignInAccount userAccount;
        private FirebaseAuth.AuthStateListener mListener;
        private FirebaseAuth mAuth;
        private FirebaseUser mUser;
        Button login,logout;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.login);

                mAuth = FirebaseAuth.getInstance();

                mListener = new FirebaseAuth.AuthStateListener() {
                        @Override
                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                mUser = mAuth.getCurrentUser();
                                if (mUser != null) {
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                } else {
                                        init();

                                }
                        }
                };
        }

        //초기화 함수
        protected void init(){

                // Views


                // Button listeners
                findViewById(R.id.sign_in_button).setOnClickListener(this);




                // Configure sign-in to request the user's ID, email address, and basic
                // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                // gso로 지정된 옵션을 사용하여 GoogleSignInClient를 빌드합니다.
                mGoogleSignInClient = GoogleSignIn. getClient ( this , gso );

        }


        @Override
        public void onStart() {
                super.onStart();

               mAuth.addAuthStateListener(mListener);
        }

        // [START onActivityResult]
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);

                // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
                if (requestCode == RC_SIGN_IN) {

                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try{ GoogleSignInAccount account = task.getResult(ApiException.class);
                                firebaseAuthWithGoogle(account);
                        }
                        catch(ApiException e) {
                                Log.w(TAG, "Google sign in failed", e);
                                //updateUI(null);
                        }
                        signOut(); //로그인 끊기.
                }
        }
        // [END onActivityResult]

        // [START handleSignInResult]
        private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
                int d = Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

                AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                                // Sign in success, update UI with the signed-in user's information
                                                Log.d(TAG, "signInWithCredential:success");
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                //updateUI(user);
                                        } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                                               // updateUI(null);
                                        }
                                }
                        });
        }
        // [END handleSignInResult]

        private void signIn() {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
        }
        // [END signin]

        private void signOut() {
                // Firebase sign out
                mAuth.signOut();
                //Toast.makeText(getApplicationContext(),"로그아웃 되었습니다",Toast.LENGTH_LONG).show();

                // Google sign out
                mGoogleSignInClient.signOut().addOnCompleteListener(this,
                        new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                        //updateUI(null);
                                }
                        });
        }

        private void revokeAccess() {
                // Firebase sign out
                mAuth.signOut();

                // Google revoke access
                mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                        new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                      //  updateUI(null);
                                }
                        });
        }
/*
        private void updateUI(FirebaseUser user) {
                if (user != null) {

                        findViewById(R.id.sign_in_button).setVisibility(View.GONE);
                       // findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
                } else {


                        findViewById(R.id.sign_in_button).setVisibility(View.GONE);
                      //  findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
                }
        }
*/
        //로그인 된 사용자의 개인정보 확인.
        //메일, 이름, 나이, 성별 확인 필요.
        private void profile() {
                String email = userAccount.getEmail();
                String id = userAccount.getId();
                String familyname = userAccount.getFamilyName();
                String givenname = userAccount.getGivenName();

                Toast.makeText(getApplicationContext(), "email : " + email + "\nid = " + id + "\nfamilyname = " + familyname + "\ngivenname = " + givenname, Toast.LENGTH_SHORT).show();

        }


        @Override
        public void onClick(View v) {
                switch (v.getId()) {
                        case R.id.sign_in_button:
                                signIn();
                                break;
                }
        }

        @Override
        protected void onStop() {
                super.onStop();
                mAuth.removeAuthStateListener(mListener);
        }
}