package com.politecnico.masterchef_pmdm_albajonathan;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.politecnico.materchef_pmdm_albajonathan.R;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText editCorreo, editClave;
    Button botonLogin, botonRegistro;
    String correo;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editCorreo = findViewById(R.id.editCorreo);
        editClave = findViewById(R.id.editClave);
        botonLogin = findViewById(R.id.botonLogin);
        botonRegistro = findViewById(R.id.botonRegistro);

        botonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
                startActivity(intent);
            }
        });

        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correo = editCorreo.getText().toString();
                if (!correo.isEmpty()) {
                    validarCorreo("http://10.0.2.2/masterchef/validar_correo.php");
                } else {
                    toast = Toast.makeText(LoginActivity.this, "Introduzca el correo por favor", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    private void validarCorreo(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.isEmpty()) {
                    Intent i = new Intent(getApplicationContext(), EventosActivity.class);
                    startActivity(i);
                } else {
                    toast = Toast.makeText(LoginActivity.this, "Correo no validado", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
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

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("correo", editCorreo.getText().toString());
                param.put("clave", editClave.getText().toString());
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
