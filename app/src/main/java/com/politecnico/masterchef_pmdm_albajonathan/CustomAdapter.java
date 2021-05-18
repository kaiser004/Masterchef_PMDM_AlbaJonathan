package com.politecnico.masterchef_pmdm_albajonathan;

// @Author - Alba Orbegozo / Jonathan Lopez - PMDM Masterchef - CI Politecnico Estella

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    ArrayList<String> nombres;
    ArrayList<String> fechas;
    ArrayList<String> horas;
    ArrayList<String> ids;
    Context context;

    public static String idEvento;

    public CustomAdapter(Context context, ArrayList<String> nombre, ArrayList<String> fecha, ArrayList<String> hora, ArrayList<String> id) {
        this.context = context;
        this.nombres = nombre;
        this.fechas = fecha;
        this.horas = hora;
        this.ids = id;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
        holder.nombre.setText(nombres.get(position));
        holder.fecha.setText(fechas.get(position));
        holder.hora.setText(horas.get(position));

        //CUANDO SE PULSA UN EVENTO, TE LLEVA A LA ACTIVITY EVENTO
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EventoActivity.class);

                idEvento = ids.get(position);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nombres.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, fecha, hora;// init the item view's

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            nombre = (TextView) itemView.findViewById(R.id.idNombre);
            fecha = (TextView) itemView.findViewById(R.id.idFecha);
            hora = (TextView) itemView.findViewById(R.id.idHora);
        }
    }
}