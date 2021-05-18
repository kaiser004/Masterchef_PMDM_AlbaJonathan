package com.politecnico.masterchef_pmdm_albajonathan;

// @Author - Alba Orbegozo / Jonathan Lopez - PMDM Masterchef - CI Politecnico Estella

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CustomAdapterVotacionesPasadas extends RecyclerView.Adapter<CustomAdapterVotacionesPasadas.MyViewHolder> {
    Context context;
    ArrayList<String> equipos, presentaciones, servicios, sabores, imagenes, tripticos;

    public CustomAdapterVotacionesPasadas(Context context, ArrayList<String> equipo, ArrayList<String> presentacion,
                                          ArrayList<String> servicio, ArrayList<String> sabor, ArrayList<String> imagen,
                                          ArrayList<String> triptico) {
        this.context = context;
        this.equipos = equipo;
        this.presentaciones = presentacion;
        this.servicios = servicio;
        this.sabores = sabor;
        this.imagenes = imagen;
        this.tripticos = triptico;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout_votaciones_pasadas, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
        holder.equipo.setText(equipos.get(position));
        holder.presentacion.setText(presentaciones.get(position));
        holder.servicio.setText(servicios.get(position));
        holder.sabor.setText(sabores.get(position));
        holder.imagen.setText(imagenes.get(position));
        holder.triptico.setText(tripticos.get(position));

    }

    @Override
    public int getItemCount() {
        return equipos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView equipo, presentacion, servicio, sabor, imagen, triptico;// init the item view's

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            equipo = (TextView) itemView.findViewById(R.id.idEquipo);
            presentacion = (TextView) itemView.findViewById(R.id.txtPresentacion);
            servicio = (TextView) itemView.findViewById(R.id.txtServicio);
            sabor = (TextView) itemView.findViewById(R.id.txtSabor);
            imagen = (TextView) itemView.findViewById(R.id.txtImagen);
            triptico = (TextView) itemView.findViewById(R.id.txtTriptico);
        }
    }
}