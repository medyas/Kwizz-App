package ml.medyas.kwizzapp.classes;

import android.text.TextUtils;

public class UtilsClass {

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
