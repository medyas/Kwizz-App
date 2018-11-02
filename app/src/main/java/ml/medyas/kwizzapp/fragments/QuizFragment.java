package ml.medyas.kwizzapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.medyas.kwizzapp.R;
import ml.medyas.kwizzapp.adapter.CategoryAdapter;

import static ml.medyas.kwizzapp.classes.UtilsClass.dp2px;

public class QuizFragment extends Fragment {
    @BindView(R.id.view_pager) ViewPager viewPager;
    private int pagerPosition = 0;

    private OnFragmentInteractionListener mListener;

    public QuizFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        viewPager.setAdapter(new CategoryAdapter(getChildFragmentManager()));
        viewPager.setClipToPadding(false);
        viewPager.setPageMargin(dp2px(getResources(), 40));
        viewPager.setPadding(48, 8, 48, 8);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(pagerPosition, true);

        return root;
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
