package miar.com.miar_project_client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import miar.com.miar_project_client.activity.LoginActivity;


public class ApplicationPreferences
{
	public static String LOGGED_IN_PREF = "ApplicationPreferences.loggedState";
	public static String LOGGED_IN_PSEUDO;

	static SharedPreferences getPreferences( Context context )
	{
		return PreferenceManager.getDefaultSharedPreferences( context );
	}

	/**
	 * Set the Login Status
	 *
	 * @param context
	 * @param loggedIn
	 */
	public static void setLoggedState( Context context, boolean loggedIn )
	{
		SharedPreferences.Editor editor = getPreferences( context ).edit();
		editor.putBoolean( LOGGED_IN_PREF, loggedIn );
		editor.apply();
	}

	/**
	 * Get the Login Status
	 *
	 * @param context
	 * @return boolean: fragment_login status
	 */
	public static boolean getLoggedState( Context context )
	{
		return getPreferences( context ).getBoolean( LOGGED_IN_PREF, false );
	}
	
	public static void checkLoggedStateAndRedirect( Context context )
	{
		// Si on est pas logged in, on affiche la page de login
		if( !ApplicationPreferences.getLoggedState( context)) {
			Intent intent = new Intent(context, LoginActivity.class);
			intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
			context.startActivity( intent);
		}
	}

	public static void setPseudoUser(Context context, String pseudo) {
		if (ApplicationPreferences.getLoggedState(context))
		{
			SharedPreferences.Editor editor = getPreferences( context ).edit();
			editor.putString( LOGGED_IN_PSEUDO, pseudo );
			editor.apply();
		}
	}

	public static String getPseudoUser( Context context )
	{
		return getPreferences( context ).getString( LOGGED_IN_PSEUDO, "" );
	}

}
