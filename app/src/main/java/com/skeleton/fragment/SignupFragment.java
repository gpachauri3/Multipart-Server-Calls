package com.skeleton.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.skeleton.R;
import com.skeleton.activity.DisplayResponseActivity;
import com.skeleton.model.Response;
import com.skeleton.retrofit.APIError;
import com.skeleton.retrofit.ApiInterface;
import com.skeleton.retrofit.MultipartParams;
import com.skeleton.retrofit.ResponseResolver;
import com.skeleton.retrofit.RestClient;
import com.skeleton.util.ValidateEditText;
import com.skeleton.util.customview.MaterialEditText;
import com.skeleton.util.imagepicker.ImageChooser;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.RequestBody;

import static com.skeleton.fragment.SigninFragment.clearEditText;

/**
 * Created by gaurav on 16/5/17.
 */

public class SignupFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = SignupFragment.class.getName();
    private CircleImageView imageView;
    private MaterialEditText editTextFname, editTextLname, editTextCounteryCode, editTextPhone, editTextEmail,
            editTextDOB, editTextPassword, editTextConfirmPassword;
    private Button buttonSignUp;
    private Spinner spinnerOrientation;
    private ImageChooser imageChooser;
    private RadioGroup radioGroupGender;
    private RadioButton radioButtonMale;
    private CheckBox checkBox;

    private String mOrientation, mPassword;
    private Uri uriImage;
    private int mGender;
    private String mCountryCode;
    private String mDateOfBirth;
    private File imagefile;

    /**
     * returns new instance of {@link SignupFragment}
     *
     * @return : instance of {@link SignupFragment}
     */
    public static SignupFragment getInstance() {
        return new SignupFragment();

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_page, container, false);
        init(view);

        spinnerOrientation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position,
                                       final long id) {

                mOrientation = spinnerOrientation.getAdapter().getItem(position).toString();
            }

            @Override
            public void onNothingSelected(final AdapterView<?> parent) {

            }
        });
        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final RadioGroup group, @IdRes final int checkedId) {
                if (checkedId == R.id.frag_signup_rb_male) {
                    mGender = 0;
                } else {
                    mGender = 1;
                }
            }
        });
        return view;
    }

    /**
     * Initializes the views and variables
     *
     * @param view : rootView of the fragment
     */
    private void init(final View view) {

        imageView = (CircleImageView) view.findViewById(R.id.frag_signup_img);
        editTextFname = (MaterialEditText) view.findViewById(R.id.frag_signup_et_fname);
        editTextLname = (MaterialEditText) view.findViewById(R.id.frag_signup_et_lname);
        editTextCounteryCode = (MaterialEditText) view.findViewById(R.id.frag_signup_et_country);
        editTextPhone = (MaterialEditText) view.findViewById(R.id.frag_signup_et_phone);
        editTextEmail = (MaterialEditText) view.findViewById(R.id.frag_signup_tv_email);
        editTextDOB = (MaterialEditText) view.findViewById(R.id.frag_signup_et_dob);
        editTextPassword = (MaterialEditText) view.findViewById(R.id.frag_signup_et_pass);
        editTextConfirmPassword = (MaterialEditText) view.findViewById(R.id.frag_signup_et_confpass);
        spinnerOrientation = (Spinner) view.findViewById(R.id.frag_signup_sp_orien);
        radioGroupGender = (RadioGroup) view.findViewById(R.id.frag_signup_rg);
        radioButtonMale = (RadioButton) view.findViewById(R.id.frag_signup_rb_male);
        checkBox = (CheckBox) view.findViewById(R.id.frag_signup_cb);

        /*
        Set floating label
         */
        enableFoatingEditText(editTextFname, editTextLname, editTextCounteryCode, editTextPhone
                , editTextEmail, editTextDOB, editTextPassword, editTextConfirmPassword);

        buttonSignUp = (Button) view.findViewById(R.id.frag_signup_bt_signup);
        buttonSignUp.setOnClickListener(this);
        view.getRootView().requestFocus();
        spinnerOrientation.requestFocus();
        spinnerOrientation.setAdapter(getAdapterForSpinner());
        imageView.setOnClickListener(this);
    /*
    Default Selections
     */
        radioButtonMale.setChecked(true);
        spinnerOrientation.setSelection(0);
        mGender = 0;
        mOrientation = getString(R.string.spinner_orientation_hindu);


    }

    /**
     * Implements methods of OnClickListener
     *
     * @param v : view clicked
     */
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.frag_signup_bt_signup:
                if (validateData()) {
                    uploadData();
                }
                break;
            case R.id.frag_signup_img:
                chooseImage();
                break;
            default:
                return;
        }
    }

    /**
     * Make server request to upload data
     */
    private void uploadData() {
        Toast.makeText(getContext(), "Data ready", Toast.LENGTH_SHORT).show();
        HashMap<String, RequestBody> multipartParams = new MultipartParams.Builder()
                .add(KEY_FRAGMENT_FNAME, editTextFname.getText().toString().trim())
                .add(KEY_FRAGMENT_LNAME, editTextLname.getText().toString().trim())
                .add(KEY_FRAGMENT_DOB, mDateOfBirth)
                .add(KEY_FRAGMENT_COUNTRY_CODE, mCountryCode)
                .add(KEY_FRAGMENT_PHONE, editTextPhone.getText().toString().trim())
                .add(KEY_FRAGMENT_EMAIL, editTextEmail.getText().toString().trim())
                .add(KEY_FRAGMENT_PASSWORD, editTextPassword.getText().toString().trim())
                .add(KEY_FRAGMENT_LANGUAGE, VALUE_FRAGMENT_LANGUAGE)
                .add(KEY_FRAGMENT_DEVICE_TYPE, VALUE_FRAGMENT_DEVICE_TYPE)
                .add(KEY_FRAGMENT_DEVICE_TOKEN, VALUE_RAGMENT_DEVICE_TOKEN)
                .add(KEY_FRAGMENT_APP_VERSION, VALUE_FRAGMENT_APP_VERSION)
                .add(KEY_FRAGMENT_GENDER, mGender)
                .add(KEY_FRAGMENT_ORIENTATION, mOrientation)
                .add(KEY_FRAGMENT_PROFILE_PIC, imagefile).build().getMap();

        ApiInterface apiInterface = RestClient.getApiInterface();
        apiInterface.userRegister(multipartParams).enqueue(new ResponseResolver<Response>(getActivity(), true, true) {
            @Override
            public void success(final Response response) {
                Log.d(TAG, "success: " + response.getStatusCode());
                Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                if ("200".equals(response.getStatusCode().toString())) {
                    clearEditText(editTextFname, editTextLname, editTextPhone, editTextDOB,
                            editTextConfirmPassword, editTextEmail, editTextPassword, editTextCounteryCode);
                    Intent intent = new Intent(getActivity(), DisplayResponseActivity.class);
                    intent.putExtra("response", response);
                    startActivity(intent);

                }
            }

            @Override
            public void failure(final APIError error) {
                Log.d(TAG, "failure: Status " + error.getStatusCode());
                Log.d(TAG, "failure: Message" + error.getMessage());
            }
        });


    }

    /**
     * Validate fields for sign up
     *
     * @return true: vaidated,else false
     */
    private boolean validateData() {
        mPassword = editTextPassword.getText().toString().trim();
        mCountryCode = editTextCounteryCode.getText().toString().trim();

        if (uriImage == null) {
            Toast.makeText(getContext(), "Please choose a pofile picture!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!ValidateEditText.checkName(editTextFname, true)) {
            return false;
        }
        if (!ValidateEditText.checkName(editTextLname, false)) {
            return false;
        }
        if (mCountryCode == null) {
            return false;
        }
        if (!checkDOB(editTextDOB)) {
            return false;
        }
        if (!ValidateEditText.checkPhoneNumber(editTextPhone)) {
            return false;
        }
        if (!ValidateEditText.checkEmail(editTextEmail)) {
            return false;
        }
        if (!ValidateEditText.checkPassword(editTextPassword, false)) {
            return false;
        }
        if (!ValidateEditText.checkPassword(editTextConfirmPassword, true)) {
            return false;
        }
        if (mOrientation == null) {
            Toast.makeText(getContext(), R.string.error_select_gender, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!mPassword.equals(editTextConfirmPassword.getText().toString().trim())) {
            Toast.makeText(getContext(), R.string.error_match_password, Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!checkBox.isChecked()) {
            Toast.makeText(getContext(), R.string.error_accept_terms, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Image chooser
     */
    private void chooseImage() {
        imageChooser = new ImageChooser.Builder(this).setCropEnabled(false).build();
        imageChooser.selectImage(new ImageChooser.OnImageSelectListener() {
            @Override
            public void loadImage(final List<ChosenImage> list) {
                Log.d(TAG, "loadImage: " + list.get(0).getQueryUri());
                uriImage = Uri.parse(list.get(0).getQueryUri());
                imagefile = new File(String.valueOf(uriImage));

                if (uriImage != null) {
                    //imageView.setImageURI(uriImage);
                    Glide.with(SignupFragment.this)
                            .load(uriImage)
                            .centerCrop()
                            .into(imageView);

                }
            }

            @Override
            public void croppedImage(final File mCroppedImage) {

            }
        });
    }

    /**
     * set adapter for the spinnerOrientation
     *
     * @return : array adapter for spinner
     */
    private ArrayAdapter<String> getAdapterForSpinner() {
        String[] orientations = {getString(R.string.spinner_orientation_hindu),
                getString(R.string.spinner_orientation_sikh), getString(R.string.spinner_orientation_christian)};
        ArrayAdapter adapterOrientation = new ArrayAdapter(getContext(),
                android.R.layout.simple_spinner_item, orientations);
        adapterOrientation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapterOrientation;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        imageChooser.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        imageChooser.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * Checks if date is in valid format
     *
     * @param editText : editTextDOB containing date
     * @return : true if valid, else returns false
     */
    private boolean checkDOB(final EditText editText) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String s = editText.getText().toString();
        try {
            Date date = df.parse(s);
            mDateOfBirth = s;
            return true;

        } catch (ParseException e) {
            editText.setError(getString(R.string.error_invalid_date));
            return false;
        }
    }

    /**
     * Enable floating label for {@link MaterialEditText}
     *
     * @param editTexts :list of editText
     */
    public static void enableFoatingEditText(final MaterialEditText... editTexts) {
        for (MaterialEditText editText : editTexts) {
            editText.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        }
    }
}
