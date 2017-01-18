package game.com.multiplecategory.mypojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by mii-android2 on 17/11/16.
 */
public class CatrgoryListContributer {

    @SerializedName("status")
    int status;

    @SerializedName("message")
    String message;

    @SerializedName("Data")
    ArrayList<CategoryArrayData> Data;

    public ArrayList<CategoryArrayData> getData() {
        return Data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
