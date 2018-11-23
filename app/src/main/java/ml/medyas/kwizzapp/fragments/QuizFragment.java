package ml.medyas.kwizzapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.medyas.kwizzapp.R;
import ml.medyas.kwizzapp.adapter.CategoryAdapter;
import ml.medyas.kwizzapp.classes.UserCategories;
import ml.medyas.kwizzapp.classes.UserSingleton;

import static ml.medyas.kwizzapp.activities.MainActivity.ITEM_POSITION;
import static ml.medyas.kwizzapp.classes.UtilsClass.dp2px;
import static ml.medyas.kwizzapp.classes.UtilsClass.getDefaultCategories;

public class QuizFragment extends Fragment {
    @BindView(R.id.view_pager) ViewPager viewPager;
    @BindView(R.id.quiz_progress) ProgressBar progressBar;

    private int pagerPosition = 0;

    private static final float MIN_SCALE = 0.5f;
    private static final float MAX_ROTATION = 30;

    private OnFragmentInteractionListener mListener;
    private CategoryAdapter mAdapter ;

    public static final String TAG = "QuizFragment";
    private int itemPosition = 0;

    public QuizFragment() {
        // Required empty public constructor
    }

    private FirebaseFirestore db;
    private FirebaseUser user;
    private UserCategories userCategories;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = UserSingleton.getUser();
        db = FirebaseFirestore.getInstance();
        if (getArguments() != null) {
            itemPosition = getArguments().getInt(ITEM_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_quiz, container, false);
        ButterKnife.bind(this, root);

        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt("pagerPosition");
        }

        db.collection("users").whereEqualTo("uid", user.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                userCategories = document.toObject(UserCategories.class);
                                UserSingleton.setUserDoc(document.getId());
                                setupViewPager();
                                break;
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            createUserCategories(user.getUid());
                        }
                    }
                });

        return root;
    }

    private void setupViewPager() {
        UserSingleton.setUserCategories(userCategories);
        mAdapter = new CategoryAdapter(getChildFragmentManager(), userCategories.getCategories());
        viewPager.setAdapter(mAdapter);
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(dp2px(getResources(), 40));
        viewPager.setPadding(48, 8, 48, 8);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(pagerPosition, true);
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View view, float position) {
                final float scale = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                final float rotation = MAX_ROTATION * Math.abs(position);

                if (position <= 0f) {
                    view.setTranslationX(view.getWidth() * -position * 0.19f);
                    view.setPivotY(0.5f * view.getHeight());
                    view.setPivotX(0.5f * view.getWidth());
                    view.setScaleX(scale);
                    view.setScaleY(scale);
                    view.setRotationY(rotation);
                } else if (position <= 1f) {
                    view.setTranslationX(view.getWidth() * -position * 0.19f);
                    view.setPivotY(0.5f * view.getHeight());
                    view.setPivotX(0.5f * view.getWidth());
                    view.setScaleX(scale);
                    view.setScaleY(scale);
                    view.setRotationY(-rotation);
                }
            }
        });
        viewPager.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        viewPager.setCurrentItem(itemPosition, true);
    }

    private void createUserCategories(final String uid) {
        UserCategories uc = new UserCategories(uid, getDefaultCategories());
        db.collection("users")
                .add(uc)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        userCategories = documentReference.get().getResult().toObject(UserCategories.class);
                        UserSingleton.setUserDoc(documentReference.getId());
                        setupViewPager();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("pagerPosition", viewPager.getCurrentItem());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
