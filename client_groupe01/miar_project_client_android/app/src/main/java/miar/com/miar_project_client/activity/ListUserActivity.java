package miar.com.miar_project_client.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

import miar.com.miar_project_client.R;
import miar.com.miar_project_client.adapter.UserAdapter;
import miar.com.miar_project_client.api.EventApi;
import miar.com.miar_project_client.api.SpentApi;
import miar.com.miar_project_client.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListUserActivity extends AppCompatActivity
{
	private UserAdapter userAdapter;
	private ListView listView;
	private String ID_EVENT;
	private Button delete;
	private static int RELOAD = 10;
	private List< User > contributors = null;
	private Intent returnIntent;
	private SwipeRefreshLayout refresh;
	@Override
	protected void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		setContentView( R.layout.activity_list_user );
		Toolbar toolbar = findViewById( R.id.toolbar );
		setSupportActionBar( toolbar );
		Intent intent = getIntent();

		getSupportActionBar().setDisplayHomeAsUpEnabled( true );
		getSupportActionBar().setDisplayShowHomeEnabled( true );

		if( intent != null )
		{
			contributors = (List< User >) intent.getSerializableExtra( "listUser" );
			ID_EVENT = intent.getStringExtra( "idEvent" );

			userAdapter = new UserAdapter( getApplicationContext(), contributors, contributors.get( 0 ) );
			listView = findViewById( R.id.contributors );
			listView.setChoiceMode( ListView.CHOICE_MODE_MULTIPLE );
			listView.setAdapter( userAdapter );

			delete = findViewById( R.id.delete );

			listView.setOnItemClickListener( new AdapterView.OnItemClickListener()
			{
				@Override
				public void onItemClick( AdapterView< ? > parent, View view, int i, long id )
				{
					Intent intent = new Intent( view.getContext(), SpentDetails.class );
					intent.putExtra( "user", contributors.get( i ) );
					intent.putExtra( "idEvent", ID_EVENT );
					intent.putExtra( "position", i );
					startActivityForResult( intent, RELOAD );
				}
			} );
			refresh = findViewById(R.id.swipe_list_container);
			refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				@Override
				public void onRefresh() {
					getbalances();
				}
			});

			delete.setOnClickListener( new View.OnClickListener()
			{
				@Override
				public void onClick( View v )
				{
					if( listView.getCount() > 0 )
					{
						for( final Iterator< User > iterator = contributors.iterator(); iterator.hasNext(); )
						{
							User value = iterator.next();
							if( value.ischecked == true )
							{
								Call< Boolean > deleteContributors = EventApi.service.deleteContributors( ID_EVENT, value.pseudo );
								deleteContributors.enqueue( new Callback< Boolean >()
								{
									@Override
									public void onResponse( Call< Boolean > call, Response< Boolean > response )
									{
										getbalances();
										if( returnIntent == null )
										{
											returnIntent = new Intent();
											returnIntent.putExtra( "result", true );
											setResult( Activity.RESULT_OK, returnIntent );
										}
									}

									@Override
									public void onFailure( Call< Boolean > call, Throwable t )
									{
										Log.d( "deleteSpent", t.getMessage() );
									}
								} );
								iterator.remove();
								userAdapter.notifyDataSetChanged();
							}
						}

					}
				}
			} );
		}
	}

	@Override
	public boolean onSupportNavigateUp()
	{
		onBackPressed();
		return true;
	}

	@Override
	protected void onActivityResult( int requestCode, final int resultCode, @Nullable Intent data )
	{
		if( requestCode == RELOAD )
		{
			if( resultCode == Activity.RESULT_OK )
			{
				Boolean result = data.getBooleanExtra( "result", false );

				if( result )
				{
					getbalances();
					returnIntent = new Intent();
					returnIntent.putExtra( "result", true );
					setResult( Activity.RESULT_OK, returnIntent );
				}
			}
			if( resultCode == Activity.RESULT_CANCELED )
			{
				//Write your code if there's no result
			}
		}
	}

	public void getbalances() {
		Call<List<User>> getBalances = SpentApi.service.getBalances(ID_EVENT);
		getBalances.enqueue(new Callback<List<User>>() {
			@Override
			public void onResponse(Call<List<User>> call, Response<List<User>> response) {
				contributors = response.body();
				userAdapter = new UserAdapter(getApplicationContext(), contributors, contributors.get(0));
				listView = findViewById(R.id.contributors);
				listView.setAdapter(userAdapter);
				listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
						Intent intent = new Intent(view.getContext(), SpentDetails.class);
						intent.putExtra("user", contributors.get(i));
						intent.putExtra("idEvent", ID_EVENT);
						intent.putExtra("position", i);
						startActivityForResult(intent, RELOAD);
					}
				});
				refresh.setRefreshing(false);
			}


			@Override
			public void onFailure(Call<List<User>> call, Throwable t) {
				Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
				refresh.setRefreshing(false);
				Log.e("getBalances", t.getMessage());
			}
		});
	}
}
