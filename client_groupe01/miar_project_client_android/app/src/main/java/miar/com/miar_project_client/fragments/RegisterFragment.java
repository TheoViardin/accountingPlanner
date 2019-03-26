package miar.com.miar_project_client.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import miar.com.miar_project_client.R;

public class RegisterFragment extends Fragment
{
	public EditText login;
	public EditText password;
	public EditText email;

	public RegisterFragment() { }

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate( R.layout.fragment_register, container, false );

		login = view.findViewById( R.id.login );
		password = view.findViewById( R.id.password );
		email = view.findViewById( R.id.email );

		return view;
	}
}
