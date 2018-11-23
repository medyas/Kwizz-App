package ml.medyas.kwizzapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.medyas.kwizzapp.R;
import ml.medyas.kwizzapp.classes.Categories;
import ml.medyas.kwizzapp.classes.CategoryItemClass;

import static ml.medyas.kwizzapp.activities.MainActivity.categoryList;
import static ml.medyas.kwizzapp.classes.UtilsClass.imgs;

public class CategoryItemFragment extends Fragment {
    @BindView(R.id.category_btn) LinearLayout categoryBtn;
    @BindView(R.id.category_btn_title) TextView categoryBtnTitle;
    @BindView(R.id.category_btn_sub) TextView categoryBtnSubTitle;
    @BindView(R.id.category_img) ImageView categoryImage;
    @BindView(R.id.category_title) TextView categoryTitle;

    private int position = 0;
    private Categories categories;

    private CategoryItemInterface mListener;

    public CategoryItemFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            position = getArguments().getInt("position");
            categories = getArguments().getParcelable("category");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_category_item, container, false);
        ButterKnife.bind(this, root);
        CategoryItemClass categoryItemClass = categoryList.get(position);


        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
        Glide.with(this)
                .load(imgs[Integer.parseInt(categoryItemClass.getImg())-1])
                .apply(requestOptions)
                .into(categoryImage);
        categoryTitle.setText(categoryItemClass.getName());

        if(categories.getStatus().equals("unlocked")) {
            categoryBtnTitle.setText("Start Quiz");
            categoryBtnSubTitle.setVisibility(View.GONE);
        }
        categoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCategoryItemButtonClicked(position, categories);
            }
        });

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CategoryItemInterface) {
            mListener = (CategoryItemInterface) context;
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
    public interface CategoryItemInterface {

        void onCategoryItemButtonClicked(int position, Categories categories);
    }
}
