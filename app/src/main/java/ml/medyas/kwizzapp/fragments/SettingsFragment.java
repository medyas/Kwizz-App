package ml.medyas.kwizzapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ml.medyas.kwizzapp.R;
import ml.medyas.kwizzapp.activities.LoginActivity;

public class SettingsFragment extends Fragment {
    @BindView(R.id.settings_logout) Button logout;
    @BindView(R.id.settings_pic_text) TextView picText;
    @BindView(R.id.settings_email_text) TextView emailText;

    private OnSettingsFragmentsInteractions mListener;

    public SettingsFragment() {
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
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, root);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity().getApplicationContext(), LoginActivity.class));
                getActivity().finishAffinity();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        emailText.setText(user.getEmail());
        if(user.getPhotoUrl() != null && !user.getPhotoUrl().equals("")) {
            picText.setText(user.getDisplayName()+".jpeg");
        }

        return root;
    }

    @OnClick({R.id.settings_email, R.id.settings_password, R.id.settings_profile, R.id.settings_notify})
    public void onItemClick(LinearLayout item) {
        int id = item.getId();
        switch (id) {
            case R.id.settings_email:
                mListener.onAddSettingsFragment(ProfileSettingsFragment.newInstance(ProfileSettingsFragment.Settings.email));
                break;
            case R.id.settings_password:
                mListener.onAddSettingsFragment(ProfileSettingsFragment.newInstance(ProfileSettingsFragment.Settings.password));
                break;
            case R.id.settings_profile:
                mListener.onAddSettingsFragment(ProfileSettingsFragment.newInstance(ProfileSettingsFragment.Settings.picture));
                break;
            case R.id.settings_notify:
                mListener.onAddSettingsFragment(ProfileSettingsFragment.newInstance(ProfileSettingsFragment.Settings.notify));
                break;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSettingsFragmentsInteractions) {
            mListener = (OnSettingsFragmentsInteractions) context;
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
    public interface OnSettingsFragmentsInteractions {
        // TODO: Update argument type and name
        void onAddSettingsFragment(Fragment frag);
    }
}
