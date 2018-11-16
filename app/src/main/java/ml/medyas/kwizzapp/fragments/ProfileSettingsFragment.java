package ml.medyas.kwizzapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.medyas.kwizzapp.R;
import ml.medyas.kwizzapp.classes.UserSingleton;

import static ml.medyas.kwizzapp.classes.UtilsClass.getMimeType;
import static ml.medyas.kwizzapp.classes.UtilsClass.hideKeyboard;
import static ml.medyas.kwizzapp.classes.UtilsClass.isValidEmail;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnProfileSettingsFragmentInteractions} interface
 * to handle interaction events.
 * Use the {@link ProfileSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileSettingsFragment extends Fragment {
    @BindView(R.id.profile_settings_loader) ProgressBar loader;
    @BindView(R.id.picture_settings) ConstraintLayout pictureSettings;
    @BindView(R.id.profile_settings_picture) ImageView picture;
    @BindView(R.id.profile_settings_select) ImageButton select;
    @BindView(R.id.profile_settings_upload) Button upload;

    @BindView(R.id.email_settings) LinearLayout emailSettings;
    @BindView(R.id.settings_email_update) Button updateEmail;
    @BindView(R.id.settings_email) TextInputEditText email;
    @BindView(R.id.settings_email_conf) TextInputEditText emailConfirm;

    @BindView(R.id.password_settings) LinearLayout passwordSettings;
    @BindView(R.id.settings_password_update) Button updatePassword;
    @BindView(R.id.settings_password) TextInputEditText password;
    @BindView(R.id.settings_password_confirm) TextInputEditText passwordConfirm;

    @BindView(R.id.notify_settings) LinearLayout notifySettings;

    @BindView(R.id.upload_progress) TextView progress;

    public static final String ARG_PARAM1 = "param1";
    public static final int PICK_IMAGE = 1542;
    private Settings settings;
    private Uri imagePath = null;

    FirebaseStorage storage;
    StorageReference storageRef;
    private FirebaseUser user;
    private UploadTask  uploadTask;


    public static final String TAG = "ProfileSettingsFragment";

    private OnProfileSettingsFragmentInteractions mListener;

    public ProfileSettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ProfileSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileSettingsFragment newInstance(Settings param1) {
        ProfileSettingsFragment fragment = new ProfileSettingsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            settings = (Settings) getArguments().getSerializable(ARG_PARAM1);
        }

        user = UserSingleton.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile_settings, container, false);
        ButterKnife.bind(this, root);

        switch (settings) {
            case email:
                setupEmailSettings();
                break;
            case picture:
                setupPictureSettings();
                break;
            case password:
                setupPasswordSettings();
                break;
            case notify:
                setupNotifySettings();
                break;
        }

        return root;
    }

    private void setupNotifySettings() {
        notifySettings.setVisibility(View.VISIBLE);
    }

    private void setupPasswordSettings() {
        passwordSettings.setVisibility(View.VISIBLE);
        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validatePassword()) {
                    hideKeyboard(getActivity());
                    updatePassword(password.getText().toString());
                }
            }
        });
    }

    private void updatePassword(String password) {
        user.updatePassword(password)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity().getApplicationContext(), "Password Updated !", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Could not update your password!", Toast.LENGTH_SHORT).show();
                        }
                        mListener.onFragmentBackPressed();
                    }
                });
    }

    private boolean validatePassword() {
        boolean valid = true;
        if (!passwordConfirm.getText().toString().equals(password.getText().toString())) {
            passwordConfirm.setError("Passwords must match!");
            password.setError("Passwords must match!");
            valid = false;
        } else {
            passwordConfirm.setError(null);
            password.setError(null);
        }
        if (TextUtils.isEmpty(password.getText())) {
            password.setError("Password should not be empty!");
            valid = false;
        } else{
            password.setError(null);
        }
        if (password.getText().length() <= 5) {
            password.setError("A minimum of characters is required for your password!");
            valid = false;
        } else{
            password.setError(null);
        }
        if (TextUtils.isEmpty(passwordConfirm.getText())) {
            passwordConfirm.setError("Password should not be empty!");
            valid = false;
        } else{
            passwordConfirm.setError(null);
        }
        if (passwordConfirm.getText().length() <= 5) {
            passwordConfirm.setError("A minimum of characters is required for your password!");
            valid = false;
        } else{
            passwordConfirm.setError(null);
        }


        return valid;
    }

    private void setupEmailSettings() {
        emailSettings.setVisibility(View.VISIBLE);
        updateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateEmail()) {
                    hideKeyboard(getActivity());
                    updateEmail(email.getText().toString());
                }
            }
        });
    }

    private boolean validateEmail() {
        boolean valid = true;
        if(!emailConfirm.getText().toString().equals(email.getText().toString())) {
            email.setError("Emails does not match!");
            emailConfirm.setError("Emails does not match!");
            valid = false;
        } else {
            email.setError(null);
            emailConfirm.setError(null);
        }
        if (!isValidEmail(email.getText().toString())) {
            email.setError("Provide a valid email address!");
            valid = false;
        } else{
            email.setError(null);
        }
        if (!isValidEmail(emailConfirm.getText().toString())) {
            emailConfirm.setError("Provide a valid email address!");
            valid = false;
        } else{
            emailConfirm.setError(null);
        }


        return valid;
    }

    private void updateEmail(String email) {
        emailSettings.setVisibility(View.GONE);
        loader.setVisibility(View.VISIBLE);
        user.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity().getApplicationContext(), "Email Updated !", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Could not update email address!", Toast.LENGTH_SHORT).show();
                        }
                        mListener.onFragmentBackPressed();
                    }
                });
    }

    private void setupPictureSettings() {
        pictureSettings.setVisibility(View.VISIBLE);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        if(user.getPhotoUrl() != null && !user.getPhotoUrl().equals("")) {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(40)))
                    .into(picture);
        }

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imagePath == null) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please select a picture first!", Toast.LENGTH_SHORT).show();
                } else {
                    pictureSettings.setVisibility(View.GONE);
                    loader.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.VISIBLE);
                    deleteOldPicture();
                }
            }
        });
    }

    private void deleteOldPicture() {
        if(user.getPhotoUrl() != null && !user.getPhotoUrl().equals("")) {
            // Create a reference to the file to delete
            StorageReference desertRef = storageRef.child(String.format("profile/images/%s.%s", user.getUid(), getImageType(user.getPhotoUrl().toString())));

            // Delete the file
            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                    uploadPicture();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                    Toast.makeText(getActivity().getApplicationContext(), "Unable to upload picture", Toast.LENGTH_SHORT).show();
                    mListener.onFragmentBackPressed();
                }
            });
        } else {
            uploadPicture();
        }
    }

    private String getImageType(String s) {
        return s.substring(s.indexOf(user.getUid())+user.getUid().length()+1, s.indexOf("?alt"));
    }

    private void uploadPicture() {

        final String path = user.getUid()+"."+getMimeType(getActivity().getApplicationContext(), imagePath);
        StorageReference riversRef = storageRef.child(String.format("profile/images/%s", path));
        uploadTask = riversRef.putFile(imagePath);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        progress.setText(String.format("%d", getProgress(taskSnapshot.getBytesTransferred(), taskSnapshot.getTotalByteCount())));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        exception.printStackTrace();
                        Toast.makeText(getActivity().getApplicationContext(), "Unable to upload picture", Toast.LENGTH_SHORT).show();
                        mListener.onFragmentBackPressed();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        getDownloadLink(path);
                    }
                });

    }

    private void getDownloadLink(String path) {
        storageRef.child("profile/images/"+path).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(final Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                user.updateProfile(new UserProfileChangeRequest.Builder()
                        .setPhotoUri(uri)
                        .build())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity().getApplicationContext(), "Picture Updated", Toast.LENGTH_SHORT).show();
                            mListener.onProfilePictureUpdate(uri);
                            mListener.onFragmentBackPressed();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Unable to update Picture", Toast.LENGTH_SHORT).show();
                            mListener.onFragmentBackPressed();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    private int getProgress(long bytesTransferred, long totalByteCount) {
        return (int) ((bytesTransferred*100)/totalByteCount);
    }

    private void pickImage() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Profile Picture");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                Toast.makeText(getActivity().getApplicationContext(), "Error selecting picture", Toast.LENGTH_SHORT).show();
                return;
            }
            imagePath = data.getData();
            Glide.with(this)
                    .load(imagePath)
                    .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(40)))
                    .into(picture);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(uploadTask != null) uploadTask.cancel();
        if (context instanceof OnProfileSettingsFragmentInteractions) {
            mListener = (OnProfileSettingsFragmentInteractions) context;
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
    public interface OnProfileSettingsFragmentInteractions {
        // TODO: Update argument type and name
        void onFragmentBackPressed();
        void onProfilePictureUpdate(Uri uri);
    }

    public enum Settings {
        email, password, picture, notify
    }
}
