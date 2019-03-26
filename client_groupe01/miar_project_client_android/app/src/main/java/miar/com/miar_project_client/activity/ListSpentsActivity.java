package miar.com.miar_project_client.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import miar.com.miar_project_client.R;
import miar.com.miar_project_client.adapter.SpentAdapter;
import miar.com.miar_project_client.model.Spent;

public class ListSpentsActivity extends AppCompatActivity {
    private SpentAdapter spentAdapter;
    private ListView listView;
    private TextView notSpent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_spents);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        notSpent = findViewById(R.id.notSpent);
        listView = findViewById(R.id.spents);

        if (intent != null) {
            final List<Spent> spents = (List<Spent>) intent.getSerializableExtra("Spents");
            if (!spents.isEmpty()) {
                spentAdapter = new SpentAdapter(getApplicationContext(), spents, false);
                listView.setAdapter(spentAdapter);
                notSpent.setVisibility(View.GONE);
            } else {
                listView.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
