package com.sacredheartcolaba.app.main_fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sacredheartcolaba.app.MainActivity;
import com.sacredheartcolaba.app.R;
import com.sacredheartcolaba.app.extras.CustomFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends CustomFragment {

    private final String TAG = getClass().getName();

    //    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MainActivity parentActivity;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_contact, container, false);

        parentActivity = (MainActivity) getActivity();
        parentActivity.setTitle(R.string.main_nav_contact);
//        mSwipeRefreshLayout = parentActivity.mSwipeRefreshLayout;

        return fragmentView;
    }

    @Override
    public void refresh() {
//        mSwipeRefreshLayout.setRefreshing(true);
//        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public String getTAG() {
        return TAG;
    }

    @Override
    public void onBackPressed() {
        parentActivity.getSupportFragmentManager().popBackStack();
    }

}
