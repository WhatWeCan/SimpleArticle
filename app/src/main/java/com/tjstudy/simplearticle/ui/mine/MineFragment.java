package com.tjstudy.simplearticle.ui.mine;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tjstudy.simplearticle.R;

import butterknife.ButterKnife;

/**
 * 我的界面 Fragment
 */
public class MineFragment extends Fragment {

    private View fragmentMine;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (fragmentMine == null) {
            fragmentMine = inflater.inflate(R.layout.fragment_mine, container, false);
        }
        ViewGroup parent = (ViewGroup) fragmentMine.getParent();
        if (parent != null) {
            parent.removeView(fragmentMine);
        }
        ButterKnife.bind(this, fragmentMine);
        return fragmentMine;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
