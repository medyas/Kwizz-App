package ml.medyas.kwizzapp.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ml.medyas.kwizzapp.R;
import ml.medyas.kwizzapp.classes.CategoryItemClass;
import ml.medyas.kwizzapp.classes.UserCoins;
import ml.medyas.kwizzapp.fragments.CategoryItemFragment;
import ml.medyas.kwizzapp.fragments.EarnCoinsFragment;
import ml.medyas.kwizzapp.fragments.LeaderBoardFragment;
import ml.medyas.kwizzapp.fragments.NotificationsFragment;
import ml.medyas.kwizzapp.fragments.QuizFragment;
import ml.medyas.kwizzapp.fragments.SettingsFragment;

import static ml.medyas.kwizzapp.classes.UtilsClass.fixMinDrawerMargin;
import static ml.medyas.kwizzapp.classes.UtilsClass.getCategories;

public class MainActivity extends AppCompatActivity implements QuizFragment.OnFragmentInteractionListener, LeaderBoardFragment.OnFragmentInteractionListener,
        NotificationsFragment.OnFragmentInteractionListener, EarnCoinsFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener,
        CategoryItemFragment.CategoryItemInterface {
    public static final String CATEGORY_POSITION = "categoryPosition";
    public static final String QUESTION_DIFF = "questionDiff";
    public static final String QUESTION_NUM = "questionNum";
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.main_container) FrameLayout container;
    @BindView(R.id.user_letter) TextView userLetter;
    @BindView(R.id.user_name) TextView userName;
    @BindView(R.id.user_coins) TextView userCoins;
    @BindView(R.id.user_img) ImageView userImage;

    private int[] menuItems = {R.id.menu_item_quiz, R.id.menu_item_leaderboard, R.id.menu_item_notification, R.id.menu_item_earn_coins};

    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private FirebaseFirestore db;

    private final String TAG = getClass().getName();
    public static List<CategoryItemClass> categoryList = new ArrayList<ml.medyas.kwizzapp.classes.CategoryItemClass>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("usersCoins").child(user.getUid());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserCoins coins = dataSnapshot.getValue(UserCoins.class);
                userCoins.setText(String.format("%d Coins", coins.getUserCoins()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Could not retrieve user data!", Toast.LENGTH_SHORT).show();
            }
        });


        fixMinDrawerMargin(drawerLayout);

        categoryList.addAll(getCategories());

        if(savedInstanceState == null) {
            replaceFragment(new QuizFragment(), getString(R.string.quizzes));
        }
        userLetter.setText(user.getDisplayName().substring(0, 1).toUpperCase());
        userName.setText(String.format("%s%s", user.getDisplayName().substring(0, 1).toUpperCase(), user.getDisplayName().substring(1)));
        //userCoins.setText("2,5846 Coins");

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

    public void buyCategories() {
        Map<String, Object> u = new HashMap<>();
        u.put("userId", user.getUid());
        u.put("categories", Arrays.asList(""));

        // Add a new document with a generated ID
        db.collection("users")
                .add(u)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
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
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finishAffinity();
                Toast.makeText(this, "Please verify you email address!", Toast.LENGTH_SHORT).show();
            }
            if(!user.getPhotoUrl().equals("")) {
                userImage.setVisibility(View.VISIBLE);
                userLetter.setVisibility(View.GONE);
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .into(userImage);
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

    @Override
    public void onCategoryItemButtonClicked(final int position) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.question_difficulty, null);
        dialogBuilder.setView(dialogView);

        final Spinner spinnerNum = dialogView.findViewById(R.id.question_num);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterNum = ArrayAdapter.createFromResource(this,
                R.array.numbers_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterNum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerNum.setAdapter(adapterNum);

        final Spinner spinnerDiff = dialogView.findViewById(R.id.question_diff);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterDiff = ArrayAdapter.createFromResource(this,
                R.array.difficulty_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterDiff.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerDiff.setAdapter(adapterDiff);


        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Select", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(MainActivity.this, QuizzesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(CATEGORY_POSITION, position);
                bundle.putString(QUESTION_DIFF, getResources().getStringArray(R.array.difficulty_array)[spinnerDiff.getSelectedItemPosition()]);
                bundle.putString(QUESTION_NUM, getResources().getStringArray(R.array.numbers_array)[spinnerNum.getSelectedItemPosition()]);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        alertDialog.show();
    }
}
