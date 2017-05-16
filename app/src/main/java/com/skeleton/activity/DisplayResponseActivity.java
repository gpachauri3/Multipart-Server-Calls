package com.skeleton.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.skeleton.R;
import com.skeleton.model.Response;

/**
 * Created by gaurav on 16/5/17.
 */

public class DisplayResponseActivity extends AppCompatActivity {
    private TextView textViewFname, textViewLname, textViewAccess, textViewId, textViewDob, textViewCreated,
            textViewUpdated, textViewCountry, textViewPhone, textViewUserImage, textViewGender,
            textViewAbout, textViewDrinking, textViewlanguage;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_response);
        init();

    }

    /**
     * parse data from response and set to textviews
     *
     * @param response : response from server
     */
    private void setData(final Response response) {
        textViewFname.append(response.getData().getUserDetails().getFirstName());
        textViewLname.append(response.getData().getUserDetails().getLastName());
        textViewAccess.append(response.getData().getAccessToken());
        textViewId.append(response.getData().getUserDetails().getId());
        textViewDob.append(response.getData().getUserDetails().getDob());
        textViewCreated.append(response.getData().getUserDetails().getCreatedAt());
        textViewUpdated.append(response.getData().getUserDetails().getUpdatedAt());
        textViewCountry.append(response.getData().getUserDetails().getCountryCode());
        textViewPhone.append(response.getData().getUserDetails().getPhoneNo());
        textViewGender.append(response.getData().getUserDetails().getGender());
        textViewAbout.append(response.getData().getUserDetails().getAboutMe());
        textViewlanguage.append(response.getData().getUserDetails().getLanguage());
    }

    /**
     * initilizes views and variables
     */
    private void init() {
        Response response = (Response) getIntent().getSerializableExtra("response");

        textViewFname = (TextView) findViewById(R.id.screen_b_first_name);
        textViewLname = (TextView) findViewById(R.id.screen_b_last_name);
        textViewAccess = (TextView) findViewById(R.id.screen_b_access);
        textViewId = (TextView) findViewById(R.id.screen_b_id);
        textViewDob = (TextView) findViewById(R.id.screen_b_dob);
        textViewCreated = (TextView) findViewById(R.id.screen_b_created_at);
        textViewUpdated = (TextView) findViewById(R.id.screen_b_updated_at);
        textViewDob = (TextView) findViewById(R.id.screen_b_dob);
        textViewCountry = (TextView) findViewById(R.id.screen_b_country_code);
        textViewPhone = (TextView) findViewById(R.id.screen_b_phone);
        textViewUserImage = (TextView) findViewById(R.id.screen_b_user_image);
        textViewGender = (TextView) findViewById(R.id.screen_b_gender);
        textViewAbout = (TextView) findViewById(R.id.screen_b_about);
        textViewlanguage = (TextView) findViewById(R.id.screen_b_language);
        setData(response);

    }
}

