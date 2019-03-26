package miar.com.miar_project_client.api;

import android.content.Context;
import android.util.Log;

import java.util.Properties;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RequestService
{
	public static Retrofit service;

	private static String FILE = "application.properties";

	//TODO connexion emulateur
	private static String URL_EMULATEUR = "http://10.0.2.2";
	//TODO connexion portable
	private static String URL_FILAIRE = "http://192.168.42.54";

	public static void initialize( Context context )
	{
		if( service != null )
			return;

		Properties properties = new Properties();

		try
		{
			properties.load( context.getAssets().open( FILE ) );

			Log.i("RequestService", properties.getProperty("server.address", "http://192.168.42.171:8080"));

			service = new Retrofit.Builder()
					.baseUrl( properties.getProperty( "server.address", URL_EMULATEUR + ":8080" ) )
					.addConverterFactory(ScalarsConverterFactory.create())
					.addConverterFactory(GsonConverterFactory.create())
					.build();

		} catch( Exception e )
		{
			Log.e( "RequestService", e.toString() );
		}
	}
}
