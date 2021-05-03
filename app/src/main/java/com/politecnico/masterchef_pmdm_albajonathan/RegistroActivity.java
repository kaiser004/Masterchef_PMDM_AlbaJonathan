package com.politecnico.masterchef_pmdm_albajonathan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.politecnico.materchef_pmdm_albajonathan.R;

public class RegistroActivity extends AppCompatActivity {

    Button registrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        registrarse = (Button) findViewById(R.id.btnRegistro);
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistroActivity.this , LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}
