package com.politecnico.masterchef_pmdm_albajonathan;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.politecnico.materchef_pmdm_albajonathan.R;
import java.util.ArrayList;

public class VotacionesActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> equipos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_votaciones);
        getSupportActionBar().hide();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        CustomAdapterVotaciones CustomAdapterVotaciones = new CustomAdapterVotaciones(VotacionesActivity.this, equipos);
        recyclerView.setAdapter(CustomAdapterVotaciones);

    }

}
