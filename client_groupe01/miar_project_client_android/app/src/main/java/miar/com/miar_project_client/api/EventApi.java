package miar.com.miar_project_client.api;

import java.util.List;

import miar.com.miar_project_client.model.Event;
import miar.com.miar_project_client.model.EventForDetail;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EventApi
{
	EventApi service = RequestService.service.create( EventApi.class );

	@POST( "/api/evenement" )
	Call< String > createEvent( @Body Event event );

	@GET( "/api/evenements" )
	Call< List< Event > > getAllEvents();

	@GET( "/api/evenements/{pseudoCreateur}" )
	Call< List< Event > > getEventsOfUser( @Path( "pseudoCreateur" ) String name );

	@GET( "/api/evenement/{idEvent}" )
	Call< EventForDetail > getUsersInEvent( @Path( "idEvent" ) String idEvent );

	@POST( "/api/evenement/membre/{idEvent}/{contributorPseudo}" )
	Call< String > addMemberEvent( @Path( "idEvent" ) String idEvent, @Path( "contributorPseudo" ) String pseudo );

	@DELETE( "api/evenement/membre/{idEvent}/{pseudoParticipant}" )
	Call< Boolean > deleteContributors( @Path( "idEvent" ) String idEvent, @Path( "pseudoParticipant" ) String pseudoParticipant );

}
