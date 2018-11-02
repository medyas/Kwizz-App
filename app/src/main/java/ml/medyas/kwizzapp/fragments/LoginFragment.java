package ml.medyas.kwizzapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.medyas.kwizzapp.R;

import static ml.medyas.kwizzapp.classes.UtilsClass.isValidEmail;


public class LoginFragment extends Fragment {
    @BindView(R.id.login_forgot_pass) TextView forgotPass;
    @BindView(R.id.button_login) Button login;
    @BindView(R.id.login_email) EditText email;
    @BindView(R.id.login_password) EditText password;
    @BindView(R.id.button_spalsh_create_account) Button createAccount;
    @BindView(R.id.login_app_logo) ImageView logo;

    private LoginFragmentInterface mListener;

    public LoginFragment() {
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
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, root);

        Glide.with(this)
                .load(R.drawable.app_logo)
                .into(logo);

        forgotPass.setText(Html.fromHtml(getString(R.string.forgor_pass)));
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onForgotPassword();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateInputs()) {
                    mListener.onLoginUser(email.getText().toString(), password.getText().toString());
                }
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCreateAccount();
            }
        });

        return root;
    }

    public boolean validateInputs() {
        boolean valid = true;
        if (!isValidEmail(email.getText())) {
            email.setError("Provide a valid email address!");
            valid = false;
        }
        if (TextUtils.isEmpty(password.getText())) {
            password.setError("Password should not be empty!");
            valid = false;
        }
        if (password.getText().length() <= 5) {
            password.setError("A minimum of characters is required for your password!");
            valid = false;
        }
        return valid;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragmentInterface) {
            mListener = (LoginFragmentInterface) context;
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
    public interface LoginFragmentInterface {

        void onLoginUser(String email, String password);
        void onForgotPassword();
        void onCreateAccount();
    }
}
