package com.skeleton.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skeleton.R;

/**
 * Created by gaurav on 16/5/17.
 */

public class ViewPagerSignupSignin extends BaseFragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_signup_signin, container, false);
        init(view);
        return view;
    }

    /**
     * Intializes variables and views
     *
     * @param view : rootView of Fragment
     */
    private void init(final View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.frag_viewpager_tabs);
        viewPager = (ViewPager) view.findViewById(R.id.frag_viewpager_vp);
        /*
        Adapter for the viewpager
         */
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(final int position) {
                switch (position) {
                    case 0:
                        return SigninFragment.getInstance();
                    case 1:
                        return SignupFragment.getInstance();
                    default:
                        return SigninFragment.getInstance();

                }
            }

            @Override
            public CharSequence getPageTitle(final int position) {
                switch (position) {
                    case 0:
                        return TITLE_FRAGMENT_SIGNIN;
                    case 1:
                        return TITLE_FRAGMENT_SIGNUP;
                    default:
                        return TITLE_FRAGMENT_SIGNIN;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getContext(),
                R.color.button_primary));
        tabLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.chocklate_brown));
        tabLayout.setTabTextColors(ContextCompat.getColor(getContext(), R.color.gray_light),
                ContextCompat.getColor(getContext(), R.color.white));
    }


    /**
     * returns new instance of {@link ViewPagerSignupSignin}
     *
     * @return : instance of {@link ViewPagerSignupSignin}
     */
    public static ViewPagerSignupSignin getInstance() {
        return new ViewPagerSignupSignin();
    }

}
