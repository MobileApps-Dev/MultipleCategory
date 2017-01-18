package game.com.multiplecategory.retrofit2;

import game.com.multiplecategory.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mii-android2 on 11/1/17.
 */

public class MiiRestClient {

    Retrofit retrofit;
    SycnInterface service;

    public MiiRestClient(){

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.LIVE_DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(SycnInterface.class);
    }

    public SycnInterface getService(){
        return service;
    }
}
