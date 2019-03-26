package miar.com.miar_project_client.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import miar.com.miar_project_client.ApplicationPreferences;
import miar.com.miar_project_client.R;


public class AccountFragment extends Fragment
{
	public AccountFragment()
	{
	}


	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate( R.layout.fragment_account, container, false );

		TextView username = view.findViewById( R.id.userName );
		username.setText( ApplicationPreferences.getPseudoUser( getContext() ) );

		return view;
	}
}

