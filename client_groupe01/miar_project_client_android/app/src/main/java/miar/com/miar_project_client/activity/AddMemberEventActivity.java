package miar.com.miar_project_client.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import miar.com.miar_project_client.R;
import miar.com.miar_project_client.adapter.AllUserAdapter;
import miar.com.miar_project_client.adapter.MemberAdapter;
import miar.com.miar_project_client.api.EventApi;
import miar.com.miar_project_client.api.UserApi;
import miar.com.miar_project_client.model.EventForDetail;
import miar.com.miar_project_client.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMemberEventActivity extends Activity {

    private MemberAdapter adapter;
    private AllUserAdapter adapter2;
    ListView l;
    ListView l2;
    Button valider;
    Button retour;
    String idEvent;
    EditText recherche;
    List<User> listUsersInEvent = new ArrayList<User>();
    List<User> listAllUsers = new ArrayList<User>();
    List<String> listOthersUsers = new ArrayList<String>();
    List<String> listUserChecked = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_member);

        idEvent = getIntent().getExtras().getString("eventId");
        //idEvent = "5c767d7d40683f067433026e";
        getAllUsers();

        valider = findViewById(R.id.memberValid);
        retour = findViewById(R.id.memberRetour);

        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDetailActivity();
            }
        });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (String u : listUserChecked) {
                    addMemberEvent(idEvent, u);
                }
                openDetailActivity();
            }
        });
    }

    public void populate(List<String> data) {
        this.adapter.clear();
        this.adapter.addAll(data);
        this.adapter.notifyDataSetChanged();
    }

    public void populate2(List<String> data) {
        this.adapter2.clear();
        this.adapter2.addAll(data);
        this.adapter2.notifyDataSetChanged();
    }


    public void getAllUsers() {
        Call<ArrayList<User>> getAll = UserApi.service.getAllUsers();
        getAll.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                listAllUsers = response.body();
                System.out.println(response.body().get(0).pseudo + " " + response.body().get(1).pseudo);
                System.out.println(listAllUsers);
                Log.i("Add Member Event", "list all users: " + listAllUsers);
                getUsersInEvent();
            }
            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Log.e("Add Member Event", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG);
            }
        });
    }

    public void getUsersInEvent() {
        Call<EventForDetail> response = EventApi.service.getUsersInEvent(idEvent);
        response.enqueue(new Callback<EventForDetail>() {
            @Override
            public void onResponse(Call<EventForDetail> call, Response<EventForDetail> response) {
                EventForDetail e = response.body();
                listUsersInEvent = e.contributors;
                Log.i("Add Member Event", "list users in event: " + listUsersInEvent);

                for (User us : listAllUsers) {
                    boolean found = false;
                    for (User u : listUsersInEvent) {
                        if (us.pseudo.equals(u.pseudo)) {
                            found = true;
                            System.out.println("FIND TROUVEE" + us.pseudo);
                            break;
                        }
                    }
                    if (!found) {
                        listOthersUsers.add(us.pseudo);
                        System.out.println("OTHERS USERS : " + us.pseudo);
                    }
                }

                final ArrayList<String> listPseudoUsersInEvent = new ArrayList<String>();
                for (User u : listUsersInEvent){
                    System.out.println(u.pseudo);
                    listPseudoUsersInEvent.add(u.pseudo);
                }
                adapter = new MemberAdapter(getApplicationContext(), R.layout.user_item, listPseudoUsersInEvent);
                adapter2 = new AllUserAdapter(getApplicationContext(), R.layout.participant_item, listOthersUsers, listUserChecked);
                populate(listPseudoUsersInEvent);
                populate2(listOthersUsers);
                System.out.println("APRES ADAPTER ********");
                System.out.println("SYSOUT LIST PSEUDO USER IN EVENT ********");
                for(String s : listPseudoUsersInEvent){
                    System.out.println(s);
                }
                System.out.println("SYSOUT LIST OTHERS USERS*******");
                for(String v : listOthersUsers){
                    System.out.println(v);
                }

                l = findViewById(R.id.addMemberList);
                l.setTextFilterEnabled(true);
                l.setAdapter(adapter);
                l2 = findViewById(R.id.listUserAll);
                l2.setAdapter(adapter2);

                //recherche = (EditText) findViewById(R.id.memberRecherche);
/*
                recherche.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        AddMemberEvent.this.adapter.getFilter().filter(s);
                        populate(listPseudoUsersInEvent);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
               // recherche.addTextChangedListener(myFilter);
*/
            }

            @Override
            public void onFailure(Call<EventForDetail> call, Throwable t) {
                Log.e("Add Member Event", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG);
            }
        });
    }

    private void openDetailActivity() {
        Intent intent = new Intent(AddMemberEventActivity.this, DetailEventActivity.class);
        intent.putExtra( "eventId", idEvent );
        startActivity(intent);
    }

    public void addMemberEvent(String id, String pseudo) {
        Call<String> addMember = EventApi.service.addMemberEvent(idEvent, pseudo);
        addMember.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                boolean ok = (response.code() == 200);
                Log.i("Add Member event", "Member add : " + ok);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("Add Member failed ", t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG);
            }
        });
    }
}

