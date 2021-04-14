package com.arbind.musicplayer;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Adapter extends FragmentPagerAdapter {
    Context context;
    int tabCount;

    public Adapter(Context context, FragmentManager fm, int tabCount) {
        super(fm, tabCount);
        this.context = context;
        this.tabCount = tabCount;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                PlayerFragment playerFragment = new PlayerFragment();
                return playerFragment;
            case 1:
                SonglistFragment songlistFragment = new SonglistFragment();
                return songlistFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
