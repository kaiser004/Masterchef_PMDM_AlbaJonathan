package com.politecnico.masterchef_pmdm_albajonathan;

// @Author - Alba Orbegozo / Jonathan Lopez - PMDM Masterchef - CI Politécnico Estella

//Imports
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.politecnico.masterchef_pmdm_albajonathan.R;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    //Variables
    Toast toast;
    Button registrarse;
    String Nombre , Apellidos , Departamento , Intolerancia , Correo , Clave;
    EditText edtNombre , edtApellidos , edtDepartamento , edtIntolerancia , edtCorreo , edtClave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        getSupportActionBar().hide();

        edtNombre = findViewById(R.id.edtNombre);
        edtApellidos = findViewById(R.id.edtApellidos);
        edtDepartamento = findViewById(R.id.edtDepartamento);
        edtIntolerancia = findViewById(R.id.edtIntolerancia);
        edtCorreo = findViewById(R.id.edtCorreo);
        edtClave = findViewById(R.id.edtClave);

        ConstraintLayout fondo = findViewById(R.id.fondo);

        //Comprobamos el idioma y cambiamos el color de los botones
        if (Locale.getDefault().getLanguage() == "es") {
            fondo.setBackgroundColor(Color.parseColor("#083C3C"));
        } else if (Locale.getDefault().getLanguage() == "en")  {
            fondo.setBackgroundColor(Color.parseColor("#253165"));
        } else {
            fondo.setBackgroundColor(Color.parseColor("#5C1212"));
        }

        registrarse = (Button) findViewById(R.id.btnRegistro);
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Nombre = edtNombre.getText().toString();
                Apellidos = edtApellidos.getText().toString();
                Departamento = edtDepartamento.getText().toString();
                Intolerancia = edtIntolerancia.getText().toString();
                Correo = edtCorreo.getText().toString();
                Clave = edtClave.getText().toString();

                //Si ningún campo está vacío, se inserta el Juez en la BD y se vacían los campos
                if (!Nombre.isEmpty() && !Apellidos.isEmpty() && !Departamento.isEmpty() && !Intolerancia.isEmpty() && !Correo.isEmpty() && !Clave.isEmpty()) {
                    añadirRegistro("https://politecnico-estella.ddns.net:10443/masterchef_03/masterchef/registro_registrar_juez.php");
                    edtNombre.setText("");
                    edtApellidos.setText("");
                    edtDepartamento.setText("");
                    edtIntolerancia.setText("");
                    edtCorreo.setText("");
                    edtClave.setText("");
                    Intent intent = new Intent(RegistroActivity.this , LoginActivity.class);
                    startActivity(intent);
                //Si algún campo está vacío se avisa al usuario
                } else {
                    toast = Toast.makeText(RegistroActivity.this, "No se permiten campos vacios", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    //Accede a la BD y añade el Juez
    private void añadirRegistro(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Se le comunica al usuario que el Juez se ha registrado correctamente
                Toast.makeText(RegistroActivity.this, "Juez registrado con éxito", Toast.LENGTH_SHORT).show();
            }
        //En el caso de que haya algún error se muestran los mensajes descriptivos
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            //Establecemos los datos para la consulta .php
            protected Map<String , String> getParams() throws AuthFailureError{
                Map<String , String> param = new HashMap<String , String>();
                param.put("Nombre" , Nombre);
                param.put("Apellidos" , Apellidos);
                param.put("Departamento" , Departamento);
                param.put("Intolerancia" , Intolerancia);
                param.put("Correo" , Correo);
                param.put("Clave" , Clave);
                return param;
            }
        };
        //Realizamos la consulta
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
