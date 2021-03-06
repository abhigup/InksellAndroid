package models;

import java.util.Date;

import utilities.Utility;

/**
 * Created by Abhinav on 20/07/15.
 */
public class PostSummaryEntity {

    public int CompanyId;

    public boolean IsSoldOut;

    public boolean IsVisibleToAll;

    public int LocationId;

    public int PostId;

    public Date Postdate;

    public String PostedBy;

    public String Title;

    public String UserImageUrl;

    public int categoryid;

    public String PostDefaultImage;

    public boolean isFavourite;

    public boolean HasPostTitlePic()
    {
        return !Utility.IsStringNullorEmpty(PostDefaultImage);
    }

    public boolean isEditable = false;
}
