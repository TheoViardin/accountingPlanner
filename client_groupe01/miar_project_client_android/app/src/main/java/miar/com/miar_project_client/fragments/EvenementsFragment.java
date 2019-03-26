package miar.com.miar_project_client.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import miar.com.miar_project_client.ApplicationPreferences;
import miar.com.miar_project_client.R;
import miar.com.miar_project_client.activity.DetailEventActivity;
import miar.com.miar_project_client.api.EventApi;
import miar.com.miar_project_client.model.Event;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EvenementsFragment extends Fragment
{
	private View view;
	private RecyclerView list;
	private SwipeRefreshLayout listRefresh;

	private EvenementsAdapter adapter = new EvenementsAdapter();

	public EvenementsFragment() { }

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		view = inflater.inflate( R.layout.fragment_evenements, container, false );

		list = view.findViewById( R.id.EvenementsRecycler );
		list.setHasFixedSize( true );
		list.setLayoutManager( new LinearLayoutManager( view.getContext() ) );
		list.setAdapter( adapter );

		listRefresh = view.findViewById( R.id.listRefresh );

		listRefresh.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener()
		{
			@Override
			public void onRefresh()
			{
				refreshList();
			}
		} );

		refreshList();

		return view;
	}

	public void refreshList()
	{
		adapter.clear();
		adapter.notifyDataSetChanged();

		EventApi.service.getEventsOfUser( ApplicationPreferences.getPseudoUser( getContext() ) )
				.enqueue( new Callback< List< Event > >()
				{
					@Override
					public void onResponse( Call< List< Event > > call, Response< List< Event > > response )
					{
						List< Event > events = response.body();

						if( response.code() == HttpURLConnection.HTTP_OK )
							adapter.mDataset = events;
						else
							Toast.makeText( getContext(), "" + response.code(), Toast.LENGTH_SHORT ).show();

						listRefresh.setRefreshing( false );
						Log.i( "EvenementsFragment", ( events != null ? "" + events.size() : "no events" ) );
						view.findViewById( R.id.noEventsText ).setVisibility( ( events == null || events.size() == 0 ) ? View.VISIBLE : View.INVISIBLE );
					}

					@Override
					public void onFailure( Call< List< Event > > call, Throwable t )
					{
						Toast.makeText( getContext(), "Erreur lors du chargement des données", Toast.LENGTH_LONG ).show();
						Log.e( "EvenementsFragment", t.getMessage() );
						t.printStackTrace();

						listRefresh.setRefreshing( false );
					}
				} );

		adapter.notifyDataSetChanged();
	}

	public class EvenementsAdapter extends RecyclerView.Adapter< EvenementsAdapter.ViewHolder >
	{
		private List< Event > mDataset = new ArrayList<>();

		public EvenementsAdapter() { }

		public EvenementsAdapter( ArrayList< Event > mDataset )
		{
			this.mDataset = mDataset;
		}

		@NonNull
		@Override
		public EvenementsAdapter.ViewHolder onCreateViewHolder( @NonNull ViewGroup parent, int i )
		{
			Button v = (Button) LayoutInflater.from( parent.getContext() )
					.inflate( R.layout.event_list_button, parent, false );

			return new ViewHolder( v );
		}

		@Override
		public void onBindViewHolder( @NonNull ViewHolder view, int i )
		{
			view.title.setText( mDataset.get( i ).name );
			final int pos = i;

			view.title.setOnClickListener( new View.OnClickListener()
			{
				@Override
				public void onClick( View v )
				{
					Intent intent = new Intent( getContext(), DetailEventActivity.class );
					intent.putExtra( "eventId", mDataset.get( pos ).id );
					startActivity( intent );
				}

			} );
		}

		public void clear()
		{
			this.mDataset.clear();
			this.notifyDataSetChanged();
		}

		@Override
		public int getItemCount()
		{
			return this.mDataset.size();
		}

		public class ViewHolder extends RecyclerView.ViewHolder
		{
			private Button title;

			public ViewHolder( @NonNull Button view )
			{
				super( view );
				title = view.findViewById( R.id.eventListTitle );
			}
		}

	}
}
