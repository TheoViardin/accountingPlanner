package miar.com.miar_project_client.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import miar.com.miar_project_client.R;


public class InvitationsFragment extends Fragment
{
	public InvitationsFragment() { }

	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container,
	                          Bundle savedInstanceState )
	{
		return inflater.inflate( R.layout.fragment_invitations, container, false );
	}
}
