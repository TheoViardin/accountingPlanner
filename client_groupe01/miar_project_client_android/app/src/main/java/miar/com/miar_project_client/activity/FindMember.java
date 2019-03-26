package miar.com.miar_project_client.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import miar.com.miar_project_client.R;
import miar.com.miar_project_client.adapter.MemberAdapter;
import miar.com.miar_project_client.api.UserApi;
import miar.com.miar_project_client.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindMember extends Activity
{
	private MemberAdapter adapter;
	private Button valider;
	ArrayList< User > m;
	EditText name;
	TextView pseudo;
	TextView mail;
	Button entrer;
	Button retour;
	Boolean value = false;


	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.member_find );

		valider = findViewById( R.id.memberListChoisir );
		entrer = findViewById( R.id.memberListEntrer );
		retour = findViewById( R.id.memberListRetour );

		name = findViewById( R.id.memberListName );
		pseudo = findViewById( R.id.memberListPseudo );
		mail = findViewById( R.id.memberListEmail );

		entrer.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View view )
			{
				getUserByName();
			}
		} );

		valider.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View view )
			{
				if( value )
				{
					Intent response = new Intent( FindMember.this, AddMemberEventActivity.class );
					response.putExtra( "pseudo", pseudo.getText().toString() );
					response.putExtra( "mail", mail.getText().toString() );
					setResult( FindMember.RESULT_OK, response );
					finish();
				}
				else
				{
					Toast.makeText( getApplicationContext(), "************************** value FALSE *******************************", Toast.LENGTH_LONG );
				}

			}
		} );

	}

	public void getUserByName()
	{
		String user = name.getText().toString();

		Call< User > response = UserApi.service.getUserByName( user );

		response.enqueue( new Callback< User >()
		{
			@Override
			public void onResponse( Call< User > call, Response< User > response )
			{
				User ok = response.body();
				Log.i( "FindMember", "user : " + ok );
				pseudo.setText( ok.pseudo.toString() );
				mail.setText( ok.email.toString() );
				value = true;
			}

			@Override
			public void onFailure( Call< User > call, Throwable t )
			{
				Log.e( "FindMember", t.getMessage() );
				Toast.makeText( getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG );
			}
		} );
	}

}