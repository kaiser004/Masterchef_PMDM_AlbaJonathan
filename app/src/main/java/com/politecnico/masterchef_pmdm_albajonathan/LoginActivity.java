package com.politecnico.masterchef_pmdm_albajonathan;

// @Author - Alba Orbegozo / Jonathan Lopez - PMDM Masterchef - CI Politécnico Estella

//Imports
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    //Variables
    Toast toast;
    String correo, clave;

    EditText editCorreo, editClave;
    Button botonLogin, botonRegistro;

    //Variable global con el ID del Juez logeado
    public static String idJuez;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        new NukeSSLCerts().nuke();

        editClave = findViewById(R.id.editClave);
        editCorreo = findViewById(R.id.editCorreo);
        botonLogin = findViewById(R.id.botonVotar);
        botonRegistro = findViewById(R.id.botonRegistro);

        //Botón Registro, te lleva a la pantalla de registro
        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        //Botón Login, comprueba los campos Correo y Contraseña y accede a la BD para validar al juez
        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correo = editCorreo.getText().toString();
                clave = editClave.getText().toString();

                //Si contienen texto, valida al juez y lo identifica en una variable global
                if (!correo.isEmpty() & !clave.isEmpty()) {
                    //validarCorreo("http://10.0.2.2/masterchef/login_validar_correo.php");
                    validarCorreo("https://politecnico-estella.ddns.net:10443/masterchef_03/masterchef/login_validar_correo.php");
                //Si están vacíos avisa al usuario con un Toast
                } else {
                    toast = Toast.makeText(LoginActivity.this, "Introduzca los datos de acceso por favor", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    //Accede a la BD y comprueba si el juez está validado
    private void validarCorreo(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Si la consulta no devuelve nada, significa que el juez está validado y se pasa al ActivityEventos
                if (!response.isEmpty()) {
                    //identificarJuez("http://10.0.2.2/masterchef/login_identificar_juez.php?correo=" + correo);
                    identificarJuez("https://politecnico-estella.ddns.net:10443/masterchef_03/masterchef/login_identificar_juez.php?correo=" + correo);
                    Intent i = new Intent(getApplicationContext(), EventosActivity.class);
                    startActivity(i);
                //Si nos devuelve algo, avisamos al usuario de que no está validado en la BD
                } else {
                    toast = Toast.makeText(LoginActivity.this, "Juez no validado", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        //En el caso de que haya algún error se muestran los mensajes descriptivos
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "No podemos conectarnos a la red, comprueba la conexión";
                } else if (error instanceof ServerError) {
                    message = "Imposible conectarse con el servidor";
                } else if (error instanceof AuthFailureError) {
                    message = "Error de autenticación, comprueba la conexión";
                } else if (error instanceof ParseError) {
                    message = "Error de parseo. Intenta de nuevo un poco más tarde";
                } else if (error instanceof NoConnectionError) {
                    message = "Imposible conectarse a Internet";
                } else if (error instanceof TimeoutError) {
                    message = "Tiempo de espera agotado, comprueba la conexión a Internet";
                }
                toast = Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG);
                toast.show();
            }
        }) {
            //Establecemos los datos para la consulta .php
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("correo", editCorreo.getText().toString());
                param.put("clave", editClave.getText().toString());
                return param;
            }
        };
        //Realizamos la consulta
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //Consulta a la BD el juez registrado y lo guarda en la variable global
    private void identificarJuez(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        //Recogemos la ID del Juez
                        jsonObject = response.getJSONObject(i);
                        idJuez = jsonObject.getString("ID_juez");
                    } catch (JSONException ex) {
                        toast = Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        //En el caso de que haya algún error se muestran los mensajes descriptivos
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                toast = Toast.makeText(getApplicationContext(), "JUEZ NO ALMACENADO", Toast.LENGTH_LONG);
                toast.show();
            }
        });
        //Realizamos la consulta
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

}
