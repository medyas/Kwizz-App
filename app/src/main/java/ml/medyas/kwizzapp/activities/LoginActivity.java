package ml.medyas.kwizzapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.medyas.kwizzapp.R;
import ml.medyas.kwizzapp.fragments.ForgotPasswordFragment;
import ml.medyas.kwizzapp.fragments.LoginFragment;
import ml.medyas.kwizzapp.fragments.RegisterFragment;
import ml.medyas.kwizzapp.fragments.SplashFragment;

import static ml.medyas.kwizzapp.classes.UtilsClass.hideKeyboard;

public class LoginActivity extends AppCompatActivity implements SplashFragment.SplashFragmentInterface,
        LoginFragment.LoginFragmentInterface, RegisterFragment.RegisterFragmentInterface,
        ForgotPasswordFragment.ForgotPasswordFragmentInterface {
    @BindView(R.id.login_progress) ProgressBar progressBar;
    @BindView(R.id.login_container) FrameLayout container;

    public static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //FirebaseApp.initializeApp(LoginActivity.this);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        if(savedInstanceState == null) {
            replaceFragment(new SplashFragment());
        }
    }

    private void replaceFragment(Fragment frag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_container, frag)
                .commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finishAffinity();
        }
    }

    void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        container.setVisibility(View.GONE);
    }
    void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        container.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateAccount() {
        replaceFragment(new RegisterFragment());
    }

    @Override
    public void onLogIn() {
        replaceFragment(new LoginFragment());
    }

    @Override
    public void onLoginUser(String email, String password) {
        hideKeyboard(this);
        showProgressBar();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            hideProgressBar();
                        }

                    }
                });
    }

    @Override
    public void onForgotPassword() {
        replaceFragment(new ForgotPasswordFragment());
    }

    @Override
    public void onCreateNewAccount(final String username, String email, String password, String confirm) {
        hideKeyboard(this);
        showProgressBar();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User profile updated.");
                                                updateUI(user);
                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();
                                                updateUI(null);
                                                hideProgressBar();
                                            }
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                            hideProgressBar();
                        }
                    }
                });
    }

    @Override
    public void onResetPassword(String email) {
        hideKeyboard(this);
        showProgressBar();
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "A password reset email has benn sent!", Toast.LENGTH_LONG).show();
                            hideProgressBar();
                            replaceFragment(new LoginFragment());
                        }
                    }
                });
    }
}
