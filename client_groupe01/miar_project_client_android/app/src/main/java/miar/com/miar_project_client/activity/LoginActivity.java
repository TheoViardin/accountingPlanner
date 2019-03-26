package miar.com.miar_project_client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import miar.com.miar_project_client.ApplicationPreferences;
import miar.com.miar_project_client.R;
import miar.com.miar_project_client.api.UserApi;
import miar.com.miar_project_client.fragments.LoginFragment;
import miar.com.miar_project_client.fragments.RegisterFragment;
import miar.com.miar_project_client.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// Login Page
public class LoginActivity extends AppCompatActivity
{
	private ViewPager pager;

	private LoginFragment login;
	private RegisterFragment register;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.login_activity );

		pager = findViewById( R.id.viewpager );
		setupViewPager( pager );


	}

	// Ici on ajoute les fragments login et register et on set l'adapter
	private void setupViewPager( ViewPager pager )
	{
		ViewPagerAdapter adapter = new LoginActivity.ViewPagerAdapter( getSupportFragmentManager() );

		login = new LoginFragment();
		register = new RegisterFragment();

		adapter.addFragment( login );
		adapter.addFragment( register );

		pager.setAdapter( adapter );
	}

	public void openLogin( View view )
	{
		pager.setCurrentItem( 0 );
	}

	public void openRegister( View view )
	{
		pager.setCurrentItem( 1 );
	}

	private void openMainActivity()
	{
		Intent intent = new Intent( this, MainActivity.class);
		startActivity( intent );
	}

	@Override
	public void onBackPressed()
	{
		if( pager.getCurrentItem() != 0 ) // Si on est sur la page d'enregistrement, on override pour éviter de quitter l'app
			pager.setCurrentItem( 0 );
	}

	@Override
	public boolean onKeyUp( int keyCode, KeyEvent event )
	{
		if( keyCode == KeyEvent.KEYCODE_ENTER ) // Quand on appui sur entrer sur le dernier input d'un fragment. A noter: ce keycode n'est appelé que sur le dernier entry du layout/fragment
		{
			if( pager.getCurrentItem() == 0 )
				login( login.getView() );
			else
				register( register.getView() );

			return true;
		}

		return super.onKeyUp( keyCode, event );
	}

	public void login( View view )
	{
		User user = new User();
		user.pseudo = login.loginInput.getText().toString();
		user.mdp = login.passwordInput.getText().toString();

		final String pseudo = user.pseudo;

		Call< Boolean > response = UserApi.service.login( user );

		response.enqueue( new Callback< Boolean >()
		{
			@Override
			public void onResponse( Call< Boolean > call, Response< Boolean > response )
			{
				boolean ok = ( response.code() == 200 );
				ApplicationPreferences.setLoggedState( getApplicationContext(), ok );
				ApplicationPreferences.setPseudoUser( getApplicationContext(), pseudo );
				Log.i( "LoginActivity", "login: " + response.code() );

				if( ok )
					openMainActivity();
				else
					Toast.makeText( getApplicationContext(), "Erreur lors de la connexion", Toast.LENGTH_SHORT ).show();
			}

			@Override
			public void onFailure( Call< Boolean > call, Throwable t )
			{
				Log.e( "LoginActivity", t.getMessage() );
				Toast.makeText( getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG ).show();
			}
		} );
	}

	public void register( View view )
	{
		final User user = new User();

		user.pseudo = register.login.getText().toString();
		user.mdp = register.password.getText().toString();
		user.email = register.email.getText().toString();

		Call< String > response = UserApi.service.register( user );

		response.enqueue( new Callback< String >()
		{
			@Override
			public void onResponse( Call< String > call, Response< String > response )
			{
				boolean ok = ( response.code() == 200 );

				ApplicationPreferences.setLoggedState( getApplicationContext(), ok );
				ApplicationPreferences.setPseudoUser( getApplicationContext(), user.pseudo );
				Log.i( "LoginActivity", "register: " + response.code() );

				if( ok )
					openMainActivity();
				else
					Toast.makeText( getApplicationContext(), response.body(), Toast.LENGTH_SHORT ).show();
			}

			@Override
			public void onFailure( Call< String > call, Throwable t )
			{
				Log.e( "LoginActivity", t.getMessage() );
				Toast.makeText( getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG ).show();
			}
		} );
	}

	class ViewPagerAdapter extends FragmentPagerAdapter
	{
		private final List< Fragment > mFragmentList = new ArrayList<>();

		public ViewPagerAdapter( FragmentManager manager )
		{
			super( manager );
		}

		@Override
		public Fragment getItem( int position )
		{
			return mFragmentList.get( position );
		}

		@Override
		public int getCount()
		{
			return mFragmentList.size();
		}

		public void addFragment( Fragment fragment )
		{
			mFragmentList.add( fragment );
		}
	}
}
