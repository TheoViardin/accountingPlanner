package miar.com.miar_project_client.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.DateFormat;

import miar.com.miar_project_client.R;
import miar.com.miar_project_client.api.EventApi;
import miar.com.miar_project_client.api.SpentApi;
import miar.com.miar_project_client.model.Event;
import miar.com.miar_project_client.model.EventForDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailEventActivity extends AppCompatActivity
{
	private Event event = null;
	private TextView title, description, date, lieu, total, moyenne, creator;
	private Button contributors, spents, addMember, addSpent;
	private int RELOAD = 10;
	String eventIdMOCK;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_detail_event );
		Toolbar toolbar = findViewById( R.id.toolbar );
		setSupportActionBar( toolbar );

		getSupportActionBar().setDisplayHomeAsUpEnabled( true );
		getSupportActionBar().setDisplayShowHomeEnabled( true );

		FloatingActionButton fab = findViewById( R.id.fab );
		fab.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View view )
			{
				Snackbar.make( view, "Replace with your own action", Snackbar.LENGTH_LONG )
						.setAction( "Action", null ).show();
			}
		} );

		Intent i = getIntent();
		if( i != null )
		{
			eventIdMOCK = i.getStringExtra( "eventId" );
			//TODO chargement de l'évènement
			getEvent( eventIdMOCK );
		}


	}

	//TODO evenement recupérée
	public void getEvent( final String eventIdMOCK )
	{
		Call< EventForDetail > getEvent = EventApi.service.getUsersInEvent( eventIdMOCK );
		getEvent.enqueue( new Callback< EventForDetail >()
		{
			@Override
			public void onResponse( Call< EventForDetail > call, Response< EventForDetail > response )
			{
				final EventForDetail event = response.body();
				//TODO set title
				title = findViewById( R.id.title );
				title.setText( event.name );
				//TODO set createur pseudo
				creator = findViewById( R.id.creator );
				creator.setText( "organisé par " + event.creator.pseudo );
				//TODO set description
				description = findViewById( R.id.description );
				description.setText( event.description );
				if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O )
				{
					description.setJustificationMode( Layout.JUSTIFICATION_MODE_INTER_WORD );
				}
				//TODO set date
				date = findViewById( R.id.date );
				DateFormat dateFormat = android.text.format.DateFormat.getDateFormat( getApplicationContext() );
				date.setText( dateFormat.format( event.supposedDate ) );
				//TODO lieu
				lieu = findViewById( R.id.lieu );
				lieu.setText( event.place );


				contributors = findViewById( R.id.contributors );
				contributors.setText( event.contributors.size() + " participants" );
				contributors.setOnClickListener( new View.OnClickListener()
				{
					@Override
					public void onClick( View v )
					{
						Intent intent = new Intent( v.getContext(), ListUserActivity.class );
						intent.putExtra( "listUser", (Serializable) event.contributors );
						intent.putExtra( "idEvent", event.id );
						startActivityForResult( intent, RELOAD );
					}
				} );

				spents = findViewById( R.id.spents );
				spents.setText( event.spents.size() + " dépenses" );

				spents.setOnClickListener( new View.OnClickListener()
				{
					@Override
					public void onClick( View v )
					{
						Intent intent = new Intent( v.getContext(), ListSpentsActivity.class );
						intent.putExtra( "Spents", (Serializable) event.spents );
						startActivity( intent );
					}
				} );

				addMember = findViewById( R.id.addMember );
				addMember.setOnClickListener( new View.OnClickListener()
				{
					@Override
					public void onClick( View v )
					{
						Intent intent = new Intent( v.getContext(), AddMemberEventActivity.class );
						intent.putExtra( "eventId", eventIdMOCK );
						startActivity( intent );
					}
				} );

				addSpent = findViewById( R.id.addSpent );
				addSpent.setOnClickListener( new View.OnClickListener()
				{
					@Override
					public void onClick( View v )
					{
						Intent intent = new Intent( v.getContext(), AddDepenseActivity.class );
						intent.putExtra( "eventId", eventIdMOCK );
						startActivity( intent );
					}
				} );
			}

			@Override
			public void onFailure( Call< EventForDetail > call, Throwable t )
			{
				Toast.makeText( getApplicationContext(), "erreur " + t.getMessage(), Toast.LENGTH_LONG ).show();
				Log.e( "error api get event", t.getMessage() );
			}
		} );

		//TODO Récuperation du total
		Call< Double > getTotal = SpentApi.service.getTotal( eventIdMOCK );
		getTotal.enqueue( new Callback< Double >()
		{
			@Override
			public void onResponse( Call< Double > call, Response< Double > response )
			{
				total = findViewById( R.id.total );
				total.setText( "Total : " + String.format( "%.2f", response.body() ) + "€" );
			}

			@Override
			public void onFailure( Call< Double > call, Throwable t )
			{
				Log.e( "error ", t.getMessage() );
			}
		} );

		//TODO Récuperation de la moyenne
		Call< Double > getAverage = SpentApi.service.getAverage( eventIdMOCK );
		getAverage.enqueue( new Callback< Double >()
		{
			@Override
			public void onResponse( Call< Double > call, Response< Double > response )
			{
				moyenne = findViewById( R.id.moyenne );
				moyenne.setText( "Moyenne : " + String.format( "%.2f", response.body() ) + "€" );
			}

			@Override
			public void onFailure( Call< Double > call, Throwable t )
			{
				Log.e( "error ", t.getMessage() );
			}
		} );
	}


	@Override
	protected void onActivityResult( int requestCode, int resultCode, @Nullable Intent data )
	{
		if( requestCode == RELOAD )
		{
			if( resultCode == Activity.RESULT_OK )
			{
				Boolean result = data.getBooleanExtra( "result", false );
				if( result )
				{
					getEvent( eventIdMOCK );
				}
			}
			if( resultCode == Activity.RESULT_CANCELED )
			{
				//Write your code if there's no result
			}
		}
	}

}
