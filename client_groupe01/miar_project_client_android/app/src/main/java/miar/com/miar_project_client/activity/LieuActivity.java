package miar.com.miar_project_client.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import miar.com.miar_project_client.R;

import static android.support.constraint.Constraints.TAG;

public class LieuActivity extends Activity
{
	EditText ville;
	EditText adresse;
	EditText cp;
	Button entrer;
	ArrayList< String > lieu = new ArrayList< String >();
	int v;
	Boolean verif = true;

	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.lieu_activity );

		adresse = (EditText) findViewById( R.id.lieuAdresse );
		ville = (EditText) findViewById( R.id.lieuVille );
		cp = (EditText) findViewById( R.id.lieuCP );
		entrer = (Button) findViewById( R.id.lieuEntrer );

		v = getIntent().getExtras().getInt( "recup" );
		if( v == 1 )
		{
			lieu = getIntent().getStringArrayListExtra( "lieu" );
			adresse.setText( lieu.get( 0 ) );
			ville.setText( lieu.get( 1 ) );
			cp.setText( lieu.get( 2 ) );
		}

		entrer.setOnClickListener( new View.OnClickListener()
		{
			@Override
			public void onClick( View view )
			{
				if( v == 1 )
				{
					lieu.removeAll( lieu );
				}
                /*Integer test = Integer.parseInt(cp.getText().toString());
                while (verif) {
                    if (!(test instanceof Integer) && !(String.valueOf(test).length() == 5)) {
                        verif = false;
                    }
                }
                if (!(adresse.getText().toString().length() <= 25) || !(ville.getText().toString().length() <= 15)) {
                    verif = false;
                }*/
				// if (verif == true) {
				lieu.add( adresse.getText().toString() );
				lieu.add( ville.getText().toString() );
				lieu.add( cp.getText().toString() );
				Intent response = new Intent( LieuActivity.this, CreateEventActivity.class );
				response.putExtra( "lieu", lieu );
				Log.i( TAG, "PUT EXTRA " + lieu );
				setResult( LieuActivity.RESULT_OK, response );
				finish();
				//}
			}
		} );

	}
}
