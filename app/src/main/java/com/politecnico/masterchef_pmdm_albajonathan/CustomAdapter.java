package com.politecnico.masterchef_pmdm_albajonathan;

// @Author - Alba Orbegozo / Jonathan Lopez - PMDM Masterchef - CI Politécnico Estella

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.politecnico.materchef_pmdm_albajonathan.R;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    ArrayList<String> nombre;
    ArrayList<String> fecha;
    ArrayList<String> hora;
    Context context;

    public CustomAdapter(Context context, ArrayList<String> nombre, ArrayList<String> fecha, ArrayList<String> hora) {
        this.context = context;
        this.nombre = nombre;
        this.fecha = fecha;
        this.hora = hora;
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
        holder.nombre.setText(nombre.get(position));
        holder.fecha.setText(fecha.get(position));
        holder.hora.setText(hora.get(position));
    }

    @Override
    public int getItemCount() {
        return nombre.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, fecha, hora;// init the item view's

        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            nombre = (TextView) itemView.findViewById(R.id.nombre);
            fecha = (TextView) itemView.findViewById(R.id.fecha);
            hora = (TextView) itemView.findViewById(R.id.hora);
        }
    }
}
