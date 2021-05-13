package com.politecnico.masterchef_pmdm_albajonathan;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VotacionesActivity extends AppCompatActivity {

    String idR;
    Toast toast;
    Button botonVotar;
    RecyclerView recyclerView;
    ArrayList<String> equipos = new ArrayList<>();

    //SQLite
    SQLiteDatabase db;
    VotacionesDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_votaciones);
        getSupportActionBar().hide();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        //ID Evento
        idR = getIntent().getStringExtra("idE");
        buscarEquipos("http://10.0.2.2/masterchef/equipos.php?id=" + idR);

        botonVotar = findViewById(R.id.botonVotar);
        botonVotar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (true){
                    recogerVotacion();
                }else{
                    toast = Toast.makeText(VotacionesActivity.this, "Por favor, guarda las votaciones antes de enviarlas", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });
    }

    private void buscarEquipos(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        equipos.add(jsonObject.getString("Nombre_equipo"));
                    } catch (JSONException ex) {
                        toast = Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }

                CustomAdapterVotaciones CustomAdapterVotaciones = new CustomAdapterVotaciones(VotacionesActivity.this, equipos);
                recyclerView.setAdapter(CustomAdapterVotaciones);

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

    private void enviarVotaciones(String URL, String[] array) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(VotacionesActivity.this, "Operación exitosa", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String , String> getParams() throws AuthFailureError {
                Map<String , String> param = new HashMap<String , String>();
                param.put("presentacion" , array[0]);
                param.put("servicio" , array[1]);
                param.put("sabor" , array[2]);
                param.put("imagen" , array[3]);
                param.put("triptico" , array[4]);
                param.put("juez" , array[5]);
                param.put("evento" , array[6]);
                param.put("equipo" , array[7]);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void recogerVotacion() {
        //Instanciamos la clase VotacionDbHelper
        dbHelper = new VotacionesDbHelper(VotacionesActivity.this);

        // Gets the data repository in write mode
        db = dbHelper.getWritableDatabase();

        //Leemos los datos
        Cursor cursor = db.query(
                Contract.Votaciones.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()) {
            String presentacion = cursor.getString(0);
            String servicio = cursor.getString(1);
            String sabor = cursor.getString(2);
            String imagen = cursor.getString(3);
            String triptico = cursor.getString(4);
            String juez = cursor.getString(5);
            String evento = cursor.getString(6);
            String equipo = cursor.getString(7);

            String[] array = {presentacion, servicio, sabor, imagen, triptico, juez, evento, equipo};
            enviarVotaciones("http://10.0.2.2/masterchef/votaciones.php", array);
        }

        db.execSQL("DELETE FROM " + Contract.Votaciones.TABLE_NAME);
        cursor.close();
        db.close();
    }
}
