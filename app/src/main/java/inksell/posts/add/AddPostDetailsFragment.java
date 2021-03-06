package inksell.posts.add;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import butterknife.InjectView;
import enums.CategoryType;
import inksell.inksell.R;
import models.AutomobileEntity;
import models.ElectronicEntity;
import models.FurnitureEntity;
import models.IPostEntity;
import models.OtherEntity;
import utilities.Utility;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPostDetailsFragment extends BaseAddFragment {

    @InjectView(R.id.add_post_title)
    EditText postTitle;

    @InjectView(R.id.add_post_used_period)
    EditText usedPeriod;

    @InjectView(R.id.add_post_expected_price)
    EditText expectedPrice;

    @InjectView(R.id.add_post_actual_price)
    EditText actualPrice;

    @InjectView(R.id.add_post_make)
    EditText make;

    @InjectView(R.id.add_post_description)
    EditText description;

    public AddPostDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public int getViewResId() {
        return R.layout.fragment_add_post_details;
    }

    @Override
    public void initViewAfterSettingEditableView(LayoutInflater inflater, View view, Bundle savedInstanceState) {
    }

    @Override
    public void setEditableView(LayoutInflater inflater, View view, Bundle savedInstanceState) {
        postTitle.setEnabled(false);
        switch (categoryType) {
            case Electronics: {
                ElectronicEntity entity = (ElectronicEntity) iPostEntity;
                postTitle.setText(entity.PostTitle);
                usedPeriod.setText(entity.UsedPeriod);
                expectedPrice.setText(entity.ExpectedPrice);
                actualPrice.setText(entity.ActualPrice);
                make.setText(entity.MakeBrand);
                description.setText(entity.PostDescription);
            }
                break;
            case Automobile: {
                AutomobileEntity entity = (AutomobileEntity) iPostEntity;
                postTitle.setText(entity.PostTitle);
                postTitle.setText(entity.PostTitle);
                usedPeriod.setText(entity.UsedPeriod);
                expectedPrice.setText(entity.ExpectedPrice);
                actualPrice.setText(entity.ActualPrice);
                make.setText(entity.MakeBrand);
                description.setText(entity.PostDescription);
            }
                break;
            case Furniture: {
                FurnitureEntity entity = (FurnitureEntity) iPostEntity;
                postTitle.setText(entity.PostTitle);
                postTitle.setText(entity.PostTitle);
                usedPeriod.setText(entity.UsedPeriod);
                expectedPrice.setText(entity.ExpectedPrice);
                actualPrice.setText(entity.ActualPrice);
                make.setText(entity.MakeBrand);
                description.setText(entity.PostDescription);
            }
                break;
            case Others: {
                OtherEntity entity = (OtherEntity) iPostEntity;
                postTitle.setText(entity.PostTitle);
                postTitle.setText(entity.PostTitle);
                usedPeriod.setText(entity.UsedPeriod);
                expectedPrice.setText(entity.ExpectedPrice);
                actualPrice.setText(entity.ActualPrice);
                make.setText(entity.MakeBrand);
                description.setText(entity.PostDescription);
            }
                break;
        }
    }

    @Override
    public boolean verifyAndGetPost(IPostEntity iPostEntity, CategoryType categoryType) {

        if(Utility.IsEditTextNullorEmpty(postTitle))
        {
            Utility.ShowToast(R.string.ErrorTitleIsRequired);
            return false;
        }

        switch (categoryType)
        {
            case Electronics: {
                ElectronicEntity entity = (ElectronicEntity) iPostEntity;
                entity.PostTitle = postTitle.getText().toString();
                entity.UsedPeriod = usedPeriod.getText().toString();
                entity.ActualPrice = actualPrice.getText().toString();
                entity.ExpectedPrice = expectedPrice.getText().toString();
                entity.PostDescription = description.getText().toString();
                entity.MakeBrand = make.getText().toString();
            }
                break;
            case Automobile: {
                AutomobileEntity entity = (AutomobileEntity) iPostEntity;
                entity.PostTitle = postTitle.getText().toString();
                entity.UsedPeriod = usedPeriod.getText().toString();
                entity.ActualPrice = actualPrice.getText().toString();
                entity.ExpectedPrice = expectedPrice.getText().toString();
                entity.PostDescription = description.getText().toString();
                entity.MakeBrand = make.getText().toString();
            }
                break;
            case Furniture: {
                FurnitureEntity entity = (FurnitureEntity) iPostEntity;
                entity.PostTitle = postTitle.getText().toString();
                entity.UsedPeriod = usedPeriod.getText().toString();
                entity.ActualPrice = actualPrice.getText().toString();
                entity.ExpectedPrice = expectedPrice.getText().toString();
                entity.PostDescription = description.getText().toString();
                entity.MakeBrand = make.getText().toString();
            }
                break;
            case Others: {
                OtherEntity entity = (OtherEntity) iPostEntity;
                entity.PostTitle = postTitle.getText().toString();
                entity.UsedPeriod = usedPeriod.getText().toString();
                entity.ActualPrice = actualPrice.getText().toString();
                entity.ExpectedPrice = expectedPrice.getText().toString();
                entity.PostDescription = description.getText().toString();
                entity.MakeBrand = make.getText().toString();
            }
                break;
        }
        return true;
    }

    @Override
    public boolean canBeDiscarded() {
        if(Utility.IsEditTextNullorEmpty(postTitle))
        {
            return true;
        }
        return false;
    }
}
