package com.serialmmf.Anbattery.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.serialmmf.Anbattery.battery.BatteryFragment;
import com.serialmmf.Anbattery.option.OptionFragment;

/**
 * Created by Juan Carlos Alvarez Carrillo.
 */

public class NougatPageAdapter extends FragmentPagerAdapter
{

    public NougatPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return BatteryFragment.newInstance();
            case 1:
                return OptionFragment.newInstance();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
