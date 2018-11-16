package ml.medyas.kwizzapp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.medyas.kwizzapp.R;
import ml.medyas.kwizzapp.classes.SimpleIdlingResource;
import ml.medyas.kwizzapp.classes.UserCategories;
import ml.medyas.kwizzapp.classes.UserCoins;
import ml.medyas.kwizzapp.fragments.ForgotPasswordFragment;
import ml.medyas.kwizzapp.fragments.LoginFragment;
import ml.medyas.kwizzapp.fragments.RegisterFragment;
import ml.medyas.kwizzapp.fragments.SplashFragment;

import static ml.medyas.kwizzapp.classes.UtilsClass.getDefaultCategories;
import static ml.medyas.kwizzapp.classes.UtilsClass.hideKeyboard;

public class LoginActivity extends AppCompatActivity implements SplashFragment.SplashFragmentInterface,
        LoginFragment.LoginFragmentInterface, RegisterFragment.RegisterFragmentInterface,
        ForgotPasswordFragment.ForgotPasswordFragmentInterface {
    @BindView(R.id.login_progress) ProgressBar progressBar;
    @BindView(R.id.login_container) FrameLayout container;

    public static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseFirestore db;

    @Nullable
    private SimpleIdlingResource mIdlingResource  = null;
    public SimpleIdlingResource getIdlingResource() {
        if(mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //FirebaseApp.initializeApp(LoginActivity.this);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        if (getIdlingResource() != null) {
            getIdlingResource().setIdleState(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.primaryLight));
        }

        if(savedInstanceState == null) {
            replaceFragment(new LoginFragment());
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

        mIdlingResource.setIdleState(false);

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

                        mIdlingResource.setIdleState(true);

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

        mIdlingResource.setIdleState(false);

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
                                                createUserCategories(user.getUid(), user);
                                            } else {
                                                // If sign in fails, display a message to the user.
                                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();
                                                FirebaseAuth.getInstance().signOut();
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

    private void createUserCategories(final String uid, final FirebaseUser user) {
        UserCategories userCategories = new UserCategories(uid, getDefaultCategories());
        db.collection("users")
                .add(userCategories)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        writeNewUser(uid, user);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    private void writeNewUser(String userId, final FirebaseUser user) {
        UserCoins u = new UserCoins(userId, 250);
        mDatabase.child("usersCoins").child(userId).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    updateUI(user);
                } else {
                    Log.d(TAG, "Error, "+task.getException().getMessage());
                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    updateUI(null);
                    hideProgressBar();
                }

                mIdlingResource.setIdleState(true);
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
