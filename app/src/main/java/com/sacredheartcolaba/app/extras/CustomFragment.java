package com.sacredheartcolaba.app.extras;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

/**
 * Created by user on 20-Mar-17.
 */

public abstract class CustomFragment extends Fragment implements Constants {


    private SwipeRefreshLayout mSwipeRefreshLayout;

    public CustomFragment() {
        // Required empty public constructor
    }

    public abstract void refresh();

    public abstract String getTAG();

    public abstract void onBackPressed();
}
