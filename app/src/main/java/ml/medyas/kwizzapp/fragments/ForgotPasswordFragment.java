package ml.medyas.kwizzapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.medyas.kwizzapp.R;

import static ml.medyas.kwizzapp.classes.UtilsClass.isValidEmail;

public class ForgotPasswordFragment extends Fragment {
    @BindView(R.id.forgot_email) EditText email;
    @BindView(R.id.button_forgot) Button reset;

    private ForgotPasswordFragmentInterface mListener;

    public ForgotPasswordFragment() {
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
        View root = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        ButterKnife.bind(this, root);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInput()) {
                    mListener.onResetPassword(email.getText().toString());
                }
            }
        });

        return root;
    }

    public boolean validateInput() {
        boolean valid = true;
        if(!isValidEmail(email.getText())) {
            email.setError("Provide a valid Email Address!");
            valid = false;
        }

        return valid;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ForgotPasswordFragmentInterface) {
            mListener = (ForgotPasswordFragmentInterface) context;
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
    public interface ForgotPasswordFragmentInterface {
        void onResetPassword(String email);
    }
}
