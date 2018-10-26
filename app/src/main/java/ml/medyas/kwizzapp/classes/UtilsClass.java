package ml.medyas.kwizzapp.classes;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;

public class UtilsClass {

    public static int[] categoriesList = {9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32};

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static void fixMinDrawerMargin(DrawerLayout drawerLayout) {
        try {
            Field f = DrawerLayout.class.getDeclaredField("mMinDrawerMargin");
            f.setAccessible(true);
            f.set(drawerLayout, 0);

            drawerLayout.requestLayout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
