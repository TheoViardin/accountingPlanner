package miar.com.miar_project_client.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import miar.com.miar_project_client.ApplicationPreferences;
import miar.com.miar_project_client.R;
import miar.com.miar_project_client.api.EventApi;
import miar.com.miar_project_client.model.Event;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener
{
	private Calendar calendar = Calendar.getInstance();
	private String lieu = "";

	private TextInputLayout title, description;
	private boolean titleValid, descValid;

	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_create_event );

		this.setSupportActionBar( (Toolbar) findViewById( R.id.toolbar2 ) );
		this.getSupportActionBar().setTitle( "Creation d'un événement" );
		this.getSupportActionBar().setDisplayHomeAsUpEnabled( true );

		title = this.findViewById( R.id.title );
		description = this.findViewById( R.id.description );

		this.bindCheckers();
	}

	private void bindCheckers()
	{
		title.getEditText().addTextChangedListener( new TextWatcher()
		{
			@Override
			public void beforeTextChanged( CharSequence s, int start, int count, int after ) { }

			@Override
			public void onTextChanged( CharSequence s, int start, int before, int count )
			{
				titleValid = s.toString().replaceAll( " ", "" ).length() > 3;

				if( !titleValid )
					title.setError( "Le titre doit faire 3 characteres non-blanc minimum." );
				else
					title.setError( null );
			}

			@Override
			public void afterTextChanged( Editable s ) { }
		} );

		description.getEditText().addTextChangedListener( new TextWatcher()
		{
			@Override
			public void beforeTextChanged( CharSequence s, int start, int count, int after ) { }

			@Override
			public void onTextChanged( CharSequence s, int start, int before, int count )
			{
				descValid = s.length() < 300;

				if( !descValid )
					description.setError( "La description doit faire 300 characteres maximum." );
				else
					description.setError( null );
			}

			@Override
			public void afterTextChanged( Editable s ) { }
		} );

	}

	@Override
	public boolean onOptionsItemSelected( MenuItem item )
	{
		switch( item.getItemId() )
		{
			case android.R.id.home:
				this.onBackPressed();
				return true;
		}

		return super.onOptionsItemSelected( item );
	}

	@Override
	public boolean onCreateOptionsMenu( Menu menu )
	{
		this.getMenuInflater().inflate( R.menu.toolbar_validate, menu );
		return super.onCreateOptionsMenu( menu );
	}

	public boolean onValidate( MenuItem item )
	{
		if( !this.validateElements() )
			return false;

		Event event = new Event();
		event.name = title.getEditText().getText().toString();
		event.description = description.getEditText().getText().toString();
		event.place = lieu;
		event.creatorPseudo = ApplicationPreferences.getPseudoUser( getApplicationContext() );
		event.supposedDate = new SimpleDateFormat( "yyyy-MM-dd" ).format( calendar.getTime() );

		EventApi.service.createEvent( event )
				.enqueue( new Callback< String >()
				{
					@Override
					public void onResponse( Call< String > call, Response< String > response )
					{
						boolean ok = response.code() == 200;

						Log.i( "CreateEventActivity", "event: " + response.code() );

						if( ok )
							openMainActivity();
						else
							Toast.makeText( getApplicationContext(), "Erreur lors de la création de l'événement", Toast.LENGTH_SHORT );
					}

					@Override
					public void onFailure( Call< String > call, Throwable t )
					{
						Log.e( "CreateEventActivity", t.getMessage() );
						Toast.makeText( getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG );
					}
				} );

		return true;
	}

	private boolean validateElements()
	{
		return titleValid && descValid && !lieu.equals( "" );
	}

	public void openMapPicker( View view )
	{
		AlertDialog.Builder builder = new AlertDialog.Builder( this );
		builder.setTitle( "Entrez un lieu" );

		final EditText input = new EditText( this );
		input.setInputType( InputType.TYPE_CLASS_TEXT );
		builder.setView( input );

		builder.setPositiveButton( "OK", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick( DialogInterface dialog, int which )
			{
				lieu = input.getText().toString();
			}
		} );

		builder.setNegativeButton( "Cancel", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick( DialogInterface dialog, int which )
			{
				dialog.cancel();
			}
		} );

		builder.show();
	}

	public void openDatePicker( View view )
	{
		calendar = Calendar.getInstance();

		int year = calendar.get( Calendar.YEAR );
		int month = calendar.get( Calendar.MONTH );
		int day = calendar.get( Calendar.DAY_OF_MONTH );

		DatePickerDialog picker = new DatePickerDialog( CreateEventActivity.this, this, year, month, day );
		picker.getDatePicker().setMinDate( calendar.getTimeInMillis() );
		picker.show();
	}

	@Override
	public void onDateSet( DatePicker view, int year, int month, int dayOfMonth )
	{
		calendar.set( Calendar.YEAR, year );
		calendar.set( Calendar.MONTH, month );
		calendar.set( Calendar.DAY_OF_MONTH, dayOfMonth );
	}

	private void openMainActivity()
	{
		Intent intent = new Intent( CreateEventActivity.this, MainActivity.class);
		startActivity( intent );
	}
}
