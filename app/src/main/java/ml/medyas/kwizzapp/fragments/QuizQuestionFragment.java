package ml.medyas.kwizzapp.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.medyas.kwizzapp.R;
import ml.medyas.kwizzapp.classes.QuestionClass;

public class QuizQuestionFragment extends Fragment {
    @BindView(R.id.txtProgress) TextView txtProgress;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.progress_layout) FrameLayout progressLayout;
    @BindView(R.id.quiz_report) ImageView report;
    @BindView(R.id.quiz_fav) ImageView fav;
    @BindView(R.id.quiz_question) TextView quizQuestion;
    @BindView(R.id.quiz_answer1) RadioButton quizAnswer1;
    @BindView(R.id.quiz_answer2) RadioButton quizAnswer2;
    @BindView(R.id.quiz_answer3) RadioButton quizAnswer3;
    @BindView(R.id.quiz_answer4) RadioButton quizAnswer4;
    @BindView(R.id.quiz_group) RadioGroup quizGroup;

    private RadioButton[] btns;

    private QuizQuestionInterface mListener;

    private QuestionClass quiz;
    private CountDownTimer counter;
    private long quizTime = 30000;
    private long timeSec = 1000;
    private long counterPosition = 0;

    public QuizQuestionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            quiz = getArguments().getParcelable("question");
        }
        if(savedInstanceState != null) {
            quiz = savedInstanceState.getParcelable("question");
            counterPosition = savedInstanceState.getLong("counterPosition");
            quizTime = counterPosition;
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if ((int) getResources().getDimension(R.dimen.progress_text_size) == 28) {
            inflater.inflate(R.menu.quiz_question_menu, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.quiz_send) {
            if(counter != null) {
                counter.cancel();
                processAnswer();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_quiz_question, container, false);
        ButterKnife.bind(this, root);

        btns  = new RadioButton[]{quizAnswer1, quizAnswer2, quizAnswer3, quizAnswer4};

        String[] ans = new String[4];
        ans[0] = quiz.getIncorrect_answers()[0];
        ans[1] = quiz.getIncorrect_answers()[1];
        ans[2] = quiz.getIncorrect_answers()[2];
        ans[3] = quiz.getCorrect_answer();
        List<String> answers = Arrays.asList(ans);
        Collections.shuffle(answers);

        quizQuestion.setText(Html.fromHtml(quiz.getQuestion()));
        quizAnswer1.setText(answers.get(0));
        quizAnswer2.setText(answers.get(1));
        quizAnswer3.setText(answers.get(2));
        quizAnswer4.setText(answers.get(3));

        if(counter != null) counter = null;
        counter = new CountDownTimer(quizTime, timeSec) {
            @Override
            public void onTick(long l) {
                counterPosition = l;
                txtProgress.setText(String.format("%d", (l/1000)));
                if(getProgress(l) >= 75) {
                    progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.custom_progressbar_red));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    progressBar.setProgress(getProgress(l), true);
                } else {
                    progressBar.setProgress(getProgress(l));
                }
            }

            @Override
            public void onFinish() {
                txtProgress.setText(String.format("%d", 0));
                progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.custom_progressbar_red));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    progressBar.setProgress(100, true);
                } else {
                    progressBar.setProgress(100);
                }
                processAnswer();
            }
        }.start();

        quizGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                onButtonClicked(getRadioButton(radioGroup.getCheckedRadioButtonId()));
                processAnswer();
            }
        });

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Added to Favorite ", Toast.LENGTH_SHORT).show();
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Question reported !", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Nullable
    private RadioButton getRadioButton(int id) {
        RadioButton temp = null;
        switch (id) {
            case R.id.quiz_answer1:
                temp = quizAnswer1;
                break;
            case R.id.quiz_answer2:
                temp = quizAnswer2;
                break;
            case R.id.quiz_answer3:
                temp = quizAnswer3;
                break;
            case R.id.quiz_answer4:
                temp = quizAnswer4;
                break;
        }
        return temp;
    }

    public void processAnswer() {
        if(counter != null) counter.cancel();
        txtProgress.setText(String.format("%d", 0));
        progressBar.setProgress(0);
        quizGroup.setEnabled(false);
        boolean correct;
        if(getRadioButton(quizGroup.getCheckedRadioButtonId()) != null) {
            String answer = getRadioButton(quizGroup.getCheckedRadioButtonId()).getText().toString();
            correct = answer.equals(quiz.getCorrect_answer());
            if (correct) {
                getRadioButton(quizGroup.getCheckedRadioButtonId()).setTextColor(getResources().getColor(android.R.color.white));
                getRadioButton(quizGroup.getCheckedRadioButtonId()).setBackground(getResources().getDrawable(R.drawable.button_valid));
            } else {
                getRadioButton(quizGroup.getCheckedRadioButtonId()).setTextColor(getResources().getColor(android.R.color.white));
                getRadioButton(quizGroup.getCheckedRadioButtonId()).setBackground(getResources().getDrawable(R.drawable.button_invalid));
                determineCorrectAnswer();
            }
        } else {
            correct = false;
            determineCorrectAnswer();
        }
        mListener.onFinished(correct);

    }

    private void determineCorrectAnswer() {
        for(RadioButton btn: btns) {
            if (btn.getText().toString().equals(quiz.getCorrect_answer())) {
                btn.setTextColor(getResources().getColor(android.R.color.white));
                btn.setBackground(getResources().getDrawable(R.drawable.button_valid));
                break;
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("question", quiz);
        outState.putLong("counterPosition", counterPosition);
    }

    public int getProgress(long l) {
        return ((30 - ( (int) l/1000)) *100) /30;
    }

    //@OnClick({R.id.quiz_answer1, R.id.quiz_answer2, R.id.quiz_answer3, R.id.quiz_answer4})
    public void onButtonClicked(RadioButton btn) {
        for(RadioButton item: btns) {
            item.setTextColor(ContextCompat.getColorStateList(getActivity().getApplicationContext(), android.R.color.white));
            item.setChecked(false);
        }

        btn.setTextColor(ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.colorPrimary));
        btn.setChecked(true);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof QuizQuestionInterface) {
            mListener = (QuizQuestionInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        if(counter != null) {
            counter.cancel();
            counter = null;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface QuizQuestionInterface {

        void onFinished(boolean correct);
    }
}
