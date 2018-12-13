package ml.medyas.kwizzapp.classes;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import ml.medyas.kwizzapp.R;

public class UtilsClass {

    private static String[][] categories =

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
    public static String categoriesJson = "{ \"index\": \" %s \",\"name\": \" %s \",\"img\": \" %s \",\"description\": \" %s \"}";
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

    private static String[][] leaderBoardTemp =
            {
                    {"speechblatancy", "", "https://cdn.pixabay.com/photo/2017/08/01/08/29/people-2563491_960_720.jpg"},
                    {"customizereliably", "", "https://cdn.pixabay.com/photo/2016/01/19/17/48/woman-1149911_960_720.jpg"},
                    {"dorsetopera", "", "https://cdn.pixabay.com/photo/2014/05/03/00/50/flower-child-336658_960_720.jpg"},
                    {"illinoisyippee", "", "https://cdn.pixabay.com/photo/2014/07/31/23/49/guitarist-407212_960_720.jpg"},
                    {"fokemidriff", "", "https://cdn.pixabay.com/photo/2015/01/08/18/29/entrepreneur-593358_960_720.jpg"},
                    {"nigglejog", "", "https://cdn.pixabay.com/photo/2017/01/23/19/40/woman-2003647_960_720.jpg"},
                    {"sharplyconfused", "", "https://cdn.pixabay.com/photo/2018/01/06/09/25/hijab-3064633_960_720.jpg"},
                    {"doozystartop", "", "https://cdn.pixabay.com/photo/2015/08/05/04/25/people-875617_960_720.jpg"},
                    {"meansnowball", "", "https://cdn.pixabay.com/photo/2018/04/27/03/50/portrait-3353699_960_720.jpg"},
                    {"recolorfasten", "", "https://cdn.pixabay.com/photo/2016/01/19/18/04/man-1150058_960_720.jpg"},
                    {"blazerodkey", "", "https://cdn.pixabay.com/photo/2017/04/01/21/06/portrait-2194457_960_720.jpg"},
                    {"mauvemouth", "", "https://cdn.pixabay.com/photo/2017/08/01/01/33/beanie-2562646_960_720.jpg"},
                    {"pogoatticus", "", "https://cdn.pixabay.com/photo/2016/09/24/03/20/passion-1690965_960_720.jpg"},
                    {"ovalwharf", "", "https://cdn.pixabay.com/photo/2017/06/23/22/54/model-2436214_960_720.jpg"},
                    {"larkmanor", "", "https://cdn.pixabay.com/photo/2018/01/15/07/51/woman-3083375_960_720.jpg"},
                    {"werelutz", "", "https://cdn.pixabay.com/photo/2016/06/28/19/25/man-1485335_960_720.jpg"},
                    {"renewablysandpit", "", "https://cdn.pixabay.com/photo/2016/02/19/10/56/man-1209494_960_720.jpg"},
                    {"toolingproclaim", "", "https://cdn.pixabay.com/photo/2017/07/31/14/55/black-and-white-2558273_960_720.jpg"},
            };
    public static String leaderBoardJson = "{ \"username\": \" %s \",\"imageUrl\": \" %s \",\"score\": \" %d \"}";
    public static List<UserLeaderBoardClass> getLeaderBoardList() {
        List<UserLeaderBoardClass> list = new ArrayList<>();// create instance of Random class
        Random rand = new Random();
        for (String[] item: leaderBoardTemp) {
            int temp = rand.nextInt();
            list.add(new UserLeaderBoardClass(item[0], ((temp>0)?temp:-temp), item[2]));
        }

        Collections.sort(list, new Comparator<UserLeaderBoardClass>() {
            @Override
            public int compare(UserLeaderBoardClass userLeaderBoardClass, UserLeaderBoardClass t1) {
                return (userLeaderBoardClass.getScore() > t1.getScore()) ? -1 : (userLeaderBoardClass.getScore() < t1.getScore()) ? 1 : 0 ;
            }
        });

        return list;
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

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 115);
    }
}
