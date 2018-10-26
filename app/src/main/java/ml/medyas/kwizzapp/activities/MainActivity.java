package ml.medyas.kwizzapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ml.medyas.kwizzapp.R;
import ml.medyas.kwizzapp.fragments.EarnCoinsFragment;
import ml.medyas.kwizzapp.fragments.LeaderBoardFragment;
import ml.medyas.kwizzapp.fragments.NotificationsFragment;
import ml.medyas.kwizzapp.fragments.QuizFragment;
import ml.medyas.kwizzapp.fragments.SettingsFragment;

import static ml.medyas.kwizzapp.classes.UtilsClass.fixMinDrawerMargin;

public class MainActivity extends AppCompatActivity implements QuizFragment.OnFragmentInteractionListener, LeaderBoardFragment.OnFragmentInteractionListener,
        NotificationsFragment.OnFragmentInteractionListener, EarnCoinsFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener{
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.main_container) FrameLayout container;
    @BindView(R.id.user_letter) TextView userLetter;
    @BindView(R.id.user_name) TextView userName;
    @BindView(R.id.user_coins) TextView userCoins;

    private int[] menuItems = {R.id.menu_item_quiz, R.id.menu_item_leaderboard, R.id.menu_item_notification, R.id.menu_item_earn_coins};

    private FirebaseUser user;
    private final String TAG = getClass().getName();

    // hide keybord, minimize the btn size / margins or paddings / change input text color / text size  finish

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        fixMinDrawerMargin(drawerLayout);

        if(savedInstanceState == null) {
            replaceFragment(new QuizFragment(), getString(R.string.quizzes));
        }
        userLetter.setText(user.getDisplayName().substring(0, 1).toUpperCase());
        userName.setText(String.format("%s%s", user.getDisplayName().substring(0, 1).toUpperCase(), user.getDisplayName().substring(1)));
        userCoins.setText("2,5846 Coins");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.quizzes));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_noun_menu_220342);
        drawerLayout.setFitsSystemWindows(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
        // NavUtils.navigateUpFromSameTask(this);
    }

    @OnClick(R.id.menu_close)
    public void onMenuClose() {
        drawerLayout.closeDrawers();
    }

    @OnClick({R.id.menu_item_quiz, R.id.menu_item_leaderboard, R.id.menu_item_notification, R.id.menu_item_earn_coins, R.id.menu_item_logout, R.id.menu_item_settings})
    public void onMenuItemClicked(LinearLayout layout) {
        int id = layout.getId();
        Fragment frag;
        switch (id) {
            case R.id.menu_item_quiz:
                frag = getSupportFragmentManager().findFragmentByTag(getString(R.string.quizzes));
                if(!(frag != null && frag.isVisible())) {
                    getSupportActionBar().setTitle(getString(R.string.quizzes));
                    replaceFragment(new QuizFragment(), getString(R.string.quizzes));
                }
                break;
            case R.id.menu_item_leaderboard:
                frag = getSupportFragmentManager().findFragmentByTag(getString(R.string.leaderboard));
                if(!(frag != null && frag.isVisible())) {
                    getSupportActionBar().setTitle(getString(R.string.leaderboard));
                    replaceFragment(new LeaderBoardFragment(), getString(R.string.leaderboard));
                }
                break;
            case R.id.menu_item_notification:
                frag = getSupportFragmentManager().findFragmentByTag(getString(R.string.notifications));
                if(!(frag != null && frag.isVisible())) {
                    getSupportActionBar().setTitle(getString(R.string.notifications));
                    replaceFragment(new NotificationsFragment(), getString(R.string.notifications));
                }
                break;
            case R.id.menu_item_earn_coins:
                frag = getSupportFragmentManager().findFragmentByTag(getString(R.string.earn_coins));
                if(!(frag != null && frag.isVisible())) {
                    getSupportActionBar().setTitle(getString(R.string.earn_coins));
                    replaceFragment(new EarnCoinsFragment(), getString(R.string.earn_coins));
                }
                break;
            case R.id.menu_item_settings:
                frag = getSupportFragmentManager().findFragmentByTag(getString(R.string.settings));
                if(!(frag != null && frag.isVisible())) {
                    getSupportActionBar().setTitle(getString(R.string.settings));
                    replaceFragment(new SettingsFragment(), getString(R.string.settings));
                }
                break;
            case R.id.menu_item_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finishAffinity();
                break;
        }

        setItemSelected(id);
        drawerLayout.closeDrawers();

    }

    public void replaceFragment(Fragment frag, String key) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, frag, key)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void setItemSelected(int itemId) {
        for(int id: menuItems) {
            findViewById(id).setBackground(null);
        }

        findViewById(itemId).setBackground(getResources().getDrawable(R.drawable.menu_item_background));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (user != null) {
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();
            if (!emailVerified) {
                user.sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Email sent.");
                                }
                            }
                        });

                Log.d(TAG, "Email not verified !");
            }

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
