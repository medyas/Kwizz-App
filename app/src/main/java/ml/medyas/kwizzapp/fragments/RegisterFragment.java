package ml.medyas.kwizzapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.medyas.kwizzapp.R;

import static ml.medyas.kwizzapp.activities.LoginActivity.TAG;
import static ml.medyas.kwizzapp.classes.UtilsClass.isValidEmail;

public class RegisterFragment extends Fragment {
    @BindView(R.id.button_register) Button register;
    @BindView(R.id.register_username) EditText username;
    @BindView(R.id.register_email) EditText email;
    @BindView(R.id.register_password) EditText password;
    @BindView(R.id.register_pass_confirm) EditText confirm;
    @BindView(R.id.button_splash_login) Button login;

    private RegisterFragmentInterface mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, root);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInputs()) {
                    mListener.onCreateNewAccount(username.getText().toString(),
                            email.getText().toString(),
                            password.getText().toString(),
                            confirm.getText().toString());
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onLogIn();
            }
        });

        return root;
    }

    public boolean validateInputs() {
        boolean valid = true;
        if (TextUtils.isEmpty(username.getText())) {
            username.setError("Provide a username!");
            valid = false;
        }
        if (username.getText().length() <= 5) {
            username.setError("Username should be more then 5 characters!");
            valid = false;
        }
        if (!isValidEmail(email.getText())) {
            email.setError("Provide a valid email address!");
            valid = false;
        }
        if (TextUtils.isEmpty(password.getText())) {
            password.setError("Password should not be empty!");
            valid = false;
        }
        if (password.getText().length() <= 5) {
            password.setError("Password should be more then 5 characters!");
            valid = false;
        }
        if (!password.getText().toString().equals(confirm.getText().toString())) {
            Log.d(TAG, password.getText()+ ""+ confirm.getText());
            confirm.setError("Both passwords must match!");
            valid = false;
        }
        return valid;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegisterFragmentInterface) {
            mListener = (RegisterFragmentInterface) context;
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
    public interface RegisterFragmentInterface {

        void onCreateNewAccount(String username, String email, String password, String confirm);
        void onLogIn();
    }
}
