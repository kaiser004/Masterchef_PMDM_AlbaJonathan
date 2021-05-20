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
import java.util.Locale;
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
        buscarRegistrosId("https://politecnico-estella.ddns.net:10443/masterchef_03/masterchef/evento_datos_evento.php?id=" + CustomAdapter.idEvento);

        //Recogemos la información del juez
        String URL = "https://politecnico-estella.ddns.net:10443/masterchef_03/masterchef/evento_datos_juez.php?juez=" + LoginActivity.idJuez + "&evento=" + CustomAdapter.idEvento;
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
                    cancelarJuez("https://politecnico-estella.ddns.net:10443/masterchef_03/masterchef/evento_cancelar_juez.php");
                } else if (estado.equals("Sin solicitar")) {
                    solicitarJuez("https://politecnico-estella.ddns.net:10443/masterchef_03/masterchef/evento_solicitar_juez.php");
                }
            }
        });
    }

    //Se comprueba el estado del juez
    public void comprobaciones() {

        //Si el evento está finalizado
        if (EventosActivity.estadoEvento.equals("Finalizado")) {
            //Deshabilitamos el botón Juez
            deshabilitarBotonJuez();

            //Establecemos el mensaje informativo y cambiamos el texto del botón Votaciones
            if (Locale.getDefault().getLanguage() == "es") {
                botonVotacion.setText("VER VOTACION");
                textParticipacion.setText("VOTACIÓN FINALIZADA");
                botonVotacion.setBackgroundResource(R.drawable.boton_redondo_castellano);
            } else if (Locale.getDefault().getLanguage() == "en")  {
                botonVotacion.setText("SEE VOTING");
                textParticipacion.setText("VOTING COMPLETED");
                botonVotacion.setBackgroundResource(R.drawable.boton_redondo_ingles);
            } else {
                botonVotacion.setText("IKUS BOTAZIOA");
                textParticipacion.setText("BOTAZIOA OSATUTA");
                botonVotacion.setBackgroundResource(R.drawable.boton_redondo_euskera);
            }

        //Si el evento está "En curso"
        } else {
            switch (estado) {
                case "Admitido":
                    //Cambiamos el color al botón juez
                    botonJuez.setBackgroundResource(R.drawable.boton_redondo_rojo);

                    //Establecemos el mensaje informativo y cambiamos el texto del botón Juez
                    if (Locale.getDefault().getLanguage() == "es") {
                        textParticipacion.setText("Participación como Juez: ACTIVA");
                        botonJuez.setText("CANCELAR JUEZ");
                        botonVotacion.setBackgroundResource(R.drawable.boton_redondo_castellano);
                    } else if (Locale.getDefault().getLanguage() == "en")  {
                        textParticipacion.setText("Participation as Judge: ACTIVE");
                        botonJuez.setText("CANCEL JUDGE");
                        botonVotacion.setBackgroundResource(R.drawable.boton_redondo_ingles);
                    } else {
                        textParticipacion.setText("Parte hartzea epaile gisa: AKTIBOA");
                        botonJuez.setText("UTZI EPAILEA");
                        botonVotacion.setBackgroundResource(R.drawable.boton_redondo_euskera);
                    }
                    break;
                case "Denegado":
                    //Deshabilitamos los botones
                    deshabilitarBotonJuez();
                    deshabilitarBotonVotar();

                    //Establecemos el mensaje informativo
                    if (Locale.getDefault().getLanguage() == "es") {
                        textParticipacion.setText("Participación como Juez: DENEGADA");
                    } else if (Locale.getDefault().getLanguage() == "en")  {
                        textParticipacion.setText("Participation as Judge: DENIED");
                    } else {
                        textParticipacion.setText("Parte hartzea epaile gisa: UKATUTA");
                    }
                    break;
                case "En espera":
                    //Deshabilitamos el botón para votar
                    deshabilitarBotonVotar();
                    //Cambiamos el color del botón Juez
                    botonJuez.setBackgroundResource(R.drawable.boton_redondo_rojo);

                    //Establecemos el mensaje informativo y cambiamos el texto del botón Juez
                    if (Locale.getDefault().getLanguage() == "es") {
                        textParticipacion.setText("Participación como Juez: ESPERANDO CONFIRMACIÓN");
                        botonJuez.setText("CANCELAR JUEZ");
                    } else if (Locale.getDefault().getLanguage() == "en")  {
                        textParticipacion.setText("Participation as Judge: WAITING CONFIRMATION");
                        botonJuez.setText("CANCEL JUDGE");
                    } else {
                        textParticipacion.setText("Parte hartzea epaile gisa: BAIEZTAPENAREN ZAIN");
                        botonJuez.setText("UTZI EPAILEA");
                    }
                    break;
                case "Sin solicitar":
                    //Deshabilitamos el botón votar y cambiamos el color al botón Juez
                    deshabilitarBotonVotar();
                    botonJuez.setBackgroundResource(R.drawable.boton_redondo_verde);

                    //Establecemos el mensaje informativo y cambiamos el texto del botón Juez
                    if (Locale.getDefault().getLanguage() == "es") {
                        textParticipacion.setText("Participación como Juez: SIN SOLICITAR");
                        botonJuez.setText("SOLICITAR JUEZ");
                    } else if (Locale.getDefault().getLanguage() == "en")  {
                        textParticipacion.setText("Participation as Judge: NOT REQUESTED");
                        botonJuez.setText("REQUEST JUDGE");
                    } else {
                        textParticipacion.setText("Parte hartzea epaile gisa: EZ DA ESKATU");
                        botonJuez.setText("EPAILEA ESKATU");
                    }
                    break;
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
                        edtnombreEvento.setText(jsonObject.getString("Nombre_evento"));
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
                        textParticipacion.setText(jsonObject.getString("Solicitud"));
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