package com.example.searchevent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

public class FragmentAdapter2 extends FragmentStateAdapter {
    public FragmentAdapter2(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 1:
                return new ArtistFragment();
            case 2:
                return new VenueFragment();
        }
        return new DetailFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
