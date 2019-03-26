package miar.com.miar_project_client.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import miar.com.miar_project_client.ApplicationPreferences;
import miar.com.miar_project_client.R;
import miar.com.miar_project_client.api.UserApi;
import miar.com.miar_project_client.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyUserActivity extends Activity {
    EditText oldpass;
    EditText newpass;
    EditText oldmail;
    EditText newmail;
    Button changeuser;
    Button back;
    User user = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_user);

        oldpass = (EditText) findViewById(R.id.OldPass);
        newpass = (EditText) findViewById(R.id.NewPass);
        oldmail = (EditText) findViewById(R.id.OldMail);
        newmail = (EditText) findViewById(R.id.NewMail);
        changeuser = (Button) findViewById(R.id.ModifyUser);
        back = (Button) findViewById(R.id.Annuler);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyUserActivity.super.onBackPressed();
            }
        });

        changeuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean value = true;
                while (value) {
                    if (oldpass.getText().toString().matches("")) {
                        Toast.makeText( getApplicationContext(), "Veuillez indiquer votre ancien mot de passe", Toast.LENGTH_SHORT ).show();
                        value = false;
                    } else if (newpass.getText().toString().matches("")) {
                        Toast.makeText( getApplicationContext(), "Veuillez indiquer votre nouveau mot de passe", Toast.LENGTH_SHORT ).show();
                        value = false;
                    } else if (oldmail.getText().toString().matches("")) {
                        Toast.makeText( getApplicationContext(), "Veuillez indiquer votre ancienne adresse mail", Toast.LENGTH_SHORT ).show();
                        value = false;
                    } else if (newmail.getText().toString().matches("")) {
                        Toast.makeText( getApplicationContext(), "Veuillez indiquer votre nouvelle adresse mail", Toast.LENGTH_SHORT ).show();
                        value = false;
                    } else {
                        String pseudo = ApplicationPreferences.getPseudoUser(view.getContext());
                        modifyUser(pseudo);
                        System.out.println("Utilisateur modifié avec succes.");
                        value = false;
                    }
                }
            }
        });
    }

    public void modifyUser(String pseudo) {
        String op = oldpass.getText().toString();
        String np = newpass.getText().toString();
        String om = oldmail.getText().toString();
        String nm = newmail.getText().toString();

        Call< String > response = UserApi.service.modifyUser(pseudo,op,np,om,nm);
        response.enqueue( new Callback< String >()
        {
            @Override
            public void onResponse( Call< String > call, Response< String > response )
            {
                System.out.println(response);
                boolean ok = (response.code() == 200);
                ApplicationPreferences.setLoggedState( getApplicationContext(), ok );
                Log.i( "ModifyUserActivity", "User Modified: " + ok );

                if( ok ) {
                    Toast.makeText( getApplicationContext(), "Utilisateur modifié avec succès", Toast.LENGTH_SHORT ).show();
                    ModifyUserActivity.super.onBackPressed();
                }
                else
                    Toast.makeText( getApplicationContext(), response.body(), Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onFailure( Call< String > call, Throwable t )
            {
                Log.e( "ModifyUserActivity", "Erreur "+t.getMessage() );
                Toast.makeText( getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG ).show();
            }
        } );
    }
}
