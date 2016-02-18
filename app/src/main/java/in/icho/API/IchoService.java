package in.icho.API;

/**
 * Created by sahebjot on 2/13/16.
 */

import java.util.List;

import in.icho.model.Item;
import in.icho.model.Page;
import retrofit2.http.Body;
import retrofit2.http.POST;
import in.icho.model.User;
import in.icho.model.Token;

public interface IchoService {
    @POST("/register")
    retrofit2.Call<User> registerUser(@Body User user);

    @POST("/login")
    retrofit2.Call<Token> logUserIn(@Body User user);

    @POST("/api/items")
    retrofit2.Call<List<Item>> getList(@Body Page p);
}
