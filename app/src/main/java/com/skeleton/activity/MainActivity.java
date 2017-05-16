package com.skeleton.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.skeleton.R;
import com.skeleton.fragment.ViewPagerSignupSignin;

/**
 * Created by gaurav on 16/5/17.
 */

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.signup_signip_replace_frame, ViewPagerSignupSignin.getInstance());
        transaction.commit();

    }
}
