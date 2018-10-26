package ml.medyas.kwizzapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import ml.medyas.kwizzapp.R;
import ml.medyas.kwizzapp.classes.OpentDBCalls;
import ml.medyas.kwizzapp.classes.OpentDBClass;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizFragment extends Fragment {

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

        new OpentDBCalls().getQuestion(10, 9, "multiple").enqueue(new Callback<OpentDBClass>() {
            @Override
            public void onResponse(Call<OpentDBClass> call, Response<OpentDBClass> response) {
                Log.d("QuizFragment", response.body().getResults().get(0).getQuestion());
            }

            @Override
            public void onFailure(Call<OpentDBClass> call, Throwable t) {

            }
        });

        return root;
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
