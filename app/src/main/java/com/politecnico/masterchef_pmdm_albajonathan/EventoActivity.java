package com.politecnico.masterchef_pmdm_albajonathan;

// @Author - Alba Orbegozo / Jonathan Lopez - PMDM Masterchef - CI Politécnico Estella

//Imports
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.HashMap;
import java.util.Map;

public class EventoActivity extends AppCompatActivity {

    //Variables
    Toast toast;
    String estado;

    Button botonJuez;
    Button botonVotacion;

    TextView edtnombreEvento , edtlugar , edtfecha , edthora , edtdescripcion, textParticipacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);
        getSupportActionBar().hide();

        //Instanciamos las variables
        edtnombreEvento = findViewById(R.id.txtNombreEvento);
        edtlugar = findViewById(R.id.txtLugar);
        edtfecha = findViewById(R.id.txtFecha);
        edthora = findViewById(R.id.txtHora);
        edtdescripcion = findViewById(R.id.txtDescripcion);
        botonVotacion = findViewById(R.id.botonVotacion);
        botonJuez = findViewById(R.id.botonJuez);
        textParticipacion = findViewById(R.id.textParticipacion);


        //Recogemos los datos del evento
        buscarRegistrosId("http://10.0.2.2/masterchef/evento_datos_evento.php?id=" + CustomAdapter.idEvento);
        //buscarRegistrosId("https://politecnico-estella.ddns.net:10443/masterchef_03/masterchef/evento_datos_evento.php?id=" + CustomAdapter.idEvento);

        //Recogemos la información del juez
        String URL = "http://10.0.2.2/masterchef/evento_datos_juez.php?juez=" + LoginActivity.idJuez + "&evento=" + CustomAdapter.idEvento;
        //String URL = "https://politecnico-estella.ddns.net:10443/masterchef_03/masterchef/evento_datos_juez.php?juez=" + LoginActivity.idJuez + "&evento=" + CustomAdapter.idEvento;
        comprobarEstadoJuez(URL);


        //Cuando se pulsa el botón "Realizar Votación"
        botonVotacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventoActivity.this, VotacionesActivity.class);
                startActivity(intent);
            }
        });

        //Cuando se pulsa el botón "txt Juez"
        botonJuez.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (estado.equals("Admitido") || estado.equals("En espera")) {
                    cancelarJuez("http://10.0.2.2/masterchef/evento_cancelar_juez.php");
                    //cancelarJuez("https://politecnico-estella.ddns.net:10443/masterchef_03/masterchef/evento_cancelar_juez.php");
                } else if (estado.equals("Sin solicitar")) {
                    solicitarJuez("http://10.0.2.2/masterchef/evento_solicitar_juez.php");
                    //solicitarJuez("https://politecnico-estella.ddns.net:10443/masterchef_03/masterchef/evento_solicitar_juez.php");
                }
            }
        });
    }

    //Se comprueba el estado del juez
    public void comprobaciones() {
        if (EventosActivity.estadoEvento.equals("Finalizado")) {
            deshabilitarBotonJuez();

            botonVotacion.setText("VER VOTACION");
            textParticipacion.setText("VOTACIÓN FINALIZADA");
        } else {

            if (estado.equals("Admitido")) {
                //Establecemos el mensaje informativo
                textParticipacion.setText("Participación como Juez: ACTIVA");

                //Cambiamos el texto y color al botón juez
                botonJuez.setText("CANCELAR JUEZ");
                botonJuez.setBackgroundResource(R.drawable.boton_redondo_rojo);
            } else if (estado.equals("Denegado")) {
                //Establecemos el mensaje informativo
                textParticipacion.setText("Participación como Juez: DENEGADA");

                deshabilitarBotonJuez();
                deshabilitarBotonVotar();
            } else if (estado.equals("En espera")) {
                //Establecemos el mensaje informativo
                textParticipacion.setText("Participación como Juez: ESPERANDO CONFIRMACIÓN");

                //Cambiamos el texto y color al botón juez
                botonJuez.setText("CANCELAR JUEZ");
                botonJuez.setBackgroundResource(R.drawable.boton_redondo_rojo);

                deshabilitarBotonVotar();
            } else if (estado.equals("Sin solicitar")) {
                //Establecemos el mensaje informativo
                textParticipacion.setText("Participación como Juez: SIN SOLICITAR");

                //Cambiamos el texto y color al botón juez
                botonJuez.setText("SOLICITAR JUEZ");
                botonJuez.setBackgroundResource(R.drawable.boton_redondo_verde);

                deshabilitarBotonVotar();
            }
        }
    }

    //Deshabilitamos el botón para votar
    private void deshabilitarBotonVotar() {
        botonVotacion.setEnabled(false);
        botonVotacion.setVisibility(View.INVISIBLE);
    }

    //Deshabilitamos el botón juez
    private void deshabilitarBotonJuez() {
        botonJuez.setEnabled(false);
        botonJuez.setVisibility(View.INVISIBLE);
    }

    //Consulta a phpMyAdmin para recuperar los datos del evento
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

    //Consulta a phpMyAdmin para comprobar el estado de la solicitud de juez
    private void comprobarEstadoJuez(String URL) {

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

                if (jsonObject == null) {
                    estado = "Sin solicitar";
                    comprobaciones();
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

    //Accede a la BD y añade la solicitud
    private void solicitarJuez(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                estado = "En espera";
                comprobaciones();
            }
            //En el caso de que haya algún error se muestran los mensajes descriptivos
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            //Establecemos los datos para la consulta .php
            protected Map<String , String> getParams() throws AuthFailureError {
                Map<String , String> param = new HashMap<String , String>();
                param.put("Juez" , LoginActivity.idJuez);
                param.put("Evento" , CustomAdapter.idEvento);
                param.put("Solicitud" , "En espera");
                return param;
            }
        };
        //Realizamos la consulta
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //Accede a la BD y borra la solicitud del juez
    private void cancelarJuez(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                estado = "Sin solicitar";
                comprobaciones();
            }
            //En el caso de que haya algún error se muestran los mensajes descriptivos
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            //Establecemos los datos para la consulta .php
            protected Map<String , String> getParams() throws AuthFailureError {
                Map<String , String> param = new HashMap<String , String>();
                param.put("Juez" , LoginActivity.idJuez);
                param.put("Evento" , CustomAdapter.idEvento);
                return param;
            }
        };
        //Realizamos la consulta
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}