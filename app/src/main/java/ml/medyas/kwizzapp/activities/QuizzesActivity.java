package ml.medyas.kwizzapp.activities;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.medyas.kwizzapp.R;
import ml.medyas.kwizzapp.classes.CategoryItemClass;
import ml.medyas.kwizzapp.classes.OpentDBCalls;
import ml.medyas.kwizzapp.classes.OpentDBClass;
import ml.medyas.kwizzapp.classes.QuestionClass;
import ml.medyas.kwizzapp.classes.UserCoins;
import ml.medyas.kwizzapp.fragments.QuizQuestionFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static ml.medyas.kwizzapp.activities.MainActivity.CATEGORY_POSITION;
import static ml.medyas.kwizzapp.activities.MainActivity.QUESTION_DIFF;
import static ml.medyas.kwizzapp.activities.MainActivity.QUESTION_NUM;
import static ml.medyas.kwizzapp.classes.UtilsClass.getCategories;

public class QuizzesActivity extends AppCompatActivity implements QuizQuestionFragment.QuizQuestionInterface{
    @BindView(R.id.question_container) FrameLayout container;
    @BindView(R.id.question_toolbar) Toolbar toolbar;
    @BindView(R.id.content_failed) LinearLayout failedLayout;
    @BindView(R.id.content_loading) LinearLayout loadingLayout;
    @BindView(R.id.content_start) Button startQuiz;

    private int questionPosition = 0;
    private List<QuestionClass> questionsList = new ArrayList<QuestionClass>();

    private FirebaseUser user;
    private DatabaseReference mDatabase;

    private int position = 0;
    private boolean loaded = false;
    private long coinsCount = 0;
    private UserCoins userCoins;
    private String difficulty;
    private String questions;
    private CategoryItemClass categoryItemClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizzes);
        ButterKnife.bind(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference("usersCoins").child(user.getUid());
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userCoins = dataSnapshot.getValue(UserCoins.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

        if(getIntent() != null) {
            Bundle bundle =  getIntent().getExtras();
            position = bundle.getInt(CATEGORY_POSITION);
            difficulty = bundle.getString(QUESTION_DIFF);
            questions = bundle.getString(QUESTION_NUM);
        }
        if(savedInstanceState != null) {
            position = savedInstanceState.getInt(CATEGORY_POSITION);
            difficulty = savedInstanceState.getString(QUESTION_DIFF);
            questions = savedInstanceState.getString(QUESTION_NUM);
            questionsList = savedInstanceState.getParcelableArrayList("quizzes");
            loaded = savedInstanceState.getBoolean("loadedData");
        }

        if(loaded) {
            loadingLayout.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
        }

        categoryItemClass = getCategories().get(position);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        if (savedInstanceState == null) {
            if(difficulty.equals("any") && categoryItemClass.getIndex().equals("any")) {
                new OpentDBCalls().getQuestion(questions, "multiple").enqueue(new Callback<OpentDBClass>() {
                    @Override
                    public void onResponse(Call<OpentDBClass> call, Response<OpentDBClass> response) {
                        processQuestions(response.body().getResults());
                    }

                    @Override
                    public void onFailure(Call<OpentDBClass> call, Throwable t) {
                        processQuestions(null);
                    }
                });
            } else if(difficulty.equals("any")) {
                new OpentDBCalls().getQuestionByCategory(questions, categoryItemClass.getIndex(), "multiple").enqueue(new Callback<OpentDBClass>() {
                    @Override
                    public void onResponse(Call<OpentDBClass> call, Response<OpentDBClass> response) {
                        processQuestions(response.body().getResults());
                    }

                    @Override
                    public void onFailure(Call<OpentDBClass> call, Throwable t) {
                        processQuestions(null);
                    }
                });
            } else if(categoryItemClass.getIndex().equals("any")) {
                new OpentDBCalls().getQuestionByDiff(questions, difficulty,"multiple").enqueue(new Callback<OpentDBClass>() {
                    @Override
                    public void onResponse(Call<OpentDBClass> call, Response<OpentDBClass> response) {
                        processQuestions(response.body().getResults());
                    }

                    @Override
                    public void onFailure(Call<OpentDBClass> call, Throwable t) {
                        processQuestions(null);
                    }
                });
            } else {
                new OpentDBCalls().getQuestionAll(questions, categoryItemClass.getIndex(), difficulty, "multiple").enqueue(new Callback<OpentDBClass>() {
                    @Override
                    public void onResponse(Call<OpentDBClass> call, Response<OpentDBClass> response) {
                        processQuestions(response.body().getResults());
                    }

                    @Override
                    public void onFailure(Call<OpentDBClass> call, Throwable t) {
                        processQuestions(null);
                    }
                });
            }
        }

        getSupportActionBar().setTitle(categoryItemClass.getName());

        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startQuiz.setVisibility(View.GONE);
                showAkDialog();
            }
        });
    }

    private void showAkDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle("Starting Quiz");
        TextView txt = new TextView(this);
        txt.setText("By selecting an answer, the question will be processed!");
        dialogBuilder.setView(txt);
        dialogBuilder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i){
                container.setVisibility(View.VISIBLE);
                replaceFragment();
            }
        });
        dialogBuilder.create().show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(QUESTION_DIFF, difficulty);
        outState.putString(QUESTION_NUM, questions);
        outState.putInt(CATEGORY_POSITION, position);
        outState.putBoolean("loadedData", loaded);
        outState.putParcelableArrayList("quizzes", (ArrayList<? extends Parcelable>) questionsList);
    }

    @Override
    public void onBackPressed() {
        if(questionPosition < questionsList.size()) {
            quitDialog().show();
        } else {
            super.onBackPressed();
        }
    }

    public AlertDialog quitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("The quiz is still running, Are you sure you want to exit?");
        // Add the buttons
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                questionPosition = questionsList.size();
                UserCoins u = new UserCoins(user.getUid(), (userCoins.getUserCoins()+coinsCount));
                mDatabase.setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(QuizzesActivity.this, "Data saved.", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(QuizzesActivity.this, "Could not save you data!", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        });

        // Create the AlertDialog
        return builder.create();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void processQuestions(List<QuestionClass> results) {
        loaded = true;
        if ( results == null || results.isEmpty()) {
            failedLayout.setVisibility(View.VISIBLE);
            loadingLayout.setVisibility(View.GONE);
            Toast.makeText(this, "Found Empty Data!", Toast.LENGTH_SHORT).show();
            return;
        }

        loadingLayout.setVisibility(View.GONE);
        startQuiz.setVisibility(View.VISIBLE);
        questionsList.addAll(results);

    }

    @Override
    public void onFinished(boolean correct) {
        if (correct) {
            coinsCount++;
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(questionPosition < questionsList.size()) {
                    replaceFragment();
                } else {
                    // implment dialog for displsying the status  correct/missed questions
                    Toast.makeText(QuizzesActivity.this, "Completed the Quiz", Toast.LENGTH_SHORT).show();
                    updateUserCoins();
                    showCelebrationDialog();
                    handler.removeCallbacksAndMessages(this);
                }
            }
        }, 2000);

    }

    private void showCelebrationDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.celebration_dialog, null);
        alertDialog.setView(dialogView);

        ImageView img = dialogView.findViewById(R.id.celeb_background);
        Glide.with(this)
                .asGif()
                .load(R.raw.celeb_background)
                .into(img);
        TextView celebScore = dialogView.findViewById(R.id.celeb_score);
        TextView celebTitle = dialogView.findViewById(R.id.celeb_title);
        TextView celebText = dialogView.findViewById(R.id.celeb_text);
        celebScore.setText(String.format("%d/%d", coinsCount, questionsList.size()));
        celebTitle.setText(determineTitle());
        celebText.setText(String.format("You got %d out of %d questions", coinsCount, questionsList.size()));

        Button celebButton = dialogView.findViewById(R.id.celeb_btn);
        celebButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        alertDialog.setCancelable(false);
        AlertDialog dialog = alertDialog.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private String determineTitle() {
        int size = questionsList.size();
        if(size == coinsCount) {
            return "Congratulations";
        } else if(coinsCount <= (size/2)) {
            return "Too bad";
        } else {
            return "Nice Job";
        }
    }

    private void updateUserCoins() {
        UserCoins u = new UserCoins(user.getUid(), (userCoins.getUserCoins()+coinsCount));
        mDatabase.setValue(u);
    }

    public void replaceFragment() {
        Fragment frag = new QuizQuestionFragment();
        Bundle bundle = new Bundle();
        QuestionClass quiz = questionsList.get(0);
        Log.d("QuizFragment", quiz.getQuestion()+" " +quiz.getCorrect_answer()+" "+ quiz.getIncorrect_answers().length );
        bundle.putParcelable("question", questionsList.get(questionPosition));
        frag.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.question_container, frag)
                .commit();

        getSupportActionBar().setSubtitle(String.format("Question %d/%d", questionPosition+1, questionsList.size()));
        questionPosition++;
    }
}
