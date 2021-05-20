package com.politecnico.masterchef_pmdm_albajonathan;

// @Author - Alba Orbegozo / Jonathan Lopez - PMDM Masterchef - CI Politécnico Estella

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.politecnico.masterchef_pmdm_albajonathan.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Locale;

public class EventosActivity extends AppCompatActivity  {

    Toast toast;
    Button botonEventosActivos;
    RecyclerView recyclerView;
    ArrayList<String> nombres = new ArrayList<>();
    ArrayList<String> fechas = new ArrayList<>();
    ArrayList<String> horas = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<>();

    Button eventosActivos;
    Button eventosPasados;

    public static String estadoEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos);
        getSupportActionBar().hide();

        botonEventosActivos = findViewById(R.id.btnEventosActivos);

        //Comprobamos el idioma y cambiamos el color de los botones
        if (Locale.getDefault().getLanguage() == "es") {
            botonEventosActivos.setBackgroundResource(R.drawable.boton_redondo_castellano);
        } else if (Locale.getDefault().getLanguage() == "en")  {
            botonEventosActivos.setBackgroundResource(R.drawable.boton_redondo_ingles);
        } else {
            botonEventosActivos.setBackgroundResource(R.drawable.boton_redondo_euskera);
        }

        estadoEvento = "En curso";

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        //buscarRegistro("http://10.0.2.2/masterchef/eventos_buscar_eventos.php?estado=" + "En curso");
        buscarRegistro("https://politecnico-estella.ddns.net:10443/masterchef_03/masterchef/eventos_buscar_eventos.php?estado=" + "En curso");

        eventosActivos = (Button) findViewById(R.id.btnEventosActivos);
        eventosActivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //buscarRegistro("http://10.0.2.2/masterchef/eventos_buscar_eventos.php?estado=" + "En curso");
                buscarRegistro("https://politecnico-estella.ddns.net:10443/masterchef_03/masterchef/eventos_buscar_eventos.php?estado=" + "En curso");
                estadoEvento = "En curso";
                nombres.clear();
                fechas.clear();
                horas.clear();
                ids.clear();
            }
        });

        eventosPasados = (Button) findViewById(R.id.btnEventosPasados);
        eventosPasados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //buscarRegistro("http://10.0.2.2/masterchef/eventos_buscar_eventos.php?estado=" + "Finalizado");
                buscarRegistro("https://politecnico-estella.ddns.net:10443/masterchef_03/masterchef/eventos_buscar_eventos.php?estado=" + "Finalizado");
                estadoEvento = "Finalizado";
                nombres.clear();
                fechas.clear();
                horas.clear();
                ids.clear();
            }
        });

    }

    private void buscarRegistro(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        nombres.add(jsonObject.getString("Nombre_evento"));
                        fechas.add(jsonObject.getString("Fecha"));
                        horas.add(jsonObject.getString("hora"));
                        ids.add(jsonObject.getString("ID_evento"));
                    } catch (JSONException ex) {
                        toast = Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }

                CustomAdapter customAdapter = new CustomAdapter(EventosActivity.this, nombres, fechas, horas, ids);
                recyclerView.setAdapter(customAdapter);

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