package miar.com.miar_project_client.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import miar.com.miar_project_client.ApplicationPreferences;
import miar.com.miar_project_client.R;
import miar.com.miar_project_client.activity.CreateEventActivity;
import miar.com.miar_project_client.activity.LoginActivity;
import miar.com.miar_project_client.activity.ModifyUserActivity;
import miar.com.miar_project_client.api.UserApi;
import miar.com.miar_project_client.fragments.AccountFragment;
import miar.com.miar_project_client.fragments.EvenementsFragment;
import miar.com.miar_project_client.fragments.InvitationsFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


// Activité regrouppant les pages suivantes: invitations, événements, compte
public class MainActivity extends AppCompatActivity
{
	private TabLayout tabLayout;
	private ViewPager viewPager;

	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );

		setContentView(R.layout.main_activity );

		viewPager = findViewById( R.id.viewpager );
		setupViewPager( viewPager );

		tabLayout = findViewById( R.id.tabs );
		tabLayout.setupWithViewPager( viewPager );

		setupTabIcons();
		tabLayout.getTabAt( 1 ).select();
	}

	private void setupTabIcons()
	{
		tabLayout.getTabAt( 0 ).setIcon( R.drawable.ic_notifications_black_24dp );
		tabLayout.getTabAt( 1 ).setIcon( R.drawable.ic_local_activity_black_24dp );
		tabLayout.getTabAt( 2 ).setIcon( R.drawable.ic_account_circle_black_24dp );
	}

	private void setupViewPager( ViewPager viewPager )
	{
		ViewPagerAdapter adapter = new ViewPagerAdapter( getSupportFragmentManager() );

		adapter.addFragment( new InvitationsFragment(), "Invitations" );
		adapter.addFragment( new EvenementsFragment(),"Événements" );
		adapter.addFragment( new AccountFragment(), "Compte" );
		viewPager.setAdapter( adapter );
	}

	public void logout( View view )
	{
		ApplicationPreferences.setLoggedState(view.getContext(), false);

		Intent intent = new Intent( view.getContext(), LoginActivity.class);
		startActivity( intent );
	}
	public void informations (View view){
		Intent intent = new Intent( view.getContext(), ModifyUserActivity.class );
		startActivity( intent );
	}

	public void confidentialite (View view){
			String url = "https://policies.google.com/privacy?hl=fr";
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			startActivity(i);
	}

	public void condition (View view){
		String url = "https://policies.google.com/terms?hl=fr";
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}

	public void supprimer(View view){
	    final View v = view;
        final String pseudo = ApplicationPreferences.getPseudoUser(view.getContext());
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
		alertDialogBuilder.setTitle("Attention");
		alertDialogBuilder
				.setMessage("Cette action est définitive! Voulez vous vraiment supprimer votre compte ?")
				.setCancelable(false)
				.setPositiveButton("Oui",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
                        Call< Boolean > response = UserApi.service.removeUser(pseudo);

                        response.enqueue( new Callback< Boolean >()
                        {
                            @Override
                            public void onResponse( Call< Boolean > call, Response< Boolean > response )
                            {
                                boolean ok = (response.code()==200);
                                ApplicationPreferences.setLoggedState( v.getContext(), false );
                                Log.i( "MainActivity", "delete: " + ok );

                                if( ok ) {
                                    Toast.makeText(getApplicationContext(), "Compte supprimé", Toast.LENGTH_SHORT).show();
                                    MainActivity.this.finish();
                                }
                                else
                                    Toast.makeText( getApplicationContext(), "Erreur lors de la suppression: " + pseudo+ + response.code(), Toast.LENGTH_SHORT ).show();
                            }

                            @Override
                            public void onFailure( Call< Boolean > call, Throwable t )
                            {
                                Log.e( "MainActivity","Erreur "+t.getMessage() );
                                Toast.makeText( getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG ).show();
                            }
                        } );
					}
				})
				.setNegativeButton("Non",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	// Ce ViewPagerAdapter gère les tabs du menu principal
	class ViewPagerAdapter extends FragmentPagerAdapter
	{
		private final List< Fragment > mFragmentList = new ArrayList<>();
		private final List< String > mFragmentTitleList = new ArrayList<>();

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

		public void addFragment( Fragment fragment, String title )
		{
			mFragmentList.add( fragment );
			mFragmentTitleList.add( title );
		}

		@Override
		public CharSequence getPageTitle( int position )
		{
			return mFragmentTitleList.get( position );
		}
	}

	public void openCreateEvent(View view){
		Intent intent = new Intent( this, CreateEventActivity.class);
		startActivity( intent );
	}

}