package inksell.posts.add;


import android.app.Fragment;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import Constants.AppData;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import inksell.inksell.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddUserDetailsFragment extends BaseAddFragment {

    @InjectView(R.id.add_post_contact_person)
    EditText contactPerson;

    @InjectView(R.id.add_post_contact_number)
    EditText contactNumber;

    @InjectView(R.id.add_post_contact_email)
    EditText contactEmail;

    @InjectView(R.id.add_post_contact_address)
    EditText contactAddress;

    @InjectView(R.id.add_post_contact_city)
    EditText contactCity;

    @InjectView(R.id.checkBoxUseMyContactInfo)
    CheckBox useMyContactInfo;

    @InjectView(R.id.my_image)
    CircleImageView myImage;

    @InjectView(R.id.my_name)
    TextView myName;

    public AddUserDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public int getViewResId() {
        return R.layout.fragment_add_user_details;
    }

    @Override
    public void initView(View view) {

        Picasso.with(getActivity()).load(AppData.UserData.UserImageUrl)
                .into(myImage);

        myName.setText(AppData.UserData.Username);

        useMyContactInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked && AppData.UserData!=null)
                {
                    contactPerson.setText(AppData.UserData.Username);
                    contactNumber.setText(AppData.UserData.PhoneNumber);
                    contactEmail.setText(AppData.UserData.PersonalEmail);
                    contactAddress.setText(AppData.UserData.Address);
                    contactCity.setText(AppData.UserData.City);
                }
            }
        });

    }
}