package ml.medyas.kwizzapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ml.medyas.kwizzapp.R;
import ml.medyas.kwizzapp.adapter.LeaderBoardAdapter;
import ml.medyas.kwizzapp.classes.UserLeaderBoardClass;

import static ml.medyas.kwizzapp.classes.UtilsClass.calculateNoOfColumns;
import static ml.medyas.kwizzapp.classes.UtilsClass.getLeaderBoardList;

public class LeaderBoardFragment extends Fragment {
    @BindView(R.id.leader_board_recyclerview) RecyclerView mRecyclerView;

    private LeaderBoardAdapter mAdapter;
    private List<UserLeaderBoardClass> list = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public LeaderBoardFragment() {
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
        View root = inflater.inflate(R.layout.fragment_leader_board, container, false);
        ButterKnife.bind(this, root);

        list = getLeaderBoardList();

        GridLayoutManager layout = new GridLayoutManager(getContext(), calculateNoOfColumns(getActivity().getApplicationContext()));
        layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                if( mAdapter.getItemViewType(i) == 0) {
                    return calculateNoOfColumns(getActivity().getApplicationContext());
                } else {
                    return 1;
                }
            }
        });

        mRecyclerView.setLayoutManager(layout);
        mAdapter = new LeaderBoardAdapter(list, getContext());
        mRecyclerView.setAdapter(mAdapter);


        mAdapter.notifyDataSetChanged();

        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
