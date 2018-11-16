package ml.medyas.kwizzapp.classes;


import com.google.firebase.auth.FirebaseUser;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UserSingleton {
    private static  FirebaseUser user;
    private static UserCategories userCategories;
    private static String userDoc;

    @Provides
    @Singleton
    public static UserCategories getUserCategories() {
        return userCategories;
    }

    public static void setUserCategories(UserCategories userCategories) {
        UserSingleton.userCategories = userCategories;
    }

    @Provides
    @Singleton
    public static FirebaseUser getUser() {
        return user;
    }

    public static void setUser(FirebaseUser user) {
        UserSingleton.user = user;
    }

    public static String getUserDoc() {
        return userDoc;
    }

    public static void setUserDoc(String userDoc) {
        UserSingleton.userDoc = userDoc;
    }
}
