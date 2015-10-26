package inksell.login;

import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import Constants.StorageConstants;
import butterknife.InjectView;
import inksell.common.BaseActionBarActivity;
import inksell.inksell.R;
import services.InksellCallback;
import services.RestClient;
import utilities.LocalStorageHandler;
import utilities.NavigationHelper;
import utilities.ResponseStatus;
import utilities.Utility;

public class already_activity extends BaseActionBarActivity {

    @InjectView(R.id.AlreadyTextEmail)
    TextView txtEmail;

    @Override
    protected void initDataAndLayout() {

    }

    @Override
    protected void initActivity() {
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_already_activity;
    }

    public void submit_click(View view) {
        String email = txtEmail.getText().toString();

        if(email.isEmpty()) {
            Utility.ShowInfoDialog(R.string.ErrorAlreadyEmptyEmail);
            return;
        }

        RestClient.get().registerUserAgain(email).enqueue(new InksellCallback<String>() {
            @Override
            public void onSuccess(String s) {
                if(Utility.GetUUID(s)!=null) {
                    LocalStorageHandler.SaveData(StorageConstants.UserUUID, s);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("isAlreadyRegistered", "true");
                    NavigationHelper.NavigateTo(verify_activity.class, map);
                }
            }

            @Override
            public void onError(ResponseStatus responseStatus) {

            }
        });
    }
}
