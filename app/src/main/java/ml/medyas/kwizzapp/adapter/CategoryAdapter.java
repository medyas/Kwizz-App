package ml.medyas.kwizzapp.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ml.medyas.kwizzapp.classes.Categories;
import ml.medyas.kwizzapp.fragments.CategoryItemFragment;

import static ml.medyas.kwizzapp.activities.MainActivity.categoryList;

public class CategoryAdapter extends FragmentStatePagerAdapter {
    private List<Categories> categories = new ArrayList<>();

    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }

    public CategoryAdapter(FragmentManager fm, List<Categories> categories) {
        super(fm);
        this.categories = categories;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment frag = new CategoryItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", i);
        bundle.putParcelable("category", categories.get(i));
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }
}
