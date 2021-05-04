package com.politecnico.masterchef_pmdm_albajonathan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.politecnico.materchef_pmdm_albajonathan.R;

import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity {

    Toast toast;
    Button registrarse;
    EditText edtNombre , edtApellidos , edtDepartamento , edtIntolerancia , edtCorreo , edtClave;
    String Nombre , Apellidos , Departamento , Intolerancia , Correo , Clave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        edtNombre = findViewById(R.id.edtNombre);
        edtApellidos = findViewById(R.id.edtApellidos);
        edtDepartamento = findViewById(R.id.edtDepartamento);
        edtIntolerancia = findViewById(R.id.edtIntolerancia);
        edtCorreo = findViewById(R.id.edtCorreo);
        edtClave = findViewById(R.id.edtClave);

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

                if (!Nombre.isEmpty() && !Apellidos.isEmpty() && !Departamento.isEmpty() && !Intolerancia.isEmpty() && !Correo.isEmpty() && !Clave.isEmpty()){
                    añadirRegistro("http://10.0.2.2/masterchef/insertar.php");
                    edtNombre.setText("");
                    edtApellidos.setText("");
                    edtDepartamento.setText("");
                    edtIntolerancia.setText("");
                    edtCorreo.setText("");
                    edtClave.setText("");
                    Intent intent = new Intent(RegistroActivity.this , LoginActivity.class);
                    startActivity(intent);
                }else{
                    toast = Toast.makeText(RegistroActivity.this, "No se permiten campos vacios", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });
    }

    private void añadirRegistro(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(RegistroActivity.this, "Operación exitosa", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
