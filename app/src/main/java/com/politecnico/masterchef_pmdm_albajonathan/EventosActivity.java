package com.politecnico.masterchef_pmdm_albajonathan;

// @Author - Alba Orbegozo / Jonathan Lopez - PMDM Masterchef - CI Politécnico Estella

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.politecnico.materchef_pmdm_albajonathan.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class EventosActivity extends AppCompatActivity  {

    Toast toast;
    TabLayout tabLayout;
    ViewPager viewPager;
    TabItem eventosActivos, eventosPasados;

    ArrayList<String> nombre = new ArrayList<>();
    ArrayList<String> fecha = new ArrayList<>();
    ArrayList<String> hora = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);
        getSupportActionBar().hide();

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewpager);

        eventosActivos = findViewById(R.id.eventosActivos);
        eventosPasados = findViewById(R.id.eventosPasados);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);


        /*
        eventosActivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarRegistro("http://10.0.2.2/masterchef/buscar.php" + "En curso");
            }
        });

        eventosPasados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarRegistro("http://10.0.2.2/masterchef/buscar.php" + "Finalizado");
            }
        });
         */


        //buscarRegistro("http://10.0.2.2/masterchef/buscar.php?estado=" + "Finalizado");

        CustomAdapter customAdapter = new CustomAdapter(EventosActivity.this, nombre, fecha, hora);
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView

    }

    private void buscarRegistro(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        nombre.add(jsonObject.getString("Nombre_evento"));
                        fecha.add(jsonObject.getString("Fecha"));
                        hora.add(jsonObject.getString("hora"));
                    } catch (JSONException ex) {
                        toast = Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    toast = Toast.makeText(getApplicationContext(), "ERROR DE CONEXIÓN", Toast.LENGTH_LONG);
                    toast.show();
                }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

}
