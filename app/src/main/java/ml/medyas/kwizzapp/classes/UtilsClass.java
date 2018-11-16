package ml.medyas.kwizzapp.classes;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ml.medyas.kwizzapp.R;

public class UtilsClass {

    static String[][] categories =

    {

        {
            "any", "Any Category", "25", ""
        },
        {
            "9", "General Knowledge", "1", ""
        },
        {
            "10", "Entertainment: Books", "2", ""
        },
        {
            "11", "Entertainment: Film", "3", ""
        },
        {
            "12", "Entertainment: Music", "4", ""
        },
        {
            "13", "Entertainment: Musicals & Theatres", "5", ""
        },
        {
            "14", "Entertainment: Television", "6", ""
        },
        {
            "15", "Entertainment: Video Games", "7", ""
        },
        {
            "16", "Entertainment: Board Games", "8", ""
        },
        {
            "17", "Science & Nature", "9", ""
        },
        {
            "18", "Science: Computers", "10", ""
        },
        {
            "19", "Science: Mathematics", "11", ""
        },
        {
            "20", "Mythology", "12", ""
        },
        {
            "21", "Sports", "13", ""
        },
        {
            "22", "Geography", "14", ""
        },
        {
            "23", "History", "15", ""
        },
        {
            "24", "Politics", "16", ""
        },
        {
            "25", "Art", "17", ""
        },
        {
            "26", "Celebrities", "18", ""
        },
        {
            "27", "Animals", "19", ""
        },
        {
            "28", "Vehicles", "20", ""
        },
        {
            "29", "Entertainment: Comics", "21", ""
        },
        {
            "30", "Science: Gadgets", "22", ""
        },
        {
            "31", "Entertainment: Japanese Anime & Manga", "23", ""
        },
        {
            "32", "Entertainment: Cartoon & Animations", "24", ""
        }
    };
    public static String json = "{ \"index\": \" %s \",\"name\": \" %s \",\"img\": \" %s \",\"description\": \" %s \"}";
    public static int imgs[] = {R.drawable.category1, R.drawable.category2, R.drawable.category3, R.drawable.category4, R.drawable.category5, R.drawable.category6, R.drawable.category7,
            R.drawable.category8, R.drawable.category9, R.drawable.category10, R.drawable.category11, R.drawable.category12, R.drawable.category13, R.drawable.category14, R.drawable.category15,
            R.drawable.category16, R.drawable.category17, R.drawable.category18, R.drawable.category19, R.drawable.category20, R.drawable.category21, R.drawable.category22, R.drawable.category23,
            R.drawable.category24, R.drawable.category25};

    public static List<CategoryItemClass> getCategories() {
        List<CategoryItemClass> list = new ArrayList<>();
        for(String[] temp: categories) {
            CategoryItemClass item = new CategoryItemClass(temp[0], temp[1], temp[2], temp[3]);
            list.add(item);
        }

        return list;
    }

    public static List<Categories> getDefaultCategories() {
        List<Categories> cat = new ArrayList<>();
        for (String[] temp: categories) {
            Categories c = new Categories(temp[0], "locked");
            cat.add(c);
        }

        return cat;
    }

    public static boolean isValidEmail(CharSequence target) {
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
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static int dp2px(Resources resource, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resource.getDisplayMetrics());
    }

    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }
}
