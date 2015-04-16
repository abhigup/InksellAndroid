package inksell.login;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import Constants.StorageConstants;
import butterknife.ButterKnife;
import butterknife.InjectView;
import inksell.inksell.Home;
import inksell.inksell.R;
import models.BaseActionBarActivity;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import services.RestClient;
import utilities.LocalStorageHandler;
import utilities.ResponseStatus;
import utilities.Utility;

public class verify_activity extends BaseActionBarActivity {

    String guid;
    boolean isAlreadyRegistered = false;

    @InjectView(R.id.verifyTextCode)
    TextView txtCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_activity);

        ButterKnife.inject(this);

        String uuid = LocalStorageHandler.GetData(StorageConstants.UserUUID, String.class);
        if(!Utility.IsStringNullorEmpty(uuid))
        {
            guid = uuid;
        }
    }

    @Override
    protected void setIntentExtras()
    {
        isAlreadyRegistered = Boolean.parseBoolean(this.intentExtraMap.get("isAlreadyRegistered").toString());
    }

    public void verify_click(View view) {
        if(Utility.IsStringNullorEmpty(guid))
        {
            Utility.ShowInfoDialog(R.string.ErrorUserNotExists);
            return;
        }
        if(Utility.IsStringNullorEmpty(txtCode.getText().toString()))
        {
            Utility.ShowInfoDialog(R.string.ErrorVerifyEmptyCode);
        }
        RestClient.get().verifyNewUser(guid, txtCode.getText().toString(), isAlreadyRegistered?1:0, new Callback<Integer>() {
            @Override
            public void success(Integer integer, Response response) {
                if(isAlreadyRegistered && ResponseStatus.values()[integer] == ResponseStatus.UserSuccessfullyVerified)
                {
                    Utility.NavigateTo(Home.class);
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
