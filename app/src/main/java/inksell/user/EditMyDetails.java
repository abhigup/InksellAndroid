package inksell.user;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import Constants.AppData;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import inksell.common.BaseActionBarActivity;
import inksell.inksell.Home;
import inksell.inksell.R;
import models.UserEntity;
import services.InksellCallback;
import services.RestClient;
import utilities.NavigationHelper;
import utilities.ResponseStatus;
import utilities.Utility;

public class EditMyDetails extends BaseActionBarActivity {

    @InjectView(R.id.edit_my_image)
    CircleImageView myImage;

    @InjectView(R.id.edit_my_name)
    TextView myName;

    @InjectView(R.id.edit_my_email)
    TextView myEmail;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.edit_personal_email)
    EditText personalEmail;

    @InjectView(R.id.edit_address)
    EditText editAddress;

    @InjectView(R.id.edit_city)
    EditText editCity;

    @InjectView(R.id.edit_phone)
    EditText editPhone;

    @InjectView(R.id.loading_spinner)
    ProgressBar progressBar;

    @InjectView(R.id.layout_error_tryAgain)
    RelativeLayout layoutErrorTryAgain;

    @InjectView(R.id.tryAgainButton)
    Button tryAgainButton;

    @InjectView(R.id.edit_user_layout)
    LinearLayout layout;

    @InjectView(R.id.save_changes)
    Button saveChanges;

    @InjectView(R.id.loading_full_page)
    RelativeLayout loadingFullPage;

    @InjectView(R.id.loading_Text)
    TextView loadingText;

    private boolean isNewUser = false;

    @Override
    protected void initDataAndLayout() {
        progressBar.setVisibility(View.VISIBLE);
        layoutErrorTryAgain.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);

        loadUserData();
    }

    protected void setIntentExtras()
    {
        isNewUser = Boolean.parseBoolean(this.intentExtraMap.get("isNewUser"));
    }


    @Override
    protected void initActivity() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadingFullPage.setVisibility(View.GONE);
        saveChanges.setOnClickListener(saveUserDetails());
        tryAgainButton.setOnClickListener(refresh_click());
    }

    private View.OnClickListener saveUserDetails() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingText.setText(getString(R.string.saving));
                loadingFullPage.setVisibility(View.VISIBLE);

                final UserEntity userEntity = AppData.UserData;
                userEntity.Address = editAddress.getText().toString();
                userEntity.City = editCity.getText().toString();
                userEntity.PersonalEmail = personalEmail.getText().toString();
                userEntity.PhoneNumber = editPhone.getText().toString();

                RestClient.post().CreateUpdateUserData(userEntity).enqueue(new InksellCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        loadingFullPage.setVisibility(View.GONE);
                        ResponseStatus status = ResponseStatus.values()[integer];
                        AppData.UserData = userEntity;

                        NavigateToHomeIfNewUser();
                    }

                    @Override
                    public void onError(ResponseStatus responseStatus) {
                        loadingFullPage.setVisibility(View.GONE);
                        NavigateToHomeIfNewUser();
                    }
                });
            }
        };
    }

    private void NavigateToHomeIfNewUser()
    {
        if(isNewUser)
        {
            NavigationHelper.NavigateTo(Home.class, true);
        }
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_edit_my_details;
    }
//
//    @Override
//    protected DraggerPosition getDragPosition() {
//        return DraggerPosition.LEFT;
//    }

    private void loadUserData()
    {
        RestClient.get().getUserDetails(AppData.UserGuid).enqueue(new InksellCallback<UserEntity>() {
            @Override
            public void onSuccess(UserEntity userEntity) {
                progressBar.setVisibility(View.GONE);
                layoutErrorTryAgain.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);

                AppData.UserData = userEntity;
                initUserData();
            }

            @Override
            public void onError(ResponseStatus responseStatus)
            {
                progressBar.setVisibility(View.GONE);
                layoutErrorTryAgain.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
            }
        });

    }

    public void initUserData()
    {

        Utility.setUserPic(myImage, AppData.UserData.UserImageUrl, AppData.UserData.Username);

        myName.setText(AppData.UserData.Username);
        myEmail.setText(AppData.UserData.CorporateEmail);

        personalEmail.setText(AppData.UserData.PersonalEmail);
        editAddress.setText(AppData.UserData.Address);
        editCity.setText(AppData.UserData.City);
        editPhone.setText(AppData.UserData.PhoneNumber);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
