package frgp.utn.edu.com.ui.informes;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;



import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentPagerAdapter;
import org.jetbrains.annotations.NotNull;

public class ViewPagerAdapter extends FragmentPagerAdapter {


    public ViewPagerAdapter(@NonNull @NotNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public @NotNull Fragment getItem(int position) {

        Fragment fragment;
        switch (position) {
            case 0:

                fragment = new InformesFragment();

                break;
            case 1:

                fragment = new InformesFragment1();
                break;
            // you may add more cases for more fragments
            default:
                // default fragment
                fragment = new InformesFragment();
                break;
        }

        return fragment;
    }
    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE; // Forzar recreación del Fragment
    }

    @Override
    public int getCount() {
        // Show total number of pages.
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {

            return "Reporte 1";
        } else  {
            return "Reporte 2";
        }

    }
}
