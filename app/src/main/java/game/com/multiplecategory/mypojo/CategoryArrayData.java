package game.com.multiplecategory.mypojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by mii-android2 on 17/11/16.
 */
public class CategoryArrayData {

    @SerializedName("category")
    String category;

    @SerializedName("count")
    int count;

    @SerializedName("sub_category")
    ArrayList<String> sub_category;

    public String getCategory() {
        return category;
    }

    public ArrayList<String> getSub_category() {
        return sub_category;
    }

    public int getCount() {
        return count;
    }
}
