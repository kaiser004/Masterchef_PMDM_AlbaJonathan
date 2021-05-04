package com.politecnico.masterchef_pmdm_albajonathan;

// @Author - Alba Orbegozo / Jonathan Lopez - PMDM Masterchef - CI Politécnico Estella

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.politecnico.materchef_pmdm_albajonathan.R;

public class EventoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);
        getSupportActionBar().hide();

    }

}
