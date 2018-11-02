package ml.medyas.kwizzapp.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ml.medyas.kwizzapp.fragments.CategoryItemFragment;

import static ml.medyas.kwizzapp.activities.MainActivity.categoryList;

public class CategoryAdapter extends FragmentStatePagerAdapter {

    public CategoryAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment frag = new CategoryItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", i);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }
}
