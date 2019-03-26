package miar.com.miar_project_client.api;

import java.util.ArrayList;

import miar.com.miar_project_client.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserApi
{
	UserApi service = RequestService.service.create( UserApi.class );

	@POST( "/api/membre/login" )
	Call< Boolean > login( @Body User user );

	@POST( "/api/membre" )
	Call< String > register( @Body User user );

	@GET("api/membre/{pseudo}")
	Call<User> getUserByName(@Path("pseudo") String pseudo);

	@GET("api/membre/average/{idEvenement}/{pseudo}")
	Call<Double> getAverageInEvent(@Path("idEvenement") String idEvenement, @Path("pseudo") String pseudo);

	@GET("api/membre/total/{idEvenement}/{pseudo}")
	Call<Double> getTotalInEvent(@Path("idEvenement") String idEvenement, @Path("pseudo") String pseudo);

	@PUT("api/membre/{pseudo}/{oldpass}/{newpass}/{oldmail}/{newmail}")
	Call<String> modifyUser(@Path("pseudo") String pseudo,@Path("oldpass") String oldpass,@Path("newpass") String newpass, @Path("oldmail") String oldmail,@Path("newmail") String newmail);

	@DELETE("/membre/{pseudo}")
    Call<Boolean> removeUser(@Path("pseudo")String pseudo);

	@GET("/membres")
	Call<ArrayList<User>> getAllUsers();
}
