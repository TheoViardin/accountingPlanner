package miar.com.miar_project_client;

import android.app.Application;
import android.content.Context;

import miar.com.miar_project_client.api.RequestService;

public class MainApplication extends Application
{
	@Override
	public void onCreate() // Appelé avant le début de la première activité
	{
		super.onCreate();

		Context ctx = getApplicationContext();

		// Initialisation du service de requetes HTTP retrofit
		RequestService.initialize( ctx );

		// Vérifications d'état de login.
		ApplicationPreferences.checkLoggedStateAndRedirect( ctx );
	}
}
