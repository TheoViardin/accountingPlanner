package miar.com.miar_project_client.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import miar.com.miar_project_client.R;

public class LoginFragment extends Fragment
{
	public EditText loginInput;
	public EditText passwordInput;

	public LoginFragment() { }

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
	{
		View view = inflater.inflate( R.layout.fragment_login, container, false );

		loginInput = view.findViewById( R.id.login );
		passwordInput = view.findViewById( R.id.password );

		return view;
	}
}