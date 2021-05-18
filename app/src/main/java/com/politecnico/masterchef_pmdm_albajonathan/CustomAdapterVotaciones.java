package com.politecnico.masterchef_pmdm_albajonathan;

// @Author - Alba Orbegozo / Jonathan Lopez - PMDM Masterchef - CI Politecnico Estella

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomAdapterVotaciones extends RecyclerView.Adapter<CustomAdapterVotaciones.MyViewHolder> {
    Map<String , Boolean> comprobar = new HashMap<String , Boolean>();
    Context context;
    ArrayList<String> equipos;
    static boolean listo = false;
    Boolean termminado = VotacionesActivity.listo;

    //SQLite
    SQLiteDatabase db;
    VotacionesDbHelper dbHelper;

    public CustomAdapterVotaciones(Context context, ArrayList<String> equipo) {
        this.context = context;
        this.equipos = equipo;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        termminado = false;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout_votaciones, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
        holder.equipo.setText(equipos.get(position));

        //Maximos y Minimos de los NumberPicker
        holder.presentacion.setMinValue(0);
        holder.presentacion.setMaxValue(100);
        holder.servicio.setMinValue(0);
        holder.servicio.setMaxValue(100);
        holder.sabor.setMinValue(0);
        holder.sabor.setMaxValue(100);
        holder.imagen.setMinValue(0);
        holder.imagen.setMaxValue(100);
        holder.triptico.setMinValue(0);
        holder.triptico.setMaxValue(100);

        //CUANDO SE PULSA EN GUARDAR VOTACION, SE ALMACENAN LOS DATOS EN SQLITE
        holder.botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (VotacionesActivity.listo){
                    Toast toast = Toast.makeText(context.getApplicationContext(), "ERROR DE CONEXIÓN", Toast.LENGTH_LONG);
                    toast.show();
                    holder.botonGuardar.setEnabled(false);
                }else{
                    if (comprobar.containsKey(holder.equipo.getText())){
                        comprobar.remove(holder.equipo.getText());
                        bd();
                        String eq = (String) holder.equipo.getText();
                        db.delete(Contract.Votaciones.TABLE_NAME,"equipo=?",new String[]{eq});
                        //db.execSQL("DELETE FROM " + Contract.Votaciones.TABLE_NAME + " WHERE equipo =" + );
                        db.close();
                    }

                    if (!holder.botonGuardar.getText().equals("Editar votacion")){
                        comprobar.put((String) holder.equipo.getText(), true);

                        guardarVotacion((String) holder.equipo.getText(), String.valueOf(holder.presentacion.getValue()),
                                String.valueOf(holder.servicio.getValue()), String.valueOf(holder.sabor.getValue()),
                                String.valueOf(holder.imagen.getValue()), String.valueOf(holder.triptico.getValue()));
                    }
                    //Comprobar con una variable booleana que estan seleccionadas todas
                    if (comprobar.size() == equipos.size()){
                        listo = true;
                    }else{
                        listo = false;
                    }
                    if (holder.botonGuardar.getText().equals("Guardar votacion")){
                        holder.botonGuardar.setText("Editar votacion");
                        holder.presentacion.setEnabled(false);
                        holder.presentacion.setBackgroundColor(Color.GRAY);
                        holder.servicio.setEnabled(false);
                        holder.servicio.setBackgroundColor(Color.GRAY);
                        holder.sabor.setEnabled(false);
                        holder.sabor.setBackgroundColor(Color.GRAY);
                        holder.imagen.setEnabled(false);
                        holder.imagen.setBackgroundColor(Color.GRAY);
                        holder.triptico.setEnabled(false);
                        holder.triptico.setBackgroundColor(Color.GRAY);
                    }else{
                        holder.botonGuardar.setText("Guardar votacion");
                        holder.presentacion.setEnabled(true);
                        holder.presentacion.setBackgroundColor(Color.WHITE);
                        holder.servicio.setEnabled(true);
                        holder.servicio.setBackgroundColor(Color.WHITE);
                        holder.sabor.setEnabled(true);
                        holder.sabor.setBackgroundColor(Color.WHITE);
                        holder.imagen.setEnabled(true);
                        holder.imagen.setBackgroundColor(Color.WHITE);
                        holder.triptico.setEnabled(true);
                        holder.triptico.setBackgroundColor(Color.WHITE);
                    }
                }
            }
        });

    }



    @Override
    public int getItemCount() {
        return equipos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView equipo;// init the item view's
        Button botonGuardar;
        NumberPicker presentacion, servicio, sabor, imagen, triptico;

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            equipo = (TextView) itemView.findViewById(R.id.idEquipo);
            presentacion = itemView.findViewById(R.id.presentacionNP);
            servicio = itemView.findViewById(R.id.servicioNP);
            sabor = itemView.findViewById(R.id.saborNP);
            imagen = itemView.findViewById(R.id.imagenNP);
            triptico = itemView.findViewById(R.id.tripticoNP);
            botonGuardar = itemView.findViewById(R.id.botonGuardar);
        }
    }

    private void bd() {
        //Instanciamos la clase VotacionDbHelper
        dbHelper = new VotacionesDbHelper(context);

        // Gets the data repository in write mode
        db = dbHelper.getWritableDatabase();
    }

    private void guardarVotacion(String equipo, String presentacion, String servicio, String sabor, String imagen, String triptico) {
        bd();

        //Metemos los valores a la BD
        ContentValues values = new ContentValues();
        values.put(Contract.Votaciones.COLUMN_NAME_PRESENTACION, presentacion);
        values.put(Contract.Votaciones.COLUMN_NAME_SERVICIO, servicio);
        values.put(Contract.Votaciones.COLUMN_NAME_SABOR, sabor);
        values.put(Contract.Votaciones.COLUMN_NAME_IMAGEN, imagen);
        values.put(Contract.Votaciones.COLUMN_NAME_TRIPTICO, triptico);
        values.put(Contract.Votaciones.COLUMN_NAME_JUEZ, LoginActivity.idJuez);
        values.put(Contract.Votaciones.COLUMN_NAME_EVENTO, CustomAdapter.idEvento);
        values.put(Contract.Votaciones.COLUMN_NAME_EQUIPO, equipo);

        // Insert the new row, returning the primary key value of the new row
        db.insert(Contract.Votaciones.TABLE_NAME, null, values);
        values.clear();

        db.close();
    }
}