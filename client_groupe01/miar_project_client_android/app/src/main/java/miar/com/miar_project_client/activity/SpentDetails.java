package miar.com.miar_project_client.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import miar.com.miar_project_client.R;
import miar.com.miar_project_client.adapter.SpentAdapter;
import miar.com.miar_project_client.api.SpentApi;
import miar.com.miar_project_client.api.UserApi;
import miar.com.miar_project_client.model.Spent;
import miar.com.miar_project_client.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpentDetails extends AppCompatActivity {

    private TextView username, solde, average, total;
    private SpentAdapter spentAdapter;
    private ListView listView;
    private User user;
    private String ID_EVENT;
    private Button delete;
    private Intent intent;
    private static int RELOAD = 10;
    private Intent returnIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spent_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        intent = getIntent();
        if (intent != null) {
            user = (User) intent.getSerializableExtra("user");

            ID_EVENT = intent.getStringExtra("idEvent");
            if (user != null) {
                username = findViewById(R.id.pseudo);
                username.setText(user.pseudo);


                solde = findViewById(R.id.solde);


                if (user.balance != 0.00) {
                    solde.setText("Solde : " + String.format("%.2f", user.balance) + " €");

                    if (user.balance > 0.00) {
                        solde.setTextColor(Color.parseColor("#019940"));

                    } else {
                        solde.setTextColor(Color.RED);
                    }

                }
                getSpents();
                getAverage();
                getTotal();
            }
        }
    }

    public void getSpents() {
        Call<List<Spent>> getSPentOfUser = SpentApi.service.getSPentOfUser(ID_EVENT, user.pseudo);
        getSPentOfUser.enqueue(new Callback<List<Spent>>() {
            @Override
            public void onResponse(Call<List<Spent>> call, Response<List<Spent>> response) {
                if (!response.body().isEmpty()) {
                    final List<Spent> spents = response.body();
                    listView = findViewById(R.id.spents);

                    spentAdapter = new SpentAdapter(getApplicationContext(), spents, true);
                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                    delete = findViewById(R.id.delete);
                    delete.setClickable(false);
                    delete.setVisibility(View.GONE);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            spents.get(position).isChecked = !spents.get(position).isChecked;
                            spentAdapter.notifyDataSetChanged();
                            listView.setItemChecked(position, spents.get(position).isChecked);

                            for (Spent s : spents) {
                                if (s.isChecked) {
                                    delete.setVisibility(View.VISIBLE);
                                    delete.setClickable(true);
                                    return;
                                }
                            }

                            delete.setClickable(false);
                            delete.setVisibility(View.GONE);
                        }
                    });
                    listView.setAdapter(spentAdapter);


                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SparseBooleanArray checked = listView.getCheckedItemPositions();
                            if (listView.getCount() > 0) {
                                final int[] index = {0};
                                Boolean test = false;
                                List<String> spentsId = new ArrayList<>();
                                for (Iterator<Spent> iterator = spents.iterator(); iterator.hasNext(); ) {
                                    Spent value = iterator.next();
                                    if (checked.get(index[0]) == true) {
                                        test = true;
                                        spentsId.add(value.spentId);
                                        iterator.remove();
                                        spentAdapter.notifyDataSetChanged();
                                        index[0]++;
                                    }
                                }
                                if (test) {
                                    Call<Boolean> deleteSpent = SpentApi.service.deleteSpent(ID_EVENT, spentsId);
                                    deleteSpent.enqueue(new Callback<Boolean>() {
                                        @Override
                                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                            if (returnIntent == null) {
                                                Intent returnIntent = new Intent();
                                                returnIntent.putExtra("result", true);
                                                setResult(Activity.RESULT_OK, returnIntent);
                                            }
                                            getAverage();
                                            getTotal();
                                        }

                                        @Override
                                        public void onFailure(Call<Boolean> call, Throwable t) {
                                            Log.e("deleteSpent", t.getMessage());
                                        }
                                    });
                                }
                                for (Spent s : spents) {
                                    s.isChecked = false;
                                    spentAdapter.notifyDataSetChanged();
                                }
                                delete.setClickable(false);
                                delete.setVisibility(View.GONE);
                            }
                        }
                    });

                } else {

                }
            }

            @Override
            public void onFailure(Call<List<Spent>> call, Throwable t) {
                Log.i("getSPentOfUser", t.getMessage());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void getAverage() {
        Call<Double> getAverageInEvent = UserApi.service.getAverageInEvent(ID_EVENT, user.pseudo);
        getAverageInEvent.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                average = findViewById(R.id.average);
                average.setText("Moyenne :" + String.format("%.2f", response.body()) + "€");
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                Log.e("getAverageInEvent", t.getMessage());
            }
        });
    }

    public void getTotal() {
        Call<Double> getTotalInEvent = UserApi.service.getTotalInEvent(ID_EVENT, user.pseudo);
        getTotalInEvent.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                total = findViewById(R.id.total);
                total.setText("Total :" + String.format("%.2f", response.body()) + "€");
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                Log.e("getAverageInEvent", t.getMessage());
            }
        });
    }

}
