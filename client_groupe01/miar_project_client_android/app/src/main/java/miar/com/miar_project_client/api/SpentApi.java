package miar.com.miar_project_client.api;

import java.util.List;

import miar.com.miar_project_client.model.Spent;
import miar.com.miar_project_client.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface SpentApi
{
	SpentApi service = RequestService.service.create( SpentApi.class );

	@GET( "api/evenement/depenses/total/{idEvent}" )
	Call< Double > getTotal( @Path( "idEvent" ) String idEvent );

	@GET( "api/evenement/depenses/cout/{idEvent}" )
	Call< Double > getAverage( @Path( "idEvent" ) String idEvent );

	@GET( "api/evenement/depenses/{idEvent}" )
	Call< List< Spent > > getSpents( @Path( "idEvent" ) String idEvent );

	@GET( "api/evenement/depenses/{idEvent}/{pseudo}" )
	Call< List< Spent > > getSPentOfUser( @Path( "idEvent" ) String idEvent, @Path( "pseudo" ) String pseudo );

    @DELETE("api/evenement/depense/{idEvent}/{idSpent}")
    Call<Boolean> deleteSpent(@Path("idEvent") String idEvent, @Path("idSpent") String idSpent);

    @GET("api/evenement/depenses/solde/{idEvent}")
    Call<List<User>> getBalances(@Path("idEvent") String idEvent);

    @HTTP(method = "DELETE", path = "api/evenement/depense/{idEvent}", hasBody = true)
    Call<Boolean> deleteSpent(@Path("idEvent") String idEvent, @Body List<String> idSpents);

    @POST("/api/evenement/depense/{idEvent}/{pseudo}")
    Call<String> addSpentEvent(@Body Spent spent, @Path("idEvent") String idEvent, @Path("pseudo") String pseudo);
}
