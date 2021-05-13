package com.politecnico.masterchef_pmdm_albajonathan;

// @Author - Alba Orbegozo / Jonathan Lopez - PMDM Masterchef - CI Politécnico Estella

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.politecnico.masterchef_pmdm_albajonathan.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EventoActivity extends AppCompatActivity {

    Toast toast;
    String evento, estado;
    int juez;

    Button botonJuez;
    Button botonVotacion;

    TextView edtnombreEvento , edtlugar , edtfecha , edthora , edtdescripcion, textParticipacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);
        getSupportActionBar().hide();

        edtnombreEvento = findViewById(R.id.txtNombreEvento);
        edtlugar = findViewById(R.id.txtLugar);
        edtfecha = findViewById(R.id.txtFecha);
        edthora = findViewById(R.id.txtHora);
        edtdescripcion = findViewById(R.id.txtDescripcion);
        botonVotacion = findViewById(R.id.botonVotacion);
        botonJuez = findViewById(R.id.botonJuez);
        textParticipacion = findViewById(R.id.textParticipacion);

        evento = getIntent().getStringExtra("idE");
        juez = 1;
        buscarRegistrosId("http://10.0.2.2/masterchef/evento.php?id=" + evento);

        String URL = "http://10.0.2.2/masterchef/juez.php?juez=" + juez + "&evento=" + evento;
        comprobarEstado(URL);

        botonVotacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventoActivity.this, VotacionesActivity.class);
                Bundle parametros = new Bundle();
                parametros.putString("idE", evento);
                intent.putExtras(parametros);

                intent.putExtras(parametros);
                startActivity(intent);
            }
        });

        botonJuez.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL = "http://10.0.2.2/masterchef/juez.php?juez=" + juez + "&evento=" + evento;
                comprobarEstado(URL);
                comprobaciones();
            }
        });

    }

    public void comprobaciones() {
        if (estado.equals("Admitido"))  {
            textParticipacion.setText("Participación como Juez: ACTIVA");
            botonJuez.setText("CANCELAR JUEZ");
        } else if (estado.equals("Denegado")) {
            textParticipacion.setText("Participación como Juez: DENEGADA");
            botonJuez.setText("... JUEZ");
        } else if (estado.equals("En espera")) {
            textParticipacion.setText("Participación como Juez: ESPERANDO CONFIRMACIÓN");
            botonJuez.setText("... JUEZ");
        }
    }

    private void buscarRegistrosId(String URL){
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        edtnombreEvento.setText(jsonObject.getString("Nombre_Evento"));
                        edtfecha.setText(jsonObject.getString("Fecha"));
                        edthora.setText(jsonObject.getString("hora"));
                        edtlugar.setText(jsonObject.getString("Lugar"));
                        edtdescripcion.setText(jsonObject.getString("Descripcion"));
                    } catch (JSONException ex) {
                        toast = Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                toast = Toast.makeText(getApplicationContext(), "ERROR DE CONEXIÓN", Toast.LENGTH_LONG);
                toast.show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void comprobarEstado(String URL) {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        textParticipacion.setText(jsonObject.getString("solicitud"));
                        estado = (String) textParticipacion.getText();
                        comprobaciones();
                    } catch (JSONException ex) {
                        toast = Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                toast = Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG);
                toast.show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

}