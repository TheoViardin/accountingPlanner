package miar.com.miar_project_client.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import miar.com.miar_project_client.R;
import miar.com.miar_project_client.api.EventApi;
import miar.com.miar_project_client.api.SpentApi;
import miar.com.miar_project_client.model.Event;
import miar.com.miar_project_client.model.EventForDetail;
import miar.com.miar_project_client.model.Spent;
import miar.com.miar_project_client.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDepenseActivity extends Activity {

	Spinner categ;
	Spinner participant;
	List<User> lesParticipants = new ArrayList<User>();
	Button valider;
	EditText montant;
	String idEvent;
	EditText intitule;
	Boolean verif = true;
	TextView msg;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_depense);

		idEvent = getIntent().getExtras().getString("eventId");
		//idEvent = "5c767d7d40683f067433026e";

		getUsersInEvent();
		System.out.println("LIST PARTICIPANT" + lesParticipants);

		categ = (Spinner) findViewById(R.id.depenseCategories);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
				R.array.categories, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categ.setAdapter(adapter);

		intitule = (EditText) findViewById(R.id.depenseIntitule);
		montant = (EditText) findViewById(R.id.depenseMontant);
		valider = (Button) findViewById(R.id.depenseEntrer);
		msg = (TextView) findViewById(R.id.depenseMsg);

		valider.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				while(verif){
					if(intitule.getText().toString().matches("")){
						msg.setText("Veuillez saisir un titre !");
						verif = false;
					}
					if(montant.getText().toString().matches("")){
						msg.setText("Veuillez saisir un montant ! ");
						verif = false;
					} else {
						addSpentEvent();
						verif = false;
					}
				}
			}
		});
	}

	public void getUsersInEvent() {
		Call<EventForDetail> lesUsers = EventApi.service.getUsersInEvent(idEvent);
		lesUsers.enqueue(new Callback<EventForDetail>() {
			@Override
			public void onResponse(Call<EventForDetail> call, Response<EventForDetail> response) {
				//lesParticipants = response.body();
				EventForDetail e = response.body();
				lesParticipants = e.contributors;

				participant = (Spinner) findViewById(R.id.depenseParticipant);
				String[] pseudos = new String[lesParticipants.size()];

				for (int i = 0; i < lesParticipants.size(); i++) {
					pseudos[i] = lesParticipants.get(i).pseudo;
					System.out.println("DANS LE FOR : " + lesParticipants.get(i).pseudo );
				}

				ArrayAdapter<CharSequence> a = new ArrayAdapter<CharSequence>(AddDepenseActivity.this, android.R.layout.simple_spinner_item, pseudos);
				a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				participant.setAdapter(a);
			}

			@Override
			public void onFailure(Call<EventForDetail> call, Throwable t) {
				Log.e("Add Member Event", t.getMessage());
				Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG);
			}
		});
	}

	public void addSpentEvent() {

		String categorie = categ.getSelectedItem().toString();
		String auteur = participant.getSelectedItem().toString();
		Spent s = new Spent();
		s.amount = Double.parseDouble(montant.getText().toString());
		s.label = intitule.getText().toString();
		s.creatorPseudo = auteur;
		Call<String> sendSpent = SpentApi.service.addSpentEvent(s, idEvent, auteur);
		sendSpent.enqueue(new Callback<String>() {
			@Override
			public void onResponse(Call<String> call, Response<String> response) {
				boolean ok = (response.code() == 200);
				System.out.println(response.code());
				Log.i("AddDepense", "spent: " + ok);
				if (ok)
					openDetailActivity();
				else
					Toast.makeText(getApplicationContext(), "Erreur lors de la connexion", Toast.LENGTH_SHORT);
			}

			@Override
			public void onFailure(Call<String> call, Throwable t) {
				Log.e("Add Spent failed ", t.getMessage());
				Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG);
			}
		});
	}

	private void openDetailActivity() {
		Intent intent = new Intent(AddDepenseActivity.this, DetailEventActivity.class);
		intent.putExtra( "eventId", idEvent );
		startActivity(intent);
	}

}
