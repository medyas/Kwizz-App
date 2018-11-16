package ml.medyas.kwizzapp.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ml.medyas.kwizzapp.R;
import ml.medyas.kwizzapp.classes.Categories;
import ml.medyas.kwizzapp.classes.CategoryItemClass;
import ml.medyas.kwizzapp.classes.UserCoins;
import ml.medyas.kwizzapp.classes.UserSingleton;
import ml.medyas.kwizzapp.fragments.CategoryItemFragment;
import ml.medyas.kwizzapp.fragments.EarnCoinsFragment;
import ml.medyas.kwizzapp.fragments.LeaderBoardFragment;
import ml.medyas.kwizzapp.fragments.NotificationsFragment;
import ml.medyas.kwizzapp.fragments.ProfileSettingsFragment;
import ml.medyas.kwizzapp.fragments.QuizFragment;
import ml.medyas.kwizzapp.fragments.SettingsFragment;

import static ml.medyas.kwizzapp.classes.UtilsClass.fixMinDrawerMargin;
import static ml.medyas.kwizzapp.classes.UtilsClass.getCategories;

public class MainActivity extends AppCompatActivity implements QuizFragment.OnFragmentInteractionListener, LeaderBoardFragment.OnFragmentInteractionListener,
        NotificationsFragment.OnFragmentInteractionListener, EarnCoinsFragment.OnFragmentInteractionListener, SettingsFragment.OnSettingsFragmentsInteractions,
        CategoryItemFragment.CategoryItemInterface, ProfileSettingsFragment.OnProfileSettingsFragmentInteractions {
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

    private int[] menuItems = {R.id.menu_item_quiz, R.id.menu_item_leaderboard, R.id.menu_item_notification, R.id.menu_item_earn_coins, R.id.menu_item_settings};

    private FirebaseUser user;
    private DatabaseReference mDatabase;
    private Boolean hiddenToolbar = false;
    private UserCoins coins;
    private FirebaseFirestore db;

    private final String TAG = getClass().getName();
    public static List<CategoryItemClass> categoryList = new ArrayList<ml.medyas.kwizzapp.classes.CategoryItemClass>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        UserSingleton.setUser(user);
        db = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("usersCoins").child(user.getUid());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                coins = dataSnapshot.getValue(UserCoins.class);
                userCoins.setText(String.format("%d Coins", coins.getUserCoins()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Could not retrieve user data!", Toast.LENGTH_SHORT).show();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // finally change the color
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.primaryLight));
        }

        fixMinDrawerMargin(drawerLayout);

        categoryList.addAll(getCategories());


        setSupportActionBar(toolbar);
        if(savedInstanceState == null) {
            replaceFragment(new QuizFragment(), getString(R.string.quizzes));
        } else {
            hiddenToolbar = savedInstanceState.getBoolean("toolbarHidden");
        }
        setUpToolbar(hiddenToolbar);
        if(savedInstanceState == null) {
            getSupportActionBar().setTitle(getString(R.string.quizzes));
        }

        userLetter.setText(user.getDisplayName().substring(0, 1).toUpperCase());
        userName.setText(String.format("%s%s", user.getDisplayName().substring(0, 1).toUpperCase(), user.getDisplayName().substring(1)));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("toolbarHidden", hiddenToolbar);
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
            if(user.getPhotoUrl() != null && !user.getPhotoUrl().equals("")) {
                userImage.setVisibility(View.VISIBLE);
                userLetter.setVisibility(View.INVISIBLE);
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(40)))
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onCategoryItemButtonClicked(final int position, final Categories categories) {
        if(UserSingleton.getUserCategories().getCategories().get(position).getStatus().equals("unlocked")) {
            showQuizDialog(position);
        } else {
            if(coins.getUserCoins() >= 250) {
                UserSingleton.getUserCategories().getCategories().get(position).setStatus("unlocked");
                // implement loading dialog
                UserCoins u = new UserCoins(user.getUid(), (coins.getUserCoins()-250));
                mDatabase.setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            db.collection("users").document(UserSingleton.getUserDoc())
                                    .update("categories", UserSingleton.getUserCategories().getCategories())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully updated!");
                                            showQuizDialog(position);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error updating document", e);
                                        }
                                    });
                        } else {
                            Toast.makeText(MainActivity.this, "Unexpected Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "You don\'t have sufficient coins!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showQuizDialog(final int position) {
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


    public void setUpToolbar(Boolean hide) {
        hiddenToolbar = hide;
        if (hide) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
            getSupportActionBar().setTitle("Profile Settings");
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onFragmentBackPressed();
                    setUpToolbar(false);
                }
            });
        } else {
            getSupportActionBar().setTitle(getString(R.string.settings));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_noun_menu_220342);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawerLayout.openDrawer(Gravity.START);
                }
            });
        }
    }

    @Override
    public void onAddSettingsFragment(Fragment frag) {
        setUpToolbar(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, frag, ProfileSettingsFragment.TAG)
                .addToBackStack(ProfileSettingsFragment.TAG)
                .commit();
    }

    @Override
    public void onFragmentBackPressed() {
        setUpToolbar(false);
        getSupportFragmentManager().popBackStack(ProfileSettingsFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onProfilePictureUpdate(Uri uri) {
        Glide.with(this)
                .load(uri)
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(40)))
                .into(userImage);
    }
}
