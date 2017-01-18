package game.com.multiplecategory.retrofit2;

import game.com.multiplecategory.mypojo.CatrgoryListContributer;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by mii-android2 on 11/1/17.
 */

public interface SycnInterface {

    @GET("/miisecretory/API_category_list_app.php")
    Call<CatrgoryListContributer> getCategoryList();
}
